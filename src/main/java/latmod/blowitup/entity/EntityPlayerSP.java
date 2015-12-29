package latmod.blowitup.entity;

import latmod.blowitup.Main;
import latmod.core.input.*;
import latmod.lib.util.Pos2D;
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

	public void onUpdate()
	{
		double mx = 0D;
		double my = 0D;
		double m = 0.1D;
		if(LMKeyboard.isCtrlDown()) m = 0.25D;
		else if(LMKeyboard.isShiftDown()) m = 0.03D;

		boolean keyLeft = LMKeyboard.isKeyDown(Keyboard.KEY_A);
		boolean keyDown = LMKeyboard.isKeyDown(Keyboard.KEY_S);
		boolean keyRight = LMKeyboard.isKeyDown(Keyboard.KEY_D);
		boolean keyUp = LMKeyboard.isKeyDown(Keyboard.KEY_W);

		if(keyLeft)
		{
			mx -= m;
			rotation = 270F;
		}
		if(keyRight)
		{
			mx += m;
			rotation = 90F;
		}
		if(keyUp)
		{
			my -= m;
			rotation = 0F;

			if(keyLeft) rotation -= 45F;
			else if(keyRight) rotation += 45F;
		}
		if(keyDown)
		{
			my += m;
			rotation = 180F;

			if(keyLeft) rotation += 45F;
			else if(keyRight) rotation -= 45F;
		}

		if(LMMouse.dx != 0 || LMMouse.dy != 0 || LMMouse.isButtonDown(0))
		{
			Pos2D screen = Main.inst.clientWorld.renderer.getPosOnScreen(pos.x, pos.y);

			rotation = (float)(Math.atan2(LMMouse.y - screen.y, LMMouse.x - screen.x) * 180D / Math.PI) + 90F;
		}

		if(move(mx, my))
		{
			Main.inst.clientWorld.renderer.camera.set(pos.x, pos.y);
			Main.inst.clientWorld.renderer.markLightDirty();
		}
	}
}