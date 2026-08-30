package com.arrowfletching.item;

import com.arrowfletching.entity.WindArrowEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/** The Wind Arrow item; fires a {@link WindArrowEntity}. */
public class WindArrowItem extends ArrowItem {

    public WindArrowItem(Properties properties) {
        super(properties);
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack ammo, LivingEntity shooter, ItemStack weapon) {
        return new WindArrowEntity(level, shooter, ammo.copyWithCount(1), weapon);
    }
}
