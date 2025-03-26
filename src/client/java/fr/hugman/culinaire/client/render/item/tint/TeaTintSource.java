package fr.hugman.culinaire.client.render.item.tint;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.culinaire.component.CulinaireComponentTypes;
import fr.hugman.culinaire.component.TeaTypesComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.item.tint.TintSource;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.ColorHelper;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record TeaTintSource(int defaultColor) implements TintSource {
	public static final MapCodec<TeaTintSource> CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(Codecs.RGB.fieldOf("default").forGetter(TeaTintSource::defaultColor)).apply(instance, TeaTintSource::new)
	);

	public TeaTintSource() {
		this(-13083194);
	}

	@Override
	public int getTint(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity user) {
		TeaTypesComponent component = stack.get(CulinaireComponentTypes.TEA_TYPES);
		return component != null
			? ColorHelper.fullAlpha(component.getColor(this.defaultColor))
			: ColorHelper.fullAlpha(this.defaultColor);
	}

	@Override
	public MapCodec<TeaTintSource> getCodec() {
		return CODEC;
	}
}
