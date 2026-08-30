package com.arrowfletching.item;

import com.arrowfletching.entity.BeeArrowEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/** The Bee Arrow item; fires a {@link BeeArrowEntity}. */
public class BeeArrowItem extends ArrowItem {

    public BeeArrowItem(Properties properties) {
        super(properties);
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack ammo, LivingEntity shooter, ItemStack weapon) {
        return new BeeArrowEntity(level, shooter, ammo.copyWithCount(1), weapon);
    }
}
