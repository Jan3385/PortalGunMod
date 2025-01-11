package net.hatDealer.portalgunmod.util;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.glfw.GLFW;

public class ModKeybinds {

    public static final KeyMapping OpenUI = new KeyMapping(
            "key.portalgunmod.openui", // Translation key
            GLFW.GLFW_KEY_R,              // Default key (G)
            "key.categories.portalgunmod" // Key category
    );

    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(OpenUI);
    }
}
