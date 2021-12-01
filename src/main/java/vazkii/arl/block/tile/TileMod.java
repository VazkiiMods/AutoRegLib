/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 21, 2014, 9:18:28 PM (GMT)]
 */
package vazkii.arl.block.tile;

import javax.annotation.Nonnull;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import vazkii.arl.util.VanillaPacketDispatcher;

public abstract class TileMod extends TileEntity {

	public TileMod(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	@Nonnull
	@Override
	public CompoundNBT save(CompoundNBT par1nbtTagCompound) {
		CompoundNBT nbt = super.save(par1nbtTagCompound);

		writeSharedNBT(par1nbtTagCompound);
		return nbt;
	}

	@Override
	public void load(BlockState p_230337_1_, CompoundNBT p_230337_2_) {
		super.load(p_230337_1_, p_230337_2_);

		readSharedNBT(p_230337_2_);
	}

	public void writeSharedNBT(CompoundNBT cmp) {
		// NO-OP
	}

	public void readSharedNBT(CompoundNBT cmp) {
		// NO-OP
	}
	
	public void sync() {
		VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
	}
	
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT cmp = new CompoundNBT();
		writeSharedNBT(cmp);
		return new SUpdateTileEntityPacket(getBlockPos(), 0, cmp);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
		super.onDataPacket(net, packet);
		readSharedNBT(packet.getTag());
	}

}
