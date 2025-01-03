package net.hatDealer.portalgunmod.networking;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ItemNBTUpdatePacket {
    private final CompoundTag nbtData;

    public ItemNBTUpdatePacket(CompoundTag nbtData) {
        this.nbtData = nbtData;
    }

    public static void encode(ItemNBTUpdatePacket packet, FriendlyByteBuf buffer) {
        buffer.writeNbt(packet.nbtData);
    }

    public static ItemNBTUpdatePacket decode(FriendlyByteBuf buffer) {
        return new ItemNBTUpdatePacket(buffer.readNbt());
    }

    public static void handle(ItemNBTUpdatePacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            // Handle the packet on the main thread
            Player player = context.getSender();
            if (player != null) {
                ItemStack heldItem = player.getMainHandItem();
                if (!heldItem.isEmpty()) {
                    heldItem.setTag(packet.nbtData);
                }
            }
        });
        context.setPacketHandled(true);
    }
}