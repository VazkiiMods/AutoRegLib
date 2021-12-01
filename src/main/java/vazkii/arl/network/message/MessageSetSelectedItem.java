package vazkii.arl.network.message;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.network.NetworkEvent;
import vazkii.arl.network.IMessage;

/**
 * @author WireSegal
 * Created at 2:13 PM on 9/6/19.
 */
public class MessageSetSelectedItem implements IMessage {
	private static final long serialVersionUID = -8037505410464752326L;

	public ItemStack stack;

	public MessageSetSelectedItem() { }

	public MessageSetSelectedItem(ItemStack stack) {
		this.stack = stack;
	}

	public boolean receive(NetworkEvent.Context context) {
		context.enqueueWork(() -> {
			Player player = context.getSender();
			if (player != null)
				player.inventory.setCarried(stack);
		});

		return true;
	}
}
