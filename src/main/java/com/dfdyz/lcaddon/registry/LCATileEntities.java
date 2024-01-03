package com.dfdyz.lcaddon.registry;

import com.dfdyz.lcaddon.LCAddon;
import com.dfdyz.lcaddon.world.tileentity.AnimatedPianoTileEntity;
import com.dfdyz.lcaddon.world.tileentity.ElectronicPianoTileEntity;
import com.dfdyz.lcaddon.world.tileentity.PatchedPianoTileEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class LCATileEntities {
    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, LCAddon.MODID);

    public static final RegistryObject<BlockEntityType<AnimatedPianoTileEntity>> ANIMATED_PIANO;

    public static final RegistryObject<BlockEntityType<ElectronicPianoTileEntity>> ELECTRONIC_PIANO;

    static {
        /*
        PATCHED_PIANO = TILE_ENTITIES.register("patched_piano", () -> {
            return BlockEntityType.Builder.of(
                    PatchedPianoTileEntity::new,
                    LCABlocks.STEINWAY_D_PIANO.get()
            ).build(null);
        });*/

        ANIMATED_PIANO = TILE_ENTITIES.register("animated_piano", () -> {
            return BlockEntityType.Builder.of(
                    AnimatedPianoTileEntity::new,
                    LCABlocks.STEINWAY_D_PIANO.get()
            ).build(null);
        });

        ELECTRONIC_PIANO = TILE_ENTITIES.register("electronic_piano", () -> {
            return BlockEntityType.Builder.of(
                    ElectronicPianoTileEntity::new,
                    LCABlocks.ELECTRONIC_PIANO.get()
            ).build(null);
        });
    }
}
