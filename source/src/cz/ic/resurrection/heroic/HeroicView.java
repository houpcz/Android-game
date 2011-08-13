package cz.ic.resurrection.heroic;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

public class HeroicView {
	
	Heroic heroic;
	
	BoardView boardView;
	GameObjectView boardTop;
	GameObjectView boardBottom;

	public HeroicView(Heroic heroic, Context context)
	{
		this.heroic = heroic;
		boardView = new BoardView(context);
		boardTop = new GameObjectView(context, R.drawable.board_marble_dark);
		boardBottom = new GameObjectView(context, R.drawable.board_marble_light);
		boardTop.setPos(0, 0);
		boardView.setPos(0, boardTop.getBounds().height());
		boardBottom.setPos(0, boardTop.getBounds().height() + boardView.getBounds().height());
	}
	
	public void draw(Canvas canvas)
	{
		boardTop.draw(canvas);
		boardBottom.draw(canvas);
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
