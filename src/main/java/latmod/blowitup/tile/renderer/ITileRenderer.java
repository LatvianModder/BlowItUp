package latmod.blowitup.tile.renderer;

import latmod.blowitup.tile.Tile;
import latmod.core.rendering.TextureManager;

/**
 * Created by LatvianModder on 27.12.2015.
 */
public interface ITileRenderer
{
	public void renderTile(Tile t, int x, int z, TextureManager m);
}