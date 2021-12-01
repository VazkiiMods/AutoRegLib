package vazkii.arl.util;

import java.util.concurrent.Callable;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.Tag;
import net.minecraft.core.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.gui.GuiUtils;
import net.minecraftforge.fml.common.Mod;
import vazkii.arl.AutoRegLib;
import vazkii.arl.interf.IDropInItem;
import vazkii.arl.network.message.MessageDropIn;
import vazkii.arl.network.message.MessageDropInCreative;
import vazkii.arl.network.message.MessageSetSelectedItem;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = AutoRegLib.MOD_ID)
public final class DropInHandler {
	
	@CapabilityInject(IDropInItem.class)
	public static Capability<IDropInItem> DROP_IN_CAPABILITY = null;

	public static void register() {
		CapabilityManager.INSTANCE.register(IDropInItem.class, CapabilityFactory.INSTANCE, CapabilityFactory.INSTANCE);
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void onDrawScreen(GuiScreenEvent.DrawScreenEvent.Post event) {
		Minecraft mc = Minecraft.getInstance();
		Screen gui = mc.screen;
		if(gui instanceof AbstractContainerScreen) {
			AbstractContainerScreen<?> containerGui = (AbstractContainerScreen<?>) gui;
			ItemStack held = mc.player.inventory.getCarried();
			if(!held.isEmpty()) {
				AbstractContainerMenu container = containerGui.getMenu();
				Slot under = containerGui.getSlotUnderMouse();
				for(Slot s : container.slots) {
					ItemStack stack = s.getItem();
					IDropInItem dropin = getDropInHandler(stack);
					if(dropin != null && dropin.canDropItemIn(mc.player, stack, held, s)) {
						if(s == under) {
							int x = event.getMouseX();
							int y = event.getMouseY();
							int width = gui.width;
							int height = gui.height;
							
							GuiUtils.drawHoveringText(event.getMatrixStack(), dropin.getDropInTooltip(stack), x, y, width, height, -1, mc.font);
						} else {
							int x = containerGui.getGuiLeft() + s.x;
							int y = containerGui.getGuiTop() + s.y;

							RenderSystem.pushMatrix();
							RenderSystem.disableDepthTest();
							RenderSystem.translatef(0, 0, 500);
							
							mc.font.drawShadow(event.getMatrixStack(), "+", x + 10, y + 8, 0xFFFF00);
							RenderSystem.enableDepthTest();
							RenderSystem.popMatrix();
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void onRightClick(GuiScreenEvent.MouseReleasedEvent.Pre event) {
		Minecraft mc = Minecraft.getInstance();
		Screen gui = mc.screen;
		if(gui instanceof AbstractContainerScreen && event.getButton() == 1) {
			AbstractContainerScreen<?> container = (AbstractContainerScreen<?>) gui;
			Slot under = container.getSlotUnderMouse();
			ItemStack held = mc.player.inventory.getCarried();

			if(under != null && !held.isEmpty() && under.mayPickup(mc.player)) {
				ItemStack stack = under.getItem();
				IDropInItem dropin = getDropInHandler(stack);
				if(dropin != null) {
					AutoRegLib.network.sendToServer(container instanceof CreativeModeInventoryScreen ?
							new MessageDropInCreative(under.getSlotIndex(), held) :
							new MessageDropIn(under.index));

					container.isQuickCrafting = false;
					event.setCanceled(true);
				}
			}
		}
	}

	public static void executeDropIn(Player player, int slot) {
		if (player == null)
			return;

		AbstractContainerMenu container = player.containerMenu;
		Slot slotObj = container.slots.get(slot);
		ItemStack target = slotObj.getItem();
		IDropInItem dropin = getDropInHandler(target);

		ItemStack stack = player.inventory.getCarried();

		if(dropin != null && dropin.canDropItemIn(player, target, stack, slotObj)) {
			ItemStack result = dropin.dropItemIn(player, target, stack, slotObj);
			slotObj.set(result);
			player.inventory.setCarried(stack);
			if (player instanceof ServerPlayer) {
				((ServerPlayer) player).ignoreSlotUpdateHack = false;
				((ServerPlayer) player).broadcastCarriedItem();
			}
		}
	}

	public static void executeCreativeDropIn(Player player, int slot, ItemStack held) {
		if (player == null || !player.isCreative())
			return;

		ItemStack target = player.inventory.getItem(slot);
		IDropInItem dropin = getDropInHandler(target);
		Slot slotObj = player.inventoryMenu.slots.get(slot);

		if(dropin != null && dropin.canDropItemIn(player, target, held, slotObj)) {
			ItemStack result = dropin.dropItemIn(player, target, held, slotObj);
			player.inventory.setItem(slot, result);
			player.inventory.setCarried(held);
			if (player instanceof ServerPlayer)
				AutoRegLib.network.sendToPlayer(new MessageSetSelectedItem(held),
					(ServerPlayer) player);
		}
	}


	public static IDropInItem getDropInHandler(ItemStack stack) {
		LazyOptional<IDropInItem> opt = stack.getCapability(DROP_IN_CAPABILITY, null);
		if(opt.isPresent())
			return opt.orElseGet(CapabilityFactory.DefaultImpl::new);

		if(stack.getItem() instanceof IDropInItem)
			return (IDropInItem) stack.getItem();

		return null;
	}

	private static class CapabilityFactory implements Capability.IStorage<IDropInItem>, Callable<IDropInItem> {

		private static CapabilityFactory INSTANCE = new CapabilityFactory(); 

		@Override
		public Tag writeNBT(Capability<IDropInItem> capability, IDropInItem instance, Direction side) {
			return null;
		}

		@Override
		public void readNBT(Capability<IDropInItem> capability, IDropInItem instance, Direction side, Tag nbt) {
			// NO-OP
		}

		@Override
		public IDropInItem call() {
			return new DefaultImpl();
		}

		private static class DefaultImpl implements IDropInItem {

			@Override
			public boolean canDropItemIn(Player player, ItemStack stack, ItemStack incoming, Slot slotObj) {
				return false;
			}

			@Override
			public ItemStack dropItemIn(Player player, ItemStack stack, ItemStack incoming, Slot slotObj) {
				return incoming;
			}

		}

	}

}
