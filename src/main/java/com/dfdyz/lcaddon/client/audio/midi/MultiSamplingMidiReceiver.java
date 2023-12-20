package com.dfdyz.lcaddon.client.audio.midi;

import com.dfdyz.lcaddon.LCAddon;
import com.dfdyz.lcaddon.client.audio.instruments.MultiSamplingInstrument;
import com.dfdyz.lcaddon.utils.ReflectionUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.ywzj.midi.audio.NotePlayer;
import org.ywzj.midi.instrument.Instrument;
import org.ywzj.midi.instrument.receiver.MidiReceiver;
import org.ywzj.midi.pose.PoseManager;
import org.ywzj.midi.pose.action.PianoPlayPose;
import org.ywzj.midi.util.MathUtils;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class MultiSamplingMidiReceiver extends MidiReceiver {
    protected MultiSamplingInstrument mInstrument;
    public MultiSamplingMidiReceiver(MultiSamplingInstrument instrument, Player player, Vec3 pos) {
        super(instrument, player, pos);
        mInstrument = instrument;
    }

    public void SetInstrument(MultiSamplingInstrument instrument){
        mInstrument = instrument;
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
