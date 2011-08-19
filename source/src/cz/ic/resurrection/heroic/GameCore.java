package cz.ic.resurrection.heroic;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class GameCore extends Activity {
		public static final String LOG_TAG = "heroicquest";

	    private GameThread gameThread;
	    private GameView gameView;

	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        super.onCreateOptionsMenu(menu);

	        MenuInflater inflater = getMenuInflater();
	        inflater.inflate(R.layout.game_menu, menu);

	        return true;
	    }

	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	            case R.id.level1:
	                gameThread.doStart(1);
	                return true;
	            case R.id.level2:
	                gameThread.doStart(2);
	                return true;
	            case R.id.level3:
	                gameThread.doStart(3);
	                return true;
	            case R.id.level4:
	                gameThread.doStart(4);
	                return true;
	            case R.id.level5:
	                gameThread.doStart(5);
	                return true;
	            case R.id.level6:
	                gameThread.doStart(6);
	                return true;
	            case R.id.level7:
	                gameThread.doStart(7);
	                return true;
	            case R.id.level8:
	                gameThread.doStart(8);
	                return true;
	            case R.id.level9:
	                gameThread.doStart(9);
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
	        gameThread.saveState(outState);
	        
	        Log.w(LOG_TAG, "SIS called");
	    }
	    
	    @Override
	    public void onRestoreInstanceState(Bundle savedInstanceState) {
	      super.onRestoreInstanceState(savedInstanceState);
	      Log.w(LOG_TAG, "SIS restore");
	      gameThread.restoreState(savedInstanceState);
	    }

	}