package latmod.blowitup.world;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.latmod.lib.FinalIDObject;
import com.latmod.lib.PixelBuffer;
import com.latmod.lib.io.ByteIOStream;
import com.latmod.lib.math.MathHelperLM;
import com.latmod.lib.math.Pos2I;
import latmod.blowitup.tile.Tile;

import javax.imageio.ImageIO;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by LatvianModder on 27.12.2015.
 */
public class Level extends FinalIDObject
{
    public final int width;
    public final int height;
    public final Map<Byte, Tile> tileRegistry;
    public final List<Pos2I> spawnpoints;
    private final Map<Pos2I, Tile> tiles;
    public float ambient_light;

    public Level(String id, int w, int h)
    {
        super(id);
        width = w;
        height = h;
        tileRegistry = new HashMap<>();
        spawnpoints = new ArrayList<>();
        tiles = new HashMap<>();
        ambient_light = 0F;
    }

    public static Level copyOf(Level l)
    {
        if(l == null)
        {
            return null;
        }
        Level l1 = new Level(l.getID(), l.width, l.height);

        try
        {
            ByteIOStream io = new ByteIOStream();
            l.writeToIO(io);
            io.flip();
            l1.readFromIO(io);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return l1;
    }

    public static Level loadFromJson(File f, JsonObject o) throws Exception
    {
        try
        {
            String id = o.get("id").getAsString();
            String image = o.get("image").getAsString();
            PixelBuffer buffer = new PixelBuffer(ImageIO.read(new File(f, image)));
            Level level = new Level(id, buffer.width, buffer.height);

            for(int i = 0; i < buffer.pixels.length; i++)
            {
                buffer.pixels[i] = 0xFF000000 | buffer.pixels[i];
            }

            if(o.has("ambient_light"))
            {
                level.ambient_light = o.get("ambient_light").getAsFloat();
            }

            int spawnpointsColor = 0xFF000000 | Integer.decode(o.get("spawnpoints").getAsString()).intValue();

            JsonObject o1 = o.get("tiles").getAsJsonObject();

            byte nextTileID = 0;
            for(Map.Entry<String, JsonElement> e : o1.entrySet())
            {
                JsonObject o2 = e.getValue().getAsJsonObject();
                Tile tile = new Tile(e.getKey(), Integer.decode(o2.get("color").getAsString()).intValue(), ++nextTileID);
                tile.loadFromJson(o2);
                level.tileRegistry.put(tile.byteID, tile);
            }

            Map<Integer, Tile> colorMap = new HashMap<>();
            for(Tile t : level.tileRegistry.values())
            {
                colorMap.put(t.color, t);
            }

            for(int y = 0; y < level.height; y++)
            {
                for(int x = 0; x < level.width; x++)
                {
                    int pixel = 0xFF000000 | buffer.pixels[x + y * level.width];

                    if(pixel == spawnpointsColor)
                    {
                        level.spawnpoints.add(new Pos2I(x, y));
                    }
                    else
                    {
                        Tile t = colorMap.get(pixel);
                        if(t != null)
                        {
                            level.setTile(new Pos2I(x, y), t);
                        }
                    }
                }
            }

            return level;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        return null;
    }

    public boolean exists(int x, int y)
    {
        return x >= 0 && y >= 0 && x < width && y < height;
    }

    public boolean exists(Pos2I pos)
    {
        return pos != null && exists(pos.x, pos.y);
    }

    public Pos2I getRandomSpawnpoint()
    {
        return spawnpoints.get(MathHelperLM.RAND.nextInt(spawnpoints.size()));
    }

    public void writeToIO(DataOutput io) throws Exception
    {
        io.writeFloat(ambient_light);
        io.writeByte(spawnpoints.size());
        for(int i = 0; i < spawnpoints.size(); i++)
        {
            Pos2I p = spawnpoints.get(i);
            io.writeShort(p.x);
            io.writeShort(p.y);
        }

        io.writeByte(tileRegistry.size());
        for(Tile t : tileRegistry.values())
        {
            t.writeToIO(io);
        }

        io.writeInt(tiles.size());
        for(Map.Entry<Pos2I, Tile> t : tiles.entrySet())
        {
            Pos2I p = t.getKey();
            io.writeShort(p.x);
            io.writeShort(p.y);
            io.writeByte(t.getValue().byteID);
        }
    }

    public void readFromIO(DataInput io) throws Exception
    {
        ambient_light = io.readFloat();
        spawnpoints.clear();
        int s = io.readUnsignedByte();
        for(int i = 0; i < s; i++)
        {
            int x = io.readUnsignedShort();
            int y = io.readUnsignedShort();
            spawnpoints.add(new Pos2I(x, y));
        }

        tileRegistry.clear();
        s = io.readUnsignedByte();
        for(int i = 0; i < s; i++)
        {
            Tile t = Tile.readFromIO(io);
            tileRegistry.put(t.byteID, t);
        }

        tiles.clear();
        s = io.readInt();
        for(int i = 0; i < s; i++)
        {
            int x = io.readUnsignedShort();
            int y = io.readUnsignedShort();
            byte id = io.readByte();
            Tile t = tileRegistry.get(id);
            if(t != null)
            {
                tiles.put(new Pos2I(x, y), t);
            }
        }
    }

    public Tile getTile(Pos2I pos)
    {
        if(!exists(pos))
        {
            return Tile.air;
        }
        Tile t = tiles.get(pos);
        return (t == null) ? Tile.air : t;
    }

    public List<Pos2I> getTilePositions(Tile t)
    {
        List<Pos2I> l = new ArrayList<>();
        for(Map.Entry<Pos2I, Tile> e : tiles.entrySet())
        {
            if(e.getValue() == t)
            {
                l.add(e.getKey());
            }
        }
        return l;
    }

    public Tile setTile(Pos2I pos, Tile t)
    {
        if(!exists(pos))
        {
            return Tile.air;
        }

        if(t == null || t == Tile.air)
        {
            return tiles.remove(pos);
        }

        return tiles.put(pos.copy(), t);
    }

    public Set<Map.Entry<Pos2I, Tile>> tiles()
    {
        return tiles.entrySet();
    }

    public Set<Pos2I> tilePositions()
    {
        return tiles.keySet();
    }

    public Tile getTileFromID(String id)
    {
        for(Tile t : tileRegistry.values())
        {
            if(t.equals(id))
            {
                return t;
            }
        }
        return Tile.air;
    }
}