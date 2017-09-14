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

    public void getData(int page,int cate,String keyword) {

        finished = false;
        Log.d("search",String.valueOf(cate));

        if(keyword != null && keyword.length() > 0 ) {
            String target_url = "http://166.111.68.66:2042/news/action/query/search?pageNo=";
            target_url += String.valueOf(page);

            if (cate > 0) {
                target_url += "&category=" + String.valueOf(cate);
            }
            try {
                target_url += "&keyword=" + URLEncoder.encode(keyword,"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            final String finalTarget_url = target_url;
            Log.d("search",finalTarget_url);
            Thread thread = new Thread() {

                public void run() {
                    Map<String, Object> map = null;
                    try {
                        URL cs = null;
                        HttpURLConnection connection = null;
                        try {

                            cs = new URL(finalTarget_url);
                            Log.d("search",finalTarget_url);
                            connection = (HttpURLConnection) cs.openConnection();
                            connection.setRequestMethod("GET");
                            connection.connect();

                        } catch (MalformedURLException e1) {
                            // TODO Auto-generated catch block
                        }

                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String inputLine;
                        inputLine = in.readLine();

                        Log.d("search",inputLine);
                        JSONObject jsonobj = new JSONObject(inputLine);
                        JSONArray jsonarray = jsonobj.getJSONArray("list");

                        for (int i = 0; i < jsonarray.length(); i++) {

                            News tmpnews = new News(jsonarray.getJSONObject(i));
                            map = new HashMap<String, Object>();
                            map.put("title", tmpnews.Title);
                            map.put("info", tmpnews.Intro);
                            map.put("id", tmpnews.ID);
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

        else{
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
                    if(new File("/data/data/com.java.a21/files/" + String.valueOf(cate) + "/img" + String.valueOf(i) + ".jpg").exists())
                        map.put("img", "/data/data/com.java.a21/files/" + String.valueOf(cate) + "/img" + String.valueOf(i) + ".jpg");
                    else
                        map.put("img","");

                    newslist.add(map);
                }
                finished = true;
        }

        else {
            String target_url = "http://166.111.68.66:2042/news/action/query/latest?pageNo=";
            target_url += String.valueOf(page);

            if (cate > 0) {
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
                        JSONObject jsonobj = new JSONObject(inputLine);
                        JSONArray jsonarray = jsonobj.getJSONArray("list");

                        for (int i = 0; i < jsonarray.length(); i++) {

                            News tmpnews = new News(jsonarray.getJSONObject(i));
                            map = new HashMap<String, Object>();
                            map.put("title", tmpnews.Title);
                            map.put("info", tmpnews.Intro);
                            map.put("id", tmpnews.ID);
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
    }
    Bundle getNewsById(int id) {
        Bundle map = new Bundle();
        map.putString("title", (String)newslist.get(id).get("title"));
        map.putString("content",  (String)newslist.get(id).get("info"));
        return map;
    }

}

