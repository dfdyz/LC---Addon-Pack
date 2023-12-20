package com.dfdyz.lcaddon.client.models.entity;// Made with Blockbench 4.9.2


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class MoonLampModel<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "moon_lamp"), "main");
	private final ModelPart bb_main;

	public MoonLampModel(ModelPart root) {
		this.bb_main = root.getChild("bb_main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -12.0F, -5.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(18, 17).addBox(-3.0F, -13.0F, -4.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(0, 16).addBox(-3.0F, -4.0F, -4.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(28, 24).addBox(-3.0F, -11.0F, -6.0F, 6.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 0).addBox(-3.0F, -11.0F, 3.0F, 6.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(14, 24).addBox(4.0F, -11.0F, -4.0F, 1.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(0, 23).addBox(-5.0F, -11.0F, -4.0F, 1.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}