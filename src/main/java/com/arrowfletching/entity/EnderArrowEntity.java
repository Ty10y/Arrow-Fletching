package com.arrowfletching.entity;

import com.arrowfletching.item.ModItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

/** Arrow that teleports its shooter to wherever it lands. */
public class EnderArrowEntity extends AbstractArrow {

    public EnderArrowEntity(EntityType<? extends EnderArrowEntity> type, Level level) {
        super(type, level);
    }

    public EnderArrowEntity(Level level, LivingEntity shooter, ItemStack pickupItemStack, ItemStack firedFromWeapon) {
        super(ModEntities.ENDER_ARROW.get(), shooter, level, pickupItemStack, firedFromWeapon);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ModItems.ENDER_ARROW.get());
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (this.level().isClientSide()) {
            return;
        }

        Entity owner = this.getOwner();
        if (owner instanceof LivingEntity living && owner.level() == this.level()) {
            double x = this.getX();
            double y = this.getY();
            double z = this.getZ();

            this.level().playSound(null, x, y, z, SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS);
            living.teleportTo(x, y, z);
            living.resetFallDistance();
            this.level().playSound(null, x, y, z, SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS);
        }

        this.discard();
    }
}
