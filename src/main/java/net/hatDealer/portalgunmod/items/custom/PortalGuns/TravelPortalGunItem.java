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

public class TravelPortalGunItem extends PortalgunItem {
    public TravelPortalGunItem(Properties pProperties) { super(pProperties); }

    @Override
    protected void Shoot(ArrowItem projectile, Player pPlayer, Level pLevel, ItemStack itemProjectile, ItemStack PortalGun) {
        //WIP - rework
        CompoundTag PortalGunNBT = PortalGun.getOrCreateTag();

        Vec3i PortalPos = PortalGunNBTReader.GetPosFromTravelersPortalGunNBT(PortalGunNBT, pPlayer.position());
        String PortalDimKey = PortalGunNBTReader.GetDimKeyFromTravelersPortalGunNBT(PortalGunNBT);

        AbstractArrow abstractArrow;
        if(projectile instanceof PortalProjectileItem p){
            abstractArrow = p.createArrow(pLevel, itemProjectile,
                    pPlayer, PortalDimKey, PortalPos, getPortalLifetime(), getPortalDisappear());
        }else{
            assert (projectile instanceof PortalProjectileItemUnstable);

            abstractArrow = ((PortalProjectileItemUnstable)projectile).createArrow(pLevel, itemProjectile, pPlayer);
        }

        abstractArrow.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0F, 3.0F, 0.0F);

        pLevel.addFreshEntity(abstractArrow);

        pPlayer.getCooldowns().addCooldown(this, 20);
    }
    @Override
    public int getPortalLifetime() { return 100; }
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {

        pTooltipComponents.add(
                Component.translatable("tooltip.portalgun").withStyle(ChatFormatting.DARK_GRAY));

        pTooltipComponents.add(
                Component.translatable("tooltip.portalgun.special.travel").withStyle(ChatFormatting.GRAY));

        pTooltipComponents.add(
                Component.translatable("tooltip.portalgun.portalduration").withStyle(ChatFormatting.GRAY)
                        .append(Component.literal((String.valueOf(getPortalLifetime()/20) + "s")).withStyle(ChatFormatting.GOLD)));

        pTooltipComponents.add(
                Component.translatable("tooltip.portalgun.portalpeople").withStyle(ChatFormatting.GRAY)
                        .append(Component.literal("NO").withStyle(ChatFormatting.DARK_RED)));

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
