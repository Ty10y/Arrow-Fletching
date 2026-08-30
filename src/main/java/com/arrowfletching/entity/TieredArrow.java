package com.arrowfletching.entity;

/**
 * Implemented by shared-entity arrow families (ore/fish/mining) that render a different
 * flying texture per tier. The variant is synced to the client so the renderer can pick it.
 */
public interface TieredArrow {
    int getVariant();
}
