package kr.syeyoung.zombieshologram;

import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = ZombiesHoloFixer.MODID, version = ZombiesHoloFixer.VERSION)
public class ZombiesHoloFixer
{
    public static final String MODID = "zombies_hologrambug_fixer";
    public static final String VERSION = "1.0";
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        Keybinds.register();
        FMLCommonHandler.instance().bus().register(new EventListener());
    }
}
