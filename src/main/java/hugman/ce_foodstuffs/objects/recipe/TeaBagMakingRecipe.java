package hugman.ce_foodstuffs.objects.recipe;

import hugman.ce_foodstuffs.init.CEFItems;
import hugman.ce_foodstuffs.init.data.CEFRecipeSerializers;
import hugman.ce_foodstuffs.objects.item.tea.TeaType;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.List;

public class TeaBagMakingRecipe extends SpecialCraftingRecipe {
	private static final Ingredient PAPER = Ingredient.ofItems(Items.PAPER);
	private static final Ingredient STRING = Ingredient.ofItems(Items.STRING);

	public TeaBagMakingRecipe(Identifier id) {
		super(id);
	}

	@Override
	public boolean matches(CraftingInventory inv, World world) {
		boolean hasPaper = false;
		boolean hasString = false;
		int totalPotency = 0;
		for(int j = 0; j < inv.size(); ++j) {
			ItemStack stack = inv.getStack(j);
			if(!stack.isEmpty()) {
				if(PAPER.test(stack)) {
					if(hasPaper) {
						return false;
					}
					hasPaper = true;
				}
				else if(STRING.test(stack)) {
					if(hasString) {
						return false;
					}
					hasString = true;
				}
				else {
					List<TeaType> teaTypes = TeaType.getTypes(stack);
					if(teaTypes.isEmpty()) {
						return false;
					}
					else {
						for(TeaType teaType : teaTypes) {
							totalPotency = totalPotency + teaType.getStrength().getPotency();
						}
					}
				}
			}
		}
		return hasPaper && hasString && totalPotency >= 1 && totalPotency <= 3;
	}

	@Override
	public ItemStack craft(CraftingInventory inv) {
		ItemStack givenStack = new ItemStack(CEFItems.TEA_BAG);
		CompoundTag compoundTag = givenStack.getOrCreateTag();
		ListTag listTag = new ListTag();
		for(int j = 0; j < inv.size(); ++j) {
			ItemStack stack = inv.getStack(j);
			if(!stack.isEmpty()) {
				List<TeaType> teaTypes = TeaType.getTypes(stack);
				if(!teaTypes.isEmpty()) {
					for(TeaType teaType : teaTypes) {
						CompoundTag typeTag = new CompoundTag();
						typeTag.putString("Flavor", teaType.getFlavor().getName());
						typeTag.putString("Strength", teaType.getStrength().getName());
						listTag.add(typeTag);
					}
				}
			}
		}
		compoundTag.put("TeaTypes", listTag);
		return givenStack;
	}

	@Override
	public boolean fits(int width, int height) {
		return width * height >= 2;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return CEFRecipeSerializers.TEA_BAG_MAKING;
	}
}
