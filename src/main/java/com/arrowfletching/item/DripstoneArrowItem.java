package com.arrowfletching.item;

import com.arrowfletching.entity.DripstoneArrowEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/** The Dripstone Arrow item; fires a {@link DripstoneArrowEntity}. */
public class DripstoneArrowItem extends ArrowItem {

    public DripstoneArrowItem(Properties properties) {
        super(properties);
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack ammo, LivingEntity shooter, ItemStack weapon) {
        return new DripstoneArrowEntity(level, shooter, ammo.copyWithCount(1), weapon);
    }
}
