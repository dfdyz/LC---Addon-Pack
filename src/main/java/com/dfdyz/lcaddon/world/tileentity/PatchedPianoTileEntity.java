package com.dfdyz.lcaddon.world.tileentity;

import com.dfdyz.lcaddon.registry.LCATileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.ywzj.midi.gui.screen.ClavichordScreen;

import javax.annotation.Nullable;

public class PatchedPianoTileEntity extends BlockEntity {
    @OnlyIn(Dist.CLIENT)
    public ClavichordScreen pianoScreen;
    //public Instruments.Registry instrument = Instruments.Test1;
    public PatchedPianoTileEntity(BlockPos pos, BlockState state) {
        super(null ,pos, state);
    }

    public PatchedPianoTileEntity(BlockEntityType<?> tileType, BlockPos pos, BlockState state) {
        super(tileType ,pos, state);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return new CompoundTag();
    }



    @Override
    public void handleUpdateTag(CompoundTag tag) {
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if(FMLEnvironment.dist == Dist.CLIENT){
            this.closeReceiver();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void closeReceiver() {
        if (this.pianoScreen != null) {
            this.pianoScreen.closeMidiReceiver();
        }
    }
}
