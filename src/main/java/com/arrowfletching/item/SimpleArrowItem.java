package com.arrowfletching.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * A generic arrow item whose fired entity is supplied by an {@link ArrowEntityFactory},
 * optionally overriding base damage and charging extra bow durability. Lets most arrows
 * skip a dedicated item class.
 */
public class SimpleArrowItem extends ArrowItem {

    private final ArrowEntityFactory factory;
    private final double baseDamage;     // < 0 = leave the entity default
    private final int extraBowDamage;    // extra durability taken from the bow per shot

    public SimpleArrowItem(Properties properties, ArrowEntityFactory factory) {
        this(properties, factory, -1.0, 0);
    }

    public SimpleArrowItem(Properties properties, ArrowEntityFactory factory, double baseDamage) {
        this(properties, factory, baseDamage, 0);
    }

    public SimpleArrowItem(Properties properties, ArrowEntityFactory factory, double baseDamage, int extraBowDamage) {
        super(properties);
        this.factory = factory;
        this.baseDamage = baseDamage;
        this.extraBowDamage = extraBowDamage;
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack ammo, LivingEntity shooter, ItemStack weapon) {
        AbstractArrow arrow = factory.create(level, shooter, ammo.copyWithCount(1), weapon);
        if (this.baseDamage >= 0) {
            arrow.setBaseDamage(this.baseDamage);
        }
        // Charge extra bow wear for heavier arrows (on top of the vanilla 1 per shot).
        if (this.extraBowDamage > 0 && !level.isClientSide() && !weapon.isEmpty()) {
            weapon.hurtAndBreak(this.extraBowDamage, shooter, EquipmentSlot.MAINHAND);
        }
        return arrow;
    }
}
