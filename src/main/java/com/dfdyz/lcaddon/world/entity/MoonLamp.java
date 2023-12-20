package com.dfdyz.lcaddon.world.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Collections;
import java.util.List;

public class MoonLamp extends LivingEntity {
    public MoonLamp(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    private static final List<ItemStack> EMPTY_LIST = Collections.emptyList();
    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return EMPTY_LIST;
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }


}
