package latmod.blowitup.world;

import com.google.gson.*;
import latmod.blowitup.Main;
import latmod.blowitup.tile.Tile;
import latmod.lib.*;
import latmod.lib.util.*;

import java.io.File;

/**
 * Created by LatvianModder on 27.12.2015.
 */
public class Level extends FinalIDObject
{
	public static final FastList<Level> levels = new FastList<>();

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
						levels.add(new Level(o));
					}
				}
				catch(Exception e)
				{ e.printStackTrace(); }
			}
		}
	}

	public final int width;
	public final int height;
	public final FastMap<Pos2I, Tile> tiles;

	public Level(String id, int w, int h)
	{
		super(id);
		width = w;
		height = h;
		tiles = new FastMap<>();
	}

	public Level(JsonObject o)
	{
		this(o.get("id").getAsString(), o.get("width").getAsInt(), o.get("height").getAsInt());
	}

	public void writeToIO(ByteIOStream io)
	{
	}

	public void readFromIO(ByteIOStream io)
	{
	}
}