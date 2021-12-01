package vazkii.arl.network.message;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.network.NetworkEvent;
import vazkii.arl.network.IMessage;
import vazkii.arl.util.DropInHandler;

public class MessageDropInCreative implements IMessage {

	private static final long serialVersionUID = 6654581117899104558L;

	public int slot;
	public ItemStack stack;

	public MessageDropInCreative() { }

	public MessageDropInCreative(int slot, ItemStack stack) {
		this.slot = slot;
		this.stack = stack;
	}
	
	public boolean receive(NetworkEvent.Context context) {
		context.enqueueWork(() -> DropInHandler.executeCreativeDropIn(context.getSender(), slot, stack));
		
		return true;
	}
	
}
