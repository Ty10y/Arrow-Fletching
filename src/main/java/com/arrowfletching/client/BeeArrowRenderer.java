package com.arrowfletching.client;

import com.arrowfletching.ArrowFletching;
import com.arrowfletching.entity.BeeArrowEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class BeeArrowRenderer extends TexturedArrowRenderer<BeeArrowEntity> {
    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(ArrowFletching.MOD_ID, "textures/entity/bee_arrow.png");

    public BeeArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected Identifier texture() {
        return TEXTURE;
    }
}
