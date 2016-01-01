package latmod.blowitup;

import latmod.core.input.LMKeyboard;
import latmod.lib.config.*;
import latmod.lib.util.IntBounds;
import org.lwjgl.input.Keyboard;

import java.io.File;

/**
 * Created by LatvianModder on 29.12.2015.
 */
public class Settings
{
	public static final ConfigEntryString username = new ConfigEntryString("username", "Player");
	public static final ConfigEntryColor player_color = new ConfigEntryColor("player_color", 0xFF0094FF, false);

	private static ConfigFile configFile = null;

	public static void load()
	{
		if(configFile == null)
		{
			ConfigGroup g = new ConfigGroup("settings");
			configFile = new ConfigFile(g, new File(Main.inst.gameLocation, "settings.json"));
			g.addAll(Settings.class, null, false);
			g.add(new ConfigGroup("keys").addAll(KeyBinding.class, null, false), false);
		}
		configFile.load();
	}

	public static class KeyBinding extends ConfigEntryInt
	{
		public static final KeyBinding up = new KeyBinding("up", Keyboard.KEY_W);
		public static final KeyBinding down = new KeyBinding("down", Keyboard.KEY_S);
		public static final KeyBinding left = new KeyBinding("left", Keyboard.KEY_A);
		public static final KeyBinding right = new KeyBinding("right", Keyboard.KEY_D);
		public static final KeyBinding sneak = new KeyBinding("sneak", Keyboard.KEY_LSHIFT);
		public static final KeyBinding attack = new KeyBinding("attack", Keyboard.KEY_SPACE);
		public static final KeyBinding flashlight = new KeyBinding("flashlight", Keyboard.KEY_F);
		public static final KeyBinding chat = new KeyBinding("chat", Keyboard.KEY_Y);

		public KeyBinding(String id, int key)
		{ super(id, new IntBounds(key, 0, Keyboard.KEYBOARD_SIZE - 1)); }

		public String getAsString()
		{ return Keyboard.getKeyName(get()); }

		public boolean isPressed()
		{ return LMKeyboard.isKeyDown(get()); }
	}
}