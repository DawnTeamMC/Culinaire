package fr.hugman.culinaire.tag;

import fr.hugman.culinaire.Culinaire;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class CulinaireItemTags {
    public static final TagKey<Item> PIES = of("pies");
    public static final TagKey<Item> BOWL_FOOD = of("bowl_food");

    public static final TagKey<Item> SANDWICHES = of("sandwiches");
    public static final TagKey<Item> SANDWICH_BREAD = of("sandwich_bread");
    public static final TagKey<Item> SANDWICH_INGREDIENT_BLACKLIST = of("sandwich_ingredient_blacklist");

    public static final TagKey<Item> GREEN_TEA_INGREDIENTS = of("tea_ingredients/green");
    public static final TagKey<Item> WHITE_TEA_INGREDIENTS = of("tea_ingredients/white");

    private static TagKey<Item> of(String path) {
        return TagKey.create(Registries.ITEM, Culinaire.id(path));
    }
}
