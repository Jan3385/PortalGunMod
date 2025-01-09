package net.hatDealer.portalgunmod.items.custom.PortalGuns;

import net.hatDealer.portalgunmod.items.custom.PortalProjectiles.PortalProjectileItem;
import net.hatDealer.portalgunmod.items.custom.PortalProjectiles.PortalProjectileItemUnstable;
import net.hatDealer.portalgunmod.items.custom.PortalgunItem;
import net.hatDealer.portalgunmod.util.PortalGunNBTReader;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NormalPortalGunItem extends PortalgunItem {
    public NormalPortalGunItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected void Shoot(ArrowItem projectile, Player pPlayer, Level pLevel, ItemStack itemProjectile, ItemStack PortalGun) {
        CompoundTag PortalGunNBT = PortalGun.getOrCreateTag();

        Vec3i PortalPos = PortalGunNBTReader.GetPosFromPortalGunNBT(PortalGunNBT, pPlayer.position());
        String PortalDimKey = PortalGunNBTReader.GetDimKeyDromPortalGunNBT(PortalGunNBT);

        AbstractArrow abstractArrow;
        if(projectile instanceof PortalProjectileItem p){
            abstractArrow = p.createArrow(pLevel, itemProjectile,
                    pPlayer, PortalDimKey, PortalPos, getPortalLifetime(PortalGun), getPortalDisappear(PortalGun));
        }else{
            assert (projectile instanceof PortalProjectileItemUnstable);

            abstractArrow = ((PortalProjectileItemUnstable)projectile).createArrow(pLevel, itemProjectile, pPlayer);
        }

        abstractArrow.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0F, 3.0F, 0.0F);

        pLevel.addFreshEntity(abstractArrow);

        pPlayer.getCooldowns().addCooldown(this, 20);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltip.portalgun").withStyle(ChatFormatting.DARK_GRAY));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
