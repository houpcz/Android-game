package cz.ic.resurrection.heroic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

public abstract class GameObject {
	Drawable imageActive;
	Context context;
	
	int imageWidth;
	int imageHeight;
	
	public GameObject(Context context, int resource)
	{
		this.context = context;
		imageActive = context.getResources().getDrawable(resource);
		imageWidth = imageActive.getIntrinsicWidth();
		imageHeight = imageActive.getIntrinsicHeight();	
		setPos(0, 0);
	}
	
	public void setPos(int x, int y)
	{
		imageActive.setBounds(x, y, x + imageWidth, y + imageHeight);
	}
	
	public void draw(Canvas c)
	{
		imageActive.draw(c);
	}
	
	abstract public void update(double time);
}
