package com.dfdyz.lcaddon.world.tileentity;

import com.dfdyz.lcaddon.network.ClientPack.CP_UpdateAnimatedPiano;
import com.dfdyz.lcaddon.network.PacketMgr;
import com.dfdyz.lcaddon.registry.LCATileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class AnimatedPianoTileEntity extends PatchedPianoTileEntity {
    //public Instruments.Registry instrument = Instruments.Test1;
    public int[] keyState = new int[5];

    public boolean dirty = false;

    public AnimatedPianoTileEntity(BlockPos pos, BlockState state) {
        super(LCATileEntities.ANIMATED_PIANO.get() ,pos, state);
        for (int i = 0; i < keyState.length; i++) {
            keyState[i]= 0;
        }
    }

    public AnimatedPianoTileEntity(BlockEntityType<?> tileType, BlockPos pos, BlockState state) {
        super(tileType ,pos, state);
        for (int i = 0; i < keyState.length; i++) {
            keyState[i]= 0;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void setKeyClient(int note, boolean state){
        PacketMgr.sendToServer(new CP_UpdateAnimatedPiano(this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ(), note, state));
    }

    @OnlyIn(Dist.CLIENT)
    public void ResetAllKeysClient(){
        PacketMgr.sendToServer(new CP_UpdateAnimatedPiano(this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ(), -100, false));
    }

    public void ResetAllKeys(){
        dirty = true;
        for (int i = 0; i < keyState.length; i++) {
            keyState[i]= 0;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public boolean getKeyState(int note){
        int chunk = note / 32;
        int pos = note % 32;
        return ((keyState[chunk] >> pos) & 1) > 0;
    }

    public void setKey(int note, boolean state){
        dirty = true;
        if(note >= 0 && note < 32*keyState.length){
            int chunk = note / 32;
            int pos = note % 32;
            if(state){
                keyState[chunk] |= 1 << pos;
            }
            else {
                keyState[chunk] &= ~(1 << pos);
            }
        }
    }

}
