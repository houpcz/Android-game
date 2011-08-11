package cz.ic.resurrection.heroic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;


	class GameThread extends Thread {
        public static final int STATE_LOSE = 1;
        public static final int STATE_PAUSE = 2;
        public static final int STATE_READY = 3;
        public static final int STATE_RUNNING = 4;
        public static final int STATE_WIN = 5;
        private int gameState;
 
        private int canvasHeight = 1;
        private int canvasWidth = 1;

        //private Handler handler;
    private Context gameContext;
    Heroic heroic;
    HeroicView heroicView;

    /** Used to figure out elapsed time between frames */
    private long timeLast;

    private Paint linePaint;
    private Paint linePaintBad;

    private boolean isOver = true;
    private SurfaceHolder surfaceHolder;

    public GameThread(SurfaceHolder surfaceHolder, Context context,
            Handler handler) {
        
    	Log.w(GameCore.LOG_TAG, "Thread created");
    	
        this.surfaceHolder = surfaceHolder;
        //this.handler = handler; // now is not useful
        gameContext = context;
        
        heroic = new Heroic();
        heroicView = new HeroicView(heroic, context);
        heroic.SetBoardView(heroicView.getBoardView());
        
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setARGB(255, 0, 255, 0);

        linePaintBad = new Paint();
        linePaintBad.setAntiAlias(true);
        linePaintBad.setARGB(255, 120, 180, 0);
    }

    /**
     * Starts the game, setting parameters for the current difficulty.
     */
    public void doStart() {
        synchronized (surfaceHolder) {
        	// START GAME

            timeLast = System.currentTimeMillis() + 100;
            setState(STATE_RUNNING);
            heroic.setNewGame();
        }
    }

    /**
     * Pauses the physics update & animation.
     */
    public void pause() {
        synchronized (surfaceHolder) {
            if (gameState == STATE_RUNNING) setState(STATE_PAUSE);
        }
    }

    /**
     * Restores game state from the indicated Bundle. Typically called when
     * the Activity is being restored after having been previously
     * destroyed.
     *
     * @param savedState Bundle containing the game state
     */
    public synchronized void restoreState(Bundle savedState) {
        synchronized (surfaceHolder) {
            setState(STATE_PAUSE);
            // Restore state from bundle         
        }
    }

    @Override
    public void run() {
    	Log.w(GameCore.LOG_TAG, "Thread start");
        while (!isOver) {
            Canvas c = null;
            try {
                c = surfaceHolder.lockCanvas(null);
                synchronized (surfaceHolder) {
                    if (gameState == STATE_RUNNING) 
                    	update();
                    draw(c);
                }
            } finally {
                // do this in a finally so that if an exception is thrown
                // during the above, we don't leave the Surface in an
                // inconsistent state
                if (c != null) {
                    surfaceHolder.unlockCanvasAndPost(c);
                }
            }
        }
        Log.w(GameCore.LOG_TAG, "Thread end");
    }

    /**
     * Dump game state to the provided Bundle. Typically called when the
     * Activity is being suspended.
     *
     * @return Bundle with this view's state
     */
    public Bundle saveState(Bundle map) {
        synchronized (surfaceHolder) {
            if (map != null) {
            	// save state
            }
        }
        return map;
    }

    /**
     * Sets the current difficulty.
     *
     * @param difficulty
     */
    public void setDifficulty(int difficulty) {
        synchronized (surfaceHolder) {
            // set difficulty
        }
    }

    /**
     * Used to signal the thread whether it should be running or not.
     * Passing true allows the thread to run; passing false will shut it
     * down if it's already running. Calling start() after this was most
     * recently called with false will result in an immediate shutdown.
     *
     * @param b true to run, false to shut down
     */
    public void setIsOver(boolean over) {
        isOver = over;
    }

    /**
     * Sets the game mode. That is, whether we are running, paused, in the
     * failure state, in the victory state, etc.
     *
     * @see #setState(int, CharSequence)
     * @param mode one of the STATE_* constants
     */
    public void setState(int mode) {
        synchronized (surfaceHolder) {
            setState(mode, null);
        }
    }

    /**
     * Sets the game mode. That is, whether we are running, paused, in the
     * failure state, in the victory state, etc.
     *
     * @param mode one of the STATE_* constants
     * @param message string to add to screen or null
     */
    public void setState(int mode, CharSequence message) {
        synchronized (surfaceHolder) {
            gameState = mode;
            Log.w(GameCore.LOG_TAG, "Set state " + mode);
            if (gameState == STATE_RUNNING) {
                //Message msg = handler.obtainMessage();
                //Bundle b = new Bundle();
                //msg.setData(b);
                //handler.sendMessage(msg);
            } else {
                //Message msg = handler.obtainMessage();
                //Bundle b = new Bundle();
                //msg.setData(b);
                //handler.sendMessage(msg);
            }
        }
    }

    public void setSurfaceSize(int width, int height) {
        // synchronized to make sure these all change atomically
        synchronized (surfaceHolder) {
            canvasWidth = width;
            canvasHeight = height;

            //mBackgroundImage = Bitmap.createScaledBitmap(
            //        mBackgroundImage, width, height, true);
        }
    }

    /**
     * Resumes from a pause.
     */
    public void unpause() {
        // Move the real time clock up to now
        synchronized (surfaceHolder) {
            timeLast = System.currentTimeMillis() + 100;
        }
        setState(STATE_RUNNING);
    }

    /**
     * Handles a key-down event.
     *
     * @param keyCode the key that was pressed
     * @param msg the original event object
     * @return true
     */
    boolean doKeyDown(int keyCode, KeyEvent msg) {
        synchronized (surfaceHolder) {
            boolean okStart = false;
            if (keyCode == KeyEvent.KEYCODE_DPAD_UP) okStart = true;
            if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) okStart = true;
            if (keyCode == KeyEvent.KEYCODE_S) okStart = true;

            if (okStart
                    && (gameState == STATE_READY || gameState == STATE_LOSE || gameState == STATE_WIN)) {
                // ready-to-start -> start
                doStart();
                return true;
            } else if (gameState == STATE_PAUSE && okStart) {
                // paused -> running
                unpause();
                return true;
            } else if (gameState == STATE_RUNNING) {
                
            }

            return false;
        }
    }

    /**
     * Handles a key-up event.
     *
     * @param keyCode the key that was pressed
     * @param msg the original event object
     * @return true if the key was handled and consumed, or else false
     */
    boolean doKeyUp(int keyCode, KeyEvent msg) {
        boolean handled = false;

        synchronized (surfaceHolder) {
            if (gameState == STATE_RUNNING) {

            }
        }

        return handled;
    }

    public boolean onTouchEvent(MotionEvent event)
    {
    	//Log.w(GameCore.LOG_TAG, "onTouchEvent " + event.getX() + "; " + event.getY());
    	
    	return heroicView.onTouchEvent(event);
    }
    /**
     * Draws the ship, fuel/speed bars, and background to the provided
     * Canvas.
     */
    private void draw(Canvas canvas) {
        heroicView.draw(canvas);
    }

    private void update() {
        long now = System.currentTimeMillis();

        if (timeLast > now) return;

        double elapsed = (now - timeLast) / 1000.0;
        heroic.update(elapsed);
        timeLast = now;
    }
}
