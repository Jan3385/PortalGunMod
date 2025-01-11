package net.hatDealer.portalgunmod.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.Set;
import java.util.function.Supplier;

public class RequestDimensionsPacket {
    public static void encode(RequestDimensionsPacket packet, FriendlyByteBuf buffer) {
        // No data is needed for the request packet, just send an empty packet
    }

    public static RequestDimensionsPacket decode(FriendlyByteBuf buffer) {
        return new RequestDimensionsPacket();  // No data to decode
    }

    public static void handle(RequestDimensionsPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            // Handle the request on the server side
            ServerPlayer player = context.getSender();
            if (player != null) {
                // Send the response with dimension IDs
                sendDimensionsToClient(player);
            }
        });
        context.setPacketHandled(true);
    }

    private static void sendDimensionsToClient(ServerPlayer player) {
        // Get all dimension keys
        Set<ResourceKey<Level>> dimensions = player.server.levelKeys();

        // Send the response packet
        DimensionIDsResponsePacket responsePacket = new DimensionIDsResponsePacket(dimensions);
        // Send the response to the client
        ModNetworking.CHANNEL.sendTo(responsePacket, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }
}