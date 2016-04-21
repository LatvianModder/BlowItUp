package latmod.blowitup.tile;

import com.google.gson.JsonObject;
import latmod.blowitup.client.GameClient;
import latmod.core.Resource;
import latmod.core.rendering.Texture;
import latmod.lib.Bits;
import latmod.lib.util.FinalIDObject;

import java.io.*;

/**
 * Created by LatvianModder on 27.12.2015.
 */
public class Tile extends FinalIDObject
{
	public static final TileAir air = new TileAir();
	
	public static final byte FLAG_CAN_COLLIDE = 0;
	public static final byte FLAG_CAN_EXPLODE = 1;
	public static final byte FLAG_IS_TRANSPARENT = 2;
	private static final byte FLAG_HAS_TEXTURE_NAME = 3;
	private static final byte FLAG_IS_LIT = 4;
	private static final byte FLAG_HAS_DATA = 5;
	
	public final int color;
	public final byte byteID;
	private byte flags = 0;
	
	public String texture_name = null;
	private Texture texture = null;
	public float light_value = 0F;
	public JsonObject customData = null;
	
	public Tile(String id, int col, byte i)
	{
		super(id);
		color = 0xFF000000 | col;
		byteID = i;
		setFlag(FLAG_CAN_COLLIDE, true);
		setFlag(FLAG_CAN_EXPLODE, false);
		setFlag(FLAG_IS_TRANSPARENT, false);
	}
	
	public boolean getFlag(byte flag)
	{ return Bits.getBit(flags, flag); }
	
	public boolean setFlag(byte flag, boolean v)
	{
		if(getFlag(flag) != v)
		{
			flags = Bits.setBit(flags, flag, v);
			return true;
		}
		return false;
	}
	
	public void writeToIO(DataOutput io) throws Exception
	{
		setFlag(FLAG_HAS_TEXTURE_NAME, texture_name != null);
		setFlag(FLAG_IS_LIT, light_value > 0F);
		
		io.writeUTF(getID());
		io.writeInt(color);
		io.writeByte(byteID);
		io.writeByte(flags);
		if(getFlag(FLAG_HAS_TEXTURE_NAME)) io.writeUTF(texture_name);
		if(getFlag(FLAG_IS_LIT)) io.writeFloat(light_value);
	}
	
	public static Tile readFromIO(DataInput io) throws Exception
	{
		String id = io.readUTF();
		int col = io.readInt();
		byte intID = io.readByte();
		Tile tile = new Tile(id, col, intID);
		tile.flags = io.readByte();
		if(tile.getFlag(FLAG_HAS_TEXTURE_NAME)) tile.texture_name = io.readUTF();
		if(tile.getFlag(FLAG_IS_LIT)) tile.light_value = io.readFloat();
		return tile;
	}
	
	public Texture getTexture()
	{
		if(texture == null)
		{
			String s = (texture_name != null) ? texture_name : "tiles/" + getID() + ".png";
			texture = GameClient.inst.getTextureManager().getTexture(Resource.getTexture(s));
		}
		return texture;
	}
	
	public void loadFromJson(JsonObject o)
	{
		if(o.has("texture_name")) texture_name = o.get("texture_name").getAsString();
		if(o.has("light_value")) light_value = o.get("light_value").getAsFloat();
		if(light_value > 0F) setFlag(Tile.FLAG_IS_TRANSPARENT, true);
		
		if(o.has("transparent")) setFlag(Tile.FLAG_IS_TRANSPARENT, o.get("transparent").getAsBoolean());
		if(o.has("can_collide")) setFlag(Tile.FLAG_CAN_COLLIDE, o.get("can_collide").getAsBoolean());
		if(o.has("can_explode")) setFlag(Tile.FLAG_CAN_EXPLODE, o.get("can_explode").getAsBoolean());
	}
}