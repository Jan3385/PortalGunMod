package net.hatDealer.portalgunmod.datagen.event;

import net.hatDealer.portalgunmod.PortalGunMod;
import net.hatDealer.portalgunmod.items.ModItems;
import net.hatDealer.portalgunmod.screens.NormalPortalGunScreen;
import net.hatDealer.portalgunmod.screens.PortalScreen;
import net.hatDealer.portalgunmod.screens.TravelPortalGunScreen;
import net.hatDealer.portalgunmod.util.ModKeybinds;
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
        Minecraft mc = Minecraft.getInstance();
        if(mc.player == null) return;
        //if(mc.screen != null && !(mc.screen instanceof PortalScreen)) return;

        if (ModKeybinds.OpenUI.consumeClick()){
            if(mc.player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == ModItems.PortalGun.get()){
                mc.setScreen(new NormalPortalGunScreen(Component.translatable("screen.portal.title"),
                        mc.player.getItemInHand(InteractionHand.MAIN_HAND)));
            }
            else if(mc.player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == ModItems.TravelPortalGun.get()){
                mc.setScreen(new TravelPortalGunScreen(Component.translatable("screen.portal.title"),
                        mc.player.getItemInHand(InteractionHand.MAIN_HAND)));
            }
        }
    }
}

//@SubscribeEvent
//public static void registerAttributres(EntityAttributeCreationEvent event){
//    event.put(ModEntities.PORTAL_ENTITY.get(), PortalEntity.createAttributes().build());
//}