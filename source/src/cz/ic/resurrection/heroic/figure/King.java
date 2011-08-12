package cz.ic.resurrection.heroic.figure;

import cz.ic.resurrection.heroic.Board;

public class King extends Figure {

	@Override
	public void setLegalMoves(Board board, BoardPos pos) {
		board.setBoardLegal(pos.y - 1, pos.x);
		board.setBoardLegal(pos.y - 1, pos.x - 1);
		board.setBoardLegal(pos.y + 1, pos.x);
		board.setBoardLegal(pos.y + 1, pos.x + 1);
		board.setBoardLegal(pos.y, pos.x - 1);
		board.setBoardLegal(pos.y + 1, pos.x - 1);
		board.setBoardLegal(pos.y, pos.x + 1);
		board.setBoardLegal(pos.y - 1, pos.x + 1);
	}

	@Override
	public void deathEvent(Board board, BoardPos pos) {
		// TODO Auto-generated method stub
		
	}

}
