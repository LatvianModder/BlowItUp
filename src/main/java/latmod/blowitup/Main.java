package latmod.blowitup;

import latmod.blowitup.tile.Tiles;
import latmod.blowitup.world.*;
import latmod.core.*;
import latmod.core.input.LMMouse;
import latmod.core.input.keys.*;
import latmod.core.rendering.*;
import latmod.lib.util.Pos2I;
import org.lwjgl.input.Keyboard;

import java.io.File;
import java.util.logging.Logger;

/**
 * Created by LatvianModder on 27.12.2015.
 */
public class Main extends LMFrame implements IKeyPressed
{
	public static Main inst = null;
	public static final Logger logger = Logger.getLogger("Game");

	public Main(String[] args) throws Exception
	{ super(args, 800, 600); }

	public static void main(String[] args) throws Exception
	{ inst = new Main(args); }

	public File gameLocation = null;
	public World clientWorld = null;

	public void onLoaded() throws Exception
	{
		super.onLoaded();
		logger.setParent(LatCoreGL.logger);
		setTitle("Blow It Up");
		gameLocation = new File("./");
		Level.load();
		logger.info("Loaded levels: " + Level.levels);

		clientWorld = new World(Level.levels.get("test_level"));
		clientWorld.renderer.updateLight();
		clientWorld.renderer.markDirty();
	}

	public void onRender() throws Exception
	{
		Renderer.enter2D();
		GLHelper.texture.enable();

		if(clientWorld != null)
		{
			GLHelper.push();
			GLHelper.translate(30, 30);
			GLHelper.scale(32D, 32D, 1D);
			clientWorld.renderer.render(textureManager);
			GLHelper.pop();
		}

		if(LMMouse.isButtonDown(1))
		{
			clientWorld.renderer.updateLight();
		}

		font.drawText(4D, 4D, "FPS: " + FPS);
	}

	public void onKeyPressed(EventKeyPressed e)
	{
		Pos2I p = new Pos2I(LMMouse.x, LMMouse.y);
		p.x = (p.x -= 30) / 32;
		p.y = (p.y -= 30) / 32;

		if(e.key == Keyboard.KEY_R)
			clientWorld.level.setTile(p, Tiles.air);
		else if(e.key == Keyboard.KEY_L)
			clientWorld.level.setTile(p, Tiles.lamp);
		else if(e.key == Keyboard.KEY_P)
			clientWorld.level.setTile(p, Tiles.planks);
		else if(e.key == Keyboard.KEY_S)
			clientWorld.renderer.smoothLight = !clientWorld.renderer.smoothLight;

		clientWorld.renderer.updateLight();
	}
}