package vazkii.arl.network.message;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.network.NetworkEvent;
import vazkii.arl.network.IMessage;
import vazkii.arl.util.DropInHandler;

public class MessageDropIn implements IMessage {

	private static final long serialVersionUID = 4879090175821123361L;

	public int slot;
	public ItemStack stack;

	public MessageDropIn() { }
	
	public MessageDropIn(int slot, ItemStack stack) {
		this.slot = slot;
		this.stack = stack;
	}
	
	public boolean receive(NetworkEvent.Context context) {
		context.enqueueWork(() -> DropInHandler.executeDropIn(context.getSender(), slot, stack));
		
		return true;
	}
	
}
