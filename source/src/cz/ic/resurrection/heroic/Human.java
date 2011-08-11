package cz.ic.resurrection.heroic;

import android.util.Log;
import android.view.MotionEvent;

public class Human extends Player implements BoardTouchListener {

	boolean myTurn;
	FigureMarked figureMarked;
	
	Human(HeroicInterface heroicInterface) {
		super(heroicInterface);

		myTurn = false;
		figureMarked = new FigureMarked();
	}

	@Override
	void TakeTurn() {
		figureMarked.isMarked = false;
		myTurn = true;
	}

	@Override
	public void TouchBoard(byte row, byte col, int action) {
		if(myTurn)
		{
			if(action == MotionEvent.ACTION_DOWN)
			{
				Log.d(GameCore.LOG_TAG, "row:" + row + " col:" + col);
				if(!figureMarked.isMarked) {
					figureMarked.isMarked = heroicInterface.markFigure(row, col);
					if(figureMarked.isMarked)
					{
						figureMarked.col = col;
						figureMarked.row = row;
					}
				}
				else {
					if(row == figureMarked.row && col == figureMarked.col)
					{
						figureMarked.isMarked = false;
						heroicInterface.markFigure(row, col);
					} else
					if(heroicInterface.moveFigure(row, col)) {
						myTurn = false;
					}
				}
				
			}
		}
	}

	
}
