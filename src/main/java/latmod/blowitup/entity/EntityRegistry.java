package latmod.blowitup.entity;

import java.util.HashMap;

/**
 * Created by LatvianModder on 28.12.2015.
 */
public class EntityRegistry
{
	private static final HashMap<String, Class<? extends Entity>> registry = new HashMap<>();
	private static final HashMap<Class<?>, Integer> IDs = new HashMap<>();
	
	public static void init()
	{
		
	}
	
	public static void register(int i, String id, Class<? extends Entity> c)
	{
		registry.put(id, c);
		IDs.put(c, Integer.valueOf(i));
	}
	
	public static int getID(Class<?> c)
	{
		if(c == null || c == Entity.class) return 0;
		Integer id = IDs.get(c);
		if(id == null) return getID(c.getSuperclass());
		return 0;
	}
}