package cz.ic.resurrection.heroic;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

public class HeroicView {
	
	Heroic heroic;
	
	BoardView boardView;

	public HeroicView(Heroic heroic, Context context)
	{
		this.heroic = heroic;
		boardView = new BoardView(context);
	}
	
	public void draw(Canvas canvas)
	{
		boardView.draw(canvas);
	}
	
	public boolean onTouchEvent(MotionEvent event)
    {
		return boardView.onTouchEvent(event);
    }
	
	public BoardView getBoardView()
	{
		return boardView;
	}

	public void setCanvasSize(int canvasWidth, int canvasHeight) {
		boardView.setCanvasSize(canvasWidth, canvasHeight);
	}
}
