package nf.co.av_club.youtubevisualiser2;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;

/**
 * Provides drawing instructions for a GLSurfaceView object. This class
 * must override the OpenGL ES drawing lifecycle methods:
 * <ul>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceCreated}</li>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onDrawFrame}</li>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceChanged}</li>
 * </ul>
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "MyGLRenderer";

    private Square   mSquare;
    private Texture texture;
    private static  Context context;
    private static ArrayList<VideoPreview> q;
    private static ArrayList<VideoPreview> currDraw;

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];

    private final float[] translator = new float[16];

    private float mAngle;
    private boolean newVideo = true;
    private boolean firstRun = true;
    private boolean pop4Videos = true;
    private boolean newRow = true;
    private boolean delt;
    private float z = 0f;
    private boolean currVideoTotallyLoaded = false;
    private boolean currVideoCompleted = false;

    public MyGLRenderer(Context context) {
        q = new ArrayList<VideoPreview>();
        currDraw = new ArrayList<VideoPreview>();

        this.context = context;
    }

    private static GLText glText;

    private int width;
    private int height;

    public boolean down = false;

    static Video currVideo;
    Texture tex;

    float[] mModel = new float[16];

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        mSquare   = new Square();

        // Create the GLText
        glText = new GLText(context.getAssets());

        // Load the font from file (set size + padding), creates the texture
        // NOTE: after a successful call to this the font is ready for rendering!
        glText.load( "Roboto-Regular.ttf", 80, 2, 2 );  // Create Font (Height: 14 Pixels / X+Y Padding 2 Pixels)

        // enable texture + alpha blending
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);

    }

    @Override
    public void onDrawFrame(GL10 unused) {
        float[] scratch = new float[16];

        Matrix.setIdentityM(scratch, 0);
        Matrix.scaleM(scratch, 0, 1.0f, 0.75f, 1.0f);

        mMVPMatrix = new float[16];
        Matrix.setIdentityM(mMVPMatrix, 0);
        //Matrix.multiplyMM(mMVPMatrix, 0, mMVPMatrix, 0, scratch, 0);
        Matrix.scaleM(mMVPMatrix, 0, 1.0f, 0.25f, 0.0f);


        if(currVideo != null && currVideo.previewLoaded() && !currVideoCompleted){
            try {
                Bitmap current = currVideo.getBitmap();

                tex = new Texture(context, current);
            } catch (IOException e) {
                e.printStackTrace();
            }

            currVideoCompleted = true;
        }

        if(currVideoCompleted) {
            tex.draw(mMVPMatrix);
        }
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        this.width = width;
        this.height = height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
                                     //         l       r     b  t  n  f
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);

    }

    /**
     * Utility method for compiling a OpenGL shader.
     *
     * <p><strong>Note:</strong> When developing shaders, use the checkGlError()
     * method to debug shader coding errors.</p>
     *
     * @param type - Vertex or fragment shader type.
     * @param shaderCode - String containing the shader code.
     * @return - Returns an id for the shader.
     */
    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    /**
     * Utility method for debugging OpenGL calls. Provide the name of the call
     * just after making it:
     *
     * <pre>
     * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
     * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
     *
     * If the operation is not successful, the check throws an error.
     *
     * @param glOperation - Name of the OpenGL call to check.
     */
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }

    public static void setRelated(ArrayList<String[]> set) throws IOException {
        //Log.i("Tag", Thread.currentThread().getName());

        String[] currVideoProperties = (String[]) set.remove(0); // the first video in this list is the video to be played aka main video, check video.java
        currVideo = new Video(currVideoProperties[0], currVideoProperties[1], 0.0f, 0.0f, 0.0f);

        currVideo.downloadPreviewPhoto();

    }



    public void scanButtons(float x, float y) {
        Log.i("Touch", Thread.currentThread().getName());
        for(VideoPreview currVp : currDraw){
            //currVp.buttonQuery(x, y);
        }
    }

}