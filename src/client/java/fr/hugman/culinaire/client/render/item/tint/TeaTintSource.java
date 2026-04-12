package fr.hugman.culinaire.client.render.item.tint;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.culinaire.component.CulinaireComponentTypes;
import fr.hugman.culinaire.component.TeaTypesComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.ARGB;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record TeaTintSource(int defaultColor) implements ItemTintSource {
	public static final MapCodec<TeaTintSource> CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(ExtraCodecs.RGB_COLOR_CODEC.fieldOf("default").forGetter(TeaTintSource::defaultColor)).apply(instance, TeaTintSource::new)
	);

	public TeaTintSource() {
		this(-13083194);
	}

	@Override
	public int calculate(ItemStack stack, @Nullable ClientLevel world, @Nullable LivingEntity user) {
		TeaTypesComponent component = stack.get(CulinaireComponentTypes.TEA_TYPES);
		return component != null
			? ARGB.opaque(component.getColor(this.defaultColor))
			: ARGB.opaque(this.defaultColor);
	}

	@Override
	public MapCodec<TeaTintSource> type() {
		return CODEC;
	}
}
