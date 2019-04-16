package vazkii.arl.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;
import vazkii.arl.AutoRegLib;
import vazkii.arl.interf.IDropInItem;
import vazkii.arl.network.NetworkHandler;
import vazkii.arl.network.message.MessageDropIn;

import java.util.concurrent.Callable;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = AutoRegLib.MOD_ID)
public final class DropInHandler {

	public static void register() {
		CapabilityManager.INSTANCE.register(IDropInItem.class, CapabilityFactory.INSTANCE, CapabilityFactory.INSTANCE);
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onDrawScreen(GuiScreenEvent.DrawScreenEvent.Post event) {
		Minecraft mc = Minecraft.getMinecraft();
		GuiScreen gui = mc.currentScreen;
		if(gui instanceof GuiContainer) {
			GuiContainer container = (GuiContainer) gui;
			ItemStack held = mc.player.inventory.getItemStack();
			if(!held.isEmpty()) {
				Slot under = container.getSlotUnderMouse();
				for(Slot s : container.inventorySlots.inventorySlots) {
					ItemStack stack = s.getStack();
					IDropInItem dropin = getDropInHandler(stack);
					if(dropin != null) {
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
	@SideOnly(Side.CLIENT)
	public static void onRightClick(GuiScreenEvent.MouseInputEvent.Pre event) {
		Minecraft mc = Minecraft.getMinecraft();
		GuiScreen gui = mc.currentScreen;
		if(gui instanceof GuiContainer && Mouse.getEventButton() == 1) {
			GuiContainer container = (GuiContainer) gui;
			Slot under = container.getSlotUnderMouse();
			ItemStack held = mc.player.inventory.getItemStack();

			if(under != null && !held.isEmpty()) {
				ItemStack stack = under.getStack();
				IDropInItem dropin = getDropInHandler(stack);
				if(dropin != null) {
					NetworkHandler.INSTANCE.sendToServer(new MessageDropIn(under.slotNumber, held));
					event.setCanceled(true);
				}
			}
		}
	}
	
	public static void executeDropIn(EntityPlayer player, int slot, ItemStack stack) {
		Container container = player.openContainer;
		Slot slotObj = container.inventorySlots.get(slot);
		ItemStack target = slotObj.getStack();
		IDropInItem dropin = getDropInHandler(target);
		
		if(dropin != null && dropin.canDropItemIn(player, target, stack)) {
			ItemStack held = player.inventory.getItemStack();

			if(player.isCreative() && !stack.isEmpty())
				held = stack;

			ItemStack result = dropin.dropItemIn(player, target, held);
			slotObj.putStack(result);
			player.inventory.setItemStack(ItemStack.EMPTY);
			player.inventory.markDirty();
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
	
	private static class CapabilityFactory implements Capability.IStorage<IDropInItem>, Callable<IDropInItem> {

		private static CapabilityFactory INSTANCE = new CapabilityFactory(); 
		
		@Override
		public NBTBase writeNBT(Capability<IDropInItem> capability, IDropInItem instance, EnumFacing side) {
			return null;
		}

		@Override
		public void readNBT(Capability<IDropInItem> capability, IDropInItem instance, EnumFacing side, NBTBase nbt) {
			// NO-OP
		}

		@Override
		public IDropInItem call() {
			return new DefaultImpl();
		}
		
		private static class DefaultImpl implements IDropInItem {

			@Override
			public boolean canDropItemIn(EntityPlayer player, ItemStack stack, ItemStack incoming) {
				return false;
			}

			@Override
			public ItemStack dropItemIn(EntityPlayer player, ItemStack stack, ItemStack incoming) {
				return incoming;
			}
			
		}
		
	}
	
}
