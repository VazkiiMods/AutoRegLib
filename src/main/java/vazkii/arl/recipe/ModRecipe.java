package vazkii.arl.recipe;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

public abstract class ModRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	public ModRecipe(ResourceLocation res) {
		RecipeHandler.addRecipe(res, this);
	}
	
}
