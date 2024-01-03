package com.dfdyz.lcaddon.registry;

import com.dfdyz.lcaddon.LCAddon;
import com.dfdyz.lcaddon.client.models.entity.MoonLampModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = LCAddon.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LCALayerDefinitions {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(MoonLampModel.MOONLAMP_LayerLocation, MoonLampModel::createBodyLayer);
    }
}
