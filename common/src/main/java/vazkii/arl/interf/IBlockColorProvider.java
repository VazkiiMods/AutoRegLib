package vazkii.arl.interf;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.block.BlockColor;

public interface IBlockColorProvider extends IItemColorProvider {

	@Environment(EnvType.CLIENT)
	public BlockColor getBlockColor();

}