package latmod.blowitup.world;

/**
 * Created by LatvianModder on 27.12.2015.
 */
public enum Item
{
    GRANADE("granade", 4, false),
    SMOKE_BOMB("smoke_bomb", 4, false),
    FOG("fog", 5000, true),
    INVISIBILITY("invisibility", 10000, true),;
    public final String ID;
    public final int duration;
    public final boolean timed;

    Item(String id, int d, boolean t)
    {
        ID = id;
        duration = d;
        timed = t;
    }
}