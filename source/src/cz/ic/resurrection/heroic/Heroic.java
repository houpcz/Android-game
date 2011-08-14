package cz.ic.resurrection.heroic;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

public class Heroic implements HeroicInterface {
	public static final int STATE_READY = 0;
	public static final int STATE_PLAYING = 1;
	public static final int STATE_LIGHT_WINS = 2;
	public static final int STATE_DARK_WINS = 3;
	int state;
	
	public static final int INNER_STATE_MOVE_FIGURE = 0;
	int innerState;
	
	Player [] player;
	Board board;
	BoardView boardView;
	int activePlayer;
	Handler handler;
	Context context;
	int level;
	
	Heroic(Handler handler, Context context)
	{
		player = new Player[2];
		board = new Board(this, context);
		this.handler = handler;
		this.context = context;
		setState(STATE_READY);
	}
	
	void leavingState(int oldState)
	{
		
	}
	
	void startingState(int newState)
	{
		switch(newState)
		{
			case STATE_READY :
				changeGameStatus(context.getString(R.string.heroic_press_menu));
				break;
			case STATE_PLAYING :
				changeGameStatus("");
				break;
			case STATE_LIGHT_WINS :
				changeGameStatus(context.getString(R.string.heroic_light_wins));
				break;
			case STATE_DARK_WINS :
				changeGameStatus(context.getString(R.string.heroic_dark_wins));
				break;
		}
	}
	
	private void setState(int newState)
	{
		leavingState(state);
		startingState(newState);
		state = newState;
	}

	public void setBoardView(BoardView boardView)
	{
		this.boardView = boardView;
	}
	
	public Board getBoard() {
		return board;
	}
	
	public void setGameOver(byte winner)
	{
		switch(winner)
		{
			case Player.DARK : setState(STATE_DARK_WINS); break;
			case Player.LIGHT : setState(STATE_LIGHT_WINS); break;
		}
	}
	
	public void setNewGame(int level)
	{
		Log.w(GameCore.LOG_TAG, "Heroic : set new game");
		this.level = level;
		player[Player.LIGHT] = new Human(this);
		player[Player.DARK] = new Human(this);
		activePlayer = Player.LIGHT;
		
		board.setNewGame(level);
		boardView.setNewGame(board);
		boardView.AttachTouchListener((Human) player[Player.LIGHT]);
		boardView.AttachTouchListener((Human) player[Player.DARK]);
		
		
		player[activePlayer].TakeTurn();
		
		setState(STATE_PLAYING);
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

	public int getGameState() {
		return state;
	}
}
