package latmod.blowitup.tile;

import latmod.blowitup.world.Item;

/**
 * Created by LatvianModder on 27.12.2015.
 */
public class TileItem extends Tile
{
	public final Item item;

	public TileItem(Item i)
	{
		super("item_" + i.ID);
		item = i;
	}
}