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
        Tiles.removeIf(e -> e == null || e.isRemoved());

        AtomicReference<Float>  min = new AtomicReference<>(d);
        AtomicReference<T> min_te = new AtomicReference<>();

        Tiles.forEach((te)->{
            if(te.hasLevel()
                    && !te.isRemoved()
                    && te.getLevel().dimension().equals(level.dimension())
                    && Vec3.atCenterOf(te.getBlockPos()).distanceTo(pos) < min.get()
                    && te.getClass().equals(clazz)
            ){
                min_te.set((T) te);
                min.set((float) Vec3.atCenterOf(te.getBlockPos()).distanceTo(pos));
            }
        });
        return min_te.get();
    }

    public static void RemoveTile(BlockEntity te){
        Tiles.remove(te);
    }

}
