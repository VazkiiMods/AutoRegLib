/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [09/01/2016, 22:43:48 (GMT)]
 */
package vazkii.arl.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.interf.IModBlock;
import vazkii.arl.item.ItemModBlock;

public abstract class BlockMod extends Block implements IModBlock {

	private final String[] variants;
	private final String bareName;

	public BlockMod(String name, Material materialIn, String... variants) {
		super(materialIn);

		if(variants.length == 0)
			variants = new String[] { name };

		bareName = name;
		this.variants = variants;

		if(registerInConstruction())
			setUnlocalizedName(name);
	}

	@Override
	public Block setUnlocalizedName(String name) {
		super.setUnlocalizedName(name);
		setRegistryName(getPrefix() + name);
		GameRegistry.register(this);
		GameRegistry.register(new ItemModBlock(this), new ResourceLocation(getPrefix() + name));
		return this;
	}
	
	public boolean registerInConstruction() {
		return true;
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

}
