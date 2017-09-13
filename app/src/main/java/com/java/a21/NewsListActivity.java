package com.java.a21;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;


public class NewsListActivity extends AppCompatActivity {
    final int labelnum = 13;
    private SimpleFragmentPagerAdapter pagerAdapter;

    private ViewPager viewPager;

    private ImageButton addTabButton;

    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        addTabButton = (ImageButton) findViewById(R.id.addTabButton);
        setSupportActionBar(toolbar);



        addTabButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsListActivity.this, TypeActivity.class);
                intent.putExtra("chosen", pagerAdapter.chosen);
                intent.putExtra("typename", pagerAdapter.tabTitles);
                startActivityForResult(intent, 1500);
            }
        });

        pagerAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(), this);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(pagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    //重写   处理返回信息的监听（回调方法）
    //onActivityResult通用监听  监听所有返回信息的
    //必须要有requestCode区分有哪个请求返回的
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        pagerAdapter.chosen = data.getBooleanArrayExtra("chosen");
        pagerAdapter.refresh();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);//指定Toolbar上的视图文件
        return true;
    }

    public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

        int pagecount = labelnum;
        private String tabTitles[] = new String[]{
                "全部","科技","教育","军事","国内","社会","文化","汽车","国际","体育","财经","健康","娱乐"};
        private Context context;

        public boolean[] chosen;
        public SimpleFragmentPagerAdapter(FragmentManager fm,Context context) {
            super(fm);

            this.context = context;
            chosen = new boolean[labelnum];
            for(int i = 0; i < labelnum; i ++) {
                chosen[i] = true;
            }
        }
        public void refresh() {
            int tmp = 0;
            for(int i = 0; i < labelnum; i ++) {
                if(chosen[i] == true) {
                    tmp ++;
                }
            }
            pagecount = tmp;
            notifyDataSetChanged();
        }

        public String gettitle(int position) {
            int cnt = 0;
            int result = 0;
            for(int i = 0; i < labelnum; i ++) {
                if(chosen[i] == true) {
                    cnt ++;
                    result = i;
                }
                if(cnt == position + 1) {
                    break;
                }
            }
            return tabTitles[result];
        }

        @Override
        public Fragment getItem(int position) {
            return PageFragment.newInstance(gettitle(position));
            //return PageFragment.newInstance(tabTitles[position]);
        }

        @Override
        public int getCount() {
            return pagecount;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return gettitle(position);
        }
    }
}
