package cz.ic.resurrection.heroic.figure;

import cz.ic.resurrection.heroic.Board;

public class Bishop extends Figure {

	@Override
	public void setLegalMoves(Board board, BoardPos pos) {
		if(board.setBoardLegal(pos.y - 1, pos.x))
			board.setBoardLegal(pos.y - 2, pos.x);
		
		if(board.setBoardLegal(pos.y + 1, pos.x))
			board.setBoardLegal(pos.y + 2, pos.x);
		
		if(board.setBoardLegal(pos.y, pos.x - 1))
			board.setBoardLegal(pos.y, pos.x - 2);
		
		if(board.setBoardLegal(pos.y, pos.x + 1));
			board.setBoardLegal(pos.y, pos.x + 2);
	}

}
