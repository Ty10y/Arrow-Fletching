package com.arrowfletching.client;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;

/**
 * A concrete arrow renderer whose texture is passed in, so any arrow entity can be
 * registered with {@code ctx -> new SimpleArrowRenderer(ctx, TEXTURE)} — no subclass needed.
 */
public class SimpleArrowRenderer extends ArrowRenderer<AbstractArrow, ArrowRenderState> {

    private final Identifier texture;

    public SimpleArrowRenderer(EntityRendererProvider.Context context, Identifier texture) {
        super(context);
        this.texture = texture;
    }

    @Override
    public ArrowRenderState createRenderState() {
        return new ArrowRenderState();
    }

    @Override
    protected Identifier getTextureLocation(ArrowRenderState state) {
        return texture;
    }
}
