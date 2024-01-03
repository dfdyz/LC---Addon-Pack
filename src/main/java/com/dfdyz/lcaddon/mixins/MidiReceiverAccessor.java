package com.dfdyz.lcaddon.mixins;

import org.openjdk.nashorn.internal.objects.annotations.Setter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.ywzj.midi.instrument.receiver.MidiReceiver;

@Mixin(value = MidiReceiver.class, remap = false)
public interface MidiReceiverAccessor {
    @Accessor("isPlaying")
    void setPlayingState(boolean p);
}
