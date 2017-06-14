/*******************************************************************************
 * Copyright 2014-2017, the Biomes O' Plenty Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 * 
 * Original: https://github.com/Glitchfiend/BiomesOPlenty/blob/0f8be0526e01d918cf8f22d4904a3b74981dee6f/src/main/java/biomesoplenty/common/util/inventory/CraftingUtil.java
 * (edited to work with multiple mods) 
 ******************************************************************************/
package vazkii.arl.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreIngredient;
import vazkii.arl.interf.IRecipeGrouped;

public final class RecipeHandler {

	private static final String namespace = "arl";
	
	// Bridges

	public static void addOreDictRecipe(ItemStack output, Object... inputs) {
		addShapedRecipe(output, inputs);
	}
	
	public static void addShapelessOreDictRecipe(ItemStack output, Object... inputs) {
		addShapelessRecipe(output, inputs);
	}
	
	public static void addShapelessRecipe(ItemStack output, Object... inputs) {
		NonNullList<Ingredient> ingredients = NonNullList.create();

		for(Object input : inputs)
			ingredients.add(asIngredient(input));

		if(ingredients.isEmpty())
			throw new IllegalArgumentException("No ingredients for shapeless recipe");
		else if(ingredients.size() > 9)
			throw new IllegalArgumentException("Too many ingredients for shapeless recipe");

		ShapelessRecipes recipe = new ShapelessRecipes(outputGroup(output), output, ingredients);
		addRecipe(unusedLocForOutput(output), recipe);
	}
	
	public static void addShapedRecipe(ItemStack output, Object... inputs) {
		ArrayList<String> pattern = Lists.newArrayList();
		Map<String, Ingredient> key = Maps.newHashMap();
		Iterator itr = Arrays.asList(inputs).iterator();

		while(itr.hasNext()) {
			Object obj = itr.next();

			if (obj instanceof String) {
				String str = (String) obj;

				if(str.length() > 3)
					throw new IllegalArgumentException("Invalid string length for recipe " + str.length());

				if(pattern.size() <= 2)
					pattern.add(str);
				else
					throw new IllegalArgumentException("Recipe has too many crafting rows!");
			}
			else if (obj instanceof Character)
				key.put(((Character)obj).toString(), asIngredient(itr.next()));
			else
				throw new IllegalArgumentException("Unexpected argument of type " + obj.getClass().toString());
		}

		int width = pattern.get(0).length();
		int height = pattern.size();

		try {
			key.put(" ", Ingredient.field_193370_a);
			Object ingredients = ReflectiveMethods.prepareMaterials.invoke(null, pattern.toArray(new String[pattern.size()]), key, width, height);
			ShapedRecipes recipe = new ShapedRecipes(outputGroup(output), width, height, (NonNullList<Ingredient>) ingredients, output);
			addRecipe(unusedLocForOutput(output), recipe);
		} catch(Throwable e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void addRecipe(ResourceLocation res, IRecipe recipe) {
		try {
			ReflectiveMethods.addRecipe.invoke(null, res, recipe);
		} catch(Throwable e) {
			throw new RuntimeException(e);
		}
	}

	public static Ingredient asIngredient(Object object) {
		if(object instanceof Item)
			return Ingredient.func_193367_a((Item)object);

		else if(object instanceof Block)
			return Ingredient.func_193369_a(new ItemStack((Block)object));

		else if(object instanceof ItemStack)
			return Ingredient.func_193369_a((ItemStack)object);
		
		else if(object instanceof String)
			return new OreIngredient((String) object);
				
		throw new IllegalArgumentException("Cannot convert object of type " + object.getClass().toString() + " to an Ingredient!");
	}

	private static ResourceLocation unusedLocForOutput(ItemStack output) {
		ResourceLocation baseLoc = new ResourceLocation(namespace, output.getItem().getRegistryName().getResourcePath());
		ResourceLocation recipeLoc = baseLoc;
		int index = 0;

		// find unused recipe name
		while (CraftingManager.field_193380_a.containsKey(recipeLoc)) {
			index++;
			recipeLoc = new ResourceLocation(namespace, baseLoc.getResourcePath() + "_" + index);
		}

		return recipeLoc;
	}
	
	private static String outputGroup(ItemStack output) {
		Item item = output.getItem();
		if(item instanceof IRecipeGrouped)
			return namespace + ":" + ((IRecipeGrouped) item).getRecipeGroup();
		if(item instanceof ItemBlock) {
			Block block = ((ItemBlock) item).block;
			if(block instanceof IRecipeGrouped)
				return namespace + ":" + ((IRecipeGrouped) block).getRecipeGroup();
		}
		
		return output.getItem().getRegistryName().toString();
	}


}
