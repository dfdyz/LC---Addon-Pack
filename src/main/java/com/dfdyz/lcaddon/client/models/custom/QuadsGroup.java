package com.dfdyz.lcaddon.client.models.custom;

import com.dfdyz.lcaddon.client.renderer.LCARenderTypes;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import org.joml.*;

import java.lang.Math;
import java.util.HashMap;

public class QuadsGroup {
    public final Quad[] quads;
    public QuadsGroup(Quad[] q){
        quads = q;
    }

    public void PushVertex(MultiBufferSource buffer, PoseStack poseStack, int lightCol, HashMap<String, ResourceLocation> texture, Vector3f translate, Vector4f col){
        for (int i=0; i< quads.length; ++i){
            quads[i].PushVertex(buffer, poseStack, lightCol, texture, translate, col);
        }
    }

    public static class Quad{
        public final Vector3f[] vertexs;
        public final Vector3f normal;
        public final float[] uvs;
        public final String texture;
        public Quad(Vector3f[] p, float[] uv, String texture, Vector3f normal){
            this.vertexs = p.clone();
            this.uvs = uv.clone();
            this.texture = texture;
            this.normal = normal;
        }

        private static final int[][] uvMap = new int[][]{
                {0, 1}, {2, 1}, {2, 3}, {0, 3}
        };
        public void PushVertex(MultiBufferSource buffer, PoseStack poseStack, int lightCol, HashMap<String, ResourceLocation> texture, Vector3f translate, Vector4f col){
            RenderType rt = LCARenderTypes.getTileentityRt(texture.getOrDefault(this.texture, TextureManager.INTENTIONAL_MISSING_TEXTURE));
            VertexConsumer vertexConsumer = buffer.getBuffer(rt);

            Matrix4f matrix4f = poseStack.last().pose();
            Matrix3f matrix3f = poseStack.last().normal();

            for (int i=3; i>=0; --i){
                Vector3f normal = matrix3f.transform(new Vector3f(this.normal));
                Vector4f pos =  matrix4f.transform(new Vector4f(vertexs[i].add(translate, new Vector3f()), 1.0F));

                vertexConsumer.vertex(pos.x , pos.y, pos.z, col.x, col.y, col.z, col.w, uvs[uvMap[i][0]], uvs[uvMap[i][1]], lightCol, lightCol, normal.x ,normal.y, normal.z);
                //buffer.vertex()
            }

            if(buffer instanceof MultiBufferSource.BufferSource bs){
                bs.endBatch(rt);
            }
        }
    }
}
