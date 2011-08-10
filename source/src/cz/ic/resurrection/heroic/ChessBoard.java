package cz.ic.resurrection.heroic;

import java.util.ArrayList;

import android.content.Context;

public class ChessBoard extends GameObject {

	byte [][] boardFigures;
	byte [][] boardBackground;
	ArrayList<Figure> figure;
	
	int width;
	int height;
	
	public ChessBoard(Context context, int resource) {
		super(context, resource);
		
		width = 8;
		height = 8;
		figure = new ArrayList<Figure>();
	}

	@Override
	public void update(double time) {
		
	}

	public void load(String map)
	{
		// read map size
		boardFigures = new byte[height][width];
		boardBackground = new byte[height][width];
		figure.clear();
	}
}
