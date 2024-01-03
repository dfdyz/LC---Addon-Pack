package com.dfdyz.lcaddon.client.audio.instruments;

import com.dfdyz.lcaddon.client.audio.midi.AnimatedPianoMidiReceiver;
import com.dfdyz.lcaddon.client.audio.midi.PatchedPianoMidiReceiver;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.ywzj.midi.instrument.receiver.MidiReceiver;

public class AnimatedPiano extends PatchedPiano {
    public AnimatedPiano(String name, int[] sampling) {
        super(name, sampling);
    }

    @Override
    public MidiReceiver receiver(Player player, Vec3 vec3) {
        return new AnimatedPianoMidiReceiver(this, player, vec3);
    }
}
