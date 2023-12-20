package com.dfdyz.lcaddon.registry;

import com.dfdyz.lcaddon.LCAddon;
import com.dfdyz.lcaddon.world.block.*;
import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.ywzj.midi.all.AllTabs;

import java.util.LinkedHashMap;
import java.util.function.Supplier;

public class LCABlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, LCAddon.MODID);
    public static final LinkedHashMap<ResourceLocation, RegistryObject<Item>> BLOCK_ITEMS = Maps.newLinkedHashMap();
    //public static final RegistryObject<Block> TEST_PIANO;
    public static final RegistryObject<Block> ELECTRONIC_PIANO;
    public static final RegistryObject<Block> STEINWAY_D_PIANO;
    public static final RegistryObject<Block> EPIANO_CHAIR;
    public static final RegistryObject<Block> PIANO_CHAIR;

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, int stack) {
        RegistryObject<T> rt = BLOCKS.register(name, block);
        registerBlockItem(name, rt, stack);
        return rt;
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> rt = BLOCKS.register(name, block);
        registerBlockItem(name, rt, 1);
        return rt;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, int max_stack) {
        RegistryObject<Item> registry = LCAItems.ITEMS.register(name, () -> {
            return new BlockItem(block.get(), (new Item.Properties()).stacksTo(max_stack));
        });
        AllTabs.TAB_ITEMS.add(registry);
        BLOCK_ITEMS.put(block.getId(), registry);
        return registry;
    }

    static {
        /*
        TEST_PIANO = registerBlock("test_piano", () -> {
            return new PatchedPiano(BlockBehaviour.Properties.of()
                    .strength(1.0F)
            );
        });*/

        ELECTRONIC_PIANO = registerBlock("roland_fp30x", () -> {
            return new ElectronicPianoBlock(BlockBehaviour.Properties.of()
                    .strength(1.0F)
            );
        });

        STEINWAY_D_PIANO = registerBlock("steinway_d", () -> {
            return new SteinwayDPiano(BlockBehaviour.Properties.of()
                    .strength(1.0F).noOcclusion()
            );
        });

        EPIANO_CHAIR = registerBlock("epiano_chair_block", () -> {
            return new EPianoChairBlock(BlockBehaviour.Properties.of()
                    .strength(1.0F)
            );
        });

        PIANO_CHAIR = registerBlock("piano_chair_block", () -> {
            return new PianoChairBlock(BlockBehaviour.Properties.of()
                    .strength(1.0F)
            );
        });
    }
}
