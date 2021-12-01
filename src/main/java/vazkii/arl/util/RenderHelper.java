/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 19, 2014, 5:40:38 PM (GMT)]
 */
package vazkii.arl.util;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class RenderHelper {

	public static void renderStar(int color, float scale, long seed) {
		renderStar(color, scale, scale, scale, seed);
	}

	public static void renderStar(int color, float xScale, float yScale, float zScale, long seed) {
		Tesselator tessellator = Tesselator.getInstance();

		float ticks = (ClientTicker.ticksInGame % 200) + ClientTicker.partialTicks;
		if (ticks >= 100)
			ticks = 200 - ticks - 1;

		float f1 = ticks / 200F;
		float f2 = 0F;
		if (f1 > 0.7F)
			f2 = (f1 - 0.7F) / 0.2F;
		Random random = new Random(seed);

		RenderSystem.pushMatrix();
		RenderSystem.disableTexture();
		RenderSystem.shadeModel(GL11.GL_SMOOTH);
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		RenderSystem.disableAlphaTest();
		RenderSystem.enableCull();
		RenderSystem.depthMask(false);
		RenderSystem.scalef(xScale, yScale, zScale);

		for (int i = 0; i < (f1 + f1 * f1) / 2F * 90F + 30F; i++) {
			RenderSystem.rotatef(random.nextFloat() * 360F, 1F, 0F, 0F);
			RenderSystem.rotatef(random.nextFloat() * 360F, 0F, 1F, 0F);
			RenderSystem.rotatef(random.nextFloat() * 360F, 0F, 0F, 1F);
			RenderSystem.rotatef(random.nextFloat() * 360F, 1F, 0F, 0F);
			RenderSystem.rotatef(random.nextFloat() * 360F, 0F, 1F, 0F);
			RenderSystem.rotatef(random.nextFloat() * 360F + f1 * 90F, 0F, 0F, 1F);
			tessellator.getBuilder().begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
			float f3 = random.nextFloat() * 20F + 5F + f2 * 10F;
			float f4 = random.nextFloat() * 2F + 1F + f2 * 2F;
			float r = ((color & 0xFF0000) >> 16) / 255F;
			float g = ((color & 0xFF00) >> 8) / 255F;
			float b = (color & 0xFF) / 255F;
			tessellator.getBuilder().vertex(0, 0, 0).color(r, g, b, 1F - f2).endVertex();
			tessellator.getBuilder().vertex(-0.866D * f4, f3, -0.5F * f4).color(0, 0, 0, 0).endVertex();
			tessellator.getBuilder().vertex(0.866D * f4, f3, -0.5F * f4).color(0, 0, 0, 0).endVertex();
			tessellator.getBuilder().vertex(0, f3, 1F * f4).color(0, 0, 0, 0).endVertex();
			tessellator.getBuilder().vertex(-0.866D * f4, f3, -0.5F * f4).color(0, 0, 0, 0).endVertex();
			tessellator.end();
		}

		RenderSystem.depthMask(true);
		RenderSystem.disableCull();
		RenderSystem.disableBlend();
		RenderSystem.shadeModel(GL11.GL_FLAT);
		RenderSystem.color4f(1F, 1F, 1F, 1F);
		RenderSystem.enableTexture();
		RenderSystem.enableAlphaTest();
		RenderSystem.popMatrix();
	}

	public static String getKeyDisplayString(String keyName) {
		String key = null;
		KeyMapping[] keys = Minecraft.getInstance().options.keyMappings;
		for(KeyMapping otherKey : keys)
			if(otherKey.getName().equals(keyName)) {
				key = otherKey.saveString();
				break;
			}

		return I18n.get(key);
	}
}