package vazkii.arl.interf;

import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IBlockColorProvider extends IItemColorProvider {

	@OnlyIn(Dist.CLIENT)
	public IBlockColor getBlockColor();

}