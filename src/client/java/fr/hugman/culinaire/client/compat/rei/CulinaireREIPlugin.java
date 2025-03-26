package fr.hugman.culinaire.client.compat.rei;

import fr.hugman.culinaire.Culinaire;
import fr.hugman.culinaire.block.CulinaireBlocks;
import fr.hugman.culinaire.client.screen.KettleScreen;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.REIRuntime;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.util.Identifier;

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

        registry.addWorkstations(CulinaireREIPlugin.TEA_BREWING, EntryStacks.of(CulinaireBlocks.KETTLE));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        this.registerTeaBagDisplays(registry);
        this.registerTeaBottleDisplays(registry);
    }

    private void registerTeaBagDisplays(DisplayRegistry registry) {
        // FIXME
            /*
        for (OldTeaType teaType : TeaHelper.getAllTypes()) {
            DefaultedList<Ingredient> inputs = DefaultedList.of();
            inputs.add(TeaBagRecipe.PAPER);
            inputs.add(TeaBagRecipe.STRING);
            Ingredient ingredient = Ingredient.fromTag(teaType.getTagKey());
            if (!ingredient.isEmpty()) {
                inputs.add(ingredient);
                var stack = new ItemStack(CulinaireItems.TEA_BAG);
                stack.set(CulinaireComponentTypes.TEA_CONTENTS, List.of(teaType));
                registry.add(new ShapelessRecipe("tea_bags", CraftingRecipeCategory.MISC, stack, inputs));
            }

        }
             */
    }

    private void registerTeaBottleDisplays(DisplayRegistry registry) {
        //FIXME
        /*
        for (OldTeaType teaType : TeaHelper.getAllTypes()) {
            var input = new ItemStack(CulinaireItems.TEA_BAG);
            var output = new ItemStack(CulinaireItems.TEA_BOTTLE);
            input.set(CulinaireComponentTypes.TEA_CONTENTS, List.of(teaType));
            output.set(CulinaireComponentTypes.TEA_CONTENTS, List.of(teaType));
            registry.add(new TeaBrewingDisplay(input, output, teaType.getFlavor().getColor()));
        }
         */
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerContainerClickArea(new Rectangle(97, 16, 14, 30), KettleScreen.class, TEA_BREWING);
    }
}
