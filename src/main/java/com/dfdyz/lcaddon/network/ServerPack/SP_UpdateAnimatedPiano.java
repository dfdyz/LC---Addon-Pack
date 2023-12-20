package com.dfdyz.lcaddon.network.ServerPack;

import com.dfdyz.lcaddon.world.tileentity.AnimatedPianoTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SP_UpdateAnimatedPiano {
    public int x,y,z;
    public int[] keys;

    public SP_UpdateAnimatedPiano(){
    }

    public SP_UpdateAnimatedPiano(AnimatedPianoTileEntity te){
        this.x = te.getBlockPos().getX();
        this.y = te.getBlockPos().getY();
        this.z = te.getBlockPos().getZ();
        this.keys = te.keyState;
    }

    public SP_UpdateAnimatedPiano(int x, int y, int z, int[] keys) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.keys = keys;
    }

    public static SP_UpdateAnimatedPiano decode(FriendlyByteBuf buf) {
        SP_UpdateAnimatedPiano data = new SP_UpdateAnimatedPiano();
        data.x = buf.readInt();
        data.y = buf.readInt();
        data.z = buf.readInt();
        data.keys = buf.readVarIntArray(5);
        return data;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeVarIntArray(keys);
    }

    public static void onClientMessageReceived(SP_UpdateAnimatedPiano msg, Supplier<NetworkEvent.Context> context){
        NetworkEvent.Context ctx = context.get();
        ctx.setPacketHandled(true);
        ctx.enqueueWork(() -> {
            BlockEntity be = Minecraft.getInstance().level.getBlockEntity(new BlockPos(msg.x ,msg.y, msg.z));
            if(be != null){
                if(be instanceof AnimatedPianoTileEntity animated){
                    for (int i = 0; i < Math.min(animated.keyState.length, msg.keys.length); i++) {
                        animated.keyState[i] = msg.keys[i];
                    }
                }
            }
        });
    }
}
