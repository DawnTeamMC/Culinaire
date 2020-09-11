package hugman.ce_foodstuffs.objects.recipe;

import hugman.ce_foodstuffs.init.CEFItems;
import hugman.ce_foodstuffs.init.data.CEFRecipeSerializers;
import hugman.ce_foodstuffs.objects.item.TeaBagItem;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class TeabagRecipe extends SpecialCraftingRecipe {
    private static final Ingredient PAPER;
    private static final Ingredient STRING;
    private static final Ingredient BITTER_FLAVOR;
    private static final Ingredient UMAMI_FLAVOR;
    private static final Ingredient SOUR_FLAVOR;
    private static final Ingredient SALTY_FLAVOR;
    private static final Ingredient SWEET_FLAVOR;
    private static final Ingredient WEIRD_FLAVOR;
    private static final Ingredient POTENCY1;
    private static final Ingredient POTENCY2;
    private static final Ingredient POTENCY3;
    private static final Ingredient POTENCY1AND1;

    public TeabagRecipe(Identifier id) {
        super(id);
    }

    @Override
    public boolean matches(CraftingInventory craftingInventory, World world) {
        boolean hasPaper = false;
        boolean hasString = false;
        boolean hasValidIngredients = true;

        int stringCount = 0;
        int stringLocation = 0;
        int paperCount = 0;
        int occupiedSlots = 0;
        int totalPotency = 0;

        for (int i = 0; i < craftingInventory.size(); ++i) {
            ItemStack itemStack = craftingInventory.getStack(i);
            if (!itemStack.isEmpty()) {
                occupiedSlots++;
            }
        }
        if (occupiedSlots > 4 || occupiedSlots < 2) {
            return false;
        }

        for (int i = 0; i < craftingInventory.size(); ++i) {
            ItemStack itemStack = craftingInventory.getStack(i);
            if (!itemStack.isEmpty()) {
                if (STRING.test(itemStack)) {
                    hasString = true;
                    stringCount++;
                    stringLocation = i;
                    occupiedSlots--;
                }
            }
        }
        if (stringCount > 1 || !hasString) {
            return false;
        }


        for (int i = 0; i < craftingInventory.size(); ++i) {
            ItemStack itemStack = craftingInventory.getStack(i);
            if (!itemStack.isEmpty()) {
                if (PAPER.test(itemStack)) {
                    //craftingInventory.getHeight()*(craftingInventory.getHeight()-1) always produces the number of the lower left corner
                    if (i == stringLocation + craftingInventory.getWidth() - 1) {
                        hasPaper = true;
                        occupiedSlots--;
                    }
                    paperCount++;
                }
            }
        }

        //left of String
        ItemStack itemStack = craftingInventory.getStack(stringLocation - 1);
        if (!itemStack.isEmpty()) {
            if (POTENCY1.test(itemStack)) {
                totalPotency++;
            } else if (POTENCY2.test(itemStack)) {
                totalPotency = totalPotency + 2;
            } else if (POTENCY3.test(itemStack)) {
                totalPotency = totalPotency + 3;
            } else if (POTENCY1AND1.test(itemStack)) {
                totalPotency = totalPotency + 3;
            } else {
                return false;
            }
            occupiedSlots--;
        }

        //below String
        itemStack = craftingInventory.getStack(stringLocation + craftingInventory.getHeight());
        if (!itemStack.isEmpty()) {
            if (POTENCY1.test(itemStack)) {
                totalPotency++;
            } else if (POTENCY2.test(itemStack)) {
                totalPotency = totalPotency + 2;
            } else if (POTENCY3.test(itemStack)) {
                totalPotency = totalPotency + 3;
            } else if (POTENCY1AND1.test(itemStack)) {
                totalPotency = totalPotency + 3;
            } else {
                return false;
            }
            occupiedSlots--;
        }
        //occupied slots gets +1 for every occupiedSpot in the crafting bench if one spot is left outside of the tea area you cant craft it lol
        if (occupiedSlots > 0) {
            return false;
        }
        if (totalPotency > 3 ||totalPotency <1) {return false;}

        return hasPaper && hasString;
    }

    @Override
    public ItemStack craft(CraftingInventory inv) {
        ItemStack teabag = new ItemStack(CEFItems.TEA_BAG);
        int totalPotency = 0;
        int itemCounter  = 0;
        for (int i = 0; i < inv.size(); ++i) {
            ItemStack itemStack = inv.getStack(i);
            if (!itemStack.isEmpty()) {
                itemCounter++;
            }
        }
        if (itemCounter == 3){

        } else if (itemCounter == 4){

        } else if (itemCounter == 2){
            
        }
        return teabag;
    }

    @Override
    public boolean fits(int width, int height) {return width * height >= 2;}

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CEFRecipeSerializers.TEABAG_MAKING;
    }

    static {
        //If any tea ingredient has 2 flavors just enter in both registers and then enter as potency 1 so it will get counted twice resulting in potency 2.
         SWEET_FLAVOR = Ingredient.ofItems(new ItemConvertible[]{Items.BEETROOT_SEEDS,Items.SUGAR,Items.MELON_SEEDS,Items.SWEET_BERRIES,Items.PUMPKIN_SEEDS,Items.HONEYCOMB});
        BITTER_FLAVOR = Ingredient.ofItems(new ItemConvertible[]{Items.BAMBOO,Items.CACTUS,Items.COCOA_BEANS,Items.DEAD_BUSH,Items.WITHER_ROSE,Items.GLOWSTONE_DUST});
         UMAMI_FLAVOR = Ingredient.ofItems(new ItemConvertible[]{Items.CARROT,Items.CRIMSON_FUNGUS,Items.CRIMSON_ROOTS});
         SALTY_FLAVOR = Ingredient.ofItems(new ItemConvertible[]{Items.NETHER_SPROUTS,Items.WARPED_FUNGUS,Items.WARPED_ROOTS,Items.SEAGRASS,Items.KELP,Items.PUMPKIN_SEEDS});
          SOUR_FLAVOR = Ingredient.ofItems(new ItemConvertible[]{Items.BROWN_MUSHROOM,Items.RED_MUSHROOM,Items.FERN,Items.LARGE_FERN,Items.CHORUS_FRUIT});
         WEIRD_FLAVOR = Ingredient.ofItems(new ItemConvertible[]{Items.CHORUS_FRUIT,Items.GLOWSTONE_DUST});

          //each item should be in 1 potency class max or else it would immediately be 3 or above so it wouldn't work
          //Potency 3 can only have 1 flavor
             POTENCY1 = Ingredient.ofItems(new ItemConvertible[]{Items.BEETROOT_SEEDS,Items.SUGAR,Items.BAMBOO,Items.CACTUS,Items.CARROT,Items.CRIMSON_FUNGUS,Items.NETHER_SPROUTS,Items.WARPED_FUNGUS,Items.RED_MUSHROOM,Items.BROWN_MUSHROOM});
             POTENCY2 = Ingredient.ofItems(new ItemConvertible[]{Items.MELON_SEEDS,Items.SWEET_BERRIES,Items.COCOA_BEANS,Items.DEAD_BUSH,Items.CRIMSON_ROOTS,Items.WARPED_ROOTS,Items.SEAGRASS,Items.KELP,Items.FERN,Items.LARGE_FERN,Items.CHORUS_FRUIT,Items.GLOWSTONE_DUST});
             POTENCY3 = Ingredient.ofItems(new ItemConvertible[]{Items.HONEYCOMB,Items.WITHER_ROSE});
         POTENCY1AND1 = Ingredient.ofItems(new ItemConvertible[]{Items.PUMPKIN_SEEDS});

          PAPER = Ingredient.ofItems(new ItemConvertible[]{Items.PAPER});
         STRING = Ingredient.ofItems(new ItemConvertible[]{Items.STRING});
}
}
