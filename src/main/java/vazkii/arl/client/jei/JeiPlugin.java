package vazkii.arl.client.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import vazkii.arl.recipe.MultiRecipe;

@JEIPlugin
public class JeiPlugin implements IModPlugin {
	@Override
	public void register(IModRegistry registry) {
		for (IRecipe recipe : CraftingManager.REGISTRY) {
			if (recipe instanceof MultiRecipe) {
				registry.addRecipes(((MultiRecipe) recipe).getRecipes(), VanillaRecipeCategoryUid.CRAFTING);
			}
		}
	}

}
