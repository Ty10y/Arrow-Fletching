package com.arrowfletching.item;

import com.arrowfletching.entity.LightningArrowEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/** The Lightning Arrow item; fires a {@link LightningArrowEntity}. */
public class LightningArrowItem extends ArrowItem {

    public LightningArrowItem(Properties properties) {
        super(properties);
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack ammo, LivingEntity shooter, ItemStack weapon) {
        return new LightningArrowEntity(level, shooter, ammo.copyWithCount(1), weapon);
    }
}
