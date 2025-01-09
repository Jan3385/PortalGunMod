package net.hatDealer.portalgunmod.screens;

import net.hatDealer.portalgunmod.PortalGunMod;
import net.hatDealer.portalgunmod.items.ModItems;
import net.hatDealer.portalgunmod.items.custom.PortalgunItem;
import net.hatDealer.portalgunmod.networking.ItemNBTUpdatePacket;
import net.hatDealer.portalgunmod.networking.ModNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jline.utils.Colors;
import org.joml.Vector3i;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
public class NormalPortalGunScreen extends Screen {
    ItemStack PhysicalPortalGun;
    private final static ResourceLocation SaveIcon = new ResourceLocation(PortalGunMod.MODID,"textures/gui/save.png");
    private final static ResourceLocation SelectIcon = new ResourceLocation(PortalGunMod.MODID,"textures/gui/select.png");
    private final static ResourceLocation CoordsBackground = new ResourceLocation("textures/gui/slider.png");
    private final static ResourceLocation NineSlice = new ResourceLocation(PortalGunMod.MODID,"textures/gui/nineslice.png");
    private final static ResourceLocation Background = new ResourceLocation(PortalGunMod.MODID,"textures/gui/portal-background.png");
    //private final static int HighlightColor = Color.HSBtoRGB(46, 34.5f, 20);
    private final static int HighlightColor = 0xB0EAA700; //TODO: different color
    private int SelectedPortalDest;
    private Vec3 DestinationPos[] = new Vec3[5];
    private String DestinationDimKey[] = new String[5];
    private boolean PreferStableAmmo;
    private CompoundTag PortalGunNBT;
    private Button AmmoSelector;
    private int StableAmmo = 0;
    private int UnstableAmmo = 0;

    public NormalPortalGunScreen(Component pTitle, ItemStack PortalGun)
    {
        super(pTitle);
        this.PhysicalPortalGun = PortalGun;
        PortalGunNBT = PortalGun.getOrCreateTag();
    }

    protected void init(){
        super.init();

        for(int i = 0; i < 5; i++){
            if(PortalGunNBT.contains("VecX"+i) && PortalGunNBT.contains("VecY"+i) && PortalGunNBT.contains("VecZ"+i)){
                DestinationPos[i] = new Vec3(
                        PortalGunNBT.getDouble("VecX"+i),
                        PortalGunNBT.getDouble("VecY"+i),
                        PortalGunNBT.getDouble("VecZ"+i)
                );
            }
            if(PortalGunNBT.contains("DKey"+i)){
                DestinationDimKey[i] = PortalGunNBT.getString("DKey"+i);
            }
            if(PortalGunNBT.contains("selectedID")){
                SelectedPortalDest = PortalGunNBT.getInt("selectedID");
            }else
                SelectedPortalDest = 0;

            if(PortalGunNBT.contains("stableAmmo")){
                PreferStableAmmo = PortalGunNBT.getBoolean("stableAmmo");
            }else
                PreferStableAmmo = true;
        }

        //button selectors
        for(int i = 0; i < 5; i++){
            MakeButtonsForCoordinates(i);
        }

        //close button
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (button) -> {
            Close();
        }).bounds(this.width/2 + 90, this.height/2+40, 80, 20).build());

        //Preffered ammo selector
        AmmoSelector = this.addRenderableWidget(Button.builder(GetPreferredAmmoComponent(), (button) -> {
            FlipPreferredAmmo();
        }).bounds(this.width/2 + 105, this.height/2 - 45, 50, 20).build());

        Player localPlayer = Minecraft.getInstance().player;
        StableAmmo = countItemInInventory(localPlayer, ModItems.PortalProjectileItem.get());
        UnstableAmmo = countItemInInventory(localPlayer, ModItems.PortalProjectileUnstableItem.get());
    }
    private void MakeButtonsForCoordinates(int num){
        this.addRenderableWidget(new ImageButton(this.width / 2 + 25, this.height/2 - 60 + (num * 25), 20, 20,
                0, 0, 20, SaveIcon, 20, 40, (button) -> {
            SavePos(num);
        }));
        this.addRenderableWidget(new ImageButton(this.width / 2 + 50, this.height/2 - 60 + (num * 25), 20, 20,
                0, 0, 20, SelectIcon, 20, 40, (button) -> {
            SetPos(num);
            //Close();
        }));
    }
    private void MakeInfoForCoordinates(int num, GuiGraphics pGuiGraphics){
        pGuiGraphics.blit(CoordsBackground, this.width/2 - 180, this.height/2 - 60 + (num * 25), 0, 0, 200, 20);
        String text = "Unconfigured!";

        if(DestinationPos[num] != null && DestinationDimKey[num] != null){
            Vec3 d = DestinationPos[num];

            ResourceLocation dimLoc = new ResourceLocation(DestinationDimKey[num]);

            String DimName = dimLoc
                    .getPath()
                    .replace('_', ' '); // "the_nether" -> "the nether"
            DimName = DimName.substring(0, 1).toUpperCase() + DimName.substring(1); // "the nether" -> "The nether"

            text = "x: " + (int)d.x + " y: " + (int)d.y + " z: " + (int)d.z + " in " + DimName;
        }

        pGuiGraphics.drawString(this.font, text, this.width/2 - 175, this.height/2 - 54 + (num * 25), 16777215);
    }

    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        pGuiGraphics.pose().pushPose();

        //background
        /*pGuiGraphics.blitNineSlicedSized(
                NineSlice,
                this.width/2 - 190, this.height/2 - 85,
                380, 155,
                16,16,
                64, 64,
                0, 0,
                64,64
        );*/
        int w = this.width / 2 - 180 - 7;
        int h = this.height / 2 - 75 - 7;
        pGuiGraphics.blit(Background, w, h, 0, 0, 374, 142 + 7, 374, 142 + 7);

        //title and positions
        pGuiGraphics.fill(
                this.width/2 - 180 - 2,
                this.height/2 - 62 + (SelectedPortalDest * 25),
                this.width / 2 + 50 + 20 + 2,
                this.height/2 - 38 + (SelectedPortalDest * 25),
                HighlightColor);

        pGuiGraphics.drawCenteredString(this.font, Component.translatable("screen.portal.preferred-title"),
                this.width/2 + 130, this.height/2 - 57, 16777215);

        //Ammo info
        pGuiGraphics.drawCenteredString(this.font, "Stable Capsules: "+StableAmmo,
                this.width/2 + 130, this.height/2 - 10, 16777215);
        pGuiGraphics.drawCenteredString(this.font, "Unstable Capsules: "+UnstableAmmo,
                this.width/2 + 130, this.height/2 + 10, 16777215);

        //GUI title
        pGuiGraphics.drawCenteredString(this.font, this.title, this.width / 2, this.height/2 - 75, 16777215);

        for (int i = 0; i < 5; i++){
            MakeInfoForCoordinates(i, pGuiGraphics);
        }

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

    private void SavePos(int id){
        Player localPlayer = Minecraft.getInstance().player;
        Vec3 PlayerPos = localPlayer.position();
        String DimKey = localPlayer.level().dimension().location().toString();

        DestinationPos[id] = PlayerPos;

        PortalGunNBT.putDouble("VecX"+id, PlayerPos.x);
        PortalGunNBT.putDouble("VecY"+id, PlayerPos.y);
        PortalGunNBT.putDouble("VecZ"+id, PlayerPos.z);

        DestinationDimKey[id] = DimKey;

        PortalGunNBT.putString("DKey"+id, DimKey);
    }
    private void SetPos(int id){
        SelectedPortalDest = id;
        PortalGunNBT.putInt("selectedID", id);
    }

    public int toInt(String s, int defaultValue){
        try {
            return Integer.parseInt(s);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return defaultValue;
        }
    }
    public int limitInt(int number, int LowerLimit, int UpperLimit){
        if(number > UpperLimit) number = UpperLimit;
        else if(number < LowerLimit) number = LowerLimit;
        return number;
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
}
