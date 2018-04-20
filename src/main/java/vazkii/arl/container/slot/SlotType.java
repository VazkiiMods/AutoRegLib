package vazkii.arl.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotType extends SlotFiltered {

	public SlotType(IInventory inventoryIn, int index, int xPosition, int yPosition, Class<?> clazz) {
		super(inventoryIn, index, xPosition, yPosition,
				(stack) -> clazz.isAssignableFrom(stack.getItem().getClass()));
	}

}
