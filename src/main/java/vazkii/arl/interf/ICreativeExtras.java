package vazkii.arl.interf;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.event.CreativeModeTabEvent;

public interface ICreativeExtras {

	public default boolean canAddToCreativeTab(CreativeModeTab tab) {
		return true;
	}
	
	public void addCreativeModeExtras(CreativeModeTab tab, CreativeModeTabEvent.BuildContents event);
	
}
