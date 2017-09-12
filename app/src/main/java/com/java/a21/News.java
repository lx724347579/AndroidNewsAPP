package com.java.a21;

import java.util.HashMap;
import java.util.Map;
import org.json.*;

/**
 * Created by Kyle on 2017/9/10.
 */

public class News {
        String ClassTag = null, ID = null, Source = null;
        String Title = null, Content = null, News_time = null, News_url = null, Author = null,lang_type = null;
        String Pictures = null, Video = null, Intro = null, Category = null, Keyword = null, Crawl_source = null, Jounral = null;
        String Crawl_time = null, repeat_id = null;

        News(JSONObject jarry) {
            try {

                ClassTag = jarry.getString("newsClassTag");
                ID = jarry.getString("news_ID");
                Source = jarry.getString("news_Source");
                Title = jarry.getString("news_Title");
                News_time = jarry.getString("news_Time");
                News_url = jarry.getString("news_URL");
                Author = jarry.getString("news_Author");
                lang_type = jarry.getString("lang_Type");
                Pictures = jarry.getString("news_Pictures");
                Video = jarry.getString("news_Video");
                if(jarry.has("news_Intro"))
                    Intro = jarry.getString("news_Intro");
                if(jarry.has("news_Content"))
                    Content = jarry.getString("news_Content");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

}
