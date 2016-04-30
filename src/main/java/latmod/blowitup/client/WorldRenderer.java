package latmod.blowitup.client;

import latmod.blowitup.entity.Entity;
import latmod.blowitup.tile.Tile;
import latmod.core.IWindow;
import latmod.core.Resource;
import latmod.core.input.LMInput;
import latmod.core.rendering.GLHelper;
import latmod.core.rendering.Renderer;
import latmod.core.rendering.Texture;
import latmod.lib.MathHelperLM;
import latmod.lib.util.Pos2D;
import latmod.lib.util.Pos2I;
import org.lwjgl.opengl.GL11;

import java.util.Map;

/**
 * Created by LatvianModder on 28.12.2015.
 */
public class WorldRenderer
{
	public static final Texture textureFloor = new Texture(new Resource("blowitup", "textures/world/floor.png"));
	
	public final WorldClient world;
	public final Pos2D camera;
	public final Pos2D render;
	public double renderScale;
	private boolean isDirty = true;
	private boolean isLightDirty = true;
	private static int renderListID = -1;
	private static int lightListID = -1;
	public final Pos2D mouse;
	private final LightTile[][] lightMap;
	
	public WorldRenderer(WorldClient w)
	{
		world = w;
		mouse = new Pos2D(0D, 0D);
		camera = new Pos2D(0D, 0D);
		render = new Pos2D(0D, 0D);
		renderScale = 64D;
		lightMap = new LightTile[world.level.width][world.level.height];
		for(int y = 0; y < world.level.height; y++)
			for(int x = 0; x < world.level.width; x++)
				lightMap[x][y] = new LightTile();
	}
	
	public void render(IWindow window)
	{
		if(renderListID == -1)
		{
			renderListID = Renderer.createListID();
			lightListID = Renderer.createListID();
		}
		
		int w = world.level.width;
		int h = world.level.height;
		
		double s = renderScale = MathHelperLM.clamp(renderScale, 8D, 128D);
		
		render.x = camera.x = MathHelperLM.clamp(camera.x, 0.5D, w - 0.5D);
		render.y = camera.y = MathHelperLM.clamp(camera.y, 0.5D, h - 0.5D);
		
		double screenW = GameClient.inst.width / s;
		double screenH = GameClient.inst.height / s;
		
		render.x -= screenW / 2D;
		render.y -= screenH / 2D;
		
		if(render.x < 0.5D) render.x = 0.5D;
		if(render.y < 0.5D) render.y = 0.5D;
		
		screenW = w - 0.5D - screenW;
		screenH = h - 0.5D - screenH;
		
		if(render.x > screenW) render.x = screenW;
		if(render.y > screenH) render.y = screenH;
		
		mouse.x = (LMInput.mouseX + render.x * s) / s;
		mouse.y = (LMInput.mouseY + render.y * s) / s;
		
		GLHelper.push();
		GLHelper.translate(-render.x * s, -render.y * s);
		GLHelper.scale(s, s, 1D);
		
		if(isDirty)
		{
			isDirty = false;
			Renderer.updateList(renderListID);
			
			window.getTextureManager().bind(textureFloor);
			Renderer.rect(0D, 0D, w, h, 0D, 0D, w, h);
			
			for(Map.Entry<Pos2I, Tile> e : world.level.tiles())
			{
				window.getTextureManager().bind(e.getValue().getTexture());
				Pos2I p = e.getKey();
				Renderer.rect(p.x, p.y, 1D, 1D);
			}
			
			GLHelper.texture.disable();
			
			for(Pos2I p : world.level.spawnpoints)
			{
				Renderer.begin(GL11.GL_TRIANGLE_FAN);
				GLHelper.color.setF(1F, 1F, 1F, 1F);
				Renderer.vertex(p.x + 0.5D, p.y + 0.5D);
				GLHelper.color.setF(1F, 1F, 1F, 0F);
				double d = MathHelperLM.TWO_PI / 24D;
				for(int i = 0; i <= 24; i++)
				{
					double x = Math.cos(i * d) * 0.4D;
					double y = Math.sin(i * d) * 0.4D;
					Renderer.vertex(p.x + 0.5D + x, p.y + 0.5D + y);
				}
				Renderer.end();
			}
			
			GLHelper.texture.enable();
			GLHelper.color.setDefault();
			
			Renderer.endList();
		}
		
		Renderer.renderList(renderListID);
		
		if(world.level.ambient_light < 1F && isLightDirty)
		{
			isLightDirty = false;
			
			Renderer.updateList(lightListID);
			
			Pos2I p;
			
			for(int y = 0; y < h; y++)
				for(int x = 0; x < w; x++)
				{
					LightTile l = lightMap[x][y];
					l.c = world.level.ambient_light;
				}
			
			for(Map.Entry<Pos2I, Tile> e : world.level.tiles())
			{
				float l = e.getValue().light_value;
				if(l > 0F)
				{
					p = e.getKey();
					putLight(p.x, p.y, l, true);
				}
			}
			
			for(Pos2I p1 : world.level.spawnpoints)
				putLight(p1.x, p1.y, 3F, true);
			
			if(world.clientPlayer.getFlag(Entity.LIGHT)) putLight((int) camera.x, (int) camera.y, 5F, true);
			
			for(int y = 0; y < h; y++)
				for(int x = 0; x < w; x++)
				{
					LightTile l = lightMap[x][y];
					l.c = Math.min(1F, l.c);
				}
			
			for(int y = 0; y < h; y++)
				for(int x = 0; x < w; x++)
				{
					LightTile l = lightMap[x][y];
					
					l.nn = (l.c + getLightMapC(x - 1, y) + getLightMapC(x, y - 1) + getLightMapC(x - 1, y - 1)) / 4F;
					l.pn = (l.c + getLightMapC(x + 1, y) + getLightMapC(x, y - 1) + getLightMapC(x + 1, y - 1)) / 4F;
					l.np = (l.c + getLightMapC(x - 1, y) + getLightMapC(x, y + 1) + getLightMapC(x - 1, y + 1)) / 4F;
					l.pp = (l.c + getLightMapC(x + 1, y) + getLightMapC(x, y + 1) + getLightMapC(x + 1, y + 1)) / 4F;
				}
			
			for(int y = 0; y < h; y++)
				for(int x = 0; x < w; x++)
				{
					LightTile l = lightMap[x][y];
					l.c = 1F - l.c;
					l.nn = 1F - l.nn;
					l.pn = 1F - l.pn;
					l.np = 1F - l.np;
					l.pp = 1F - l.pp;
				}
			
			for(int y = 0; y < h; y++)
				for(int x = 0; x < w; x++)
				{
					LightTile l = lightMap[x][y];
					l.c = (l.nn + l.pn + l.np + l.pp) / 4F;
				}
			
			Renderer.begin(GL11.GL_TRIANGLES);
			
			for(int y = 0; y < h; y++)
			{
				for(int x = 0; x < w; x++)
				{
					LightTile l = lightMap[x][y];
					
					GLHelper.color.setF(0F, 0F, 0F, l.c);
					Renderer.vertex(x + 0.5D, y + 0.5D);
					
					GLHelper.color.setF(0F, 0F, 0F, l.nn);
					Renderer.vertex(x, y);
					
					GLHelper.color.setF(0F, 0F, 0F, l.pn);
					Renderer.vertex(x + 1F, y);
					
					//
					
					GLHelper.color.setF(0F, 0F, 0F, l.c);
					Renderer.vertex(x + 0.5D, y + 0.5D);
					
					GLHelper.color.setF(0F, 0F, 0F, l.pn);
					Renderer.vertex(x + 1F, y);
					
					GLHelper.color.setF(0F, 0F, 0F, l.pp);
					Renderer.vertex(x + 1F, y + 1F);
					
					//
					
					GLHelper.color.setF(0F, 0F, 0F, l.c);
					Renderer.vertex(x + 0.5D, y + 0.5D);
					
					GLHelper.color.setF(0F, 0F, 0F, l.pp);
					Renderer.vertex(x + 1F, y + 1F);
					
					GLHelper.color.setF(0F, 0F, 0F, l.np);
					Renderer.vertex(x, y + 1F);
					
					//
					
					GLHelper.color.setF(0F, 0F, 0F, l.c);
					Renderer.vertex(x + 0.5D, y + 0.5D);
					
					GLHelper.color.setF(0F, 0F, 0F, l.np);
					Renderer.vertex(x, y + 1F);
					
					GLHelper.color.setF(0F, 0F, 0F, l.nn);
					Renderer.vertex(x, y);
				}
			}
			
			Renderer.end();
			
			GLHelper.color.setDefault();
			GLHelper.endList();
		}
		
		GLHelper.color.setDefault();
		
		GLHelper.texture.enable();
		
		for(Entity e : world.entities)
			e.onRender(window);
		world.clientPlayer.onRender(window);
		
		GLHelper.texture.disable();
		if(world.level.ambient_light < 1F) Renderer.renderList(lightListID);
		
		Pos2I mouseI = mouse.toPos2I();
		GLHelper.color.setF(1F, 1F, 1F, 0.125F);
		Renderer.rect(mouseI.x, mouseI.y, 1D, 1D);
		GLHelper.texture.enable();
		
		GLHelper.color.setDefault();
		GLHelper.pop();
	}
	
	private float getLightMapC(int x, int y)
	{ return world.level.exists(x, y) ? lightMap[x][y].c : 0F; }
	
	public void markDirty()
	{
		isDirty = true;
		markLightDirty();
	}
	
	public void markLightDirty()
	{ isLightDirty = true; }
	
	private void putLight(int x, int y, float dist, boolean blocked)
	{
		if(dist <= 0F || x < 0 || y < 0 || x >= world.level.width || y >= world.level.height) return;
		
		if(lightMap[x][y].c < dist)
		{
			if(blocked)
			{
				Tile t = world.level.getTile(new Pos2I(x, y));
				if(!t.getFlag(Tile.FLAG_IS_TRANSPARENT)) return;
			}
			
			lightMap[x][y].c = dist;
			
			if(dist > 1)
			{
				float dist1 = dist - 1F;
				
				if(dist1 > 0F)
				{
					putLight(x + 1, y, dist1, blocked);
					putLight(x - 1, y, dist1, blocked);
					putLight(x, y + 1, dist1, blocked);
					putLight(x, y - 1, dist1, blocked);
				}
			}
		}
	}
	
	public Pos2D getPosOnScreen(double x, double y)
	{
		x = (x * renderScale) - render.x * renderScale;
		y = (y * renderScale) - render.y * renderScale;
		return new Pos2D(x, y);
	}
	
	public Pos2D getPosInWorld(double x, double y)
	{
		x = (x + render.x * renderScale) / renderScale;
		y = (y + render.y * renderScale) / renderScale;
		return new Pos2D(x, y);
	}
	
	private static class LightTile
	{
		public float nn;
		public float pn;
		public float np;
		public float pp;
		public float c;
	}
}
