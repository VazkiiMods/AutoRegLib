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

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;

import java.util.List;

public final class TooltipHandler {
	@Environment(EnvType.CLIENT)
	public static void tooltipIfShift(List<String> tooltip, Runnable r) {
		if(Screen.hasShiftDown())
			r.run();
		else addToTooltip(tooltip, "arl.misc.shift_for_info");
	}

	@Environment(EnvType.CLIENT)
	public static void addToTooltip(List<String> tooltip, String s, Object... format) {
		s = I18n.get(s).replaceAll("&", "\u00a7");

		Object[] formatVals = new String[format.length];
		for(int i = 0; i < format.length; i++)
			formatVals[i] = I18n.get(format[i].toString()).replaceAll("&", "\u00a7");

		if(formatVals.length > 0)
			s = String.format(s, formatVals);

		tooltip.add(s);
	}
}
