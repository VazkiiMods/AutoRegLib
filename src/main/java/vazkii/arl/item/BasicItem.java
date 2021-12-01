package vazkii.arl.item;

import net.minecraft.item.Item;
import vazkii.arl.util.RegistryHelper;

import net.minecraft.item.Item.Properties;

public class BasicItem extends Item {

	public BasicItem(String regname, Properties properties) {
		super(properties);
		
		RegistryHelper.registerItem(this, regname);
	}

}
