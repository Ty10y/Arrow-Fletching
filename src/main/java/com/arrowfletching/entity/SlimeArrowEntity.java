package com.arrowfletching.entity;

import com.arrowfletching.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

/** Arrow that slows the struck target to a crawl for ~5 seconds. */
public class SlimeArrowEntity extends AbstractEffectArrowEntity {

    private static final int DURATION_TICKS = 100; // 5 seconds
    private static final int AMPLIFIER = 6;        // very heavy slowdown

    public SlimeArrowEntity(EntityType<? extends SlimeArrowEntity> type, Level level) {
        super(type, level);
    }

    public SlimeArrowEntity(Level level, LivingEntity shooter, ItemStack pickupItemStack, ItemStack firedFromWeapon) {
        super(ModEntities.SLIME_ARROW.get(), level, shooter, pickupItemStack, firedFromWeapon);
    }

    @Override
    protected void onLand(ServerLevel level, BlockPos landPos, @Nullable LivingEntity struck) {
        if (struck != null) {
            struck.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, DURATION_TICKS, AMPLIFIER), this.getOwner());
        }
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ModItems.SLIME_ARROW.get());
    }
}
