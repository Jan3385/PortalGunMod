package net.hatDealer.portalgunmod.entity.custom;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public class PortalEntityData {
    public Vec3 pos;
    public int lifeTimeLength;
    public Direction dir;
    public int DestinationIndex;
    public boolean disappearAfterUse;
    public PortalEntityData(Vec3 pPos, int lifeTimeLength, Direction pDirection,
                            int DestinationIndex, boolean disappearAfterUse){
        this.pos = pPos;
        this.lifeTimeLength = lifeTimeLength;
        this.dir = pDirection;
        this.DestinationIndex = DestinationIndex;
        this.disappearAfterUse = disappearAfterUse;
    }
}
