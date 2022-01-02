package kr.syeyoung.zombiesaddon;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class Keybinds
{
    public static KeyBinding toggleZHF;
    public static KeyBinding ignoreZHF;
 
    public static void register()
    {
        toggleZHF = new KeyBinding("key.toggleZHF", Keyboard.KEY_J, "key.categories.zhf");
        ignoreZHF = new KeyBinding("key.ignoreZHF", Keyboard.KEY_TAB, "key.categories.zhf");
 
        ClientRegistry.registerKeyBinding(toggleZHF);
        ClientRegistry.registerKeyBinding(ignoreZHF);
    }
}