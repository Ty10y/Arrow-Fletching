package com.arrowfletching.item;

import com.arrowfletching.entity.EnderArrowEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/** The Ender Arrow item; fires an {@link EnderArrowEntity}. */
public class EnderArrowItem extends ArrowItem {

    public EnderArrowItem(Properties properties) {
        super(properties);
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack ammo, LivingEntity shooter, ItemStack weapon) {
        return new EnderArrowEntity(level, shooter, ammo.copyWithCount(1), weapon);
    }
}
