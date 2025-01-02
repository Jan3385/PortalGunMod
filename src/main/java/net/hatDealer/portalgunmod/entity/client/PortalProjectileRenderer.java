package net.hatDealer.portalgunmod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hatDealer.portalgunmod.PortalGunMod;
import net.hatDealer.portalgunmod.entity.custom.PortalProjectileEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

//ArrowRenderer works instead od EntityRenderer
public class PortalProjectileRenderer extends EntityRenderer<PortalProjectileEntity> {

    private final PortalProjectileModel model;
    public PortalProjectileRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model =  new PortalProjectileModel(pContext.bakeLayer(ModModelLayers.PORTAL_PROJECTILE_LAYER));
    }

    @Override
    public void render(PortalProjectileEntity entity, float entityYaw, float partialTicks, PoseStack pPoseStack, MultiBufferSource buffer, int packedLight) {
        pPoseStack.pushPose();
        //pPoseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
        //pPoseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot()) + 90.0F));
        VertexConsumer vertexconsumer = ItemRenderer.getFoilBufferDirect(buffer, this.model.renderType(this.getTextureLocation(entity)), true, false);
        this.model.renderToBuffer(pPoseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.1F);
        pPoseStack.popPose();

        super.render(entity, entityYaw, partialTicks, pPoseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(PortalProjectileEntity pEntity) {
        return new ResourceLocation(PortalGunMod.MODID, "textures/entity/portal_projectile.png");
    }
}
