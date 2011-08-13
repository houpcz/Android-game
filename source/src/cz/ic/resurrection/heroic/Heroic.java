package cz.ic.resurrection.heroic;

import android.util.Log;

public class Heroic implements HeroicInterface {
	Player [] player;
	Board board;
	BoardView boardView;
	int activePlayer;
	
	Heroic()
	{
		player = new Player[2];
		board = new Board(this);
	}

	public void SetBoardView(BoardView boardView)
	{
		this.boardView = boardView;
	}
	
	public Board getBoard() {
		return board;
	}
	
	public void setNewGame()
	{
		Log.w(GameCore.LOG_TAG, "Heroic : set new game");
		
		player[Player.LIGHT] = new Human(this);
		player[Player.DARK] = new Human(this);
		activePlayer = Player.LIGHT;
		
		board.setNewGame();
		boardView.setNewGame(board);
		boardView.AttachTouchListener((Human) player[Player.LIGHT]);
		boardView.AttachTouchListener((Human) player[Player.DARK]);
		
		
		player[activePlayer].TakeTurn();
		
		
	}
	
	private void nextTurn()
	{
		activePlayer = (activePlayer + 1) % 2;
		player[activePlayer].TakeTurn();
		
		board.setNextTurn();
	}
	
	public void update(double time)
	{
		
	}

	public int getActivePlayer()
	{
		return activePlayer;
	}
	
	@Override
	public boolean markFigure(byte row, byte col) {
		return board.markFigure(row, col);
	}

	@Override
	public boolean moveFigure(byte row, byte col) {
		boolean status = board.moveFigure(row, col);
		if(status)
		{
			nextTurn();
		}
		return status;
	}
}
