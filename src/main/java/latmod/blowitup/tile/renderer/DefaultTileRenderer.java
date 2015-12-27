package latmod.blowitup.tile.renderer;

import latmod.blowitup.tile.Tile;
import latmod.core.rendering.*;

/**
 * Created by LatvianModder on 27.12.2015.
 */
public class DefaultTileRenderer implements ITileRenderer
{
	public static final DefaultTileRenderer instance = new DefaultTileRenderer();

	public void renderTile(Tile t, int x, int z, TextureManager m)
	{
		Texture tex = t.getTexture(m, 0);
		if(tex == null) return;

		GLHelper.push();
		GLHelper.translate(x, z);

		GLHelper.pop();
	}
}
