/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 *
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 *
 * File Created @ [20/03/2016, 16:33:11 (GMT)]
 */
package vazkii.arl.item;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.block.BlockModSlab;
import vazkii.arl.interf.IModBlock;
import vazkii.arl.interf.IVariantHolder;

import javax.annotation.Nonnull;

public class ItemModBlockSlab extends ItemSlab implements IVariantHolder {

	private IModBlock modBlock;

	public ItemModBlockSlab(Block block, ResourceLocation res) {
		super(block, ((BlockModSlab) block).getSingleBlock(), ((BlockModSlab) block).getFullBlock());
		modBlock = (IModBlock) block;

		if(getVariants().length > 1)
			setHasSubtypes(true);
		setRegistryName(res);
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}

	@Nonnull
	@Override
	public ItemBlock setTranslationKey(@Nonnull String par1Str) {
		return (ItemBlock) super.setTranslationKey(par1Str);
	}

	@Nonnull
	@Override
	public String getTranslationKey(ItemStack par1ItemStack) {
		int dmg = par1ItemStack.getItemDamage();
		String[] variants = getVariants();

		String name;
		if(dmg >= variants.length)
			name = modBlock.getBareName();
		else name = variants[dmg];

		return "tile." + getPrefix() + name;
	}

	@Override
	public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> subItems) {
		String[] variants = getVariants();
		if(isInCreativeTab(tab))
			for(int i = 0; i < variants.length; i++)
				subItems.add(new ItemStack(this, 1, i));
	}

	@Override
	public String[] getVariants() {
		return modBlock.getVariants();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ItemMeshDefinition getCustomMeshDefinition() {
		return modBlock.getCustomMeshDefinition();
	}

	@Nonnull
	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return modBlock.getBlockRarity(stack);
	}
	
	@Override
	public String getModNamespace() {
		return modBlock.getModNamespace();
	}

}
