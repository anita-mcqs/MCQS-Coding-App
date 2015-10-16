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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bluelinelabs.logansquare.LoganSquare;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
    Button quizButton;
    private String downloadedJSONTxt="";
    private static String packageURL= "http://192.168.1.7:4444/question/exam/4";//PHP Exam      //test download JSON
    int id;
    private String questionIDTemp="";
    private ProgressBar spinner;
    private List<Question> qList;
    private List<Question> finalList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectedexam);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        actionBarTitle = (TextView) findViewById(R.id.action_bar_text);
        actionBarTitle.setText(R.string.title_activity_download_exam);
        finalList = new ArrayList<Question>();
        Intent i = getIntent();

        name = i.getStringExtra("name");
        id = i.getIntExtra("id", 0);
        description = i.getStringExtra("description");
        spinner = (ProgressBar) findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        txtName = (TextView) findViewById(R.id.textViewExamName);
        txtDescription = (TextView) findViewById(R.id.textViewExamDescription);
        downloadButton = (Button) findViewById(R.id.button);
        quizButton = (Button) findViewById(R.id.button3);

        txtName.setText(name);
        txtDescription.setText(description);
        questionIDTemp = readFromFileID();

        quizButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent startQuiz = new Intent(DownloadExam.this, ViewQuestion.class);
                startActivity(startQuiz);
            }
        });





        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.setVisibility(View.VISIBLE);
                try{
                    downloadedJSONTxt = new DownloadQuestion().execute(packageURL).get();

                    String toPathImages = "/data/data/" + getPackageName()+"/files/images";
                    File imageFolder = new File(toPathImages);
                    File[] imageFiles = imageFolder.listFiles();
                    System.out.println("no of images!: "+ imageFiles.length);
                    for(int i=0;i<imageFiles.length;i++){
                        imageFiles[i].delete();
                    }
                    System.out.println("*******************************Logan Square**********************************");
                    qList = LoganSquare.parseList(downloadedJSONTxt, Question.class);
                    for (int i = 0; i < qList.size(); i++) {
                        // System.out.println("QuestionId: " + qList.get(i).getQuestionId());
                        if (qList.get(i).getImages() != null) {
                            try {
                                URL[] imageURLS = new URL[qList.get(i).getImages().length];
                                for (int j = 0; j < qList.get(i).getImages().length; j++) {
                                    URL imageURL = new URL(qList.get(i).getImages()[j]);
                                    imageURLS[j] = imageURL;
                                }
                                new DownloadImages().execute(qList.get(i));

                                //  finalList.add(qList.get(i));
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                        } else {
                            finalList.add(qList.get(i));//add questions without images to final list
                        }
                    }
                    System.out.println("final List Download: " + finalList.size());




                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                catch(IOException er){
                    er.printStackTrace();
                }
                catch (ExecutionException e)
                {
                    e.printStackTrace();
                }




            }
        });
    }
    private String readFromFileID() {
        String ret = "";
        String toPath = "/data/data/" + getPackageName();
        try {
            InputStream inputStream = openFileInput("myQuestionIds.txt");
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
        return ret;
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







    private class DownloadImages extends AsyncTask<Question, Integer, Integer> {
        Question myQ;

        protected Integer doInBackground(Question... urls) {

            myQ = urls[0];
            try {

                URL[] imageURLS = new URL[myQ.getImages().length];
                for (int j = 0; j < myQ.getImages().length; j++) {
                    URL imageURL = new URL(myQ.getImages()[j]);
                    imageURLS[j] = imageURL;
                }

                int count = imageURLS.length;
                for (int i = 0; i < count; i++) {


                    URL testPath = imageURLS[i];
                    String testPath2 = new File(testPath.getPath()).getName();
                    String toPathImages = "/data/data/" + getPackageName() + "/files/images/";
                    InputStream in = new BufferedInputStream(testPath.openStream());
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    byte[] buf = new byte[1024];
                    int n = 0;
                    while (-1 != (n = in.read(buf))) {
                        out.write(buf, 0, n);
                    }
                    out.close();
                    in.close();
                    byte[] response = out.toByteArray();

                    FileOutputStream fos = new FileOutputStream(toPathImages + testPath2);
                    fos.write(response);
                    fos.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return 1;
        }
        protected void onPostExecute(Integer result)
        {
            System.out.println("test post execute" + result);
            finalList.add(myQ);
            System.out.println("array size: "+ finalList.size());

            if(finalList.size()==1){//test exam 4 - only one question
                spinner.setVisibility(View.INVISIBLE);
                quizButton.setEnabled(true);
            }
//            if(finalList.size()==qList.size()){
//                //all images downloaded
//                spinner.setVisibility(View.INVISIBLE);
//                quizButton.setEnabled(true);
//
//            }




        }

    }







        private class DownloadQuestion extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... urls){
            MyJSONParser jsonParser = new MyJSONParser(questionIDTemp);
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
          //  System.out.println("ret: " + ret);
            return ret;
        }

        protected void onPostExecute(String result)
        {
            downloadedJSONTxt = result;
        }
    }
}
