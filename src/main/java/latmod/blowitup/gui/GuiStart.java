package latmod.blowitup.gui;

import latmod.core.gui.Button;
import latmod.core.gui.Gui;
import latmod.core.input.EventMousePressed;

/**
 * Created by LatvianModder on 29.12.2015.
 */
public class GuiStart extends Gui
{
	public GuiStart()
	{
		super("start", null);
	}
	
	@Override
	public void addWidgets()
	{
		mainPanel.add(new Button("play", 0, 0, 100, 20, "Play")
		{
			@Override
			public void onPressed(EventMousePressed e)
			{
			}
		});
		
		mainPanel.add(new Button("exit", 0, 100, 100, 20, "Exit")
		{
			@Override
			public void onPressed(EventMousePressed e)
			{
			}
		});
	}
}