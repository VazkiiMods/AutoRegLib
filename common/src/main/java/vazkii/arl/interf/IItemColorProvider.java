package vazkii.arl.interf;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.item.ItemColor;

public interface IItemColorProvider {
	@Environment(EnvType.CLIENT)
	ItemColor getItemColor();

}