/*
 *  This class is licensed under the WTFPL
 *  http://www.wtfpl.net/
 */
package vazkii.arl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import vazkii.arl.util.RegistryHelper;

@Mod(AutoRegLib.MOD_ID)
public class AutoRegLib {

	public static final String MOD_ID = "autoreglib";

	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public AutoRegLib() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus(); 
		bus.addListener(this::loadComplete);
	}
	
	public void loadComplete(FMLLoadCompleteEvent event) {
		RegistryHelper.loadComplete(event);
	}
	
}
