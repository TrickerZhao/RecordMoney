package com.tricker.recordmoney;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.tricker.recordmoney.model.User;

import java.util.List;

/**
 * Created by Tricker on 2016/9/18  018.
 */
public class MyApplication extends Application {
    private static Context context;
    private List<Activity> mActivityList;
    private static User user;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        MyApplication.user = user;
        //把该用户存到SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        SharedPreferences.Editor editor =prefs.edit();
        editor.putString("loginUser",user.getName());
        editor.commit();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
    public static Context getContext(){
        return context;
    }
    
    public void addActivity(Activity activity){
        if(!mActivityList.contains(activity)){
            mActivityList.add(activity);
        }
    }
    public void exitAll(){
        for (Activity a:mActivityList) {
            if(a.isFinishing()){
                a.finish();
            }
            System.exit(0);
//            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}
