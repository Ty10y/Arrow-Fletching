package com.arrowfletching.entity;

import com.arrowfletching.item.ModItems;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Shared entity for the fish damage-tier arrows (cod/salmon/pufferfish).
 * Flies through water with no slowdown; a synced variant drives the per-tier flying texture.
 */
public class FishArrowEntity extends AbstractArrow implements TieredArrow {

    private static final EntityDataAccessor<Integer> DATA_VARIANT =
            SynchedEntityData.defineId(FishArrowEntity.class, EntityDataSerializers.INT);

    public FishArrowEntity(EntityType<? extends FishArrowEntity> type, Level level) {
        super(type, level);
    }

    public FishArrowEntity(Level level, LivingEntity shooter, ItemStack pickupItemStack, ItemStack firedFromWeapon) {
        super(ModEntities.FISH_ARROW.get(), shooter, level, pickupItemStack, firedFromWeapon);
        this.setVariant(variantFor(pickupItemStack.getItem()));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT, 0);
    }

    public void setVariant(int variant) {
        this.entityData.set(DATA_VARIANT, variant);
    }

    @Override
    public int getVariant() {
        return this.entityData.get(DATA_VARIANT);
    }

    /** Order must match the texture array in ClientSetup. */
    public static int variantFor(Item item) {
        if (item == ModItems.SALMON_ARROW.get()) return 1;
        if (item == ModItems.PUFFERFISH_ARROW.get()) return 2;
        return 0; // cod / default
    }

    @Override
    protected float getWaterInertia() {
        return 0.99F;
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ModItems.COD_ARROW.get());
    }
}
