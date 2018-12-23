/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [16/01/2016, 18:30:59 (GMT)]
 */
package vazkii.arl.util;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public final class TooltipHandler {

	@SideOnly(Side.CLIENT)
	public static void tooltipIfShift(List<String> tooltip, Runnable r) {
		if(GuiScreen.isShiftKeyDown())
			r.run();
		else addToTooltip(tooltip, "arl.misc.shiftForInfo");
	}

	@SideOnly(Side.CLIENT)
	public static void addToTooltip(List<String> tooltip, String s, Object... format) {
		s = local(s).replaceAll("&", "\u00a7");

		Object[] formatVals = new String[format.length];
		for(int i = 0; i < format.length; i++)
			formatVals[i] = local(format[i].toString()).replaceAll("&", "\u00a7");

		if(formatVals.length > 0)
			s = String.format(s, formatVals);

		tooltip.add(s);
	}

	@SideOnly(Side.CLIENT)
	public static String local(String s) {
		return I18n.translateToLocal(s);
	}
	
}
