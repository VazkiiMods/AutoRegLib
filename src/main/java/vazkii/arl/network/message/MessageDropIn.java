package vazkii.arl.network.message;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.network.NetworkEvent;
import vazkii.arl.network.IMessage;
import vazkii.arl.util.DropInHandler;

public class MessageDropIn implements IMessage {

	private static final long serialVersionUID = 4879090175821123361L;

	public int slot;
	public ItemStack stack = ItemStack.EMPTY;
	
	public MessageDropIn() { }
	
	public MessageDropIn(int slot) { 
		this.slot = slot;
	}
	
	public MessageDropIn(int slot, ItemStack stack) { 
		this(slot);
		this.stack = stack;
	}
	
	public boolean receive(NetworkEvent.Context context) {
		ServerPlayerEntity player = context.getSender();
		context.enqueueWork(() -> DropInHandler.executeDropIn(player, slot, stack));
		
		return true;
	}
	
}
