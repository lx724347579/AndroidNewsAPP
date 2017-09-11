package com.example.cdogemaru.javahw;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;

import org.w3c.dom.Text;

import java.io.LineNumberReader;

import static java.io.FileDescriptor.in;

public class ContentActivity extends AppCompatActivity {
    private SpeechSynthesizer mTts;
    private SynthesizerListener mLst;
    boolean isTtsPlaying = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(intent.getStringExtra("title"));


        mTts = SpeechSynthesizer.createSynthesizer(ContentActivity.this, mTtsInitListener);
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
        mTts.setParameter(SpeechConstant.PITCH, "50");
        mTts.setParameter(SpeechConstant.VOLUME, "80");
        Button button;
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTts != null)
                {
                    if (!isTtsPlaying) {
                        String text = "起爷起爷起";
                        int code = mTts.startSpeaking(text, mTtsListener);
                        Toast.makeText(ContentActivity.this,
                                String.valueOf(code), Toast.LENGTH_SHORT).show();
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
        //TODO ALSO NEED TO SET LISTENER FOR "BACK"

//        btn_rs_skip = (Button)findViewById(R.id.btn_rs_skip);
//        btn_rs_skip.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {//采用Intent绑定Bundle的形式回传值
//
//                //新建一个Bundle，Bundle主要放值类型
//                Bundle bundle = new Bundle();
//                bundle.putString("rs", "我是RsActivity关闭后回传的值！");
//                //将Bundle赋给Intent
//                data.putExtras(bundle);
//                //跳转回MainActivity
//                //注意下面的RESULT_OK常量要与回传接收的Activity中onActivityResult（）方法一致
//                RsActivity.this.setResult(RESULT_OK, data);
//                //关闭当前activity
//                RsActivity.this.finish();
//            }
//        });

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
