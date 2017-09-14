package com.java.a21;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Kyle on 2017/9/11.
 */

public class GetNewsContent {
    public News news = null;
    boolean finished;

    public void getData(String url) {
        final String finalTarget_url = url;
        finished = false;
        Thread thread = new Thread() {

            public void run() {
                JSONObject jsonobj = null;

                try {
                    URL cs = null;
                    HttpURLConnection connection = null;
                    try {

                        cs = new URL(finalTarget_url);
                        connection = (HttpURLConnection) cs.openConnection();
                        connection.setRequestMethod("GET");

                        connection.setConnectTimeout(1000);
                        connection.connect();

                    } catch (MalformedURLException e1) {
                        // TODO Auto-generated catch block
                    }

                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    inputLine = in.readLine();
                    jsonobj = new JSONObject(inputLine);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                }
                news = new News(jsonobj);
                finished = true;

            }
        };
        thread.start();
    }
}
