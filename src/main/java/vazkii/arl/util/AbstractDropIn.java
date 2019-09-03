package vazkii.arl.util;

import javax.annotation.Nonnull;

import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import vazkii.arl.interf.IDropInItem;

public abstract class AbstractDropIn implements ICapabilityProvider, IDropInItem {

	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, Direction facing) {
		if(capability == DropInHandler.DROP_IN_CAPABILITY)
			return LazyOptional.of(() -> this).cast();
		else return LazyOptional.empty();
	}

}
