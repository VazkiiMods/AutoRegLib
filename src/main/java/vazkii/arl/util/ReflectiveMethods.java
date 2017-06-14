package vazkii.arl.util;

import java.lang.reflect.Method;
import java.util.Map;

import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class ReflectiveMethods {

	private static final String[] PREPARE_MATERIALS_SIG = { "a", "func_192402_a" }; 
	private static final String[] ADD_RECIPE_SIG = { "a", "func_193372_a" }; 

	public static final Method prepareMaterials;
	public static final Method addRecipe;

	static {
		Method m = ReflectionHelper.findMethod(ShapedRecipes.class, null, PREPARE_MATERIALS_SIG, String[].class, Map.class, int.class, int.class);
		m.setAccessible(true);
		prepareMaterials = m;
		
		m = ReflectionHelper.findMethod(CraftingManager.class, null, ADD_RECIPE_SIG, ResourceLocation.class, IRecipe.class);
		m.setAccessible(true);
		addRecipe = m;
	}
	
	private ReflectiveMethods() { }
	
}
