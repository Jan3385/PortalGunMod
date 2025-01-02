package net.hatDealer.portalgunmod.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraftforge.common.util.ITeleporter;

import java.util.function.Function;

public class TeleportLogic {
    private TeleportLogic(){ }

    public static BlockPos FindViablePositionFor(ServerLevel dim, BlockPos OriginalPos){
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
}
