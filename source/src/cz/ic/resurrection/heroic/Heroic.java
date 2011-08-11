package cz.ic.resurrection.heroic;

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
		player[Player.LIGHT] = new Human(this);
		player[Player.DARK] = new Human(this);
		board.setNewGame();
		
		boardView.AttachTouchListener((Human) player[Player.LIGHT]);
		boardView.AttachTouchListener((Human) player[Player.DARK]);
		
		activePlayer = Player.LIGHT;
		player[activePlayer].TakeTurn();
		
		
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
}
