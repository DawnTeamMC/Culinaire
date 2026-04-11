package fr.hugman.culinaire.entity.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.GameEvent;

public class TeleportRandomlyStatusEffect extends StatusEffect {
    protected TeleportRandomlyStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {
        int diameter = Math.max(128, 16 + 8 * amplifier);
        boolean bl = false;

        for (int i = 0; i < 16; i++) {
            double d = entity.getX() + (entity.getRandom().nextDouble() - 0.5) * diameter;
            double e = MathHelper.clamp(
                    entity.getY() + (entity.getRandom().nextDouble() - 0.5) * diameter,
                    world.getBottomY(),
                    world.getBottomY() + world.getLogicalHeight() - 1
            );
            double f = entity.getZ() + (entity.getRandom().nextDouble() - 0.5) * diameter;
            if (entity.hasVehicle()) {
                entity.stopRiding();
            }

            Vec3d vec3d = entity.getEntityPos();
            if (entity.teleport(d, e, f, true)) {
                world.emitGameEvent(GameEvent.TELEPORT, vec3d, GameEvent.Emitter.of(entity));
                SoundCategory soundCategory;
                SoundEvent soundEvent;
                if (entity instanceof FoxEntity) {
                    soundEvent = SoundEvents.ENTITY_FOX_TELEPORT;
                    soundCategory = SoundCategory.NEUTRAL;
                } else {
                    soundEvent = SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT;
                    soundCategory = SoundCategory.PLAYERS;
                }

                world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), soundEvent, soundCategory);
                entity.onLanding();
                bl = true;
                break;
            }
        }

        if (bl && entity instanceof PlayerEntity playerEntity) {
            playerEntity.clearCurrentExplosion();
        }

        return bl;
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        int i = Math.max(15, 300 - (60 * amplifier));
        return i > 0 ? duration % i == 0 : true;
    }
}
