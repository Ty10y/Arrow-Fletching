package com.arrowfletching;

import com.arrowfletching.entity.ModEntities;
import com.arrowfletching.item.ModItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraft.world.item.CreativeModeTabs;

/**
 * Main entrypoint for the Arrow Fletching mod.
 *
 * Adds new craftable arrows. The first is the TNT Arrow, which explodes a small
 * area where it lands.
 */
@Mod(ArrowFletching.MOD_ID)
public class ArrowFletching {
    public static final String MOD_ID = "arrow_fletching";

    public ArrowFletching(IEventBus modEventBus) {
        ModItems.ITEMS.register(modEventBus);
        ModEntities.ENTITY_TYPES.register(modEventBus);

        modEventBus.addListener(this::addCreative);

        // Game-bus handler: clean up the Lichen Arrow's hidden light when its lichen is broken.
        NeoForge.EVENT_BUS.addListener(ModEvents::onBlockBreak);
        // (BreakBlockEvent handler in ModEvents)
    }

    /** Put the TNT Arrow into the vanilla Combat creative tab. */
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(ModItems.TNT_ARROW);
            event.accept(ModItems.WATER_ARROW);
            event.accept(ModItems.LAVA_ARROW);
            event.accept(ModItems.ENDER_ARROW);
            event.accept(ModItems.BEE_ARROW);
            event.accept(ModItems.WIND_ARROW);
            event.accept(ModItems.LIGHTNING_ARROW);
            event.accept(ModItems.DRIPSTONE_ARROW);
            event.accept(ModItems.CAGE_ARROW);
            event.accept(ModItems.SLIME_ARROW);
            event.accept(ModItems.FISHING_ROD_ARROW);
            event.accept(ModItems.LICHEN_ARROW);
            // Ore/material damage tiers
            event.accept(ModItems.STONE_ARROW);
            event.accept(ModItems.FLINT_ARROW);
            event.accept(ModItems.COPPER_ARROW);
            event.accept(ModItems.IRON_ARROW);
            event.accept(ModItems.GOLD_ARROW);
            event.accept(ModItems.DIAMOND_ARROW);
            event.accept(ModItems.NETHERITE_ARROW);
            // Fish tiers
            event.accept(ModItems.COD_ARROW);
            event.accept(ModItems.SALMON_ARROW);
            event.accept(ModItems.PUFFERFISH_ARROW);
            // Mining tiers
            event.accept(ModItems.WOODEN_MINING_ARROW);
            event.accept(ModItems.STONE_MINING_ARROW);
            event.accept(ModItems.COPPER_MINING_ARROW);
            event.accept(ModItems.IRON_MINING_ARROW);
            event.accept(ModItems.GOLDEN_MINING_ARROW);
            event.accept(ModItems.DIAMOND_MINING_ARROW);
            event.accept(ModItems.NETHERITE_MINING_ARROW);
            // Homing
            event.accept(ModItems.MEMBRANE_ARROW);
        }
    }
}
