package com.dfdyz.lcaddon.client.audio.midi;

import com.dfdyz.lcaddon.LCAddon;
import com.dfdyz.lcaddon.client.audio.instruments.MultiSamplingInstrument;
import com.dfdyz.lcaddon.utils.ReflectionUtils;
import com.dfdyz.lcaddon.utils.ThreadUtils;
import com.dfdyz.lcaddon.utils.TileEntityManager;
import com.dfdyz.lcaddon.world.tileentity.AnimatedPianoTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import org.ywzj.midi.audio.NotePlayer;
import org.ywzj.midi.audio.sound.MidiSound;
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

public class AnimatedPianoMidiReceiver extends PatchedPianoMidiReceiver{
    private static final ConcurrentHashMap<UUID, Integer> SOUND_DELAYS = ReflectionUtils.GetField(NotePlayer.class, "SOUND_DELAYS");
    protected AnimatedPianoTileEntity tileEntity;

    public AnimatedPianoMidiReceiver(MultiSamplingInstrument instrument, Player player, Vec3 pos) {
        super(instrument, player, pos);
        Level level = player.level();
        if(pos == null){
            Vec3 p = player.position();
            tileEntity = TileEntityManager.GetNearestInRange(level, p, 4, AnimatedPianoTileEntity.class);

            if(tileEntity != null){
                System.out.println("Found Animated Piano At " + tileEntity.getBlockPos());
            }
        }else {
            tileEntity = (AnimatedPianoTileEntity) level.getBlockEntity(new BlockPos((int)pos.x, ((int)pos.y), (int)pos.z));
        }
    }

    private final HashSet<UUID> inPedalKeys = new HashSet();
    private boolean pedal = false;
    private final List<Integer> posePlayNotes = new ArrayList();
    private long lastTimeStamp;

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
                    stopNote(note);
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
                stopNote(note);
            } else if (command == ShortMessage.CONTROL_CHANGE) {
                int controller = shortMessage.getData1();
                int value = shortMessage.getData2();
                if (controller == 64) {
                    if (value < 10) {
                        if(pedal){
                            pedal = false;
                            inPedalKeys.forEach(NotePlayer::stopNote);
                            inPedalKeys.clear();
                            tileEntity.setKeyClient(0, false);
                            /*
                            if (tileEntity != null){
                                tileEntity.ResetAllKeys();
                                playedKeys.forEach((note, uuid)->{
                                    tileEntity.setKey(note, true);
                                });
                            }*/
                        }
                    } else if (value > 110) {
                        pedal = true;
                        tileEntity.setKeyClient(0, true);
                    }
                }
            }
        }
    }

    @Override
    public void playNote(int note, int velocity, int delay) {
        if (this.playedKeys.containsKey(note)) {
            UUID uuid = this.playedKeys.get(note);
            NotePlayer.stopNote(uuid);
            DelayStop(uuid, note);
            this.playedKeys.remove(note);
        }

        int level = Sample(velocity);
        int pNode = note + level * LCAddon.level_base;
        UUID uuid = UUID.randomUUID();
        NotePlayer.playNote(uuid, this.instrument.getPortable() ? this.player.position() : this.pos, this.instrument, pNode, VelocityModifier(velocity, level), delay);
        DelayPlay(note, delay);
        this.playedKeys.put(note, uuid);
    }

    private void DelayPlay(int note, int delay){
        if(tileEntity != null){
            if(delay > 0){
                ThreadUtils.Start(() -> {
                    try {Thread.sleep(delay);}
                    catch (Exception var2) {}
                    if(tileEntity != null) tileEntity.setKeyClient(note, true);
                });
            }
            else {
                tileEntity.setKeyClient(note, true);
            }
        }
    }


    @Override
    public void stopNote(int note) {
        UUID uuid = this.playedKeys.get(note);
        if (uuid != null) {
            if (pedal) {
                inPedalKeys.add(uuid);
            }
            else {
                NotePlayer.stopNote(uuid);
            }
            this.playedKeys.remove(note);
        }
        DelayStop(uuid, note);
    }

    private void DelayStop(UUID uuid, int note){
        if(tileEntity != null){
            ThreadUtils.Start(() -> {
                try {
                    if(uuid != null) Thread.sleep(SOUND_DELAYS.get(uuid));
                } catch (Exception var2) {}
                if(tileEntity != null){
                    tileEntity.setKeyClient(note, false);
                }
            });
        }
    }

    @Override
    public void stopAllKeys() {
        super.stopAllKeys();
        if(tileEntity != null){
            tileEntity.ResetAllKeysClient();
        }
    }
}
