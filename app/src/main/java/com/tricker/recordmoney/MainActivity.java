package com.tricker.recordmoney;

import android.app.FragmentManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.tricker.recordmoney.util.TrickerUtils;

import java.io.File;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private int type;//租金、份子钱还是销售额
    private Cursor editCursor;//编辑传递
    private FloatingActionButton fab;
    public FloatingActionButton getFab() {
        return fab;
    }

    public Cursor getEditCursor() {
        return editCursor;
    }

    public void setEditCursor(Cursor editCursor) {
        this.editCursor = editCursor;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //设置默认出来就显示Record
        getFragmentManager().beginTransaction().replace(R.id.container, RecordFragment.newInstance(1,getEditCursor(),this.type))
                .commit();
    }
    private long lastTime = 0;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
            if (System.currentTimeMillis() - lastTime > 2000) {
                TrickerUtils.showToast(this, "再按一次退出登录！");
                lastTime = System.currentTimeMillis();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        getMenuInflater().inflate(R.menu.global, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_about) {
            TrickerUtils.showToast(this, "Tricker 出品，必属精品！！！");
            return true;
        }else if(id==R.id.action_copyDB){
            File fromFile = new File(TrickerUtils.getPath(this, TrickerUtils.DATABASE_PATH));
            File toFile = new File(Environment.getExternalStorageDirectory().getPath()+File.separator+"tricker"+File.separator+"tricker.db");
            String result =TrickerUtils.copyfile(fromFile, toFile , true);
            TrickerUtils.showToast(this, result);
        }else if(id==R.id.action_coverDB){
            File fromFile = new File(TrickerUtils.getPath(this, TrickerUtils.DATABASE_PATH));
            File toFile = new File(Environment.getExternalStorageDirectory().getPath()+File.separator+"tricker"+File.separator+"tricker.db");
            String result =TrickerUtils.copyfile(toFile, fromFile , true);
            TrickerUtils.showToast(this, result);
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        FragmentManager fragmentManager = getFragmentManager();
        int id = item.getItemId();

        if (id == R.id.nav_record) {
            fragmentManager.beginTransaction().replace(R.id.container, RecordFragment.newInstance(1,getEditCursor(),this.type))
                    .commit();
            //清空数据，用户再次点击记录，就是新增操作
            setEditCursor(null);
        } else if (id == R.id.nav_query) {
            fragmentManager.beginTransaction().replace(R.id.container, QueryFragment.newInstance(1,type))
                    .commit();
        } else if (id == R.id.nav_map) {
            fragmentManager.beginTransaction().replace(R.id.container, MapFragment.newInstance(1))
                    .commit();
        } else if (id == R.id.nav_weather) {
            Intent intent = new Intent(this, ChooseAreaActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
//            Snackbar.make(fab,"施工中，请绕道！",Snackbar.LENGTH_SHORT).show();
            TrickerUtils.showSnackbar(fab,"施工中，请绕道！");
        } else if (id == R.id.nav_send) {
//            Snackbar.make(fab,"施工中，请绕道！",Snackbar.LENGTH_SHORT).show();
            TrickerUtils.showSnackbar(fab,"施工中，请绕道！");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
