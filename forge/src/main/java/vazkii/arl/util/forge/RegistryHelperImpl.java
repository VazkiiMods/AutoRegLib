package vazkii.arl.util.forge;

import com.google.common.collect.ArrayListMultimap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import vazkii.arl.AutoRegLib;
import vazkii.arl.interf.IItemColorProvider;
import vazkii.arl.util.RegistryHelper;

import java.util.*;
import java.util.function.Supplier;

public class RegistryHelperImpl {
    private static final Map<String, ModData> modData = new HashMap<>();
    private static final Queue<Pair<Item, IItemColorProvider>> itemColors = new ArrayDeque<>();

    public static ModData getCurrentModData() {
        return getModData(ModLoadingContext.get().getActiveNamespace());
    }

    private static ModData getModData(String modid) {
        ModData data = modData.get(modid);
        if(data == null) {
            data = new ModData();
            modData.put(modid, data);

            FMLJavaModLoadingContext.get().getModEventBus().register(RegistryHelper.class);
        }

        return data;
    }

    private static class ModData {

        private final ArrayListMultimap<ResourceLocation, Supplier<Object>> defers = ArrayListMultimap.create();

        @SuppressWarnings({ "unchecked" })
        private <T>  void register(IForgeRegistry<T> registry) {
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
