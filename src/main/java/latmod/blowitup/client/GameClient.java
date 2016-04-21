package latmod.blowitup.client;

import com.google.gson.*;
import latmod.blowitup.entity.EntityRegistry;
import latmod.blowitup.gui.GuiStart;
import latmod.blowitup.world.Level;
import latmod.core.*;
import latmod.core.input.*;
import latmod.core.rendering.*;
import latmod.lib.LMJsonUtils;
import org.lwjgl.input.*;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * Created by LatvianModder on 27.12.2015.
 */
public class GameClient extends LMFrame
{
	public static GameClient inst = null;
	public static final Logger logger = Logger.getLogger("GameClient");
	
	public GameClient(String[] args) throws Exception
	{ super(args, 800, 600); }
	
	public static void main(String[] args) throws Exception
	{ inst = new GameClient(args); }
	
	public File gameLocation = null;
	public WorldClient clientWorld = null;
	public List<Object> debugInfo;
	public static final Map<String, Level> levels = new HashMap<>();
	
	public void onLoaded() throws Exception
	{
		super.onLoaded();
		logger.setParent(LatCoreGL.logger);
		setTitle("Blow It Up");
		gameLocation = new File("game/");
		if(!gameLocation.exists()) gameLocation.mkdirs();
		ClientSettings.load();
		
		loadLevels();
		EntityRegistry.init();
		
		openGui(new GuiStart());
		debugInfo = new ArrayList<>();
	}
	
	public static void loadLevels()
	{
		levels.clear();
		File f = new File(GameClient.inst.gameLocation, "levels");
		if(!f.exists()) f.mkdirs();
		
		File[] f1 = f.listFiles();
		if(f1 != null) for(File f2 : f1)
		{
			if(f2.isFile() && f2.getName().endsWith(".json"))
			{
				try
				{
					JsonElement e = LMJsonUtils.fromJson(f2);
					
					if(e != null && e.isJsonObject())
					{
						JsonObject o = e.getAsJsonObject();
						Level l = Level.loadFromJson(f, o);
						if(l != null) levels.put(l.getID(), l);
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		
		GameClient.logger.info("Loaded levels: " + levels.keySet());
	}
	
	public void onRender() throws Exception
	{
		GLHelper.background.setF(1F, 1F, 1F, 1F);
		
		debugInfo.clear();
		debugInfo.add("FPS: " + FPS);
		
		Renderer.enter2D();
		GLHelper.texture.enable();
		
		if(clientWorld != null)
		{
			clientWorld.onUpdate();
			clientWorld.renderer.render();
			//debugInfo.add(clientWorld.level.getTile(clientWorld.renderer.mouse.toPos2I()));
		}
		
		GLHelper.color.setF(0F, 0F, 0F, 1F);
		for(int i = 0; i < debugInfo.size(); i++)
			getGui().font.drawText(4D, 4D + i * 20, String.valueOf(debugInfo.get(i)));
	}
	
	public static void test(Consumer<Integer> t)
	{
		t.accept(10);
	}
	
	public void onKeyPressed(EventKeyPressed e)
	{
		if(e.key == Keyboard.KEY_ESCAPE) destroy();
		else if(e.key == Keyboard.KEY_I)
		{
			loadLevels();
			clientWorld = new WorldClient(levels.get("test_level"));
			return;
		}
		
		if(clientWorld != null) clientWorld.clientPlayer.keyPressed(e.key);
	}
	
	public void onMousePressed(EventMousePressed e)
	{
		if(e.button == 1) Mouse.setGrabbed(!Mouse.isGrabbed());
	}
	
	public void onMouseScrolled(EventMouseScrolled e)
	{
		//clientWorld.renderer.renderScale *= e.up ? 2D : 0.5D;
	}
}