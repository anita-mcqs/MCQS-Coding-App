package com.mcqs.anita.mcqs_android_version1;

import android.content.ContextWrapper;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.res.AssetManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import java.io.BufferedReader;
import android.widget.Button;
import android.widget.TextView;
import com.google.gson.JsonParser;

import java.io.InputStreamReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private Button startQuiz;
    private Button downloadExam;
    private TextView actionBarTitle;
    private String myJSONString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        actionBarTitle = (TextView) findViewById(R.id.action_bar_text);
        actionBarTitle.setText(R.string.title_activity_main);
        startQuiz = (Button) findViewById(R.id.buttonStartQuiz);
        downloadExam = (Button) findViewById(R.id.buttonDownloadExam);



        startQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startQuiz = new Intent(MainActivity.this, ViewQuestion.class);

                startActivity(startQuiz);
            }
        });

        downloadExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chooseExam = new Intent(MainActivity.this, ChooseExam.class);
                startActivity(chooseExam);
            }
        });




    }















    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
       //     return true;
       // }

        return super.onOptionsItemSelected(item);
    }







}
