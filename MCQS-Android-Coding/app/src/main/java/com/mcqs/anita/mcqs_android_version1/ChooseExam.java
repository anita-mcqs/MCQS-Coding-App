package com.mcqs.anita.mcqs_android_version1;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ChooseExam extends AppCompatActivity {

    private TextView actionBarTitle;
    private ListView lv;
    private ListViewAdapter adapter;
    private EditText inputSearch;
    private ArrayList<Exam> examList;
    private Button downloadOriginal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_exam);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        actionBarTitle = (TextView) findViewById(R.id.action_bar_text);
        actionBarTitle.setText(R.string.title_activity_choose_exam);

        downloadOriginal = (Button) findViewById(R.id.button2);

        downloadOriginal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                File dir = getFilesDir();
                File file = new File(dir, "myJSON.txt");
                boolean deleted = file.delete();//delete text file in internal storage
                String toPathImages = "/data/data/" + getPackageName()+"/files/images";
                File imageFolder = new File(toPathImages);
                File[] imageFiles = imageFolder.listFiles();
                System.out.println("no of images!: "+ imageFiles.length);
                for(int i=0;i<imageFiles.length;i++){
                   imageFiles[i].delete();
                }
                imageFolder.delete();

                Intent startQuiz = new Intent(ChooseExam.this, ViewQuestion.class);
                startActivity(startQuiz);
            }
        });

        examList = new ArrayList<Exam>();

        // TODO: 23/09/2015 Download Exam List - http://192.168.1.7:4444/question/list/ 
        createList();

        adapter = new ListViewAdapter(this, examList);

        lv = (ListView) findViewById(R.id.listView);
        inputSearch = (EditText) findViewById(R.id.editText);

        lv.setAdapter(adapter);

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = inputSearch.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });
    }




    // TODO: 22/09/2015 Sample Data
    private void createList(){

        Exam exam1 = new Exam("Package 1", 1,"Package Description For Package One");
        Exam exam2 = new Exam("Package 2", 2,"Package Description For Package Two");
        Exam exam3 = new Exam("Package 3", 3,"Package Description For Package Three");
        Exam exam4 = new Exam("Package 4", 4,"Package Description For Package Four");
        Exam exam5 = new Exam("Package 5", 5,"Package Description For Package Five");
        Exam exam6 = new Exam("Package 6", 6,"Package Description For Package Six");
        Exam exam7 = new Exam("Package 7", 7,"Package Description For Package Seven");
        
        examList.add(exam1);
        examList.add(exam2);
        examList.add(exam3);
        examList.add(exam4);
        examList.add(exam5);
        examList.add(exam6);
        examList.add(exam7);
    }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_choose_exam, menu);
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
}
