package vazkii.arl.recipe;

import java.util.function.Predicate;
import java.util.stream.Collectors;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

public class BlacklistOreIngredient extends OreIngredient {

	final Predicate<ItemStack> blacklist;
	
	public BlacklistOreIngredient(String key, Predicate<ItemStack> blacklist) {
		super(key);
		this.blacklist = blacklist;
	}
	
	@Override
	public boolean apply(ItemStack input) {
		return super.apply(input) && !blacklist.test(input);
	}
	
}
