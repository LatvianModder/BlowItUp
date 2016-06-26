package latmod.blowitup.world;

import com.latmod.lib.math.Pos2I;
import latmod.blowitup.entity.Entity;
import latmod.blowitup.entity.EntityRegistry;
import latmod.blowitup.tile.Tile;

import java.io.DataInput;
import java.io.DataOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by LatvianModder on 27.12.2015.
 */
public class World
{
    public final Level level;
    public final List<Entity> entities;
    public final List<AABB> aabbs;

    public World(Level l)
    {
        level = Level.copyOf(l);
        entities = new ArrayList<>();
        aabbs = new ArrayList<>();
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

    public void writeToIO(DataOutput io) throws Exception
    {
        io.writeUTF(level.getID());
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

    public void onUpdate()
    {
        aabbs.clear();

        for(Map.Entry<Pos2I, Tile> e : level.tiles())
        {
            if(e.getValue().getFlag(Tile.FLAG_CAN_COLLIDE))
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
                if(aabb != null)
                {
                    aabbs.add(aabb);
                }
            }

            for(int i = 0; i < entities.size(); i++)
            {
                entities.get(i).onUpdate();
            }

            for(int i = entities.size() - 1; i >= 0; i--)
            {
                if(entities.get(i).getFlag(Entity.DEAD))
                {
                    entities.remove(i);
                }
            }
        }
    }
}