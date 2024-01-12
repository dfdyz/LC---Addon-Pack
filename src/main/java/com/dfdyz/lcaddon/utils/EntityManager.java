package com.dfdyz.lcaddon.utils;

import com.dfdyz.lcaddon.LCAddon;
import com.google.common.collect.Sets;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerSetSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;


public class EntityManager {
    private static final Set<Entity> Entitys = Sets.newConcurrentHashSet();

    public static void AddEntity(Entity t){
        //System.out.println("Add: " + t.getStringUUID());
        Entitys.add(t);
    }
    public static void RemoveEntity(Entity te){
        Entitys.remove(te);
    }

    public static <T extends Entity> T GetNearestInRange(Level level, Vec3 pos, float d, Class<T> clazz){
        Entitys.removeIf(e ->  e == null || e.isRemoved());
        AtomicReference<Float> min = new AtomicReference<>(d);
        AtomicReference<T> min_te = new AtomicReference<>();
        Entitys.forEach((te)->{
            if(te.level() != null
                    && !te.isRemoved()
                    && te.level().dimension().equals(level.dimension())
                    && te.position().distanceTo(pos) < min.get()
                    && te.getClass().equals(clazz)
            ){
                min_te.set((T) te);
                min.set((float) te.position().distanceTo(pos));
            }
        });
        return min_te.get();
    }

    public static void Init(){
        Entitys.clear();
    }

    @OnlyIn(Dist.CLIENT)
    @Mod.EventBusSubscriber(modid = LCAddon.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class EventsHandler{
        @OnlyIn(Dist.CLIENT)
        @SubscribeEvent
        public static void OnPlayerEnterWorld(PlayerEvent.PlayerLoggedInEvent event){
            if (Minecraft.getInstance().player == null || event.getEntity() == Minecraft.getInstance().player){
                //System.out.println("Init");
                Init();
            }
        }
    }

}
