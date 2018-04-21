package vazkii.arl.recipe;

import java.util.Collection;

import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CompoundIngredient;

// why is this constructor protected again? :thinking:
public class PublicCompoundIngredient extends CompoundIngredient {

	public PublicCompoundIngredient(Collection<Ingredient> children) {
		super(children);
	}

}
