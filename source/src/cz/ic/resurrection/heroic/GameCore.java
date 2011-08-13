package cz.ic.resurrection.heroic;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class GameCore extends Activity {
		public static final String LOG_TAG = "heroicquest";
		
	    private static final int MENU_START = 0;

	    private GameThread gameThread;
	    private GameView gameView;

	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        super.onCreateOptionsMenu(menu);

	        menu.add(0, MENU_START, 0, R.string.menu_start);

	        return true;
	    }

	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	            case MENU_START:
	                gameThread.doStart();
	                return true;
	        }

	        return false;
	    }

	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        setContentView(R.layout.main);

	        Log.w(LOG_TAG, "onCreate");
	        
	        gameView = (GameView) findViewById(R.id.game);
	        
	        gameView.createThread();
	        gameThread = gameView.getThread();
	        gameView.setStatusText((TextView) findViewById(R.id.main_text));

	        if (savedInstanceState == null) {
	            // we were just launched: set up a new game
	            Log.w(LOG_TAG, "SIS is null");
	        } else {
	            // we are being restored: resume a previous game
	            gameThread.restoreState(savedInstanceState);
	            Log.w(LOG_TAG, "SIS is nonnull");
	        }
	    }

	    /**
	     * Invoked when the Activity loses user focus.
	     */
	    @Override
	    protected void onPause() {
	        super.onPause();
	        Log.w(LOG_TAG, "onPause");
	        gameThread.pause(); // pause game when Activity pauses
	    }
	    
	    protected void onStop()
	    {
	    	super.onStop();
	    	Log.w(LOG_TAG, "onStop");
	    }
	    
	    protected void onRestart()
	    {
	    	super.onRestart();
	    	Log.w(LOG_TAG, "onRestart");
	    	gameView.createThread();
	        gameThread = gameView.getThread();
	    }

	    protected void onResume()
	    {
	    	super.onResume();
	    	Log.w(LOG_TAG, "onResume");
	    }
	    /**
	     * Notification that something is about to happen, to give the Activity a
	     * chance to save state.
	     *
	     * @param outState a Bundle into which this Activity should save its state
	     */
	    @Override
	    protected void onSaveInstanceState(Bundle outState) {
	        // just have the View's thread save its state into our Bundle
	        super.onSaveInstanceState(outState);
	        //gameThread.saveState(outState);
	        
	        Log.w(LOG_TAG, "SIS called");
	    }
	}