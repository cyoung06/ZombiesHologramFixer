package kr.syeyoung.zombiesaddon;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = ZombiesAddon.MODID, version = ZombiesAddon.VERSION)
public class ZombiesAddon
{
    public static final String MODID = "zombies_hologrambug_fixer";
    public static final String VERSION = "1.4";
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        Keybinds.register();
        FMLCommonHandler.instance().bus().register(new EventListener());
    }
}
