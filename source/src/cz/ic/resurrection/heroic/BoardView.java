package cz.ic.resurrection.heroic;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

public class BoardView extends GameObjectView {
	private Board board;
	private ArrayList<BoardTouchListener> touchListener;
	private int FIGURE_WIDTH = 30;
	
	public BoardView(Context context)
	{
		super(context, R.drawable.board_marble);
		touchListener = new ArrayList<BoardTouchListener>();
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
			
		Drawable figureImage = null;	
		if(board == null)
			return;
		
		byte [][] b = board.getBoard();
		
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
							case Board.FIG_KNIGHT :  figureImage = context.getResources().getDrawable(R.drawable.w_cavalery); break;
							case Board.FIG_BISHOP : figureImage = context.getResources().getDrawable(R.drawable.w_infantry); break;
							case Board.FIG_KING : figureImage = context.getResources().getDrawable(R.drawable.w_king); break;
						}
						break;
					case Player.DARK :
						switch(-b[loop1][loop2])
						{
							case Board.FIG_KNIGHT :  figureImage = context.getResources().getDrawable(R.drawable.b_cavalery); break;
							case Board.FIG_BISHOP : figureImage = context.getResources().getDrawable(R.drawable.b_infantry); break;
							case Board.FIG_KING : figureImage = context.getResources().getDrawable(R.drawable.b_king); break;
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
		
		
		drawMarkedFigure(canvas);
	}
	
	private void drawMarkedFigure(Canvas canvas)
	{
		FigureMarked figure = board.getMarkedFigure();
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
				touch.TouchBoard(y, x, event.getAction());
			}
			return true;
		}
		return false;
	}
	
	public void AttachTouchListener(BoardTouchListener listener)
	{
		touchListener.add(listener);
	}

	public void setNewGame(Board board) {
		this.board = board;
		touchListener.clear();
	}

	public void setCanvasSize(int canvasWidth, int canvasHeight) {		
		setPos(0, (canvasHeight - imageHeight) / 2);
	}
}
