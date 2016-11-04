package nf.co.av_club.youtubevisualiser2;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by VF on 11/3/2016.
 */
public class Get implements Runnable {
    private final String url;

    public Get(String url){
        this.url = url;

    }

    public void run(){
        try
        {
            // open a socket
            Socket socket = openSocket("ytphp-colonel.c9.io", 80);

            // write-to, and read-from the socket.
            // in this case just write a simple command to a web server.
            String response = writeToAndReadFromSocket(socket, "GET " + "/yt.php?videoKey=" + url + " HTTP/1.1\r\n"+
                    "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\r\n" +
                    //"Accept-Encoding: gzip, deflate, sdch, br\r\n" +
                    //"Accept-Language: en-US,en;q=0.8\r\n" +
                    "Cache-Control: max-age=0\r\n" +
                    "Connection: keep-alive\r\n" +
                    "Keep-Alive: 300\r\n" +
                    "Host: ytphp-colonel.c9.io\r\n" +
                    "Upgrade-Insecure-Requests: 1\r\n" +
                    "User-Agent: Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.76 Mobile Safari/537.36\r\n" +
                    "\r\n");

            Log.i("HTTP", response);

            String result = (response.split("\n\n\n",2))[1];

            Log.i("HTTP", result);

            // close the socket, and we're done
            socket.close();

            try {
                JSONObject obj = new JSONObject(result);
                Iterator<String> str = obj.keys();

                final ArrayList<String[]> set = new ArrayList<String[]>();

                while(str.hasNext()){
                    final String key = str.next();
                    final String value = obj.getString(key);

                    String tmp[] = {key, value};

                    set.add(tmp);

                    Log.i("json", "key: " + key + ", " + "value: " + value);
                }


                MyGLSurfaceView glView = MyGLSurfaceView.getInstance();
                glView.queueEvent(new Runnable(){
                    @Override
                    public void run() {
                        try {
                            MyGLRenderer.setRelated(set);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @NonNull
    private String writeToAndReadFromSocket(Socket socket, String writeTo) throws Exception
    {
        try
        {
            // write text to the socket
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedWriter.write(writeTo);
            bufferedWriter.flush();

            // read text from the socket
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String str;
            while ((str = bufferedReader.readLine()) != null)
            {
                sb.append(str + "\n");
            }

            // close the reader, and return the results as a String
            bufferedReader.close();
            return sb.toString();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Open a socket connection to the given server on the given port.
     * This method currently sets the socket timeout value to 10 seconds.
     * (A second version of this method could allow the user to specify this timeout.)
     */
    private Socket openSocket(String server, int port) throws Exception
    {
        Socket socket;

        // create a socket with a timeout
        try
        {
            InetAddress inteAddress = InetAddress.getByName(server);
            SocketAddress socketAddress = new InetSocketAddress(inteAddress, port);

            // create a socket
            socket = new Socket();

            // this method will block no more than timeout ms.
            int timeoutInMs = 10*1000;   // 10 seconds
            socket.connect(socketAddress, timeoutInMs);

            return socket;
        }
        catch (SocketTimeoutException ste)
        {
            System.err.println("Timed out waiting for the socket.");
            ste.printStackTrace();
            throw ste;
        }
    }
}
