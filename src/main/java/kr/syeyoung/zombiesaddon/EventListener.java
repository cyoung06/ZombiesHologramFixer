package kr.syeyoung.zombiesaddon;

import com.google.common.base.Predicate;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import javax.annotation.Nullable;
import java.util.*;

public class EventListener {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void interact(final FMLNetworkEvent.ClientConnectedToServerEvent event)
    {
        System.out.println("registered pipe");
        event.getManager().channel().pipeline().addLast(new ChannelOutboundHandlerAdapter() {
            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                try {
                    if (!ZHFState || Keybinds.ignoreZHF.isKeyDown()) {
                        super.write(ctx, msg, promise);
                        return;
                    }
                    if (msg instanceof CPacketUseEntity) {
                        if (((CPacketUseEntity) msg).getAction() == CPacketUseEntity.Action.ATTACK) {
                            super.write(ctx, msg, promise);
                            return;
                        }
                        ItemStack item = Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem();
                        if (item != null && item.getItem() == Item.REGISTRY.getObjectById(267)) {
                            super.write(ctx, msg, promise);
                            return;
                        }
                        Entity clicked = ((CPacketUseEntity) msg).getEntityFromWorld(Minecraft.getMinecraft().thePlayer.worldObj);
                        if (!(clicked instanceof EntityArmorStand)) {
                            super.write(ctx, msg, promise);
                            return;
                        }
                        super.write(ctx, new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND), promise);
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
            Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString("Toggled ZHF to "+(ZHFState ? "on" : "off")));
        }
    }


    @SubscribeEvent
    public void onRenderGui(RenderGameOverlayEvent.Post event)
    {
        try {
            if (event.getType() != RenderGameOverlayEvent.ElementType.EXPERIENCE) return;
            String str = "ZHF " + (ZHFState ? "on" : "off");
            FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
            fr.drawStringWithShadow(str, new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth() - fr.getStringWidth(str), 0, 0xFFFF55);

//            Scoreboard sb = Minecraft.getMinecraft().thePlayer.getWorldScoreboard();
//            ScoreObjective so = sb.getObjectiveInDisplaySlot(1);
//            if (so != null && so.getDisplayName().replaceAll("§.", "").equalsIgnoreCase("ZOMBIES")) {
//                ScoreObjective health = sb.getObjectiveInDisplaySlot(Scoreboard.getObjectiveDisplaySlotNumber("belowName"));
//                HashMap<String, String> states = new HashMap<String, String>();
//                for (Score sc : sb.getSortedScores(so)) {
//                    ScorePlayerTeam scoreplayerteam1 = sb.getPlayersTeam(sc.getPlayerName());
//                    String s1 = ScorePlayerTeam.formatPlayerName(scoreplayerteam1, sc.getPlayerName());
//                    if (sc.getScorePoints() >= 7 && sc.getScorePoints() <= 10) {
//                        String[] strs = s1.replaceAll("§.", "").split(":");
//                        states.put(strs[0].replaceAll("[^a-zA-Z0-9]", "").trim(), strs[1].replaceAll("[^a-zA-Z0-9]", "").trim());
//                    }
//                }
//                Collection<Score> healths = sb.getSortedScores(health);
//
//                Map<String, Integer> healthsMap = new HashMap<String, Integer>();
//                for (Score sc : healths) {
//                    healthsMap.put(sc.getPlayerName(), sc.getScorePoints());
//                }
//
//                int i = new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight() / 2 - 24;
//                for (String pl : healthsMap.keySet()) {
//                    Integer healthInt = healthsMap.get(pl);
//                    String state = states.get(pl);
//
//                    String msg = "REVIVE".equalsIgnoreCase(state) ? "REVIVE" :
//                            "DEAD".equalsIgnoreCase(state) ? "DEAD" : String.valueOf(healthInt);
//                    System.out.println(pl + " - " + state);
//                    boolean rev = "REVIVE".equalsIgnoreCase(state);
//                    boolean dead = "DEAD".equalsIgnoreCase(state);
//
//                    drawString(pl + ": " + msg, i += 8, dead ? 0x000000 :
//                            (healthInt < 3 || rev) ? 0xAA0000 :
//                                    healthInt < 7 ? 0xFF5555 :
//                                            healthInt < 10 ? 0xFFAA00 : 0xFFFF55);
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void drawString(String str, int y, int color) {
        FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
        fr.drawStringWithShadow(str, 5, y, color);
    }
}
