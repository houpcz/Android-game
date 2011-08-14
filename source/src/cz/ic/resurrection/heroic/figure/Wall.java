package cz.ic.resurrection.heroic.figure;

import cz.ic.resurrection.heroic.Board;

public class Wall extends Figure{

	@Override
	public void setLegalMoves(Board board, BoardPos pos) {
		// wall cant move
	}
	
	public boolean canMove(Board board, BoardPos pos)
	{
		return false;
	}
	
	public void deathEvent(Board board, BoardPos pos, byte ownerColor) {
		board.killFigure(pos.y, pos.x);
	}

}
