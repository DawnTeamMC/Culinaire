package com.hugman.culinaire.compat.rei;

import com.hugman.culinaire.Culinaire;
import com.hugman.culinaire.recipe.TeaBagMakingRecipe;
import com.hugman.culinaire.registry.content.TeaContent;
import com.hugman.culinaire.screen.KettleScreen;
import com.hugman.culinaire.tea.TeaHelper;
import com.hugman.culinaire.tea.TeaType;
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
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

public class CulinaireREIPlugin implements REIClientPlugin {
    public static final CategoryIdentifier<TeaBrewingDisplay> TEA_BREWING = CategoryIdentifier.of(Culinaire.id("plugins/tea_brewing"));
    private static final Identifier DISPLAY_TEXTURE = Culinaire.id("textures/gui/rei/display.png");
    private static final Identifier DARK_DISPLAY_TEXTURE = Culinaire.id("textures/gui/rei/dark_display.png");

    public static Identifier getDisplayTexture() {
        return REIRuntime.getInstance().isDarkThemeEnabled() ? DARK_DISPLAY_TEXTURE : DISPLAY_TEXTURE;
    }

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new TeaBrewingCategory());

        registry.setPlusButtonArea(TEA_BREWING, bounds -> null);

        registry.addWorkstations(CulinaireREIPlugin.TEA_BREWING, EntryStacks.of(TeaContent.KETTLE));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        this.registerTeaBagDisplays(registry);
        this.registerTeaBottleDisplays(registry);
    }

    private void registerTeaBagDisplays(DisplayRegistry registry) {
        for (TeaType teaType : TeaHelper.getAllTypes()) {
            DefaultedList<Ingredient> inputs = DefaultedList.of();
            inputs.add(TeaBagMakingRecipe.PAPER);
            inputs.add(TeaBagMakingRecipe.STRING);
            Ingredient ingredient = Ingredient.fromTag(teaType.getTag());
            if (!ingredient.isEmpty()) {
                inputs.add(ingredient);
                ItemStack output = TeaHelper.appendTeaType(new ItemStack(TeaContent.TEA_BAG), teaType);
                Identifier id = new Identifier("culinaire", teaType.getStrength().getName() + "_" + teaType.getFlavor().getName() + "_tea_bag");
                registry.add(new ShapelessRecipe(id, "tea_bags", CraftingRecipeCategory.MISC, output, inputs));
            }
        }
    }

    private void registerTeaBottleDisplays(DisplayRegistry registry) {
        for (TeaType teaType : TeaHelper.getAllTypes()) {
            ItemStack input = TeaHelper.appendTeaType(new ItemStack(TeaContent.TEA_BAG), teaType);
            ItemStack output = TeaHelper.appendTeaType(new ItemStack(TeaContent.TEA_BOTTLE), teaType);
            registry.add(new TeaBrewingDisplay(input, output, teaType.getFlavor().getColor()));
        }
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerContainerClickArea(new Rectangle(97, 16, 14, 30), KettleScreen.class, TEA_BREWING);
    }
}
