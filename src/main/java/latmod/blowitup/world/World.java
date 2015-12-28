package latmod.blowitup.world;

/**
 * Created by LatvianModder on 27.12.2015.
 */
public class World
{
	public final Level level;
	public final WorldRenderer renderer;

	public World(Level l)
	{
		level = l;
		renderer = new WorldRenderer(this);
	}
}