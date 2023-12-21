package com.dfdyz.lcaddon.client.gui.screens;

import com.dfdyz.lcaddon.utils.EntityManager;
import com.dfdyz.lcaddon.utils.MidiTrackUtils;
import com.dfdyz.lcaddon.world.entity.MoonLampEntity;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import org.ywzj.midi.gui.screen.ClavichordScreen;
import org.ywzj.midi.gui.widget.CommonButton;
import org.ywzj.midi.instrument.Instrument;

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
                    linkedLamp = EntityManager.GetNearestInRange(minecraft.level, pos, 5f, MoonLampEntity.class);
                    LinkLamp.setMessage(Component.literal((linkedLamp != null && !linkedLamp.isRemoved()) ? linkedLamp.position().toString() :  "LinkLamp" ));
                });
        this.addRenderableWidget(this.LinkLamp);
    }

}
