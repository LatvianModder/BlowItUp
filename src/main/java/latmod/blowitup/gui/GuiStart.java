package latmod.blowitup.gui;

import latmod.core.gui.Button;
import latmod.core.input.mouse.EventMousePressed;

/**
 * Created by LatvianModder on 29.12.2015.
 */
public class GuiStart extends GuiBase
{
	public GuiStart()
	{
		super("start");
	}

	public void loadWidgets()
	{
		addWidget(new Button(this, 0, 0, 100, 20, "Play")
		{
			public void onPressed(EventMousePressed e)
			{
			}
		});

		addWidget(new Button(this, 0, 100, 100, 20, "Exit")
		{
			public void onPressed(EventMousePressed e)
			{
			}
		});
	}
}