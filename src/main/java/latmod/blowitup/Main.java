package latmod.blowitup;

import com.google.gson.*;
import latmod.blowitup.entity.EntityRegistry;
import latmod.blowitup.gui.*;
import latmod.blowitup.world.*;
import latmod.core.*;
import latmod.core.input.keys.*;
import latmod.core.input.mouse.*;
import latmod.core.rendering.*;
import latmod.lib.*;
import org.lwjgl.input.*;
import org.xml.sax.XMLReader;

import java.io.File;
import java.util.Collections;
import java.util.logging.Logger;

/**
 * Created by LatvianModder on 27.12.2015.
 */
public class Main extends LMFrame implements IKeyPressed, IMouseScrolled, IMousePressed
{
	public static Main inst = null;
	public static final Logger logger = Logger.getLogger("Game");

	public Main(String[] args) throws Exception
	{ super(args, 800, 600); }

	public static void main(String[] args) throws Exception
	{ inst = new Main(args); }

	public File gameLocation = null;
	public GameRenderer renderer;
	public WorldClient clientWorld = null;
	public FastList<Object> debugInfo;
	public static final FastMap<String, Level> levels = new FastMap<>();
	public GuiBase currentGui = null;

	public void onLoaded() throws Exception
	{
		super.onLoaded();
		logger.setParent(LatCoreGL.logger);
		setTitle("Blow It Up");
		gameLocation = new File("game/");
		if(!gameLocation.exists()) gameLocation.mkdirs();
		renderer = new GameRenderer(resManager, textureManager);
		Settings.load();

		loadLevels();
		EntityRegistry.init();

		currentGui = new GuiStart();
		debugInfo = new FastList<>();
	}

	public static void loadLevels()
	{
		levels.clear();
		File f = new File(Main.inst.gameLocation, "levels");
		if(!f.exists()) f.mkdirs();

		File[] f1 = f.listFiles();
		if(f1 != null) for(File f2 : f1)
		{
			if(f2.isFile() && f2.getName().endsWith(".json"))
			{
				try
				{
					JsonElement e = LMJsonUtils.getJsonElement(f2);

					if(e != null && e.isJsonObject())
					{
						JsonObject o = e.getAsJsonObject();
						Level l = Level.loadFromJson(f, o);
						if(l != null) levels.put(l.ID, l);
					}
				}
				catch(Exception e)
				{ e.printStackTrace(); }
			}
		}

		Main.logger.info("Loaded levels: " + levels.keySet());
	}

	public void onRender() throws Exception
	{
		debugInfo.clear();
		debugInfo.add("FPS: " + FPS);

		Renderer.enter2D();
		GLHelper.texture.enable();

		if(clientWorld != null)
		{
			clientWorld.onUpdate();
			clientWorld.renderer.render(renderer);
			//debugInfo.add(clientWorld.level.getTile(clientWorld.renderer.mouse.toPos2I()));
		}

		currentGui.onRender();

		for(int i = 0; i < debugInfo.size(); i++)
			font.drawText(4D, 4D + i * 20, String.valueOf(debugInfo.get(i)));
	}

	public void onKeyPressed(EventKeyPressed e)
	{
		if(e.key == Keyboard.KEY_ESCAPE)
			destroy();
		else if(e.key == Keyboard.KEY_I)
		{
			loadLevels();
			clientWorld = new WorldClient(levels.get("test_level"));
			return;
		}

		if(clientWorld != null)
			clientWorld.clientPlayer.keyPressed(e.key);
	}

	public void onMousePressed(EventMousePressed e)
	{
		if(e.button == 1) Mouse.setGrabbed(!Mouse.isGrabbed());
	}

	public void onMouseScrolled(EventMouseScrolled e)
	{
		//clientWorld.renderer.renderScale *= (LMMouse.dwheel > 0) ? 2D : 0.5D;
	}
}