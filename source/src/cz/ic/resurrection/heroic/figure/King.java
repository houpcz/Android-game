package cz.ic.resurrection.heroic.figure;

import cz.ic.resurrection.heroic.R;
import android.content.Context;

public class King extends Figure {

	public King(Context context, Color color, int x, int y, Figure[][] map) {
		super(context, (color == Color.BLACK) ? R.drawable.b_king : R.drawable.w_king, x, y, color, map);
	}

}
