package com.dfdyz.lcaddon.client.models.custom;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.compress.utils.Lists;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class AnimatedQuads {
    public final HashMap<String, QuadsGroup> parts = Maps.newHashMap();
    public final HashMap<String, Vector3f> translate = Maps.newHashMap();
    public final HashMap<String, Vector4f> color = Maps.newHashMap();
    public final HashMap<String, ResourceLocation> textures = Maps.newHashMap();
    public final Vector2f texture_size = new Vector2f();
    private final Vector3f ZERO = new Vector3f(0,0,0);
    private static final Vector4f WHITE = new Vector4f(1);
    public AnimatedQuads(){

    }

    public void InitState(){
        translate.clear();
        color.clear();
    }


    public void SetTranslate(String name, Vector3f vector3f){
        translate.put(name, vector3f);
    }
    public void SetColor(String name, Vector4f col){
        color.put(name, col);
    }
    public void PushVertex(MultiBufferSource buffer, PoseStack poseStack, int lightCol){
        parts.forEach((name, part)->{
            Vector3f tsl = translate.getOrDefault(name, ZERO);
            Vector4f color = this.color.getOrDefault(name, WHITE);
            part.PushVertex(buffer, poseStack, lightCol, textures, tsl, color);
        });
    }

    static AnimatedQuads createInternal(ModelJsonObject model){
        AnimatedQuads aq = new AnimatedQuads();
        ArrayList<QuadsGroup.Quad> qs = Lists.newArrayList();

        model.textures.forEach((key, val)->{
            ResourceLocation o = new ResourceLocation(val);
            aq.textures.put("#" + key, new ResourceLocation(o.getNamespace(), "textures/" + o.getPath() + ".png"));
        });
        aq.texture_size.set(model.texture_size);

        model.groups.forEach((e)->{
            if(e.isJsonObject()){
                ModelJsonObject.Group group = ModelJsonObject.Group.fromJson(e.getAsJsonObject());
                qs.clear();
                group.toQuads(model, qs);
                QuadsGroup qg = new QuadsGroup(qs.toArray(new QuadsGroup.Quad[0]));
                aq.parts.put(group.name, qg);
            }
            else {
                int idx = e.getAsInt();
                ModelJsonObject.Element elem = model.elements[idx];
                if(elem.name != ""){
                    qs.clear();
                    elem.dumpQuads(qs, model.texture_size);
                    QuadsGroup qg = new QuadsGroup(qs.toArray(new QuadsGroup.Quad[0]));
                    aq.parts.put(elem.name, qg);
                }
            }
        });

        return aq;
    }

    public static AnimatedQuads create(ResourceLocation location){
        Minecraft mc = Minecraft.getInstance();

        try {
            InputStream is = mc.getResourceManager().open(location);
            OutputStream os = new ByteArrayOutputStream();;
            byte[] bytes=new byte[1024];
            int len;
            while ((len=is.read(bytes))!=-1){
                os.write(bytes,0,len);
            }
            is.close();
            String json = os.toString();
            ModelJsonObject mjo = ModelJsonObject.fromJson(json);
            return createInternal(mjo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




}
