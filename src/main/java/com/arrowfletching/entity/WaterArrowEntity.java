package com.arrowfletching.entity;

import com.arrowfletching.item.ModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

/** Arrow that places a water source block where it lands. */
public class WaterArrowEntity extends AbstractPlacingArrowEntity {

    public WaterArrowEntity(EntityType<? extends WaterArrowEntity> type, Level level) {
        super(type, level);
    }

    public WaterArrowEntity(Level level, LivingEntity shooter, ItemStack pickupItemStack, ItemStack firedFromWeapon) {
        super(ModEntities.WATER_ARROW.get(), level, shooter, pickupItemStack, firedFromWeapon);
    }

    @Override
    protected Block getPlacedBlock() {
        return Blocks.WATER;
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ModItems.WATER_ARROW.get());
    }
}
