package com.dfdyz.lcaddon.client.audio.instruments;

import com.dfdyz.lcaddon.client.audio.midi.PatchedPianoMidiReceiver;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.ywzj.midi.instrument.Instrument;
import org.ywzj.midi.instrument.receiver.MidiReceiver;

public class PatchedPiano extends MultiSamplingInstrument {
    public PatchedPiano(String name, int[] sampling) {
        super(name, sampling,false, false);
    }

    @Override
    public MidiReceiver receiver(Player player, Vec3 vec3) {
        return new PatchedPianoMidiReceiver(this, player, vec3);
    }
}
