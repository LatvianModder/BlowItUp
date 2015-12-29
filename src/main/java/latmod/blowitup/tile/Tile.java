package latmod.blowitup.tile;

import latmod.blowitup.GameRenderer;
import latmod.core.rendering.Texture;
import latmod.core.res.Resource;
import latmod.lib.Bits;
import latmod.lib.util.FinalIDObject;

import java.io.*;

/**
 * Created by LatvianModder on 27.12.2015.
 */
public class Tile extends FinalIDObject
{
	public static final TileAir air = new TileAir();

	public static final int FLAG_CAN_COLLIDE = 0;
	public static final int FLAG_CAN_EXPLODE = 1;
	public static final int FLAG_IS_TRANSPARENT = 2;
	private static final int FLAG_HAS_TEXTURE_NAME = 3;
	private static final int FLAG_IS_LIT = 4;


	public final int color;
	public final byte byteID;
	public final boolean[] flags;

	public String texture_name = null;
	private Texture texture = null;
	public float light_value = 0F;

	public Tile(String id, int col, byte i)
	{
		super(id);
		color = 0xFF000000 | col;
		byteID = i;
		flags = new boolean[8];
		flags[FLAG_CAN_COLLIDE] = true;
		flags[FLAG_CAN_EXPLODE] = false;
		flags[FLAG_IS_TRANSPARENT] = false;
	}

	public void writeToIO(DataOutput io) throws Exception
	{
		flags[FLAG_IS_LIT] = texture_name != null;
		flags[FLAG_IS_LIT] = light_value > 0F;

		io.writeUTF(ID);
		io.writeInt(color);
		io.writeByte(byteID);
		io.writeByte(Bits.toBits(flags));
		if(flags[FLAG_HAS_TEXTURE_NAME]) io.writeUTF(texture_name);
		if(flags[FLAG_IS_LIT]) io.writeFloat(light_value);
	}

	public static Tile readFromIO(DataInput io) throws Exception
	{
		String id = io.readUTF();
		int col = io.readInt();
		byte intID = io.readByte();
		Tile tile = new Tile(id, col, intID);
		Bits.fromBits(tile.flags, io.readByte() & 0xFF);
		if(tile.flags[FLAG_HAS_TEXTURE_NAME]) tile.texture_name = io.readUTF();
		if(tile.flags[FLAG_IS_LIT]) tile.light_value = io.readFloat();
		return tile;
	}

	public Texture getTexture(GameRenderer r)
	{
		if(texture == null)
		{
			String s = (texture_name != null) ? texture_name : "tiles/" + ID + ".png";
			texture = r.textureManager.getTexture(Resource.getTexture(s));
		}
		return texture;
	}
}