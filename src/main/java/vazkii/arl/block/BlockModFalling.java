package vazkii.arl.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.properties.IProperty;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vazkii.arl.interf.IModBlock;
import vazkii.arl.item.ItemModBlock;
import vazkii.arl.util.ProxyRegistry;

import javax.annotation.Nonnull;

public abstract class BlockModFalling extends BlockFalling implements IModBlock {

	private final String[] variants;
	private final String bareName;

	public BlockModFalling(String name, String... variants) {
		if(variants.length == 0)
			variants = new String[] { name };

		bareName = name;
		this.variants = variants;

		if(registerInConstruction())
			setUnlocalizedName(name);
	}

	@Nonnull
	@Override
	public Block setUnlocalizedName(@Nonnull String name) {
		super.setUnlocalizedName(name);
		setRegistryName(getPrefix() + name);
		ProxyRegistry.register(this);
		ProxyRegistry.register(createItemBlock(new ResourceLocation(getPrefix() + name)));
		return this;
	}
	
	public ItemBlock createItemBlock(ResourceLocation res) {
		return new ItemModBlock(this, res);
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
