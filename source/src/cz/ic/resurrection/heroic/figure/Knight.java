package cz.ic.resurrection.heroic.figure;

import cz.ic.resurrection.heroic.Board;

public class Knight extends Figure {

	@Override
	public void setLegalMoves(Board board, BoardPos pos) {
		board.setBoardLegal(pos.y - 1, pos.x - 2);
		board.setBoardLegal(pos.y + 1, pos.x - 2);
		board.setBoardLegal(pos.y + 1, pos.x + 2);
		board.setBoardLegal(pos.y - 1, pos.x + 2);
		board.setBoardLegal(pos.y - 2, pos.x - 1);
		board.setBoardLegal(pos.y + 2, pos.x - 1);
		board.setBoardLegal(pos.y + 2, pos.x + 1);
		board.setBoardLegal(pos.y - 2, pos.x + 1);
	}
	
	public boolean canMove(Board board, BoardPos pos)
	{
		if(board.canMoveLegal(pos.y - 1, pos.x - 2))
			return true;
		if(board.canMoveLegal(pos.y + 1, pos.x - 2))
			return true;
		if(board.canMoveLegal(pos.y + 1, pos.x + 2))
			return true;
		if(board.canMoveLegal(pos.y - 1, pos.x + 2))
			return true;
		if(board.canMoveLegal(pos.y - 2, pos.x - 1))
			return true;
		if(board.canMoveLegal(pos.y + 2, pos.x - 1))
			return true;
		if(board.canMoveLegal(pos.y + 2, pos.x + 1))
			return true;
		if(board.canMoveLegal(pos.y - 2, pos.x + 1))
			return true;
		
		return false;
	}
}
