package latmod.blowitup.client;

import latmod.core.input.LMInput;
import latmod.lib.LMColor;
import latmod.lib.util.FinalIDObject;
import org.lwjgl.glfw.GLFW;

/**
 * Created by LatvianModder on 29.12.2015.
 */
public class ClientSettings
{
	public static String username = "Player";
	public static final LMColor player_color = new LMColor.RGB(0, 0x94, 255);
	
	public static void load()
	{
		//setFile(new File(GameClient.inst.gameLocation, "settings.json"));
	}
	
	public static class KeyBinding extends FinalIDObject
	{
		public static final KeyBinding up = new KeyBinding("up", GLFW.GLFW_KEY_W);
		public static final KeyBinding down = new KeyBinding("down", GLFW.GLFW_KEY_S);
		public static final KeyBinding left = new KeyBinding("left", GLFW.GLFW_KEY_A);
		public static final KeyBinding right = new KeyBinding("right", GLFW.GLFW_KEY_D);
		public static final KeyBinding sneak = new KeyBinding("sneak", GLFW.GLFW_KEY_LEFT_SHIFT);
		public static final KeyBinding attack = new KeyBinding("attack", GLFW.GLFW_KEY_SPACE);
		public static final KeyBinding flashlight = new KeyBinding("flashlight", GLFW.GLFW_KEY_F);
		public static final KeyBinding chat = new KeyBinding("chat", GLFW.GLFW_KEY_T);
		
		public final int defKey;
		
		public KeyBinding(String id, int key)
		{
			super(id);
			defKey = key;
		}
		
		public int get()
		{
			//FIXME
			return defKey;
		}
		
		@Override
		public String toString()
		{ return GLFW.glfwGetKeyName(get(), 0); }
		
		public boolean isPressed()
		{ return LMInput.isKeyDown(get()); }
	}
}