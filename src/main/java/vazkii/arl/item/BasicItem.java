package vazkii.arl.item;

import net.minecraft.item.Item;
import vazkii.arl.util.RegistryHelper;

public class BasicItem extends Item {

	public BasicItem(String regname, Properties properties) {
		super(properties);
		
		RegistryHelper.registerItem(this, regname);
	}

}
