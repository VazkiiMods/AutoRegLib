/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 *
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 *
 * File Created @ [20/03/2016, 17:58:14 (GMT)]
 */
package vazkii.arl.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.interf.IModBlock;
import vazkii.arl.item.ItemModBlock;
import vazkii.arl.recipe.RecipeHandler;
import vazkii.arl.util.ProxyRegistry;

public abstract class BlockModStairs extends BlockStairs implements IModBlock {

	private final String[] variants;
	private final String bareName;

	public BlockModStairs(String name, IBlockState state) {
		super(state);

		variants = new String[] { name };
		bareName = name;

		setUnlocalizedName(name);
		useNeighborBrightness = true;
	}

	@Override
	public Block setUnlocalizedName(String name) {
		super.setUnlocalizedName(name);
		setRegistryName(getPrefix() + name);
		ProxyRegistry.register(this);
		ProxyRegistry.register(new ItemModBlock(this, new ResourceLocation(getPrefix() + name)));
		return this;
	}

	@Override
	public String getBareName() {
		return bareName;
	}

	@Override
	public String[] getVariants() {
		return variants;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ItemMeshDefinition getCustomMeshDefinition() {
		return null;
	}

	@Override
	public EnumRarity getBlockRarity(ItemStack stack) {
		return EnumRarity.COMMON;
	}

	@Override
	public IProperty[] getIgnoredProperties() {
		return new IProperty[0];
	}

	@Override
	public IProperty getVariantProp() {
		return null;
	}

	@Override
	public Class getVariantEnum() {
		return null;
	}

	public static void initStairs(Block base, int meta, BlockStairs block) {
		RecipeHandler.addOreDictRecipe(ProxyRegistry.newStack(block, 4),
				"B  ", "BB ", "BBB",
				'B', ProxyRegistry.newStack(base, 1, meta));
	}


}
