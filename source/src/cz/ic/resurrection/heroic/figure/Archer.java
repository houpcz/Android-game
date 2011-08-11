package cz.ic.resurrection.heroic.figure;

import cz.ic.resurrection.heroic.Board;

public class Archer extends Figure {

	@Override
	public void setLegalMoves(Board board, BoardPos pos) {
		if(board.setBoardLegal(pos.y - 1, pos.x - 1))
			board.setBoardLegal(pos.y - 2, pos.x - 2);
		if(board.setBoardLegal(pos.y + 1, pos.x + 1))
			board.setBoardLegal(pos.y + 2, pos.x + 2);
		if(board.setBoardLegal(pos.y + 1, pos.x - 1))
			board.setBoardLegal(pos.y + 2, pos.x - 2);
		if(board.setBoardLegal(pos.y - 1, pos.x + 1))
			board.setBoardLegal(pos.y - 2, pos.x + 2);
	}

}
