package com.example.cdogemaru.javahw;

import android.app.ListActivity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.*;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.*;
import java.util.*;
import android.view.*;
import android.content.*;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yalantis.phoenix.PullToRefreshView;

import butterknife.BindView;
import java.util.regex.*;
import java.io.*;
import java.net.*;
import org.json.*;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    //private List<Map<String, Object>> mData;
    private int requestcode = 1500;
    int pageno = 1;
    private MyAdapter adapter;
    private NewsApply kernel;
    private ImgApply imgapply;

    PullToRefreshListView newsview;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //mData = kernel.getData();
        setContentView(R.layout.activity_main);
        newsview = (PullToRefreshListView) findViewById(R.id.newsview);
        adapter = new MyAdapter(this);
        kernel = new NewsApply();
        imgapply = new ImgApply();
        //TODO LOAD THE DATA

        kernel.getData(pageno);
        while(true) {
            if(kernel.finished)
                break;
        }

        newsview.setAdapter(adapter);


        newsview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, ContentActivity.class);
                Bundle map = kernel.getNewsById(i-1);
                intent.putExtras(map);
                startActivityForResult(intent, requestcode);
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

                kernel.getData(++pageno);


                //newsview.getRefreshableView().setSelection(pageno*20 - 20);
                newsview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        newsview.onRefreshComplete();
                    }
                }, 200);
            }
        });
    }

    /**
     * 接收当前Activity跳转后，目标Activity关闭后的回传值
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        switch(resultCode){
//            case RESULT_OK:{//接收并显示Activity传过来的值
//                Bundle bundle = data.getExtras();
//                String rs = bundle.getString("rs");
//                tv_main_result.setText(rs);
//                break;
//            }
//            default:
//                break;
//        }
    }



    public final class ViewHolder{
        public ImageView img;
        public TextView title;
        public TextView info;
    }

    public class MyAdapter extends BaseAdapter{
        private LayoutInflater mInflater;

        public MyAdapter(Context context){
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return kernel.newslist.size();
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
            //holder.img.setImageBitmap((Bitmap)kernel.newslist.get(position).get("img"));


            //holder.img.setImageResource(R.drawable.timg);
            String imgurl = (String)kernel.newslist.get(position).get("img");
            if(imgurl.isEmpty())
                holder.img.setImageResource(R.drawable.timg);
            else
                imgapply.showImageAsyn(holder.img,imgurl,R.drawable.timg);

            holder.title.setText((String)kernel.newslist.get(position).get("title"));
            holder.info.setText((String)kernel.newslist.get(position).get("info"));
            return convertView;
        }

    }

}
