package com.arrowfletching.entity;

import com.arrowfletching.ArrowFletching;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, ArrowFletching.MOD_ID);

    public static final Supplier<EntityType<TntArrowEntity>> TNT_ARROW =
            registerArrow("tnt_arrow", TntArrowEntity::new);

    public static final Supplier<EntityType<WaterArrowEntity>> WATER_ARROW =
            registerArrow("water_arrow", WaterArrowEntity::new);

    public static final Supplier<EntityType<LavaArrowEntity>> LAVA_ARROW =
            registerArrow("lava_arrow", LavaArrowEntity::new);

    public static final Supplier<EntityType<EnderArrowEntity>> ENDER_ARROW =
            registerArrow("ender_arrow", EnderArrowEntity::new);

    public static final Supplier<EntityType<BeeArrowEntity>> BEE_ARROW =
            registerArrow("bee_arrow", BeeArrowEntity::new);

    public static final Supplier<EntityType<WindArrowEntity>> WIND_ARROW =
            registerArrow("wind_arrow", WindArrowEntity::new);

    public static final Supplier<EntityType<LightningArrowEntity>> LIGHTNING_ARROW =
            registerArrow("lightning_arrow", LightningArrowEntity::new);

    public static final Supplier<EntityType<DripstoneArrowEntity>> DRIPSTONE_ARROW =
            registerArrow("dripstone_arrow", DripstoneArrowEntity::new);

    public static final Supplier<EntityType<CageArrowEntity>> CAGE_ARROW =
            registerArrow("cage_arrow", CageArrowEntity::new);

    public static final Supplier<EntityType<SlimeArrowEntity>> SLIME_ARROW =
            registerArrow("slime_arrow", SlimeArrowEntity::new);

    public static final Supplier<EntityType<FishingRodArrowEntity>> FISHING_ROD_ARROW =
            registerArrow("fishing_rod_arrow", FishingRodArrowEntity::new);

    public static final Supplier<EntityType<LichenArrowEntity>> LICHEN_ARROW =
            registerArrow("lichen_arrow", LichenArrowEntity::new);

    // Shared entity types for the tiered families + homing.
    public static final Supplier<EntityType<OreArrowEntity>> ORE_ARROW =
            registerArrow("ore_arrow", OreArrowEntity::new);

    public static final Supplier<EntityType<FishArrowEntity>> FISH_ARROW =
            registerArrow("fish_arrow", FishArrowEntity::new);

    public static final Supplier<EntityType<MiningArrowEntity>> MINING_ARROW =
            registerArrow("mining_arrow", MiningArrowEntity::new);

    // Homing arrow follows a server-driven curve the client can't predict, so it needs a
    // position update every tick — otherwise the client snaps to it once per second ("teleport").
    public static final Supplier<EntityType<MembraneArrowEntity>> MEMBRANE_ARROW =
            registerArrow("membrane_arrow", MembraneArrowEntity::new, 1);

    /** Registers an arrow-sized MISC entity type with the standard tracking settings. */
    private static <T extends net.minecraft.world.entity.Entity> Supplier<EntityType<T>> registerArrow(
            String name, EntityType.EntityFactory<T> factory) {
        return registerArrow(name, factory, 20);
    }

    private static <T extends net.minecraft.world.entity.Entity> Supplier<EntityType<T>> registerArrow(
            String name, EntityType.EntityFactory<T> factory, int updateInterval) {
        ResourceKey<EntityType<?>> key = ResourceKey.create(
                Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(ArrowFletching.MOD_ID, name));
        return ENTITY_TYPES.register(name,
                () -> EntityType.Builder.of(factory, MobCategory.MISC)
                        .sized(0.5F, 0.5F)
                        .clientTrackingRange(4)
                        .updateInterval(updateInterval)
                        .build(key));
    }
}
