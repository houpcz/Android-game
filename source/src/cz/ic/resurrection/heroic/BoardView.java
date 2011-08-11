package cz.ic.resurrection.heroic;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

public class BoardView extends GameObjectView {
	Board board;
	ArrayList<BoardTouchListener> touchListener;
	int FIGURE_WIDTH = 30;
	
	public BoardView(Board board, Context context)
	{
		super(context, R.drawable.board_marble);
		this.board = board;
		touchListener = new ArrayList<BoardTouchListener>();
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		
		byte [][] b = board.getBoard();
		Drawable figureImage = null;
		
		if(b == null)
			return;
		
		FigureMarked figure = board.getMarkedFigure();
		
		for(int loop1 = 0; loop1 < b.length; loop1++)
		{
			for(int loop2 = 0; loop2 < b[loop1].length; loop2++)
			{
				figureImage = null;
				switch(Board.getFigureColor(b[loop1][loop2]))
				{
					case Player.LIGHT :
						switch(b[loop1][loop2])
						{
							case Board.FIG_KING : figureImage = context.getResources().getDrawable(R.drawable.w_king);
						}
						break;
					case Player.DARK :
						switch(-b[loop1][loop2])
						{
							case Board.FIG_KING : figureImage = context.getResources().getDrawable(R.drawable.b_king);
						}
						break;
					case Player.GREY :
						figureImage = null;
						break;
				}
				if(figureImage != null)
				{
					int left = loop2 * FIGURE_WIDTH;
					int right = left + FIGURE_WIDTH;
					int top = loop1 * FIGURE_WIDTH;
					int bottom = top + FIGURE_WIDTH;
					figureImage.setBounds(left, top, right, bottom);
					figureImage.draw(canvas);
				}
			}
		}
		
		
		if(figure.isMarked)
		{
			Paint paint = new Paint();
			paint.setARGB(120, 255, 0, 0);
			int left = figure.col * FIGURE_WIDTH;
			int right = left + FIGURE_WIDTH;
			int top = figure.row * FIGURE_WIDTH;
			int bottom = top + FIGURE_WIDTH;
			canvas.drawRect(left, top, right, bottom, paint);
		}
	}
	
	public boolean onTouchEvent(MotionEvent event)
	{
		if(getBounds().contains((int) event.getX(), (int) event.getY()))
		{
			// TODO is not OK, repair it
			byte x = (byte) (((int) event.getX() - getBounds().left) / FIGURE_WIDTH);
			byte y = (byte) (((int) event.getY() - getBounds().top) / FIGURE_WIDTH);
			for(BoardTouchListener touch : touchListener)
			{
				touch.TouchBoard(y, x);
			}
			return true;
		}
		return false;
	}
	
	public void AttachTouchListener(BoardTouchListener listener)
	{
		touchListener.add(listener);
	}
}
