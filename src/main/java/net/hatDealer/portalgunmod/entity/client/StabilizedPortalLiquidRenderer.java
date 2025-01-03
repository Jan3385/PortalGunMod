package net.hatDealer.portalgunmod.entity.client;

import net.hatDealer.portalgunmod.entity.custom.StablePortalLiquidProjectile;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

public class StabilizedPortalLiquidRenderer extends ThrownItemRenderer<StablePortalLiquidProjectile> {
    public StabilizedPortalLiquidRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, 1.0f, true);
    }
}
