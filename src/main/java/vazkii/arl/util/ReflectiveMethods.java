package vazkii.arl.util;

import java.lang.reflect.Method;

import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class ReflectiveMethods {

	private static final String[] RECIPE_ADD_SIG = { "func_192402_a", "a" }; 
	
	public static final Method recipeAdd;
	
	static {
		Method m = ReflectionHelper.findMethod(ShapedRecipes.class, null, RECIPE_ADD_SIG);
		m.setAccessible(true);
		recipeAdd = m;
	}
	
	private ReflectiveMethods() { }
	
}
