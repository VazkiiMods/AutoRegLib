package vazkii.arl.interf;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.List;

public interface IDropInItem {

	@CapabilityInject(IDropInItem.class)
	Capability<IDropInItem> DROP_IN_CAPABILITY = null;

	boolean canDropItemIn(EntityPlayer player, ItemStack stack, ItemStack incoming);

	ItemStack dropItemIn(EntityPlayer player, ItemStack stack, ItemStack incoming);

	@SideOnly(Side.CLIENT)
	default List<String> getDropInTooltip(ItemStack stack) {
		return Collections.singletonList(I18n.format("arl.misc.rightClickAdd"));
	}

}
