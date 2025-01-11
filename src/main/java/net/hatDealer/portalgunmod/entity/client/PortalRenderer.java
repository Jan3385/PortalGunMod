package net.hatDealer.portalgunmod.entity.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.hatDealer.portalgunmod.PortalGunMod;
import net.hatDealer.portalgunmod.entity.custom.PortalEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

//public class PortalRenderer extends LivingEntityRenderer<PortalEntity, PortalModel<PortalEntity>> {
@OnlyIn(Dist.CLIENT)
public class PortalRenderer extends EntityRenderer<PortalEntity> {
    private final PortalModel model;
    private float size = 1;
    private static final int PortalAnimSpeed = 3;
    private static final ResourceLocation PortalTexture = new ResourceLocation(PortalGunMod.MODID, "textures/entity/portal-backup.png");
    public PortalRenderer(EntityRendererProvider.Context pContext) {

        //super(pContext, new PortalModel<>(pContext.bakeLayer(ModModelLayers.PORTAL_LAYER)), 0f);
        super(pContext);
        this.model = new PortalModel(pContext.bakeLayer(ModModelLayers.PORTAL_LAYER));
    }

    public void render(PortalEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        if(!pEntity.isInitialised()) return;

        pPoseStack.pushPose();

        size = 1;


        if(pEntity.GetLifeTime() <= PortalAnimSpeed) size = (pEntity.GetLifeTime()+pPartialTicks)/PortalAnimSpeed;
        else if(pEntity.getRemainingTime() <= PortalAnimSpeed) size = Math.max((pEntity.getRemainingTime()-pPartialTicks)/PortalAnimSpeed, 0);

        Quaternionf rotation = new Quaternionf();
        //order: D-U-N-S-W-E
        if(pEntity.get3dDirection().get3DDataValue() <= 0){
            rotation.rotateX((float)(Math.PI*3)/2f);
            pPoseStack.scale(1 * size,1 * size, 0.5f * size);
            pPoseStack.translate(0,1f, 0.50f); // x 0.5f z
        } else if (pEntity.get3dDirection().get3DDataValue() == 1) {
            rotation.rotateX((float)Math.PI/2);
            pPoseStack.scale(1 * size,1, 0.5f * size);
            pPoseStack.translate(0,1f, -0.50f);

        }else{
            pPoseStack.translate(0,0.5f + (0.5 - size/2), 0);
            pPoseStack.scale(1 * size,1 * size, 1 * size);
        }

        if(pEntity.get3dDirection().get3DDataValue() > 1) rotation.rotateY(degToRads(pEntityYaw));

        pPackedLight = 255;
        this.shadowStrength = 0;
        pPoseStack.mulPose(rotation);

        VertexConsumer vertexconsumerUniversal = pBuffer.getBuffer(this.getRenderType(false));
        this.model.renderToBuffer(pPoseStack, vertexconsumerUniversal, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1F);

        pPoseStack.pushPose();
        pPoseStack.scale(.98f, .98f, .98f);
        vertexconsumerUniversal = pBuffer.getBuffer(this.getRenderType(true));
        this.model.renderToBuffer(pPoseStack, vertexconsumerUniversal, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1F);

        pPoseStack.popPose();
        pPoseStack.popPose();

        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(PortalEntity pEntity) {
        return PortalTexture;
    }

    protected RenderType getRenderType(boolean universalRendering) {
        if(universalRendering) return RenderType.entitySolid(PortalTexture);
        return RenderType.endPortal();
    }

    public float degToRads(float deg){
        return (float) (deg*Math.PI)/180;
    }

    @Override
    protected boolean shouldShowName(PortalEntity pEntity) { return false; }
    protected int getBlockLightLevel(PortalEntity pEntity, BlockPos pPos) { return 15;}
}

