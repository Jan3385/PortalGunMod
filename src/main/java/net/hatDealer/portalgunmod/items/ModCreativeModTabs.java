package net.hatDealer.portalgunmod.items;

import net.hatDealer.portalgunmod.PortalGunMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PortalGunMod.MODID);

    public static final RegistryObject<CreativeModeTab> OtherWorlds_TAB = CREATIVE_MODE_TABS.register("otherworlds_tab",
            ()-> CreativeModeTab.builder()
                    .icon(()->new ItemStack(ModItems.PortalGun.get()))
                    .title(Component.translatable("creativetab.portalgunmod_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        //add items to creative tab
                        pOutput.accept(ModItems.PortalGun.get());
                        pOutput.accept(ModItems.PortalProjectileItem.get());
                        pOutput.accept(ModItems.PortalProjectileUnstableItem.get());
                        pOutput.accept(ModItems.UnstablePortalPotion.get());
                        pOutput.accept(ModItems.StabilizedPortalPotion.get());
                        pOutput.accept(ModItems.PrecursorPortalPotion.get());

                    })
                    .build());

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
