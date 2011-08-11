package cz.ic.resurrection.heroic;

import android.util.Log;


public class Board {
	// Positive are light
	public static final int FIG_KING = 1; // 1 white king, -1 black king 
	
	Heroic heroic;
	byte [][] board;
	
	FigureMarked figureMarked;
	
	public Board(Heroic heroic)
	{
		this.heroic = heroic;
	}
	
	public void setNewGame()
	{
		figureMarked = new FigureMarked();
		board = new byte[8][8];
		board[0][0] = FIG_KING;
		board[7][7] = -FIG_KING;
	}
	
	public final byte [][] getBoard()
	{
		return board;
	}
	
	public FigureMarked getMarkedFigure()
	{
		return figureMarked;
	}
	
	public boolean markFigure(byte row, byte col)
	{
		if(isActivePlayerFigure(row, col))
		{
			Log.w(GameCore.LOG_TAG, "activeFigure - " + row);
			if(figureMarked.isMarked)
			{
				figureMarked.isMarked = false;
			} else {
				figureMarked.isMarked = true;
				figureMarked.row = row;
				figureMarked.col = col;
			}
			return true;
		}
		
		return false;
	}
	
	boolean isActivePlayerFigure(byte row, byte col)
	{
		return heroic.getActivePlayer() == getFigureColor(board[row][col]);
	}
	
	public static int getFigureColor(byte fig)
	{
		if(fig > 0)
			return Player.LIGHT;
		else if(fig == 0)
			return Player.GREY;
		else
			return Player.DARK;
	}
}
