/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [08/01/2016, 23:02:00 (GMT)]
 */
package vazkii.arl.util;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.AutoRegLib;
import vazkii.arl.interf.*;
import vazkii.arl.item.ItemMod;

import java.util.HashMap;
import java.util.Objects;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = AutoRegLib.MOD_ID)
public final class ModelHandler {

	public static final HashMap<String, ModelResourceLocation> resourceLocations = new HashMap<>();

	@SubscribeEvent
	public static void onRegister(ModelRegistryEvent event) {
		for(IVariantHolder holder : ItemMod.variantHolders)
			registerModels(holder);
	}

	@SubscribeEvent
	public static void onItemColorRegister(ColorHandlerEvent.Item event) {
		for(IVariantHolder holder : ItemMod.variantHolders)
			if (holder instanceof IItemColorProvider) {

				Item item;
				if (holder instanceof Block)
					item = Item.getItemFromBlock((Block) holder);
				else if (holder instanceof Item)
					item = (Item) holder;
				else
					continue;

				if (item == Items.AIR)
					continue;

				event.getItemColors().registerItemColorHandler(((IItemColorProvider) holder).getItemColor(), item);
			}
	}

	@SubscribeEvent
	public static void onBlockColorRegister(ColorHandlerEvent.Block event) {
		for(IVariantHolder holder : ItemMod.variantHolders)
			if (holder instanceof IBlockColorProvider) {
				Block block;
				if (holder instanceof ItemBlock)
					block = ((ItemBlock) holder).getBlock();
				else if (holder instanceof Block)
					block = (Block) holder;
				else
					continue;

				event.getBlockColors().registerBlockColorHandler(((IBlockColorProvider) block).getBlockColor(), block);
			}
	}

	public static void registerModels(IVariantHolder holder) {
		if (holder instanceof Item) {
			String unique = holder.getUniqueModel();
			String prefix = holder.getPrefix();
			Item i = (Item) holder;

			ItemMeshDefinition def = holder.getCustomMeshDefinition();
			if (def != null)
				ModelLoader.setCustomMeshDefinition((Item) holder, def);
			else registerModels(i, prefix, holder.getVariants(), unique, false);


			if (holder instanceof IExtraVariantHolder) {
				IExtraVariantHolder extra = (IExtraVariantHolder) holder;
				registerModels(i, prefix, extra.getExtraVariants(), unique, true);
			}
		} else if (holder instanceof Block) {
			// Set IStateMapper for blocks without items.
			registerBlock((Block) holder);
		}
	}

	public static void registerBlock(Block block) {
		IModBlock quarkBlock = (IModBlock) block;
		IProperty variantProp = quarkBlock.getVariantProp();

		IStateMapper mapper = quarkBlock.getStateMapper();
		IProperty[] ignored = quarkBlock.getIgnoredProperties();
		if(mapper != null || ignored != null && ignored.length > 0) {
			if(mapper != null)
				ModelLoader.setCustomStateMapper(block, mapper);
			else
				ModelLoader.setCustomStateMapper(block, new StateMap.Builder().ignore(ignored).build());
		}
	}

	@SuppressWarnings("unchecked")
	public static void registerModels(Item item, String prefix, String[] variants, String uniqueVariant, boolean extra) {
		if(item instanceof ItemBlock && ((ItemBlock) item).getBlock() instanceof IModBlock) {
			IModBlock quarkBlock = (IModBlock) ((ItemBlock) item).getBlock();
			Class clazz = quarkBlock.getVariantEnum();

			if (clazz != null) {
				IProperty variantProp = quarkBlock.getVariantProp();

				IStateMapper mapper = quarkBlock.getStateMapper();
				IProperty[] ignored = quarkBlock.getIgnoredProperties();
				if (mapper != null || ignored != null && ignored.length > 0) {
					if (mapper == null) {
						for (IProperty p : ignored) {
							if (p == variantProp) {
								registerVariantsDefaulted(item, (Block) quarkBlock, clazz, variantProp.getName());
								return;
							}
						}
					}
				}
			}
		}

		for(int i = 0; i < variants.length; i++) {
			String var = variants[i];
			if(!extra && uniqueVariant != null)
				var = uniqueVariant;

			String name = prefix + var;
			ModelResourceLocation loc = new ModelResourceLocation(name, "inventory");
			if(!extra) {
				ModelLoader.setCustomModelResourceLocation(item, i, loc);
				resourceLocations.put(getKey(item, i), loc);
			} else {
				ModelBakery.registerItemVariants(item, loc);
				resourceLocations.put(variants[i], loc);
			}
		}
	}

	private static <T extends Enum<T> & IStringSerializable> void registerVariantsDefaulted(Item item, Block b, Class<T> enumclazz, String variantHeader) {
		String baseName = Objects.requireNonNull(b.getRegistryName()).toString();
		for(T e : enumclazz.getEnumConstants()) {
			String variantName = variantHeader + "=" + e.getName();
			ModelResourceLocation loc = new ModelResourceLocation(baseName, variantName);
			int i = e.ordinal();
			ModelLoader.setCustomModelResourceLocation(item, i, loc);
			resourceLocations.put(getKey(item, i), loc);
		}
	}

	public static ModelResourceLocation getModelLocation(ItemStack stack) {
		if(!stack.isEmpty())
			return null;

		return getModelLocation(stack.getItem(), stack.getItemDamage());
	}

	public static ModelResourceLocation getModelLocation(Item item, int meta) {
		String key = getKey(item, meta);
		if(resourceLocations.containsKey(key))
			return resourceLocations.get(key);

		return null;
	}

	private static String getKey(Item item, int meta) {
		return "i_" + item.getRegistryName() + "@" + meta;
	}

}
