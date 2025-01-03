package net.hatDealer.portalgunmod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hatDealer.portalgunmod.entity.custom.UnstablePortalLiquidProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.ResourceLocation;

public class UnstablePortalLiquidRender extends ThrownItemRenderer<UnstablePortalLiquidProjectile> {
    public UnstablePortalLiquidRender(EntityRendererProvider.Context pContext) {
        super(pContext, 1.0f, true);
    }
}
