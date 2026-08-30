package com.arrowfletching.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

/**
 * Base class for arrows that place a single block where they land.
 * Subclasses supply the block to place and their pickup item.
 */
public abstract class AbstractPlacingArrowEntity extends AbstractArrow {

    protected AbstractPlacingArrowEntity(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
    }

    protected AbstractPlacingArrowEntity(EntityType<? extends AbstractArrow> type, Level level,
                                         LivingEntity shooter, ItemStack pickupItemStack, ItemStack firedFromWeapon) {
        super(type, shooter, level, pickupItemStack, firedFromWeapon);
    }

    /** The block whose default (source) state is placed on impact. */
    protected abstract Block getPlacedBlock();

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);
        // Place into the open space in front of the face we struck.
        tryPlaceAndDiscard(hitResult.getBlockPos().relative(hitResult.getDirection()));
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
        // No block face to work from; place where the arrow currently is.
        tryPlaceAndDiscard(this.blockPosition());
    }

    private void tryPlaceAndDiscard(BlockPos pos) {
        if (!this.level().isClientSide()) {
            Level level = this.level();
            BlockState existing = level.getBlockState(pos);
            if (existing.isAir() || existing.canBeReplaced()) {
                level.setBlock(pos, getPlacedBlock().defaultBlockState(), Block.UPDATE_ALL);
            }
            this.discard();
        }
    }
}
