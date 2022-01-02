package kr.syeyoung.zombieshologram;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.lwjgl.Sys;

import java.awt.*;

public class EventListener {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void interact(final FMLNetworkEvent.ClientConnectedToServerEvent event)
    {
        System.out.println("registered pipe");
        event.manager.channel().pipeline().addLast(new ChannelOutboundHandlerAdapter() {
            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                try {
                    if (!ZHFState) {
                        super.write(ctx, msg, promise);
                        return;
                    }
                    if (msg instanceof C02PacketUseEntity) {
                        if (((C02PacketUseEntity) msg).getAction() == C02PacketUseEntity.Action.ATTACK) {
                            super.write(ctx, msg, promise);
                            return;
                        }
                        Entity clicked = ((C02PacketUseEntity) msg).getEntityFromWorld(Minecraft.getMinecraft().thePlayer.worldObj);
                        if (!(clicked instanceof EntityArmorStand)) {
                            super.write(ctx, msg, promise);
                            return;
                        }
                        ItemStack item = Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem();
                        if (item != null && item.getItem() == Item.getItemById(267)) {
                            super.write(ctx, msg, promise);
                            return;
                        }
                        super.write(ctx, new C08PacketPlayerBlockPlacement(item), promise);
                    } else {
                        super.write(ctx, msg, promise);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    super.write(ctx, msg, promise);
                }
            }
        });
    }

    public static volatile boolean ZHFState = false;

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event)
    {
        if (Keybinds.toggleZHF.isPressed())
        {
            ZHFState = !ZHFState;
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Toggled ZHF to "+(ZHFState ? "on" : "off")));
        }
    }


    @SubscribeEvent
    public void onRenderGui(RenderGameOverlayEvent.Post event)
    {
        if (event.type != RenderGameOverlayEvent.ElementType.EXPERIENCE) return;
        String str = "ZHF "+(ZHFState ? "on" : "off");
        FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
        fr.drawStringWithShadow(str, new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth() - fr.getStringWidth(str), 0, 0xFFFF55);
    }
}
