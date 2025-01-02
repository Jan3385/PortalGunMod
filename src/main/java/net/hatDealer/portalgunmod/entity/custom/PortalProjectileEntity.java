package net.hatDealer.portalgunmod.entity.custom;

import net.hatDealer.portalgunmod.entity.ModEntities;
import net.hatDealer.portalgunmod.items.ModItems;
import net.hatDealer.portalgunmod.util.TeleportLogic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;


public class PortalProjectileEntity extends AbstractArrow {
    private int portalHeight = 2;
    private int portalWidth = 1;
    private int portalTime = 200;
    private int portalIndex = 0;
    private int ProjectileLifeLength = 200;
    private boolean disappearAfterUse = false;
    Vec3 SpawnPos;
    private int maxDistance = 7;

    public PortalProjectileEntity(EntityType<PortalProjectileEntity> pEntityType, Level pLevel){
        super(pEntityType, pLevel);
    }
    public PortalProjectileEntity(double x, double y, double z, Level pLevel){
        super(ModEntities.PORTAL_PROJECTILE.get(), x, y, z, pLevel);
    }
    public PortalProjectileEntity(LivingEntity shooter, Level pLevel, int portalIndex, int portalTime, boolean disappearAfterUse){
        super(ModEntities.PORTAL_PROJECTILE.get(), shooter, pLevel);
        this.portalIndex = portalIndex;
        this.portalTime = portalTime;
        this.disappearAfterUse = disappearAfterUse;
        this.SpawnPos = this.position();
    }
    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        if(!this.level().isClientSide){
            spawnPortal(pResult.getLocation(), pResult.getDirection(), pResult.getDirection().toYRot());
        }
    }
    public void spawnPortal(Vec3 pPos, Direction pDirection, float yRot){
        if(!this.level().isClientSide){
            Vec3 hitPos = new Vec3(pPos.x,pPos.y-0.5f,pPos.z);

            PortalEntityData pData = new PortalEntityData(
                    hitPos,
                    portalTime,
                    pDirection,
                    portalIndex,
                    disappearAfterUse
            );

            //PortalEntity portalentity = new PortalEntity(this.level(), pData);
            PortalEntity portalentity = ModEntities.PORTAL_ENTITY.get().spawn(this.getServer().getLevel(this.level().dimension()),
                    BlockPos.ZERO ,MobSpawnType.TRIGGERED);
            portalentity.init(pData, yRot);

            //portalentity.setYRot(yRot);

            //something
            this.level().broadcastEntityEvent(this, (byte)0);

            portalentity.playPlacementSound();

            float f = portalWidth / 2.0F;
            float f1 = portalHeight / 2F;

            portalentity.setBoundingBox(
                    new AABB(pData.pos.x - (double)f, pData.pos.y - (double)f1, pData.pos.z - (double)f,
                            pData.pos.x + (double)f, pData.pos.y + (double)f1, pData.pos.z + (double)f));

            this.level().gameEvent(this.getOwner(), GameEvent.ENTITY_PLACE, portalentity.position());

            this.discard();
        }
    }
    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(ModItems.PortalProjectileItem.get());
    }

    @Override
    protected float getWaterInertia() {
        return 1F;
    }
    @Override
    public boolean isNoGravity(){ return true; }
    @Override
    protected void onHitEntity(EntityHitResult pResult){
        //do not teleport unteleportable entities
        if(pResult.getEntity().getClass() == PortalEntity.class ||
                pResult.getEntity().getClass() == this.getClass() ||
                !pResult.getEntity().canChangeDimensions()) return;

        if(this.level().isClientSide){
            this.level().addParticle(ParticleTypes.SOUL, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            return;
        }

        //teleport logic
        ServerLevel dim = pResult.getEntity().getServer().getLevel(pResult.getEntity().level().dimension());
        BlockPos pos = TeleportLogic.FindViablePositionFor(dim, this.getOnPos());
        TeleportLogic.TeleportEntity(pResult.getEntity(), dim, pos);
    }
    //update logic
    public void tick() {
        super.tick();
        if (this.level().isClientSide && !this.inGround) {
            this.level().addParticle(ParticleTypes.PORTAL, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
        }
        if(!this.level().isClientSide){
            ProjectileLifeLength--;
            if(ProjectileLifeLength <= 0) this.discard();


            if(SpawnPos.distanceTo(this.position()) >= maxDistance){
                //spawnPortal(this.position(), this.getDirection().getOpposite());
                spawnPortal(this.position(),
                        Direction.getNearest(
                            this.position().subtract(SpawnPos).x,
                            this.position().subtract(SpawnPos).y,
                            this.position().subtract(SpawnPos).z),
                        this.getYRot()+180);
            }
        }
    }
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);

        this.portalHeight = pCompound.getInt("portalHeight");
        this.portalWidth = pCompound.getInt("portalWidth");
        this.portalTime = pCompound.getInt("portalTime");
        this.portalIndex =pCompound.getInt("portalIndex");
        this.ProjectileLifeLength =pCompound.getInt("portalLife");
        this.disappearAfterUse = pCompound.getBoolean("pDisappear");
        this.SpawnPos = new Vec3(pCompound.getDouble("sPosX"), pCompound.getDouble("sPosY"), pCompound.getDouble("sPosZ"));
    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("portalHeight", this.portalHeight);
        pCompound.putInt("portalWidth", this.portalWidth);
        pCompound.putInt("portalTime", this.portalTime);
        pCompound.putInt("portalIndex", this.portalIndex);
        pCompound.putInt("portalLife", this.ProjectileLifeLength);
        pCompound.putBoolean("pDisappear", this.disappearAfterUse);

        pCompound.putDouble("sPosX", this.SpawnPos.x);
        pCompound.putDouble("sPosY", this.SpawnPos.y);
        pCompound.putDouble("sPosZ", this.SpawnPos.z);
    }
    public boolean shouldRender(double pX, double pY, double pZ) {
        return true;
    }
}
