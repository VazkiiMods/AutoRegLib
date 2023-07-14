package vazkii.arl.util.fabric;

import com.google.common.collect.ArrayListMultimap;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import vazkii.arl.AutoRegLib;
import vazkii.arl.util.RegistryHelper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class RegistryHelperImpl {
    private static final Map<String, ModData> modData = new HashMap<>();

    public static ModData getCurrentModData() {
        //return getModData(ModLoadingContext.get().getActiveNamespace());
        return getModData(AutoRegLib.MOD_ID);
    }

    private static ModData getModData(String modid) {
        ModData data = modData.get(modid);
        if(data == null) {
            data = new ModData();
            modData.put(modid, data);

            // TODO FIX
            //FMLJavaModLoadingContext.get().getModEventBus().register(RegistryHelper.class);
        }

        return data;
    }

    private static class ModData {

        private final ArrayListMultimap<ResourceLocation, Supplier<Object>> defers = ArrayListMultimap.create();

        // TODO FIX
        @SuppressWarnings({ "unchecked" })
        private <T>  void register(Registry<T> registry) {
            ResourceLocation registryRes = registry.getRegistryName();

            if(defers.containsKey(registryRes)) {
                Collection<Supplier<Object>> ourEntries = defers.get(registryRes);
                for(Supplier<Object> supplier : ourEntries) {
                    T entry = (T) supplier.get();
                    ResourceLocation name = RegistryHelper.getInternalName(entry);
                    registry.register(name, entry);
                    AutoRegLib.LOGGER.debug("Registering to " + registryRes + " - " + name);
                }

                defers.removeAll(registryRes);
            }
        }

    }
}
