package net.hatDealer.portalgunmod.items.custom;

import net.hatDealer.portalgunmod.entity.custom.PortalProjectileEntity;
import net.hatDealer.portalgunmod.entity.custom.PortalProjectileUnstableEntity;
import net.hatDealer.portalgunmod.items.ModItems;
import net.hatDealer.portalgunmod.items.custom.PortalProjectiles.PortalProjectileItem;
import net.hatDealer.portalgunmod.items.custom.PortalProjectiles.PortalProjectileItemUnstable;
import net.hatDealer.portalgunmod.util.ModTags;
import net.hatDealer.portalgunmod.util.PortalGunNBTReader;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public abstract class PortalgunItem extends ProjectileWeaponItem {
    public PortalgunItem(Properties pProperties){
        super(pProperties);
    }
    public static final Predicate<ItemStack> PORTAL_ONLY = (itemStack) -> {
        return itemStack.is(ModTags.Items.PORTAL_AMMO);
    };
    public static int getPortalLifetime(ItemStack pPortalGun) {
        return 160;
    }
    public static boolean getPortalDisappear(ItemStack pPortalGun) {
        return true;
    }
    public int getDefaultProjectileRange() {
        return 40;
    }
    public static float getChargeLevel(ItemStack pStack, LivingEntity pEntity, int speed){
        //portal shows empty if being held by a mob
        if(!(pEntity instanceof Player player)){
            return 0;
        }

        ItemStack ammo = getPortalGunProjectile(pStack, player);

        if(ammo.isEmpty()) return 0;
        if(ammo.getItem() == ModItems.PortalProjectileItem.get()) return 1;
        if(ammo.getItem() == ModItems.PortalProjectileUnstableItem.get()) return 2;

        return 0;
    }
    /**
     * Called when the player stops using an Item (stops holding the right mouse button).
     */
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
    }

    /**
     * How long it takes to use or consume an item
     */
    public int getUseDuration(ItemStack pStack) {
        return 100;
    }

    /**
     * Returns the action that specifies what animation to play when the item is being used.
     */
    public @NotNull UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
    }
    /**
     * Called to trigger the item's "innate" right click behavior. To handle when this item is used on a Block, see
     */
    public @NotNull InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack PortalGun = pPlayer.getItemInHand(pHand);
        boolean infinite_ammo = pPlayer.getAbilities().instabuild;

        ItemStack itemstack = PortalgunItem.getPortalGunProjectile(PortalGun, pPlayer);

        if (!itemstack.isEmpty() || infinite_ammo) {
            //generate portal ammo if using infinite ammo
            if (itemstack.isEmpty()) {
                itemstack = new ItemStack(ModItems.PortalProjectileItem.get());
            }
            //on server, create and launch projectile
            if (!pLevel.isClientSide) {
                CompoundTag PortalGunNBT = PortalGun.getOrCreateTag();

                Vec3i PortalPos = PortalGunNBTReader.GetPosFromPortalGunNBT(PortalGunNBT, pPlayer.position());
                String PortalDimKey = PortalGunNBTReader.GetDimKeyDromPortalGunNBT(PortalGunNBT);

                ArrowItem arrowitem = (ArrowItem) (itemstack.getItem());

                AbstractArrow abstractArrow;
                if(arrowitem instanceof PortalProjectileItem p){
                    abstractArrow = p.createArrow(pLevel, itemstack,
                            pPlayer, PortalDimKey, PortalPos, getPortalLifetime(PortalGun), getPortalDisappear(PortalGun));
                }else{
                    assert (arrowitem instanceof PortalProjectileItemUnstable);

                    abstractArrow = ((PortalProjectileItemUnstable)arrowitem).createArrow(pLevel, itemstack, pPlayer);
                }

                abstractArrow.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0F, 3.0F, 0.0F);

                pLevel.addFreshEntity(abstractArrow);

                pPlayer.getCooldowns().addCooldown(this, 20);

            }

            //play sound of portal device
            pLevel.playSound((Player) null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.CONDUIT_DEACTIVATE,
                    SoundSource.PLAYERS, 1.0F, 1.0F / (pLevel.getRandom().nextFloat() * 0.4F + 1.2F) + 2 * 0.5F);

            //remove ammo, if player doesnt have infinite ammo
            if (!infinite_ammo) {
                itemstack.shrink(1);
                if (itemstack.isEmpty()) {
                    pPlayer.getInventory().removeItem(itemstack);
                }
            }

            //infinite durability
            //player.awardStat(Stats.ITEM_USED.get(this));

            return InteractionResultHolder.success(pPlayer.getItemInHand(pHand));
        }
        else {
            return InteractionResultHolder.fail(pPlayer.getItemInHand(pHand));
        }
    }

    /**
     * Get the predicate to match ammunition when searching the player's inventory, not their main/offhand
     */
    public @NotNull Predicate<ItemStack> getAllSupportedProjectiles() {
        return PORTAL_ONLY;
    }
    public static ItemStack getPortalGunProjectile(ItemStack pShootable, Player pPlayer){
        if (!(pShootable.getItem() instanceof PortalgunItem)) {
            return ItemStack.EMPTY;
        } else {
            CompoundTag PortalGunNBT = pShootable.getOrCreateTag();
            boolean preferStableAmmo = PortalGunNBT.contains("stableAmmo") ? PortalGunNBT.getBoolean("stableAmmo") : true;
            Item PreferredAmmo = preferStableAmmo ? ModItems.PortalProjectileItem.get() : ModItems.PortalProjectileUnstableItem.get();

            Predicate<ItemStack> predicate = ((ProjectileWeaponItem)pShootable.getItem()).getSupportedHeldProjectiles();
            ItemStack itemstack = ProjectileWeaponItem.getHeldProjectile(pPlayer, predicate);

            if (!itemstack.isEmpty()) {
                return net.minecraftforge.common.ForgeHooks.getProjectile(pPlayer, pShootable, itemstack);
            } else {
                predicate = ((ProjectileWeaponItem)pShootable.getItem()).getAllSupportedProjectiles();

                for(int i = 0; i < pPlayer.getInventory().getContainerSize(); ++i) {
                    ItemStack itemstack1 = pPlayer.getInventory().getItem(i);
                    if (PreferredAmmo == itemstack1.getItem()) {
                        return net.minecraftforge.common.ForgeHooks.getProjectile(pPlayer, pShootable, itemstack1);
                    }
                }

                for(int i = 0; i < pPlayer.getInventory().getContainerSize(); ++i) {
                    ItemStack itemstack1 = pPlayer.getInventory().getItem(i);
                    if (predicate.test(itemstack1)) {
                        return net.minecraftforge.common.ForgeHooks.getProjectile(pPlayer, pShootable, itemstack1);
                    }
                }

                return net.minecraftforge.common.ForgeHooks.getProjectile(pPlayer, pShootable, pPlayer.getAbilities().instabuild ? new ItemStack(ModItems.PortalProjectileItem.get()) : ItemStack.EMPTY);
            }
        }
    }
}
