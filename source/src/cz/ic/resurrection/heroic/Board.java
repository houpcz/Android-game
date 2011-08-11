package cz.ic.resurrection.heroic;

import android.util.Log;
import cz.ic.resurrection.heroic.figure.Bishop;
import cz.ic.resurrection.heroic.figure.BoardPos;
import cz.ic.resurrection.heroic.figure.Figure;
import cz.ic.resurrection.heroic.figure.King;
import cz.ic.resurrection.heroic.figure.Knight;


public class Board {
	// Positive are light
	public static final byte FIG_KNIGHT = 3;
	public static final byte FIG_BISHOP = 2;
	public static final byte FIG_KING = 1; // 1 white king, -1 black king 
	public static final byte FIG_NONE = 0;
	public static final byte BOARD_WIDTH = 8;
	
	Heroic heroic;
	byte [][] board;
	boolean [][] boardLegalClick;
	Figure [] figure;
	
	FigureMarked figureMarked;
	
	public Board(Heroic heroic)
	{
		this.heroic = heroic;
		figure = new Figure[10];
		figure[FIG_KING] = new King();
		figure[FIG_BISHOP] = new Bishop();
		figure[FIG_KNIGHT] = new Knight();
	}
	
	public final boolean [][] getBoardLegal()
	{
		return boardLegalClick;
	}
	private void setBoardLegal()
	{
		if(!figureMarked.isMarked)
		{
			for(byte loop1 = 0; loop1 < BOARD_WIDTH; loop1++)
			{
				for(byte loop2 = 0; loop2 < BOARD_WIDTH; loop2++)
				{
					boardLegalClick[loop1][loop2] = isActivePlayerFigure(loop1, loop2);
				}
			}
		} else {
			for(byte loop1 = 0; loop1 < BOARD_WIDTH; loop1++)
			{
				for(byte loop2 = 0; loop2 < BOARD_WIDTH; loop2++)
				{
					boardLegalClick[loop1][loop2] = false;
				}
			}
			figure[getFigureOnPos(figureMarked.pos)].setLegalMoves(this, figureMarked.pos);
		}
	}
	
	private int getFigureOnPos(BoardPos pos) {
		return (board[pos.y][pos.x] < 0) ? -board[pos.y][pos.x] : board[pos.y][pos.x];
	}

	public void setBoardLegal(boolean isLegal, int row, int col)
	{
		if(row >= 0 && row < BOARD_WIDTH &&
		   col >= 0 && col < BOARD_WIDTH)
			boardLegalClick[row][col] = isLegal;
	}
	
	public void setNewGame()
	{
		figureMarked = new FigureMarked();
		board = new byte[BOARD_WIDTH][BOARD_WIDTH];
		boardLegalClick = new boolean[BOARD_WIDTH][BOARD_WIDTH];
		
		board[0][0] = FIG_KING;
		board[1][0] = FIG_BISHOP;
		board[0][1] = FIG_BISHOP;
		board[1][1] = FIG_KNIGHT;
		
		board[6][7] = -FIG_BISHOP;
		board[7][6] = -FIG_BISHOP;
		board[6][6] = -FIG_KNIGHT;
		board[7][7] = -FIG_KING;
		
		setBoardLegal();
	}
	
	public void setNextTurn()
	{
		figureMarked.isMarked = false;
		setBoardLegal();
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
				figureMarked.pos.y = row;
				figureMarked.pos.x = col;
			}
			setBoardLegal();
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

	public boolean moveFigure(byte row, byte col) {
		if(figureMarked.isMarked)
		{
			if(boardLegalClick[row][col])
			{
				board[row][col] = board[figureMarked.pos.y][figureMarked.pos.x];
				board[figureMarked.pos.y][figureMarked.pos.x] = FIG_NONE;
				return true;
			} else {
				figureMarked.isMarked = false;
				setBoardLegal();
			}
		}
		return false;
	}
}
