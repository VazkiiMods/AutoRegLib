package vazkii.arl.util;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import vazkii.arl.interf.IDropInItem;

import javax.annotation.Nonnull;

public abstract class AbstractDropIn implements ICapabilityProvider, IDropInItem {

	@Override
	@SuppressWarnings("ConstantConditions")
	public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing) {
		return capability == DROP_IN_CAPABILITY;
	}

	@Override
	@SuppressWarnings("ConstantConditions")
	public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing) {
		return capability == DROP_IN_CAPABILITY ? DROP_IN_CAPABILITY.cast(this) : null;
	}

}
