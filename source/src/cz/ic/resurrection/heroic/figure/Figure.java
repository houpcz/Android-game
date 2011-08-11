package cz.ic.resurrection.heroic.figure;

import cz.ic.resurrection.heroic.Board;

public abstract class Figure {
	abstract public void setLegalMoves(Board board, BoardPos pos);
}
