package latmod.blowitup;

import latmod.core.rendering.TextureManager;
import latmod.core.res.ResourceManager;

/**
 * Created by LatvianModder on 29.12.2015.
 */
public class GameRenderer
{
	public final ResourceManager resourceManager;
	public final TextureManager textureManager;

	public GameRenderer(ResourceManager rm, TextureManager tm)
	{
		resourceManager = rm;
		textureManager = tm;
	}
}