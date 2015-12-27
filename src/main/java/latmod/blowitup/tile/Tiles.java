package latmod.blowitup.tile;

import latmod.blowitup.world.Item;
import latmod.lib.Registry;

/**
 * Created by LatvianModder on 27.12.2015.
 */
public class Tiles
{
	public static final Registry<Tile> registry = new Registry<>(true);

	public static final Tile get(String key)
	{
		Registry.Entry<Tile> e = registry.get(key);
		return (e == null) ? air : e.value;
	}

	public static final Tile get(int ID)
	{
		Registry.Entry<Tile> e = registry.getFromID(ID);
		return (e == null) ? air : e.value;
	}

	public static final Tile air = new TileAir("air");
	public static final Tile stone = new TileWall("stone").register();
	public static final Tile planks = new Tile("planks").register();
	public static final Tile[] items = new TileItem[Item.values().length];

	static
	{
		for(int i = 0; i < items.length; i++)
			items[i] = new TileItem(Item.values()[i]).register();
	}
}