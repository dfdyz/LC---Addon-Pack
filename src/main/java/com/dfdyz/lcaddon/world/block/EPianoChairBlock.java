package com.dfdyz.lcaddon.world.block;

import com.dfdyz.lcaddon.utils.BlockShapeUtils;
import com.mojang.math.Vector3d;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.ywzj.midi.block.PianoBenchBlock;
import org.ywzj.midi.entity.SeatEntity;

public class EPianoChairBlock extends PianoBenchBlock {
    private static final VoxelShape SHAPE_NORTH = Shapes.box(0.1, 0.05, 0.2, 0.9, 0.6, 0.75);
    private static final VoxelShape SHAPE_SOUTH = Shapes.box(0.1, 0.05, 0.25, 0.9, 0.6, 0.8);
    private static final VoxelShape SHAPE_WEST = Shapes.box(0.2, 0.05, 0.1, 0.75, 0.6, 0.9);
    private static final VoxelShape SHAPE_EAST = Shapes.box(0.25, 0.05, 0.1, 0.8, 0.6, 0.9);


    public EPianoChairBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        Vector3d seatPos = new Vector3d(pos.getX() + 0.5, pos.getY() + 0.3, pos.getZ() + 0.5);
        Direction direction = state.getValue(FACING);
        return SeatEntity.create(world, pos, seatPos, player, direction);
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return BlockShapeUtils.GetShapeByDirection(pState.getValue(FACING), SHAPE_NORTH, SHAPE_SOUTH, SHAPE_EAST, SHAPE_WEST);
    }
}
