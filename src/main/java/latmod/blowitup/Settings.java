package latmod.blowitup;

import latmod.lib.config.*;

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
			configFile = new ConfigFile(new ConfigGroup("settings").addAll(Settings.class), new File(Main.inst.gameLocation, "settings.json"));
		configFile.load();
	}
}