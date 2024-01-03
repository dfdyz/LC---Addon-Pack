package com.dfdyz.lcaddon.mixins;

import com.dfdyz.lcaddon.LCAddon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.ywzj.midi.util.MidiUtils;

@Mixin(value = MidiUtils.class, remap = false)
public class MixinMidiUtils {
    private static final String[] simpleNotation = new String[]{"a", "as", "b", "c", "cs", "d", "ds", "e", "f", "fs", "g", "gs"};

    @Inject(method = "noteToNotation", at = @At("HEAD"), cancellable = true)
    private static void PatchedNotation(int n, CallbackInfoReturnable<String> cir) {
        if (n >= LCAddon.level_base){
            int level = n / LCAddon.level_base - 1;
            int note = n % LCAddon.level_base;
            int octave = (note + 3) / 12 - 1;
            int noteIndex = (note + 3) % 12;
            String a = simpleNotation[noteIndex] + octave + "_" + level;
            //LCAddon.LOGGER.info("MixinMidi: n:{}, note:{}, octave:{}, noteIndex:{}", new Object[]{n, note, octave, noteIndex});
            cir.setReturnValue(a);
            cir.cancel();
        }
    }

}
