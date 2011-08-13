package cz.ic.resurrection.heroic.figure;

import cz.ic.resurrection.heroic.Board;

public class NullFigure extends Figure {

	@Override
	public void setLegalMoves(Board board, BoardPos pos) {

	}
	
	public boolean canMove(Board board, BoardPos pos)
	{
		return false;
	}

	@Override
	public void deathEvent(Board board, BoardPos pos) {

	}

}
