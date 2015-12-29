package latmod.blowitup.entity;

import latmod.lib.FastMap;

/**
 * Created by LatvianModder on 28.12.2015.
 */
public class EntityRegistry
{
	private static final FastMap<String, Class<? extends Entity>> registry = new FastMap<>();
	private static final FastMap<Class<?>, Integer> IDs = new FastMap<>();

	public static void init()
	{

	}

	public static void register(int i, String id, Class<? extends Entity> c)
	{ registry.put(id, c); IDs.put(c, Integer.valueOf(i)); }

	public static int getID(Class<?> c)
	{
		if(c == null || c == Entity.class) return 0;
		Integer id = IDs.get(c);
		if(id == null) return getID(c.getSuperclass());
		return 0;
	}
}