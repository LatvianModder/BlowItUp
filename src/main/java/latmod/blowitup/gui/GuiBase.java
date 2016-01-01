package latmod.blowitup.gui;

import latmod.blowitup.Main;
import latmod.core.gui.Gui;

/**
 * Created by LatvianModder on 29.12.2015.
 */
public abstract class GuiBase extends Gui
{
	public GuiBase(String s)
	{ super(Main.inst, s); }
}