package latmod.blowitup.entity;

import latmod.blowitup.client.GameClient;
import latmod.core.Resource;
import latmod.core.rendering.*;
import latmod.lib.util.Pos2I;

import java.io.*;

/**
 * Created by LatvianModder on 28.12.2015.
 */
public class EntityPlayer extends Entity
{
	public String name = null;
	public float rotation = 0F;
	public int color = 0xFF0094FF;
	private Texture texture, textureMask;
	public final Pos2I posI;
	
	public EntityPlayer()
	{
		super();
		setFlag(LIGHT, true);
		radius = 0.5D;
		posI = new Pos2I();
	}
	
	public final void writeToIO(DataOutput io) throws Exception
	{
		super.writeToIO(io);
		io.writeUTF(name);
		io.writeFloat(rotation);
		io.writeInt(color);
	}
	
	public final void readFromIO(DataInput io) throws Exception
	{
		super.readFromIO(io);
		name = io.readUTF();
		rotation = io.readFloat();
		color = io.readInt();
	}
	
	public void onRender()
	{
		GLHelper.push();
		GLHelper.translate(pos.x, pos.y);
		GLHelper.rotateZ(rotation);
		GLHelper.texture.enable();
		
		if(texture == null)
		{
			texture = GameClient.inst.getTextureManager().getTexture(Resource.getTexture("entities/player.png"));
			textureMask = GameClient.inst.getTextureManager().getTexture(Resource.getTexture("entities/player_mask.png"));
		}
		
		GLHelper.color.setI(color, 255);
		textureMask.bind();
		Renderer.rect(-0.5D, -0.5D, 1D, 1D);
		GLHelper.color.setDefault();
		texture.bind();
		Renderer.rect(-0.5D, -0.5D, 1D, 1D);
		GLHelper.pop();
	}
}