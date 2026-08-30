package com.arrowfletching.entity;

import com.arrowfletching.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * Shared entity for the mining arrows. Excavates an NxN area (single layer) where it lands;
 * N is derived from which tier item was fired, so it persists with the arrow's pickup stack.
 */
public class MiningArrowEntity extends AbstractEffectArrowEntity implements TieredArrow {

    private static final EntityDataAccessor<Integer> DATA_VARIANT =
            SynchedEntityData.defineId(MiningArrowEntity.class, EntityDataSerializers.INT);

    public MiningArrowEntity(EntityType<? extends MiningArrowEntity> type, Level level) {
        super(type, level);
    }

    public MiningArrowEntity(Level level, LivingEntity shooter, ItemStack pickupItemStack, ItemStack firedFromWeapon) {
        super(ModEntities.MINING_ARROW.get(), level, shooter, pickupItemStack, firedFromWeapon);
        this.setVariant(variantFor(pickupItemStack.getItem()));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT, 0);
    }

    public void setVariant(int variant) {
        this.entityData.set(DATA_VARIANT, variant);
    }

    @Override
    public int getVariant() {
        return this.entityData.get(DATA_VARIANT);
    }

    /** Order must match the texture array in ClientSetup. */
    public static int variantFor(Item item) {
        if (item == ModItems.STONE_MINING_ARROW.get()) return 1;
        if (item == ModItems.COPPER_MINING_ARROW.get()) return 2;
        if (item == ModItems.IRON_MINING_ARROW.get()) return 3;
        if (item == ModItems.GOLDEN_MINING_ARROW.get()) return 4;
        if (item == ModItems.DIAMOND_MINING_ARROW.get()) return 5;
        if (item == ModItems.NETHERITE_MINING_ARROW.get()) return 6;
        return 0; // wooden / default
    }

    @Override
    protected void onLand(ServerLevel level, BlockPos landPos, @Nullable LivingEntity struck) {
        Item tier = this.getPickupItem().getItem();
        int n = sizeFor(tier);
        TagKey<Block> tooWeakFor = incorrectFor(tier);
        int lo = -(n / 2);
        int hi = lo + n - 1;
        // NxNxN cube centered on the landing block.
        for (int dx = lo; dx <= hi; dx++) {
            for (int dy = lo; dy <= hi; dy++) {
                for (int dz = lo; dz <= hi; dz++) {
                    BlockPos pos = landPos.offset(dx, dy, dz);
                    BlockState state = level.getBlockState(pos);
                    if (state.isAir() || state.getDestroySpeed(level, pos) < 0) {
                        continue; // skip air and unbreakable blocks (bedrock, etc.)
                    }
                    if (state.is(tooWeakFor)) {
                        continue; // this tier's pickaxe can't harvest it — leave it floating
                    }
                    level.destroyBlock(pos, true, this, 512);
                }
            }
        }
    }

    /** Maps the fired tier item to the excavation width. */
    private static int sizeFor(Item item) {
        if (item == ModItems.STONE_MINING_ARROW.get()) return 2;
        if (item == ModItems.COPPER_MINING_ARROW.get()) return 3;
        if (item == ModItems.IRON_MINING_ARROW.get()) return 4;
        if (item == ModItems.GOLDEN_MINING_ARROW.get()) return 5;
        if (item == ModItems.DIAMOND_MINING_ARROW.get()) return 7;
        if (item == ModItems.NETHERITE_MINING_ARROW.get()) return 11;
        return 1; // wooden / default
    }

    /** The tag of blocks this tier's pickaxe is too weak to harvest (matches vanilla mining levels). */
    private static TagKey<Block> incorrectFor(Item item) {
        if (item == ModItems.STONE_MINING_ARROW.get()) return BlockTags.INCORRECT_FOR_STONE_TOOL;
        if (item == ModItems.COPPER_MINING_ARROW.get()) return BlockTags.INCORRECT_FOR_COPPER_TOOL;
        if (item == ModItems.IRON_MINING_ARROW.get()) return BlockTags.INCORRECT_FOR_IRON_TOOL;
        // Gold pickaxes are wood-level in vanilla, but this arrow's size/cost sits at iron tier,
        // so give it iron-level harvesting to match.
        if (item == ModItems.GOLDEN_MINING_ARROW.get()) return BlockTags.INCORRECT_FOR_IRON_TOOL;
        if (item == ModItems.DIAMOND_MINING_ARROW.get()) return BlockTags.INCORRECT_FOR_DIAMOND_TOOL;
        if (item == ModItems.NETHERITE_MINING_ARROW.get()) return BlockTags.INCORRECT_FOR_NETHERITE_TOOL;
        return BlockTags.INCORRECT_FOR_WOODEN_TOOL; // wooden / default
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ModItems.WOODEN_MINING_ARROW.get());
    }
}
