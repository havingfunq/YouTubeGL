package nf.co.av_club.youtubevisualiser2;

import android.graphics.Bitmap;

/**
 * Created by VF on 11/4/2016.
 */
public class Video {
    float x;
    float y;
    float z;
    String key;
    String title;
    GetPhoto gp;

    public Video(String key, String title, float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
        this.key = key;
        this.title = title;
    }

    public void downloadPreviewPhoto(){
        gp = new GetPhoto(key);
        gp.run();
    }

    public boolean previewLoaded(){
        return gp.previewLoaded();
    }

    public Bitmap getBitmap(){
        return gp.getLoadedBitmap();
    }
}
