package kr.syeyoung.zombieshologram;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class Keybinds
{
    public static KeyBinding toggleZHF;
 
    public static void register()
    {
        toggleZHF = new KeyBinding("key.toggleZHF", Keyboard.KEY_J, "key.categories.zhf");
 
        ClientRegistry.registerKeyBinding(toggleZHF);
    }
}