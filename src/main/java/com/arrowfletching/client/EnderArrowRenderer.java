package com.arrowfletching.client;

import com.arrowfletching.ArrowFletching;
import com.arrowfletching.entity.EnderArrowEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class EnderArrowRenderer extends TexturedArrowRenderer<EnderArrowEntity> {
    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(ArrowFletching.MOD_ID, "textures/entity/ender_arrow.png");

    public EnderArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected Identifier texture() {
        return TEXTURE;
    }
}
