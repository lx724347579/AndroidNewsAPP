package com.java.a21;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.java.a21.R;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

import java.util.ArrayList;

public class ContentActivity extends AppCompatActivity {

    private ListView listview;
    private RequestOptions requestOptions;

    private ArrayList<String>imagelist  = new ArrayList<String>();
    private String title, text;

    private SpeechSynthesizer mTts;
    private SynthesizerListener mLst;
    boolean isTtsPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.timg);
        requestOptions.error(R.drawable.timg);


        Intent intent = getIntent();

        GetNewsContent apply = new GetNewsContent();
        apply.getData("http://166.111.68.66:2042/news/action/query/detail?newsId=" + intent.getStringExtra("id"));

        while (true) {
            Log.v("ac","1");
            if (apply.finished)
                break;
        }

        title = apply.news.Title;
        text = apply.news.Content;
        String tmp = apply.news.Pictures;
        Log.i("pic",tmp);
        if(tmp.length() > 0)
            if (tmp.contains(";"))
            {
                String[] c = tmp.split(";|\\s");
                for (int i = 0; i < c.length; i++)
                    imagelist.add(c[i]);
            }
            else
                imagelist.add(tmp);

        listview = (ListView) findViewById(R.id.newslayout);
        MyAdapter adapter = new MyAdapter(this);
        listview.setAdapter(adapter);

        mTts = SpeechSynthesizer.createSynthesizer(ContentActivity.this, mTtsInitListener);
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
        mTts.setParameter(SpeechConstant.PITCH, "50");
        mTts.setParameter(SpeechConstant.VOLUME, "80");
        ImageButton button;
        button = (ImageButton) findViewById(R.id.button);
        button.setImageDrawable(getResources().getDrawable(R.drawable.sound));
        ImageButton button1;
        button1 = (ImageButton) findViewById(R.id.button1);
        button1.setImageDrawable(getResources().getDrawable(R.drawable.share2));
        ImageButton button2;
        button2 = (ImageButton) findViewById(R.id.button2);
        button2.setImageDrawable(getResources().getDrawable(R.drawable.star));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTts != null)
                {
                    if (!isTtsPlaying) {
                        String readtext = text;
                        int code = mTts.startSpeaking(readtext, mTtsListener);
                        if (code != ErrorCode.SUCCESS) {
                            Toast.makeText(ContentActivity.this,
                                    "failed" + code,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            isTtsPlaying = true;
                            Toast.makeText(ContentActivity.this,
                                    "reading", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        isTtsPlaying = false;
                        mTts.stopSpeaking();
                    }
                }else {
                    Toast.makeText(ContentActivity.this,
                            "init failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
            if(position == 0) {
                holder.text.setText((CharSequence) title);
                holder.text.setTypeface(Typeface.SANS_SERIF,Typeface.BOLD);
                holder.text.setTextSize(40);
                Glide.with(listview).load("").into(holder.img);
             }
            else {
                if(position == this.getCount()-1) {
                    holder.text.setText(text);
                    holder.img.setImageDrawable(null);
                }
                else {
                    String imgurl = (String)imagelist.get(position-1);
                    holder.text.setText("");
                    Log.i("pics",imgurl);
                    Glide.with(listview).load(imgurl).apply(requestOptions).into(holder.img);
                    //imgapply.showImageAsyn(holder.img,imgurl,R.drawable.timg);

                }
            }
            return convertView;
        }

        //TODO ALSO NEED TO SET LISTENER FOR "BACK"

    }

    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
            Log.d("tts", "开始播放");
        }

        @Override
        public void onSpeakPaused() {
            Log.d("tts", "暂停播放");
        }

        @Override
        public void onSpeakResumed() {
            Log.d("tts", "继续播放");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
            // 合成进度
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
        }

        @Override
        public void onCompleted(SpeechError error) {
            isTtsPlaying = false;
            if (error == null) {
                Log.d("tts", "播放完成");
                Toast.makeText(ContentActivity.this,
                        "read completed", Toast.LENGTH_SHORT).show();
            } else {
                Log.d("tts", error.getPlainDescription(true));
                Toast.makeText(ContentActivity.this,
                        error.getPlainDescription(true), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {

        }
    };
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d("tts", "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                Log.d("tts", "初始化失败，错误码：" + code);
            }
        }
    };

}
