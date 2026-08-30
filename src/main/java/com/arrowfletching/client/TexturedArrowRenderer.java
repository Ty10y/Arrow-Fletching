package com.arrowfletching.client;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;

/**
 * Renders any of this mod's arrows with the vanilla arrow model and a per-arrow texture.
 */
public abstract class TexturedArrowRenderer<T extends AbstractArrow> extends ArrowRenderer<T, ArrowRenderState> {

    public TexturedArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    /** The texture for this arrow. */
    protected abstract Identifier texture();

    @Override
    public ArrowRenderState createRenderState() {
        return new ArrowRenderState();
    }

    @Override
    protected Identifier getTextureLocation(ArrowRenderState state) {
        return texture();
    }
}
