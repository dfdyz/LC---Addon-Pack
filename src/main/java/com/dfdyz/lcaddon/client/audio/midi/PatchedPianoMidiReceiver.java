package com.dfdyz.lcaddon.client.audio.midi;

import com.dfdyz.lcaddon.LCAddon;
import com.dfdyz.lcaddon.client.audio.instruments.MultiSamplingInstrument;
import com.dfdyz.lcaddon.utils.ThreadUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.ywzj.midi.audio.NotePlayer;
import org.ywzj.midi.audio.sound.MidiSound;
import org.ywzj.midi.pose.PoseManager;
import org.ywzj.midi.pose.action.PianoPlayPose;
import org.ywzj.midi.util.MathUtils;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PatchedPianoMidiReceiver extends MultiSamplingMidiReceiver {
    private final HashSet<UUID> inPedalKeys = new HashSet();
    private boolean pedal = false;
    private final List<Integer> posePlayNotes = new ArrayList();
    private long lastTimeStamp;
    public PatchedPianoMidiReceiver(MultiSamplingInstrument instrument, Player player, Vec3 pos) {
        super(instrument, player, pos);
    }

    @Override
    public void send(MidiMessage message, long timeStamp, int delay) {
        if (timeStamp < lastTimeStamp) {
            lastTimeStamp = 0;
        }
        if (message instanceof ShortMessage shortMessage) {
            int command = shortMessage.getCommand();
            if (command == ShortMessage.NOTE_ON) {
                int note = shortMessage.getData1();
                int velocity = shortMessage.getData2();
                if (velocity == 0) {
                    UUID uuid = playedKeys.get(note);
                    if (uuid != null) {
                        if (pedal) {
                            inPedalKeys.add(uuid);
                        } else {
                            NotePlayer.stopNote(uuid);
                        }
                        playedKeys.remove(note);
                    }
                    return;
                }
                playNote(note, velocity, delay);
                posePlayNotes.add(note);
                if (timeStamp - lastTimeStamp > 10 || posePlayNotes.size() > 8) {
                    if (MathUtils.distance(player.getX(), player.getY(), player.getZ(), pos.x, pos.y, pos.z) < 3) {
                        PianoPlayPose.handle(player, posePlayNotes);
                    } else {
                        PoseManager.clearCache(player.getUUID());
                    }
                    posePlayNotes.clear();
                    lastTimeStamp = timeStamp;
                }
            } else if (command == ShortMessage.NOTE_OFF) {
                int note = shortMessage.getData1();
                UUID uuid = playedKeys.get(note);
                if (uuid != null) {
                    if (pedal) {
                        inPedalKeys.add(uuid);
                    } else {
                        NotePlayer.stopNote(uuid);
                    }
                    playedKeys.remove(note);
                }
            } else if (command == ShortMessage.CONTROL_CHANGE) {
                int controller = shortMessage.getData1();
                int value = shortMessage.getData2();
                if (controller == 64) {
                    if (value < 10) {
                        if(pedal){
                            pedal = false;
                            inPedalKeys.forEach(NotePlayer::stopNote);
                            inPedalKeys.clear();
                        }
                    } else if (value > 110) {
                        pedal = true;
                    }
                }
            }
        }
    }
    public void stopPose() {
        PoseManager.clearCache(this.player.getUUID());
    }

}
