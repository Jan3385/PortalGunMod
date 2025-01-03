package net.hatDealer.portalgunmod.screens;

import net.hatDealer.portalgunmod.items.custom.PortalgunItem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector3i;

@OnlyIn(Dist.CLIENT)
public class PortalGunScreen extends Screen {
    //input fields
    EditBox x_export;
    EditBox y_export;
    EditBox z_export;
    EditBox portal_life_export;
    ItemStack PhysicalPortalGun;
    Vector3i PortalVector;
    Button B_Disappears;
    boolean DisappearsAfterUse;

    public PortalGunScreen(Component pTitle, ItemStack PortalGun)
    {
        super(pTitle);
        this.PhysicalPortalGun = PortalGun;
        this.DisappearsAfterUse = PortalgunItem.getPortalDisappear(PhysicalPortalGun);
    }

    protected void init(){
        super.init();

        this.x_export = new EditBox(this.font, 100, this.height/2 - 30, 26, 20,
                Component.literal("X"));
        this.x_export.setMaxLength(3);
        this.x_export.setValue(String.valueOf(PortalVector.x));
        this.addWidget(this.x_export);

        this.y_export = new EditBox(this.font, this.width / 2 - 13, this.height/2 - 30, 26, 20,
                Component.literal("Y"));
        this.y_export.setMaxLength(3);
        this.y_export.setValue(String.valueOf(PortalVector.y));
        this.addWidget(this.y_export);

        this.z_export = new EditBox(this.font, this.width - 100 - 30, this.height/2 - 30, 26, 20,
                Component.literal("Z"));
        this.z_export.setMaxLength(3);
        this.z_export.setValue(String.valueOf(PortalVector.z));
        this.addWidget(this.z_export);

        this.portal_life_export = new EditBox(this.font, this.width / 2 - 40, this.height/2 + 30, 26, 20,
                Component.translatable("screen.portal.portalLife"));
        this.portal_life_export.setMaxLength(2);
        this.portal_life_export.setValue(String.valueOf(PortalgunItem.getPortalLifetime(PhysicalPortalGun)/20));
        this.addWidget(this.portal_life_export);

        this.B_Disappears = this.addRenderableWidget(Button.builder(getPortalDisappearButtonComponent(), (p_296214_) -> {
            this.SwitchPortalDisappear();
        }).bounds(this.width / 2 + 5, this.height/2 + 30, 140, 20).build());

        //close button
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (p_280793_) -> {
            Close();
        }).bounds(this.width / 2 - 100, this.height-50, 200, 20).build());

        // increase and decrease buttons
        //X vector
        this.addRenderableWidget(Button.builder(Component.literal("<"), (p_280793_) -> {
            changeVec(0, -1);
        }).bounds(x_export.getX() - (int)(x_export.getWidth()/1.2f), x_export.getY(), 20, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal(">"), (p_280793_) -> {
            changeVec(0, 1);
        }).bounds(x_export.getX() + (int)(x_export.getWidth()*1.07), x_export.getY(), 20, 20).build());

        //Y vector
        this.addRenderableWidget(Button.builder(Component.literal("<"), (p_280793_) -> {
            changeVec(1, -1);
        }).bounds(y_export.getX() - (int)(y_export.getWidth()/1.2f), y_export.getY(), 20, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal(">"), (p_280793_) -> {
            changeVec(1, 1);
        }).bounds(y_export.getX() + (int)(y_export.getWidth()*1.07), y_export.getY(), 20, 20).build());

        //Z vector
        this.addRenderableWidget(Button.builder(Component.literal("<"), (p_280793_) -> {
            changeVec(2, -1);
        }).bounds(z_export.getX() - (int)(z_export.getWidth()/1.2f), z_export.getY(), 20, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal(">"), (p_280793_) -> {
            changeVec(2, 1);
        }).bounds(z_export.getX() + (int)(z_export.getWidth()*1.07), z_export.getY(), 20, 20).build());

    }

    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        pGuiGraphics.pose().pushPose();
        //title and vector titles
        pGuiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 40, 16777215);
        pGuiGraphics.drawCenteredString(this.font, x_export.getMessage(), x_export.getX()+(x_export.getWidth()/2), x_export.getY()-x_export.getHeight()+9, 16777215);
        pGuiGraphics.drawCenteredString(this.font, y_export.getMessage(), y_export.getX()+(y_export.getWidth()/2), y_export.getY()-y_export.getHeight()+9, 16777215);
        pGuiGraphics.drawCenteredString(this.font, z_export.getMessage(), z_export.getX()+(z_export.getWidth()/2), z_export.getY()-z_export.getHeight()+9, 16777215);
        pGuiGraphics.drawCenteredString(this.font, portal_life_export.getMessage(), portal_life_export.getX()-(portal_life_export.getWidth()+20), portal_life_export.getY()+(portal_life_export.getHeight()/2 - 5), 16777215);
        pGuiGraphics.pose().popPose();

        int i = (this.width - this.width/2) / 2;
        int j = (this.height - this.height/2) / 2;
        //TODO: pozadí
        //pGuiGraphics.blit(new ResourceLocation("textures/gui/container/enchanting_table.png"), i, j, 0, 0, this.width/2, this.height);

        this.x_export.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.y_export.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.z_export.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.portal_life_export.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public Vector3i PortalIndexToVec3i(int n){
        String nString = String.format("%09d", n);
        Vector3i returnVector = new Vector3i();
        for(int i = 2; i < 9; i+=3){
            int temp = toInt(nString.charAt(i - 2) + String.valueOf(nString.charAt(i-1)) + nString.charAt(i), 0);
            if(i == 2){
                returnVector.z = temp;
            } else if (i == 5) {
                returnVector.y = temp;
            }else {
                returnVector.x = temp;
            }
        }
        return returnVector;
    }
    public void changeVec(int vecId, int increment){
        switch (vecId){
            case 0:
                PortalVector.x += increment;
                PortalVector.x = limitInt(PortalVector.x, 0, 999);
                x_export.setValue(String.valueOf(PortalVector.x));
                break;
            case 1:
                PortalVector.y += increment;
                PortalVector.y = limitInt(PortalVector.y, 0, 999);
                y_export.setValue(String.valueOf(PortalVector.y));
                break;
            case 2:
                PortalVector.z += increment;
                PortalVector.z = limitInt(PortalVector.z, 0, 999);
                z_export.setValue(String.valueOf(PortalVector.z));
                break;
        }
    }
    public int getIndexFromVector(Vector3i vec){
        String sIndex = String.format("%03d", vec.z) + String.format("%03d", vec.y) + String.format("%03d", vec.x);
        return toInt(sIndex, 0);
    }


    public void Close(){
        //saving NBT
        Vector3i saveVec = new Vector3i (
                toInt(this.x_export.getValue(), PortalVector.x),
                toInt(this.y_export.getValue(), PortalVector.y),
                toInt(this.z_export.getValue(), PortalVector.z));

        int sIndex = getIndexFromVector(saveVec);
        int sLifetime = limitInt(toInt(this.portal_life_export.getValue(), 10), 0, 99)*20;

        //PacketHandler.sendToServer(new csSendPortalGunNBT(sIndex, sLifetime, DisappearsAfterUse));


        this.minecraft.setScreen((Screen)null);
    }
    public void SwitchPortalDisappear(){
        //flip
        DisappearsAfterUse = !DisappearsAfterUse;

        if(DisappearsAfterUse){
            B_Disappears.setMessage(Component.translatable("screen.portal.disappearsAfterUse"));
        }else {
            B_Disappears.setMessage(Component.translatable("screen.portal.doesntDisappearAfterUse"));
        }
    }
    public Component getPortalDisappearButtonComponent(){
        if(DisappearsAfterUse) return Component.translatable("screen.portal.disappearsAfterUse");
        return Component.translatable("screen.portal.doesntDisappearAfterUse");
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
}
