package com.example.cdogemaru.javahw;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.LineNumberReader;

import static java.io.FileDescriptor.in;

public class ContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(intent.getStringExtra("title"));

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

}
