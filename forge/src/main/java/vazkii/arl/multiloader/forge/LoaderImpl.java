package vazkii.arl.multiloader.forge;

import vazkii.arl.multiloader.Loader;

public class LoaderImpl {
    public static Loader getCurrent() {
        return Loader.FORGE;
    }
}