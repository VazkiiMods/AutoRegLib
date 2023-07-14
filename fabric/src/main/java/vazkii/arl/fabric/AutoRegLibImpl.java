package vazkii.arl.fabric;

import vazkii.arl.AutoRegLib;
import net.fabricmc.api.ModInitializer;

public class AutoRegLibImpl implements ModInitializer {
    @Override
    public void onInitialize() {
        AutoRegLib.init();
    }
}