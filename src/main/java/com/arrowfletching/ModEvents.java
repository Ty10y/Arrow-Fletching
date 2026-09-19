package com.arrowfletching;

import com.arrowfletching.menu.FletchingTableMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.TriState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
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

    /**
     * Right-clicking a vanilla fletching table opens the custom arrow-upgrade menu.
     * The vanilla block has no menu of its own; we cancel the interaction and open ours
     * server-side, preserving the block's villager job-site behaviour and mod compat.
     */
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        if (!level.getBlockState(pos).is(Blocks.FLETCHING_TABLE)) {
            return;
        }
        Player player = event.getEntity();
        // Standard convention (chests, furnaces): sneaking with an item in hand skips the
        // menu so the held item's place/use action runs instead. Empty-handed crouch still opens.
        if (player.isSecondaryUseActive() && !player.getItemInHand(event.getHand()).isEmpty()) {
            return;
        }
        // Suppress BOTH the vanilla block interaction and the held item's use, exactly like a
        // crafting table. Without denying the item use, a held bow (or other usable item) would
        // start drawing and fire when the menu closes. Must run on the client too, or the client
        // predicts the item use before the server says otherwise.
        event.setUseBlock(TriState.FALSE);
        event.setUseItem(TriState.FALSE);
        if (level.isClientSide()) {
            return;
        }
        player.openMenu(new SimpleMenuProvider(
                (id, inv, p) -> new FletchingTableMenu(id, inv, ContainerLevelAccess.create(level, pos)),
                Component.translatable("container.arrow_fletching.fletching_table")));
    }
}
