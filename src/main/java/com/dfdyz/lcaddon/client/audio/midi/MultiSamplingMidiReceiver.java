package com.dfdyz.lcaddon.client.audio.midi;

import com.dfdyz.lcaddon.LCAddon;
import com.dfdyz.lcaddon.client.audio.instruments.MultiSamplingInstrument;
import com.dfdyz.lcaddon.client.gui.screens.PatchedPianoScreen;
import com.dfdyz.lcaddon.mixins.MidiReceiverAccessor;
import com.dfdyz.lcaddon.utils.MidiTrackUtils;
import com.dfdyz.lcaddon.utils.ReflectionUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.ywzj.midi.audio.NotePlayer;
import org.ywzj.midi.instrument.Instrument;
import org.ywzj.midi.instrument.receiver.MidiReceiver;
import org.ywzj.midi.pose.PoseManager;
import org.ywzj.midi.pose.action.PianoPlayPose;
import org.ywzj.midi.util.MathUtils;

import javax.sound.midi.*;
import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public abstract class MultiSamplingMidiReceiver extends MidiReceiver {
    protected MultiSamplingInstrument mInstrument;
    public MultiSamplingMidiReceiver(MultiSamplingInstrument instrument, Player player, Vec3 pos) {
        super(instrument, player, pos);
        mInstrument = instrument;
    }

    public void SetInstrument(MultiSamplingInstrument instrument){
        mInstrument = instrument;
    }

    @Override
    public void play(File file) {
        new Thread(() -> {
            MultiSamplingMidiReceiver receiver = this;
            try {
                ((MidiReceiverAccessor)receiver).setPlayingState(true);
                Sequence sequence = MidiSystem.getSequence(file);
                int ticksPerBeat = sequence.getResolution();
                double msPerTick = 60000.0 / (80 * ticksPerBeat);
                long previousTimestamp = 0;
                List<MidiEvent> singleTrack = Arrays.stream(sequence.getTracks()).flatMap(track -> {
                    List<MidiEvent> events = new ArrayList<>();
                    String name = MidiTrackUtils.getTrackName(track);
                    if(name.equals("lca_lamp_color")){
                        System.out.println("found lca lamp track");
                        for (int i = 0; i < track.size(); i++) {
                            MidiEvent e = track.get(i);
                            MidiMessage m = e.getMessage();
                            if(m instanceof ShortMessage sm){
                                MidiTrackUtils.LampColMsg lm = MidiTrackUtils.LampColMsg.fromShortMsg(sm);
                                e = new MidiEvent(lm, e.getTick());
                                //System.out.println("  Lamp event at: "+e.getTick());
                            }
                            events.add(e);
                        }
                    }else {
                        for (int i = 0; i < track.size(); i++) {
                            events.add(track.get(i));
                        }
                    }
                    return events.stream();
                }).sorted(Comparator.comparingLong(MidiEvent::getTick)).collect(Collectors.toList());
                for (MidiEvent event : singleTrack) {
                    if (!receiver.isPlaying()) {
                        break;
                    }
                    if (event.getMessage() instanceof MetaMessage metaMessage) {
                        if (metaMessage.getType() == 0x51) {
                            byte[] data = metaMessage.getData();
                            int microsecondsPerBeat = ((data[0] & 0xFF) << 16) | ((data[1] & 0xFF) << 8) | (data[2] & 0xFF);
                            int bpm = Math.round(60000000.0f / microsecondsPerBeat);
                            msPerTick = 60000.0 / (bpm * ticksPerBeat);
                        }
                    }
                    MidiMessage message = event.getMessage();
                    long deltaTime = event.getTick() - previousTimestamp;
                    if (deltaTime > 0) {
                        Thread.sleep((long) (deltaTime * msPerTick));
                    }
                    if(message instanceof MidiTrackUtils.LampColMsg lampColMsg){
                        sendLampMsg(lampColMsg);
                    }
                    else send(message, event.getTick());
                    previousTimestamp = event.getTick();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            } finally {
                ((MidiReceiverAccessor)receiver).setPlayingState(false);
                stopAllKeys();
                stopPose();
                midiInstrumentScreen.callbackPlayButton();
            }
        }).start();
    }

    protected void sendLampMsg(MidiTrackUtils.LampColMsg msg){
        if(midiInstrumentScreen != null && midiInstrumentScreen instanceof PatchedPianoScreen screen){
            screen.consumeLampMsg(msg);
        }
    }

    protected int Sample(int velocity){
        for (int i=1; i<mInstrument.sampling.length; ++i){
            if (velocity >= mInstrument.sampling[i-1] && velocity < mInstrument.sampling[i]){
                return i-1;
            }
        }
        return 0;
    }

    protected int VelocityModifier(int velocity, int level){
        return velocity;
    }

    @Override
    public void playNote(int note, int velocity, int delay) {
        if (this.playedKeys.containsKey(note)) {
            NotePlayer.stopNote(this.playedKeys.get(note));
            this.playedKeys.remove(note);
        }
        int level = Sample(velocity);
        int pNode = note + level * LCAddon.level_base;
        UUID uuid = UUID.randomUUID();
        NotePlayer.playNote(uuid, this.instrument.getPortable() ? this.player.position() : this.pos, this.instrument, pNode, VelocityModifier(velocity, level), delay);
        this.playedKeys.put(note, uuid);
    }

    @Override
    public void stopNote(int note) {
        UUID uuid = this.playedKeys.get(note);
        if (uuid != null) {
            NotePlayer.stopNote(uuid);
            this.playedKeys.remove(note);
        }
    }

}
