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

public abstract class TileEntityMessage<T extends TileEntity> extends NetworkMessage {

	public BlockPos pos;
	public transient T tile;
	public transient MessageContext context;

	public TileEntityMessage() { }

	public TileEntityMessage(BlockPos pos) {
		this.pos = pos;
	}

	@Override
	public final IMessage handleMessage(MessageContext context) {
		this.context = context;
		World world = context.getServerHandler().playerEntity.getEntityWorld();
		TileEntity tile = world.getTileEntity(pos);
		if(tile != null)
			try {
				T castTile = (T) tile;
				this.tile = castTile;
				((WorldServer) world).addScheduledTask(getAction());
			} catch(ClassCastException e) { }

		return super.handleMessage(context);
	}

	public abstract Runnable getAction();

}
