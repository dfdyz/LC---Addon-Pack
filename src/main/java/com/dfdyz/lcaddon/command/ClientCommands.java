package com.dfdyz.lcaddon.command;

import com.dfdyz.lcaddon.LCAddon;
import com.dfdyz.lcaddon.client.models.entity.MoonLampModel;
import com.dfdyz.lcaddon.utils.TestUtils;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = LCAddon.MODID, value = Dist.CLIENT)
public class ClientCommands {


    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void register(RegisterClientCommandsEvent event) {
        /*
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal("lca").then(
                Commands.literal("testSynth").executes(
                        (args)->{
                            TestUtils.TestSynthSound();
                            return 0;
                        })
                )
        );*/
    }


}
