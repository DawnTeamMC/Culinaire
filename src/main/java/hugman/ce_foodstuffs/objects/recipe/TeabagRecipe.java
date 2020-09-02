package hugman.ce_foodstuffs.objects.recipe;

import hugman.ce_foodstuffs.init.CEFItems;
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

    public TeabagRecipe(Identifier id) {
        super(id);
    }

    @Override
    public boolean matches(CraftingInventory craftingInventory, World world) {
        boolean hasPaper  = false;
        boolean hasString = false;

        int currentPaperCount = 0;
        int stringLocation = 0;
        int occupiedSlots  = 0;
        int totalPotency   = 0;
        //Is used for paper, string and Ingredients to insure there aren't more ingredients than needed.

        //The paper is the anchor on which every other ingredients relative coordinates are calculated
        // the paper should always be in the bottom left of the 2x2 square (P = paper/S = string/I = Ingredient)
        // I S
        // P I

        //|1|23
        //|4|56
        //|7|89 height*x + 1

        // 12
        //|34|  height*(height-1)+1

        //Only works if crafting benches are as wide as they are tall

        for(int i = 0; i < craftingInventory.size(); ++i) {
            ItemStack itemStack = craftingInventory.getStack(i);
            if (!itemStack.isEmpty()) {
                occupiedSlots++;
            }
        }
        if (occupiedSlots > 4 || occupiedSlots < 3){return false;}
        //if too many items are in the box return false either 3 or 4 items must be in there
        //Later on for each occupiedSpot is reduced by 1 for each found ingredient so if occupiedSpots is > 0 at the end of the routine 1 item is missplaced

        for(int i = 0; i < craftingInventory.size(); ++i) {
            ItemStack itemStack = craftingInventory.getStack(i);
            if (!itemStack.isEmpty()) {
                if (STRING.test(itemStack)) {
                    hasString = true;
                    currentPaperCount++;
                    stringLocation = i;
                    occupiedSlots--;
                }
            }
        }

        //Check if string is in the right spot and there isn't too much string
        if (currentPaperCount == 1 && hasString){
            for (int i = 0; i < craftingInventory.getHeight(); i++) {
                if (stringLocation == craftingInventory.getHeight() * i + 1) {
                    return false;
                }
            }
            //Since crafting benches are always equal height and size we know that if we count from left to right each height*X will be a crafting slot on the left outer edge. (Where paper can be because string couldnt be left and down from there)
            for (int i = 0; i < craftingInventory.getHeight(); i++) {
                if (stringLocation==craftingInventory.getHeight()*(craftingInventory.getHeight()-1)+1+i){return false;}
                //craftingInventory.getHeight()*(craftingInventory.getHeight()-1) always produces the number of the lower left corner
            }


            ItemStack itemStack = craftingInventory.getStack(stringLocation - craftingInventory.getHeight() + 1);
            //craftingInventory.getHeight()-1 + the current slots location is always up and right of the item
            if (!itemStack.isEmpty()){
                if (PAPER.test(itemStack)) {
                    hasPaper = true;
                    occupiedSlots--;
                    //hasPaper indicates if there is paper and if its in the right spot if both a true hasPaper is true
                }
            }

            if (hasPaper){
                //Check the lower right ingredient
                itemStack = craftingInventory.getStack(stringLocation+1);
                if (!itemStack.isEmpty()) {
                    if (POTENCY1.test(itemStack)) {
                        totalPotency++;
                    } else if (POTENCY2.test(itemStack)) {
                        totalPotency = totalPotency + 2;
                    } else if (POTENCY3.test(itemStack)) {
                        totalPotency = totalPotency + 3;
                    } else {return false;}
                    occupiedSlots--;
                }

                //Check the upper left ingredient
                itemStack = craftingInventory.getStack(stringLocation-craftingInventory.getHeight()-1);
                if (!itemStack.isEmpty()) {
                    if (POTENCY1.test(itemStack)) {
                        totalPotency++;
                    } else if (POTENCY2.test(itemStack)) {
                        totalPotency = totalPotency + 2;
                    } else if (POTENCY3.test(itemStack)) {
                        totalPotency = totalPotency + 3;
                    } else {return false;}
                    occupiedSlots--;
                }

                if (totalPotency>3 || totalPotency < 2 || occupiedSlots > 0){
                    return false;
                }else {return true;}//haha true dat
            }else {return false;}
        }
        return false;
    }

    @Override
    public ItemStack craft(CraftingInventory inv) {
        //check which flavor the ingredient is- and add a tag to it
        //Add potency: if its 1 ingredient take that potency if its 2 ingredient just default it to 1 (any other combination shouldnt be craftable)
        //return teabag
        return null;
    }

    @Override
    public boolean fits(int width, int height) {return width * height >= 2;}

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
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
             POTENCY1 = Ingredient.ofItems(new ItemConvertible[]{Items.BEETROOT_SEEDS,Items.SUGAR,Items.BAMBOO,Items.CACTUS,Items.CARROT,Items.CRIMSON_FUNGUS,Items.NETHER_SPROUTS,Items.WARPED_FUNGUS,Items.RED_MUSHROOM,Items.BROWN_MUSHROOM,Items.PUMPKIN_SEEDS});
             POTENCY2 = Ingredient.ofItems(new ItemConvertible[]{Items.MELON_SEEDS,Items.SWEET_BERRIES,Items.COCOA_BEANS,Items.DEAD_BUSH,Items.CRIMSON_ROOTS,Items.WARPED_ROOTS,Items.SEAGRASS,Items.KELP,Items.FERN,Items.LARGE_FERN,Items.CHORUS_FRUIT,Items.GLOWSTONE_DUST});
             POTENCY3 = Ingredient.ofItems(new ItemConvertible[]{Items.HONEYCOMB,Items.WITHER_ROSE});

          PAPER = Ingredient.ofItems(new ItemConvertible[]{Items.PAPER});
        STRING = Ingredient.ofItems(new ItemConvertible[]{Items.STRING});
}
}
