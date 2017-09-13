package com.java.a21;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class TypeActivity extends AppCompatActivity {
    final int labelnum = 13;

    Button button[] = new Button[labelnum];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        final boolean[] chosen = intent.getBooleanArrayExtra("chosen");
        final String[] typename = intent.getStringArrayExtra("typename");
        Integer[] id = {R.id.button0,R.id.button1,R.id.button2,R.id.button3,R.id.button4,R.id.button5,R.id.button6,R.id.button7,R.id.button8,R.id.button9,R.id.button10,R.id.button11,R.id.button12};

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent rintent = new Intent();
                rintent.putExtra("chosen", chosen);

                setResult(1500, rintent);
                finish();
            }
        });


        for(int i = 0; i < labelnum; i ++) {
            final int tmp = i;
            button[i] = (Button) findViewById(id[i]);
            button[i].setText(typename[i]);
            if(chosen[i] == true) {
                button[i].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
            else {
                button[i].setBackgroundColor(getResources().getColor(R.color.colorUnchosen));
            }

            button[i].setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    if(chosen[tmp] == false) {
                        button[tmp].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        chosen[tmp] = true;
                    }
                    else {
                        button[tmp].setBackgroundColor(getResources().getColor(R.color.colorUnchosen));
                        chosen[tmp] = false;
                    }
                }
            });
        }
    }

}
