package vazkii.arl.interf;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.event.CreativeModeTabEvent;

public interface ICreativeExtras {
	default boolean canAddToCreativeTab(CreativeModeTab tab) {
		return true;
	}

	void addCreativeModeExtras(CreativeModeTab tab, CreativeModeTabEvent.BuildContents event);
}
