package vazkii.arl.util;

import java.util.Collection;
import java.util.HashMap;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class ProxyRegistry {
	
	private static Multimap<Class<?>, IForgeRegistryEntry<?>> entries = MultimapBuilder.hashKeys().arrayListValues().build();

	private static HashMap<Block, Item> temporaryItemBlockMap = new HashMap();
	
	public static <T extends IForgeRegistryEntry<T>> void register(IForgeRegistryEntry<T> obj) {
		entries.put(obj.getRegistryType(), obj);
		
		if(obj instanceof ItemBlock) {
			ItemBlock iblock = (ItemBlock) obj;
			Block block = iblock.getBlock();
			temporaryItemBlockMap.put(block, iblock);
		}
	}
	
	private static Item getItemMapping(Block block) {
		Item i = Item.getItemFromBlock(block);
		if((i == null || i == Item.getItemFromBlock(Blocks.AIR)) && temporaryItemBlockMap.containsKey(block))
			return temporaryItemBlockMap.get(block);
		
		return i;
	}

	public static ItemStack newStack(Block block) {
		return newStack(block, 1);
	}
	
	public static ItemStack newStack(Block block, int size) {
		return newStack(block, size, 0);
	}
	
	public static ItemStack newStack(Block block, int size, int meta) {
		return newStack(getItemMapping(block), size, meta);
	}
	
	public static ItemStack newStack(Item item) {
		return newStack(item, 1);
	}
	
	public static ItemStack newStack(Item item, int size) {
		return newStack(item, size, 0);
	}
	
	public static ItemStack newStack(Item item, int size, int meta) {
		return new ItemStack(item, size, meta);
	}
	
	@SubscribeEvent
	public static void onRegistryEvent(RegistryEvent.Register event) {
		Class<?> type = event.getRegistry().getRegistrySuperType();
		System.out.println("REGISTRY FOR " + type);
		
		if(entries.containsKey(type)) {
			Collection<IForgeRegistryEntry<?>> ourEntries = entries.get(type);
			for(IForgeRegistryEntry<?> entry : ourEntries) {
				event.getRegistry().register(entry);
				System.out.println("Add: " + entry.getRegistryName());
			}
		}
	}
	
}
