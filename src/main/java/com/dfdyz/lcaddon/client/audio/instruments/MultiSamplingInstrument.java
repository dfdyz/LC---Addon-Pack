package com.dfdyz.lcaddon.client.audio.instruments;

import com.dfdyz.lcaddon.LCAddon;
import com.dfdyz.lcaddon.client.audio.midi.PatchedPianoMidiReceiver;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.ywzj.midi.instrument.Instrument;
import org.ywzj.midi.instrument.receiver.MidiReceiver;

public class MultiSamplingInstrument extends Instrument {
    public final int[] sampling;


    public MultiSamplingInstrument(String name, @NotNull int[] sampling, boolean loop, boolean portable) {
        super(name, loop, portable);
        int[] s;
        int length = sampling.length + (sampling[0] != 0 ? 1:0) + (sampling[sampling.length-1] != 128 ? 1:0);
        s = new int[length];
        for (int i=0; i<length; ++i){
            if(i == 0 && sampling[0] != 0)
                s[i] = 0;
            else if (i == length-1 && sampling[sampling.length-1] != 128)
                s[i] = 128;
            else
                s[i] = sampling[i - (sampling[0] != 0 ? 1:0)];
        }
        //LCAddon.LOGGER.info("Instrument: [" + name + "] with sampling: " + sampling.toString());
        this.sampling = s.clone();
    }
    @Override
    public MidiReceiver receiver(Player player, Vec3 vec3) {
        return new PatchedPianoMidiReceiver(this, player, vec3);
    }
}
