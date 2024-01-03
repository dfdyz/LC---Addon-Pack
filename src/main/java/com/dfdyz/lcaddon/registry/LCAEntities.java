package com.dfdyz.lcaddon.registry;

import com.dfdyz.lcaddon.LCAddon;
import com.dfdyz.lcaddon.world.entity.MoonLampEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = LCAddon.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LCAEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, LCAddon.MODID);

    /*
    public static final RegistryObject<EntityType<FakePlayer>> FAKE_PLAYER = ENTITIES.register("fake_player", () ->
            EntityType.Builder.<FakePlayer>of(FakePlayer::new, MobCategory.CREATURE)
                    .sized(0.8F, 1.9F).clientTrackingRange(4).updateInterval(20).build("fake_player")
    );*/


    public static final RegistryObject<EntityType<MoonLampEntity>> MOON_LAMP =
            ENTITIES.register("moon_lamp", () ->
            EntityType.Builder.of(MoonLampEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(1).build("moon_lamp")
    );

    @SubscribeEvent
    public static void registerEntityAttributes(final EntityAttributeCreationEvent event){
        LCAddon.LOGGER.info("Register Entity Attributes.......");
        event.put(LCAEntities.MOON_LAMP.get(), MoonLampEntity.createAttributes().build());
    }




}
