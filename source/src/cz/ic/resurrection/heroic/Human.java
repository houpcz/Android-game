package cz.ic.resurrection.heroic;

import android.util.Log;
import android.view.MotionEvent;

public class Human extends Player implements BoardTouchListener {

	boolean myTurn;
	boolean isMarked;
	
	Human(HeroicInterface heroicInterface) {
		super(heroicInterface);

		myTurn = false;
	}

	@Override
	void TakeTurn() {
		isMarked = false;
		myTurn = true;
	}

	@Override
	public void TouchBoard(byte row, byte col, int action) {
		if(myTurn)
		{
			if(action == MotionEvent.ACTION_DOWN)
			{
				Log.d(GameCore.LOG_TAG, "row:" + row + " col:" + col);
				if(!isMarked)
					isMarked = heroicInterface.markFigure(row, col);
				else if(heroicInterface.moveFigure(row, col)) {
					myTurn = false;
				}
			}
		}
	}

	
}
