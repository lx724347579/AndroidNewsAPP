package com.example.cdogemaru.javahw;

import android.app.ListActivity;
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

import butterknife.BindView;

public class MainActivity extends AppCompatActivity {
//public class MainActivity extends ListActivity {
    private List<Map<String, Object>> mData;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mData = getData();
        setContentView(R.layout.activity_main);
        ListView newsview = (ListView)findViewById(R.id.newsview);
        MyAdapter adapter = new MyAdapter(this);
        newsview.setAdapter(adapter);
        newsview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, "Click item" + i, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title", "G1");
        map.put("info", "google 1");
        map.put("img", R.drawable.i1);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "G2");
        map.put("info", "google 2");
        map.put("img", R.drawable.i1);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "G3");
        map.put("info", "google 3");
        map.put("img", R.drawable.i1);
        list.add(map);

        return list;
    }

    // ListView 中某项被选中后的逻辑
//    @Override
//    protected void onListItemClick(ListView l, View v, int position, long id) {
//
//        Log.v("MyListView4-click", (String)mData.get(position).get("title"));
//    }

    /**
     * listview中点击按键弹出对话框
     */
    public void showInfo(){
        new AlertDialog.Builder(this)
                .setTitle("我的listview")
                .setMessage("介绍...")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();

    }



    public final class ViewHolder{
        public ImageView img;
        public TextView title;
        public TextView info;
        //public Button viewBtn;
    }


    public class MyAdapter extends BaseAdapter{

        private LayoutInflater mInflater;


        public MyAdapter(Context context){
            this.mInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mData.size();
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
                //holder.viewBtn = (Button)convertView.findViewById(R.id.view_btn);
                convertView.setTag(holder);

            }else {

                holder = (ViewHolder)convertView.getTag();
            }


            holder.img.setBackgroundResource((Integer)mData.get(position).get("img"));
            holder.title.setText((String)mData.get(position).get("title"));
            holder.info.setText((String)mData.get(position).get("info"));

            //holder.viewBtn.setOnClickListener(new View.OnClickListener() {

//                @Override
//                public void onClick(View v) {
//                    showInfo();
//                }
//            });


            return convertView;
        }

    }

}
