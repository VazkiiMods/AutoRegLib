package vazkii.arl.interf;

import java.util.Collections;
import java.util.List;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IDropInItem {

	boolean canDropItemIn(PlayerEntity player, ItemStack stack, ItemStack incoming);

	ItemStack dropItemIn(PlayerEntity player, ItemStack stack, ItemStack incoming);

	@OnlyIn(Dist.CLIENT)
	default List<ITextProperties> getDropInTooltip(ItemStack stack) {
		return Collections.singletonList(new TranslationTextComponent("arl.misc.right_click_add"));
	}

}
