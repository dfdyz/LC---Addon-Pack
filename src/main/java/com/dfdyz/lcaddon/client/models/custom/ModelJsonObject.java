package com.dfdyz.lcaddon.client.models.custom;

import com.dfdyz.lcaddon.LCAddon;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.joml.Vector3f;


import java.util.ArrayList;
import java.util.HashMap;

public class ModelJsonObject {
    public float[] texture_size = {16, 16};
    public HashMap<String, String> textures = Maps.newHashMap();
    public Element[] elements = {};
    public JsonArray groups;

    public static class Element{
        public String name;
        public float[] from,to;
        public HashMap<String, Face> faces = Maps.newHashMap();

        public void dumpQuads(ArrayList<QuadsGroup.Quad> dist, float[] textureSize){
            faces.forEach(((s, face) -> {
                dist.add(face.toQuad(s, this, textureSize));
            }));
        }


        public static class Face{
            public float[] uv = {};
            public String texture;

            public QuadsGroup.Quad toQuad(String facing, Element elem, float[] textureSize){
                Vector3f[] vets = new Vector3f[4];
                float[] uvs = uv.clone();
                uvs[0] /= 16;
                uvs[1] /= 16;
                uvs[2] /= 16;
                uvs[3] /= 16;

                Vector3f normal = new Vector3f();
                switch (facing){
                    case "north":
                        vets[0] = new Vector3f(elem.to[0]/16.f, elem.to[1]/16.f, elem.from[2]/16.f);
                        vets[1] = new Vector3f(elem.from[0]/16.f, elem.to[1]/16.f, elem.from[2]/16.f);
                        vets[2] = new Vector3f(elem.from[0]/16.f, elem.from[1]/16.f, elem.from[2]/16.f);
                        vets[3] = new Vector3f(elem.to[0]/16.f, elem.from[1]/16.f, elem.from[2]/16.f);
                        normal.set(0,0,-1);
                        break;
                    case "south":
                        vets[0] = new Vector3f(elem.from[0]/16.f, elem.to[1]/16.f, elem.to[2]/16.f);
                        vets[1] = new Vector3f(elem.to[0]/16.f, elem.to[1]/16.f, elem.to[2]/16.f);
                        vets[2] = new Vector3f(elem.to[0]/16.f, elem.from[1]/16.f, elem.to[2]/16.f);
                        vets[3] = new Vector3f(elem.from[0]/16.f, elem.from[1]/16.f, elem.to[2]/16.f);
                        normal.set(0,0,1);
                        break;
                    case "west":
                        vets[0] = new Vector3f(elem.from[0]/16.f, elem.to[1]/16.f, elem.from[2]/16.f);
                        vets[1] = new Vector3f(elem.from[0]/16.f, elem.to[1]/16.f, elem.to[2]/16.f);
                        vets[2] = new Vector3f(elem.from[0]/16.f, elem.from[1]/16.f, elem.to[2]/16.f);
                        vets[3] = new Vector3f(elem.from[0]/16.f, elem.from[1]/16.f, elem.from[2]/16.f);
                        normal.set(-1,0,0);
                        break;
                    case "east":
                        vets[0] = new Vector3f(elem.to[0]/16.f, elem.to[1]/16.f, elem.to[2]/16.f);
                        vets[1] = new Vector3f(elem.to[0]/16.f, elem.to[1]/16.f, elem.from[2]/16.f);
                        vets[2] = new Vector3f(elem.to[0]/16.f, elem.from[1]/16.f, elem.from[2]/16.f);
                        vets[3] = new Vector3f(elem.to[0]/16.f, elem.from[1]/16.f, elem.to[2]/16.f);
                        normal.set(1,0,0);
                        break;
                    case "up":
                        vets[0] = new Vector3f(elem.from[0]/16.f, elem.to[1]/16.f, elem.from[2]/16.f);
                        vets[1] = new Vector3f(elem.to[0]/16.f, elem.to[1]/16.f, elem.from[2]/16.f);
                        vets[2] = new Vector3f(elem.to[0]/16.f, elem.to[1]/16.f, elem.to[2]/16.f);
                        vets[3] = new Vector3f(elem.from[0]/16.f, elem.to[1]/16.f, elem.to[2]/16.f);
                        normal.set(0,1,0);
                        break;
                    case "down":
                        vets[0] = new Vector3f(elem.from[0]/16.f, elem.from[1]/16.f, elem.to[2]/16.f);
                        vets[1] = new Vector3f(elem.from[0]/16.f, elem.from[1]/16.f, elem.from[2]/16.f);
                        vets[2] = new Vector3f(elem.to[0]/16.f, elem.from[1]/16.f, elem.from[2]/16.f);
                        vets[3] = new Vector3f(elem.to[0]/16.f, elem.from[1]/16.f, elem.to[2]/16.f);
                        normal.set(0,-1,0);
                        break;
                    default:
                        break;
                }
                return new QuadsGroup.Quad(vets, uvs, texture, normal);
            }
        }
    }

    public static class Group{
        public String name;
        public ArrayList<GroupElement<?>> child = Lists.newArrayList();
        public void toQuads(ModelJsonObject model, ArrayList<QuadsGroup.Quad> dist){
            child.forEach((e)->{
                if(e.type == GroupElement.Type.Group){
                    ((Group)e.elem).toQuads(model, dist);
                }
                else {
                    int index = (int)e.elem;
                    Element elem = model.elements[index];
                    elem.dumpQuads(dist, model.texture_size);
                }
            });
        }
        public static Group fromJson(JsonObject o){
            Group g = new Group();
            g.name = o.get("name").getAsString();
            JsonArray child = o.getAsJsonArray("children");
            child.forEach((e)->{
                if(e.isJsonObject()){
                    g.child.add(new GroupElement.EGroup(Group.fromJson(e.getAsJsonObject())));
                }
                else {
                    g.child.add(new GroupElement.EInteger(e.getAsInt()));
                }
            });
            return g;
        }

        public static abstract class GroupElement<T>{
            public final T elem;
            public final Type type;

            public enum Type{
                Group, Integer;
            }

            public GroupElement(T elem, Type type) {
                this.elem = elem;
                this.type = type;
            }

            public static class EGroup extends GroupElement<Group>{
                public EGroup(Group group) {
                    super(group, Type.Group);
                }
            }
            public static class EInteger extends GroupElement<Integer>{
                public EInteger(int num) {
                    super(num, Type.Integer);
                }
            }
        }
    }


    public static ModelJsonObject fromJson(String s){
        ModelJsonObject mo = new ModelJsonObject();
        JsonObject model = JsonParser.parseString(s).getAsJsonObject();

        JsonArray jArray = model.getAsJsonArray("texture_size");
        mo.texture_size[0] = jArray.get(0).getAsFloat();
        mo.texture_size[1] = jArray.get(1).getAsFloat();

        mo.textures = LCAddon.gson.fromJson(model.getAsJsonObject("textures"),
                new TypeToken<HashMap<String, String>>(){}.getType());

        mo.elements = LCAddon.gson.fromJson(model.get("elements"),
                Element[].class);
        mo.groups = model.getAsJsonArray("groups");

        return mo;
    }


}
