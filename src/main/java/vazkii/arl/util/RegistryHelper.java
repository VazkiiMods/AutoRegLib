package vazkii.arl.util;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Queue;
import java.util.function.Supplier;

import com.google.common.collect.ArrayListMultimap;
import com.mojang.datafixers.util.Pair;

import net.minecraft.world.level.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import vazkii.arl.AutoRegLib;
import vazkii.arl.interf.IBlockColorProvider;
import vazkii.arl.interf.IBlockItemProvider;
import vazkii.arl.interf.IItemColorProvider;
import vazkii.arl.interf.IItemPropertiesFiller;

public final class RegistryHelper {

	private static final Map<String, ModData> modData = new HashMap<>();

	private static Queue<Pair<Item, IItemColorProvider>> itemColors = new ArrayDeque<>();
	private static Queue<Pair<Block, IBlockColorProvider>> blockColors = new ArrayDeque<>();

	private static ModData getCurrentModData() {
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
	
	@SubscribeEvent
	public static void onRegistryEvent(RegistryEvent.Register<?> event) {
		getCurrentModData().register(event.getRegistry());
	}

	public static void registerBlock(Block block, String resloc) {
		registerBlock(block, resloc, true);
	}

	public static void registerBlock(Block block, String resloc, boolean hasBlockItem) {
		register(block, resloc);

		if(hasBlockItem) {
			ModData data = getCurrentModData();
			data.defers.put(Item.class, () -> data.createItemBlock(block));
		}

		if(block instanceof IBlockColorProvider)
			blockColors.add(Pair.of(block, (IBlockColorProvider) block));
	}

	public static void registerItem(Item item, String resloc) {
		register(item, resloc);

		if(item instanceof IItemColorProvider)
			itemColors.add(Pair.of(item, (IItemColorProvider) item));
	}

	public static <T extends IForgeRegistryEntry<T>> void register(IForgeRegistryEntry<T> obj, String resloc) {
		if(obj == null)
			throw new IllegalArgumentException("Can't register null object.");

		obj.setRegistryName(GameData.checkPrefix(resloc, false));
		getCurrentModData().defers.put(obj.getRegistryType(), () -> obj);
	}

	public static <T extends IForgeRegistryEntry<T>> void register(IForgeRegistryEntry<T> obj) {
		if(obj == null)
			throw new IllegalArgumentException("Can't register null object.");
		if(obj.getRegistryName() == null)
			throw new IllegalArgumentException("Can't register object without registry name.");

		getCurrentModData().defers.put(obj.getRegistryType(), () -> obj);
	}

	public static void setCreativeTab(Block block, CreativeModeTab group) {
		ResourceLocation res = block.getRegistryName();
		if(res == null)
			throw new IllegalArgumentException("Can't set the creative tab for a block without a registry name yet");

		getCurrentModData().groups.put(block.getRegistryName(), group);
	}

	public static void loadComplete(FMLLoadCompleteEvent event) {
		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> loadCompleteClient(event));

		itemColors.clear();
		blockColors.clear();
	}

	@OnlyIn(Dist.CLIENT)
	private static boolean loadCompleteClient(FMLLoadCompleteEvent event) {
		Minecraft mc = Minecraft.getInstance();
		BlockColors bcolors = mc.getBlockColors();
		ItemColors icolors = mc.getItemColors();

		while(!blockColors.isEmpty()) {
			Pair<Block, IBlockColorProvider> pair = blockColors.poll();
			BlockColor color = pair.getSecond().getBlockColor();

			bcolors.register(color, pair.getFirst());
		}

		while(!itemColors.isEmpty()) {
			Pair<Item, IItemColorProvider> pair = itemColors.poll();
			ItemColor color = pair.getSecond().getItemColor();

			icolors.register(color, pair.getFirst());
		}

		return true;
	}

	private static class ModData {

		private Map<ResourceLocation, CreativeModeTab> groups = new LinkedHashMap<>();

		private ArrayListMultimap<Class<?>, Supplier<IForgeRegistryEntry<?>>> defers = ArrayListMultimap.create();

		@SuppressWarnings({ "rawtypes", "unchecked" })
		private void register(IForgeRegistry registry) {
			Class<?> type = registry.getRegistrySuperType();

			if(defers.containsKey(type)) {
				Collection<Supplier<IForgeRegistryEntry<?>>> ourEntries = defers.get(type);
				for(Supplier<IForgeRegistryEntry<?>> supplier : ourEntries) {
					IForgeRegistryEntry<?> entry = supplier.get();
					registry.register(entry);
					AutoRegLib.LOGGER.debug("Registering to " + registry.getRegistryName() + " - " + entry.getRegistryName());
				}

				defers.removeAll(type);
			}
		}

		private Item createItemBlock(Block block) {
			Item.Properties props = new Item.Properties();
			ResourceLocation registryName = block.getRegistryName();

			CreativeModeTab group = groups.get(registryName);
			if(group != null)
				props = props.tab(group);

			if(block instanceof IItemPropertiesFiller)
				((IItemPropertiesFiller) block).fillItemProperties(props);

			BlockItem blockitem;
			if(block instanceof IBlockItemProvider)
				blockitem = ((IBlockItemProvider) block).provideItemBlock(block, props);
			else blockitem = new BlockItem(block, props);

			if(block instanceof IItemColorProvider)
				itemColors.add(Pair.of(blockitem, (IItemColorProvider) block));

			return blockitem.setRegistryName(registryName);
		}

	}

}
