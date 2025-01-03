package net.hatDealer.portalgunmod.items;

import net.hatDealer.portalgunmod.PortalGunMod;
import net.hatDealer.portalgunmod.items.custom.PortalGuns.NormalPortalGunItem;
import net.hatDealer.portalgunmod.items.custom.PortalProjectileItem;
import net.hatDealer.portalgunmod.items.custom.StabilizedPortalLiquid;
import net.hatDealer.portalgunmod.items.custom.UnstablePortalLiquid;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
	public static final DeferredRegister<Item> ITEMS =
			DeferredRegister.create(ForgeRegistries.ITEMS, PortalGunMod.MODID);
	
	public static final RegistryObject<Item> PortalGun = ITEMS.register("portalgun",
			()-> new NormalPortalGunItem(new Item.Properties().stacksTo(1)));

	public static final RegistryObject<Item> PortalProjectileItem = ITEMS.register("portalprojectileitem",
			()-> new PortalProjectileItem(new Item.Properties().stacksTo(16)));

	public static final RegistryObject<Item> UnstablePortalPotion = ITEMS.register("unstable_portal_potion",
			() -> new UnstablePortalLiquid(new Item.Properties().stacksTo(8)));

	public static final RegistryObject<Item> StabilizedPortalPotion = ITEMS.register("stabilized_portal_potion",
			() -> new StabilizedPortalLiquid(new Item.Properties().stacksTo(8)));
	
	public static void register(IEventBus eventBus) {
		ITEMS.register(eventBus);
	}
}
