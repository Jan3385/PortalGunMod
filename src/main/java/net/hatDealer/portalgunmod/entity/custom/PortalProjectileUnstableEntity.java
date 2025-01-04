package net.hatDealer.portalgunmod.entity.custom;

import net.hatDealer.portalgunmod.entity.ModEntities;
import net.hatDealer.portalgunmod.util.TeleportLogic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class PortalProjectileUnstableEntity extends AbstractArrow {
    private Vec3 SpawnPos;
    private int ProjectileLifeLength = 800;
    private static final int maxDistance = 14;

    public PortalProjectileUnstableEntity(LivingEntity shooter, Level pLevel){
        super(ModEntities.PORTAL_PROJECTILE.get(), shooter, pLevel); //TODO: change PORTAL_PROJECTILE
        SpawnPos = this.position().add(0,0,0); //create a new object of the vector
    }
    @Override
    protected ItemStack getPickupItem() {
        return null;
    }
    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        if(!this.level().isClientSide){
            TeleportLogic.ChaoticExplosion(
                    pResult.getLocation(),
                    this.level(),
                    this.getMotionDirection(),
                    this.getYRot(),
                    this.getOwner());
            discard();
        }
    }
    @Override
    protected void onHitEntity(EntityHitResult pResult){
        //teleport logic
        //do not teleport unteleportable entities
        if(pResult.getEntity().getClass() != PortalEntity.class &&
                pResult.getEntity().getClass() != this.getClass() &&
                pResult.getEntity().canChangeDimensions()) {

            if(this.level().isClientSide){
                this.level().addParticle(ParticleTypes.SOUL, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
                return;
            }

            if(!this.level().isClientSide){
                String DimToken = TeleportLogic.GetRandomVanillaDimToken();
                ResourceLocation DimLocation = new ResourceLocation(DimToken);
                ResourceKey<Level> dimensionKey = ResourceKey.create(Registries.DIMENSION, DimLocation);
                ServerLevel serverLevel = getServer().getLevel(dimensionKey);

                Vec3 randomVec3Pos = this.position().add(random.nextInt(-4000, 4000), 0, random.nextInt(-4000, 4000));
                BlockPos randomPos = new BlockPos((int)randomVec3Pos.x, (int)randomVec3Pos.y, (int)randomVec3Pos.z);

                BlockPos pos = TeleportLogic.FindViablePositionFor(serverLevel, randomPos);
                TeleportLogic.TeleportEntity(pResult.getEntity(), serverLevel, pos);
            }
        }

        if(!this.level().isClientSide){
            TeleportLogic.ChaoticExplosion(
                    pResult.getLocation(),
                    this.level(),
                    this.getMotionDirection(),
                    this.getYRot(),
                    this.getOwner());
            discard();
        }
    }
    public void tick() {
        super.tick();
        //client particles
        if (this.level().isClientSide && !this.inGround) {
            this.level().addParticle(ParticleTypes.PORTAL, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
        }
        //kill projectile after some time, spawns portal after going too far
        if(!this.level().isClientSide){
            ProjectileLifeLength--;
            if(ProjectileLifeLength <= 0) this.discard();


            if(SpawnPos.distanceTo(this.position()) >= maxDistance){
                TeleportLogic.ChaoticExplosion(
                        this.position(),
                        this.level(),
                        this.getMotionDirection(),
                        this.getYRot(),
                        this.getOwner());
                discard();
            }
        }
    }
    @Override
    protected float getWaterInertia() {
        return 1F;
    }
    @Override
    public boolean isNoGravity(){ return true; }
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);

        this.SpawnPos = new Vec3(pCompound.getDouble("sPosX"), pCompound.getDouble("sPosY"), pCompound.getDouble("sPosZ"));
    }
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);

        pCompound.putDouble("sPosX", this.SpawnPos.x);
        pCompound.putDouble("sPosY", this.SpawnPos.y);
        pCompound.putDouble("sPosZ", this.SpawnPos.z);
    }
}
