package vazkii.arl.util;

import java.lang.reflect.Method;
import java.util.Map;

import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class ReflectiveMethods {

	private static final String[] RECIPE_ADD_SIG = { "func_192402_a" }; 
	
	public static final Method recipeAdd;
	
	static {
		Method m = ReflectionHelper.findMethod(ShapedRecipes.class, null, RECIPE_ADD_SIG, String[].class, Map.class, int.class, int.class);
		m.setAccessible(true);
		recipeAdd = m;
	}
	
	private ReflectiveMethods() { }
	
}
