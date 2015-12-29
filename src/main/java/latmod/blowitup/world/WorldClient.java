package latmod.blowitup.world;

import latmod.blowitup.entity.EntityPlayerSP;

/**
 * Created by LatvianModder on 29.12.2015.
 */
public class WorldClient extends World
{
	public final WorldRenderer renderer;
	public final EntityPlayerSP clientPlayer;

	public WorldClient(Level l)
	{
		super(l);
		renderer = new WorldRenderer(this);
		clientPlayer = new EntityPlayerSP();
		clientPlayer.onCreated(this, 0);
	}
}