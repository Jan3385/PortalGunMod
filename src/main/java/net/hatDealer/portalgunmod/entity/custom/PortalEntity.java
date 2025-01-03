package net.hatDealer.portalgunmod.entity.custom;

import net.hatDealer.portalgunmod.entity.ModEntities;
import net.hatDealer.portalgunmod.util.TeleportLogic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PortalEntity extends Entity{
    private static final EntityDataAccessor<Direction> DIRECTION = SynchedEntityData.defineId(PortalEntity.class, EntityDataSerializers.DIRECTION);
    private static final EntityDataAccessor<Boolean> INITIALISED = SynchedEntityData.defineId(PortalEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> SIZE = SynchedEntityData.defineId(PortalEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> lifeTimeLength = SynchedEntityData.defineId(PortalEntity.class, EntityDataSerializers.INT);
    private int lifeTime;
    private String DestinationDimKey;
    private Vec3 DestinationPos;
    private boolean disappearAfterUse;
    private boolean canTeleport;
    public PortalEntity(EntityType<? extends Entity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.entityData.set(INITIALISED, false);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DIRECTION, Direction.SOUTH);
        this.entityData.define(INITIALISED, false);
        this.entityData.define(SIZE, 0f);
        this.entityData.define(lifeTimeLength, 200);
    }

    //init for standart portal - normal functionality
    public void init(PortalEntityData pData){
        if(!this.level().isClientSide){
            this.setPos(pData.pos);
            this.setLifeTimeLength(pData.lifeTimeLength);
            this.setDirection(pData.dir);
            this.DestinationDimKey = pData.DestinationDimKey;
            this.DestinationPos = pData.DestinationPos;
            this.disappearAfterUse = pData.disappearAfterUse;

            this.entityData.set(INITIALISED, true);
            this.entityData.set(SIZE, 0f);

            this.canTeleport = true;
        }
    }
    //init for standart portal with yRot - normal functionality
    public void init(PortalEntityData pData, float yRot){
        if(!this.level().isClientSide){
            this.setPos(pData.pos);
            this.setLifeTimeLength(pData.lifeTimeLength);
            this.setYRotAndDir(yRot);
            this.DestinationDimKey = pData.DestinationDimKey;
            this.DestinationPos = pData.DestinationPos;
            this.disappearAfterUse = pData.disappearAfterUse;

            this.entityData.set(INITIALISED, true);
            this.entityData.set(SIZE, 1f);

            this.canTeleport = true;
        }
    }
    //init for exit portal - only decorative
    public void init(float yRot){
        if(!this.level().isClientSide){
            this.setYRotAndDir(yRot);
            this.setLifeTimeLength(40);
            //this.setPos(this.position().add(new Vec3(this.getDirection().getOpposite().step().div(4,1,4))));

            this.DestinationDimKey = this.level().dimension().toString();
            this.DestinationPos = this.position();

            this.disappearAfterUse = true;

            this.entityData.set(INITIALISED, true);
            this.entityData.set(SIZE, 1f);

            this.canTeleport = false;
        }
    }
    public boolean isInitialised(){
        return this.entityData.get(INITIALISED);
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void tick() {
        //Mby remove super.tick()?
        super.tick();
        this.checkBelowWorld();
        //client particles
        if(!this.level().isClientSide) {
            if (this.lifeTime == this.getLifeTimeLength()) {
                despawnPortal();
            }
            checkCollisions();
        }
        if(this.level().isClientSide){
            if(random.nextInt(5) == 1)
                this.level().addParticle(ParticleTypes.REVERSE_PORTAL, this.getX() + (random.nextFloat()*2) - 1, this.getY() + (random.nextFloat()*4) - 2, this.getZ() + (random.nextFloat()*2) - 1,
                        (random.nextFloat()*.4) - .2, (random.nextFloat()*.4) - .2, (random.nextFloat()*.4) - .2);
        }
        this.lifeTime++;
    }
    public float GetLifeTime(){
        return this.lifeTime;
    }
    public float getRemainingTime(){
        return this.getLifeTimeLength()-this.lifeTime;
    }
    public float getSize(){
        return unlinearyInterpolate(0.001f, 1, this.entityData.get(SIZE));
    }
    public float getRawSize(){
        return this.entityData.get(SIZE);
    }
    public static float unlinearyInterpolate(float a, float b, float t) {
        double logA = Math.log(a);
        double logB = Math.log(b);
        return (float) Math.exp(logA + (logB - logA) * t);
    }
    public void IncrementSize(float value){
        this.entityData.set(SIZE, Math.min(getRawSize()+value, 1));
    }
    public void DecrementSize(float value){
        this.entityData.set(SIZE, Math.max(getRawSize()-value, 0));
    }
    public int getLifeTimeLength(){
        return this.entityData.get(lifeTimeLength);
    }
    public void setLifeTimeLength(int time){
        this.entityData.set(lifeTimeLength, time);
    }

    public void handleEntityEvent(byte pId) {
        if (pId == 32) {
            if (this.level().isClientSide) {
                //play when hit: ?
                //this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ARMOR_STAND_HIT, this.getSoundSource(), 0.3F, 1.0F, false);
            }
        } else {
            super.handleEntityEvent(pId);
        }

    }

    @Override
    public boolean isPickable() { return true; }
    /**
     * Updates facing and bounding box based on it
     */
    protected void setDirection(Direction pFacingDirection) {
        if(!this.level().isClientSide) {
            this.entityData.set(DIRECTION, pFacingDirection);
            this.setYRot(pFacingDirection.toYRot());
        }
    }
    protected void setYRotAndDir(float yRot) {
        if(!this.level().isClientSide) {
            this.setYRot(yRot);
            this.entityData.set(DIRECTION, Direction.fromYRot(yRot));
        }
    }
    public Direction get3dDirection() {
        return this.entityData.get(DIRECTION);
    }
    @Override
    public @NotNull EntityDimensions getDimensions(Pose pPose) {
        //return super.getDimensions(pPose);
        return EntityDimensions.fixed(1, 2);

    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        Vec3 pos = new Vec3(pCompound.getFloat("TileX"), pCompound.getFloat("TileY"), pCompound.getFloat("TileZ"));
        this.setPos(pos);

        //direction isnt saved but loaded using saved yRot
        this.setDirection(Direction.fromYRot(this.getYRot()));

        this.entityData.set(INITIALISED, pCompound.getBoolean("INITIALISED"));
        this.entityData.set(SIZE, pCompound.getFloat("SIZE"));

        this.DestinationDimKey = pCompound.getString("DestinationDimKey");

        double x = pCompound.getDouble("DestinationPosX");
        double y = pCompound.getDouble("DestinationPosY");
        double z = pCompound.getDouble("DestinationPosZ");
        this.DestinationPos = new Vec3(x,y,z);

        this.lifeTime = pCompound.getInt("lifeTime");
        this.setLifeTimeLength(pCompound.getInt("lifeTimeLength"));
        this.canTeleport = pCompound.getBoolean("canTeleport");
        this.disappearAfterUse = pCompound.getBoolean("disappearAfterUse");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        Vec3 pos = this.position();
        pCompound.putDouble("TileX", pos.x());
        pCompound.putDouble("TileY", pos.y());
        pCompound.putDouble("TileZ", pos.z());

        pCompound.putBoolean("INITIALISED", this.getEntityData().get(INITIALISED));
        pCompound.putFloat("SIZE", this.getEntityData().get(SIZE));

        pCompound.putString("DestinationDimKey", this.DestinationDimKey);
        pCompound.putDouble("DestinationPosX", this.DestinationPos.x);
        pCompound.putDouble("DestinationPosY", this.DestinationPos.y);
        pCompound.putDouble("DestinationPosZ", this.DestinationPos.z);

        pCompound.putInt("lifeTime", this.lifeTime);
        pCompound.putInt("lifeTimeLength", this.getLifeTimeLength());
        pCompound.putBoolean("canTeleport", this.canTeleport);
        pCompound.putBoolean("disappearAfterUse", this.disappearAfterUse);
    }
    public void playPlacementSound() {
        this.level().playSound((Player)null, this.getBlockX(), this.getBlockY(), this.getBlockZ(), SoundEvents.BEACON_POWER_SELECT, SoundSource.PLAYERS, 0.1F, 0.7F);
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        this.level().playSound((Player)null, this.getBlockX(), this.getBlockY(), this.getBlockZ(), SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.PLAYERS, 1.0F, 0.87F);
        return true;
    }

    @Override
    public boolean canCollideWith(Entity pEntity) {
        return false;
    }
    @Override
    public boolean canBeCollidedWith() {
        return false;
    }
    public boolean isPortalOnSide(){
        int side = this.get3dDirection().get3DDataValue();
        return side > 1;
    }
    protected void checkCollisions() {
        if(!this.level().isClientSide && canTeleport){
            List<Entity> collisionList = this.level().getEntities(this, this.getBoundingBox(), EntitySelector.LIVING_ENTITY_STILL_ALIVE);

            MinecraftServer server = this.getServer();

            assert server != null;

            ResourceKey<Level> dimensionKey = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(this.DestinationDimKey));
            ServerLevel dim = server.getLevel(dimensionKey);

            for (Entity entity : collisionList) {
                //do not teleport portalEntity or projectiles
                if (entity.getClass() == PortalEntity.class || entity.getClass() == PortalProjectileEntity.class) continue;
                //do not teleport non-teleportable entities
                if(!entity.canChangeDimensions()) continue;

                if(dim == null){
                    this.despawnPortal();
                    return;
                }

                //get pos with translation
                Vec3 pos = this.position();

                BlockPos teleportPos = new BlockPos((int) this.DestinationPos.x, (int) this.DestinationPos.y, (int) this.DestinationPos.z);

                //find a nice teleport position
                teleportPos = TeleportLogic.FindViablePositionFor(dim, teleportPos);

                //negate fall damage
                entity.resetFallDistance();

                //teleport
                TeleportLogic.TeleportEntity(entity, dim, teleportPos);

                //spawn cosmetic portal on other side
                PortalEntity portal = ModEntities.PORTAL_ENTITY.get().spawn(this.getServer().getLevel(entity.level().dimension()), entity.blockPosition(), MobSpawnType.TRIGGERED);

                portal.init(this.getYRot());

                this.level().gameEvent(this,GameEvent.ENTITY_PLACE, portal.position());

                if(disappearAfterUse) this.despawnPortal();
            }
        }

    }
    public void spawnPortalOnOtherSide(PortalEntityData pData) {
        if (!this.level().isClientSide) {
            //spawn other portal

            PortalEntity otherPortalEntity = ModEntities.PORTAL_ENTITY.get().spawn(this.getServer().getLevel(this.level().dimension()),
                   BlockPos.ZERO, MobSpawnType.TRIGGERED);

            otherPortalEntity.init(pData);

            otherPortalEntity.playPlacementSound();

            float f = 1 / 2.0F;
            float f1 = 2 / 2F;

            otherPortalEntity.setBoundingBox(
                    new AABB(pData.pos.x - (double) f, pData.pos.y - (double) f1, pData.pos.z - (double) f,
                            pData.pos.x + (double) f, pData.pos.y + (double) f1, pData.pos.z + (double) f));

            this.level().gameEvent(otherPortalEntity, GameEvent.ENTITY_PLACE, otherPortalEntity.position());

            //this.level().addFreshEntity(otherPortalEntity);

            //otherPortalEntity.asignDestination(this.getDestinationReturn());

            //this.asignDestination(otherPortalEntity.getDestinationReturn());

            //Vec3 vec3Pos = new Vec3(pData.pos.x, pData.pos.y, pData.pos.z);
            //otherPortalEntity.changeDimension(DimensionHelper.getOrCreateWorld(this.getServer(), pData.ReturnDestinationIndex), new ITeleporter() {
            //    @Override
            //    public PortalInfo getPortalInfo(Entity entity, ServerLevel destWorld, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
            //        return new PortalInfo(vec3Pos, Vec3.ZERO, entity.getYRot(), entity.getXRot());
            //    }
            //});

        }
    }
    public void despawnPortal(){
        this.level().playSound((Player) null, this.getBlockX(), this.getBlockY(), this.getBlockZ(),
                SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 0.5F, 0.6F);
        this.discard();
    }
}
