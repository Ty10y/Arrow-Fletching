package com.arrowfletching.entity;

import com.arrowfletching.item.ModItems;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Bursts into a firework where it lands, with a random shape and colors per shot.
 * Nearby mobs take vanilla firework-star damage (one star).
 */
public class FireworkArrowEntity extends AbstractEffectArrowEntity {

    /** Same id vanilla rockets use for "explode now". */
    private static final byte EVENT_BURST = 17;

    private static final double DAMAGE_RADIUS = 5.0;
    /** Vanilla rocket damage for one star: 5 + 2 per explosion. */
    private static final float BURST_DAMAGE = 7.0F;

    // Random seed chosen when fired; synced so the client builds the same firework as the server.
    private static final EntityDataAccessor<Integer> DATA_SEED =
            SynchedEntityData.defineId(FireworkArrowEntity.class, EntityDataSerializers.INT);

    public FireworkArrowEntity(EntityType<? extends FireworkArrowEntity> type, Level level) {
        super(type, level);
    }

    public FireworkArrowEntity(Level level, LivingEntity shooter, ItemStack pickupItemStack, ItemStack firedFromWeapon) {
        super(ModEntities.FIREWORK_ARROW.get(), level, shooter, pickupItemStack, firedFromWeapon);
        this.entityData.set(DATA_SEED, this.random.nextInt());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_SEED, 0);
    }

    @Override
    protected void onLand(ServerLevel level, BlockPos landPos, @Nullable LivingEntity struck) {
        // Sent before discard() so clients still have this entity when the event arrives.
        level.broadcastEntityEvent(this, EVENT_BURST);
        this.gameEvent(GameEvent.EXPLODE, this.getOwner());
        dealBurstDamage(level);
    }

    @Override
    public void handleEntityEvent(@EntityEvent.Value byte id) {
        if (id == EVENT_BURST && this.level().isClientSide()) {
            this.level().createFireworks(this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0,
                    List.of(randomExplosion(RandomSource.create(this.entityData.get(DATA_SEED)))), !this.isSilent());
        }
        super.handleEntityEvent(id);
    }

    /** Mirrors FireworkRocketEntity: falls off with distance and needs line of sight. */
    private void dealBurstDamage(ServerLevel level) {
        Vec3 center = this.position();
        var source = this.damageSources().source(DamageTypes.FIREWORKS, this, this.getOwner());
        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(DAMAGE_RADIUS))) {
            double distance = this.distanceTo(target);
            if (distance > DAMAGE_RADIUS || !canSee(level, center, target)) {
                continue;
            }
            target.hurtServer(level, source, BURST_DAMAGE * (float) Math.sqrt((DAMAGE_RADIUS - distance) / DAMAGE_RADIUS));
        }
    }

    private boolean canSee(ServerLevel level, Vec3 from, LivingEntity target) {
        for (int step = 0; step < 2; step++) {
            Vec3 to = new Vec3(target.getX(), target.getY(0.5 * step), target.getZ());
            if (level.clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this))
                    .getType() == HitResult.Type.MISS) {
                return true;
            }
        }
        return false;
    }

    /** Random shape, 1-3 colors, an optional fade color, and optional trail/twinkle. */
    private static FireworkExplosion randomExplosion(RandomSource random) {
        FireworkExplosion.Shape[] shapes = FireworkExplosion.Shape.values();
        DyeColor[] dyes = DyeColor.values();

        IntList colors = new IntArrayList();
        int colorCount = 1 + random.nextInt(3);
        for (int i = 0; i < colorCount; i++) {
            colors.add(dyes[random.nextInt(dyes.length)].getFireworkColor());
        }
        IntList fadeColors = new IntArrayList();
        if (random.nextBoolean()) {
            fadeColors.add(dyes[random.nextInt(dyes.length)].getFireworkColor());
        }

        return new FireworkExplosion(shapes[random.nextInt(shapes.length)], colors, fadeColors,
                random.nextBoolean(), random.nextBoolean());
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ModItems.FIREWORK_ARROW.get());
    }
}
