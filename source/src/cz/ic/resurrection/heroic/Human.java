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
	public void TouchBoard(byte row, byte col, int action, boolean clickable) {
		if(myTurn)
		{
			if(action == MotionEvent.ACTION_DOWN)
			{
				Log.d(GameCore.LOG_TAG, "row:" + row + " col:" + col);
				if(!figureMarked.isMarked && clickable) {
					figureMarked.isMarked = heroicInterface.markFigure(row, col);
					if(figureMarked.isMarked)
					{
						figureMarked.pos.x = col;
						figureMarked.pos.y = row;
					}
				}
				else {
					if(row == figureMarked.pos.y && col == figureMarked.pos.x)
					{
						figureMarked.isMarked = false;
						heroicInterface.markFigure(row, col);
					} else
					if(heroicInterface.moveFigure(row, col)) {
						myTurn = false;
					} else {
						figureMarked.isMarked = false;
					}
				}
				
			}
		}
	}

	
}
