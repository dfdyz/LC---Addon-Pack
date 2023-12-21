package com.dfdyz.lcaddon.client.renderer.entity;

import com.dfdyz.lcaddon.LCAddon;
import com.dfdyz.lcaddon.client.models.entity.MoonLampModel;
import com.dfdyz.lcaddon.world.entity.MoonLampEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector4f;

public class MoonLampRenderer extends LivingEntityRenderer<MoonLampEntity, MoonLampModel> {
    private static final ResourceLocation Texture = new ResourceLocation(LCAddon.MODID, "textures/entity/moon_lamp.png");
    public MoonLampRenderer(EntityRendererProvider.Context context, MoonLampModel model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Override
    public void render(MoonLampEntity entity, float size_, float pt, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        //System.out.println("Renderer");
        poseStack.pushPose();
        poseStack.scale(0.5f,0.5f,0.5f);
        poseStack.translate(0, -0.5f, 0);
        //super.render(entity, size_, pt, poseStack, bufferSource, light);

        Vector4f color = entity.getColor(pt);

        model.renderToBuffer(poseStack, bufferSource.getBuffer(model.renderType(Texture)),
                light,
                getOverlayCoords(entity, this.getWhiteOverlayProgress(entity, pt)),
                color.x,color.y,color.z,color.w);
        poseStack.popPose();
    }

    @Override
    protected void renderNameTag(MoonLampEntity p_114498_, Component p_114499_, PoseStack p_114500_, MultiBufferSource p_114501_, int p_114502_) {

    }

    @Override
    public ResourceLocation getTextureLocation(MoonLampEntity moonLamp) {
        return Texture;
    }

    @Override
    protected int getBlockLightLevel(MoonLampEntity lamp, BlockPos p_114497_) {
        return (int) (8+lamp.getColor(1).w * 7);
    }
}
