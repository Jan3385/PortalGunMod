package net.hatDealer.portalgunmod.entity.client;

import net.hatDealer.portalgunmod.PortalGunMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ModModelLayers {
    public static final ModelLayerLocation PORTAL_PROJECTILE_LAYER = new ModelLayerLocation(
            new ResourceLocation(PortalGunMod.MODID, "portal_projectile_layer"), "main");
    public static final ModelLayerLocation PORTAL_LAYER = new ModelLayerLocation(
            new ResourceLocation(PortalGunMod.MODID, "portal_layer"), "main");
}
