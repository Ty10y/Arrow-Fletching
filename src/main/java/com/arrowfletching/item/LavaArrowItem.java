package com.arrowfletching.item;

import com.arrowfletching.entity.LavaArrowEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/** The Lava Arrow item; fires a {@link LavaArrowEntity}. */
public class LavaArrowItem extends ArrowItem {

    public LavaArrowItem(Properties properties) {
        super(properties);
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack ammo, LivingEntity shooter, ItemStack weapon) {
        return new LavaArrowEntity(level, shooter, ammo.copyWithCount(1), weapon);
    }
}
