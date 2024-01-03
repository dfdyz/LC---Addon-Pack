package com.dfdyz.lcaddon.client.gui;

import com.dfdyz.lcaddon.client.gui.screens.ElectronicPianoScreen;
import com.dfdyz.lcaddon.client.gui.screens.PatchedPianoScreen;
import com.dfdyz.lcaddon.world.tileentity.ElectronicPianoTileEntity;
import com.dfdyz.lcaddon.world.tileentity.PatchedPianoTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import org.ywzj.midi.gui.screen.ClavichordScreen;
import org.ywzj.midi.instrument.Instrument;
import org.ywzj.midi.util.ComponentUtils;
import org.ywzj.midi.util.MathUtils;

import java.util.UUID;

import static net.minecraft.Util.NIL_UUID;

public class ScreenMgr {
    public static void openPianoScreen(Instrument instrument, BlockPos pos, PatchedPianoTileEntity pianoBlockEntity, int distance) {
        if (checkDistance(pos, 4)) {
            if (pianoBlockEntity.pianoScreen == null) {
                pianoBlockEntity.pianoScreen = new PatchedPianoScreen(instrument, pos, ComponentUtils.literal("钢琴"),"c4", "b6");
            }

            Minecraft.getInstance().tell(() -> {
                Minecraft.getInstance().setScreen(pianoBlockEntity.pianoScreen);
            });
        }
    }

    public static void openEPianoScreen(Instrument instrument, BlockPos pos, ElectronicPianoTileEntity pianoBlockEntity) {
        if (checkDistance(pos, 3)) {
            if (pianoBlockEntity.pianoScreen == null) {
                pianoBlockEntity.pianoScreen = new ElectronicPianoScreen(instrument, pos, ComponentUtils.literal("电钢琴"),"c4", "b6");
            }

            Minecraft.getInstance().tell(() -> {
                Minecraft.getInstance().setScreen(pianoBlockEntity.pianoScreen);
            });
        }
    }

    private static boolean checkDistance(BlockPos pos, int distance) {
        Player player = Minecraft.getInstance().player;
        if (player != null && MathUtils.distance(player.getX(), player.getY(), player.getZ(), pos.getX(), pos.getY(), pos.getZ()) > distance) {
            player.sendMessage(ComponentUtils.translatable("info.ywzj_midi.warn_2"), NIL_UUID);
            return false;
        } else {
            return true;
        }
    }
}
