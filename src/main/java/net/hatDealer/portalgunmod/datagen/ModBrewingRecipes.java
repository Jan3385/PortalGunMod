package net.hatDealer.portalgunmod.datagen;

import net.hatDealer.portalgunmod.items.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ModBrewingRecipes {
    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            BrewingRecipeRegistry.addRecipe(
                    Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD)),
                    Ingredient.of(Items.ENDER_PEARL),
                    ModItems.UnstablePortalPotion.get().getDefaultInstance());

            BrewingRecipeRegistry.addRecipe( //TODO: make reciep for portal fluid precursor to use as ingredient instead of unstable portal
                    Ingredient.of(ModItems.UnstablePortalPotion.get().getDefaultInstance()),
                    Ingredient.of(Items.CHORUS_FRUIT),
                    ModItems.UnstablePortalPotion.get().getDefaultInstance());

            BrewingRecipeRegistry.addRecipe(
                    Ingredient.of(ModItems.UnstablePortalPotion.get().getDefaultInstance()),
                    Ingredient.of(Items.AMETHYST_SHARD),
                    ModItems.StabilizedPortalPotion.get().getDefaultInstance());
        });
    }
}
