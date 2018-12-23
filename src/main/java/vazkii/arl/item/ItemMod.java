/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [08/01/2016, 21:47:43 (GMT)]
 */
package vazkii.arl.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.interf.IVariantHolder;
import vazkii.arl.util.ProxyRegistry;
import vazkii.arl.util.TooltipHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public abstract class ItemMod extends Item implements IVariantHolder {

	public static final List<IVariantHolder> variantHolders = new ArrayList<>();

	private final String[] variants;
	private final String bareName;

	public ItemMod(String name, String... variants) {
		setUnlocalizedName(name);
		if(variants.length > 1)
			setHasSubtypes(true);

		if(variants.length == 0)
			variants = new String[] { name };

		bareName = name;
		this.variants = variants;
	}

	@Nonnull
	@Override
	public Item setUnlocalizedName(@Nonnull String name) {
		super.setUnlocalizedName(name);
		setRegistryName(new ResourceLocation(getPrefix() + name));
		ProxyRegistry.register(this);

		return this;
	}

	@Nonnull
	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		int dmg = par1ItemStack.getItemDamage();
		String[] variants = getVariants();

		String name;
		if(dmg >= variants.length)
			name = bareName;
		else name = variants[dmg];

		return "item." + getPrefix() + name;
	}

	@Override
	public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> subItems) {
		if(isInCreativeTab(tab))
			for(int i = 0; i < getVariants().length; i++)
				subItems.add(new ItemStack(this, 1, i));
	}

	@Override
	public String[] getVariants() {
		return variants;
	}

	@SideOnly(Side.CLIENT)
	public static void tooltipIfShift(List<String> tooltip, Runnable r) {
		TooltipHandler.tooltipIfShift(tooltip, r);
	}

	@SideOnly(Side.CLIENT)
	public static void addToTooltip(List<String> tooltip, String s, Object... format) {
		TooltipHandler.addToTooltip(tooltip, s, format);
	}

	@SideOnly(Side.CLIENT)
	public static String local(String s) {
		return TooltipHandler.local(s);
	}

}
