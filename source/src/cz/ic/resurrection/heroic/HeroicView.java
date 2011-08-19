package cz.ic.resurrection.heroic;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

public class HeroicView {
	
	Heroic heroic;
	
	BoardView boardView;
	HUDView boardTop;
	HUDView boardBottom;

	public HeroicView(Heroic heroic, Context context)
	{
		this.heroic = heroic;
		boardView = new BoardView(context);
		boardTop = new HUDView(heroic, context, R.drawable.board_marble_dark, 0, 0);
		boardBottom = new HUDView(heroic, context, R.drawable.board_marble_light, 0, boardTop.getBounds().height() + boardView.getBounds().height());
		boardView.setPos(0, boardTop.getBounds().height());
	}
	
	public void draw(Canvas canvas)
	{
		boardTop.draw(canvas);
		boardBottom.draw(canvas);
		boardView.draw(canvas);		
	}
	
	public boolean onTouchEvent(MotionEvent event)
    {
		if(heroic.getGameState() != Heroic.STATE_PLAYING)
			return false;
		
		return boardView.onTouchEvent(event) || boardBottom.onTouchEvent(event);
    }
	
	public BoardView getBoardView()
	{
		return boardView;
	}

	public void setCanvasSize(int canvasWidth, int canvasHeight) {
		boardView.setCanvasSize(canvasWidth, canvasHeight);
	}
}
