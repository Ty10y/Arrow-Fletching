package com.arrowfletching.entity;

import com.arrowfletching.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/** Arrow that boxes the struck target in iron bars (3x3 walls, 3 tall, with a roof). */
public class CageArrowEntity extends AbstractEffectArrowEntity {

    public CageArrowEntity(EntityType<? extends CageArrowEntity> type, Level level) {
        super(type, level);
    }

    public CageArrowEntity(Level level, LivingEntity shooter, ItemStack pickupItemStack, ItemStack firedFromWeapon) {
        super(ModEntities.CAGE_ARROW.get(), level, shooter, pickupItemStack, firedFromWeapon);
    }

    @Override
    protected void onLand(ServerLevel level, BlockPos landPos, @Nullable LivingEntity struck) {
        // Center on the mob if we hit one, otherwise on the landing block.
        BlockPos center = struck != null ? struck.blockPosition() : landPos;
        BlockState bars = Blocks.IRON_BARS.defaultBlockState();

        // Walls: the 8 perimeter columns, 3 blocks tall (center column left open for the mob).
        for (int y = 0; y <= 2; y++) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (dx == 0 && dz == 0) {
                        continue;
                    }
                    place(level, center.offset(dx, y, dz), bars);
                }
            }
        }
        // Roof: full 3x3 cap so it can't jump or fly out.
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                place(level, center.offset(dx, 3, dz), bars);
            }
        }
    }

    private static void place(ServerLevel level, BlockPos pos, BlockState state) {
        BlockState existing = level.getBlockState(pos);
        if (existing.isAir() || existing.canBeReplaced()) {
            level.setBlock(pos, state, net.minecraft.world.level.block.Block.UPDATE_ALL);
        }
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ModItems.CAGE_ARROW.get());
    }
}
