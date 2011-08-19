package cz.ic.resurrection.heroic;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

public class HUDView extends GameObjectView {
	Heroic heroic;
	Context context;
	byte color;
	GameObjectView spellbook;
	
	public HUDView(Heroic heroic, Context context, int drawableID, int x, int y)
	{
		super(context, drawableID);
		setPos(x, y);
		color = (drawableID == R.drawable.board_marble_light) ? Player.LIGHT : Player.DARK;
		int spellbookID = (color == Player.LIGHT) ? R.drawable.b_spellbook : R.drawable.w_spellbook;
		spellbook = new GameObjectView(context, spellbookID);
		spellbook.setPos(getBounds().right - spellbook.getImageWidth(), getBounds().top);
		this.heroic = heroic;
		this.context = context;
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		spellbook.draw(canvas);
	}
	
	public boolean onTouchEvent(MotionEvent event)
	{
		if(spellbook.getBounds().contains((int) event.getX(), (int) event.getY()))
		{
			return true;
		}
		
		return false;
	}
}