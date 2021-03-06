package com.java.a21;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.Switch;

import java.io.File;
import android.widget.Toast;


public class NewsListActivity extends AppCompatActivity {
    final int labelnum = 13;
    private SimpleFragmentPagerAdapter pagerAdapter;
    private ViewPager viewPager;

    private Switch night_mode_switch;

    private Switch no_pictures_switch;

    private ImageButton addTabButton;
    private TabLayout tabLayout;
    String keyword = new String();

    public static void deleteDirWihtFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDirWihtFile(file); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        addTabButton = (ImageButton) findViewById(R.id.addTabButton);
        night_mode_switch = (Switch) findViewById(R.id.night_mode_switch);
        no_pictures_switch = (Switch) findViewById(R.id.no_pictures_switch);


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("News++");
        setSupportActionBar(toolbar);

        String path = "/data/data/com.java.a21/files";
        File file = new File(path);
        //deleteDirWihtFile(file);
        Log.d("cao",String.valueOf(file.exists()));

        night_mode_switch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Boolean checked = ((Switch)view).isChecked();
                if (checked)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                else
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                recreate();
            }
        });

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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

            super.onActivityResult(requestCode, resultCode, data);
        try {
            pagerAdapter.chosen = data.getBooleanArrayExtra("chosen");
            pagerAdapter.refresh();
        }catch (NullPointerException e){

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);//指定Toolbar上的视图文件
        final SearchView searchview = (SearchView) menu.findItem(R.id.search).getActionView();
        searchview.setSubmitButtonEnabled(true);
        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                keyword = query;
                pagerAdapter.notifyDataSetChanged();

                Toast.makeText(NewsListActivity.this, query, Toast.LENGTH_SHORT);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText == null || newText.equals("")) {
                    keyword = "";
                    pagerAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });

        return true;
    }




    public class SimpleFragmentPagerAdapter extends FragmentStatePagerAdapter {

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
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
        @Override
        public Fragment getItem(int position) {
            Log.d("fuck",String.valueOf(position)+gettitle(position));
            return PageFragment.newInstance(gettitle(position),keyword);
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
