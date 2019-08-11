package vazkii.arl.interf;

import java.util.Collections;
import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IDropInItem {

	boolean canDropItemIn(PlayerEntity player, ItemStack stack, ItemStack incoming);

	ItemStack dropItemIn(PlayerEntity player, ItemStack stack, ItemStack incoming);

	@OnlyIn(Dist.CLIENT)
	default List<String> getDropInTooltip(ItemStack stack) {
		return Collections.singletonList(I18n.format("arl.misc.rightClickAdd"));
	}

}
