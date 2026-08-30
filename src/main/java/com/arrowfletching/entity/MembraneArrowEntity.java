package com.arrowfletching.entity;

import com.arrowfletching.item.ModItems;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * Homing arrow: flies straight briefly, then steers toward the nearest mob.
 *
 * It keeps its launch speed constant and only rotates the direction of travel a little each
 * tick, so it visibly curves through the air instead of snapping to the target. The turn rate
 * scales with speed — a fast arrow covers ground quickly, so it must turn harder to still hit.
 */
public class MembraneArrowEntity extends AbstractArrow {

    private static final int START_DELAY = 5;          // ticks of straight flight before homing
    private static final double RANGE = 24.0;          // target search radius
    private static final double TURN_PER_SPEED = 0.13; // radians/tick per block/tick of speed
    private static final double MAX_TURN = 0.60;       // hard cap on turn per tick (radians)

    private int flightTicks = 0;
    private double launchSpeed = -1.0; // captured from the initial velocity, then held constant

    public MembraneArrowEntity(EntityType<? extends MembraneArrowEntity> type, Level level) {
        super(type, level);
    }

    public MembraneArrowEntity(Level level, LivingEntity shooter, ItemStack pickupItemStack, ItemStack firedFromWeapon) {
        super(ModEntities.MEMBRANE_ARROW.get(), shooter, level, pickupItemStack, firedFromWeapon);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide() || this.isInGround()) {
            return;
        }

        Vec3 velocity = this.getDeltaMovement();
        if (this.launchSpeed < 0) {
            double s = velocity.length();
            if (s > 1.0e-3) {
                this.launchSpeed = s; // remember the speed it was fired at
            }
        }

        flightTicks++;
        if (flightTicks < START_DELAY || this.launchSpeed < 0) {
            return;
        }

        LivingEntity target = findNearest();
        if (target == null) {
            return;
        }

        Vec3 heading = velocity.normalize();
        Vec3 toTarget = target.getBoundingBox().getCenter().subtract(this.position());
        if (toTarget.lengthSqr() < 1.0e-6) {
            return;
        }
        Vec3 desired = toTarget.normalize();

        // Rotate the heading toward the target by at most maxTurn radians this tick.
        double angle = Math.acos(Mth.clamp(heading.dot(desired), -1.0, 1.0));
        double maxTurn = Math.min(MAX_TURN, TURN_PER_SPEED * this.launchSpeed);
        double t = angle < 1.0e-4 ? 1.0 : Math.min(1.0, maxTurn / angle);

        Vec3 newHeading = heading.add(desired.subtract(heading).scale(t));
        if (newHeading.lengthSqr() < 1.0e-6) {
            newHeading = desired;
        }
        // Keep the launch speed — only the direction changes.
        this.setDeltaMovement(newHeading.normalize().scale(this.launchSpeed));
    }

    private LivingEntity findNearest() {
        Entity owner = this.getOwner();
        AABB box = this.getBoundingBox().inflate(RANGE);
        LivingEntity best = null;
        double bestDist = Double.MAX_VALUE;
        for (LivingEntity candidate : this.level().getEntitiesOfClass(LivingEntity.class, box,
                e -> e != owner && e.isAlive() && !e.isSpectator())) {
            double dist = candidate.distanceToSqr(this);
            if (dist < bestDist) {
                bestDist = dist;
                best = candidate;
            }
        }
        return best;
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ModItems.MEMBRANE_ARROW.get());
    }
}
