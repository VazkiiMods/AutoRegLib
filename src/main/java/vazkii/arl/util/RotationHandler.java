package vazkii.arl.util;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;

public final class RotationHandler {

	private static final Rotation[] FACING_TO_ROTATION = new Rotation[] {
		Rotation.NONE,
		Rotation.NONE,
		Rotation.NONE,
		Rotation.CLOCKWISE_180,
		Rotation.COUNTERCLOCKWISE_90,
		Rotation.CLOCKWISE_90
	};
	
	public static EnumFacing rotateFacing(EnumFacing facing, Rotation rot) {
		return rot.rotate(facing);
	}
	
	public static EnumFacing rotateFacing(EnumFacing facing, EnumFacing rot) {
		return rotateFacing(facing, getRotationFromFacing(rot));
	}
	
	public static Rotation getRotationFromFacing(EnumFacing facing) {
		return FACING_TO_ROTATION[facing.ordinal()];
	}
	
}
