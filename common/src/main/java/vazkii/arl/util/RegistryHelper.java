package vazkii.arl.util;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import com.google.common.collect.ArrayListMultimap;
import com.mojang.datafixers.util.Pair;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;
import org.jetbrains.annotations.ApiStatus;
import vazkii.arl.AutoRegLib;
import vazkii.arl.interf.IBlockColorProvider;
import vazkii.arl.interf.IBlockItemProvider;
import vazkii.arl.interf.IItemColorProvider;
import vazkii.arl.interf.IItemPropertiesFiller;

public final class RegistryHelper {

	//private static final Map<String, ModData> modData = new HashMap<>();

	private static final Queue<Pair<Item, IItemColorProvider>> itemColors = new ArrayDeque<>();
	private static final Queue<Pair<Block, IBlockColorProvider>> blockColors = new ArrayDeque<>();

	private static final Map<Object, ResourceLocation> internalNames = new HashMap<>();

	@ExpectPlatform
	private static ModData getCurrentModData() {
		throw new AssertionError();
	}
	
	public static <T> ResourceLocation getRegistryName(T obj, IForgeRegistry<T> registry) {
		if(internalNames.containsKey(obj))
			return getInternalName(obj);
		
		return registry.getKey(obj);
	}
	
	public static void setInternalName(Object obj, ResourceLocation name) {
		internalNames.put(obj, name);
	}
	
	public static ResourceLocation getInternalName(Object obj) {
		return internalNames.get(obj);
	}

//	//@SubscribeEvent
//	@ExpectPlatform
//	public static void onRegistryEvent(RegisterEvent event) {
//		getCurrentModData().register(event.getForgeRegistry());
//	}

	public static void registerBlock(Block block, String resloc) {
		registerBlock(block, resloc, true);
	}

	public static void registerBlock(Block block, String resloc, boolean hasBlockItem) {
		register(block, resloc, ForgeRegistries.BLOCKS);

		if(hasBlockItem) {
			ModData data = getCurrentModData();
			data.defers.put(ForgeRegistries.ITEMS.getRegistryName(), () -> data.createItemBlock(block));
		}

		if(block instanceof IBlockColorProvider)
			blockColors.add(Pair.of(block, (IBlockColorProvider) block));
	}

	public static void registerItem(Item item, String resloc) {
		register(item, resloc, ForgeRegistries.ITEMS);

		if(item instanceof IItemColorProvider)
			itemColors.add(Pair.of(item, (IItemColorProvider) item));
	}
	
	public static <T> void register(T obj, String resloc, IForgeRegistry<T> registry) {
		if(obj == null)
			throw new IllegalArgumentException("Can't register null object.");

		setInternalName(obj, GameData.checkPrefix(resloc, false));
		getCurrentModData().defers.put(registry.getRegistryName(), () -> obj);
	}

//	public static <T> void register(T obj, ResourceKey<Registry<T>> registry) {
//		if(obj == null)
//			throw new IllegalArgumentException("Can't register null object.");
//		if(getInternalName(obj) == null)
//			throw new IllegalArgumentException("Can't register object without registry name.");
//
//		getCurrentModData().defers.put(registry.location(), () -> obj);
//	}

	public static void setCreativeTab(ItemLike itemlike, CreativeModeTab group) {
		ResourceLocation res = getInternalName(itemlike);
		if(res == null)
			throw new IllegalArgumentException("Can't set the creative tab for an ItemLike without a registry name yet");

		CreativeTabHandler.itemsPerCreativeTab.put(group, itemlike);
	}

	public static void submitBlockColors(BiConsumer<BlockColor, Block> consumer) {
		blockColors.forEach(p -> consumer.accept(p.getSecond().getBlockColor(), p.getFirst()));
		blockColors.clear();
	}

	public static void submitItemColors(BiConsumer<ItemColor, Item> consumer) {
		itemColors.forEach(p -> consumer.accept(p.getSecond().getItemColor(), p.getFirst()));
		itemColors.clear();
	}

	private static class ModData {

		private ArrayListMultimap<ResourceLocation, Supplier<Object>> defers = ArrayListMultimap.create();

		@ExpectPlatform
		private <T>  void register() {
			throw new AssertionError();
		}

		private Item createItemBlock(Block block) {
			Item.Properties props = new Item.Properties();
			ResourceLocation registryName = getInternalName(block);

			if(block instanceof IItemPropertiesFiller)
				((IItemPropertiesFiller) block).fillItemProperties(props);

			BlockItem blockitem;
			if(block instanceof IBlockItemProvider)
				blockitem = ((IBlockItemProvider) block).provideItemBlock(block, props);
			else blockitem = new BlockItem(block, props);

			if(block instanceof IItemColorProvider)
				itemColors.add(Pair.of(blockitem, (IItemColorProvider) block));

			setInternalName(blockitem, registryName);
			return blockitem;
		}

	}

}
