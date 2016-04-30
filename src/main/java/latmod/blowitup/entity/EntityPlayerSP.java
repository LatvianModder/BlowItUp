package latmod.blowitup.entity;

import latmod.blowitup.client.ClientSettings;
import latmod.blowitup.client.WorldClient;
import latmod.blowitup.tile.Tile;
import latmod.core.IWindow;
import latmod.core.input.LMInput;
import latmod.lib.util.Pos2D;
import latmod.lib.util.Pos2I;
import org.lwjgl.glfw.GLFW;

/**
 * Created by LatvianModder on 29.12.2015.
 */
public class EntityPlayerSP extends EntityPlayer
{
	public EntityPlayerSP()
	{
		super();
	}
	
	@Override
	public void onCreated()
	{
		super.onCreated();
	}
	
	@Override
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
		
		if(LMInput.mouseDX != 0 || LMInput.mouseDY != 0 || LMInput.isMouseDown(GLFW.GLFW_MOUSE_BUTTON_LEFT))
		{
			Pos2D screen = clientWorld.renderer.getPosOnScreen(pos.x, pos.y);
			
			rotation = (float) (Math.atan2(LMInput.mouseY - screen.y, LMInput.mouseX - screen.x) * 180D / Math.PI);
		}
		
		if(move(mx, my))
		{
			markDirty();
		}
		
		clientWorld.renderer.camera.set(pos.x, pos.y);
		
		if(getFlag(LIGHT) && !posI.equalsPos(posI1)) clientWorld.renderer.markLightDirty();
		posI.set(posI1.x, posI1.y);
	}
	
	@Override
	public void onRender(IWindow window)
	{
		super.onRender(window);
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
		
		if(key == GLFW.GLFW_KEY_R) clientWorld.level.setTile(p, Tile.air);
		else if(key == GLFW.GLFW_KEY_L) clientWorld.level.setTile(p, clientWorld.level.getTileFromID("lamp"));
		else if(key == GLFW.GLFW_KEY_P) clientWorld.level.setTile(p, clientWorld.level.getTileFromID("planks"));
		clientWorld.renderer.markDirty();
	}
}