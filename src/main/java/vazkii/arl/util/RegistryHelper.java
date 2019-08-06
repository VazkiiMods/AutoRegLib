package vazkii.arl.util;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import com.mojang.datafixers.util.Pair;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import vazkii.arl.AutoRegLib;
import vazkii.arl.interf.IBlockColorProvider;
import vazkii.arl.interf.IBlockItemProvider;
import vazkii.arl.interf.IItemColorProvider;
import vazkii.arl.interf.IItemPropertiesFiller;

@EventBusSubscriber(modid = AutoRegLib.MOD_ID)
public final class RegistryHelper {
	
	private static Map<ResourceLocation, ItemGroup> groups = new HashMap<>();
	private static Queue<Block> blocksNeedingItemBlock = new ArrayDeque<>();
	
	private static Queue<Pair<Item, IItemColorProvider>> itemColors = new ArrayDeque<>();
	private static Queue<Pair<Block, IBlockColorProvider>> blockColors = new ArrayDeque<>();

	public static void registerBlock(RegistryEvent.Register<Block> event, Block block) {
		registerBlock(event, block, true);
	}
	
	public static void registerBlock(RegistryEvent.Register<Block> event, Block block, boolean hasBlockItem) {
		event.getRegistry().register(block);
		
		if(hasBlockItem)
			blocksNeedingItemBlock.add(block);
		
		if(block instanceof IBlockColorProvider)
			blockColors.add(Pair.of(block, (IBlockColorProvider) block));
	}
	
	public static void registerItem(RegistryEvent.Register<Item> event, Item item) {
		event.getRegistry().register(item);
		
		if(item instanceof IItemColorProvider)
			itemColors.add(Pair.of(item, (IItemColorProvider) item));
	}
	
	public static void setCreativeTab(Block block, ItemGroup group) {
		ResourceLocation res = block.getRegistryName();
		if(res == null)
			throw new IllegalArgumentException("Can't set the creative tab for a block without a registry name yet");
		
		groups.put(block.getRegistryName(), group);
	}
	
	@SubscribeEvent
	public static void register(RegistryEvent.Register<Item> event) {
		while(!blocksNeedingItemBlock.isEmpty()) {
			Block block = blocksNeedingItemBlock.poll();
			
			Item.Properties props = new Item.Properties();
			ResourceLocation registryName = block.getRegistryName();
			
			ItemGroup group = groups.get(registryName);
			if(group != null)
				props = props.group(group);
			
			if(block instanceof IItemPropertiesFiller)
				((IItemPropertiesFiller) block).fillItemProperties(props);
			
			BlockItem blockitem;
			if(block instanceof IBlockItemProvider)
				blockitem = ((IBlockItemProvider) block).provideItemBlock(block, props);
			else blockitem = new BlockItem(block, props);
			
			if(block instanceof IItemColorProvider)
				itemColors.add(Pair.of(blockitem, (IItemColorProvider) block));
			
			event.getRegistry().register(blockitem.setRegistryName(registryName));
		}
	}

	public static void loadComplete(FMLLoadCompleteEvent event) {
		DistExecutor.runForDist(() -> () -> loadCompleteClient(event), 
								() -> () -> true);
		
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
			IBlockColor color = pair.getSecond().getBlockColor();
			
			bcolors.register(color, pair.getFirst());
		}
		
		while(!itemColors.isEmpty()) {
			Pair<Item, IItemColorProvider> pair = itemColors.poll();
			IItemColor color = pair.getSecond().getItemColor();
			
			icolors.register(color, pair.getFirst());
		}
		
		return true;
	}
	
}
