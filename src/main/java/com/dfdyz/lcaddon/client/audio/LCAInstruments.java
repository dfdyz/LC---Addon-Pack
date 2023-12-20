package com.dfdyz.lcaddon.client.audio;

import com.dfdyz.lcaddon.LCAddon;
import com.dfdyz.lcaddon.client.audio.instruments.AnimatedPiano;
import com.dfdyz.lcaddon.client.audio.instruments.MultiSamplingInstrument;
import com.dfdyz.lcaddon.client.audio.instruments.PatchedPiano;
import com.dfdyz.lcaddon.registry.LCASoundEvents;
import com.dfdyz.lcaddon.utils.ReflectionUtils;
import net.minecraft.world.level.ItemLike;
import org.ywzj.midi.YwzjMidi;
import org.ywzj.midi.all.AllInstruments;
import org.ywzj.midi.all.AllSounds;
import org.ywzj.midi.instrument.Instrument;
import org.ywzj.midi.util.MidiUtils;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class LCAInstruments {
    private static final ConcurrentHashMap<Integer, Instrument> ALL_INSTRUMENTS;
    public static final Instrument STEINWAY_D;

    public static Instrument register(Instrument instrument, String keyStart, String keyEnd){
        LCASoundEvents.registerInstrumentSingleSampling(instrument, MidiUtils.notationToNote(keyStart), MidiUtils.notationToNote(keyEnd));
        ALL_INSTRUMENTS.put(instrument.getIndex(), instrument);
        LCAddon.LOGGER.info("Registering instrument {} with id {} by {}", new Object[]{instrument.getName(), instrument.getIndex(), LCAddon.MODID});
        return instrument;
    }

    public static Instrument register(MultiSamplingInstrument instrument, String keyStart, String keyEnd){
        //AllSounds.registerKeys(LCAddon.MODID, instrument.getName(), MidiUtils.notationToNote(keyStart), MidiUtils.notationToNote(keyEnd));
        LCASoundEvents.registerInstrumentMultiSampling(instrument, MidiUtils.notationToNote(keyStart), MidiUtils.notationToNote(keyEnd), instrument.sampling.length - 1);
        ALL_INSTRUMENTS.put(instrument.getIndex(), instrument);
        LCAddon.LOGGER.info("Registering instrument {} with id {} by {}", new Object[]{instrument.getName(), instrument.getIndex(), LCAddon.MODID});
        return instrument;
    }
    public static void prevRegister(){

    }

    static {
        ALL_INSTRUMENTS = ReflectionUtils.GetField(AllInstruments.class, "ALL_INSTRUMENTS");
        LCAddon.LOGGER.info("Reg Instrument");
        STEINWAY_D = register(new AnimatedPiano("steinway_d", new int[]{ 15, 31, 63 }), "a1", "c8");
    }
}
