package com.dfdyz.lcaddon.registry;

import com.dfdyz.lcaddon.LCAddon;

import com.dfdyz.lcaddon.client.models.entity.MoonLampModel;
import com.dfdyz.lcaddon.client.renderer.entity.MoonLampRenderer;
import com.dfdyz.lcaddon.client.renderer.tileentity.AnimatedPianoAdditionalRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = LCAddon.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LCARenderers {

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerRenderer(EntityRenderersEvent.RegisterRenderers event){
        registerTileEntityRenderers(event);
        registerEntityRenderers(event);
    }

    protected static void registerTileEntityRenderers(EntityRenderersEvent.RegisterRenderers event){

    }


    protected static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event){
        //event.registerEntityRenderer(LCAEntities.FAKE_PLAYER.get(), FakePlayerRenderer::new);
        event.registerEntityRenderer(LCAEntities.MOON_LAMP.get(), (c) -> {
            return new MoonLampRenderer(c,
                    new MoonLampModel(c.bakeLayer(MoonLampModel.MOONLAMP_LayerLocation)),
                    0);
        });
    }


    public static void onRenderTypeSetup() {
        ItemBlockRenderTypes.setRenderLayer(LCABlocks.STEINWAY_D_PIANO.get(),
                RenderType.tripwire()
        );

        ItemBlockRenderTypes.setRenderLayer(LCABlocks.ELECTRONIC_PIANO.get(),
                RenderType.tripwire()
        );

        BlockEntityRenderers.register(LCATileEntities.ANIMATED_PIANO.get(), AnimatedPianoAdditionalRenderer::new);
        BlockEntityRenderers.register(LCATileEntities.ELECTRONIC_PIANO.get(), AnimatedPianoAdditionalRenderer::new);

    }






}
