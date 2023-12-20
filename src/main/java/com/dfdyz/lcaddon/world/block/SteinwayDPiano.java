package com.dfdyz.lcaddon.world.block;

import com.dfdyz.lcaddon.client.gui.ScreenMgr;
import com.dfdyz.lcaddon.utils.BlockShapeUtils;
import com.dfdyz.lcaddon.world.tileentity.AnimatedPianoTileEntity;
import com.dfdyz.lcaddon.world.tileentity.PatchedPianoTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class SteinwayDPiano extends AnimatedPianoBlock {
    private static  VoxelShape SHAPE_SOUTH = Shapes.box(-0.55, 0.0, -1.0, 1.55, 2, 2.0);
    private static  VoxelShape SHAPE_NORTH = Shapes.box(-0.55, 0.0, -1.0, 1.55, 2, 2.0);
    private static  VoxelShape SHAPE_WEST = Shapes.box(-1.0, 0.0, -0.55, 2.0, 2, 1.55);
    private static  VoxelShape SHAPE_EAST = Shapes.box(-1.0, 0.0, -0.55, 2.0, 2, 1.55);

    public SteinwayDPiano(Properties properties) {
        super(properties);
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return BlockShapeUtils.GetShapeByDirection(pState.getValue(FACING), SHAPE_NORTH, SHAPE_SOUTH, SHAPE_EAST, SHAPE_WEST);
    }
}
