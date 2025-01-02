package net.hatDealer.portalgunmod.entity;

import net.hatDealer.portalgunmod.PortalGunMod;
import net.hatDealer.portalgunmod.entity.custom.PortalEntity;
import net.hatDealer.portalgunmod.entity.custom.PortalProjectileEntity;
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
    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
}
