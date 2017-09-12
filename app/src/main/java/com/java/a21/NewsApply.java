package com.java.a21;
import android.os.Bundle;

import java.util.*;
import java.io.*;
import java.net.*;
import org.json.*;

public class NewsApply {
    List<Map<String, Object>> newslist = new ArrayList<Map<String, Object>>();
    boolean finished;

    public void getData(int page) {
        finished = false;
        String target_url = "http://166.111.68.66:2042/news/action/query/latest?pageNo=";
        target_url += String.valueOf(page);
        final String finalTarget_url = target_url;
        Thread thread = new Thread(){

            public void run() {

                Map<String, Object> map = null;
                try {
                    URL cs = null;
                    HttpURLConnection connection = null;
                    try {

                        cs = new URL(finalTarget_url);
                        connection = (HttpURLConnection) cs.openConnection();
                        connection.setRequestMethod("GET");
                        connection.connect();

                    } catch (MalformedURLException e1) {
                        // TODO Auto-generated catch block
                    }

                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    inputLine = in.readLine();
                    JSONObject jsonobj = new JSONObject(inputLine);
                    JSONArray jsonarray = jsonobj.getJSONArray("list");

                    for (int i = 0; i < jsonarray.length(); i++) {
                        News tmpnews = new News(jsonarray.getJSONObject(i));

                        map = new HashMap<String, Object>();
                        map.put("title", tmpnews.Title);
                        map.put("info", tmpnews.Intro);
                        map.put("id",tmpnews.ID);
                        if(tmpnews.Pictures.contains(";"))
                            map.put("img", tmpnews.Pictures.split(";")[0]);
                        else
                            map.put("img", tmpnews.Pictures);

                        //map.put("img","11");
                        newslist.add(map);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                }
                finished = true;
           }
       };


        thread.start();
    }
    Bundle getNewsById(int id) {
        Bundle map = new Bundle();
        map.putString("title", (String)newslist.get(id).get("title"));
        map.putString("content",  (String)newslist.get(id).get("info"));
        return map;
    }

}

