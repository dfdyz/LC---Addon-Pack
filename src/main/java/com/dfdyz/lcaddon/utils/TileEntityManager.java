package com.dfdyz.lcaddon.utils;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;


import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class TileEntityManager {

    private static final Set<BlockEntity> Tiles = Sets.newConcurrentHashSet();

    public static void AddTile(BlockEntity t){
        Tiles.add(t);
    }

    public static <T extends BlockEntity> T GetNearestInRange(Level level, Vec3 pos, float d, Class<T> clazz){
        float min = d;
        AtomicReference<T> min_te = new AtomicReference<>();
        Tiles.forEach((te)->{
            if(te.hasLevel()
                    && !te.isRemoved()
                    && te.getLevel().dimension().equals(level.dimension())
                    && te.getBlockPos().getCenter().distanceTo(pos) < min
                    && te.getClass().equals(clazz)

            ){
                min_te.set((T) te);
            }
        });
        return min_te.get();
    }

    public static void RemoveTile(BlockEntity te){
        Tiles.remove(te);
    }

}
