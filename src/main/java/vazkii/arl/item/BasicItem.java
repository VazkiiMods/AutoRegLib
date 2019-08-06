package vazkii.arl.item;

import net.minecraft.item.Item;

public class BasicItem extends Item {

	public BasicItem(String regname, Properties properties) {
		super(properties);
		setRegistryName(regname);
	}

}
