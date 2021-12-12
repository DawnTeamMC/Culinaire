package com.hugman.culinaire.compat.rei;

import com.hugman.culinaire.Culinaire;
import com.hugman.culinaire.init.CulinaireTeaBundle;
import com.hugman.culinaire.objects.item.tea.TeaHelper;
import com.hugman.culinaire.objects.item.tea.TeaType;
import com.hugman.culinaire.objects.recipe.TeaBagMakingRecipe;
import com.hugman.culinaire.objects.screen.KettleScreen;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.REIRuntime;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

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

		registry.addWorkstations(CulinaireREIPlugin.TEA_BREWING, EntryStacks.of(CulinaireTeaBundle.KETTLE));
	}

	@Override
	public void registerDisplays(DisplayRegistry registry) {
		this.registerTeaBagDisplays(registry);
		this.registerTeaBottleDisplays(registry);
	}

	private void registerTeaBagDisplays(DisplayRegistry registry) {
		for(TeaType teaType : TeaHelper.getAllTypes()) {
			DefaultedList<Ingredient> inputs = DefaultedList.of();
			inputs.add(TeaBagMakingRecipe.PAPER);
			inputs.add(TeaBagMakingRecipe.STRING);
			if(!teaType.getTag().values().isEmpty()) {
				inputs.add(Ingredient.fromTag(teaType.getTag()));
				ItemStack output = TeaHelper.appendTeaType(new ItemStack(CulinaireTeaBundle.TEA_BAG), teaType);
				Identifier id = new Identifier("culinaire", teaType.getStrength().getName() + "_" + teaType.getFlavor().getName() + "_tea_bag");
				registry.add(new ShapelessRecipe(id, "tea_bags", output, inputs));
			}
		}
	}

	private void registerTeaBottleDisplays(DisplayRegistry registry) {
		for(TeaType teaType : TeaHelper.getAllTypes()) {
			ItemStack input = TeaHelper.appendTeaType(new ItemStack(CulinaireTeaBundle.TEA_BAG), teaType);
			ItemStack output = TeaHelper.appendTeaType(new ItemStack(CulinaireTeaBundle.TEA_BOTTLE), teaType);
			registry.add(new TeaBrewingDisplay(input, output, teaType.getFlavor().getColor()));
		}
	}

	@Override
	public void registerScreens(ScreenRegistry registry) {
		registry.registerContainerClickArea(new Rectangle(97, 16, 14, 30), KettleScreen.class, TEA_BREWING);
	}
}
