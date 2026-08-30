package com.arrowfletching.item;

import com.arrowfletching.entity.TntArrowEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * The TNT Arrow item. When fired from a bow/crossbow it spawns a
 * {@link TntArrowEntity}, which explodes on impact.
 */
public class TntArrowItem extends ArrowItem {

    public TntArrowItem(Properties properties) {
        super(properties);
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack ammo, LivingEntity shooter, ItemStack weapon) {
        return new TntArrowEntity(level, shooter, ammo.copyWithCount(1), weapon);
    }
}
