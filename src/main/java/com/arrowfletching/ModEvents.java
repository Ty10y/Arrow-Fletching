package com.arrowfletching;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;

/**
 * Game-bus event handlers. Registered on NeoForge.EVENT_BUS from the mod constructor.
 */
public final class ModEvents {

    private ModEvents() {
    }

    /**
     * The Lichen Arrow places a hidden light block directly above the glow lichen it grows.
     * When that lichen is broken, remove the paired light so it isn't left floating forever.
     */
    public static void onBlockBreak(BreakBlockEvent event) {
        if (!(event.getLevel() instanceof Level level) || level.isClientSide()) {
            return;
        }
        if (!event.getState().is(Blocks.GLOW_LICHEN)) {
            return;
        }
        BlockPos lightPos = event.getPos().above();
        if (level.getBlockState(lightPos).is(Blocks.LIGHT)) {
            level.setBlock(lightPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
        }
    }
}
