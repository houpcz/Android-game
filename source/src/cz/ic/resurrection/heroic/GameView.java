package cz.ic.resurrection.heroic;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class GameView extends SurfaceView implements SurfaceHolder.Callback {
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
    
    public void createThread()
    {
    	thread = new GameThread(getHolder(), getContext(), new Handler() {
            @Override
            public void handleMessage(Message m) {
            }
        });
    }

    /*
     * invoked when the Surface has been created and is ready to be
     * used.
     */
    public void surfaceCreated(SurfaceHolder holder) { 
    	Log.w(GameCore.LOG_TAG, "Surface created");

    	thread.start();
        thread.setIsOver(false);
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
    }
}
