package com.hugman.culinaire.compat.rei;

import com.hugman.culinaire.Culinaire;
import com.hugman.culinaire.init.TeaBundle;
import com.hugman.culinaire.objects.recipe.TeaBagMakingRecipe;
import com.hugman.culinaire.objects.screen.KettleScreen;
import com.hugman.culinaire.objects.tea.TeaType;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.REIRuntime;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryEntryList;

import java.util.List;
import java.util.Optional;

public class CulinaireREIPlugin implements REIClientPlugin {
	public static final CategoryIdentifier<TeaBrewingDisplay> TEA_BREWING = CategoryIdentifier.of(Culinaire.MOD_DATA.id("plugins/tea_brewing"));
	private static final Identifier DISPLAY_TEXTURE = Culinaire.MOD_DATA.id("textures/gui/rei/display.png");
	private static final Identifier DARK_DISPLAY_TEXTURE = Culinaire.MOD_DATA.id("textures/gui/rei/dark_display.png");

	public static Identifier getDisplayTexture() {
		return REIRuntime.getInstance().isDarkThemeEnabled() ? DARK_DISPLAY_TEXTURE : DISPLAY_TEXTURE;
	}

	@Override
	public void registerCategories(CategoryRegistry registry) {
		registry.add(new TeaBrewingCategory());

		registry.removePlusButton(TEA_BREWING);

		registry.addWorkstations(CulinaireREIPlugin.TEA_BREWING, EntryStacks.of(TeaBundle.KETTLE));
	}

	@Override
	public void registerDisplays(DisplayRegistry registry) {
		this.registerTeaBagDisplays(registry);
		this.registerTeaBottleDisplays(registry);
	}

	private void registerTeaBagDisplays(DisplayRegistry registry) {
		for(TeaType teaType : TeaType.getAll()) {
			DefaultedList<Ingredient> inputs = DefaultedList.of();
			inputs.add(TeaBagMakingRecipe.PAPER);
			inputs.add(TeaBagMakingRecipe.STRING);

			Optional<RegistryEntryList.Named<Item>> optional = Registry.ITEM.getEntryList(teaType.potency().ingredients());

			if(optional.isPresent()) {
				List<Item> list = optional.get().stream().map(RegistryEntry::value).toList();
				if(!list.isEmpty()) {
					list.forEach(item -> inputs.add(Ingredient.ofItems(item)));
					ItemStack output = new ItemStack(TeaBundle.TEA_BAG);
					teaType.addToStack(output);
					Identifier id = Culinaire.MOD_DATA.id("tea_bag/" + teaType.flavor().getId().getPath() + "/" + teaType.potency().value());
					registry.add(new ShapelessRecipe(id, "tea_bags", output, inputs));
				}
			}
		}
	}

	private void registerTeaBottleDisplays(DisplayRegistry registry) {
		for(TeaType teaType : TeaType.getAll()) {
			ItemStack input = new ItemStack(TeaBundle.TEA_BAG);
			ItemStack output = new ItemStack(TeaBundle.TEA_BOTTLE);
			teaType.addToStack(input);
			teaType.addToStack(output);
			registry.add(new TeaBrewingDisplay(input, output, teaType.flavor().color()));
		}
	}

	@Override
	public void registerScreens(ScreenRegistry registry) {
		registry.registerContainerClickArea(new Rectangle(97, 16, 14, 30), KettleScreen.class, TEA_BREWING);
	}
}
