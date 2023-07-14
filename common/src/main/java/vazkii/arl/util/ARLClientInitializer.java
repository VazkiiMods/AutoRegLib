package vazkii.arl.util;

import vazkii.arl.AutoRegLib;

// FIXME
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = AutoRegLib.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ARLClientInitializer {
	@SubscribeEvent
	public static void registerBlockColors(RegisterColorHandlersEvent.Block evt) {
		RegistryHelper.submitBlockColors(evt.getBlockColors()::register);
	}

	@SubscribeEvent
	public static void registerItemColors(RegisterColorHandlersEvent.Item evt) {
		RegistryHelper.submitItemColors(evt.getItemColors()::register);
	}
}
