package latmod.blowitup.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.latmod.latcoregl.EventHandler;
import com.latmod.latcoregl.EventResized;
import com.latmod.latcoregl.LMFrame;
import com.latmod.latcoregl.LatCoreGL;
import com.latmod.latcoregl.input.EventKeyPressed;
import com.latmod.latcoregl.input.EventMousePressed;
import com.latmod.latcoregl.input.EventMouseScrolled;
import com.latmod.latcoregl.input.LMInput;
import com.latmod.latcoregl.rendering.GLHelper;
import com.latmod.latcoregl.rendering.Renderer;
import com.latmod.lib.json.LMJsonUtils;
import latmod.blowitup.entity.EntityRegistry;
import latmod.blowitup.gui.GuiStart;
import latmod.blowitup.world.Level;
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
    public static final Logger logger = Logger.getLogger("GameClient");
    public static final Map<String, Level> levels = new HashMap<>();
    public static GameClient inst = null;
    public File gameLocation = null;
    public WorldClient clientWorld = null;
    public List<Object> debugInfo;

    public GameClient(String[] args) throws Exception
    {
        super(args, 800, 600);
    }

    public static void main(String[] args) throws Exception
    {
        inst = new GameClient(args);
    }

    public static void loadLevels()
    {
        levels.clear();
        File f = new File(GameClient.inst.gameLocation, "levels");
        if(!f.exists())
        {
            f.mkdirs();
        }

        File[] f1 = f.listFiles();
        if(f1 != null)
        {
            for(File f2 : f1)
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
                            if(l != null)
                            {
                                levels.put(l.getID(), l);
                            }
                        }
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }

        GameClient.logger.info("Loaded levels: " + levels.keySet());
    }

    @Override
    public void onLoaded()
    {
        inst = this;

        super.onLoaded();
        logger.setParent(LatCoreGL.logger);
        setTitle("Blow It Up");
        gameLocation = new File("game/");
        if(!gameLocation.exists())
        {
            gameLocation.mkdirs();
        }
        ClientSettings.load();

        loadLevels();
        EntityRegistry.init();

        openGui(new GuiStart(this));
        debugInfo = new ArrayList<>();

        EventHandler.MAIN.addHandler(EventResized.class, event -> {
            System.out.println(event.prevWidth + " : " + event.prevHeight);
        });
    }

    @Override
    public void onRender()
    {
        GLHelper.background.setF(0.2F, 0.2F, 0.2F, 1F);

        debugInfo.clear();
        debugInfo.add("FPS: " + FPS);

        Renderer.enter2D();
        GLHelper.texture.disable();
        GLHelper.color.setF(1F, 1F, 1F, 1F);
        Renderer.rect(10, 10, 100, 10);
        GLHelper.texture.enable();

        if(clientWorld != null)
        {
            clientWorld.onUpdate();
            clientWorld.renderer.render(this);
            //debugInfo.add(clientWorld.level.getTile(clientWorld.renderer.mouse.toPos2I()));
        }

        GLHelper.color.setF(1F, 1F, 1F, 1F);
        for(int i = 0; i < debugInfo.size(); i++)
        {
            getGui().font.drawText(4D, 4D + i * 20, String.valueOf(debugInfo.get(i)));
        }
    }

    @Override
    public void onKeyPressed(EventKeyPressed e)
    {
        if(e.key == GLFW.GLFW_KEY_ESCAPE)
        {
            destroy();
        }
        else if(e.key == GLFW.GLFW_KEY_I)
        {
            loadLevels();
            clientWorld = new WorldClient(levels.get("test_level"));
            e.cancel();
        }

        if(clientWorld != null)
        {
            clientWorld.clientPlayer.keyPressed(e.key);
        }

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