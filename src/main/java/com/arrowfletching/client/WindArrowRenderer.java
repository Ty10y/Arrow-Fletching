package com.arrowfletching.client;

import com.arrowfletching.ArrowFletching;
import com.arrowfletching.entity.WindArrowEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class WindArrowRenderer extends TexturedArrowRenderer<WindArrowEntity> {
    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(ArrowFletching.MOD_ID, "textures/entity/wind_arrow.png");

    public WindArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected Identifier texture() {
        return TEXTURE;
    }
}
