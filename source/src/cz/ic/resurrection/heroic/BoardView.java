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
	
	private void drawBackground(Canvas canvas)
	{
		byte [][] b = board.getBackground();
		Drawable figureImage = null;
		
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
							case Board.TILE_RESPAWN : figureImage = context.getResources().getDrawable(R.drawable.w_respawn); break;
						}
					case Player.DARK :
						switch(-b[loop1][loop2])
						{
							case Board.TILE_RESPAWN : figureImage = context.getResources().getDrawable(R.drawable.b_respawn); break;
						}
				}
				if(figureImage != null)
				{
					int left = loop2 * FIGURE_WIDTH + getBounds().left;
					int right = left + FIGURE_WIDTH;
					int top = loop1 * FIGURE_WIDTH + getBounds().top;
					int bottom = top + FIGURE_WIDTH;
					figureImage.setBounds(left, top, right, bottom);
					figureImage.draw(canvas);
				}
			}
		}
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
			
		Drawable figureImage = null;	
		if(board == null)
			return;
		
		byte [][] b = board.getBoard();
		boolean [][] legal = board.getBoardLegal();
		
		drawBackground(canvas);
		
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
							case Board.FIG_WALL : figureImage = context.getResources().getDrawable(R.drawable.w_wall); break;
							case Board.FIG_ARCHER : figureImage = context.getResources().getDrawable(R.drawable.w_archer); break;
							case Board.FIG_KNIGHT : figureImage = context.getResources().getDrawable(R.drawable.w_cavalery); break;
							case Board.FIG_PAWN : figureImage = context.getResources().getDrawable(R.drawable.w_infantry); break;
							case Board.FIG_KING : figureImage = context.getResources().getDrawable(R.drawable.w_king); break;
						}
						break;
					case Player.DARK :
						switch(-b[loop1][loop2])
						{
							case Board.FIG_WALL : figureImage = context.getResources().getDrawable(R.drawable.b_wall); break;
							case Board.FIG_ARCHER : figureImage = context.getResources().getDrawable(R.drawable.b_archer); break;
							case Board.FIG_KNIGHT : figureImage = context.getResources().getDrawable(R.drawable.b_cavalery); break;
							case Board.FIG_PAWN : figureImage = context.getResources().getDrawable(R.drawable.b_infantry); break;
							case Board.FIG_KING : figureImage = context.getResources().getDrawable(R.drawable.b_king); break;
						}
						break;
					case Player.GREY :
						figureImage = null;
						break;
				}
				if(figureImage != null)
				{
					int left = loop2 * FIGURE_WIDTH + getBounds().left;
					int right = left + FIGURE_WIDTH;
					int top = loop1 * FIGURE_WIDTH + getBounds().top;
					int bottom = top + FIGURE_WIDTH;
					figureImage.setBounds(left, top, right, bottom);
					figureImage.draw(canvas);
				}
			}
		}
		
		if(board.getGameState() == Heroic.STATE_PLAYING)
		{
			for(int loop1 = 0; loop1 < b.length; loop1++)
			{
				for(int loop2 = 0; loop2 < b[loop1].length; loop2++)
				{
					if(legal[loop1][loop2])
						drawLegalTiles(canvas, loop2, loop1);
				}
			}
		}
		
		
		drawMarkedFigure(canvas);
	}
	
	private void drawLegalTiles(Canvas canvas, int col, int row)
	{
		Paint paint = new Paint();
		paint.setARGB(16, 0, 255, 0);
		paint.setStrokeWidth(2.0f);
		int left = col * FIGURE_WIDTH + getBounds().left;
		int right = left + FIGURE_WIDTH;
		int top = row * FIGURE_WIDTH + getBounds().top;
		int bottom = top + FIGURE_WIDTH;
		canvas.drawRect(left, top, right, bottom, paint);
		paint.setARGB(255, 0, 255, 0);
		canvas.drawLines(new float[] {left, top, left, bottom, 
									  left, bottom, right, bottom, 
									  right, bottom, right, top,
									  right, top, left, top}, paint);
	}
	
	private void drawMarkedFigure(Canvas canvas)
	{
		FigureMarked figure = board.getMarkedFigure();
		if(figure.isMarked)
		{
			Paint paint = new Paint();
			paint.setARGB(255, 255, 0, 0);
			paint.setStrokeWidth(2.0f);
			int left = figure.pos.x * FIGURE_WIDTH + getBounds().left;
			int right = left + FIGURE_WIDTH;
			int top = figure.pos.y * FIGURE_WIDTH + getBounds().top;
			int bottom = top + FIGURE_WIDTH;
			canvas.drawLines(new float[] {left, top, left, bottom, 
										  left, bottom, right, bottom, 
										  right, bottom, right, top,
										  right, top, left, top}, paint);
		}
	}
	
	public boolean onTouchEvent(MotionEvent event)
	{
		if(getBounds().contains((int) event.getX(), (int) event.getY()))
		{
			if(board != null)
			{
				int state = board.getGameState();
				if(state == Heroic.STATE_PLAYING)
				{
					byte x = (byte) (((int) event.getX() - getBounds().left) / FIGURE_WIDTH);
					byte y = (byte) (((int) event.getY() - getBounds().top) / FIGURE_WIDTH);
					boolean clickable = board.getBoardLegal()[y][x];
					for(BoardTouchListener touch : touchListener)
					{
						touch.TouchBoard(y, x, event.getAction(), clickable);
					}
					return true;
				} else if(state == Heroic.STATE_DARK_WINS || state == Heroic.STATE_LIGHT_WINS)
				{
					
				}
			}
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
		int leftBoard = (canvasWidth - imageWidth) / 2;
		int topBoard = (canvasHeight - imageHeight) / 2;
		setPos(leftBoard, topBoard);
	}
}
