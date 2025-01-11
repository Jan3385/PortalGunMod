package net.hatDealer.portalgunmod.datagen.event;

import net.hatDealer.portalgunmod.PortalGunMod;
import net.hatDealer.portalgunmod.entity.ModEntities;
import net.hatDealer.portalgunmod.entity.client.*;
import net.hatDealer.portalgunmod.items.ModItems;
import net.hatDealer.portalgunmod.items.custom.PortalgunItem;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = PortalGunMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {
    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(ModModelLayers.PORTAL_PROJECTILE_LAYER, PortalProjectileModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.PORTAL_LAYER, PortalModel::createBodyLayer);
    }
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {
        EntityRenderers.register(ModEntities.PORTAL_PROJECTILE.get(), PortalProjectileRenderer::new);
        EntityRenderers.register(ModEntities.UNSTABLE_PORTAL_PROJECTILE.get(), UnstablePortalLiquidRender::new);
        EntityRenderers.register(ModEntities.STABLE_PORTAL_PROJECTILE.get(), StabilizedPortalLiquidRenderer::new);
        EntityRenderers.register(ModEntities.PORTAL_ENTITY.get(), PortalRenderer::new);
    }
    @SubscribeEvent
    public static void onFMLClientSetup(final FMLClientSetupEvent event)
    {
        event.enqueueWork(() ->
        {
            ItemProperties.register( ModItems.PortalGun.get(), new ResourceLocation("charged"),
                    (itemStack, clientLevel, livingEntity, numberArg) -> PortalgunItem.getChargeLevel(itemStack, livingEntity, numberArg));
            ItemProperties.register( ModItems.TravelPortalGun.get(), new ResourceLocation("charged"),
                    (itemStack, clientLevel, livingEntity, numberArg) -> PortalgunItem.getChargeLevel(itemStack, livingEntity, numberArg));
        });
    }

}
