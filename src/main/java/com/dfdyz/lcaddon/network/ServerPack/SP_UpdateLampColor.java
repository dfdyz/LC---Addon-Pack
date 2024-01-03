package com.dfdyz.lcaddon.network.ServerPack;

import com.dfdyz.lcaddon.network.ClientPack.CP_UpdateLampColor;
import com.dfdyz.lcaddon.world.entity.MoonLampEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import org.joml.Vector4f;

import java.util.UUID;
import java.util.function.Supplier;

public class SP_UpdateLampColor {
    public UUID lamp;
    public float r,g,b,a;

    public SP_UpdateLampColor(){
        this.lamp = null;
        this.r = 0;
        this.g = 0;
        this.b = 0;
        this.a = 0;
    }

    public SP_UpdateLampColor(CP_UpdateLampColor cp){
        this.lamp = cp.lamp;
        this.r = cp.r;
        this.g = cp.g;
        this.b = cp.b;
        this.a = cp.a;
    }

    public SP_UpdateLampColor(UUID uuid, float r, float g, float b, float a){
        this.lamp = uuid;
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }


    public static SP_UpdateLampColor decode(FriendlyByteBuf buf) {
        SP_UpdateLampColor data = new SP_UpdateLampColor();
        data.lamp = buf.readUUID();
        data.r = buf.readFloat();
        data.g = buf.readFloat();
        data.b = buf.readFloat();
        data.a = buf.readFloat();
        return data;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(lamp);
        buf.writeFloat(r);
        buf.writeFloat(g);
        buf.writeFloat(b);
        buf.writeFloat(a);
    }

    public static void onClientMessageReceived(SP_UpdateLampColor msg, Supplier<NetworkEvent.Context> context){

        NetworkEvent.Context ctx = context.get();
        ctx.setPacketHandled(true);
        ctx.enqueueWork(() -> {
            Entity entity = Minecraft.getInstance().level.getEntities().get(msg.lamp);
            if(entity != null && entity instanceof MoonLampEntity lamp && !lamp.isRemoved()){
                lamp.syncColor(new Vector4f(msg.r, msg.g, msg.b, msg.a));
            }
        });
    }

}
