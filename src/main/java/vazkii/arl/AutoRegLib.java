/*
 *  This class is licensed under the WTFPL
 *  http://www.wtfpl.net/
 */
package vazkii.arl;

import java.lang.invoke.MethodHandle;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = AutoRegLib.MOD_ID, name = AutoRegLib.MOD_NAME, version = AutoRegLib.VERSION, dependencies = AutoRegLib.DEPENDENCIES)
public class AutoRegLib {

	public static final String MOD_ID = "autoreglib";
	public static final String MOD_NAME = "AutoRegLib";
	public static final String BUILD = "GRADLE:BUILD";
	public static final String VERSION = "GRADLE:VERSION-" + BUILD;
	public static final String DEPENDENCIES = "required-after:forge@[14.21.0.2331]";
	
	public static final String PROXY_COMMON = "vazkii.arl.CommonProxy";
	public static final String PROXY_CLIENT = "vazkii.arl.ClientProxy";
	
	@Instance(MOD_ID)
	public static AutoRegLib instance;

	@SidedProxy(serverSide = PROXY_COMMON, clientSide = PROXY_CLIENT)
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}
	
}
