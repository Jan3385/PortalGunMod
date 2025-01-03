package net.hatDealer.portalgunmod.datagen.event;

import net.hatDealer.portalgunmod.PortalGunMod;
import net.hatDealer.portalgunmod.items.ModItems;
import net.hatDealer.portalgunmod.screens.NormalPortalGunScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PortalGunMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class ModEventBusEvents {
    @SubscribeEvent
    public static void onKeyInputEvent(InputEvent.Key event){
        if(Minecraft.getInstance().player == null) return;
        if(Minecraft.getInstance().screen != null) return;
        if(event.getKey() == 82 && event.getAction() == 0){
            if(Minecraft.getInstance().player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == ModItems.PortalGun.get()){
                Minecraft.getInstance().setScreen(new NormalPortalGunScreen(Component.translatable("screen.portal.title"),
                        Minecraft.getInstance().player.getItemInHand(InteractionHand.MAIN_HAND)));
            }
        }
    }
}

//@SubscribeEvent
//public static void registerAttributres(EntityAttributeCreationEvent event){
//    event.put(ModEntities.PORTAL_ENTITY.get(), PortalEntity.createAttributes().build());
//}