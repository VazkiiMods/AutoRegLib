/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 *
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 *
 * File Created @ [20/03/2016, 16:23:27 (GMT)]
 */
package vazkii.arl.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.arl.block.BlockMetaVariants.EnumBase;
import vazkii.arl.interf.IModBlock;
import vazkii.arl.item.ItemModBlockSlab;
import vazkii.arl.recipe.RecipeHandler;
import vazkii.arl.util.ProxyRegistry;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Random;

public abstract class BlockModSlab extends BlockSlab implements IModBlock {

	static boolean tempDoubleSlab;
	protected boolean doubleSlab;
	private final String[] variants;
	private final String bareName;

	public static final PropertyEnum prop = PropertyEnum.create("prop", DummyEnum.class);

	public static HashMap<BlockModSlab, BlockModSlab> halfSlabs = new HashMap<>();
	public static HashMap<BlockModSlab, BlockModSlab> fullSlabs = new HashMap<>();

	@SuppressWarnings("unchecked")
	public BlockModSlab(String name, Material materialIn, boolean doubleSlab) {
		super(hacky(materialIn, doubleSlab));

		this.doubleSlab = doubleSlab;
		if(doubleSlab)
			name += "_double";

		variants = new String[] { name };
		bareName = name;

		setUnlocalizedName(name);
		if(!doubleSlab) {
			useNeighborBrightness = true;
			setDefaultState(blockState.getBaseState().withProperty(HALF, EnumBlockHalf.BOTTOM).withProperty(prop, DummyEnum.BLARG));
		}

		if (doubleSlab)
			setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
	}

	public static Material hacky(Material m, boolean doubleSlab) {
		tempDoubleSlab = doubleSlab;
		return m;
	}

	public ItemBlock createItemBlock(ResourceLocation res) {
		if (!isDouble())
			return new ItemModBlockSlab(this, res);
		return null;
	}

	@Nonnull
	@Override
	public BlockStateContainer createBlockState() {
		return tempDoubleSlab ? new BlockStateContainer(this, getVariantProp()) : new BlockStateContainer(this, HALF, getVariantProp());
	}

	@Nonnull
	@Override
	public IBlockState getStateFromMeta(int meta) {
		if(doubleSlab)
			return getDefaultState();
		else return getDefaultState().withProperty(HALF, meta == 8 ? EnumBlockHalf.TOP : EnumBlockHalf.BOTTOM);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		if(doubleSlab)
			return 0;
		else return state.getValue(HALF) == EnumBlockHalf.TOP ? 8 : 0;
	}

	public BlockSlab getFullBlock() {
		return fullSlabs.get(this);
	}

	public BlockSlab getSingleBlock() {
		return halfSlabs.get(this);
	}

	@Nonnull
	@Override
	public ItemStack getPickBlock(@Nonnull IBlockState state, RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, EntityPlayer player) {
		return new ItemStack(getSingleBlock());
	}

	@Nonnull
	@Override
	public Item getItemDropped(IBlockState p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		return Item.getItemFromBlock(getSingleBlock());
	}

	@Override
	public int quantityDropped(IBlockState state, int fortune, @Nonnull Random random) {
		return super.quantityDropped(state, fortune, random);
	}

	public void register() {
		setRegistryName(getPrefix() + bareName);
		ProxyRegistry.register(this);
		ProxyRegistry.register(createItemBlock(getRegistryName()));
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
	public EnumRarity getBlockRarity(ItemStack stack) {
		return EnumRarity.COMMON;
	}

	@Override
	public IProperty[] getIgnoredProperties() {
		return doubleSlab ? new IProperty[] { prop, HALF } : new IProperty[] { prop };
	}

	@Nonnull
	@Override
	public String getUnlocalizedName(int meta) {
		return getUnlocalizedName();
	}

	@Override
	public boolean isDouble() {
		return doubleSlab;
	}
	
	@Override
	public boolean isFullBlock(IBlockState state) {
		return isDouble();
	}

	@Override
	public boolean isSideSolid(IBlockState base_state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, EnumFacing side) {
		IBlockState state = getActualState(base_state, world, pos);
		return isDouble()
				|| (state.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.TOP && side == EnumFacing.UP)
				|| (state.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.BOTTOM && side == EnumFacing.DOWN);
	}

	@Override
	public IProperty<?> getVariantProp() {
		return prop;
	}

	@Nonnull
	@Override
	public IProperty<?> getVariantProperty() {
		return prop;
	}

	@Override
	public Class getVariantEnum() {
		return DummyEnum.class;
	}

	@Nonnull
	@Override
	public Comparable<?> getTypeForItem(@Nonnull ItemStack stack) {
		return DummyEnum.BLARG;
	}

	public static void initSlab(Block base, int meta, BlockModSlab half, BlockModSlab full) {
		fullSlabs.put(half, full);
		fullSlabs.put(full, full);
		halfSlabs.put(half, half);
		halfSlabs.put(full, half);

		half.register();
		full.register();

		RecipeHandler.addOreDictRecipe(ProxyRegistry.newStack(half, 6),
				"BBB",
				'B', ProxyRegistry.newStack(base, 1, meta));
	}

	public enum DummyEnum implements EnumBase {
		BLARG
	}

}
