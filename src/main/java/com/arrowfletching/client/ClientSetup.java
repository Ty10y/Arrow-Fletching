package com.arrowfletching.client;

import com.arrowfletching.ArrowFletching;
import com.arrowfletching.entity.ModEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

/**
 * Client-only registrations. Runs on the mod event bus, physical client only.
 */
@EventBusSubscriber(modid = ArrowFletching.MOD_ID, value = Dist.CLIENT)
public class ClientSetup {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.TNT_ARROW.get(), TntArrowRenderer::new);
        event.registerEntityRenderer(ModEntities.WATER_ARROW.get(), WaterArrowRenderer::new);
        event.registerEntityRenderer(ModEntities.LAVA_ARROW.get(), LavaArrowRenderer::new);
        event.registerEntityRenderer(ModEntities.ENDER_ARROW.get(), EnderArrowRenderer::new);
        event.registerEntityRenderer(ModEntities.BEE_ARROW.get(), BeeArrowRenderer::new);
        event.registerEntityRenderer(ModEntities.WIND_ARROW.get(), WindArrowRenderer::new);
        event.registerEntityRenderer(ModEntities.LIGHTNING_ARROW.get(), LightningArrowRenderer::new);
        event.registerEntityRenderer(ModEntities.DRIPSTONE_ARROW.get(), DripstoneArrowRenderer::new);

        // Effect arrows share the generic renderer, differing only by texture.
        simple(event, ModEntities.CAGE_ARROW.get(), "cage_arrow");
        simple(event, ModEntities.SLIME_ARROW.get(), "slime_arrow");
        simple(event, ModEntities.FISHING_ROD_ARROW.get(), "fishing_rod_arrow");
        simple(event, ModEntities.LICHEN_ARROW.get(), "lichen_arrow");

        simple(event, ModEntities.MEMBRANE_ARROW.get(), "membrane_arrow");

        // Tier families: per-tier flying texture selected by the entity's synced variant.
        // Order MUST match the variantFor() indices in each entity class.
        variant(event, ModEntities.ORE_ARROW.get(),
                "stone_arrow", "flint_arrow", "copper_arrow", "iron_arrow", "gold_arrow", "diamond_arrow", "netherite_arrow");
        variant(event, ModEntities.FISH_ARROW.get(),
                "cod_arrow", "salmon_arrow", "pufferfish_arrow");
        variant(event, ModEntities.MINING_ARROW.get(),
                "wooden_mining_arrow", "stone_mining_arrow", "copper_mining_arrow", "iron_mining_arrow",
                "golden_mining_arrow", "diamond_mining_arrow", "netherite_mining_arrow");
    }

    private static void simple(EntityRenderersEvent.RegisterRenderers event,
                               net.minecraft.world.entity.EntityType<? extends net.minecraft.world.entity.projectile.arrow.AbstractArrow> type,
                               String name) {
        event.registerEntityRenderer(type, ctx -> new SimpleArrowRenderer(ctx, tex(name)));
    }

    private static void variant(EntityRenderersEvent.RegisterRenderers event,
                                net.minecraft.world.entity.EntityType<? extends net.minecraft.world.entity.projectile.arrow.AbstractArrow> type,
                                String... names) {
        net.minecraft.resources.Identifier[] textures = new net.minecraft.resources.Identifier[names.length];
        for (int i = 0; i < names.length; i++) {
            textures[i] = tex(names[i]);
        }
        event.registerEntityRenderer(type, ctx -> new VariantArrowRenderer(ctx, textures));
    }

    private static net.minecraft.resources.Identifier tex(String name) {
        return net.minecraft.resources.Identifier.fromNamespaceAndPath(ArrowFletching.MOD_ID, "textures/entity/" + name + ".png");
    }
}
