package com.dfdyz.lcaddon.client.gui.screens;

import com.dfdyz.lcaddon.client.audio.instruments.AnimatedPiano;
import com.dfdyz.lcaddon.mixins.ClavichordScreenAccessor;
import com.dfdyz.lcaddon.utils.EntityManager;
import com.dfdyz.lcaddon.utils.MidiTrackUtils;
import com.dfdyz.lcaddon.world.entity.MoonLampEntity;
import com.dfdyz.lcaddon.world.tileentity.AnimatedPianoTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.ywzj.midi.audio.NotePlayer;
import org.ywzj.midi.gui.screen.ClavichordScreen;
import org.ywzj.midi.gui.widget.CommonButton;
import org.ywzj.midi.instrument.Instrument;
import org.ywzj.midi.pose.action.PianoPlayPose;
import org.ywzj.midi.util.MidiUtils;

import java.util.Collections;
import java.util.UUID;

public class PatchedPianoScreen extends ClavichordScreen {
    protected Button LinkLamp;
    protected BlockPos bpos;
    public MoonLampEntity linkedLamp;

    public PatchedPianoScreen(Instrument instrument, BlockPos pos, Component titleIn, String keyStart, String keyEnd) {
        super(instrument, pos, titleIn, keyStart, keyEnd);
        bpos = pos;
    }

    public void consumeLampMsg(MidiTrackUtils.LampColMsg msg){
        if(linkedLamp != null && !linkedLamp.isRemoved())
            switch (msg.part){
                case R:
                    linkedLamp.setColorR(msg.partVal);
                    break;
                case G:
                    linkedLamp.setColorG(msg.partVal);
                    break;
                case B:
                    linkedLamp.setColorB(msg.partVal);
                    break;
                case A:
                    linkedLamp.setColorA(msg.partVal);
                    break;
                default:
                    break;
            }
    }

    @Override
    protected void init() {
        super.init();
        this.LinkLamp = new CommonButton(this.width / 2 - 190, this.height / 2 + 60, 180, 20,
                Component.literal((linkedLamp != null && !linkedLamp.isRemoved()) ? linkedLamp.position().toString() :  "LinkLamp" ),
                (button) ->
                {
                    linkedLamp = EntityManager.GetNearestInRange(minecraft.level, pos, 3f, MoonLampEntity.class);
                    LinkLamp.setMessage(Component.literal((linkedLamp != null && !linkedLamp.isRemoved()) ? linkedLamp.position().toString() :  "LinkLamp" ));
                });
        this.addRenderableWidget(this.LinkLamp);
    }

    @Override
    protected void playNoteOnce(String notation) {
        UUID uuid = UUID.randomUUID();
        int note = MidiUtils.notationToNote(notation);
        NotePlayer.playNote(uuid, this.pos, this.instrument, note, this.velocitySlider.value, 0);
        PianoPlayPose.handle(Minecraft.getInstance().player, Collections.singletonList(note));

        BlockEntity te = Minecraft.getInstance().player.level().getBlockEntity(bpos);

        if(te != null && !te.isRemoved() && te instanceof AnimatedPianoTileEntity ate){
            (new Thread(() -> {
                ate.setKeyClient(note, true);
                try {
                    Thread.sleep(100L);
                    if(ate != null && !ate.isRemoved()){
                        ate.setKeyClient(note, false);
                    }
                    Thread.sleep(700L);
                    if (((ClavichordScreenAccessor)this).getPedal()) {
                        Thread.sleep(9200L);
                    }

                } catch (Exception var3) {
                }

                NotePlayer.stopNote(uuid);
            })).start();
        }
        else {
            (new Thread(() -> {
                try {
                    if (((ClavichordScreenAccessor)this).getPedal()) {
                        Thread.sleep(10000L);
                    } else {
                        Thread.sleep(800L);
                    }
                } catch (Exception var3) {
                }

                NotePlayer.stopNote(uuid);
            })).start();
        }
    }

}
