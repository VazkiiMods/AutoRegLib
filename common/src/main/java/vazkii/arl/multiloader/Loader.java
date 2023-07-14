package vazkii.arl.multiloader;

import dev.architectury.injectables.annotations.ExpectPlatform;
import org.jetbrains.annotations.ApiStatus.Internal;

import java.util.function.Supplier;

public enum Loader {
    FORGE, FABRIC, QUILT;

    public static final Loader CURRENT = getCurrent();

    public boolean isFabricLike() {
        return getCurrent() == FABRIC || getCurrent() == QUILT;
    }

    public boolean isCurrent() {
        return this == CURRENT;
    }

    public void runIfCurrent(Supplier<Runnable> run) {
        if (isCurrent())
            run.get().run();
    }

    @Internal
    @ExpectPlatform
    public static Loader getCurrent() {
        throw new AssertionError();
    }
}
