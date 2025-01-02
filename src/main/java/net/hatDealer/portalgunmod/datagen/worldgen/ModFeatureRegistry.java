package net.hatDealer.portalgunmod.datagen.worldgen;

import net.hatDealer.portalgunmod.PortalGunMod;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public final class ModFeatureRegistry {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, PortalGunMod.MODID);

    //public static final RegistryObject<RiftFeature> RIFT = register("rift", () -> new RiftFeature(RiftConfig.CODEC));

    private ModFeatureRegistry() {
    }

    private static <T extends Feature<?>> RegistryObject<T> register(String name, Supplier<T> feature) {
        return FEATURES.register(name, feature);
    }

}