package com.arrowfletching.client;

import com.arrowfletching.ArrowFletching;
import com.arrowfletching.entity.TntArrowEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class TntArrowRenderer extends TexturedArrowRenderer<TntArrowEntity> {
    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(ArrowFletching.MOD_ID, "textures/entity/tnt_arrow.png");

    public TntArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected Identifier texture() {
        return TEXTURE;
    }
}
