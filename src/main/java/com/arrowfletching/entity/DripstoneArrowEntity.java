package com.arrowfletching.entity;

import com.arrowfletching.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/** Arrow that drops a 5x5 volley of pointed dripstone from 20 blocks up. */
public class DripstoneArrowEntity extends AbstractEffectArrowEntity {

    private static final int HEIGHT = 20;
    private static final int HALF = 2; // 5x5 -> -2..2
    // Mirrors a natural falling stalactite tip: 1.0 damage per fall block * min size (6), capped at 40.
    private static final float DAMAGE_PER_DISTANCE = 6.0F;
    private static final int MAX_DAMAGE = 40;

    public DripstoneArrowEntity(EntityType<? extends DripstoneArrowEntity> type, Level level) {
        super(type, level);
    }

    public DripstoneArrowEntity(Level level, LivingEntity shooter, ItemStack pickupItemStack, ItemStack firedFromWeapon) {
        super(ModEntities.DRIPSTONE_ARROW.get(), level, shooter, pickupItemStack, firedFromWeapon);
    }

    @Override
    protected void onLand(ServerLevel level, BlockPos landPos, @Nullable LivingEntity struck) {
        // Point the tips downward so they fall as stalactites rather than facing up.
        BlockState dripstone = Blocks.POINTED_DRIPSTONE.defaultBlockState()
                .setValue(PointedDripstoneBlock.TIP_DIRECTION, Direction.DOWN);
        for (int dx = -HALF; dx <= HALF; dx++) {
            for (int dz = -HALF; dz <= HALF; dz++) {
                BlockPos spawnPos = landPos.offset(dx, HEIGHT, dz);
                // fall() spawns a gravity-affected falling block and clears the source cell.
                FallingBlockEntity entity = FallingBlockEntity.fall(level, spawnPos, dripstone);
                // Match a natural falling stalactite tip so it actually hurts on impact.
                entity.setHurtsEntities(DAMAGE_PER_DISTANCE, MAX_DAMAGE);
            }
        }
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ModItems.DRIPSTONE_ARROW.get());
    }
}
