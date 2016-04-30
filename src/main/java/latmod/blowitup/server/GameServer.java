package latmod.blowitup.server;

import java.util.logging.Logger;

/**
 * Created by LatvianModder on 12.03.2016.
 */
public class GameServer implements Runnable
{
	private static GameServer instance;
	
	public static GameServer instance()
	{ return instance; }
	
	public static void start()
	{ instance = new GameServer(); }
	
	public static void main(String[] args) throws Exception
	{ start(); }
	
	// End of static //
	
	public final Logger logger;
	public WorldServer world;
	private Thread thread;
	
	private GameServer()
	{
		logger = Logger.getLogger("GameClient");
		logger.info("Created");
		
		thread = new Thread(this, "GameServer");
		thread.start();
	}
	
	public void stop()
	{
		instance = null;
	}
	
	@Override
	public void run()
	{
		/*Level level = Level.loadFromJson(new File(""));
		
		world = new WorldServer(level);
		
		logger.info("Stared");
		
		while(Thread.currentThread() == thread)
		{
		}
		
		stop();
		*/
	}
}