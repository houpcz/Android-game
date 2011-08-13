package cz.ic.resurrection.heroic;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

public class Heroic implements HeroicInterface {
	Player [] player;
	Board board;
	BoardView boardView;
	int activePlayer;
	Handler handler;
	
	Heroic(Handler handler)
	{
		player = new Player[2];
		board = new Board(this);
		this.handler = handler;
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
		
		changeGameStatus("");
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
	
	private void changeGameStatus(String newStatus)
	{
		Message msg = handler.obtainMessage();
        Bundle b = new Bundle();
        msg.setData(b);
        b.putString("text", newStatus);
        b.putInt("viz", newStatus.compareTo("") == 0 ? View.INVISIBLE : View.VISIBLE);
        handler.sendMessage(msg);
	}
}
