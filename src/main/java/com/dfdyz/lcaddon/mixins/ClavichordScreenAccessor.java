package com.dfdyz.lcaddon.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.ywzj.midi.gui.screen.ClavichordScreen;
import org.ywzj.midi.instrument.receiver.MidiReceiver;

@Mixin(value = ClavichordScreen.class, remap = false)
public interface ClavichordScreenAccessor {
    @Accessor("uiPedal")
    boolean getPedal();
}
