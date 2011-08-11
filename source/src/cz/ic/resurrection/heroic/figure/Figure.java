package cz.ic.resurrection.heroic.figure;

import cz.ic.resurrection.heroic.GameObject;
import android.content.Context;
import android.graphics.Rect;
import android.view.MotionEvent;

public class Figure extends GameObject {
	private static boolean isSomeFigureActive = false;
	
	Figure [][] map;
	Color color;
	int posX, posY;
	
	public enum Color {
		WHITE,
		BLACK
	};
	
	public Figure(Context context, int resource, int x, int y, Color color, Figure [][] map) {
		super(context, resource);

		setPos(imageWidth * x, imageHeight * y);
		posX = x;
		posY = y;
		this.map = map;
		this.color = color;
	}

	@Override
	public void update(double time) {

	}

	public boolean onTouchEvent(MotionEvent event)
	{
		int x, y;
		Rect rect;
		if(!isSomeFigureActive)
		{
			x = (int) event.getX();
			y = (int) event.getY();
			rect = getBounds();
			if(rect.contains(x, y))
			{
				setPos(x, y);
				return true;
			}
		}
		return false;
	}
}
