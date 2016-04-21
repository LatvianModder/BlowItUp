package latmod.blowitup.entity;

import latmod.blowitup.client.*;
import latmod.blowitup.tile.Tile;
import latmod.core.input.LMMouse;
import latmod.lib.util.*;
import org.lwjgl.input.Keyboard;

/**
 * Created by LatvianModder on 29.12.2015.
 */
public class EntityPlayerSP extends EntityPlayer
{
	public EntityPlayerSP()
	{
		super();
	}
	
	public void onCreated()
	{
		super.onCreated();
	}
	
	public void onUpdate()
	{
		WorldClient clientWorld = (WorldClient) world;
		
		Pos2I posI1 = posI.copy();
		
		double mx = 0D;
		double my = 0D;
		double m = 0.06D;
		
		boolean keyLeft = ClientSettings.KeyBinding.left.isPressed();
		boolean keyRight = ClientSettings.KeyBinding.right.isPressed();
		
		setFlag(SNEAKING, ClientSettings.KeyBinding.sneak.isPressed());
		
		if(getFlag(SNEAKING)) m *= 0.5D;
		
		if(keyLeft)
		{
			mx -= m;
			rotation = 180F;
		}
		if(keyRight)
		{
			mx += m;
			rotation = 0F;
		}
		if(ClientSettings.KeyBinding.up.isPressed())
		{
			my -= m;
			rotation = 270F;
			
			if(keyLeft) rotation -= 45F;
			else if(keyRight) rotation += 45F;
		}
		if(ClientSettings.KeyBinding.down.isPressed())
		{
			my += m;
			rotation = 90F;
			
			if(keyLeft) rotation += 45F;
			else if(keyRight) rotation -= 45F;
		}
		
		if(LMMouse.dx != 0 || LMMouse.dy != 0 || LMMouse.isButtonDown(0))
		{
			Pos2D screen = clientWorld.renderer.getPosOnScreen(pos.x, pos.y);
			
			rotation = (float) (Math.atan2(LMMouse.y - screen.y, LMMouse.x - screen.x) * 180D / Math.PI);
		}
		
		if(move(mx, my))
		{
			markDirty();
		}
		
		clientWorld.renderer.camera.set(pos.x, pos.y);
		
		if(getFlag(LIGHT) && !posI.equalsPos(posI1)) clientWorld.renderer.markLightDirty();
		posI.set(posI1.x, posI1.y);
	}
	
	public void onRender()
	{
		super.onRender();
	}
	
	public void keyPressed(int key)
	{
		WorldClient clientWorld = (WorldClient) world;
		
		if(key == ClientSettings.KeyBinding.flashlight.get())
		{
			setFlag(LIGHT, !getFlag(LIGHT));
			markDirty();
			clientWorld.renderer.markLightDirty();
		}
		
		Pos2I p = clientWorld.renderer.mouse.toPos2I();
		
		if(key == Keyboard.KEY_R) clientWorld.level.setTile(p, Tile.air);
		else if(key == Keyboard.KEY_L) clientWorld.level.setTile(p, clientWorld.level.getTileFromID("lamp"));
		else if(key == Keyboard.KEY_P) clientWorld.level.setTile(p, clientWorld.level.getTileFromID("planks"));
		clientWorld.renderer.markDirty();
	}
}