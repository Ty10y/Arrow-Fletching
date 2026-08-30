package com.arrowfletching.entity;

import com.arrowfletching.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/** Arrow for spelunking: grows glow lichen at the landing spot and adds bright hidden light. */
public class LichenArrowEntity extends AbstractEffectArrowEntity {

    private static final int LIGHT_LEVEL = 15; // brighter than lichen's natural 7

    public LichenArrowEntity(EntityType<? extends LichenArrowEntity> type, Level level) {
        super(type, level);
    }

    public LichenArrowEntity(Level level, LivingEntity shooter, ItemStack pickupItemStack, ItemStack firedFromWeapon) {
        super(ModEntities.LICHEN_ARROW.get(), level, shooter, pickupItemStack, firedFromWeapon);
    }

    @Override
    protected void onLand(ServerLevel level, BlockPos landPos, @Nullable LivingEntity struck) {
        // Grow glow lichen on any solid surfaces bordering the landing cell.
        if (!level.getBlockState(landPos).isAir()) {
            return;
        }
        BlockState lichen = Blocks.GLOW_LICHEN.defaultBlockState();
        boolean placedAnyFace = false;
        for (Direction dir : Direction.values()) {
            if (level.getBlockState(landPos.relative(dir)).isFaceSturdy(level, landPos.relative(dir), dir.getOpposite())) {
                lichen = lichen.setValue(MultifaceBlock.getFaceProperty(dir), true);
                placedAnyFace = true;
            }
        }
        if (!placedAnyFace) {
            return;
        }
        level.setBlock(landPos, lichen, Block.UPDATE_ALL);

        // Add strong, invisible light directly above the lichen. It is paired 1:1 with the
        // lichen (see ModEvents): breaking the lichen removes this light again.
        BlockPos lightPos = landPos.above();
        if (level.getBlockState(lightPos).isAir()) {
            level.setBlock(lightPos,
                    Blocks.LIGHT.defaultBlockState().setValue(LightBlock.LEVEL, LIGHT_LEVEL),
                    Block.UPDATE_ALL);
        }
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ModItems.LICHEN_ARROW.get());
    }
}
