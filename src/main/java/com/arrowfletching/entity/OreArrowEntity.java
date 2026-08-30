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
 * Shared entity for the material-tier damage arrows (stone..netherite).
 * The item sets base damage; a synced variant index drives the per-tier flying texture.
 */
public class OreArrowEntity extends AbstractArrow implements TieredArrow {

    private static final EntityDataAccessor<Integer> DATA_VARIANT =
            SynchedEntityData.defineId(OreArrowEntity.class, EntityDataSerializers.INT);

    public OreArrowEntity(EntityType<? extends OreArrowEntity> type, Level level) {
        super(type, level);
    }

    public OreArrowEntity(Level level, LivingEntity shooter, ItemStack pickupItemStack, ItemStack firedFromWeapon) {
        super(ModEntities.ORE_ARROW.get(), shooter, level, pickupItemStack, firedFromWeapon);
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
        if (item == ModItems.FLINT_ARROW.get()) return 1;
        if (item == ModItems.COPPER_ARROW.get()) return 2;
        if (item == ModItems.IRON_ARROW.get()) return 3;
        if (item == ModItems.GOLD_ARROW.get()) return 4;
        if (item == ModItems.DIAMOND_ARROW.get()) return 5;
        if (item == ModItems.NETHERITE_ARROW.get()) return 6;
        return 0; // stone / default
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ModItems.FLINT_ARROW.get());
    }
}
