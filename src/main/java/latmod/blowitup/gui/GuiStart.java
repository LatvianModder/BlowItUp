package latmod.blowitup.gui;

import com.latmod.latcoregl.IWindow;
import com.latmod.latcoregl.gui.Button;
import com.latmod.latcoregl.gui.Gui;
import com.latmod.latcoregl.input.EventMousePressed;

/**
 * Created by LatvianModder on 29.12.2015.
 */
public class GuiStart extends Gui
{
    public GuiStart(IWindow w)
    {
        super(w, "start");
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