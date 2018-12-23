package vazkii.arl.recipe;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparators;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Predicate;

// Basically a copy of OreIngredient with a blacklist
public class BlacklistOreIngredient extends Ingredient {
	
	private NonNullList<ItemStack> ores;
	private IntList itemIds = null;
	private ItemStack[] array = null;
	private Predicate<ItemStack> predicate;
	private int lastSizeA = -1, lastSizeL = -1;

	public BlacklistOreIngredient(String ore, Predicate<ItemStack> predicate) {
		super(0);
		ores = OreDictionary.getOres(ore);
		this.predicate = predicate.negate();
	}

	@Override
	@Nonnull
	public ItemStack[] getMatchingStacks() {
		if(array == null || this.lastSizeA != ores.size()) {
			NonNullList<ItemStack> lst = NonNullList.create();
			for (ItemStack itemstack : this.ores) {
				if (itemstack.getMetadata() == OreDictionary.WILDCARD_VALUE) {
					NonNullList<ItemStack> newList = NonNullList.create();
					itemstack.getItem().getSubItems(CreativeTabs.SEARCH, newList);
					for(ItemStack stack : newList)
						if(predicate.test(stack))
							lst.add(stack);
				} else if(predicate.test(itemstack))
					lst.add(itemstack);
			}
			
			this.array = lst.toArray(new ItemStack[0]);
			this.lastSizeA = ores.size();
		}
		return this.array;
	}


	@Override
	@Nonnull
	public IntList getValidItemStacksPacked()
	{
		if (this.itemIds == null || this.lastSizeL != ores.size())
		{
			this.itemIds = new IntArrayList(this.ores.size());

			for (ItemStack itemstack : this.ores)
			{
				if (itemstack.getMetadata() == OreDictionary.WILDCARD_VALUE)
				{
					NonNullList<ItemStack> lst = NonNullList.create();
					itemstack.getItem().getSubItems(CreativeTabs.SEARCH, lst);
					for (ItemStack item : lst)
						if(predicate.test(item))
							this.itemIds.add(RecipeItemHelper.pack(item));
				}
				else if(predicate.test(itemstack))
				{
					this.itemIds.add(RecipeItemHelper.pack(itemstack));
				}
			}

			this.itemIds.sort(IntComparators.NATURAL_COMPARATOR);
			this.lastSizeL = ores.size();
		}

		return this.itemIds;
	}


	@Override
	public boolean apply(@Nullable ItemStack input)
	{
		if (input == null || !predicate.test(input))
			return false;

		for (ItemStack target : this.ores)
			if (OreDictionary.itemMatches(target, input, false))
				return true;

		return false;
	}

	@Override
	protected void invalidate()
	{
		this.itemIds = null;
		this.array = null;
	}

	@Override
	public boolean isSimple()
	{
		return true;
	}

}
