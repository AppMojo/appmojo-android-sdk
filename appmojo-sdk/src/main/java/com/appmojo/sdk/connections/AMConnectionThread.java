package com.appmojo.sdk.connections;

import com.appmojo.sdk.AMConnectionResponse;
import com.appmojo.sdk.utils.AMLog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by nutron on 8/5/15 AD.
 */
public class AMConnectionThread implements Runnable {

    public static final int CONNECT_STATE_COMPLETED = 1;

    private AMConnectionTask connectionTask;

    public AMConnectionThread(AMConnectionTask task){
        this.connectionTask = task;
    }


    @Override
    public void run() {
        AMConnectionResponse response = makeConnection(connectionTask.getUrlStr(),
                connectionTask.getMethod(), connectionTask.getHeaders(), connectionTask.getBody());
        connectionTask.setConnectionResponse(response);
        connectionTask.handleConnectionState(CONNECT_STATE_COMPLETED);
    }


    private AMConnectionResponse makeConnection(String urlStr, String method, Map<String, String> headers, String jBody) {
        String response = null;
        int statusCode = -1;
        URL url;
        URLConnection conn = null;
        try {
            url = new URL(urlStr);
            conn = url.openConnection();
            if(urlStr.toLowerCase(Locale.US).startsWith("http://")) {
                ((HttpURLConnection)conn).setRequestMethod(method);
                conn = prepareHeader(conn, headers);
                conn = prepareBody(conn, jBody);

                statusCode = ((HttpURLConnection)conn).getResponseCode();
                if(statusCode == HttpURLConnection.HTTP_OK) {
                    response = readContent(conn.getInputStream());
                } else {
                    response = readContent(
                            ((HttpURLConnection) conn).getErrorStream());
                }

            } else {
                ((HttpsURLConnection)conn).setRequestMethod(method);
                conn = prepareHeader(conn, headers);
                conn = prepareBody(conn, jBody);

                statusCode = ((HttpsURLConnection)conn).getResponseCode();
                if(statusCode == HttpsURLConnection.HTTP_OK) {
                    response = readContent(conn.getInputStream());
                } else {
                    response = readContent(((HttpsURLConnection) conn).getErrorStream());
                }
            }
        } catch (MalformedURLException e) {
            AMLog.d("Make connection failed, MalformedURLException.", e);
        } catch (IOException e) {
            AMLog.d("Open connection failed, IOException.", e);
        } catch (Exception e) {
            AMLog.d("Connection failed, Exception.", e);
        } finally {
            if(conn != null) {
                if(conn instanceof HttpsURLConnection) {
                    ((HttpsURLConnection)conn).disconnect();
                } else {
                    ((HttpURLConnection)conn).disconnect();
                }
            }
        }
        return new AMConnectionResponse(statusCode, response);
    }


    public URLConnection prepareHeader(URLConnection conn, Map<String, String> headers) {
        if(headers == null) {
            return conn;
        }

        Set<String> keys = headers.keySet();
        for (String key : keys) {
            String value = headers.get(key);
            conn.setRequestProperty(key, value);
        }
        return conn;
    }
    

    public URLConnection prepareBody(URLConnection conn, String body) {
        if(body == null) {
            return conn;
        }

        try {
            byte[] outputInBytes = body.getBytes("UTF-8");
            OutputStream os = conn.getOutputStream();
            os.write( outputInBytes );
            os.close();
        } catch (IOException e) {
            AMLog.d("Prepare connection failed, IOException.", e);
        }
        return conn;
    }


    public String readContent(InputStream is){
        String response = null;
        if(is!=null){
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String input;
                StringBuilder builder = new StringBuilder();
                while ((input = br.readLine()) != null){
                    builder.append(input);
                }
                br.close();
                response = builder.toString();
            } catch (IOException e) {
                AMLog.d("Read content failed, IOException.", e);
            }
        }
        return response;
    }
}
