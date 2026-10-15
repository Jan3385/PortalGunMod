package net.hatDealer.portalgunmod;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = PortalGunMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    public enum PortalTexture {
        Normal,
        PortalEffectOFF,
        RickAndMortyInspired
    }

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.EnumValue<PortalTexture> PORTAL_TEXTURE = BUILDER
            .comment("Portal Texture Type")
            .defineEnum("portalTexture", PortalTexture.Normal);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static PortalTexture portalTexture;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        portalTexture = PORTAL_TEXTURE.get();
    }
}