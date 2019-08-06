package vazkii.arl.container.slot;

import net.minecraft.inventory.IInventory;

public class SlotType extends SlotFiltered {

	public SlotType(IInventory inventoryIn, int index, int xPosition, int yPosition, Class<?> clazz) {
		super(inventoryIn, index, xPosition, yPosition,
				(stack) -> clazz.isAssignableFrom(stack.getItem().getClass()));
	}

}
