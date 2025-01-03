package net.hatDealer.portalgunmod.entity.custom;

import net.hatDealer.portalgunmod.entity.ModEntities;
import net.hatDealer.portalgunmod.items.ModItems;
import net.hatDealer.portalgunmod.util.TeleportLogic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.commands.SetBlockCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;


public class PortalProjectileEntity extends AbstractArrow {
    private int portalHeight = 2;
    private int portalWidth = 1;
    private int portalTime = 200;
    private String DestinationDimKey = "minecraft:overworld";
    private Vec3i DestinationPos = new Vec3i(150,150,150);
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
    public PortalProjectileEntity(LivingEntity shooter, Level pLevel, String DestinationDimKey, Vec3i DestinationPos, int portalTime, boolean disappearAfterUse){
        super(ModEntities.PORTAL_PROJECTILE.get(), shooter, pLevel);
        this.DestinationDimKey = DestinationDimKey;
        this.DestinationPos = DestinationPos;
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

            Vec3 hitPos = new Vec3(pPos.x,pPos.y-1f,pPos.z);
            Vec3 inFrontOfPortal = hitPos.add(Vec3.atCenterOf(pDirection.getNormal()).scale(0.25));
            BlockPos portalFeetPos = new BlockPos((int)inFrontOfPortal.x, (int)inFrontOfPortal.y, (int)inFrontOfPortal.z);

            //place portal on the ground if slightly above a free space
            if(!this.level().getBlockState(portalFeetPos).isAir() && !this.level().getBlockState(portalFeetPos.above()).isSolid()){
                if(pDirection.get3DDataValue() <= 1)
                    hitPos = new Vec3(hitPos.x, portalFeetPos.getY(), hitPos.z);
                else
                    hitPos = new Vec3(hitPos.x, portalFeetPos.getY()+1, hitPos.z);
            }

            PortalEntityData pData = new PortalEntityData(
                    hitPos,
                    portalTime,
                    pDirection,
                    disappearAfterUse,
                    DestinationDimKey,
                    DestinationPos
            );

            //PortalEntity portalentity = new PortalEntity(this.level(), pData);
            PortalEntity portalentity = ModEntities.PORTAL_ENTITY.get().spawn(this.getServer().getLevel(this.level().dimension()),
                    BlockPos.ZERO ,MobSpawnType.TRIGGERED);
            if(pDirection == Direction.DOWN || pDirection == Direction.UP){
                portalentity.init(pData);
            }else{
                portalentity.init(pData, yRot);
            }

            //something
            this.level().broadcastEntityEvent(this, (byte)0);

            portalentity.playPlacementSound();

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
        //client particles
        if (this.level().isClientSide && !this.inGround) {
            this.level().addParticle(ParticleTypes.PORTAL, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
        }
        //kill projectile after some time, spawns portal after going too far
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
        this.DestinationDimKey =pCompound.getString("DestinationDimKey");

        int x = pCompound.getInt("DestinationPosX");
        int y = pCompound.getInt("DestinationPosY");
        int z = pCompound.getInt("DestinationPosZ");
        this.DestinationPos = new Vec3i(x,y,z);

        this.ProjectileLifeLength =pCompound.getInt("portalLife");
        this.disappearAfterUse = pCompound.getBoolean("pDisappear");
        this.SpawnPos = new Vec3(pCompound.getDouble("sPosX"), pCompound.getDouble("sPosY"), pCompound.getDouble("sPosZ"));
    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("portalHeight", this.portalHeight);
        pCompound.putInt("portalWidth", this.portalWidth);
        pCompound.putInt("portalTime", this.portalTime);
        pCompound.putString("DestinationDimKey", this.DestinationDimKey);
        pCompound.putInt("DestinationPosX", this.DestinationPos.getX());
        pCompound.putInt("DestinationPosY", this.DestinationPos.getY());
        pCompound.putInt("DestinationPosZ", this.DestinationPos.getZ());

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
