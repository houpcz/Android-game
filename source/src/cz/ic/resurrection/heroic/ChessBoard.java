package cz.ic.resurrection.heroic;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import cz.ic.resurrection.heroic.figure.Figure;
import cz.ic.resurrection.heroic.figure.Figure.Color;
import cz.ic.resurrection.heroic.figure.King;
import cz.ic.resurrection.heroic.figure.NullFigure;;

public class ChessBoard extends GameObject {
	Figure [][] figure;
	
	int width;
	int height;
	
	public ChessBoard(Context context, int resource) {
		super(context, resource);
		
		width = 8;
		height = 8;
		
		load();
	}

	@Override
	public void update(double time) {
		
	}
	
	@Override
	public void draw(Canvas c)
	{
		super.draw(c);
		
		if(figure == null)
			return;
		
		for(int loop1 = 0; loop1 < figure.length; loop1++)
		{
			for(int loop2 = 0; loop2 < figure[loop1].length; loop2++)
			{
				figure[loop1][loop2].draw(c);
			}
		}
	}

	public void load()
	{
		// read map size
		figure = new Figure[height][width];
		for(int loop1 = 0; loop1 < figure.length; loop1++)
		{
			for(int loop2 = 0; loop2 < figure[loop1].length; loop2++)
			{
				figure[loop1][loop2] = new NullFigure(context);
			}
		}
		figure[0][0] = new King(context, Color.BLACK, 0, 0, figure);
		figure[height - 1][width - 1] = new King(context, Color.WHITE, width - 1, height - 1, figure);
	}
	
	public boolean onTouchEvent(MotionEvent event)
	{
		boolean status = false;
		for(int loop1 = 0; loop1 < figure.length; loop1++)
		{
			for(int loop2 = 0; loop2 < figure[loop1].length; loop2++)
			{
				status = status || figure[loop1][loop2].onTouchEvent(event);
			}
    	}
    	return status;
	}
}
