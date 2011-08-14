package cz.ic.resurrection.heroic;

import android.content.Context;
import cz.ic.resurrection.heroic.figure.Archer;
import cz.ic.resurrection.heroic.figure.BoardPos;
import cz.ic.resurrection.heroic.figure.Figure;
import cz.ic.resurrection.heroic.figure.King;
import cz.ic.resurrection.heroic.figure.Knight;
import cz.ic.resurrection.heroic.figure.NullFigure;
import cz.ic.resurrection.heroic.figure.Pawn;
import cz.ic.resurrection.heroic.figure.Wall;

public class Board {
	// Positive are light
	public static final byte FIG_WALL = 2;
	public static final byte FIG_ARCHER = 5;
	public static final byte FIG_KNIGHT = 4;
	public static final byte FIG_PAWN = 3;
	public static final byte FIG_KING = 1; // 1 white king, -1 black king 
	public static final byte FIG_NONE = 0;
	
	public static final byte TILE_RESPAWN = 1;
	
	public static final byte BOARD_WIDTH = 8;
	
	Heroic heroic;
	Context context;
	byte [][] board;
	byte [][] boardBG;
	boolean [][] boardLegalClick;
	Figure [] figure;
	
	FigureMarked figureMarked;
	
	public Board(Heroic heroic, Context context)
	{
		this.heroic = heroic;
		this.context = context;
		figure = new Figure[10];
		board = new byte[BOARD_WIDTH][BOARD_WIDTH];
		boardBG = null; 
		boardLegalClick = new boolean[BOARD_WIDTH][BOARD_WIDTH];
		
		figure[FIG_NONE] = new NullFigure();
		figure[FIG_KING] = new King();
		figure[FIG_PAWN] = new Pawn();
		figure[FIG_KNIGHT] = new Knight();
		figure[FIG_ARCHER] = new Archer();
		figure[FIG_WALL] = new Wall();
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
					BoardPos pos = new BoardPos(loop1, loop2);
					boardLegalClick[loop1][loop2] = isActivePlayerFigure(loop1, loop2) && 
													figure[getFigureOnPos(pos)].canMove(this, pos);
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

	/**
	 * @param isLegal
	 * @param row
	 * @param col
	 * @return if on the place is some figure - some figures cant go through
	 */
	public boolean setBoardLegal(int row, int col)
	{
		if(row >= 0 && row < BOARD_WIDTH &&
		   col >= 0 && col < BOARD_WIDTH)
		{
			boardLegalClick[row][col] = !isActivePlayerFigure((byte) row, (byte) col);
			return board[row][col] == FIG_NONE;
		}
		
		return false;
	}
	
	public boolean canMoveLegal(int row, int col)
	{
		if(row >= 0 && row < BOARD_WIDTH &&
		   col >= 0 && col < BOARD_WIDTH)
		{
			return !isActivePlayerFigure((byte) row, (byte) col);
		}
		return false;
	}
	
	public void setNewGame()
	{
		figureMarked = new FigureMarked();
		byte [][][] map = Map.loadBoard("map01", context);
		board = map[1];
		boardBG = map[0];
		boardLegalClick = new boolean[BOARD_WIDTH][BOARD_WIDTH];		
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
	
	public final byte [][] getBackground()
	{
		return boardBG;
	}
	
	public FigureMarked getMarkedFigure()
	{
		return figureMarked;
	}
	
	public boolean markFigure(byte row, byte col)
	{
		if(isActivePlayerFigure(row, col))
		{
			//Log.w(GameCore.LOG_TAG, "activeFigure - " + row);
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
	
	public static byte getFigureColor(byte fig)
	{
		if(fig > 0)
			return Player.LIGHT;
		else if(fig == 0)
			return Player.GREY;
		else
			return Player.DARK;
	}

	public boolean killFigure(byte row, byte col)
	{
		int fig = getFigureOnPos(new BoardPos(row, col));
		figure[fig].deathEvent(this, new BoardPos(row, col), getFigureColor(board[row][col]));
		board[row][col] = FIG_NONE;		
		return true;
	}
	
	public void defeatPlayer(byte looser)
	{
		if(looser == Player.DARK)
			heroic.setGameOver(Player.LIGHT);
		else if(looser == Player.LIGHT)
			heroic.setGameOver(Player.DARK);
	}
	
	public boolean moveFigure(byte row, byte col) {
		if(figureMarked.isMarked)
		{
			if(boardLegalClick[row][col])
			{
				int fig = getFigureOnPos(new BoardPos(row, col));
				byte color = getFigureColor(board[row][col]);
				board[row][col] = board[figureMarked.pos.y][figureMarked.pos.x];
				board[figureMarked.pos.y][figureMarked.pos.x] = FIG_NONE;
				figure[fig].deathEvent(this, new BoardPos(row, col), color);
				return true;
			} else {
				figureMarked.isMarked = false;
				setBoardLegal();
			}
		}
		return false;
	}

	public int getGameState() {
		return heroic.getGameState();
	}

	public BoardPos getFreeRespawnPlace(byte color) {
		byte respawnID = (color == Player.DARK) ? -TILE_RESPAWN : TILE_RESPAWN;
		for(byte loop1 = 0; loop1 < BOARD_WIDTH; loop1++)
		{
			for(byte loop2 = 0; loop2 < BOARD_WIDTH; loop2++)
			{
				if(boardBG[loop1][loop2] == respawnID &&
				   board[loop1][loop2] == FIG_NONE)
				{
					return new BoardPos(loop1, loop2);
				}
			}
		}
		return null;
	}
}
