package com.example.cdogemaru.javahw;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.LineNumberReader;
import java.util.ArrayList;

import static java.io.FileDescriptor.in;

public class ContentActivity extends AppCompatActivity {
    private ListView listview;
    private ImgApply imgapply;

    private ArrayList<String>imagelist  = new ArrayList<String>();
    private String title, text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        imgapply = new ImgApply();
        imgapply.Resize = false;
        Intent intent = getIntent();
        GetNewsContent apply = new GetNewsContent();
        apply.getData("http://166.111.68.66:2042/news/action/query/detail?newsId=" + intent.getStringExtra("id"));
        while (true) {
            if (apply.finished)
                break;
        }

        title = apply.news.Title;
        text = apply.news.Content;
        String tmp = apply.news.Pictures;
        if(tmp.length() > 0)
            if (tmp.contains(";"))
            {
                String[] c = tmp.split(";");
                for (int i = 0; i < c.length; i++)
                    imagelist.add(c[i]);
            }
            else
                imagelist.add(tmp);

        listview = (ListView) findViewById(R.id.newslayout);
        MyAdapter adapter = new MyAdapter(this);
        listview.setAdapter(adapter);
    }

    public final class ViewHolder{
        public ImageView img;
        public TextView text;
    }

    public class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public MyAdapter(Context context){
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return imagelist.size() + 2;
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
            ContentActivity.ViewHolder holder = null;
            if (convertView == null) {
                holder=new ContentActivity.ViewHolder();
                convertView = mInflater.inflate(R.layout.item,null);
                holder.img = (ImageView)convertView.findViewById(R.id.img);
                holder.text = (TextView)convertView.findViewById(R.id.text);

                    convertView.setTag(holder);
            }else {
                holder = (ContentActivity.ViewHolder)convertView.getTag();
            }
            //holder.img.setImageBitmap((Bitmap)newsapply.newslist.get(position).get("img"));


            //holder.img.setImageResource(R.drawable.timg);
            if(position == 0)
            {
                holder.text.setText(title);
                holder.img.setBackgroundResource(R.drawable.i1);
             }
            else if(position == this.getCount()-1) {
                holder.text.setText(text);
                holder.img.setBackgroundResource(R.drawable.i1);
            }
            else {
                String imgurl = (String)imagelist.get(position-1);
                holder.text.setText("");
                imgapply.showImageAsyn(holder.img,imgurl,R.drawable.timg);

            }
            return convertView;
        }

    }

}
