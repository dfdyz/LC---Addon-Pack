package com.dfdyz.lcaddon.world.block;

import com.dfdyz.lcaddon.client.gui.ScreenMgr;
import com.dfdyz.lcaddon.registry.LCATileEntities;
import com.dfdyz.lcaddon.utils.BlockShapeUtils;
import com.dfdyz.lcaddon.world.tileentity.ElectronicPianoTileEntity;
import com.dfdyz.lcaddon.world.tileentity.PatchedPianoTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class ElectronicPianoBlock extends AnimatedPianoBlock {
    private static final VoxelShape SHAPE_SOUTH = Shapes.box(-0.55, 0.01, 0.2, 1.55, 1, 1.0);
    private static final VoxelShape SHAPE_NORTH = Shapes.box(-0.55, 0.01, 0.0, 1.55, 1, 0.8);
    private static final VoxelShape SHAPE_WEST = Shapes.box(0.0, 0.01, -0.55, 0.8, 1, 1.55);
    private static final VoxelShape SHAPE_EAST = Shapes.box(0.2, 0.01, -0.55, 1.0, 1, 1.55);

    public ElectronicPianoBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected BlockEntityType<?> getType() {
        return LCATileEntities.ELECTRONIC_PIANO.get();
    }

    @Override
    protected void OpenScreen(BlockPos pos, PatchedPianoTileEntity pianoBlockEntity) {
        ScreenMgr.openEPianoScreen(getInstrument(), pos, (ElectronicPianoTileEntity)pianoBlockEntity);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return BlockShapeUtils.GetShapeByDirection(pState.getValue(FACING), SHAPE_NORTH, SHAPE_SOUTH, SHAPE_EAST, SHAPE_WEST);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ElectronicPianoTileEntity(pPos, pState);
    }
}
