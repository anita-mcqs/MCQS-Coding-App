package com.mcqs.anita.mcqs_android_version1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bluelinelabs.logansquare.LoganSquare;
import com.mcqs.anita.mcqs_android_version1.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class LogIn extends AppCompatActivity {
    private TextView actionBarTitle;
    private ImageView downloadIcon;
    private static String packageURL= "http://192.168.1.7:4444/question/exam/4";//PHP Exam      //test download JSON
    private String downloadedJSONTxt="";
    private EditText usernameEditText;
    private EditText passwordEditText;
    private String username;
    private String password;
    private String sendJSON = "";
    private String questionIDTemp="";
    private Button logIn;
    private Button signUp;
    private Button quizButton;
    private ProgressBar spinner;
    final Context context = this;
    private String userJSON = "";
    private List<Question> qList;
    private List<Question> finalList;
    private int logInStatus;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        downloadIcon = (ImageView) findViewById(R.id.imageViewDownloadIcon);//download icon in action bar
        downloadIcon.setVisibility(View.INVISIBLE);
        actionBarTitle = (TextView) findViewById(R.id.action_bar_text);
        actionBarTitle.setText("Download");


        logIn = (Button) findViewById(R.id.button4);
        signUp = (Button) findViewById(R.id.button5);
        usernameEditText = (EditText) findViewById(R.id.editText2);
        passwordEditText = (EditText) findViewById(R.id.editText3);
        quizButton = (Button) findViewById(R.id.button);
        spinner = (ProgressBar) findViewById(R.id.progressBar3);
        spinner.setVisibility(View.GONE);
        finalList = new ArrayList<Question>();

        signUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://192.168.1.7:2010/signup"));
                startActivity(intent);
            }
        });





        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                username = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();
                if(username.equals("")|username.equals(" ")|password.equals("")|password.equals(" ")){

                    if(password.equals("")|password.equals(" ")&&username.equals("")|username.equals(" ")){
                        System.out.println("Blank!");
                        AlertDialog alertDialog = new AlertDialog.Builder(
                                LogIn.this).create();
                        alertDialog.setTitle("Log In Error");
                        alertDialog.setMessage("Please enter your user name and password");
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        alertDialog.show();
                    }
                    else if(password.equals("")|password.equals(" ")) {
                        System.out.println("Blank Password!");
                        AlertDialog alertDialog = new AlertDialog.Builder(
                                LogIn.this).create();
                        alertDialog.setTitle("Log In Error");
                        alertDialog.setMessage("Please enter your password");
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        alertDialog.show();
                    }
                    else if (username.equals("")|username.equals(" ")){
                        System.out.println("Blank username!");
                        AlertDialog alertDialog = new AlertDialog.Builder(
                                LogIn.this).create();
                        alertDialog.setTitle("Log In Error");
                        alertDialog.setMessage("Please enter your user name");
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        alertDialog.show();
                    }
                }
                else{
                    System.out.println("OK LogIn");


                    questionIDTemp = readFromFileID();

                    userJSON = ", \"username\" : \""+ username + "\", \"password\" : \""+ password +"\"}";
                   // System.out.println(temp);
                    sendJSON = "{\"ids\":"+questionIDTemp+userJSON;
                    spinner.setVisibility(View.VISIBLE);
                    try {
                        downloadedJSONTxt = new DownloadQuestion().execute(packageURL).get();

                        if(logInStatus==401){
                            System.out.println("bad status!!!! 401!!!");
                            quizButton.setEnabled(false);
                            showError();
                            spinner.setVisibility(View.INVISIBLE);
                        }
                        else{
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
                                System.out.println("QuestionId: " + qList.get(i).getQuestionId());
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
//                        else{
//                            quizButton.setEnabled(true);
//                        }






                    }
                    catch(InterruptedException ex){
                       ex.printStackTrace();
                    }
                    catch(IOException er){
                        er.printStackTrace();
                    }
                    catch (ExecutionException e)
                    {
                        e.printStackTrace();
                    }






//                    Intent i = new Intent(LogIn.this, DownloadExam.class);
//                   // i.putExtra("details",temp);
//                    i.putExtra("name",username);
//                    i.putExtra("json",downloadedJSONTxt);
//                    startActivity(i);



                }




            }
        });
    }
    private void showError() {
        usernameEditText.setError("Password and username didn't match");
    }
    //https://github.com/napcs/qedserver/blob/master/jetty/modules/jetty/src/test/java/org/mortbay/jetty/servlet/SessionTestClient.java
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
        getMenuInflater().inflate(R.menu.menu_log_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

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
                for (int i = 0; i < count; i++)
                {
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
//            if(finalList.size()==1){//test exam 4 - only one question
//                spinner.setVisibility(View.INVISIBLE);
//                quizButton.setEnabled(true);
//            }
            if(finalList.size()==qList.size()){
                //all images downloaded
                spinner.setVisibility(View.INVISIBLE);
                quizButton.setEnabled(true);
            }
        }
    }


    private class DownloadQuestion extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... urls){
            MyJSONParser jsonParser = new MyJSONParser(sendJSON, LogIn.this);

            String myJSON = jsonParser.getJSONFromUrl(urls[0]);
            int myStatus = jsonParser.getStatus();
            System.out.println("log Status: "+myStatus);

            logInStatus = jsonParser.getStatus();

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
