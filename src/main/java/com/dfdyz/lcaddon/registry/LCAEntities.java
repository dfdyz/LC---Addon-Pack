package com.dfdyz.lcaddon.registry;

import com.dfdyz.lcaddon.LCAddon;
import com.dfdyz.lcaddon.world.entity.FakePlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class LCAEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, LCAddon.MODID);

    /*
    public static final RegistryObject<EntityType<FakePlayer>> FAKE_PLAYER = ENTITIES.register("fake_player", () ->
            EntityType.Builder.<FakePlayer>of(FakePlayer::new, MobCategory.CREATURE)
                    .sized(0.8F, 1.9F).clientTrackingRange(4).updateInterval(20).build("fake_player")
    );*/
}
