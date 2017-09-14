package com.java.a21;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
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

        final int[][] colors = new int[labelnum][1];
        final int[][] states = new int[1][];
        states[0] = new int[]{android.R.attr.state_enabled};
        for(int i = 0; i < labelnum; i ++) {
            final int tmp = i;
            button[i] = (Button) findViewById(id[i]);
            button[i].setText(typename[i]);
            button[i].setTextColor(getResources().getColor(R.color.colorWhite));

            if(chosen[i] == true) {
                colors[i][0] = getResources().getColor(R.color.colorPrimary);
            }
            else {
                colors[i][0] = getResources().getColor(R.color.colorUnchosen);
            }
            ViewCompat.setBackgroundTintList(button[i], new ColorStateList(states, colors[i]));

            button[i].setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    if(chosen[tmp] == false) {
                        colors[tmp][0] = getResources().getColor(R.color.colorPrimary);
                        ViewCompat.setBackgroundTintList(button[tmp], new ColorStateList(states, colors[tmp]));
                        chosen[tmp] = true;
                    }
                    else {
                        colors[tmp][0] = getResources().getColor(R.color.colorUnchosen);
                        ViewCompat.setBackgroundTintList(button[tmp], new ColorStateList(states, colors[tmp]));
                        chosen[tmp] = false;
                    }
                }
            });
        }
    }

}
