package net.hatDealer.portalgunmod.items.custom.PortalProjectiles;

import net.hatDealer.portalgunmod.entity.custom.PortalProjectileUnstableEntity;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PortalProjectileItemUnstable extends ArrowItem {
    public PortalProjectileItemUnstable(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public PortalProjectileUnstableEntity createArrow(Level pLevel, ItemStack pStack, LivingEntity pShooter){
        return new PortalProjectileUnstableEntity(pShooter, pLevel);
    }
}
