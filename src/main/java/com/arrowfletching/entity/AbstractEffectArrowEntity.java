package com.arrowfletching.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

/**
 * Base class for arrows that run a server-side effect where they land and then vanish.
 * Subclasses implement {@link #onLand}; {@code struck} is the entity hit, or null for a block hit.
 */
public abstract class AbstractEffectArrowEntity extends AbstractArrow {

    protected AbstractEffectArrowEntity(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
    }

    protected AbstractEffectArrowEntity(EntityType<? extends AbstractArrow> type, Level level,
                                        LivingEntity shooter, ItemStack pickupItemStack, ItemStack firedFromWeapon) {
        super(type, shooter, level, pickupItemStack, firedFromWeapon);
    }

    protected abstract void onLand(ServerLevel level, BlockPos landPos, @Nullable LivingEntity struck);

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (this.level() instanceof ServerLevel serverLevel) {
            LivingEntity struck = (result instanceof EntityHitResult entityHit
                    && entityHit.getEntity() instanceof LivingEntity living) ? living : null;
            onLand(serverLevel, this.blockPosition(), struck);
            this.discard();
        }
    }
}
