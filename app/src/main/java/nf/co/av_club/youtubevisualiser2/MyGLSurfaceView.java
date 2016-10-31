package nf.co.av_club.youtubevisualiser2;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.io.IOException;

/**
 * Created by VF on 10/25/2016.
 */
class MyGLSurfaceView extends GLSurfaceView {
    private boolean down = false;

    private final MyGLRenderer mRenderer;
///////////////////////////////////

    //programmatic instantiation
    public MyGLSurfaceView(Context context)
    {
        this(context, null);
    }

    //XML inflation/instantiation
    public MyGLSurfaceView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public MyGLSurfaceView(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        // Set the Renderer for drawing on the GLSurfaceView

        mRenderer = new MyGLRenderer(context);

        setRenderer(mRenderer);

    }


    ////////////////////
//
//    public MyGLSurfaceView(Context context) {
//        super(context);
//
//        // Create an OpenGL ES 2.0 context.
//        setEGLContextClientVersion(2);
//
//        // Set the Renderer for drawing on the GLSurfaceView
//        mRenderer = new MyGLRenderer(context);
//        setRenderer(mRenderer);
//
//    }

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX;
    private float mPreviousY;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        final float x = e.getX();
        final float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:

            case MotionEvent.ACTION_DOWN:
                // Write your code to perform an action on down
                mRenderer.down = true;

                this.queueEvent(new Runnable(){
                    @Override
                    public void run() {

                            mRenderer.scanButtons(x, y);

                    }
                });


                break;

            case MotionEvent.ACTION_UP:
                // Write your code to perform an action on touch up
                mRenderer.down = false;
                break;
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }
}