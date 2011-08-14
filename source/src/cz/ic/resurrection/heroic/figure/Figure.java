package cz.ic.resurrection.heroic.figure;

import cz.ic.resurrection.heroic.Board;

public abstract class Figure {
	abstract public void setLegalMoves(Board board, BoardPos pos);
	public void deathEvent(Board board, BoardPos pos, byte ownerColor) {
		
	}
	abstract public boolean canMove(Board board, BoardPos pos);
}
