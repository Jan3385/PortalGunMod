package net.hatDealer.portalgunmod.util;

import net.minecraftforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public interface IPortalGunCapability {
    int getPortalIndex();
    int setPortalIndex(int x);
    int getPortalLifeLength();
    int setPortalLifeLength();
}
