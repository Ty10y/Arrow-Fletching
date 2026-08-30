package com.arrowfletching.entity;

import com.arrowfletching.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

/** Arrow that calls down a lightning strike where it lands. */
public class LightningArrowEntity extends AbstractEffectArrowEntity {

    public LightningArrowEntity(EntityType<? extends LightningArrowEntity> type, Level level) {
        super(type, level);
    }

    public LightningArrowEntity(Level level, LivingEntity shooter, ItemStack pickupItemStack, ItemStack firedFromWeapon) {
        super(ModEntities.LIGHTNING_ARROW.get(), level, shooter, pickupItemStack, firedFromWeapon);
    }

    @Override
    protected void onLand(ServerLevel level, BlockPos landPos, @Nullable LivingEntity struck) {
        LightningBolt bolt = EntityTypes.LIGHTNING_BOLT.create(level, EntitySpawnReason.TRIGGERED);
        if (bolt != null) {
            bolt.setPos(landPos.getX() + 0.5, landPos.getY(), landPos.getZ() + 0.5);
            level.addFreshEntity(bolt);
        }
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ModItems.LIGHTNING_ARROW.get());
    }
}
