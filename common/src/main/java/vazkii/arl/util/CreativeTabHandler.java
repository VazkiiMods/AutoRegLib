package vazkii.arl.util;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import vazkii.arl.AutoRegLib;
import vazkii.arl.interf.ICreativeExtras;

@EventBusSubscriber(modid = AutoRegLib.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CreativeTabHandler {

	protected static Multimap<CreativeModeTab, ItemLike> itemsPerCreativeTab = HashMultimap.create();

	@SubscribeEvent
	public static void buildContents(CreativeModeTabEvent.BuildContents event) {
		CreativeModeTab tab = event.getTab();
		if(itemsPerCreativeTab.containsKey(tab))
			for(ItemLike il : itemsPerCreativeTab.get(tab)) {
				Item item = il.asItem();
				
				if(item instanceof ICreativeExtras extras && !extras.canAddToCreativeTab(tab))
					continue;
				
				event.accept(item);
				
				if(item instanceof ICreativeExtras extras)
					extras.addCreativeModeExtras(tab, event);
			}
	}

}
