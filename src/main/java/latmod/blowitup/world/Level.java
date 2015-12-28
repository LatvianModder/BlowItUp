package latmod.blowitup.world;

import com.google.gson.*;
import latmod.blowitup.Main;
import latmod.blowitup.tile.*;
import latmod.lib.*;
import latmod.lib.util.*;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.*;

/**
 * Created by LatvianModder on 27.12.2015.
 */
public class Level extends FinalIDObject
{
	public static final FastMap<String, Level> levels = new FastMap<>();

	public static void load()
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
						if(o != null)
						{
							Level l = new Level(f, o);
							levels.put(l.ID, l);
						}
						else Main.logger.info("" + o);
					}
				}
				catch(Exception e)
				{ e.printStackTrace(); }
			}
		}
	}

	public final int width;
	public final int height;
	public final FastList<Pos2I> spawnpoints;
	private final FastMap<Pos2I, Tile> tiles;

	public Level(File f, JsonObject o) throws Exception
	{
		super(o.get("id").getAsString());
		spawnpoints = new FastList<>();
		tiles = new FastMap<>();

		String image = o.get("image").getAsString();
		PixelBuffer buffer = new PixelBuffer(ImageIO.read(new File(f, image)));

		width = buffer.width;
		height = buffer.height;

		for(int i = 0; i < buffer.pixels.length; i++)
			buffer.pixels[i] = 0xFF000000 | buffer.pixels[i];

		int spawnpointsColor = 0xFF000000 | Integer.decode(o.get("spawnpoints").getAsString()).intValue();

		JsonObject o1 = o.get("tiles").getAsJsonObject();
		FastMap<Integer, Tile> tilesMap = new FastMap<>();

		for(Map.Entry<String, JsonElement> e :  o1.entrySet())
			tilesMap.put(0xFF000000 | Integer.decode(e.getKey()).intValue(), Tiles.get(e.getValue().getAsString()));

		for(int y = 0; y < height; y++)
		for(int x = 0; x < width; x++)
		{
			int pixel = buffer.pixels[x + y * width];

			if(pixel == spawnpointsColor)
				spawnpoints.add(new Pos2I(x, y));
			else
			{
				Tile t = tilesMap.get(Integer.valueOf(0xFF000000 | pixel));
				if(t != Tiles.air) tiles.put(new Pos2I(x, y), t);
			}
		}
	}

	public Tile getTile(Pos2I pos)
	{
		Tile t = tiles.get(pos);
		return (t == null) ? Tiles.air : t;
	}

	public List<Pos2I> getTilePositions(Tile t)
	{
		FastList<Pos2I> l = new FastList<>();
		for(Map.Entry<Pos2I, Tile> e : tiles.entrySet())
		{ if(e.getValue() == t) l.add(e.getKey()); }
		return l;
	}

	public Tile setTile(Pos2I p, Tile t)
	{
		if(p.x < 0 || p.y < 0 || p.x >= width || p.y >= height)
			return Tiles.air;

		if(t == null || t == Tiles.air)
			return tiles.remove(p);

		return tiles.put(p.clone(), t);
	}

	public Set<Map.Entry<Pos2I, Tile>> tiles()
	{ return tiles.entrySet(); }

	public Set<Pos2I> tilePositions()
	{ return tiles.keySet(); }
}