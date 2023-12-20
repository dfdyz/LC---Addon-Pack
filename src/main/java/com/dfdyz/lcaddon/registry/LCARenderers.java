package com.dfdyz.lcaddon.registry;

import com.dfdyz.lcaddon.LCAddon;

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

@Mod.EventBusSubscriber(modid = LCAddon.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LCARenderers {



    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerRenderer(EntityRenderersEvent.RegisterRenderers event){
        //event.registerEntityRenderer(LCAEntities.FAKE_PLAYER.get(), FakePlayerRenderer::new);
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
