package cz.ic.resurrection.heroic.figure;

import cz.ic.resurrection.heroic.R;
import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;


public class NullFigure extends Figure {

	public NullFigure(Context context) {
		super(context, R.drawable.b_wall, 0, 0, Color.BLACK, null);
	}

	public void draw(Canvas c)
	{
		// nothing
	}
	
	public boolean onTouchEvent(MotionEvent event)
	{
		return false;
	}
}
