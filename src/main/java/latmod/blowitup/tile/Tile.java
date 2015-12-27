package latmod.blowitup.tile;

import latmod.blowitup.tile.renderer.*;
import latmod.core.rendering.*;
import latmod.core.res.Resource;
import latmod.lib.util.FinalIDObject;

/**
 * Created by LatvianModder on 27.12.2015.
 */
public class Tile extends FinalIDObject
{
	private Texture texture;

	public Tile(String id)
	{
		super(id);
	}

	public Texture getTexture(TextureManager m, int side)
	{
		if(texture == null)
			texture = m.getTexture(Resource.getTexture("tiles/" + ID + ".png"));
		return texture;
	}

	public ITileRenderer getRenderer()
	{ return DefaultTileRenderer.instance; }

	public <E extends Tile> E register()
	{ Tiles.registry.register(ID, this); return (E)this; }
}