package com.dfdyz.lcaddon.network;

import com.dfdyz.lcaddon.LCAddon;
import com.dfdyz.lcaddon.network.ClientPack.CP_UpdateAnimatedPiano;
import com.dfdyz.lcaddon.network.ServerPack.SP_UpdateAnimatedPiano;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public class PacketMgr {
    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(LCAddon.MODID, LCAddon.MODID+"_channel")).networkProtocolVersion(() -> {
        return "1.1";
    }).clientAcceptedVersions("1.1"::equals).serverAcceptedVersions("1.1"::equals).simpleChannel();

    public PacketMgr() {
    }

    public static <MSG> void SendToServer(MSG msg){
        CHANNEL.sendToServer(msg);
    }


    public static <MSG> void sendToClient(MSG message, PacketDistributor.PacketTarget packetTarget) {
        CHANNEL.send(packetTarget, message);
    }

    public static <MSG> void sendToAll(MSG message) {
        sendToClient(message, PacketDistributor.ALL.noArg());
    }

    public static <MSG> void sendToAllPlayerTrackingThisEntity(MSG message, Entity entity) {
        sendToClient(message, PacketDistributor.TRACKING_ENTITY.with(() -> entity));
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        sendToClient(message, PacketDistributor.PLAYER.with(() -> player));
    }

    public static <MSG> void sendToAllPlayerTrackingThisEntityWithSelf(MSG message, ServerPlayer entity) {
        sendToPlayer(message, entity);
        sendToClient(message, PacketDistributor.TRACKING_ENTITY.with(() -> entity));
    }

    public static <MSG> void sendToAllPlayerTrackingThisBlock(MSG message, BlockEntity te) {
        sendToClient(message, PacketDistributor.TRACKING_CHUNK.with(() -> te.getLevel().getChunkAt(te.getBlockPos())));
    }

    private static int index = 0;

    public static void Init(){
        CHANNEL.registerMessage(index++, CP_UpdateAnimatedPiano.class, CP_UpdateAnimatedPiano::encode, CP_UpdateAnimatedPiano::decode, CP_UpdateAnimatedPiano::onServerMessageReceived, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(index++, SP_UpdateAnimatedPiano.class, SP_UpdateAnimatedPiano::encode, SP_UpdateAnimatedPiano::decode, SP_UpdateAnimatedPiano::onClientMessageReceived, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }
}
