package com.arrowfletching.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/** Builds the arrow entity a {@link SimpleArrowItem} fires. */
@FunctionalInterface
public interface ArrowEntityFactory {
    AbstractArrow create(Level level, LivingEntity shooter, ItemStack ammo, ItemStack weapon);
}
