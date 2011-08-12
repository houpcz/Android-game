package cz.ic.resurrection.heroic;

import cz.ic.resurrection.heroic.figure.BoardPos;

public class FigureMarked {
	public boolean isMarked;
	public BoardPos pos;
	
	public FigureMarked()
	{
		isMarked = false;
		pos = new BoardPos((byte) 0, (byte) 0);
	}
}
