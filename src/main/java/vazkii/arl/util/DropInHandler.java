package vazkii.arl.util;

import java.util.concurrent.Callable;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
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
		if(gui instanceof ContainerScreen) {
			ContainerScreen<?> containerGui = (ContainerScreen<?>) gui;
			ItemStack held = mc.player.inventory.getCarried();
			if(!held.isEmpty()) {
				Container container = containerGui.getMenu();
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
		if(gui instanceof ContainerScreen && event.getButton() == 1) {
			ContainerScreen<?> container = (ContainerScreen<?>) gui;
			Slot under = container.getSlotUnderMouse();
			ItemStack held = mc.player.inventory.getCarried();

			if(under != null && !held.isEmpty() && under.mayPickup(mc.player)) {
				ItemStack stack = under.getItem();
				IDropInItem dropin = getDropInHandler(stack);
				if(dropin != null) {
					AutoRegLib.network.sendToServer(container instanceof CreativeScreen ?
							new MessageDropInCreative(under.getSlotIndex(), held) :
							new MessageDropIn(under.index));

					container.isQuickCrafting = false;
					event.setCanceled(true);
				}
			}
		}
	}

	public static void executeDropIn(PlayerEntity player, int slot) {
		if (player == null)
			return;

		Container container = player.containerMenu;
		Slot slotObj = container.slots.get(slot);
		ItemStack target = slotObj.getItem();
		IDropInItem dropin = getDropInHandler(target);

		ItemStack stack = player.inventory.getCarried();

		if(dropin != null && dropin.canDropItemIn(player, target, stack, slotObj)) {
			ItemStack result = dropin.dropItemIn(player, target, stack, slotObj);
			slotObj.set(result);
			player.inventory.setCarried(stack);
			if (player instanceof ServerPlayerEntity) {
				((ServerPlayerEntity) player).ignoreSlotUpdateHack = false;
				((ServerPlayerEntity) player).broadcastCarriedItem();
			}
		}
	}

	public static void executeCreativeDropIn(PlayerEntity player, int slot, ItemStack held) {
		if (player == null || !player.isCreative())
			return;

		ItemStack target = player.inventory.getItem(slot);
		IDropInItem dropin = getDropInHandler(target);
		Slot slotObj = player.inventoryMenu.slots.get(slot);

		if(dropin != null && dropin.canDropItemIn(player, target, held, slotObj)) {
			ItemStack result = dropin.dropItemIn(player, target, held, slotObj);
			player.inventory.setItem(slot, result);
			player.inventory.setCarried(held);
			if (player instanceof ServerPlayerEntity)
				AutoRegLib.network.sendToPlayer(new MessageSetSelectedItem(held),
					(ServerPlayerEntity) player);
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
		public INBT writeNBT(Capability<IDropInItem> capability, IDropInItem instance, Direction side) {
			return null;
		}

		@Override
		public void readNBT(Capability<IDropInItem> capability, IDropInItem instance, Direction side, INBT nbt) {
			// NO-OP
		}

		@Override
		public IDropInItem call() {
			return new DefaultImpl();
		}

		private static class DefaultImpl implements IDropInItem {

			@Override
			public boolean canDropItemIn(PlayerEntity player, ItemStack stack, ItemStack incoming, Slot slotObj) {
				return false;
			}

			@Override
			public ItemStack dropItemIn(PlayerEntity player, ItemStack stack, ItemStack incoming, Slot slotObj) {
				return incoming;
			}

		}

	}

}
