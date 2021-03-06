package io.anuke.novi.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import io.anuke.novi.modules.World;
import io.anuke.novi.server.NoviServer;
import io.anuke.novi.systems.*;

public class Entities{
	private static CopyOnWriteArrayList<Entity> list = new CopyOnWriteArrayList<Entity>();
	private static ConcurrentHashMap<Long, Entity> map = new ConcurrentHashMap<Long, Entity>();
	
	private static boolean updated;

	private static ArrayList<EntitySystem> systems = new ArrayList<EntitySystem>();
	private static SpatialSystem spatial;
	private static IteratingSystem basesystem = new EmptySystem();
	
	public static final float loadRange = 1000;
	public static final float unloadRange = 1500;

	public static void add(Entity entity){
		list.add(entity);
		map.put(entity.getID(), entity);
		updated = true;
	}

	public static void remove(Entity entity){
		if(entity == null) return;
		
		map.remove(entity.getID());
		
		if(NoviServer.active()){
			//TODO fix binary remove?
			list.remove(entity);
		}else{
			list.remove(entity);
		}
		
		entity.invalidate();
	}

	public static void remove(long id){
		remove(get(id));
	}

	public static boolean has(long id){
		return map.containsKey(id);
	}

	public static Entity get(long id){
		return map.get(id);
	}

	public static Collection<Entity> list(){
		return list;
	}

	public static synchronized void loadEntities(Collection<Entity> entities){
		list.clear();
		map.clear();

		for(Entity entity : entities){
			entity.add();
		}
	}

	public static Iterable<EntitySystem> getSystems(){
		return systems;
	}
	
	public static <T> T getSystem(Class<T> c){
		for(EntitySystem system : systems){
			if(system.getClass() == c) return (T)system;
		}
		return null;
	}

	public static void addSystem(EntitySystem system){
		if(system instanceof SpatialSystem) spatial = (SpatialSystem)system;
		systems.add(system);
	}

	public static void setBaseSystem(IteratingSystem system){
		basesystem = system;
	}
	
	public static SpatialSystem spatial(){
		return spatial;
	}

	public static synchronized void updateAll(){
		Collection<Entity> entities = list();
		
		for(Entity entity : entities)
			if(basesystem.accept(entity))
				entity.preUpdate();

		for(EntitySystem system : systems){
			system.update(entities);
		}

		for(Entity entity : entities){
			if(!basesystem.accept(entity))
				continue;

			entity.baseUpdate();

			if(NoviServer.active()){
				entity.serverUpdate();
			}
		}

		if(updated && !NoviServer.active()){
			list.sort(Entities::compare);
			updated = false;
		}
	}
	
	public static synchronized void checkUnload(float x, float y){
		for(Entity entity : list()){
			if(!World.loopedWithin(entity.x, x, entity.y, y, unloadRange)){
				entity.remove();
			}
		}
	}

	public static synchronized void drawAll(float playerx, float playery){
		for(Entity entity : list()){
			if(entity.loaded(playerx, playery))
				entity.draw();
		}
	}

	private static void binaryRemove(Entity entity){
		int id = binarySearch(entity.getID());
		
		if(id != -1){
			list.remove(id);
		}
	}

	private static int binarySearch(long key){
		
		int low = 0;
		int high = list.size() - 1;

		while(high >= low){
			int middle = (low + high) / 2;
			long id = list.get(middle).getID();
			
			if(id == key){
				return middle;
			}else if(id < key){
				low = middle + 1;
			}else if(id > key){
				high = middle - 1;
			}
		}
		
		return -1;
	}

	private static int compareID(Entity a, Entity b){
		return a.getID() > b.getID() ? 1 : -1;
	}

	private static int compare(Entity a, Entity b){
		if(a.getLayer() == b.getLayer())
			return 0;
		return a.getLayer() > b.getLayer() ? 1 : -1;
	}
}
