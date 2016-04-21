package latmod.blowitup.world;

/**
 * Created by LatvianModder on 29.12.2015.
 */
public class AABB
{
	public final double posX0, posY0, posX1, posY1;
	
	public AABB(double x0, double y0, double x1, double y1)
	{
		posX0 = x0;
		posY0 = y0;
		posX1 = x1;
		posY1 = y1;
	}
	
	public boolean collidesWith(AABB aabb)
	{
		if(posX0 > aabb.posX1) return false;
		if(posY0 > aabb.posY1) return false;
		if(posX1 < aabb.posX0) return false;
		return posY1 >= aabb.posY0;
	}
	
	public AABB copy()
	{ return new AABB(posX0, posY0, posX1, posY1); }
	
	public AABB add(double x, double y)
	{ return new AABB(posX0 + x, posY0 + y, posX1 + x, posY1 + y); }
}