package vazkii.arl.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vazkii.arl.interf.IVariantHolder;
import vazkii.arl.util.ProxyRegistry;

import javax.annotation.Nonnull;

public abstract class ItemModPickaxe extends ItemPickaxe implements IVariantHolder {

	private final String[] variants;
	private final String bareName;

	public ItemModPickaxe(String name, ToolMaterial material, String... variants) {
		super(material);
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
	public String[] getVariants() {
		return variants;
	}
}
