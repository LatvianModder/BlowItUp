package latmod.blowitup.entity;

import latmod.blowitup.GameRenderer;
import latmod.core.rendering.*;
import latmod.core.res.Resource;

import java.io.*;

/**
 * Created by LatvianModder on 28.12.2015.
 */
public class EntityPlayer extends Entity
{
	public String name = null;
	public float rotation = 0F;
	public boolean flashlight = false;
	public int color = 0xFF0094FF;
	private Texture texture;

	public EntityPlayer()
	{
		super();
	}

	public final void writeToIO(DataOutput io) throws Exception
	{
		super.writeToIO(io);
		io.writeUTF(name);
		io.writeFloat(rotation);
		io.writeBoolean(flashlight);
		io.writeInt(color);
	}

	public final void readFromIO(DataInput io) throws Exception
	{
		super.readFromIO(io);
		name = io.readUTF();
		rotation = io.readFloat();
		flashlight = io.readBoolean();
		color = io.readInt();
	}

	public void onRender(GameRenderer r)
	{
		GLHelper.push();
		GLHelper.translate(pos.x, pos.y);
		GLHelper.rotateZ(rotation);
		GLHelper.texture.enable();
		if(texture == null)
			texture = r.textureManager.getTexture(Resource.getTexture("entities/player.png"));
		texture.bind();
		Renderer.rect(-0.5D, -0.5D, 1D, 1D);
		GLHelper.pop();
	}
}