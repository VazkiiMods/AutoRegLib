package vazkii.arl.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class ItemTickHandler {

	@SubscribeEvent
	public static void update(WorldTickEvent event) {
		if(event.phase == Phase.END) {
			List<Entity> entities = new ArrayList(event.world.loadedEntityList);
			entities.forEach((entity) -> {
				if(entity instanceof EntityItem)
					MinecraftForge.EVENT_BUS.post(new EntityItemTickEvent((EntityItem) entity));
			});
		}
	}
	
	public static class EntityItemTickEvent extends EntityEvent {

		EntityItem entityItem;
		
		public EntityItemTickEvent(EntityItem entity) {
			super(entity);
			entityItem = entity;
		}
		
		public EntityItem getEntityItem() {
			return entityItem;
		}
		
	}
	
}
