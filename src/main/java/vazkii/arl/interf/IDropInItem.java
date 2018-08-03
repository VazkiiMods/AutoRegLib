package vazkii.arl.interf;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IDropInItem {

	@CapabilityInject(IDropInItem.class)
	public static Capability<IDropInItem> DROP_IN_CAPABILITY = null;

	public boolean canDropItemIn(ItemStack stack, ItemStack incoming);

	public ItemStack dropItemIn(ItemStack stack, ItemStack incoming);

	@SideOnly(Side.CLIENT)
	public default List<String> getDropInTooltip(ItemStack stack) {
		return Arrays.asList(I18n.format("arl.misc.rightClickAdd"));
	}

}
