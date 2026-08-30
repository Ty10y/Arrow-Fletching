package com.arrowfletching.item;

import com.arrowfletching.ArrowFletching;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(ArrowFletching.MOD_ID);

    public static final DeferredItem<TntArrowItem> TNT_ARROW =
            ITEMS.registerItem("tnt_arrow", TntArrowItem::new);

    public static final DeferredItem<WaterArrowItem> WATER_ARROW =
            ITEMS.registerItem("water_arrow", WaterArrowItem::new);

    public static final DeferredItem<LavaArrowItem> LAVA_ARROW =
            ITEMS.registerItem("lava_arrow", LavaArrowItem::new);

    public static final DeferredItem<EnderArrowItem> ENDER_ARROW =
            ITEMS.registerItem("ender_arrow", EnderArrowItem::new);

    public static final DeferredItem<BeeArrowItem> BEE_ARROW =
            ITEMS.registerItem("bee_arrow", BeeArrowItem::new);

    public static final DeferredItem<WindArrowItem> WIND_ARROW =
            ITEMS.registerItem("wind_arrow", WindArrowItem::new);

    public static final DeferredItem<LightningArrowItem> LIGHTNING_ARROW =
            ITEMS.registerItem("lightning_arrow", LightningArrowItem::new);

    public static final DeferredItem<DripstoneArrowItem> DRIPSTONE_ARROW =
            ITEMS.registerItem("dripstone_arrow", DripstoneArrowItem::new);

    // --- Batch: effect arrows using the generic SimpleArrowItem ---
    public static final DeferredItem<SimpleArrowItem> CAGE_ARROW =
            ITEMS.registerItem("cage_arrow", p -> new SimpleArrowItem(p, com.arrowfletching.entity.CageArrowEntity::new));

    public static final DeferredItem<SimpleArrowItem> SLIME_ARROW =
            ITEMS.registerItem("slime_arrow", p -> new SimpleArrowItem(p, com.arrowfletching.entity.SlimeArrowEntity::new));

    public static final DeferredItem<SimpleArrowItem> FISHING_ROD_ARROW =
            ITEMS.registerItem("fishing_rod_arrow", p -> new SimpleArrowItem(p, com.arrowfletching.entity.FishingRodArrowEntity::new));

    public static final DeferredItem<SimpleArrowItem> LICHEN_ARROW =
            ITEMS.registerItem("lichen_arrow", p -> new SimpleArrowItem(p, com.arrowfletching.entity.LichenArrowEntity::new));

    // --- Ore/material damage tiers (share OreArrowEntity; damage set here) ---
    private static DeferredItem<SimpleArrowItem> ore(String id, double dmg) {
        return ITEMS.registerItem(id, p -> new SimpleArrowItem(p, com.arrowfletching.entity.OreArrowEntity::new, dmg));
    }
    public static final DeferredItem<SimpleArrowItem> STONE_ARROW     = ore("stone_arrow", 2.5);
    public static final DeferredItem<SimpleArrowItem> FLINT_ARROW     = ore("flint_arrow", 3.0);
    public static final DeferredItem<SimpleArrowItem> COPPER_ARROW    = ore("copper_arrow", 3.5);
    public static final DeferredItem<SimpleArrowItem> IRON_ARROW      = ore("iron_arrow", 4.5);
    public static final DeferredItem<SimpleArrowItem> GOLD_ARROW      = ore("gold_arrow", 5.5);
    public static final DeferredItem<SimpleArrowItem> DIAMOND_ARROW   = ore("diamond_arrow", 7.0);
    public static final DeferredItem<SimpleArrowItem> NETHERITE_ARROW = ore("netherite_arrow", 9.0);

    // --- Fish damage tiers (share FishArrowEntity; fly through water) ---
    private static DeferredItem<SimpleArrowItem> fish(String id, double dmg) {
        return ITEMS.registerItem(id, p -> new SimpleArrowItem(p, com.arrowfletching.entity.FishArrowEntity::new, dmg));
    }
    public static final DeferredItem<SimpleArrowItem> COD_ARROW        = fish("cod_arrow", 3.0);
    public static final DeferredItem<SimpleArrowItem> SALMON_ARROW     = fish("salmon_arrow", 4.0);
    public static final DeferredItem<SimpleArrowItem> PUFFERFISH_ARROW = fish("pufferfish_arrow", 5.0);

    // --- Mining tiers (share MiningArrowEntity; excavation size derived from item) ---
    // extraBowDamage scales with excavation size (size - 1): heavier tiers wear the bow faster.
    private static DeferredItem<SimpleArrowItem> mining(String id, int extraBowDamage) {
        return ITEMS.registerItem(id, p -> new SimpleArrowItem(p, com.arrowfletching.entity.MiningArrowEntity::new, -1.0, extraBowDamage));
    }
    public static final DeferredItem<SimpleArrowItem> WOODEN_MINING_ARROW    = mining("wooden_mining_arrow", 0);
    public static final DeferredItem<SimpleArrowItem> STONE_MINING_ARROW     = mining("stone_mining_arrow", 1);
    public static final DeferredItem<SimpleArrowItem> COPPER_MINING_ARROW    = mining("copper_mining_arrow", 2);
    public static final DeferredItem<SimpleArrowItem> IRON_MINING_ARROW      = mining("iron_mining_arrow", 3);
    public static final DeferredItem<SimpleArrowItem> GOLDEN_MINING_ARROW    = mining("golden_mining_arrow", 4);
    public static final DeferredItem<SimpleArrowItem> DIAMOND_MINING_ARROW   = mining("diamond_mining_arrow", 6);
    public static final DeferredItem<SimpleArrowItem> NETHERITE_MINING_ARROW = mining("netherite_mining_arrow", 10);

    // --- Homing ---
    public static final DeferredItem<SimpleArrowItem> MEMBRANE_ARROW =
            ITEMS.registerItem("membrane_arrow", p -> new SimpleArrowItem(p, com.arrowfletching.entity.MembraneArrowEntity::new));
}
