/*
 *  This class is licensed under the WTFPL
 *  http://www.wtfpl.net/
 */
package vazkii.arl;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import vazkii.arl.util.ModelHandler;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		ModelHandler.preInit();
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		ModelHandler.init();
	}
	
}
