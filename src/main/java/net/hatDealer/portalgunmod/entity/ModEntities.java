package net.hatDealer.portalgunmod.entity;

import net.hatDealer.portalgunmod.PortalGunMod;
import net.hatDealer.portalgunmod.entity.custom.PortalEntity;
import net.hatDealer.portalgunmod.entity.custom.PortalProjectileEntity;
import net.hatDealer.portalgunmod.entity.custom.StablePortalLiquidProjectile;
import net.hatDealer.portalgunmod.entity.custom.UnstablePortalLiquidProjectile;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
        DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, PortalGunMod.MODID);

    public static final RegistryObject<EntityType<PortalProjectileEntity>> PORTAL_PROJECTILE =
            ENTITY_TYPES.register("portal_projectile", ()->EntityType.Builder.<PortalProjectileEntity>of(PortalProjectileEntity::new, MobCategory.MISC)
                    .sized(1.25f, 1.25f)
                    .fireImmune()
                    .build("portal_projectile"));
    public static final RegistryObject<EntityType<PortalEntity>> PORTAL_ENTITY =
            ENTITY_TYPES.register("portal_entity", () -> EntityType.Builder.<PortalEntity>of(PortalEntity::new, MobCategory.MISC)
                    .sized(1f, 2f)
                    .build("portal_entity"));

    public static final RegistryObject<EntityType<UnstablePortalLiquidProjectile>> UNSTABLE_PORTAL_PROJECTILE =
            ENTITY_TYPES.register("unstable_portal_projectile", () -> EntityType.Builder.<UnstablePortalLiquidProjectile>of(UnstablePortalLiquidProjectile::new, MobCategory.MISC)
                    .sized(1f, 2f)
                    .build("unstable_portal_projectile"));

    public static final RegistryObject<EntityType<StablePortalLiquidProjectile>> STABLE_PORTAL_PROJECTILE =
            ENTITY_TYPES.register("stable_portal_projectile", () -> EntityType.Builder.<StablePortalLiquidProjectile>of(StablePortalLiquidProjectile::new, MobCategory.MISC)
                    .sized(1f, 2f)
                    .build("stable_portal_projectile"));

    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
}
