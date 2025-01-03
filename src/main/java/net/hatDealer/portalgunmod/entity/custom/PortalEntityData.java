package net.hatDealer.portalgunmod.entity.custom;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public class PortalEntityData {
    public Vec3 pos;
    public int lifeTimeLength;
    public Direction dir;
    public boolean disappearAfterUse;
    public String DestinationDimKey;
    public Vec3 DestinationPos;
    public PortalEntityData(Vec3 pPos, int lifeTimeLength, Direction pDirection, boolean disappearAfterUse,
                            String DestinationDimKey, Vec3 DestinationPos){
        this.pos = pPos;
        this.lifeTimeLength = lifeTimeLength;
        this.dir = pDirection;
        this.disappearAfterUse = disappearAfterUse;
        this.DestinationDimKey = DestinationDimKey;
        this.DestinationPos = DestinationPos;
    }
}
