package com.dfdyz.lcaddon.network.ClientPack;

import com.dfdyz.lcaddon.world.tileentity.AnimatedPianoTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import org.ywzj.midi.network.message.CPlayNote;

import java.util.UUID;
import java.util.function.Supplier;

public class CP_UpdateAnimatedPiano {
    public int x,y,z;
    public int note = 0;
    public boolean state = false;

    public CP_UpdateAnimatedPiano(){
    }

    public CP_UpdateAnimatedPiano(int x, int y, int z, int note, boolean state) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.note = note;
        this.state = state;
    }

    public static CP_UpdateAnimatedPiano decode(FriendlyByteBuf buf) {
        CP_UpdateAnimatedPiano data = new CP_UpdateAnimatedPiano();
        data.x = buf.readInt();
        data.y = buf.readInt();
        data.z = buf.readInt();
        data.note = buf.readInt();
        data.state = buf.readBoolean();
        return data;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeInt(note);
        buf.writeBoolean(state);
    }

    public static void onServerMessageReceived(CP_UpdateAnimatedPiano msg, Supplier<NetworkEvent.Context> context){
        NetworkEvent.Context ctx = context.get();
        ctx.setPacketHandled(true);
        ctx.enqueueWork(() -> {
            BlockEntity be = ctx.getSender().level.getBlockEntity(new BlockPos(msg.x ,msg.y, msg.z));
            if(be != null && be instanceof AnimatedPianoTileEntity animated){
                if(msg.note == -100){
                    animated.ResetAllKeys();
                }
                else {
                    animated.setKey(msg.note, msg.state);
                }
                animated.dirty = true;
            }
        });
    }


}
