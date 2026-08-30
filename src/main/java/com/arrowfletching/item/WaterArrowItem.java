package com.arrowfletching.item;

import com.arrowfletching.entity.WaterArrowEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/** The Water Arrow item; fires a {@link WaterArrowEntity}. */
public class WaterArrowItem extends ArrowItem {

    public WaterArrowItem(Properties properties) {
        super(properties);
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack ammo, LivingEntity shooter, ItemStack weapon) {
        return new WaterArrowEntity(level, shooter, ammo.copyWithCount(1), weapon);
    }
}
