package com.java.a21;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.java.a21.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by Administrator on 2015/7/30.
 */
public class PageFragment extends Fragment {

    private String mtype,keyword;
    private int requestcode = 1500;
    //int pageno = 1;
    int typeid = 0;
    Context context;
    private MyAdapter adapter;
    private NewsApply newsapply;
    private RequestOptions requestOptions;
    PullToRefreshListView newsview;
    String [] labels = {"全部","科技","教育","军事","国内","社会","文化","汽车","国际","体育","财经","健康","娱乐"};
    private ArrayList<Boolean> gray = new ArrayList<Boolean>();
    database db = null;
    public static PageFragment newInstance(String type,String keyword) {
        Bundle args = new Bundle();
        args.putString("type", type);
        args.putString("keyword", keyword);
        PageFragment pageFragment = new PageFragment();
        pageFragment.setArguments(args);

        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mtype = getArguments().getString("type");
        keyword = getArguments().getString("keyword");
        context = this.getActivity();
        Log.d("frag",keyword);
        for(int i = 0; i < 13; i++)
            if(mtype == labels[i])
                typeid = i;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("fuck" + mtype,"create");
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.timg);
        requestOptions.error(R.drawable.timg);
        //requestOptions.override(160,178);
        requestOptions.fitCenter();

        //setContentView(R.layout.activity_main);
        newsview = (PullToRefreshListView) view.findViewById(R.id.newsview);
        adapter = new MyAdapter(this.getActivity());
        newsapply = new NewsApply();
        SpeechUtility.createUtility(this.getActivity(), SpeechConstant.APPID + "=59b678fe");
        //TODO LOAD THE DATA

        newsapply.getData(1,typeid,keyword);
        while(true) {
            Log.d("ac","1");
            if(newsapply.finished)
                break;
        }
        for(int i = 0; i < 20; i++)
            gray.add(false);

        RefreshGray();
        newsview.setAdapter(adapter);
        if(keyword == null || keyword.length() == 0)
            SaveNewsList();


        newsview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("ac",String.valueOf(i));
                Intent intent = new Intent(getActivity(), ContentActivity.class);
                intent.putExtra("id",(String)newsapply.newslist.get(i-1).get("id"));
                intent.putExtra("intro",(String)newsapply.newslist.get(i-1).get("info"));


                Log.d("ac",(String)newsapply.newslist.get(i-1).get("id"));

                db = new database();
                if(db.querybyid((String)newsapply.newslist.get(i-1).get("id")).getCount() == 0)
                    db.insert((String)newsapply.newslist.get(i-1).get("id"));
                db = null;
                RefreshGray();
                adapter.notifyDataSetChanged();
                startActivity(intent);
            }
        });

        newsview.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        ILoadingLayout endLayout = newsview.getLoadingLayoutProxy(false,true);
        endLayout.setPullLabel("上拉刷新");
        endLayout.setRefreshingLabel("加载中");
        endLayout.setReleaseLabel("放开刷新");

        newsview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {}
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

                //TODO: REFRESH THE DATA

                newsapply.getData(newsapply.newslist.size()/20+1,typeid,keyword);

                for(int i = 0; i < 20; i++)
                    gray.add(false);
                newsview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        newsview.onRefreshComplete();
                        if(keyword == null || keyword.length() == 0)
                            SaveNewsList();
                    }
                }, 1000);
//                if(newsapply.finished == true)
//                    pageno++;
            }
        });
        return view;
    }

    private void RefreshGray()
    {
        db = new database();
        Cursor cursor = null;
        for(int i = 0; i < newsapply.newslist.size(); i++) {
            cursor = db.querybyid((String)newsapply.newslist.get(i).get("id"));
            Log.d("c",String.valueOf(i) + " " + String.valueOf(cursor.getCount()));
            if (cursor.getCount() == 0)
                gray.set(i, false);
            else
                gray.set(i, true);
        }
        db = null;
    }

    public final class ViewHolder{
        public ImageView img;
        public TextView title;
        public TextView info;
    }

    public class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public MyAdapter(Context context){
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return newsapply.newslist.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder=new ViewHolder();
                convertView = mInflater.inflate(R.layout.newsitem, null);
                holder.img = (ImageView)convertView.findViewById(R.id.img);
                holder.title = (TextView)convertView.findViewById(R.id.title);
                holder.info = (TextView)convertView.findViewById(R.id.info);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder)convertView.getTag();
            }
            //holder.img.setImageBitmap((Bitmap)newsapply.newslist.get(position).get("img"));


            //holder.img.setImageResource(R.drawable.timg);
            String imgurl = (String)newsapply.newslist.get(position).get("img");

//            holder.img.setImageResource(R.drawable.timg);
//            if(imgurl.length() <= 0);
//            else
            Glide.with(context).load(imgurl).apply(requestOptions).into(holder.img);

            //    imgapply.showImageAsyn(holder.img,imgurl,R.drawable.timg);

            holder.title.setText((String)newsapply.newslist.get(position).get("title"));
            holder.info.setText((String)newsapply.newslist.get(position).get("info"));
            if(gray.get(position))
            {
                holder.title.setTextColor(Color.GRAY);
                holder.info.setTextColor(Color.GRAY);
            }
            else
            {
                holder.title.setTextColor(Color.BLACK);
                holder.info.setTextColor(Color.BLACK);
            }

            return convertView;
        }

    }


    Bitmap tmpmap = null;
    File dir = null;
    int index = 0;
    private void SaveNewsList()
    {
        if(newsapply.newslist.size() == 0)
            return;
        String path = "/data/data/com.java.a21/files/" + typeid;
        dir = new File(path);
        dir.mkdirs();
        if(new File(dir + "/title" + String.valueOf(newsapply.newslist.size()-1)).exists())
            return;
        tmpmap = null;
        for(int i = newsapply.newslist.size() - 20; i < newsapply.newslist.size(); i++) {
            index = i;

            readwrite.save((String)newsapply.newslist.get(i).get("title"), dir + "/title" + String.valueOf(i));
            readwrite.save((String)newsapply.newslist.get(i).get("info"), dir + "/info" + String.valueOf(i));
            readwrite.save((String)newsapply.newslist.get(i).get("id"), dir + "/id" + String.valueOf(i));
            RequestBuilder<Bitmap> requestBuilder = Glide.with(context).asBitmap();
            Log.d("savenews",String.valueOf(i));
            Log.d("savenews",(String)newsapply.newslist.get(i).get("img"));


            requestBuilder.load((String) newsapply.newslist.get(i).get("img"));
            requestBuilder.into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                int in = index;
                @Override
                public void onResourceReady(Bitmap resource, Transition glideAnimation) {
                    //tmpmap = resource;
                    Log.d("savenews",resource.toString());
                    Log.d("savenews",String.valueOf(in));

                    readwrite.Saveimg(resource, dir + "/img" + String.valueOf(in) + ".jpg");

                }

            });
//            if(tmpmap != null) {
//                Log.d("savenews2",tmpmap.toString());
//                readwrite.Saveimg(tmpmap, dir + String.valueOf(i) + ".jpg");
//            }

        }
    }
}