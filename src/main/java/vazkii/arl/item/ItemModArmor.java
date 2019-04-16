/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 *
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 *
 * File Created @ [05/06/2016, 20:33:17 (GMT)]
 */
package vazkii.arl.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vazkii.arl.interf.IVariantHolder;
import vazkii.arl.util.ProxyRegistry;

import javax.annotation.Nonnull;

public abstract class ItemModArmor extends ItemArmor implements IVariantHolder {

	private final String bareName;

	public ItemModArmor(String name, ItemArmor.ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
		super(materialIn, renderIndexIn, equipmentSlotIn);

		setTranslationKey(name);
		bareName = name;
		setCreativeTab(CreativeTabs.COMBAT);
	}

	@Nonnull
	@Override
	public Item setTranslationKey(@Nonnull String name) {
		super.setTranslationKey(name);
		setRegistryName(new ResourceLocation(getPrefix() + name));
		ProxyRegistry.register(this);

		return this;
	}

	@Nonnull
	@Override
	public String getTranslationKey(ItemStack par1ItemStack) {
		par1ItemStack.getItemDamage();

		return "item." + getPrefix() + bareName;
	}

	@Override
	public String[] getVariants() {
		return new String[] { bareName };
	}

}
