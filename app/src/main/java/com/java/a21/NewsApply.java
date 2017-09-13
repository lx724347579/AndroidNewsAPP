package com.java.a21;
import android.os.Bundle;
import android.util.Log;

import java.util.*;
import java.io.*;
import java.net.*;
import org.json.*;

public class NewsApply {
    List<Map<String, Object>> newslist = new ArrayList<Map<String, Object>>();
    boolean finished = false;

    public void getData(int page,int cate) {

        finished = false;
        Log.d("get",String.valueOf(page));
        if(new File("/data/data/com.java.a21/files/" + String.valueOf(cate) + "/id" + String.valueOf(page*20 - 1)).exists()) {

            Map<String, Object> map = null;
            String title = new String();
            String info = new String();
            String id = new String();
            for(int i = page * 20 - 20; i < page * 20; i++) {
                map = new HashMap();
                title = readwrite.read("/data/data/com.java.a21/files/" + String.valueOf(cate) + "/title" + String.valueOf(i));
                info = readwrite.read("/data/data/com.java.a21/files/" + String.valueOf(cate) + "/info" + String.valueOf(i));
                id = readwrite.read("/data/data/com.java.a21/files/" + String.valueOf(cate) + "/id" + String.valueOf(i));
                map.put("title", title);
                map.put("info", info);
                map.put("id", id);
                map.put("img", "/data/data/com.java.a21/files/" + String.valueOf(cate) + "/img" + String.valueOf(i) + ".jpg");
                newslist.add(map);
            }
            finished = true;
        }

        else {
            String target_url = "http://166.111.68.66:2042/news/action/query/latest?pageNo=";
            target_url += String.valueOf(page);

            if(cate > 0 ){
                target_url += "&category=" + String.valueOf(cate);
            }
            final String finalTarget_url = target_url;

            Thread thread = new Thread() {

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
                        Log.d("get",inputLine);
                        JSONObject jsonobj = new JSONObject(inputLine);
                        JSONArray jsonarray = jsonobj.getJSONArray("list");

                        for (int i = 0; i < jsonarray.length(); i++) {

                            News tmpnews = new News(jsonarray.getJSONObject(i));
                            map = new HashMap<String, Object>();
                            map.put("title", tmpnews.Title);
                            map.put("info", tmpnews.Intro);
                            map.put("id", tmpnews.ID);
                            Log.d("get",tmpnews.Title);
                            Log.d("get",String.valueOf(finished));
                            if (tmpnews.Pictures.contains(";"))
                                map.put("img", tmpnews.Pictures.split(";")[0]);
                            else
                                map.put("img", tmpnews.Pictures);
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
    }
    Bundle getNewsById(int id) {
        Bundle map = new Bundle();
        map.putString("title", (String)newslist.get(id).get("title"));
        map.putString("content",  (String)newslist.get(id).get("info"));
        return map;
    }

}

