package vazkii.arl.interf;

import java.util.Collections;
import java.util.List;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IDropInItem {

	boolean canDropItemIn(Player player, ItemStack stack, ItemStack incoming, Slot slot);

	ItemStack dropItemIn(Player player, ItemStack stack, ItemStack incoming, Slot slot);

	@OnlyIn(Dist.CLIENT)
	default List<FormattedText> getDropInTooltip(ItemStack stack) {
		return Collections.singletonList(new TranslatableComponent("arl.misc.right_click_add"));
	}

}
