package com.dfdyz.lcaddon;

import com.dfdyz.lcaddon.client.audio.LCAInstruments;
import com.dfdyz.lcaddon.network.PacketMgr;
import com.dfdyz.lcaddon.registry.*;
import com.google.gson.Gson;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(LCAddon.MODID)
public class LCAddon
{
    // Directly reference a slf4j logger
    public static final Gson gson = new Gson();
    public static final String MODID = "lcaddon";
    public static final int level_base = 10000000;
    public static final Logger LOGGER = LogUtils.getLogger();

    public LCAddon()
    {
        IEventBus bus_mod = FMLJavaModLoadingContext.get().getModEventBus();
        bus_mod.addListener(this::setupCommon);
        bus_mod.addListener(this::setupClient);
        LCAInstruments.prevRegister();
        LCASoundEvents.prevInit();
        LCASoundEvents.SOUND_EVENTS.register(bus_mod);
        LCAEntities.ENTITIES.register(bus_mod);
        LCABlocks.BLOCKS.register(bus_mod);
        LCATileEntities.TILE_ENTITIES.register(bus_mod);
        LCAItems.ITEMS.register(bus_mod);
        bus_mod.register(PacketMgr.CHANNEL);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private boolean gen_sound_map = false;
    private void setupCommon(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(PacketMgr::Init);
    }

    private void setupClient(final FMLClientSetupEvent event){
        event.enqueueWork(LCAAssets::Init);
        event.enqueueWork(LCARenderers::onRenderTypeSetup);
        event.enqueueWork(()->{
            if (gen_sound_map) LCASoundEvents.GenSoundMapJson();
        });
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
}
