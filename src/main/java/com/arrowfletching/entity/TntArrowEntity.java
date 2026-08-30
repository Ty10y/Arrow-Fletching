package com.arrowfletching.entity;

import com.arrowfletching.item.ModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

/**
 * An arrow that triggers a small explosion where it lands.
 */
public class TntArrowEntity extends AbstractArrow {

    /** Radius of the explosion. Vanilla TNT is 4.0F; this is deliberately smaller. */
    private static final float EXPLOSION_RADIUS = 2.0F;

    // Constructor used by the entity type / deserialization.
    public TntArrowEntity(EntityType<? extends TntArrowEntity> type, Level level) {
        super(type, level);
    }

    // Constructor used when a player shoots the arrow.
    public TntArrowEntity(Level level, LivingEntity shooter, ItemStack pickupItemStack, ItemStack firedFromWeapon) {
        super(ModEntities.TNT_ARROW.get(), shooter, level, pickupItemStack, firedFromWeapon);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ModItems.TNT_ARROW.get());
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide()) {
            this.level().explode(
                    this,
                    this.getX(), this.getY(), this.getZ(),
                    EXPLOSION_RADIUS,
                    Level.ExplosionInteraction.TNT
            );
            this.discard();
        }
    }
}
