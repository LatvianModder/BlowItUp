package latmod.blowitup.entity;

import latmod.blowitup.*;
import latmod.blowitup.tile.Tile;
import latmod.blowitup.world.*;
import latmod.core.input.LMMouse;
import latmod.core.rendering.Renderer;
import latmod.lib.util.*;
import org.lwjgl.input.Keyboard;

/**
 * Created by LatvianModder on 29.12.2015.
 */
public class EntityPlayerSP extends EntityPlayer
{
	public WorldClient clientWorld;

	public EntityPlayerSP()
	{
		super();
	}

	public void onCreated()
	{
		super.onCreated();
		clientWorld = (WorldClient)world;
	}

	public void onUpdate()
	{
		Pos2I posI1 = posI.clone();

		double mx = 0D;
		double my = 0D;
		double m = 0.06D;

		boolean keyLeft = Settings.KeyBinding.left.isPressed();
		boolean keyRight = Settings.KeyBinding.right.isPressed();

		flags[SNEAKING] = Settings.KeyBinding.sneak.isPressed();

		if(flags[SNEAKING]) m *= 0.5D;

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
		if(Settings.KeyBinding.up.isPressed())
		{
			my -= m;
			rotation = 270F;

			if(keyLeft) rotation -= 45F;
			else if(keyRight) rotation += 45F;
		}
		if(Settings.KeyBinding.down.isPressed())
		{
			my += m;
			rotation = 90F;

			if(keyLeft) rotation += 45F;
			else if(keyRight) rotation -= 45F;
		}

		if(LMMouse.dx != 0 || LMMouse.dy != 0 || LMMouse.isButtonDown(0))
		{
			Pos2D screen = clientWorld.renderer.getPosOnScreen(pos.x, pos.y);

			rotation = (float)(Math.atan2(LMMouse.y - screen.y, LMMouse.x - screen.x) * 180D / Math.PI);
		}

		if(move(mx, my))
		{
			markDirty();
		}

		clientWorld.renderer.camera.set(pos.x, pos.y);

		if(flags[LIGHT] && !posI.equalsPos(posI1))
			clientWorld.renderer.markLightDirty();
		posI.set(posI1.x, posI1.y);
	}

	public void onRender(GameRenderer r)
	{
		super.onRender(r);
	}

	public void keyPressed(int key)
	{
		if(key == Settings.KeyBinding.flashlight.get())
		{
			flags[LIGHT] = !flags[LIGHT];
			markDirty();
			clientWorld.renderer.markLightDirty();
		}

		Pos2I p = clientWorld.renderer.mouse.toPos2I();

		if(key == Keyboard.KEY_R)
			clientWorld.level.setTile(p, Tile.air);
		else if(key == Keyboard.KEY_L)
			clientWorld.level.setTile(p, clientWorld.level.getTileFromID("lamp"));
		else if(key == Keyboard.KEY_P)
			clientWorld.level.setTile(p, clientWorld.level.getTileFromID("planks"));
		clientWorld.renderer.markDirty();
	}
}