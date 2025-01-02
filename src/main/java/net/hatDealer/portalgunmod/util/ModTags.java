package net.hatDealer.portalgunmod.util;

import net.hatDealer.portalgunmod.PortalGunMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks{
        public static final TagKey<Block> GENERATION_TOP_LAYER = tag("generation_top_layer");
        private static TagKey<Block> tag(String name){
            return BlockTags.create(new ResourceLocation(PortalGunMod.MODID, name));
        }
    }
    public static class Items {
        public static final TagKey<Item> PORTAL_AMMO = tag("portal_ammo");
        public static final TagKey<Item> FROGLIGHT = tag("froglight");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(PortalGunMod.MODID, name));
        }
    }
}
