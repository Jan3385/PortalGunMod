package net.hatDealer.portalgunmod.items.custom;

import net.hatDealer.portalgunmod.entity.custom.PortalProjectileEntity;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class PortalProjectileItem extends ArrowItem{
    public PortalProjectileItem(Properties pProperties) {
        super(pProperties);
    }

    public PortalProjectileEntity createArrow(Level pLevel, ItemStack pStack, LivingEntity pShooter, String DestinationDimKey, Vec3i DestinationPos, int portalLifeLength, boolean dissapearsAfterUse){
        return new PortalProjectileEntity(pShooter, pLevel, DestinationDimKey, DestinationPos, portalLifeLength, dissapearsAfterUse);
    }
}
