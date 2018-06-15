/*
 *  This class is licensed under the WTFPL
 *  http://www.wtfpl.net/
 */
package vazkii.arl;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import vazkii.arl.util.ItemTickHandler;
import vazkii.arl.util.ProxyRegistry;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) { 
		MinecraftForge.EVENT_BUS.register(ProxyRegistry.class);
		MinecraftForge.EVENT_BUS.register(ItemTickHandler.class);
	}

	public void init(FMLInitializationEvent event) {
		// NO-OP
	}

	public void postInit(FMLPostInitializationEvent event) {
		// NO-OP
	}
	
}
