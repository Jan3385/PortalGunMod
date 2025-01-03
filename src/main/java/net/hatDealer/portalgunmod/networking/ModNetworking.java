package net.hatDealer.portalgunmod.networking;

import net.hatDealer.portalgunmod.PortalGunMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetworking {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(PortalGunMod.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        int id = 0;
        CHANNEL.registerMessage(id++, ItemNBTUpdatePacket.class, ItemNBTUpdatePacket::encode, ItemNBTUpdatePacket::decode, ItemNBTUpdatePacket::handle);
    }
}
