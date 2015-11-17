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
import android.widget.TextView;
import android.widget.Toast;

import com.mcqs.anita.mcqs_android_version1.R;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

public class LogIn extends AppCompatActivity {
    private TextView actionBarTitle;
    private ImageView downloadIcon;
    private static String logInURL= "http://192.168.1.7:4444/question/exam/4";//PHP Exam      //test download JSON
    private String downloadedJSONTxt="";
    private EditText usernameEditText;
    private EditText passwordEditText;
    private String username;
    private String password;
    private Button logIn;
    private Button signUp;
    final Context context = this;
    private String temp = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        downloadIcon = (ImageView) findViewById(R.id.imageViewDownloadIcon);//download icon in action bar
        downloadIcon.setVisibility(View.INVISIBLE);
        actionBarTitle = (TextView) findViewById(R.id.action_bar_text);
        actionBarTitle.setText(R.string.title_activity_log_in);


        logIn = (Button) findViewById(R.id.button4);
        signUp = (Button) findViewById(R.id.button5);
        usernameEditText = (EditText) findViewById(R.id.editText2);
        passwordEditText = (EditText) findViewById(R.id.editText3);
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
                    temp = "{username : "+ username + ", password : "+ password +"}";
                    System.out.println(temp);
                    try {
                        //download user info - error check - username/password exists????cookie
                        downloadedJSONTxt = new DownloadQuestion().execute(logInURL).get();


                        //download questions - send questionsID

                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    catch (ExecutionException e)
                    {
                        e.printStackTrace();
                    }


                }




            }
        });
    }

    //https://github.com/napcs/qedserver/blob/master/jetty/modules/jetty/src/test/java/org/mortbay/jetty/servlet/SessionTestClient.java

    private class DownloadQuestion extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... urls){
            MyJSONParser jsonParser = new MyJSONParser(temp);
            String myJSON = jsonParser.getJSONFromUrl(urls[0]);
            FileOutputStream outputStream;
            if(myJSON==null){
                System.out.println("json null");
            }
            String fileName = "user.txt";
            try {
                outputStream = openFileOutput("user.txt", Context.MODE_PRIVATE);
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
                InputStream inputStream = openFileInput("user.txt");
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
}
