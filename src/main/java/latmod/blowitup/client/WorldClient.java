package latmod.blowitup.client;

import com.latmod.lib.math.Pos2I;
import latmod.blowitup.entity.EntityPlayerSP;
import latmod.blowitup.world.Level;
import latmod.blowitup.world.World;

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

        Pos2I spawnPoint = level.getRandomSpawnpoint();
        clientPlayer.pos.set(spawnPoint.x + 0.5D, spawnPoint.y + 0.5D);
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();
        clientPlayer.onUpdate();
    }
}