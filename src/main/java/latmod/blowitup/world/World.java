package latmod.blowitup.world;

import latmod.blowitup.entity.*;
import latmod.lib.FastList;

import java.io.*;

/**
 * Created by LatvianModder on 27.12.2015.
 */
public class World
{
	public final Level level;
	public final FastList<Entity> entities;

	public World(Level l)
	{
		level = Level.copyOf(l);
		entities = new FastList<>();
	}

	public void writeToIO(DataOutput io) throws Exception
	{
		io.writeUTF(level.ID);
		io.writeShort(level.width);
		io.writeShort(level.height);
		level.writeToIO(io);

		io.writeInt(entities.size());
		for(Entity e : entities)
		{
			io.writeInt(e.worldID);
			io.writeShort(EntityRegistry.getID(e.getClass()));
			e.writeToIO(io);
		}
	}

	public static World readFromIO(DataInput io) throws Exception
	{
		String levelID = io.readUTF();
		int levelW = io.readUnsignedShort();
		int levelH = io.readUnsignedShort();
		World world = new World(new Level(levelID, levelW, levelH));
		world.level.readFromIO(io);

		int s = io.readUnsignedShort();

		for(int i = 0; i < s; i++)
		{
			int wid = io.readInt();
			int type = io.readUnsignedShort();
		}

		return world;
	}
}