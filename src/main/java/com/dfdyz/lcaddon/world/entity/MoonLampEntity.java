package com.dfdyz.lcaddon.world.entity;

import com.dfdyz.lcaddon.network.ClientPack.CP_UpdateLampColor;
import com.dfdyz.lcaddon.network.PacketMgr;
import com.dfdyz.lcaddon.network.ServerPack.SP_UpdateLampColor;
import com.dfdyz.lcaddon.registry.LCAEntities;
import com.dfdyz.lcaddon.utils.EntityManager;
import com.dfdyz.lcaddon.world.tileentity.PatchedPianoTileEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector4f;

import java.util.Collections;
import java.util.List;

public class MoonLampEntity extends LivingEntity {
    public MoonLampEntity(EntityType<? extends LivingEntity> type, Level level) {
        super(LCAEntities.MOON_LAMP.get(), level);
        if(level.isClientSide()){
            EntityManager.AddEntity(this);
        }
    }

    protected Vector4f targetColor = new Vector4f(1);
    protected Vector4f currentColor = new Vector4f(1);
    protected Vector4f prevColor = new Vector4f(1);
    protected PatchedPianoTileEntity trackingPiano;

    public boolean dirty = false;

    public Vector4f getColor(float pt){
        return prevColor.lerp(currentColor, pt, new Vector4f());
    }

    public void syncColor(Vector4f target){
        targetColor = target;
        //System.out.println("sync___");
        //dirty = false;
    }

    public void setColorR(float target){
        targetColor = new Vector4f(target, targetColor.y, targetColor.z, targetColor.w);
        //System.out.println(String.format("target: %.2f, %.2f, %.2f, %s",targetColor.x, targetColor.y,targetColor.z, level().isClientSide ? "C":"S"));
        dirty = true;
    }
    public void setColorG(float target){
        targetColor = new Vector4f(targetColor.x, target, targetColor.z, targetColor.w);
        dirty = true;
    }
    public void setColorB(float target){
        targetColor = new Vector4f(targetColor.x, targetColor.y, target, targetColor.w);
        dirty = true;
    }
    public void setColorA(float target){
        targetColor = new Vector4f(targetColor.x, targetColor.y, targetColor.z, target);
        dirty = true;
    }

    private static final List<ItemStack> EMPTY_LIST = Collections.emptyList();
    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return EMPTY_LIST;
    }

    public boolean shouldHandlePacket(){
        if(trackingPiano != null){
            return trackingPiano.pianoScreen == null;
        }
        return true;
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }


    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        //readColor(tag.getCompound("targetColor"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        //tag.put("targetColor", packColor());
    }



    private CompoundTag packColor(){
        CompoundTag col = new CompoundTag();
        col.putFloat("r", targetColor.x);
        col.putFloat("g", targetColor.y);
        col.putFloat("b", targetColor.z);
        col.putFloat("a", targetColor.w);
        return col;
    }

    protected void readColor(CompoundTag tag){
        if (tag == null) return;
        targetColor = new Vector4f(
                tag.getFloat("r"),
                tag.getFloat("g"),
                tag.getFloat("b"),
                tag.getFloat("a")
        );
    }

    @Override
    public void remove(RemovalReason p_276115_) {
        super.remove(p_276115_);
        EntityManager.RemoveEntity(this);
    }

    @Override
    public void tick() {
        this.setDeltaMovement(Vec3.ZERO);
        super.tick();

        //Client only
        if(level().isClientSide){
            if(dirty){
                PacketMgr.sendToServer(
                        new CP_UpdateLampColor(
                                getUUID(),
                                targetColor.x,
                                targetColor.y,
                                targetColor.z,
                                targetColor.w
                        )
                );
                dirty = false;
            }

            prevColor.set(currentColor);
            currentColor = currentColor.lerp(targetColor, 0.6f, new Vector4f());

            //System.out.println(String.format("target: %.2f, %.2f, %.2f",targetColor.x, targetColor.y,targetColor.z));
        }
        else {
            if(dirty){

            }
        }
    }

    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    public AttributeMap getAttributes() {
        return super.getAttributes();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes();
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return super.shouldRender(x, y, z);
    }


}
