package com.arrowfletching.entity;

import com.arrowfletching.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/** Arrow that launches nearby entities skyward, like a wind charge. */
public class WindArrowEntity extends AbstractEffectArrowEntity {

    private static final double RADIUS = 4.0;
    /** Upward velocity tuned to loft an entity roughly 10 blocks. */
    private static final double LAUNCH = 1.4;

    public WindArrowEntity(EntityType<? extends WindArrowEntity> type, Level level) {
        super(type, level);
    }

    public WindArrowEntity(Level level, LivingEntity shooter, ItemStack pickupItemStack, ItemStack firedFromWeapon) {
        super(ModEntities.WIND_ARROW.get(), level, shooter, pickupItemStack, firedFromWeapon);
    }

    @Override
    protected void onLand(ServerLevel level, BlockPos landPos, @Nullable LivingEntity struck) {
        Vec3 center = Vec3.atCenterOf(landPos);
        AABB area = new AABB(landPos).inflate(RADIUS);
        List<Entity> nearby = level.getEntitiesOfClass(Entity.class, area, e -> e != this && e instanceof LivingEntity);

        for (Entity entity : nearby) {
            if (entity.position().distanceToSqr(center) > RADIUS * RADIUS) {
                continue;
            }
            Vec3 motion = entity.getDeltaMovement();
            // Mostly straight up, with a gentle outward nudge so a stack doesn't perfectly overlap.
            Vec3 away = entity.position().subtract(center);
            double hx = away.x * 0.15;
            double hz = away.z * 0.15;
            entity.setDeltaMovement(motion.x + hx, LAUNCH, motion.z + hz);
            entity.hurtMarked = true; // forces the velocity to sync to player clients
            entity.resetFallDistance();
        }
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ModItems.WIND_ARROW.get());
    }
}
