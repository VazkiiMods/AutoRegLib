package vazkii.arl.util;

import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.arl.interf.IDropInItem;
import vazkii.arl.network.NetworkHandler;
import vazkii.arl.network.message.MessageDropIn;

public final class DropInHandler {

	@SubscribeEvent
	public static void onDrawScreen(GuiScreenEvent.DrawScreenEvent.Post event) {
		Minecraft mc = Minecraft.getMinecraft();
		GuiScreen gui = mc.currentScreen;
		if(gui instanceof GuiContainer) {
			GuiContainer container = (GuiContainer) gui;
			ItemStack held = mc.player.inventory.getItemStack();
			if(!held.isEmpty()) {
				Slot under = container.getSlotUnderMouse();
				for(Slot s : container.inventorySlots.inventorySlots) {
					if(s.inventory != mc.player.inventory)
						continue;

					ItemStack stack = s.getStack();
					IDropInItem dropin = getDropInHandler(stack);
					if(dropin != null && dropin.canDropItemIn(stack, held)) {
						if(s == under) {
							int x = event.getMouseX();
							int y = event.getMouseY();
							RenderHelper.renderTooltip(x, y, dropin.getDropInTooltip(stack));
						} else {
							int x = container.getGuiLeft() + s.xPos;
							int y = container.getGuiTop() + s.yPos;

							GlStateManager.disableDepth();
							mc.fontRenderer.drawStringWithShadow("+", x + 10, y + 8, 0xFFFF00);
							GlStateManager.enableDepth();
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onRightClick(GuiScreenEvent.MouseInputEvent.Pre event) {
		Minecraft mc = Minecraft.getMinecraft();
		GuiScreen gui = mc.currentScreen;
		if(gui instanceof GuiContainer && Mouse.getEventButton() == 1) {
			GuiContainer container = (GuiContainer) gui;
			Slot under = container.getSlotUnderMouse();
			ItemStack held = mc.player.inventory.getItemStack();

			if(under != null && !held.isEmpty() && under.inventory == mc.player.inventory) {
				ItemStack stack = under.getStack();
				IDropInItem dropin = getDropInHandler(stack);
				if(dropin != null && dropin.canDropItemIn(stack, held)) {
					mc.player.inventory.setItemStack(ItemStack.EMPTY);
					NetworkHandler.INSTANCE.sendToServer(new MessageDropIn(under.getSlotIndex(), held));
					event.setCanceled(true);
				}
			}
		}
	}
	
	public static void executeDropIn(EntityPlayer player, int slot, ItemStack stack) {
		ItemStack target = player.inventory.getStackInSlot(Math.min(player.inventory.getSizeInventory() - 1, slot));
		IDropInItem dropin = getDropInHandler(target);
		
		if(dropin != null && dropin.canDropItemIn(target, stack)) {
			ItemStack held = player.inventory.getItemStack();

			if(player.isCreative() && !stack.isEmpty())
				held = stack;

			ItemStack result = dropin.dropItemIn(target, stack);
			player.inventory.setInventorySlotContents(slot, result);
			player.inventory.setItemStack(ItemStack.EMPTY);
		}
	}
	
	public static IDropInItem getDropInHandler(ItemStack stack) {
		if(stack.hasCapability(IDropInItem.DROP_IN_CAPABILITY, null)) {
			IDropInItem item = stack.getCapability(IDropInItem.DROP_IN_CAPABILITY, null);
			if(item != null)
				return item;
		}
		
		if(stack.getItem() instanceof IDropInItem)
			return (IDropInItem) stack.getItem();
		
		return null;
	}
	
}
