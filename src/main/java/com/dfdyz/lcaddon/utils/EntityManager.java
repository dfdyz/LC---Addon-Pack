package com.dfdyz.lcaddon.utils;

import com.google.common.collect.Sets;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import net.minecraft.world.phys.Vec3;


import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class EntityManager {
    private static final Set<Entity> Entitys = Sets.newConcurrentHashSet();

    public static void AddEntity(Entity t){
        Entitys.add(t);
    }
    public static void RemoveEntity(Entity te){
        Entitys.remove(te);
    }


    public static <T extends Entity> T GetNearestInRange(Level level, Vec3 pos, float d, Class<T> clazz){
        float min = d;
        AtomicReference<T> min_te = new AtomicReference<>();
        Entitys.forEach((te)->{
            if(te.level() != null
                    && !te.isRemoved()
                    && te.level().dimension().equals(level.dimension())
                    && te.position().distanceTo(pos) < min
                    && te.getClass().equals(clazz)
            ){
                min_te.set((T) te);
            }
        });
        return min_te.get();
    }
}
