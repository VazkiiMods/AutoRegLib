package vazkii.arl.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;

public class MultiRecipe extends ModRecipe {

	private List<IRecipe> subRecipes = new LinkedList<>();
	IRecipe matched;
	
	public MultiRecipe(ResourceLocation res) {
		super(res);
	}
	
	public void addRecipe(IRecipe recipe) {
		subRecipes.add(recipe);
	}

	@Override
	public boolean matches(@Nonnull InventoryCrafting inv, @Nonnull World worldIn) {
		matched = null;
		for(IRecipe recipe : subRecipes)
			if(recipe.matches(inv, worldIn)) {
				matched = recipe;
				return true;
			}
		
		return false;
	}

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull InventoryCrafting inv) {
		if(matched == null)
			return ItemStack.EMPTY;
		
		return matched.getCraftingResult(inv);
	}

	@Override
	public boolean canFit(int width, int height) {
		return false;
	}

	@Nonnull
	@Override
	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}

}
