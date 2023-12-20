package com.dfdyz.lcaddon.world.block;

import com.dfdyz.lcaddon.client.audio.LCAInstruments;
import com.dfdyz.lcaddon.client.gui.ScreenMgr;
import com.dfdyz.lcaddon.utils.BlockShapeUtils;
import com.dfdyz.lcaddon.world.tileentity.PatchedPianoTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.ywzj.midi.block.piano.PianoBlock;
import org.ywzj.midi.instrument.Instrument;

public class PatchedPianoBlock extends PianoBlock {
    private static final VoxelShape SHAPE_NORTH = Shapes.box(0.0, 0.1, 0.0, 2.0, 1.7, 1.0);
    private static final VoxelShape SHAPE_SOUTH = Shapes.box(-1.0, 0.1, 0.0, 1.0, 1.7, 1.0);
    private static final VoxelShape SHAPE_EAST = Shapes.box(0.0, 0.1, 0.0, 1.0, 1.7, 2.0);
    private static final VoxelShape SHAPE_WEST = Shapes.box(0.0, 0.1, -1.0, 1.0, 1.7, 1.0);

    public PatchedPianoBlock(Properties properties) {
        super(properties);
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return BlockShapeUtils.GetShapeByDirection(pState.getValue(FACING), SHAPE_NORTH, SHAPE_SOUTH, SHAPE_EAST, SHAPE_WEST);
    }

    @Override
    protected Instrument getInstrument() {
        return LCAInstruments.STEINWAY_D;
    }

    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        if (world.isClientSide) {
            PatchedPianoTileEntity pianoBlockEntity = (PatchedPianoTileEntity)world.getBlockEntity(pos);
            if (pianoBlockEntity == null) {
                throw new RuntimeException("找不到钢琴");
            }

            OpenScreen(pos, pianoBlockEntity);
        }
        return InteractionResult.PASS;
    }

    protected void OpenScreen(BlockPos pos, PatchedPianoTileEntity pianoBlockEntity){
        ScreenMgr.openPianoScreen(getInstrument(), pos, pianoBlockEntity, 3);
    }

    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new PatchedPianoTileEntity(pPos, pState);
    }

}
