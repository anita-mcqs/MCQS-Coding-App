package com.mcqs.anita.mcqs_android_version1;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcqs.anita.mcqs_android_version1.R;

public class LogIn extends AppCompatActivity {
    private TextView actionBarTitle;
    private ImageView downloadIcon;
    private static String logInURL= "http://192.168.1.7:4444/question/exam/4";//PHP Exam      //test download JSON
    private EditText usernameEditText;
    private EditText passwordEditText;
    private String username;
    private String password;
    private Button logIn;



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
        usernameEditText = (EditText) findViewById(R.id.editText2);
        passwordEditText = (EditText) findViewById(R.id.editText3);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                username = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();
                if(username.equals("")|username.equals(" ")|password.equals("")|password.equals(" ")){
                    System.out.println("Blank LogIn!");

                }
                else{
                    System.out.println("OK LogIn");
                }




            }
        });
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
