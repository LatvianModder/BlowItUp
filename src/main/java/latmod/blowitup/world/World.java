package latmod.blowitup.world;

import latmod.blowitup.entity.*;
import latmod.blowitup.tile.Tile;
import latmod.lib.FastList;
import latmod.lib.util.Pos2I;

import java.io.*;
import java.util.Map;

/**
 * Created by LatvianModder on 27.12.2015.
 */
public class World
{
	public final Level level;
	public final FastList<Entity> entities;
	public final FastList<AABB> aabbs;

	public World(Level l)
	{
		level = Level.copyOf(l);
		entities = new FastList<>();
		aabbs = new FastList<>();
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

	public void onUpdate()
	{
		aabbs.clear();

		for(Map.Entry<Pos2I, Tile> e :  level.tiles())
		{
			if(e.getValue().flags[Tile.FLAG_CAN_COLLIDE])
			{
				Pos2I p = e.getKey();
				aabbs.add(new AABB(p.x, p.y, p.x + 1D, p.y + 1D));
			}
		}

		if(!entities.isEmpty())
		{
			for(int i = 0; i < entities.size(); i++)
			{
				AABB aabb = entities.get(i).createAABB();
				if(aabb != null) aabbs.add(aabb);
			}

			for(int i = 0; i < entities.size(); i++)
				entities.get(i).onUpdate();

			for(int i = entities.size() - 1; i >= 0; i--)
			{
				if(entities.get(i).flags[Entity.DEAD])
					entities.remove(i);
			}
		}
	}
}