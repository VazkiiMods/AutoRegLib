package vazkii.arl.recipe;

import com.google.common.collect.ImmutableList;
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
	private ThreadLocal<IRecipe> matched = new ThreadLocal<>();

	public MultiRecipe(ResourceLocation res) {
		super(res);
	}
	
	public void addRecipe(IRecipe recipe) {
		subRecipes.add(recipe);
	}

	@Override
	public boolean matches(@Nonnull InventoryCrafting inv, @Nonnull World worldIn) {
		matched.set(null);
		for(IRecipe recipe : subRecipes)
			if(recipe.matches(inv, worldIn)) {
				matched.set(recipe);
				return true;
			}
		
		return false;
	}

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull InventoryCrafting inv) {
		IRecipe match = matched.get();

		if(match == null)
			return ItemStack.EMPTY;

		return match.getCraftingResult(inv);
	}

	@Override
	public boolean canFit(int width, int height) {
		for (IRecipe recipe : subRecipes)
			if (recipe.canFit(width, height))
				return true;
		return false;
	}

	@Override
	public boolean isDynamic() {
		return true;
	}

	@Nonnull
	@Override
	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}

	public ImmutableList<IRecipe> getRecipes() {
		return ImmutableList.copyOf(subRecipes);
	}

}
