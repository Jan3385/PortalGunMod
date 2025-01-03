package net.hatDealer.portalgunmod.entity.custom;

import net.hatDealer.portalgunmod.entity.ModEntities;
import net.hatDealer.portalgunmod.items.ModItems;
import net.hatDealer.portalgunmod.util.TeleportLogic;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class StablePortalLiquidProjectile extends ThrowableItemProjectile {
    public StablePortalLiquidProjectile(EntityType<? extends StablePortalLiquidProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public StablePortalLiquidProjectile(Level pLevel, LivingEntity pShooter) {
        super(ModEntities.UNSTABLE_PORTAL_PROJECTILE.get(), pShooter, pLevel);
    }

    public StablePortalLiquidProjectile(Level pLevel, double pX, double pY, double pZ) {
        super(ModEntities.UNSTABLE_PORTAL_PROJECTILE.get(), pX, pY, pZ, pLevel);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.UnstablePortalPotion.get();
    }


    private ParticleOptions getParticle() {
        ItemStack itemstack = this.getItemRaw();
        return (ParticleOptions)(itemstack.isEmpty() ? ParticleTypes.FALLING_OBSIDIAN_TEAR : new ItemParticleOption(ParticleTypes.ITEM, itemstack));
    }

    /**
     * Handles an entity event received from a {@link net.minecraft.network.protocol.game.ClientboundEntityEventPacket}.
     */
    public void handleEntityEvent(byte pId) {
        if (pId == 3) {
            ParticleOptions particleoptions = this.getParticle();

            for(int i = 0; i < 8; ++i) {
                this.level().addParticle(particleoptions, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }

    }

    /**
     * Called when the arrow hits an entity
     */
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        if (!this.level().isClientSide) {
            this.level().playSound((Player)null,
                    pResult.getLocation().x, pResult.getLocation().y, pResult.getLocation().z, SoundEvents.SPLASH_POTION_BREAK,
                    SoundSource.NEUTRAL, 1F, random.nextFloat() * 0.1F + 0.9F);
            this.discard();
            TeleportLogic.CreateRandomPortal(
                    this.position(),
                    this.level().dimension().location().toString(),
                    this.level(),
                    this.getMotionDirection(),
                    this.getYRot(),
                    this.getOwner(),
                    500
            );
        }
    }

    /**
     * Called when this EntityFireball hits a block or entity.
     */
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        if (!this.level().isClientSide) {
            this.level().playSound((Player)null,
                    pResult.getLocation().x, pResult.getLocation().y, pResult.getLocation().z, SoundEvents.SPLASH_POTION_BREAK,
                    SoundSource.NEUTRAL, 1F, random.nextFloat() * 0.1F + 0.9F);
            this.discard();
            TeleportLogic.CreateRandomPortal(
                    this.position(),
                    this.level().dimension().location().toString(),
                    this.level(),
                    this.getMotionDirection(),
                    this.getYRot(),
                    this.getOwner(),
                    500
            );
        }
    }
}
