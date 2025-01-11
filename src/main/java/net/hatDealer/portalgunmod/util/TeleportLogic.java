package net.hatDealer.portalgunmod.util;

import net.hatDealer.portalgunmod.blocks.ModBlocks;
import net.hatDealer.portalgunmod.entity.ModEntities;
import net.hatDealer.portalgunmod.entity.custom.PortalEntity;
import net.hatDealer.portalgunmod.entity.custom.PortalEntityData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;

import java.util.Random;
import java.util.function.Function;

public class TeleportLogic {
    private TeleportLogic(){ }

    public static BlockPos FindViablePositionFor(ServerLevel dim, BlockPos OriginalPos){
        BlockPos bPosFirst = new BlockPos(OriginalPos);
        do{
            //try to place portal down
            if (!dim.getBlockState(bPosFirst.below()).isAir()
                    && dim.getBlockState(bPosFirst).isAir()
                    && dim.getBlockState(bPosFirst.above()).isAir()) return bPosFirst;

            //if encased in blocks, skip
            if(!dim.getBlockState(bPosFirst.below()).isAir()
                && !dim.getBlockState(bPosFirst).isAir()
                && !dim.getBlockState(bPosFirst.above()).isAir()) break;

            bPosFirst = bPosFirst.below();
        }while (bPosFirst.getY() > -64);

        for(BlockPos bPos : BlockPos.spiralAround(OriginalPos, 16, Direction.EAST, Direction.SOUTH)){
             bPos = bPos.atY(127);
            while (bPos.getY() > 32) {

                if (!dim.getBlockState(bPos.below()).isAir()
                        && dim.getBlockState(bPos).isAir()
                        && dim.getBlockState(bPos.above()).isAir()) return bPos;

                bPos = bPos.below();
            }
        }
        System.out.println("No valid position found for the portal gun!");
        return OriginalPos;
    }
    public static void TeleportEntity(Entity entity, ServerLevel dim, BlockPos pos){
        entity.changeDimension(dim, new ITeleporter() {
            @Override
            public PortalInfo getPortalInfo(Entity entity, ServerLevel destWorld, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
                return new PortalInfo(pos.getCenter(), entity.getDeltaMovement(), entity.getYRot()-180f, entity.getXRot());
            }
            @Override
            public boolean playTeleportSound(ServerPlayer player, ServerLevel sourceWorld, ServerLevel destWorld)
            {
                return false;
            }
        });

        entity.setPos(pos.getX(), pos.getY(), pos.getZ());
    }
    /*
    Used when the unstable portal liquid is used
    Random stuff happens (stuff gets generated etc.)
     */
    public static void ChaoticExplosion(Vec3 pos, Level level, Direction pDirection, float yRot, Entity entity){
        if(!level.isClientSide){
            Random random = new Random();
            int randomResult = random.nextInt(0, 10);

            switch (randomResult){
                case 0: // random portal
                    String DimToken = TeleportLogic.GetRandomVanillaDimToken();
                    TeleportLogic.CreateRandomPortal(
                            pos,
                            DimToken,
                            level,
                            pDirection,
                            yRot,
                            entity,
                            8000
                    );
                    break;
                case 1: // explosion
                case 2:
                case 3:
                    level.explode(
                            entity,
                            pos.x(), pos.y(), pos.z(),
                            random.nextInt(3, 10),
                            true,
                            Level.ExplosionInteraction.BLOCK
                    );
                    break;
                case 4: // place a cube made out of random block
                case 5:
                case 6:
                    TeleportLogic.PlaceCubeOfBlocks(
                            level,
                            new BlockPos((int)pos.x, (int)pos.y, (int)pos.z),
                            random.nextInt(4, 8),
                            TeleportLogic.GetRandomCommonBlock().defaultBlockState(),
                            true
                    );
                    break;
                case 7: // place random liquid block
                case 8:
                    Block liquidBlock = random.nextBoolean() ? Blocks.WATER : Blocks.LAVA;
                    TeleportLogic.PlaceCubeOfBlocks(
                            level,
                            new BlockPos((int)pos.x, (int)pos.y, (int)pos.z),
                            random.nextInt(3, 5),
                            liquidBlock.defaultBlockState(),
                            false
                    );
                    break;
                case 9: // place a single rare block
                    level.setBlock(new BlockPos((int)pos.x, (int)pos.y, (int)pos.z), TeleportLogic.GetRandomRareBlock().defaultBlockState(), Block.UPDATE_ALL);
                    break;
            }
        }
    }

    public static void CreateRandomPortal(Vec3 pos, String DimToken, Level level, Direction pDirection, float yRot, Entity entity, int teleportSpread){
        if(!level.isClientSide){
            Random random = new Random();
            pos = pos.subtract(0, 1, 0);
            //Direction pDirection = this.getMotionDirection();
            BlockPos portalFeetPos = new BlockPos((int)pos.x, (int)pos.y, (int)pos.z);

            //place portal on the ground if slightly above a free space
            if(!level.getBlockState(portalFeetPos).isAir() && !level.getBlockState(portalFeetPos.above()).isSolid()){
                if(pDirection.get3DDataValue() <= 1)
                    pos = new Vec3(pos.x, portalFeetPos.getY(), pos.z);
                else
                    pos = new Vec3(pos.x, portalFeetPos.getY()+1, pos.z);
            }

            Vec3 NewRandomPos = pos.add(
                    new Vec3(random.nextInt(-teleportSpread/2, teleportSpread/2), 0,
                            random.nextInt(-teleportSpread/2, teleportSpread/2)));

            PortalEntityData pData = new PortalEntityData(
                    pos,
                    random.nextInt(100, 200),
                    pDirection,
                    random.nextBoolean(),
                    //this.level().dimension().location().toString(),
                    DimToken,
                    new Vec3i((int)NewRandomPos.x, (int)NewRandomPos.y, (int)NewRandomPos.z)
            );

            PortalEntity portalentity = ModEntities.PORTAL_ENTITY.get().spawn(level.getServer().getLevel(level.dimension()),
                    BlockPos.ZERO , MobSpawnType.TRIGGERED);
            if(pDirection == Direction.DOWN || pDirection == Direction.UP){
                portalentity.init(pData);
            }else{
                portalentity.init(pData, yRot);
            }

            portalentity.playPlacementSound();

            level.gameEvent(entity, GameEvent.ENTITY_PLACE, portalentity.position());
        }
    }
    public static String GetRandomVanillaDimToken(){
        Random random = new Random();
        int dimIndex = random.nextInt(3);

        final String DimTokens[] = {
                "minecraft:overworld",
                "minecraft:the_nether",
                "minecraft:the_end"
        };

        return DimTokens[dimIndex];
    }
    public static Block GetRandomRareBlock() {
        Random random = new Random();

        final Block BlockList[] = {
                Blocks.EMERALD_BLOCK,
                Blocks.OBSIDIAN,
                Blocks.ANVIL,
                Blocks.NETHERITE_BLOCK,
                Blocks.DIAMOND_BLOCK,
                Blocks.SPONGE,
                Blocks.BEACON,
                Blocks.CHORUS_FLOWER,
                Blocks.WITHER_SKELETON_SKULL,
                Blocks.END_STONE,
                Blocks.ANCIENT_DEBRIS,
                Blocks.QUARTZ_BLOCK,
                Blocks.CONDUIT,
                Blocks.DIORITE,
                Blocks.ANDESITE,
                Blocks.GRANITE,
                Blocks.GOLD_BLOCK,
                Blocks.EMERALD_ORE,
                Blocks.PIGLIN_HEAD,
                Blocks.COPPER_BLOCK,
                Blocks.PISTON,
                Blocks.LAVA_CAULDRON,
                Blocks.POWDER_SNOW_CAULDRON,
        };

        int blockIndex = random.nextInt(BlockList.length);

        return BlockList[blockIndex];
    }
    public static Block GetRandomCommonBlock() {
        Random random = new Random();

        final Block BlockList[] = {
                Blocks.DIRT,
                Blocks.DIORITE,
                Blocks.ANDESITE,
                Blocks.GRANITE,
                Blocks.SAND,
                Blocks.GRAVEL,
                Blocks.OAK_LEAVES,
                Blocks.END_STONE,
                Blocks.NETHERRACK,
                Blocks.RED_SAND,
                Blocks.TERRACOTTA,
                Blocks.GLASS,
                Blocks.BASALT,
                Blocks.COBBLESTONE,
                Blocks.STONE,
                Blocks.DEEPSLATE,
                Blocks.COBBLED_DEEPSLATE,
        };

        int blockIndex = random.nextInt(BlockList.length);

        return BlockList[blockIndex];
    }
    public static void PlaceCubeOfBlocks(Level level, BlockPos middlePos, int size, BlockState blockState, boolean destroyBlocks) {
        for (int x = -size/2; x < size/2; x++) {
            for (int y = -size/2; y < size/2; y++) {
                for (int z = -size/2; z < size/2; z++) {
                    // Calculate the position for each block in the cube
                    BlockPos currentPos = middlePos.offset(x, y, z);

                    if(destroyBlocks)
                        level.setBlock(currentPos, blockState, Block.UPDATE_ALL);
                    else if(level.getBlockState(currentPos).isAir())
                        level.setBlock(currentPos, blockState, Block.UPDATE_ALL);
                    else
                        continue;
                }
            }
        }
    }
}
