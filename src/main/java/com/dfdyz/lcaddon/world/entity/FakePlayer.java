package com.dfdyz.lcaddon.world.entity;

import com.dfdyz.lcaddon.registry.LCAEntities;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.UUID;

public class FakePlayer extends Player {
    public FakePlayer(Level level, BlockPos pos, float yaw, GameProfile profile) {
        super(level, pos, yaw, profile);

    }
    private static GameProfile GenProfile(){
            UUID uuid = UUID.randomUUID();
            return new GameProfile(uuid, uuid.toString());
    }
    public FakePlayer(EntityType<FakePlayer> type, Level level) {
        super(level, new BlockPos(0,0,0), 0f,GenProfile());
        //iii = 0;
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public AttributeMap getAttributes() {
        return super.getAttributes();
    }

    @Override
    public EntityType<?> getType() {
        return null;
        //return LCAEntities.FAKE_PLAYER.get();
    }

    @Override
    public boolean isSpectator() {
        return false;
    }

    @Override
    public boolean isCreative() {
        return false;
    }
}
