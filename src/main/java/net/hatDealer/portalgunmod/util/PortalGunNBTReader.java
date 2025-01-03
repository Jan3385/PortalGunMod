package net.hatDealer.portalgunmod.util;

import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class PortalGunNBTReader {
    public static Vec3i GetPosFromPortalGunNBT(CompoundTag pTag, Vec3 PlayerPos){
        Random random = new Random();
        int id = pTag.contains("selectedID") ? pTag.getInt("selectedID") : 0;

        Vec3i PortalPos = new Vec3i(
                pTag.contains("VecX"+id) ? pTag.getInt("VecX"+id) : (int)PlayerPos.x + random.nextInt(-1000, 1000),
                pTag.contains("VecY"+id) ? pTag.getInt("VecY"+id) : (int)PlayerPos.y + random.nextInt(-1000, 1000),
                pTag.contains("VecZ"+id) ? pTag.getInt("VecZ"+id) : (int)PlayerPos.z + random.nextInt(-1000, 1000)
        );

        return PortalPos;
    }
    public static String GetDimKeyDromPortalGunNBT(CompoundTag pTag){
        int id = pTag.contains("selectedID") ? pTag.getInt("selectedID") : 0;

        return pTag.contains("DKey"+id) ? pTag.getString("DKey"+id) : TeleportLogic.GetRandomVanillaDimToken();
    }
}
