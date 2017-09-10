package com.example.cdogemaru.javahw;
import android.os.Bundle;

import java.util.*;
import java.io.*;
import java.net.*;
import org.json.*;

public class NewsApply {
    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    public List<Map<String, Object>> getData() {

//        Thread thread = new Thread(){
//            public void run(){
//
//                Map<String, Object> map = new HashMap<String, Object>();
//                try {
//                    URL cs = null;
//                    try {
//                        cs = new URL("http://166.111.68.66:2042/news/action/query/latest");
//                    } catch (MalformedURLException e1) {
//                        // TODO Auto-generated catch block
//                        e1.printStackTrace();
//                    }
//                    BufferedReader in = new BufferedReader(new InputStreamReader(cs.openStream()));
//                    String inputLine;
//                    inputLine = in.readLine();
//                    System.out.println(inputLine);
//                    JSONObject jsonobj = new JSONObject(inputLine);
//                    JSONArray jsonarray = jsonobj.getJSONArray("list");
//
//                    //for(int i = 0; i < 10; i ++) {
//                    for (int i=0;i<jsonarray.length();i++){
//                        String newsClassTag = jsonarray.getJSONObject(i).getString("newsClassTag");
//                        String news_ID = jsonarray.getJSONObject(i).getString("news_ID");
//                        String news_Title = jsonarray.getJSONObject(i).getString("news_Title");
//                        String news_Intro = jsonarray.getJSONObject(i).getString("news_Intro");
//
//                        map = new HashMap<String, Object>();
//                        map.put("title",news_Title);
//                        map.put("info", news_Intro);
//                        map.put("img", R.drawable.i1);
//                        list.add(map);
//                    }
//                }catch (Exception e) {
//                    StackTraceElement[] st = e.getStackTrace();
//                    String tmp = e.getClass().getName();
//                    map = new HashMap<String, Object>();
//                    map.put("title",
//                            tmp);
//                    map.put("info", e.getMessage());
//                    map.put("img", R.drawable.i1);
//                    list.add(map);
//                    for (StackTraceElement stackTraceElement : st) {
//                        String sOut = "";
//                        String exclass = stackTraceElement.getClassName();
//                        String method = stackTraceElement.getMethodName();
//                        System.out.println(exclass);
//                        System.out.println(method);
//                        sOut += "\tat " + stackTraceElement + "\r\n";
//
//                        map = new HashMap<String, Object>();
//                        map.put("title",sOut);
//                        map.put("info", method);
//                        map.put("img", R.drawable.i1);
//                        list.add(map);
//                    }
//
//                }
//
//            }
//        };
//
//
//        thread.start();

        for(int i = 0; i < 20; i ++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", "G" + (i + 1));
            map.put("info", "google" + (i + 1));
            map.put("img", R.drawable.i1);
            map.put("id", (int) i + 1);
            list.add(map);
        }
        return list;
    }


    Bundle getNewsById(int id) {
        Bundle map = new Bundle();
        map.putString("title", "G" + (id));
        map.putString("content", "google" + (id));
        return map;
    }
}
