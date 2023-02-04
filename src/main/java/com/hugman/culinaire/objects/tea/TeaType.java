package com.hugman.culinaire.objects.tea;

import com.hugman.culinaire.Culinaire;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntryList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public record TeaType(TeaFlavor flavor, TeaPotency potency) {
	public static final String FLAVOR_NBT_KEY = "Flavor";
	public static final String POTENCY_NBT_KEY = "Potency";
	public static final String TYPES_NBT_KEY = "TeaTypes";

	public static TeaType withMiddlePotency(TeaFlavor flavor) {
		int i = flavor.potencies().size() / 2;
		return new TeaType(flavor, flavor.potencies().get(i));
	}

	/**
	 * Parses a {@link NbtCompound} into the corresponding tea type.
	 *
	 * @return resulting tea type, or {@code null} if the compound could not be parsed
	 */
	public static TeaType fromNbt(NbtCompound nbt) {
		try {
			TeaFlavor flavor = TeaFlavorManager.get(Identifier.tryParse(nbt.getString(FLAVOR_NBT_KEY)));
			TeaPotency potency = Objects.requireNonNull(flavor).getPotency(nbt.getInt(POTENCY_NBT_KEY));
			return new TeaType(flavor, potency);
		} catch(NullPointerException e) {
			Culinaire.LOGGER.error("Could not read tea type from NBT tag");
			return null;
		}
	}

	/**
	 * Appends tea types to the ingredients stack's NBT compound.
	 */
	public static void addToStack(ItemStack stack, List<TeaType> teaTypes) {
		NbtCompound nbt = stack.getOrCreateNbt();

		NbtList nbtList = new NbtList();
		if(nbt.contains(TYPES_NBT_KEY)) {
			nbtList = nbt.getList(TYPES_NBT_KEY, 10);
		}
		for(TeaType teaType : teaTypes) {
			nbtList.add(teaType.toNbt());
		}

		nbt.put(TYPES_NBT_KEY, nbtList);
	}

	/**
	 * Returns the tea types from the ingredients stack's NBT data.
	 */
	public static List<TeaType> fromStack(ItemStack stack) {
		List<TeaType> list = new ArrayList<>();
		NbtCompound nbt = stack.getNbt();
		if(nbt != null) {
			if(nbt.contains(TYPES_NBT_KEY)) {
				NbtList teaTypeList = nbt.getList(TYPES_NBT_KEY, 10);
				if(!teaTypeList.isEmpty()) {
					for(int i = 0; i < teaTypeList.size(); ++i) {
						TeaType teaType = TeaType.fromNbt(teaTypeList.getCompound(i));
						if(teaType != null) {
							list.add(teaType);
						}
					}
				}
			}
		}
		return list;
	}

	/**
	 * Appends a list of tea types to the tooltip of something.
	 */
	@Environment(EnvType.CLIENT)
	public static void appendTooltip(List<Text> tooltips, List<TeaType> teaTypes) {
		for(TeaType teaType : teaTypes) {
			tooltips.add(Text.translatable(teaType.potency().name()).formatted(Formatting.WHITE));
		}
		for(TeaType teaType : teaTypes) {
			tooltips.add(Text.translatable(teaType.potency().description()).formatted(Formatting.GRAY));
		}
	}

	public static List<TeaType> getAll() {
		List<TeaType> teaTypes = new ArrayList<>();


		TeaFlavorManager.getAll().forEach(flavor -> flavor.potencies().forEach(potency -> teaTypes.add(new TeaType(flavor, potency))));
		return teaTypes;
	}

	/**
	 * Gets the correct tea color of an {@link ItemStack} that contains tea NBT data.
	 */
	public static int getColor(ItemStack stack) {
		return getColor(fromStack(stack));
	}

	/**
	 * Gets the tea color from a list of {@link TeaType}.
	 */
	public static int getColor(List<TeaType> teaTypes) {
		if(teaTypes.isEmpty()) {
			return 15112486;
		}
		else {
			float f = 0.0F;
			float g = 0.0F;
			float h = 0.0F;
			int j = 0;
			for(TeaType teaType : teaTypes) {
				int k = teaType.flavor().color();
				int weight = teaType.potency().value();
				f += (float) (weight * (k >> 16 & 255)) / 255.0F;
				g += (float) (weight * (k >> 8 & 255)) / 255.0F;
				h += (float) (weight * (k >> 0 & 255)) / 255.0F;
				j += weight;
			}
			if(j == 0) {
				return 0;
			}
			else {
				f = f / (float) j * 255.0F;
				g = g / (float) j * 255.0F;
				h = h / (float) j * 255.0F;
				return (int) f << 16 | (int) g << 8 | (int) h;
			}
		}
	}

	/**
	 * Returns the tea types that accept a given ingredients as an ingredient.
	 */
	public static List<TeaType> getTypesOf(Item item) {
		List<TeaType> teaTypes = new ArrayList<>();
		for(TeaType teaType : TeaType.getAll()) {
			Optional<RegistryEntryList.Named<Item>> optional = Registry.ITEM.getEntryList(teaType.potency.ingredients());
			if(optional.isPresent()) {
				if(optional.get().contains(item.getRegistryEntry())) {
					teaTypes.add(teaType);
				}
			}
		}
		return teaTypes;
	}

	/**
	 * Translates the tea type to a {@link NbtCompound}
	 */
	public NbtCompound toNbt() {
		NbtCompound typeTag = new NbtCompound();
		try {

			Identifier flavorId = Objects.requireNonNull(TeaFlavorManager.getId((this.flavor)));
			typeTag.putString(FLAVOR_NBT_KEY, flavorId.toString());
			typeTag.putInt(POTENCY_NBT_KEY, this.potency.value());
			return typeTag;
		} catch(NullPointerException e) {
			Culinaire.LOGGER.error("Could not convert unknown tea type to NBT tag");
		}
		return typeTag;
	}

	/**
	 * Appends a tea type to the ingredients stack's NBT compound.
	 */
	public void addToStack(ItemStack stack) {
		addToStack(stack, new ArrayList<>(Collections.singleton(this)));
	}

	@Override
	public String toString() {
		return "TeaType{" + "flavor=" + this.flavor.toString() + ", flavor=" + this.potency.toString() + '}';
	}
}
