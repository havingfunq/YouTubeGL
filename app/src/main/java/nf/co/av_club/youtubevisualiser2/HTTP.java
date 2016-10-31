package nf.co.av_club.youtubevisualiser2;

/**
 * Created by VF on 10/27/2016.
 */

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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
import java.net.UnknownHostException;
import java.util.Iterator;

/**
 * Created by VF on 10/15/2016.
 */
public class HTTP {
    Context context;

    public HTTP(Context context, GetterThread gt) {

    }

    public void get(final String url) throws IOException {
        try
        {
            // open a socket
            Socket socket = openSocket("www.google.com", 80);

            // write-to, and read-from the socket.
            // in this case just write a simple command to a web server.
            String result = writeToAndReadFromSocket(socket, "GET / HTTP/1.1\r\n\r\n");

            // print out the result we got back from the server
            //System.out.println(result);
            Log.i("HTTP", result);

            // close the socket, and we're done
            socket.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

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