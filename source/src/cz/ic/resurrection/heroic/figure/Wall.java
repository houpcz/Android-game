package cz.ic.resurrection.heroic.figure;

import cz.ic.resurrection.heroic.Board;

public class Wall extends Figure{

	@Override
	public void setLegalMoves(Board board, BoardPos pos) {
		// wall cant move
	}

}
