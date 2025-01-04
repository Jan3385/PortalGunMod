package net.hatDealer.portalgunmod.datagen;

import net.hatDealer.portalgunmod.PortalGunMod;
import net.hatDealer.portalgunmod.items.ModItems;
import net.hatDealer.portalgunmod.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagGenerator extends ItemTagsProvider {
    public ModItemTagGenerator(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider,
                               CompletableFuture<TagLookup<Block>> pBlockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, pBlockTags, PortalGunMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider Provider) {
        this.tag(ModTags.Items.PORTAL_AMMO)
                .add(ModItems.PortalProjectileItem.get())
                .add(ModItems.PortalProjectileUnstableItem.get());

        this.tag(ModTags.Items.FROGLIGHT)
                .add(Items.OCHRE_FROGLIGHT)
                .add(Items.PEARLESCENT_FROGLIGHT)
                .add(Items.VERDANT_FROGLIGHT);

        this.tag(ModTags.Items.GLASSPANES)
                .add(Items.GLASS_PANE)
                .add(Items.GRAY_STAINED_GLASS_PANE)
                .add(Items.BLACK_STAINED_GLASS_PANE)
                .add(Items.BLUE_STAINED_GLASS_PANE)
                .add(Items.BROWN_STAINED_GLASS_PANE)
                .add(Items.CYAN_STAINED_GLASS_PANE)
                .add(Items.GREEN_STAINED_GLASS_PANE)
                .add(Items.LIGHT_BLUE_STAINED_GLASS_PANE)
                .add(Items.LIGHT_GRAY_STAINED_GLASS_PANE)
                .add(Items.LIME_STAINED_GLASS_PANE)
                .add(Items.MAGENTA_STAINED_GLASS_PANE)
                .add(Items.ORANGE_STAINED_GLASS_PANE)
                .add(Items.PINK_STAINED_GLASS_PANE)
                .add(Items.PURPLE_STAINED_GLASS_PANE)
                .add(Items.RED_STAINED_GLASS_PANE)
                .add(Items.WHITE_STAINED_GLASS_PANE)
                .add(Items.YELLOW_STAINED_GLASS_PANE);

    }
}
