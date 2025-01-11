package net.hatDealer.portalgunmod.screens;

import net.hatDealer.portalgunmod.PortalGunMod;
import net.hatDealer.portalgunmod.items.ModItems;
import net.hatDealer.portalgunmod.networking.ItemNBTUpdatePacket;
import net.hatDealer.portalgunmod.networking.ModNetworking;
import net.hatDealer.portalgunmod.util.ModKeybinds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class PortalScreen extends Screen {
    private final static ResourceLocation Background = new ResourceLocation(PortalGunMod.MODID,"textures/gui/portal-background.png");
    ItemStack PhysicalPortalGun;
    protected boolean PreferStableAmmo;
    protected CompoundTag PortalGunNBT;
    protected Button AmmoSelector;
    protected int StableAmmo = 0;
    protected int UnstableAmmo = 0;
    public PortalScreen(Component pTitle, ItemStack PortalGun)
    {
        super(pTitle);
        this.PhysicalPortalGun = PortalGun;
        PortalGunNBT = PortalGun.getOrCreateTag();
    }
    protected void init(){
        //close button
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (button) -> {
            Close();
        }).bounds(this.width/2 + 90, this.height/2+40, 80, 20).build());

        if(PortalGunNBT.contains("stableAmmo")){
            PreferStableAmmo = PortalGunNBT.getBoolean("stableAmmo");
        }else
            PreferStableAmmo = true;

        //Preffered ammo selector
        AmmoSelector = this.addRenderableWidget(Button.builder(GetPreferredAmmoComponent(), (button) -> {
            FlipPreferredAmmo();
        }).bounds(this.width/2 + 105, this.height/2 - 45, 50, 20).build());

        Player localPlayer = Minecraft.getInstance().player;
        StableAmmo = countItemInInventory(localPlayer, ModItems.PortalProjectileItem.get());
        UnstableAmmo = countItemInInventory(localPlayer, ModItems.PortalProjectileUnstableItem.get());
    }
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        pGuiGraphics.pose().pushPose();

        //background
        int w = this.width / 2 - 180 - 7;
        int h = this.height / 2 - 75 - 7;
        pGuiGraphics.blit(Background, w, h, -1, 0, 0, 374, 142 + 7, 374, 142 + 7);

        //title and positions
        pGuiGraphics.drawCenteredString(this.font, Component.translatable("screen.portal.preferred-title"),
                this.width/2 + 130, this.height/2 - 57, 16777215);

        //Ammo info
        pGuiGraphics.drawCenteredString(this.font, "Stable Capsules: "+StableAmmo,
                this.width/2 + 130, this.height/2 - 10, 16777215);
        pGuiGraphics.drawCenteredString(this.font, "Unstable Capsules: "+UnstableAmmo,
                this.width/2 + 130, this.height/2 + 10, 16777215);

        //GUI title
        pGuiGraphics.drawCenteredString(this.font, this.title, this.width / 2, this.height/2 - 75, 16777215);

        pGuiGraphics.pose().popPose();

        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }
    @Override
    public boolean isPauseScreen() {
        return false;
    }
    public void Close(){

        ModNetworking.CHANNEL.sendToServer(new ItemNBTUpdatePacket(PortalGunNBT));

        this.minecraft.setScreen((Screen)null);
    }
    public Component GetPreferredAmmoComponent(){
        if(PreferStableAmmo) return Component.translatable("screen.portal.preferred-stable");
        return Component.translatable("screen.portal.preferred-unstable");
    }
    public void FlipPreferredAmmo(){
        PortalGunNBT.putBoolean("stableAmmo", !PreferStableAmmo);
        PreferStableAmmo = !PreferStableAmmo;
        if(PreferStableAmmo)
            AmmoSelector.setMessage(Component.translatable("screen.portal.preferred-stable"));
        else
            AmmoSelector.setMessage(Component.translatable("screen.portal.preferred-unstable"));


    }
    public static int countItemInInventory(Player player, Item item) {
        int count = 0;

        for (ItemStack stack : player.getInventory().items) {
            if (!stack.isEmpty() && stack.getItem() == item) {
                count += stack.getCount();
            }
        }

        return count;
    }
    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers){
        if(pKeyCode == ModKeybinds.OpenUI.getKey().getValue()) {
            this.Close();
            return true;
        }else{
            return super.keyPressed(pKeyCode, pScanCode, pModifiers);
        }
    }
    public int toInt(String s, int defaultValue){
        try {
            return Integer.parseInt(s);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return defaultValue;
        }
    }
    public float toFloat(String s, int defaultValue){
        try {
            return Float.parseFloat(s);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return defaultValue;
        }
    }
    public <T extends Number & Comparable<T>> T limitNum(T number, T lowerLimit, T upperLimit) {
        if (number.compareTo(upperLimit) > 0) {
            return upperLimit;
        } else if (number.compareTo(lowerLimit) < 0) {
            return lowerLimit;
        }
        return number;
    }
}
