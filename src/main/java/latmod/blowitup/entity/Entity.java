package latmod.blowitup.entity;

import latmod.blowitup.GameRenderer;
import latmod.blowitup.world.World;
import latmod.lib.Bits;
import latmod.lib.*;
import latmod.lib.util.Pos2D;

import java.io.*;

/**
 * Created by LatvianModder on 27.12.2015.
 */
public class Entity
{
	public static final int DEAD = 0;
	public static final int DIRTY = 1;

	public World world;
	public int worldID;
	public final Pos2D pos;
	public final boolean[] flags;

	public Entity()
	{
		pos = new Pos2D();
		flags = new boolean[8];

		flags[DEAD] = false;
	}

	public final void onCreated(World w, int wid)
	{ world = w; worldID = wid; onCreated(); }

	public void writeToIO(DataOutput io) throws Exception
	{
		io.writeDouble(pos.x);
		io.writeDouble(pos.y);
		io.writeByte(Bits.toBits(flags));
	}

	public void readFromIO(DataInput io) throws Exception
	{
		pos.x = io.readDouble();
		pos.y = io.readDouble();
		Bits.fromBits(flags, io.readByte() & 0xFF);
	}

	public void markDirty()
	{ flags[DIRTY] = true; }

	public void onCreated()
	{
	}

	public void onDeath()
	{
	}

	public void onUpdate()
	{
	}

	public void onRender(GameRenderer r)
	{
	}

	public boolean move(double mx, double my)
	{
		double px = pos.x;
		double py = pos.y;

		//FIXME: Walls
		pos.x += mx;
		pos.y += my;

		pos.x = MathHelperLM.clamp(pos.x, 0.5D, world.level.width - 0.5D);
		pos.y = MathHelperLM.clamp(pos.y, 0.5D, world.level.height - 0.5D);

		return px != pos.x || py != pos.y;
	}
}