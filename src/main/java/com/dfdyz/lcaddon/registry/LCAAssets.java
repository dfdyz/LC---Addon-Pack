package com.dfdyz.lcaddon.registry;

import com.dfdyz.lcaddon.LCAddon;
import com.dfdyz.lcaddon.client.models.custom.AnimatedQuads;
import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;

public class LCAAssets {

    public static final AnimatedQuads SteinwayD;
    public static final AnimatedQuads RolandFp30;

    public static final HashMap<Block, AnimatedQuads> PianoKeysModel = Maps.newHashMap();

    public static void Init(){

    }

    static {
        SteinwayD = AnimatedQuads.create(new ResourceLocation(LCAddon.MODID, "models/block/steinway_d274_keys.json"));
        RolandFp30 = AnimatedQuads.create(new ResourceLocation(LCAddon.MODID, "models/block/roland_fp30x_keys.json"));

        PianoKeysModel.put(LCABlocks.STEINWAY_D_PIANO.get(), SteinwayD);
        PianoKeysModel.put(LCABlocks.ELECTRONIC_PIANO.get(), RolandFp30);

    }




}
