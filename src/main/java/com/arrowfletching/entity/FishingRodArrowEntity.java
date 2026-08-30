package com.arrowfletching.entity;

import com.arrowfletching.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/** Arrow that reels nearby dropped items back toward the shooter, like a fishing rod. */
public class FishingRodArrowEntity extends AbstractEffectArrowEntity {

    private static final double RADIUS = 6.0;

    public FishingRodArrowEntity(EntityType<? extends FishingRodArrowEntity> type, Level level) {
        super(type, level);
    }

    public FishingRodArrowEntity(Level level, LivingEntity shooter, ItemStack pickupItemStack, ItemStack firedFromWeapon) {
        super(ModEntities.FISHING_ROD_ARROW.get(), level, shooter, pickupItemStack, firedFromWeapon);
    }

    @Override
    protected void onLand(ServerLevel level, BlockPos landPos, @Nullable LivingEntity struck) {
        Entity owner = this.getOwner();
        if (owner == null) {
            return;
        }
        Vec3 target = owner.position().add(0, 0.5, 0);
        AABB area = new AABB(landPos).inflate(RADIUS);

        // Reel in dropped items.
        for (ItemEntity item : level.getEntitiesOfClass(ItemEntity.class, area)) {
            reel(item, target);
            item.setPickUpDelay(0);
        }

        // Reel in nearby mobs the same way (but not the shooter).
        for (LivingEntity mob : level.getEntitiesOfClass(LivingEntity.class, area, e -> e != owner)) {
            reel(mob, target);
            mob.hurtMarked = true;       // sync the yank to player clients
            mob.resetFallDistance();
        }
    }

    /** Lobs an entity toward the target point with a slight upward arc. */
    private static void reel(net.minecraft.world.entity.Entity entity, Vec3 target) {
        Vec3 toTarget = target.subtract(entity.position());
        double dist = toTarget.length();
        if (dist < 0.1) {
            return;
        }
        entity.setDeltaMovement(toTarget.scale(1.0 / dist).scale(0.9).add(0, 0.25, 0));
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ModItems.FISHING_ROD_ARROW.get());
    }
}
