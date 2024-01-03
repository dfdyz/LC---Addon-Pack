package com.dfdyz.lcaddon.registry;

import com.dfdyz.lcaddon.LCAddon;
import com.dfdyz.lcaddon.client.audio.instruments.MultiSamplingInstrument;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.ywzj.midi.YwzjMidi;
import org.ywzj.midi.all.AllSounds;
import org.ywzj.midi.instrument.Instrument;
import org.ywzj.midi.util.MidiUtils;

import java.util.LinkedHashMap;
import java.util.List;

public class LCASoundEvents {
    public static final List<AllSounds.SoundResource> ALL_SOUND_RESOURCE = Lists.newArrayList();
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, LCAddon.MODID);

    public static void registerKeysSoundEvent(Instrument instrument, int note) {
        String notation = MidiUtils.noteToNotation(note);
        String insName = instrument.getName();
        String soundName = insName + "_" + notation;
        SoundEvent soundEvent = SoundEvent.createVariableRangeEvent(new ResourceLocation(LCAddon.MODID, soundName));
        LinkedHashMap<String, SoundEvent> map = AllSounds.INSTRUMENT_WITH_SOUNDS.getOrDefault(insName, new LinkedHashMap());
        map.put(soundName, soundEvent);
        AllSounds.INSTRUMENT_WITH_SOUNDS.put(insName, map);
        ALL_SOUND_RESOURCE.add(new AllSounds.SoundResource(soundEvent.getLocation(), insName + "/" + notation));

        SOUND_EVENTS.register(soundName, () -> {
            return soundEvent;
        });
    }

    public static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(LCAddon.MODID, name)));
    }

    private static void registerKeysSoundNormal(MultiSamplingInstrument instrument, int note) {
        String notation = MidiUtils.noteToNotation(note);
        String insName = instrument.getName();
        String soundName = insName + "_" + notation;
        LinkedHashMap<String, SoundEvent> map = AllSounds.INSTRUMENT_WITH_SOUNDS.getOrDefault(insName, new LinkedHashMap());
        map.put(soundName, map.get(soundName + "_" + (instrument.sampling.length-2)));
        AllSounds.INSTRUMENT_WITH_SOUNDS.put(insName, map);
    }

    public static void registerInstrumentSingleSampling(Instrument ins, int start, int end){
        for (int n = start; n <= end; ++n){
            registerKeysSoundEvent(ins, n);
        }
    }

    public static void registerInstrumentMultiSampling(MultiSamplingInstrument ins, int start, int end, int level){
        for(int l=1; l<=level; ++l){
            registerInstrumentSingleSampling(ins, start + l * LCAddon.level_base, end + (l * LCAddon.level_base));
        }
        for (int n = start; n <= end; ++n){
            registerKeysSoundNormal(ins, n);
        }
    }


    public static void GenSoundMapJson(){
        JsonObject obj = new JsonObject();
        ALL_SOUND_RESOURCE.forEach((e)->{e.write(obj);});
        System.out.println("LCA Sound Source Json\n\n"+obj.toString()+"\n\n");
    }

    public static void prevInit(){

    }

    public static final  RegistryObject<SoundEvent> SYNTHESIZER;
    static {
        SYNTHESIZER = LCASoundEvents.registerSoundEvent("synthesizer");
    }

}
