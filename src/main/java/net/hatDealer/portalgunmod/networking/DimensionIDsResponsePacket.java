package net.hatDealer.portalgunmod.networking;

import net.hatDealer.portalgunmod.screens.TravelPortalGunScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class DimensionIDsResponsePacket {
    private final Set<ResourceKey<Level>> dimensions;

    public DimensionIDsResponsePacket(Set<ResourceKey<Level>> dimensions) {
        this.dimensions = dimensions;
    }

    public DimensionIDsResponsePacket(FriendlyByteBuf buffer) {
        this.dimensions = new HashSet<>();
        int size = buffer.readVarInt();

        for (int i = 0; i < size; i++) {
            ResourceLocation dimensionId = buffer.readResourceLocation();
            ResourceKey<Level> dimensionKey = ResourceKey.create(Registries.DIMENSION, dimensionId);
            dimensions.add(dimensionKey);
        }
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeVarInt(dimensions.size());
        for (ResourceKey<Level> dimension : dimensions) {
            buffer.writeResourceLocation(dimension.location());
        }
    }
    public static DimensionIDsResponsePacket decode(FriendlyByteBuf buffer) {
        return new DimensionIDsResponsePacket(buffer);
    }

    public static void handle(DimensionIDsResponsePacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if(Minecraft.getInstance().screen instanceof TravelPortalGunScreen s){
                for (ResourceKey<Level> dimensionKey : packet.dimensions) {
                    s.DimIDs.add(dimensionKey.location().toString());
                }
            }
        });
        context.setPacketHandled(true);
    }
}