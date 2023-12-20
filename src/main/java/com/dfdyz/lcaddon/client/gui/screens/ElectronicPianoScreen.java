package com.dfdyz.lcaddon.client.gui.screens;

import com.dfdyz.lcaddon.LCAddon;
import com.dfdyz.lcaddon.client.audio.midi.PatchedPianoMidiReceiver;
import com.dfdyz.lcaddon.utils.TranslationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import org.ywzj.midi.audio.NotePlayer;
import org.ywzj.midi.gui.screen.ClavichordScreen;
import org.ywzj.midi.gui.widget.CommonButton;
import org.ywzj.midi.instrument.Instrument;
import org.ywzj.midi.pose.action.PianoPlayPose;
import org.ywzj.midi.util.MidiUtils;

import java.util.Collections;
import java.util.UUID;

public class ElectronicPianoScreen extends ClavichordScreen {
    private Button ChangeInstrumentButton;

    public ElectronicPianoScreen(Instrument instrument, BlockPos pos, Component titleIn, String keyStart, String keyEnd) {
        super(instrument, pos, titleIn, keyStart, keyEnd);
    }

    @Override
    protected void init() {
        super.init();
        this.ChangeInstrumentButton = new CommonButton(this.width / 2 - 190, this.height / 2 + 60, 160, 20,
                Component.literal(instrument.getName()),
                (button) ->
                {
                });
        this.addRenderableWidget(this.ChangeInstrumentButton);
    }
}
