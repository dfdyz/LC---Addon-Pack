package com.dfdyz.lcaddon.world.tileentity;

import com.dfdyz.lcaddon.registry.LCATileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.ywzj.midi.instrument.Instrument;

public class ElectronicPianoTileEntity extends AnimatedPianoTileEntity {
    //public Instruments.Registry instrument = Instruments.Test1;
    private static final Instrument source[] = new Instrument[]{

    };
    private int selected_inst = 0;

    public ElectronicPianoTileEntity(BlockPos pos, BlockState state) {
        super(LCATileEntities.ELECTRONIC_PIANO.get() ,pos, state);
    }

    public ElectronicPianoTileEntity(BlockEntityType<?> tileType, BlockPos pos, BlockState state) {
        super(tileType ,pos, state);
    }

    public Instrument getNextInstrument(){
        selected_inst++;
        if(selected_inst >= source.length) selected_inst = 0;
        return source[selected_inst];
    }

}
