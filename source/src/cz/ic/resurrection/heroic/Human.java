package cz.ic.resurrection.heroic;

import android.util.Log;

public class Human extends Player implements BoardTouchListener {

	boolean myTurn;
	
	Human(HeroicInterface heroicInterface) {
		super(heroicInterface);

		myTurn = false;
	}

	@Override
	void TakeTurn() {
		myTurn = true;
	}

	@Override
	public void TouchBoard(byte row, byte col) {
		if(myTurn)
		{
			Log.w(GameCore.LOG_TAG, "row:" + row + " col:" + col);
			heroicInterface.markFigure(row, col);
		}
	}

	
}
