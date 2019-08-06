/*
 *  This class is licensed under the WTFPL
 *  http://www.wtfpl.net/
 */
package vazkii.arl;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkDirection;
import vazkii.arl.network.NetworkHandler;
import vazkii.arl.network.message.MessageDropIn;
import vazkii.arl.util.DropInHandler;
import vazkii.arl.util.RegistryHelper;

@Mod(AutoRegLib.MOD_ID)
public class AutoRegLib {

	public static final String MOD_ID = "arl";
	
	public static NetworkHandler network;

	public AutoRegLib() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus(); 
		bus.addListener(this::setup);
		bus.addListener(this::loadComplete);
	}
	
	public void setup(FMLCommonSetupEvent event) {
		DropInHandler.register();
		
		network = new NetworkHandler(MOD_ID, 1);
		network.register(MessageDropIn.class, NetworkDirection.PLAY_TO_SERVER);
	}
	
	public void loadComplete(FMLLoadCompleteEvent event) {
		RegistryHelper.loadComplete(event);
	}
	
}
