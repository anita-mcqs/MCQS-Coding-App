package com.mcqs.anita.mcqs_android_version1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

/**
 * Created by david-MCQS on 23/09/2015.
 */
public class DownloadExam extends AppCompatActivity {

    private TextView actionBarTitle;
    TextView txtName;
    TextView txtDescription;
    String description;
    String name;
    Button downloadButton;
    private String downloadedJSONTxt="";
    private static String packageURL= "http://192.168.1.7:4444/question/exam/3";//PHP Exam      //test download JSON
 //   private static String packageURL= "http://192.168.1.7:4444/question/list";//PHP Exam      //test download JSON
    int id;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectedexam);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        actionBarTitle = (TextView) findViewById(R.id.action_bar_text);
        actionBarTitle.setText(R.string.title_activity_download_exam);

        Intent i = getIntent();

        name = i.getStringExtra("name");
        id = i.getIntExtra("id", 0);
        description = i.getStringExtra("description");

        txtName = (TextView) findViewById(R.id.textViewExamName);
        txtDescription = (TextView) findViewById(R.id.textViewExamDescription);
        downloadButton = (Button) findViewById(R.id.button);

        txtName.setText(name);
        txtDescription.setText(description);


        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    String test = new DownloadQuestion().execute(packageURL).get();

                 //   System.out.println(test);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                catch (ExecutionException e)
                {
                    e.printStackTrace();
                }
                Intent startQuiz = new Intent(DownloadExam.this, ViewQuestion.class);

                startActivity(startQuiz);

            }
        });


    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_download_exam, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        // if (id == R.id.action_settings) {
        //   return true;
        //  }

        return super.onOptionsItemSelected(item);
    }



    private class DownloadQuestion extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... urls){
            MyJSONParser jsonParser = new MyJSONParser();
            String myJSON = jsonParser.getJSONFromUrl(urls[0]);
            String test = "test test test";
            FileOutputStream outputStream;
            if(myJSON==null){
                System.out.println("json null");
            }
            String fileName = "myJSON.txt";
            try {
                outputStream = openFileOutput("myJSON.txt", Context.MODE_PRIVATE);
                outputStream.write(myJSON.getBytes());
                outputStream.close();
            }
            catch(FileNotFoundException er){
                er.printStackTrace();
            }
            catch(IOException er){
                er.printStackTrace();
            }

            String ret = "";
            String toPath = "/data/data/" + getPackageName();
            try {
                InputStream inputStream = openFileInput("myJSON.txt");
                if ( inputStream != null ) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String receiveString = "";
                    StringBuilder stringBuilder = new StringBuilder();
                    while ( (receiveString = bufferedReader.readLine()) != null ) {
                        stringBuilder.append(receiveString);
                    }
                    inputStream.close();
                    ret = stringBuilder.toString();
                }
            }
            catch (FileNotFoundException e) {
                Log.e("login activity", "File not found: " + e.toString());
            } catch (IOException e) {
                Log.e("login activity", "Can not read file: " + e.toString());
            }
            System.out.println("ret: " + ret);
            return downloadedJSONTxt;
        }

        protected void onPostExecute(String result)
        {
            downloadedJSONTxt = result;
        }
    }
}
