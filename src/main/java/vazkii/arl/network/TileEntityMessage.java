/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [16/01/2016, 18:51:44 (GMT)]
 */
package vazkii.arl.network;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public abstract class TileEntityMessage<T extends TileEntity> extends NetworkMessage<TileEntityMessage<T>> {

	public BlockPos pos;
	public transient T tile;
	public transient MessageContext context;

	public TileEntityMessage() { }

	public TileEntityMessage(BlockPos pos) {
		this.pos = pos;
	}

	@Override
	@SuppressWarnings("unchecked")
	public final IMessage handleMessage(MessageContext context) {
		this.context = context;
		World world = context.getServerHandler().player.getEntityWorld();
		TileEntity tile = world.getTileEntity(pos);
		if(tile != null)
			try {
				this.tile = (T) tile;
				((WorldServer) world).addScheduledTask(getAction());
			} catch(ClassCastException ignored) { }

		return super.handleMessage(context);
	}

	public abstract Runnable getAction();

}
