package com.arrowfletching.client;

import com.arrowfletching.ArrowFletching;
import com.arrowfletching.entity.WaterArrowEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class WaterArrowRenderer extends TexturedArrowRenderer<WaterArrowEntity> {
    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(ArrowFletching.MOD_ID, "textures/entity/water_arrow.png");

    public WaterArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected Identifier texture() {
        return TEXTURE;
    }
}
