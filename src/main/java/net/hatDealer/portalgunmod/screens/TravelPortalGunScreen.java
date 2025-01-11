package net.hatDealer.portalgunmod.screens;

import net.hatDealer.portalgunmod.networking.ItemNBTUpdatePacket;
import net.hatDealer.portalgunmod.networking.ModNetworking;
import net.hatDealer.portalgunmod.networking.RequestDimensionsPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;

public class TravelPortalGunScreen extends PortalScreen {

    private Vec3i PortalPos;
    private String PortalDimKey;
    private Button ChangeDimButton;
    EditBox x_position;
    EditBox y_position;
    EditBox z_position;
    public ArrayList<String> DimIDs = new ArrayList<>();
    public TravelPortalGunScreen(Component pTitle, ItemStack PortalGun) {
        super(pTitle, PortalGun);
    }
    protected void init(){
        super.init();
        if(PortalGunNBT.contains("VecX") && PortalGunNBT.contains("VecY") && PortalGunNBT.contains("VecZ")){
            PortalPos = new Vec3i(
                    PortalGunNBT.getInt("VecX"),
                    PortalGunNBT.getInt("VecY"),
                    PortalGunNBT.getInt("VecZ")
            );
        }
        if(PortalGunNBT.contains("DKey"))
            PortalDimKey = PortalGunNBT.getString("DKey");
        else
            PortalDimKey = "minecraft:overworld";

        ChangeDimButton = this.addRenderableWidget(Button.builder(Component.literal(getDimensionName(PortalDimKey)), (button) -> {
            FlipActiveDim();
        }).bounds(this.width/2 - 130, this.height/2 - 55, 100, 20).build());

        //Load dimensions from server
        ModNetworking.CHANNEL.sendToServer(new RequestDimensionsPacket());

        // coortinate input fields
        x_position = new EditBox(this.font, this.width/2 - 115, this.height/2 - 25 + 0, 70, 20,
                Component.literal("X"));
        x_position.setMaxLength(9);
        x_position.setValue(String.valueOf(PortalGunNBT.getInt("VecX")));
        this.addWidget(x_position);

        y_position = new EditBox(this.font, this.width/2 - 115, this.height/2 - 25 + 25, 70, 20,
                Component.literal("Y"));
        y_position.setMaxLength(3);
        y_position.setValue(String.valueOf(PortalGunNBT.getInt("VecY")));
        this.addWidget(y_position);

        z_position = new EditBox(this.font, this.width/2 - 115, this.height/2 - 25 + 50, 70, 20,
                Component.literal("Z"));
        z_position.setMaxLength(9);
        z_position.setValue(String.valueOf(PortalGunNBT.getInt("VecZ")));
        this.addWidget(z_position);

        //X vector
        AddArrowsTo(x_position);
        //Y vector
        AddArrowsTo(y_position);
        //Z vector
        AddArrowsTo(z_position);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.x_position.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.y_position.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.z_position.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        pGuiGraphics.drawCenteredString(this.font, "X:",
                this.x_position.getX() - 32, this.x_position.getY()+7, 16777215);
        pGuiGraphics.drawCenteredString(this.font, "Y:",
                this.y_position.getX() - 32, this.y_position.getY()+7, 16777215);
        pGuiGraphics.drawCenteredString(this.font, "Z:",
                this.z_position.getX() - 32, this.z_position.getY()+7, 16777215);
    }

    private void AddArrowsTo(EditBox box){
        this.addRenderableWidget(Button.builder(Component.literal("<"), (p_280793_) -> {
            Decrement(box);
        }).bounds(box.getX() - 20 - 5, box.getY(), 20, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal(">"), (p_280793_) -> {
            Increment(box);
        }).bounds(box.getX() + box.getWidth() + 5, box.getY(), 20, 20).build());
    }
    private void AddInputFieldFor(EditBox box){
        box.setMaxLength(9);
        box.setValue(String.valueOf(10));
        this.addWidget(box);
    }
    private void SavePos(Vec3i Pos, String DimKey){
        PortalGunNBT.putInt("VecX", Pos.getX());
        PortalGunNBT.putInt("VecY", Pos.getY());
        PortalGunNBT.putInt("VecZ", Pos.getZ());

        PortalGunNBT.putString("DKey", DimKey);
    }
    private String getDimensionName(String DimKey){
        ResourceLocation dimLoc = new ResourceLocation(DimKey);

        String DimName = dimLoc
                .getPath()
                .replace('_', ' '); // "the_nether" -> "the nether"
        DimName = DimName.substring(0, 1).toUpperCase() + DimName.substring(1); // "the nether" -> "The nether"

        return DimName;
    }
    private void FlipActiveDim(){
        if(DimIDs.size() == 0) return;

        // Find the index of the current dimension key
        int currentIndex = DimIDs.indexOf(PortalDimKey);

        // If the dimension was found, return the next one, or wrap around to the first dimension if it is the last one
        if (currentIndex != -1) {
            int nextIndex = (currentIndex + 1) % DimIDs.size();
            PortalDimKey = DimIDs.get(nextIndex);
        }
        ChangeDimButton.setMessage(Component.literal(getDimensionName(PortalDimKey)));
    }
    private void Increment(EditBox box){
        if(box == x_position){
            x_position.setValue(String.valueOf(
                    limitNum(toInt(x_position.getValue(), 0) + 1, -999999999, 999999999)));
        }else if(box == y_position){
            y_position.setValue(String.valueOf(
                    limitNum(toInt(y_position.getValue(), 0) + 1, -64, 320)));
        }else{
            z_position.setValue(String.valueOf(
                    limitNum(toInt(z_position.getValue(), 0) + 1, -999999999, 999999999)));
        }
    }
    private void Decrement(EditBox box){
        if(box == x_position){
            x_position.setValue(String.valueOf(
                    limitNum(toInt(x_position.getValue(), 0) - 1, -999999999, 999999999)));
        }else if(box == y_position){
            y_position.setValue(String.valueOf(
                    limitNum(toInt(y_position.getValue(), 0) - 1, -999999999, 999999999)));
        }else{
            z_position.setValue(String.valueOf(
                    limitNum(toInt(z_position.getValue(), 0) - 1, -999999999, 999999999)));
        }
    }
    @Override
    public void Close(){
        this.PortalPos = new Vec3i(
                toInt(x_position.getValue(), 0),
                toInt(y_position.getValue(), 0),
                toInt(z_position.getValue(), 0)
        );

        SavePos(this.PortalPos, this.PortalDimKey);

        super.Close();
    }
}
