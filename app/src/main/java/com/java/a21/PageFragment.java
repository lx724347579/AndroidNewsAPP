package com.java.a21;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.java.a21.R;

/**
 * Created by Administrator on 2015/7/30.
 */
public class PageFragment extends Fragment {

    private String mtype;
    private int requestcode = 1500;
    int pageno = 1;
    Context context;
    private MyAdapter adapter;
    private NewsApply newsapply;
    private ImgApply imgapply;
    PullToRefreshListView newsview;

    public static PageFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString("type", type);
        PageFragment pageFragment = new PageFragment();
        pageFragment.setArguments(args);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mtype = getArguments().getString("type");
        context = this.getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);

        //setContentView(R.layout.activity_main);
        newsview = (PullToRefreshListView) view.findViewById(R.id.newsview);
        adapter = new MyAdapter(this.getActivity());
        newsapply = new NewsApply();
        imgapply = new ImgApply();
        SpeechUtility.createUtility(this.getActivity(), SpeechConstant.APPID + "=59a78bf9");
        //TODO LOAD THE DATA

        newsapply.getData(pageno);
        while(true) {
            if(newsapply.finished)
                break;
        }

        newsview.setAdapter(adapter);


        newsview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getActivity(), ContentActivity.class);
                intent.putExtra("id",(String)newsapply.newslist.get(i-1).get("id"));
                Log.d("ac",(String)newsapply.newslist.get(i).get("id"));
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

                newsapply.getData(++pageno);

                newsview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        newsview.onRefreshComplete();
                    }
                }, 1000);
            }
        });
        return view;
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
            if(imgurl.length() <= 0)
                holder.img.setImageResource(R.drawable.timg);
            else
                imgapply.showImageAsyn(holder.img,imgurl,R.drawable.timg);

            holder.title.setText((String)newsapply.newslist.get(position).get("title"));
            holder.info.setText((String)newsapply.newslist.get(position).get("info"));
            return convertView;
        }

    }

}