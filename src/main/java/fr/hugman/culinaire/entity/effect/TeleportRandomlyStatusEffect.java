package fr.hugman.culinaire.entity.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class TeleportRandomlyStatusEffect extends MobEffect {
    protected TeleportRandomlyStatusEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(ServerLevel world, LivingEntity entity, int amplifier) {
        int diameter = Math.max(128, 16 + 8 * amplifier);
        boolean bl = false;

        for (int i = 0; i < 16; i++) {
            double d = entity.getX() + (entity.getRandom().nextDouble() - 0.5) * diameter;
            double e = Mth.clamp(
                    entity.getY() + (entity.getRandom().nextDouble() - 0.5) * diameter,
                    world.getMinY(),
                    world.getMinY() + world.getLogicalHeight() - 1
            );
            double f = entity.getZ() + (entity.getRandom().nextDouble() - 0.5) * diameter;
            if (entity.isPassenger()) {
                entity.stopRiding();
            }

            Vec3 vec3d = entity.position();
            if (entity.randomTeleport(d, e, f, true)) {
                world.gameEvent(GameEvent.TELEPORT, vec3d, GameEvent.Context.of(entity));
                SoundSource soundCategory;
                SoundEvent soundEvent;
                if (entity instanceof Fox) {
                    soundEvent = SoundEvents.FOX_TELEPORT;
                    soundCategory = SoundSource.NEUTRAL;
                } else {
                    soundEvent = SoundEvents.CHORUS_FRUIT_TELEPORT;
                    soundCategory = SoundSource.PLAYERS;
                }

                world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), soundEvent, soundCategory);
                entity.resetFallDistance();
                bl = true;
                break;
            }
        }

        if (bl && entity instanceof Player playerEntity) {
            playerEntity.resetCurrentImpulseContext();
        }

        return bl;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        int i = Math.max(15, 300 - (60 * amplifier));
        return i > 0 ? duration % i == 0 : true;
    }
}
