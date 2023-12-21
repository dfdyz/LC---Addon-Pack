package com.dfdyz.lcaddon.network.ClientPack;

import com.dfdyz.lcaddon.network.PacketMgr;
import com.dfdyz.lcaddon.network.ServerPack.SP_UpdateLampColor;
import com.dfdyz.lcaddon.world.entity.MoonLampEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import org.joml.Vector4f;

import java.util.UUID;
import java.util.function.Supplier;

public class CP_UpdateLampColor {
    public UUID lamp;
    public float r,g,b,a;

    public CP_UpdateLampColor(){
        this.lamp = null;
        this.r = 0;
        this.g = 0;
        this.b = 0;
        this.a = 0;
    }

    public CP_UpdateLampColor(UUID uuid, float r, float g, float b, float a){
        this.lamp = uuid;
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }


    public static CP_UpdateLampColor decode(FriendlyByteBuf buf) {
        CP_UpdateLampColor data = new CP_UpdateLampColor();
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

    public static void onServerMessageReceived(CP_UpdateLampColor msg, Supplier<NetworkEvent.Context> context){
        NetworkEvent.Context ctx = context.get();
        ctx.setPacketHandled(true);
        ctx.enqueueWork(() -> {
            Entity entity = ctx.getSender().level().getEntities().get(msg.lamp);
            if(entity != null && entity instanceof MoonLampEntity lamp && !lamp.isRemoved()){
                PacketMgr.sendToAllPlayerTrackingThisEntity(
                        new SP_UpdateLampColor(msg),
                        ctx.getSender()
                );
            }
        });
    }

}
