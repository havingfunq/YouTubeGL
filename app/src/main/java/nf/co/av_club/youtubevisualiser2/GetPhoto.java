package nf.co.av_club.youtubevisualiser2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * Created by VF on 11/4/2016.
 */
public class GetPhoto implements Runnable {
    String key;
    Bitmap bmp;
    public boolean previewLoaded = false;

    public GetPhoto(String key){
        this.key = key;
    }

    public void run(){
        URL url = null;
        try {
            url = new URL("https://i.ytimg.com/vi/" + key + "/0.jpg");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            previewLoaded = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Bitmap getLoadedBitmap(){
        return bmp;
    }

    public boolean previewLoaded(){
        return previewLoaded;
    }
}
