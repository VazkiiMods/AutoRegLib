package vazkii.arl.interf;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.event.CreativeModeTabEvent;

public interface ICreativeExtras {
	default boolean canAddToCreativeTab(CreativeModeTab tab) {
		return true;
	}

	// TODO Fix Me
	void addCreativeModeExtras(CreativeModeTab tab, CreativeModeTabEvent.BuildContents event);
}
