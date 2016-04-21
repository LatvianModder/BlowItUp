package latmod.blowitup.tile;

/**
 * Created by LatvianModder on 27.12.2015.
 */
public class TileAir extends Tile
{
	public TileAir()
	{
		super("air", 0xFF000000, (byte) 0);
		texture_name = null;
		light_value = 0;
		setFlag(FLAG_CAN_COLLIDE, false);
		setFlag(FLAG_CAN_EXPLODE, false);
		setFlag(FLAG_IS_TRANSPARENT, true);
	}
}
