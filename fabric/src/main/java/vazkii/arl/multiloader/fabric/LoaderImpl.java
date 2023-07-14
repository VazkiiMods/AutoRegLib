package vazkii.arl.multiloader.fabric;

import net.fabricmc.loader.api.FabricLoader;
import vazkii.arl.multiloader.Loader;

public class LoaderImpl {
    public static Loader getCurrent() {
        return FabricLoader.getInstance().isModLoaded("quilt_loader") ? Loader.QUILT : Loader.FABRIC;
    }
}