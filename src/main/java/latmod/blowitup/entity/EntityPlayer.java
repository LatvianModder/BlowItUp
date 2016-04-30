package latmod.blowitup.entity;

import latmod.core.IWindow;
import latmod.core.Resource;
import latmod.core.rendering.GLHelper;
import latmod.core.rendering.Renderer;
import latmod.core.rendering.Texture;
import latmod.lib.util.Pos2I;

import java.io.DataInput;
import java.io.DataOutput;

/**
 * Created by LatvianModder on 28.12.2015.
 */
public class EntityPlayer extends Entity
{
	public static final Texture texturePlayer = new Texture(new Resource("blowitup", "textures/entities/player.png"));
	public static final Texture texturePlayerMask = new Texture(new Resource("blowitup", "textures/entities/player_mask.png"));
	
	public String name = null;
	public float rotation = 0F;
	public int color = 0xFF0094FF;
	public final Pos2I posI;
	
	public EntityPlayer()
	{
		super();
		setFlag(LIGHT, true);
		radius = 0.5D;
		posI = new Pos2I();
	}
	
	@Override
	public final void writeToIO(DataOutput io) throws Exception
	{
		super.writeToIO(io);
		io.writeUTF(name);
		io.writeFloat(rotation);
		io.writeInt(color);
	}
	
	@Override
	public final void readFromIO(DataInput io) throws Exception
	{
		super.readFromIO(io);
		name = io.readUTF();
		rotation = io.readFloat();
		color = io.readInt();
	}
	
	@Override
	public void onRender(IWindow window)
	{
		GLHelper.push();
		GLHelper.translate(pos.x, pos.y);
		GLHelper.rotateZ(rotation);
		GLHelper.texture.enable();
		
		GLHelper.color.setI(color, 255);
		window.getTextureManager().bind(texturePlayer);
		Renderer.rect(-0.5D, -0.5D, 1D, 1D);
		GLHelper.color.setDefault();
		window.getTextureManager().bind(texturePlayerMask);
		Renderer.rect(-0.5D, -0.5D, 1D, 1D);
		GLHelper.pop();
	}
}