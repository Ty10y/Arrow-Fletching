package com.arrowfletching.client;

import com.arrowfletching.entity.TieredArrow;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;

/**
 * Renders a shared-entity arrow family (ore/fish/mining) with a per-tier flying texture,
 * chosen from the entity's synced variant index.
 */
public class VariantArrowRenderer extends ArrowRenderer<AbstractArrow, VariantArrowRenderer.State> {

    private final Identifier[] textures;

    public VariantArrowRenderer(EntityRendererProvider.Context context, Identifier[] textures) {
        super(context);
        this.textures = textures;
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(AbstractArrow entity, State state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        int variant = entity instanceof TieredArrow tiered ? tiered.getVariant() : 0;
        state.texture = textures[Math.floorMod(variant, textures.length)];
    }

    @Override
    protected Identifier getTextureLocation(State state) {
        return state.texture;
    }

    /** Render state that carries the resolved per-tier texture. */
    public static class State extends ArrowRenderState {
        public Identifier texture;
    }
}
