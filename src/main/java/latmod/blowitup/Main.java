package latmod.blowitup;

import latmod.blowitup.tile.Tiles;
import latmod.blowitup.world.World;
import latmod.core.LMFrame;
import latmod.core.rendering.*;
import latmod.core.res.Resource;

import java.io.File;

/**
 * Created by LatvianModder on 27.12.2015.
 */
public class Main extends LMFrame
{
	public static Main inst = null;

	public Main(String[] args) throws Exception
	{ super(args, 800, 600); }

	public static void main(String[] args) throws Exception
	{ inst = new Main(args); }

	public File gameLocation = null;
	public World clientWorld = null;

	public void onLoaded() throws Exception
	{
		super.onLoaded();
		setTitle("Blow It Up");
		gameLocation = new File("./");
	}

	public void onRender() throws Exception
	{
		Renderer.enter2D();
		GLHelper.texture.enable();

		textureManager.getTexture(Resource.getTexture("world/floor.png")).bind();

		int w = 20;
		int h = 15;

		for(int y = 0; y < h; y++) for(int x = 0; x < w; x++)
		{
				Renderer.rect(16D + x * 32D, 16D + y * 32D, 32D, 32D, 0D, 0D, 1D, 1D);
		}

		Tiles.stone.getTexture(textureManager, 0).bind();

		for(int y = 0; y < h; y++) for(int x = 0; x < w; x++)
		{
			if(x == 0 || y == 0 || x == w - 1 || y == h - 1)
				Renderer.rect(16D + x * 32D, 16D + y * 32D, 32D, 32D, 0D, 0D, 1D, 1D);
		}

		font.drawText(4D, 4D, "FPS: " + FPS);
	}
}