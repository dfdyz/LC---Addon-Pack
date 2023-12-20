package com.dfdyz.lcaddon.utils;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockShapeUtils {

    public static VoxelShape GetShapeByDirection(Direction facing, VoxelShape N, VoxelShape S, VoxelShape E, VoxelShape W){
        VoxelShape shape;
        switch (facing) {
            case NORTH:
                shape = N;
                break;
            case SOUTH:
                shape = S;
                break;
            case EAST:
                shape = E;
                break;
            case WEST:
                shape = W;
                break;
            case DOWN:
            case UP:
            default:
                shape = null;
                break;
        }
        return shape;
    }

}
