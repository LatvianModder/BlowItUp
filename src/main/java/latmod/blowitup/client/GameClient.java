package latmod.blowitup.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import latmod.blowitup.entity.EntityRegistry;
import latmod.blowitup.gui.GuiStart;
import latmod.blowitup.world.Level;
import latmod.core.EventHandler;
import latmod.core.EventResized;
import latmod.core.LMFrame;
import latmod.core.LatCoreGL;
import latmod.core.input.EventKeyPressed;
import latmod.core.input.EventMousePressed;
import latmod.core.input.EventMouseScrolled;
import latmod.core.input.LMInput;
import latmod.core.rendering.GLHelper;
import latmod.core.rendering.Renderer;
import latmod.lib.LMJsonUtils;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	
	@Override
	public void onLoaded()
	{
		inst = this;
		
		super.onLoaded();
		logger.setParent(LatCoreGL.logger);
		setTitle("Blow It Up");
		gameLocation = new File("game/");
		if(!gameLocation.exists()) gameLocation.mkdirs();
		ClientSettings.load();
		
		loadLevels();
		EntityRegistry.init();
		
		openGui(new GuiStart(this));
		debugInfo = new ArrayList<>();
		
		EventHandler.MAIN.addHandler(EventResized.class, event -> {
			System.out.println(event.prevWidth + " : " + event.prevHeight);
		});
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
	
	@Override
	public void onRender()
	{
		GLHelper.background.setF(0F, 0F, 0F, 1F);
		
		debugInfo.clear();
		debugInfo.add("FPS: " + FPS);
		
		Renderer.enter2D();
		GLHelper.texture.enable();
		
		if(clientWorld != null)
		{
			clientWorld.onUpdate();
			clientWorld.renderer.render(this);
			//debugInfo.add(clientWorld.level.getTile(clientWorld.renderer.mouse.toPos2I()));
		}
		
		GLHelper.color.setF(1F, 1F, 1F, 1F);
		for(int i = 0; i < debugInfo.size(); i++)
			getGui().font.drawText(4D, 4D + i * 20, String.valueOf(debugInfo.get(i)));
	}
	
	@Override
	public void onKeyPressed(EventKeyPressed e)
	{
		if(e.key == GLFW.GLFW_KEY_ESCAPE) destroy();
		else if(e.key == GLFW.GLFW_KEY_I)
		{
			loadLevels();
			clientWorld = new WorldClient(levels.get("test_level"));
			e.cancel();
		}
		
		if(clientWorld != null) clientWorld.clientPlayer.keyPressed(e.key);
		
		super.onKeyPressed(e);
	}
	
	@Override
	public void onMousePressed(EventMousePressed e)
	{
		if(e.button == 1)
		{
			LMInput.setMouseGrabbed(!LMInput.isMouseGrabbed());
			e.cancel();
		}
		
		super.onMousePressed(e);
	}
	
	@Override
	public void onMouseScrolled(EventMouseScrolled e)
	{
		//clientWorld.renderer.renderScale *= e.up ? 2D : 0.5D;
		super.onMouseScrolled(e);
	}
}