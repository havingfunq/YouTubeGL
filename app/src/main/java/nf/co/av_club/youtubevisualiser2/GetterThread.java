package nf.co.av_club.youtubevisualiser2;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by VF on 10/27/2016.
 */
public class GetterThread implements Runnable {
    Thread runner;
    Context context;
    HTTP http;
    GLSurfaceView glview;
    static ArrayList<String> urlQueue;

    public GetterThread(Context context, GLSurfaceView mGLView){
        this.context = context;

        http = new HTTP(context, this);
        glview = mGLView;

        urlQueue = new ArrayList<String>();
        urlQueue.add("B7c4JypIQyI");

        runner = new Thread(this, "GetterThread");
        runner.start();


    }

    public void run() {


        while (true) {
            //String videoUrlKey;

            //videoUrlKey = urlQueue.remove(0);

            try {
                http.get("https://ytphp-colonel.c9.io/yt.php?videoKey=" + "");
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(20000);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }


//            synchronized (this) {
//                try {
//                    this.wait();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
        }


    }

    public void setJsonResponse(String response) {
        try {
            JSONObject obj = new JSONObject(response);
            Iterator<String> str = obj.keys();

            final ArrayList<String[]> set = new ArrayList<String[]>();

            while(str.hasNext()){
                final String key = str.next();
                final String value = obj.getString(key);

                boolean skip = false;

                for(String s : urlQueue){
                    if( s.compareTo( key ) == 0  ){
                        skip = true;
                        break;
                    }
                }

                if(skip){
                    continue;
                }

                String tmp[] = {key, value};

                set.add(tmp);

                urlQueue.add(key);

                Log.i("json", "key: " + key + ", " + "value: " + value);
            }

            glview.queueEvent(new Runnable(){
                @Override
                public void run() {
                    try {
                        MyGLRenderer.addVideo(set);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

}
