package latmod.blowitup.entity;

import latmod.blowitup.world.*;
import latmod.lib.Bits;
import latmod.lib.*;
import latmod.lib.util.Pos2D;

import java.io.*;

/**
 * Created by LatvianModder on 27.12.2015.
 */
public class Entity
{
	public static final byte DEAD = 0;
	public static final byte DIRTY = 1;
	public static final byte SNEAKING = 2;
	public static final byte LIGHT = 3;
	
	public World world;
	public int worldID;
	public final Pos2D pos;
	private byte flags;
	public double radius = 0.75D;
	
	public Entity()
	{
		pos = new Pos2D();
		setFlag(DEAD, false);
	}
	
	public boolean getFlag(byte flag) { return Bits.getBit(flags, flag); }
	
	public boolean setFlag(byte flag, boolean v)
	{
		if(getFlag(flag) != v)
		{
			flags = Bits.setBit(flags, flag, v);
			return true;
		}
		return false;
	}
	
	public final void onCreated(World w, int wid)
	{
		world = w;
		worldID = wid;
		onCreated();
	}
	
	public void writeToIO(DataOutput io) throws Exception
	{
		io.writeDouble(pos.x);
		io.writeDouble(pos.y);
		io.writeByte(flags);
	}
	
	public void readFromIO(DataInput io) throws Exception
	{
		pos.x = io.readDouble();
		pos.y = io.readDouble();
		flags = io.readByte();
	}
	
	public void markDirty() { setFlag(DIRTY, true); }
	
	public void onCreated()
	{
	}
	
	public void onDeath()
	{
	}
	
	public void onUpdate()
	{
	}
	
	public void onRender()
	{
	}
	
	public boolean move(double mx, double my)
	{
		double px = pos.x;
		double py = pos.y;
		
		AABB box = createAABB();
		if(box != null)
		{
			boolean collision = false;
			AABB box1 = box.add(mx, 0D);
			
			for(AABB aabb : world.aabbs)
			{
				if(aabb.collidesWith(box1))
				{
					collision = true;
					break;
				}
			}
			
			if(collision) mx = 0D;
			
			collision = false;
			box1 = box.add(0D, my);
			
			for(AABB aabb : world.aabbs)
			{
				if(aabb.collidesWith(box1))
				{
					collision = true;
					break;
				}
			}
			
			if(collision) my = 0D;
		}
		
		//FIXME: Walls
		pos.x += mx;
		pos.y += my;
		
		pos.x = MathHelperLM.clamp(pos.x, 0.5D, world.level.width - 0.5D);
		pos.y = MathHelperLM.clamp(pos.y, 0.5D, world.level.height - 0.5D);
		
		return px != pos.x || py != pos.y;
	}
	
	public AABB createAABB()
	{
		double s = radius / 2D;
		return new AABB(pos.x - s, pos.y - s, pos.x + s, pos.y + s);
	}
}