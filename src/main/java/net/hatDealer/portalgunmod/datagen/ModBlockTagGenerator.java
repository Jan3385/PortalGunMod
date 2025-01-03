package net.hatDealer.portalgunmod.datagen;

import net.hatDealer.portalgunmod.PortalGunMod;
import net.hatDealer.portalgunmod.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, PortalGunMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        /*this.tag(ModTags.Blocks.GENERATION_TOP_LAYER)
                .addTags(Tags.Blocks.GRAVEL)
                .addTags(Tags.Blocks.ORES)
                .addTags(Tags.Blocks.SAND)
                .addTags(Tags.Blocks.STORAGE_BLOCKS)
                .addTags(Tags.Blocks.ORES_NETHERITE_SCRAP)
                .addTags(Tags.Blocks.STONE)
                .addTags(Tags.Blocks.COBBLESTONE)
                .addTags(Tags.Blocks.END_STONES)
                .addTags(Tags.Blocks.GLASS)
                .addTags(Tags.Blocks.SANDSTONE)
                .addTags(Tags.Blocks.ENDERMAN_PLACE_ON_BLACKLIST)
                .addTags(Tags.Blocks.OBSIDIAN)
                .addTags(Tags.Blocks.NETHERRACK);*/
    }
}
