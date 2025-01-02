package net.hatDealer.portalgunmod;

import com.mojang.logging.LogUtils;
import net.hatDealer.portalgunmod.blocks.ModBlocks;
import net.hatDealer.portalgunmod.datagen.worldgen.ModFeatureRegistry;
import net.hatDealer.portalgunmod.entity.ModEntities;
import net.hatDealer.portalgunmod.items.ModCreativeModTabs;
import net.hatDealer.portalgunmod.items.ModItems;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(PortalGunMod.MODID)
public class PortalGunMod
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "portalgunmod";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    public PortalGunMod()
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SPEC);

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModCreativeModTabs.register(modEventBus);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);

        ModEntities.register(modEventBus);

        //POIRegistry.TYPES.register(bus);
        //ParticleTypeRegistry.TYPES.register(bus);

        ModFeatureRegistry.FEATURES.register(modEventBus);

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }
}
