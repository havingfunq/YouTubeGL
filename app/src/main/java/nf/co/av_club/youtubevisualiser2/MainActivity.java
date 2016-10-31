package nf.co.av_club.youtubevisualiser2;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends Activity {

    private GLSurfaceView mGLView;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        mGLView = new MyGLSurfaceView(this);
        setContentView(mGLView);

        Thread getterThread = new Thread(new GetterThread(this, mGLView));
        getterThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // The following call pauses the rendering thread.
        // If your OpenGL application is memory intensive,
        // you should consider de-allocating objects that
        // consume significant memory here.



        if(((MyGLSurfaceView)findViewById(R.id.surfaceviewclass)) != null) {
            mGLView = (MyGLSurfaceView)findViewById(R.id.surfaceviewclass);
        }

        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // The following call resumes a paused rendering thread.
        // If you de-allocated graphic objects for onPause()
        // this is a good place to re-allocate them.
        if(((MyGLSurfaceView)findViewById(R.id.surfaceviewclass)) != null){
            mGLView = (MyGLSurfaceView)findViewById(R.id.surfaceviewclass);
        }

        mGLView.onResume();
    }

}
