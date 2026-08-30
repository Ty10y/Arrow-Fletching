package com.arrowfletching.entity;

import com.arrowfletching.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.bee.Bee;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

/** Arrow that spawns 3 bees on impact; if it struck a mob/player, the bees swarm it. */
public class BeeArrowEntity extends AbstractEffectArrowEntity {

    private static final int BEE_COUNT = 3;

    public BeeArrowEntity(EntityType<? extends BeeArrowEntity> type, Level level) {
        super(type, level);
    }

    public BeeArrowEntity(Level level, LivingEntity shooter, ItemStack pickupItemStack, ItemStack firedFromWeapon) {
        super(ModEntities.BEE_ARROW.get(), level, shooter, pickupItemStack, firedFromWeapon);
    }

    @Override
    protected void onLand(ServerLevel level, BlockPos landPos, @Nullable LivingEntity struck) {
        for (int i = 0; i < BEE_COUNT; i++) {
            Bee bee = EntityTypes.BEE.spawn(level, landPos, EntitySpawnReason.MOB_SUMMONED);
            if (bee != null && struck != null && struck.isAlive()) {
                bee.setTarget(struck);
                bee.setPersistentAngerTarget(EntityReference.of(struck));
                bee.startPersistentAngerTimer();
            }
        }
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ModItems.BEE_ARROW.get());
    }
}
