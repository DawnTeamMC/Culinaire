package hugman.ce_foodstuffs.objects.recipe;

import hugman.ce_foodstuffs.init.CEFItems;
import hugman.ce_foodstuffs.init.data.CEFRecipeSerializers;
import hugman.ce_foodstuffs.init.data.CEFTags;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class TeabagMakingRecipe extends SpecialCraftingRecipe {
	private static final Ingredient PAPER = Ingredient.fromTag(CEFTags.Items.PAPER);
	private static final Ingredient STRING = Ingredient.fromTag(CEFTags.Items.STRING);

	//If any tea ingredient has 2 flavors just enter in both registers and then enter as potency 1 so it will get counted twice resulting in potency 2.
	private static final Ingredient SWEET_FLAVOR = Ingredient.ofItems(Items.BEETROOT_SEEDS, Items.SUGAR, Items.MELON_SEEDS, Items.SWEET_BERRIES, Items.PUMPKIN_SEEDS, Items.HONEYCOMB);
	private static final Ingredient BITTER_FLAVOR = Ingredient.ofItems(Items.BAMBOO, Items.CACTUS, Items.COCOA_BEANS, Items.DEAD_BUSH, Items.WITHER_ROSE, Items.GLOWSTONE_DUST);
	private static final Ingredient UMAMI_FLAVOR = Ingredient.ofItems(Items.CARROT, Items.CRIMSON_FUNGUS, Items.CRIMSON_ROOTS);
	private static final Ingredient SALTY_FLAVOR = Ingredient.ofItems(Items.NETHER_SPROUTS, Items.WARPED_FUNGUS, Items.WARPED_ROOTS, Items.SEAGRASS, Items.KELP, Items.PUMPKIN_SEEDS);
	private static final Ingredient SOUR_FLAVOR = Ingredient.ofItems(Items.BROWN_MUSHROOM, Items.RED_MUSHROOM, Items.FERN, Items.LARGE_FERN, Items.CHORUS_FRUIT);
	private static final Ingredient WEIRD_FLAVOR = Ingredient.ofItems(Items.CHORUS_FRUIT, Items.GLOWSTONE_DUST);
	//each item should be in 1 potency class max or else it would immediately be 3 or above so it wouldn't work
	//Potency 3 can only have 1 flavor
	private static final Ingredient POTENCY1 = Ingredient.fromTag(CEFTags.Items.TEA_POTENCY_1);
	private static final Ingredient POTENCY2 = Ingredient.fromTag(CEFTags.Items.TEA_POTENCY_2);
	private static final Ingredient POTENCY3 = Ingredient.fromTag(CEFTags.Items.TEA_POTENCY_3);
	private static final Ingredient POTENCY1AND1 = Ingredient.fromTag(CEFTags.Items.TEA_POTENCY_1_AND_1);

	public TeabagMakingRecipe(Identifier id) {
		super(id);
	}

	@Override
	public boolean matches(CraftingInventory inv, World world) {
		boolean hasPaper = false;
		boolean hasString = false;
		boolean hasValidIngredients = true;
		int stringCount = 0;
		int stringLocation = 0;
		int paperCount = 0;
		int occupiedSlots = 0;
		int totalPotency = 0;
		for(int i = 0; i < inv.size(); ++i) {
			ItemStack itemStack = inv.getStack(i);
			if(!itemStack.isEmpty()) {
				occupiedSlots++;
			}
		}
		if(occupiedSlots > 4 || occupiedSlots < 2) {
			return false;
		}
		for(int i = 0; i < inv.size(); ++i) {
			ItemStack itemStack = inv.getStack(i);
			if(!itemStack.isEmpty()) {
				if(STRING.test(itemStack)) {
					hasString = true;
					stringCount++;
					stringLocation = i;
					occupiedSlots--;
				}
			}
		}
		if(stringCount > 1 || !hasString) {
			return false;
		}
		for(int i = 0; i < inv.size(); ++i) {
			ItemStack itemStack = inv.getStack(i);
			if(!itemStack.isEmpty()) {
				if(PAPER.test(itemStack)) {
					//inv.getHeight()*(inv.getHeight()-1) always produces the number of the lower left corner
					if(i == stringLocation + inv.getWidth() - 1) {
						hasPaper = true;
						occupiedSlots--;
					}
					paperCount++;
				}
			}
		}
		//left of String
		ItemStack itemStack = inv.getStack(stringLocation - 1);
		if(!itemStack.isEmpty()) {
			if(POTENCY1.test(itemStack)) {
				totalPotency++;
			}
			else if(POTENCY2.test(itemStack)) {
				totalPotency = totalPotency + 2;
			}
			else if(POTENCY3.test(itemStack)) {
				totalPotency = totalPotency + 3;
			}
			else if(POTENCY1AND1.test(itemStack)) {
				totalPotency = totalPotency + 3;
			}
			else {
				return false;
			}
			occupiedSlots--;
		}
		//below String
		itemStack = inv.getStack(stringLocation + inv.getHeight());
		if(!itemStack.isEmpty()) {
			if(POTENCY1.test(itemStack)) {
				totalPotency++;
			}
			else if(POTENCY2.test(itemStack)) {
				totalPotency = totalPotency + 2;
			}
			else if(POTENCY3.test(itemStack)) {
				totalPotency = totalPotency + 3;
			}
			else if(POTENCY1AND1.test(itemStack)) {
				totalPotency = totalPotency + 3;
			}
			else {
				return false;
			}
			occupiedSlots--;
		}
		//occupied slots gets +1 for every occupiedSpot in the crafting bench if one spot is left outside of the tea area you cant craft it lol
		if(occupiedSlots > 0) {
			return false;
		}
		if(totalPotency > 3 || totalPotency < 1) {
			return false;
		}
		return hasPaper && hasString;
	}

	@Override
	public ItemStack craft(CraftingInventory inv) {
		ItemStack teabag = new ItemStack(CEFItems.TEA_BAG);
		int totalPotency = 0;
		int itemCounter = 0;
		for(int i = 0; i < inv.size(); ++i) {
			ItemStack itemStack = inv.getStack(i);
			if(!itemStack.isEmpty()) {
				itemCounter++;
			}
		}
		if(itemCounter == 3) {
		}
		else if(itemCounter == 4) {
		}
		else if(itemCounter == 2) {
		}
		return teabag;
	}

	@Override
	public boolean fits(int width, int height) {
		return width * height >= 2;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return CEFRecipeSerializers.TEABAG_MAKING;
	}
}
