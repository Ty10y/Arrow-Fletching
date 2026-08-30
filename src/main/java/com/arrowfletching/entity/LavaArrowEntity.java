package com.arrowfletching.entity;

import com.arrowfletching.item.ModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

/** Arrow that places a lava source block where it lands. */
public class LavaArrowEntity extends AbstractPlacingArrowEntity {

    public LavaArrowEntity(EntityType<? extends LavaArrowEntity> type, Level level) {
        super(type, level);
    }

    public LavaArrowEntity(Level level, LivingEntity shooter, ItemStack pickupItemStack, ItemStack firedFromWeapon) {
        super(ModEntities.LAVA_ARROW.get(), level, shooter, pickupItemStack, firedFromWeapon);
    }

    @Override
    protected Block getPlacedBlock() {
        return Blocks.LAVA;
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ModItems.LAVA_ARROW.get());
    }
}
