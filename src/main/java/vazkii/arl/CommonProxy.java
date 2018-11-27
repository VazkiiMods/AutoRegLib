/*
 *  This class is licensed under the WTFPL
 *  http://www.wtfpl.net/
 */
package vazkii.arl;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import vazkii.arl.network.NetworkHandler;
import vazkii.arl.util.DropInHandler;
import vazkii.arl.util.ProxyRegistry;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) { 
		MinecraftForge.EVENT_BUS.register(ProxyRegistry.class);
		
		NetworkHandler.initARLMessages();
	}

	public void init(FMLInitializationEvent event) {
		// NO-OP
	}

	public void postInit(FMLPostInitializationEvent event) {
		// NO-OP
	}
	
}
