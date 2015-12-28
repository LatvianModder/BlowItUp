package latmod.blowitup.world;

import latmod.blowitup.tile.*;
import latmod.core.input.LMMouse;
import latmod.core.rendering.*;
import latmod.core.res.Resource;
import latmod.lib.MathHelperLM;
import latmod.lib.util.Pos2I;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.*;

/**
 * Created by LatvianModder on 28.12.2015.
 */
public class WorldRenderer
{
	public final World world;
	private boolean render = true;
	private static int listID = -1;
	public final byte[][] lightMap;
	public boolean smoothLight = true;

	public WorldRenderer(World w)
	{
		world = w;
		lightMap = new byte[w.level.width][w.level.height];
	}

	public void render(TextureManager tm)
	{
		if(listID == -1) listID = Renderer.createListID();

		if(render)
		{
			render = false;
			Renderer.updateList(listID);

			int w = world.level.width;
			int h = world.level.height;

			tm.getTexture(Resource.getTexture("world/floor.png")).bind();
			Renderer.rect(0D, 0D, w, h, 0D, 0D, w, h);

			for(Map.Entry<Pos2I, Tile> e : world.level.tiles())
			{
				Tile t = e.getValue();

				if(t != Tiles.air)
				{
					Pos2I p = e.getKey();
					t.getTexture(tm, 0).bind();
					Renderer.rect(p.x, p.y, 1D, 1D);
				}
			}

			GLHelper.texture.disable();

			for(Pos2I p : world.level.spawnpoints)
			{
				Renderer.begin(GL11.GL_TRIANGLE_FAN);
				GLHelper.color.setF(1F, 1F, 1F, 1F);
				Renderer.vertex(p.x + 0.5D, p.y + 0.5D);
				GLHelper.color.setF(1F, 1F, 1F, 0F);
				for(int i = 0; i <= 60; i++)
				{
					double x = Math.cos(i * MathHelperLM.TWO_PI / 60D) * 0.4D;
					double y = Math.sin(i * MathHelperLM.TWO_PI / 60D) * 0.4D;
					Renderer.vertex(p.x + 0.5D + x, p.y + 0.5D + y);
				}
				Renderer.end();
			}

			GLHelper.push();
			if(smoothLight) GLHelper.translate(0.5D, 0.5D);

			for(int y = 0; y < h; y++) for(int x = 0; x < w; x++)
			{
				if(smoothLight)
				{
					if(x == world.level.width -1 || y == world.level.height - 1) continue;

					Renderer.beginTriangles();

					float avg = 1F - ((getLightAt(x, y) + getLightAt(x + 1, y) + getLightAt(x + 1, y + 1) + getLightAt(x, y + 1)) / 60F);

					GLHelper.color.setF(0F, 0F, 0F, avg);
					Renderer.vertex(x + 0.5D, y + 0.5D);
					setLight(x, y);
					setLight(x + 1, y);

					//

					GLHelper.color.setF(0F, 0F, 0F, avg);
					Renderer.vertex(x + 0.5D, y + 0.5D);
					setLight(x + 1, y);
					setLight(x + 1, y + 1);

					//

					GLHelper.color.setF(0F, 0F, 0F, avg);
					Renderer.vertex(x + 0.5D, y + 0.5D);
					setLight(x + 1, y + 1);
					setLight(x, y + 1);

					//

					GLHelper.color.setF(0F, 0F, 0F, avg);
					Renderer.vertex(x + 0.5D, y + 0.5D);
					setLight(x, y + 1);
					setLight(x, y);

					Renderer.end();
				}
				else
				{
					GLHelper.color.setF(0F, 0F, 0F, 1F - (getLightAt(x, y) / 15F));
					Renderer.rect(x, y, 1D, 1D);
				}
			}

			GLHelper.pop();

			GLHelper.texture.enable();
			GLHelper.color.setDefault();

			Renderer.endList();
		}

		Renderer.renderList(listID);
	}

	public void markDirty()
	{ render = true; }

	public void updateLight()
	{
		byte i0 = (byte)0;
		for(int i = 0; i < world.level.width; i++)
			Arrays.fill(lightMap[i], i0);

		Pos2I p;

		for(Map.Entry<Pos2I, Tile> e : world.level.tiles())
		{
			int l = e.getValue().getLightValue();
			if(l > 0)
			{
				l = Math.min(15, l);
				p = e.getKey();
				putLight(p.x, p.y, l, l);
			}
		}

		for(Pos2I p1 : world.level.spawnpoints)
			putLight(p1.x, p1.y, 10, 10);

		if(Mouse.isButtonDown(1))
		{
			p = new Pos2I(LMMouse.x, LMMouse.y);
			p.x = (p.x -= 30) / 32;
			p.y = (p.y -= 30) / 32;
			putLight(p.x, p.y, 15, 15);
		}

		markDirty();
	}

	private void putLight(int x, int y, int dist, int max)
	{
		if(dist <= 0 || x < 0 || y < 0 || x >= world.level.width || y >= world.level.height) return;

		Tile t = world.level.getTile(new Pos2I(x, y));
		if(!t.isTransparent()) return;

		if(lightMap[x][y] < dist)
		{
			lightMap[x][y] = (byte)dist;

			if(dist > 1)
			{
				int dist1 = dist - 1;
				putLight(x + 1, y, dist1, max);
				putLight(x - 1, y, dist1, max);
				putLight(x, y + 1, dist1, max);
				putLight(x, y - 1, dist1, max);
			}
		}
	}

	private void setLight(int x, int y)
	{
		GLHelper.color.setF(0F, 0F, 0F, 1F - (getLightAt(x, y) / 15F));
		Renderer.vertex(x, y);
	}

	public byte getLightAt(int x, int y)
	{
		if(x < 0 || y < 0 || x >= world.level.width || y >= world.level.height) return 0;
		return lightMap[x][y];
	}
}
