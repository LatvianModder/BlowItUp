package latmod.blowitup.client;

import latmod.core.input.LMKeyboard;
import latmod.lib.LMColor;
import latmod.lib.util.FinalIDObject;
import org.lwjgl.input.Keyboard;

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
		public static final KeyBinding up = new KeyBinding("up", Keyboard.KEY_W);
		public static final KeyBinding down = new KeyBinding("down", Keyboard.KEY_S);
		public static final KeyBinding left = new KeyBinding("left", Keyboard.KEY_A);
		public static final KeyBinding right = new KeyBinding("right", Keyboard.KEY_D);
		public static final KeyBinding sneak = new KeyBinding("sneak", Keyboard.KEY_LSHIFT);
		public static final KeyBinding attack = new KeyBinding("attack", Keyboard.KEY_SPACE);
		public static final KeyBinding flashlight = new KeyBinding("flashlight", Keyboard.KEY_F);
		public static final KeyBinding chat = new KeyBinding("chat", Keyboard.KEY_Y);
		
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
		
		public String getAsString() { return Keyboard.getKeyName(get()); }
		
		public boolean isPressed() { return LMKeyboard.isKeyDown(get()); }
	}
}