package nf.co.av_club.youtubevisualiser2;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends Activity {

    private GLSurfaceView mGLView;
    Thread getterThread;


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        //mGLView = new MyGLSurfaceView(this);
        setContentView(R.layout.activity_main);
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

    public void start(View v){
        EditText editText = (EditText) findViewById(R.id.EditText);
        String key = editText.getText().toString();

        getterThread = new Thread(new Get(key));
        getterThread.start();
    }
}
