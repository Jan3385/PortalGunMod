package net.hatDealer.portalgunmod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hatDealer.portalgunmod.PortalGunMod;
import net.hatDealer.portalgunmod.entity.custom.PortalEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

//public class PortalRenderer extends LivingEntityRenderer<PortalEntity, PortalModel<PortalEntity>> {
@OnlyIn(Dist.CLIENT)
public class PortalRenderer extends EntityRenderer<PortalEntity> {
    private final PortalModel model;
    private boolean renderedFirstTick = false;
    private float size = 1;
    public PortalRenderer(EntityRendererProvider.Context pContext) {

        //super(pContext, new PortalModel<>(pContext.bakeLayer(ModModelLayers.PORTAL_LAYER)), 0f);
        super(pContext);
        this.model = new PortalModel(pContext.bakeLayer(ModModelLayers.PORTAL_LAYER));
    }

    public void render(PortalEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        if(!pEntity.isInitialised()) return;

        pPoseStack.pushPose();

        if(pEntity.getRawSize() <= 1 && pEntity.getRemainingTime() > 150) pEntity.IncrementSize(pPartialTicks/5f);
        else if(pEntity.getRemainingTime() <= 5) pEntity.DecrementSize(pPartialTicks/5f);

        size = pEntity.getSize();

        Quaternionf rotation = new Quaternionf();
        //order: D-U-N-S-W-E
        if(pEntity.get3dDirection().get3DDataValue() <= 0){
            rotation.rotateX((float)(Math.PI*3)/2f);
            pPoseStack.scale(1 * size,1, 0.5f * size);
            pPoseStack.translate(0,0.5f, 0.50f);
        } else if (pEntity.get3dDirection().get3DDataValue() == 1) {
            rotation.rotateX((float)Math.PI/2);
            pPoseStack.scale(1 * size,1, 0.5f * size);
            pPoseStack.translate(0,0.5f, -0.50f);

        }else{
            pPoseStack.translate(0,0.5f + (0.5 - size/2), 0);
            pPoseStack.scale(1 * size,1 * size, 1 * size);
        }

        if(pEntity.get3dDirection().get3DDataValue() > 1) rotation.rotateY(degToRads(pEntityYaw));

        pPackedLight = 255;
        this.shadowStrength = 0;
        pPoseStack.mulPose(rotation);
        //VertexConsumer vertexconsumer = ItemRenderer.getFoilBufferDirect(pBuffer, this.model.renderType(this.getTextureLocation(pEntity)), false, false);
        VertexConsumer vertexconsumer = pBuffer.getBuffer(this.getRenderType());
        this.model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.1F);

        pPoseStack.popPose();

        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(PortalEntity pEntity) {
        return new ResourceLocation(PortalGunMod.MODID, "textures/entity/portal.png");
    }
    @Nullable
    protected RenderType getRenderType() {
        return RenderType.endPortal();
    }

    public float degToRads(float deg){
        return (float) (deg*Math.PI)/180;
    }

    @Override
    protected boolean shouldShowName(PortalEntity pEntity) { return false; }
    protected int getBlockLightLevel(PortalEntity pEntity, BlockPos pPos) { return 15;}
}

