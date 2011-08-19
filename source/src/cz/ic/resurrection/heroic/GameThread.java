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
    private int canvasHeight = 1;
    private int canvasWidth = 1;
    
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
        
        heroic = new Heroic(handler, context);
        heroicView = new HeroicView(heroic, context);
        heroic.setBoardView(heroicView.getBoardView());
        
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
    public void doStart(int level) {
        synchronized (surfaceHolder) {
        	// START GAME

            timeLast = System.currentTimeMillis() + 100;
            heroic.setNewGame(level);
        }
    }

    public void pause() {
        synchronized (surfaceHolder) {
            
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
        	heroic.restoreState(savedState);
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
                    update();
                    draw(c);
                }
            } catch(Exception e)
            {
            	Log.e(GameCore.LOG_TAG, e.getMessage());
            	Log.e(GameCore.LOG_TAG, e.toString());
            }
            finally {
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
            	heroic.saveState(map);
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

        }
    }

    public void setSurfaceSize(int width, int height) {
        // synchronized to make sure these all change atomically
        synchronized (surfaceHolder) {
            canvasWidth = width;
            canvasHeight = height;

            heroicView.setCanvasSize(canvasWidth, canvasHeight);
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
