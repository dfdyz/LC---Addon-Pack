package com.dfdyz.lcaddon.world.block;


import com.dfdyz.lcaddon.client.gui.ScreenMgr;
import com.dfdyz.lcaddon.network.PacketMgr;
import com.dfdyz.lcaddon.network.ServerPack.SP_UpdateAnimatedPiano;
import com.dfdyz.lcaddon.registry.LCATileEntities;
import com.dfdyz.lcaddon.utils.TileEntityManager;
import com.dfdyz.lcaddon.world.tileentity.AnimatedPianoTileEntity;
import com.dfdyz.lcaddon.world.tileentity.PatchedPianoTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;

public class AnimatedPianoBlock extends PatchedPianoBlock {
    public AnimatedPianoBlock(Properties properties) {
        super(properties);
    }

    protected BlockEntityType<?> getType(){
        return LCATileEntities.ANIMATED_PIANO.get();
    }



    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> type) {
        if(!p_153212_.isClientSide){
            if (type != this.getType()) return null;
            return (level,  blockPos, blockState, tileEntity)->{
                if(tileEntity instanceof AnimatedPianoTileEntity animated && animated.dirty){
                    PacketMgr.sendToAllPlayerTrackingThisBlock(
                            new SP_UpdateAnimatedPiano(animated),
                            tileEntity
                    );
                    animated.dirty = false;
                }
            };
        }
        return null;
    }



    @Override
    protected void OpenScreen(BlockPos pos, PatchedPianoTileEntity pianoBlockEntity) {
        ScreenMgr.openPianoScreen(getInstrument(), pos, pianoBlockEntity, 4);
    }

    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        AnimatedPianoTileEntity entity = new AnimatedPianoTileEntity(pPos, pState);
        //TileEntityManager.AddTile(entity);
        return entity;
    }




}
