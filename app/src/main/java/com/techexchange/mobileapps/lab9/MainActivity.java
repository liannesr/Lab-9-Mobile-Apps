package com.techexchange.mobileapps.lab9;

import android.support.v4.app.Fragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.List;
import android.content.res.Resources;
import android.support.v4.util.Preconditions;
import java.util.ArrayList;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import java.util.Random;
import android.app.Activity;
import android.util.Log;
import android.content.Intent;
import android.support.annotation.Nullable;
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        Log.d(TAG, "HIII1");
        super.onCreate(savedInstanceState);

        Log.d(TAG, "HIII");
        setContentView(R.layout.activity_main);


        FragmentManager fm = getSupportFragmentManager();
        Log.d(TAG, "lol" + fm);
        Fragment frag = fm.findFragmentById(R.id.fragment_container);
        Log.d(TAG, "Frag" + frag);

        if(frag == null){
            frag = new QuizFragment();
            fm.beginTransaction().add(R.id.fragment_container,frag).commit();
        }


    }
}
