package cz.ic.resurrection.heroic;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class GameView extends SurfaceView implements SurfaceHolder.Callback {
    class GameThread extends Thread {
        public static final int STATE_LOSE = 1;
        public static final int STATE_PAUSE = 2;
        public static final int STATE_READY = 3;
        public static final int STATE_RUNNING = 4;
        public static final int STATE_WIN = 5;
        private int gameState;
        
        private Bitmap imageBoard;

        private int canvasHeight = 1;
        private int canvasWidth = 1;

        private Handler handler;
        private Context gameContext;

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
            this.handler = handler;
            gameContext = context;
 
            Resources res = context.getResources();

            //mLanderImage = context.getResources().getDrawable(
            //        R.drawable.lander_plain);

            imageBoard = BitmapFactory.decodeResource(res,
                    R.drawable.board_marble);

            //mLanderWidth = mLanderImage.getIntrinsicWidth();
            //mLanderHeight = mLanderImage.getIntrinsicHeight();

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
        	Log.w(GameCore.LOG_TAG, "onTouchEvent " + event.getX() + "; " + event.getY());
        	return false;
        }
        /**
         * Draws the ship, fuel/speed bars, and background to the provided
         * Canvas.
         */
        private void draw(Canvas canvas) {
            // Draw the background image. Operations on the Canvas accumulate
            // so this is like clearing the screen.
            
        	canvas.drawBitmap(imageBoard, 
        					  0, 
        					  (canvasHeight - imageBoard.getHeight()) / 2, null);

            //canvas.save();
            //canvas.rotate();
            //canvas.restore();
        }

        private void update() {
            long now = System.currentTimeMillis();

            if (timeLast > now) return;

            //double elapsed = (now - mLastTime) / 1000.0;

            timeLast = now;
            
            /* if game over    int result = STATE_LOSE;

               if win        result = STATE_WIN;   doStart();    
               setState(result, message); 
            */
            
        }
    }

    private GameThread thread;
    
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        Log.w(GameCore.LOG_TAG, "GameView is created");
        
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        
        thread = null;
        
        setFocusable(true); // make sure we get key events
    }

    /**
     * Fetches the animation thread corresponding to this LunarView.
     *
     * @return the animation thread
     */
    public GameThread getThread() {
    	if(thread == null)
    	{
    		thread = new GameThread(getHolder(), getContext(), new Handler() {
                @Override
                public void handleMessage(Message m) {
                }
            });
    	}
        return thread;
    }

    /**
     * Standard override to get key-press events.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent msg) {
        return thread.doKeyDown(keyCode, msg);
    }
    
    /**
     * Standard override for key-up. We actually care about these, so we can
     * turn off the engine or stop rotating.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent msg) {
        return thread.doKeyUp(keyCode, msg);
    }

    public boolean onTouchEvent(MotionEvent event)
    {
    	return thread.onTouchEvent(event);
    }
    
    /**
     * Standard window-focus override. Notice focus lost so we can pause on
     * focus lost. e.g. user switches to take a call.
     */
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
    	Log.w(GameCore.LOG_TAG, "Focus changed");
        if (!hasWindowFocus && thread != null) thread.pause();
    }

    /* Callback invoked when the surface dimensions change. */
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
        thread.setSurfaceSize(width, height);
    }

    /*
     * invoked when the Surface has been created and is ready to be
     * used.
     */
    public void surfaceCreated(SurfaceHolder holder) { 
    	Log.w(GameCore.LOG_TAG, "Surface created");
    	getThread();
        thread.setIsOver(false);
        thread.start();
    }

    /*
     * Callback invoked when the Surface has been destroyed and must no longer
     * be touched. WARNING: after this method returns, the Surface/Canvas must
     * never be touched again!
     */
    public void surfaceDestroyed(SurfaceHolder holder) {
    	Log.w(this.getClass().getName(), "Surface destroyed");
    	
        boolean retry = true;
        thread.setIsOver(true);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            	Log.e(GameCore.LOG_TAG, e.getMessage());
            }
        }
        thread = null;
    }
}
