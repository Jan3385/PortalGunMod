package net.hatDealer.portalgunmod.items.custom;

import net.hatDealer.portalgunmod.entity.custom.PortalProjectileEntity;
import net.hatDealer.portalgunmod.items.ModItems;
import net.hatDealer.portalgunmod.util.ModTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.UseAnim;
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

    public static int getPortalIndex(ItemStack pPortalGun) {
        CompoundTag compoundtag = pPortalGun.getOrCreateTag();
        return compoundtag.getInt("index");
    }
    public static int getPortalLifetime(ItemStack pPortalGun) {
        CompoundTag compoundtag = pPortalGun.getOrCreateTag();
        compoundtag.putInt("pLife", 100); //TODO: temp
        return 100;
    }
    public static boolean getPortalDisappear(ItemStack pPortalGun) {
        CompoundTag compoundtag = pPortalGun.getOrCreateTag();
        return compoundtag.getBoolean("pDisappear");
    }
    public static float getChargeLevel(ItemStack pStack, LivingEntity pEntity, int speed){
        //portal shows empty if being held by a mob
        if(!(pEntity instanceof Player p)){
            return 0;
        }

        boolean hasAmmo = !p.getProjectile(ModItems.PortalGun.get().getDefaultInstance()).isEmpty();

        if (hasAmmo) return 1f;
        else return 0f;
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

    /**
     * Called to trigger the item's "innate" right click behavior. To handle when this item is used on a Block, see
     */
    public @NotNull InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack pStack = pPlayer.getItemInHand(pHand);
        boolean infinite_ammo = pPlayer.getAbilities().instabuild;
        ItemStack itemstack = pPlayer.getProjectile(pStack);

        if (!itemstack.isEmpty() || infinite_ammo) {
            //generate portal ammo if using infinite ammo
            if (itemstack.isEmpty()) {
                itemstack = new ItemStack(ModItems.PortalProjectileItem.get());
            }

            //on server, create and launch projectile
            if (!pLevel.isClientSide) {
                PortalProjectileItem arrowitem = (PortalProjectileItem)
                        (itemstack.getItem() instanceof PortalProjectileItem ? itemstack.getItem() : ModItems.PortalProjectileItem.get());
                PortalProjectileEntity abstractarrow = arrowitem.createArrow(pLevel, itemstack,
                        pPlayer, getPortalIndex(pStack), getPortalLifetime(pStack), getPortalDisappear(pStack));
                abstractarrow = customArrow(abstractarrow);

                abstractarrow.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0F, 3.0F, 0.0F);

                pLevel.addFreshEntity(abstractarrow);

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

    public PortalProjectileEntity customArrow(PortalProjectileEntity arrow) {
        return arrow;
    }

    public int getDefaultProjectileRange() {
        return 40;
    }
}
