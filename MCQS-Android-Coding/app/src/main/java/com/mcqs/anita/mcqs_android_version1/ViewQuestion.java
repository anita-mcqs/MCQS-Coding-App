package com.mcqs.anita.mcqs_android_version1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.view.View;
import com.bluelinelabs.logansquare.LoganSquare;
import com.google.gson.JsonParser;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import us.feras.mdv.MarkdownView;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import android.view.View.OnClickListener;

import org.json.JSONArray;
import org.json.JSONException;

@SuppressLint("SetJavaScriptEnabled")
public class ViewQuestion extends AppCompatActivity  {

    // private static String questionURL = "http://192.168.1.7:2010/api/fullQuestion";
    private ArrayList<Question> questionList = new ArrayList<Question>();
    private MarkdownView questionText;
    private Button optionOne;
    private Button optionTwo;
    private Button optionThree;
    private Button optionFour;
    private Button optionFive;
    private Question myQuestion1;
    private ArrayList<QuestionOptions> myOptions;
    private Button nextButton;
    private Button explanationButton;
    private Button imageButton;
    private Button questionButton;
    private MarkdownView explainText;
    private ScrollView explainScroll;
    private ScrollView backgroundScroll;
    private ScrollView parentScroll;
    private Boolean viewStatus = false;
    private ProgressBar progressBar;
    private int progressInt=0;
    private int progressMax=0;
    private String myJSONString = "";
    private TouchImageView questionImage;
    private TextView actionBarTitle;
    private String[] imageURLS;
    private int count=1;
    private int jsonArraySize1;
    private int myCount=3;
    private ImageView downloadIcon;
    private Question displayQ = new Question();
    private List<Question> qList;
    private List<Question> finalList;
    private ArrayList<Integer> questionIds = new ArrayList<>();
    private String questionIDTemp="";
    private ArrayList<Boolean> progressData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_question);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        actionBarTitle = (TextView) findViewById(R.id.action_bar_text);
        downloadIcon = (ImageView) findViewById(R.id.imageViewDownloadIcon);//download icon in action bar
        actionBarTitle.setText(R.string.title_activity_view_question);
        downloadIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chooseExam = new Intent(ViewQuestion.this, ChooseExam.class);
                startActivity(chooseExam);
            }
        });
        finalList = new ArrayList<Question>();

        checkFiles();//if there don't copy file
        myJSONString =  readFromFile();
        questionIDTemp = readFromFileID();
        try {
            JSONArray myIds = new JSONArray(questionIDTemp);
            if(myIds==null){
                System.out.println("No Questions Attempted yet");
            }
            else{

                for(int i=0;i<myIds.length(); i++){
                    questionIds.add(myIds.optInt(i));
                }
            }
        }
        catch(JSONException er){
            er.printStackTrace();
        }

      //  System.out.println("id length: "+ questionIds.size());
        JsonParser jsonParser = new JsonParser();

        try
        {//if finallist is empty!!
            qList = LoganSquare.parseList(myJSONString, Question.class);
          //  System.out.println("question array: " + qList.size()+ " "+ finalList.size());
                for (int i = 0; i < qList.size(); i++) {
                //    System.out.println("QuestionId: " + qList.get(i).getQuestionId());
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
              //  System.out.println("finallist: " + finalList.size());
                if (finalList.size()>0) {
                    int choice = (int) (Math.random() * finalList.size());//random question
                    displayQ = finalList.get(choice);
                    System.out.println("Display Question");
                    displayQuestions(displayQ);
                }
            else{
                    System.out.println("No Question");
                }
            }
            catch(IOException er){
                er.printStackTrace();
            }
            catch(Exception e){
                e.printStackTrace();
            }

      //  int choice = (int) (Math.random() * finalList.size());//random question
       // displayQ = finalList.get(0);
        // displayQ = finalList.get(choice);

       // registerForContextMenu(questionText);
     //   registerForContextMenu(explainText);
    }


    @Override
    public void onBackPressed() {//back button - go back to menu page
        Intent startQuiz = new Intent( ViewQuestion.this,MainActivity.class);
        startActivity(startQuiz);
       // Log.d("CDA", "onBackPressed Called");
       // Intent setIntent = new Intent(Intent.ACTION_MAIN);
       // setIntent.addCategory(Intent.CATEGORY_HOME);
      //  setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
       // startActivity(setIntent);
    }

    private String readFromFile() {
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
        return ret;
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

    private void checkFiles() {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        AssetManager assetMgr = getAssets();
        InputStream in = null;
        OutputStream out = null;
        String toPath = "/data/data/" + getPackageName()+"/files/";
        String toPathImages = "/data/data/" + getPackageName()+"/files/images";
        Boolean fileThere = fileExistance("myJSON.txt");
        Boolean fileIDThere = fileExistance("myQuestionIds.txt");
        if(fileThere==true)
        {
            // System.out.println("not empty");
        }
        else
        {
            copyAssetFolder(assetMgr, "json", toPath);
            copyAssetFolder(assetMgr, "ids", toPath);
            copyAssetFolder(assetMgr, "myImages", toPathImages);
        }
    }

    private static boolean copyAssetFolder(AssetManager assetManager,
                                           String fromAssetPath, String toPath) {
        try {
            String[] files = assetManager.list(fromAssetPath);
            new File(toPath).mkdirs();
            boolean res = true;
            for (String file : files)
                if (file.contains("."))
                    res &= copyAsset(assetManager,
                            fromAssetPath + "/" + file,
                            toPath + "/" + file);
                else
                    res &= copyAssetFolder(assetManager,
                            fromAssetPath + "/" + file,
                            toPath + "/" + file);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean fileExistance(String fname){
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }

    private static boolean copyAsset(AssetManager assetManager,
                                     String fromAssetPath, String toPath) {
        InputStream in = null;
        OutputStream out = null;
        try {
            String[] fileNames = assetManager.list(fromAssetPath);
            in = assetManager.open(fromAssetPath);
            new File(toPath).createNewFile();
            out = new FileOutputStream(toPath);
            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }


private void displayQuestions(Question myQ)
{
   // System.out.println("progress count to display!! "+ count);
    int questionNumbers = questionList.size();
    int choice = (int) (Math.random() * questionNumbers);//random question
    Question displayQuestion = myQ;
    String displayBackgroundString = displayQuestion.getBackground();
    String displayQuestionString = displayQuestion.getQuestion();
    String displayCoreString = displayQuestion.getCore();
    String displayExplanationString = displayQuestion.getExplanation();
    final int qID = displayQuestion.getQuestionId();
    imageURLS = displayQuestion.getImages();
    QuestionOptions[] questionOptions = displayQuestion.getQuestionOptions();
    myOptions = new ArrayList<QuestionOptions>(Arrays.asList(questionOptions));
    Collections.shuffle(myOptions);//shuffle options

    progressBar = (ProgressBar) findViewById(R.id.progressBar2);

    progressBar.setMax(10);

    int progressCount=0;
    if(progressData.size()==10) {
        for (int i = 0; i < progressData.size(); i++) {
            boolean progress = progressData.get(i);
            if(progress==true){
                progressCount=progressCount+1;
            }

        }
    }
    System.out.println("Progress Data Count: " + progressCount);
    progressBar.setProgress(progressCount);

    questionText = (MarkdownView) findViewById(R.id.textViewQuestion);
    optionOne = (Button) findViewById(R.id.buttonOption1);
    optionTwo = (Button) findViewById(R.id.buttonOption2);
    optionThree = (Button) findViewById(R.id.buttonOption3);
    optionFour = (Button) findViewById(R.id.buttonOption4);
    optionFive = (Button) findViewById(R.id.buttonOption5);
    nextButton = (Button) findViewById(R.id.buttonNext);
    explanationButton = (Button) findViewById(R.id.buttonExplanation);
    imageButton = (Button) findViewById(R.id.buttonImage);
    questionButton = (Button) findViewById(R.id.buttonQuestion);
    explainText = (MarkdownView) findViewById(R.id.textViewExplanation);
    backgroundScroll = (ScrollView) findViewById(R.id.scrollView);
    questionImage = (TouchImageView) findViewById(R.id.imageView);
    WebSettings settingsQ = questionText.getSettings();

    questionText.setScrollBarStyle(MarkdownView.SCROLLBARS_OUTSIDE_OVERLAY);
    questionText.setScrollbarFadingEnabled(false);

    WebSettings settingsE = explainText.getSettings();
    explainText.setScrollBarStyle(MarkdownView.SCROLLBARS_OUTSIDE_OVERLAY);
    explainText.setScrollbarFadingEnabled(false);

    String myQuestion = displayBackgroundString+"\n"+displayQuestionString;
    String testQ = "my string 20% and 30% of course? There is.";
    String myExplanation = displayCoreString+"\n"+displayExplanationString;
    String testE = "my string 100% and 130% of course? There is.";
    myQuestion.replaceAll("\\s+", "\n");
    myQuestion.replaceAll("\\s+", System.getProperty("line.separator"));
    myExplanation.replaceAll("\\s+", "\n");
    myExplanation.replaceAll("\\s+", System.getProperty("line.separator"));
    myQuestion=myQuestion.replace("%", "&#37;");//replace % sign with ASCII
    testQ= testQ.replace("%", "&#37;");
    testE=testE.replace("%", "&#37;");
    myExplanation=myExplanation.replace("%", "&#37;");
    settingsQ.setJavaScriptEnabled(true);
    settingsE.setJavaScriptEnabled(true);
    questionText.getSettings().setLoadsImagesAutomatically(true);
    explainText.getSettings().setLoadsImagesAutomatically(true);

    questionText.addJavascriptInterface(new WebAppInterface(this), "Android");
    explainText.addJavascriptInterface(new WebAppInterface(this), "Android");

    questionText.loadMarkdown("<script type=\"text/javascript\">function showAndroidImage(image) {Android.showImage(image);}</script>" + myQuestion, "file:///android_asset/markdown_css_themes/foghorn.css");

    explainText.loadMarkdown("<script type=\"text/javascript\">\n" +
            "    function showAndroidImage(ImageE) {\n" +
            "        Android.showImageE(ImageE);\n" +
            "    }\n" +
            "</script>" + myExplanation, "file:///android_asset/markdown_css_themes/foghorn.css");

    optionOne.setText(myOptions.get(0).getAnswer());
    optionTwo.setText(myOptions.get(1).getAnswer());
    optionThree.setText(myOptions.get(2).getAnswer());
    optionFour.setText(myOptions.get(3).getAnswer());
    optionFive.setText(myOptions.get(4).getAnswer());

    optionOne.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean status = myOptions.get(0).isCorrectAnswer();
            if (status == true) {
                optionOne.setBackgroundColor(Color.parseColor("#4caf50"));
                questionIds.add(qID);

                if(progressData.size()==10){
                    progressData.remove(0);
                    progressData.add(true);
                }else{
                    progressData.add(true);
                }

            } else {
                optionOne.setBackgroundColor(Color.parseColor("#F44336"));
                showCorrectAnswer(1);
                if(progressData.size()==10){
                    progressData.remove(0);
                    progressData.add(false);
                }else{
                    progressData.add(false);
                }
            }
            disableOptionButtons();
            explanationButton.setEnabled(true);
            nextButton.setEnabled(true);
            questionButton.setVisibility(View.INVISIBLE);
            imageButton.setVisibility(View.INVISIBLE);
            explanationButton.setVisibility(View.VISIBLE);
        }
    });

    optionTwo.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean status = myOptions.get(1).isCorrectAnswer();
            if (status == true) {
                optionTwo.setBackgroundColor(Color.parseColor("#4caf50"));
                questionIds.add(qID);

                if(progressData.size()==10){
                    progressData.remove(0);
                    progressData.add(true);
                }else{
                    progressData.add(true);
                }

            } else {
                optionTwo.setBackgroundColor(Color.parseColor("#F44336"));
                showCorrectAnswer(2);
                if(progressData.size()==10){
                    progressData.remove(0);
                    progressData.add(false);
                }else{
                    progressData.add(false);
                }
            }
            explanationButton.setEnabled(true);
            disableOptionButtons();
            nextButton.setEnabled(true);
            questionButton.setVisibility(View.INVISIBLE);
            imageButton.setVisibility(View.INVISIBLE);
            explanationButton.setVisibility(View.VISIBLE);
        }
    });

    optionThree.setOnClickListener(new OnClickListener(){
        @Override
        public void onClick(View view) {
            boolean status = myOptions.get(2).isCorrectAnswer();
            if(status==true){
                optionThree.setBackgroundColor(Color.parseColor("#4caf50"));
                questionIds.add(qID);
                if(progressData.size()==10){
                    progressData.remove(0);
                    progressData.add(true);
                }else{
                    progressData.add(true);
                }

            }
            else{
                optionThree.setBackgroundColor(Color.parseColor("#F44336"));
                showCorrectAnswer(3);
                if(progressData.size()==10){
                    progressData.remove(0);
                    progressData.add(false);
                }else{
                    progressData.add(false);
                }
            }
            explanationButton.setEnabled(true);
            disableOptionButtons();
            nextButton.setEnabled(true);
            questionButton.setVisibility(View.INVISIBLE);
            imageButton.setVisibility(View.INVISIBLE);
            explanationButton.setVisibility(View.VISIBLE);
        }
    });

    optionFour.setOnClickListener(new OnClickListener(){
        @Override
        public void onClick(View view) {
            boolean status = myOptions.get(3).isCorrectAnswer();
            if(status==true){
                optionFour.setBackgroundColor(Color.parseColor("#4caf50"));
                questionIds.add(qID);
                if(progressData.size()==10){
                    progressData.remove(0);
                    progressData.add(true);
                }else{
                    progressData.add(true);
                }
            }
            else{
                optionFour.setBackgroundColor(Color.parseColor("#F44336"));
                showCorrectAnswer(4);
                if(progressData.size()==10){
                    progressData.remove(0);
                    progressData.add(false);
                }else{
                    progressData.add(false);
                }
            }
            explanationButton.setEnabled(true);
            disableOptionButtons();
            nextButton.setEnabled(true);
            questionButton.setVisibility(View.INVISIBLE);
            imageButton.setVisibility(View.INVISIBLE);
            explanationButton.setVisibility(View.VISIBLE);
        }
    });

    optionFive.setOnClickListener(new OnClickListener(){
        @Override
        public void onClick(View view) {
            boolean status = myOptions.get(4).isCorrectAnswer();
            if(status==true){
                optionFive.setBackgroundColor(Color.parseColor("#4caf50"));
                questionIds.add(qID);
                if(progressData.size()==10){
                    progressData.remove(0);
                    progressData.add(true);
                }else{
                    progressData.add(true);
                }
            }
            else{
                optionFive.setBackgroundColor(Color.parseColor("#F44336"));
                showCorrectAnswer(5);
                if(progressData.size()==10){
                    progressData.remove(0);
                    progressData.add(false);
                }else{
                    progressData.add(false);
                }
            }
            explanationButton.setEnabled(true);
            disableOptionButtons();
            nextButton.setEnabled(true);
            questionButton.setVisibility(View.INVISIBLE);
            imageButton.setVisibility(View.INVISIBLE);
            explanationButton.setVisibility(View.VISIBLE);
        }
    });

    nextButton.setOnClickListener(new OnClickListener(){
        @Override
        public void onClick(View view) {

            int choice = (int) (Math.random() * finalList.size());//random question
            long startTime = System.nanoTime();

            saveQuestionIds(questionIds);//save correct answer ids

            displayQ = finalList.get(choice);
            long endTime = System.nanoTime();
            long timeDifference = (endTime - startTime);
            String time = String.valueOf(timeDifference);

            count++;
            if(count>10){
                progressBar.setVisibility(View.VISIBLE);
            }

            displayQuestions(displayQ);
            questionImage.resetZoom();
            explanationButton.setEnabled(false);
            nextButton.setEnabled(false);
            explainText.setVisibility(View.INVISIBLE);
            questionText.setVisibility(View.VISIBLE);
            optionOne.setVisibility(View.VISIBLE);
            optionTwo.setVisibility(View.VISIBLE);
            optionThree.setVisibility(View.VISIBLE);
            optionFour.setVisibility(View.VISIBLE);
            optionFive.setVisibility(View.VISIBLE);
            questionImage.setVisibility(View.INVISIBLE);
            questionButton.setVisibility(View.INVISIBLE);
            explanationButton.setVisibility(View.VISIBLE);
            viewStatus = false;
            optionOne.setBackgroundColor(Color.parseColor("#D8D8D8"));
            optionTwo.setBackgroundColor(Color.parseColor("#D8D8D8"));
            optionThree.setBackgroundColor(Color.parseColor("#D8D8D8"));
            optionFour.setBackgroundColor(Color.parseColor("#D8D8D8"));
            optionFive.setBackgroundColor(Color.parseColor("#D8D8D8"));
        }
    });

    imageButton.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {

            explainText.setVisibility(View.INVISIBLE);
            questionText.setVisibility(View.INVISIBLE);
            optionOne.setVisibility(View.INVISIBLE);
            optionTwo.setVisibility(View.INVISIBLE);
            optionThree.setVisibility(View.INVISIBLE);
            optionFour.setVisibility(View.INVISIBLE);
            optionFive.setVisibility(View.INVISIBLE);
            questionImage.setVisibility(View.VISIBLE);
            explanationButton.setVisibility(View.INVISIBLE);
            questionButton.setVisibility(View.VISIBLE);
            imageButton.setVisibility(View.INVISIBLE);
        }
    });

    questionButton.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {

            if(viewStatus==false) {
                explainText.setVisibility(View.INVISIBLE);
                questionText.setVisibility(View.VISIBLE);
                optionOne.setVisibility(View.VISIBLE);
                optionTwo.setVisibility(View.VISIBLE);
                optionThree.setVisibility(View.VISIBLE);
                optionFour.setVisibility(View.VISIBLE);
                optionFive.setVisibility(View.VISIBLE);
                questionImage.setVisibility(View.INVISIBLE);
                questionImage.resetZoom();
                questionButton.setVisibility(View.INVISIBLE);
                explanationButton.setVisibility(View.VISIBLE);
            }
            if(viewStatus==true){
                explainText.setVisibility(View.INVISIBLE);
                questionText.setVisibility(View.VISIBLE);
                optionOne.setVisibility(View.VISIBLE);
                optionTwo.setVisibility(View.VISIBLE);
                optionThree.setVisibility(View.VISIBLE);
                optionFour.setVisibility(View.VISIBLE);
                optionFive.setVisibility(View.VISIBLE);
                questionImage.resetZoom();
                questionImage.setVisibility(View.INVISIBLE);
                questionButton.setVisibility(View.INVISIBLE);
                imageButton.setVisibility(View.INVISIBLE);
                explanationButton.setVisibility(View.VISIBLE);
            }
        }
    });

    explanationButton.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {

            explainText.setVisibility(View.VISIBLE);
            questionImage.resetZoom();
            questionText.setVisibility(View.INVISIBLE);
            optionOne.setVisibility(View.INVISIBLE);
            optionTwo.setVisibility(View.INVISIBLE);
            optionThree.setVisibility(View.INVISIBLE);
            optionFour.setVisibility(View.INVISIBLE);
            optionFive.setVisibility(View.INVISIBLE);
            questionImage.setVisibility(View.INVISIBLE);
            questionButton.setVisibility(View.VISIBLE);
            imageButton.setVisibility(View.INVISIBLE);
            explanationButton.setVisibility(View.INVISIBLE);
            viewStatus = true;
        }
    });
}


    private void saveQuestionIds(ArrayList<Integer> myIds){

        ArrayList<Integer> questionIds = myIds;
        String fileName = "myQuestionIds.txt";
        FileOutputStream outputStream;

        JSONArray questionArray = new JSONArray();
        for(int i=0; i<questionIds.size(); i++){
            questionArray.put(questionIds.get(i));
        }
        String questionIdsString = questionArray.toString();
        try {
            outputStream = openFileOutput("myQuestionIds.txt", Context.MODE_PRIVATE);
            outputStream.write(questionIdsString.getBytes());
            outputStream.close();
        }
        catch(FileNotFoundException er){
            er.printStackTrace();
        }
        catch(IOException er){
            er.printStackTrace();
        }




    }


    private void showCorrectAnswer(int sel){
        for (int i = 0; i < myOptions.size(); i++) {
            boolean stat = myOptions.get(i).isCorrectAnswer();
            if (stat == true) {
                int buttonID = i;
                if(sel==1){
                    if (i == 1) {
                        optionTwo.setBackgroundColor(Color.parseColor("#4caf50"));
                    } else if (i == 2) {
                        optionThree.setBackgroundColor(Color.parseColor("#4caf50"));
                    }
                    else if (i == 3) {
                        optionFour.setBackgroundColor(Color.parseColor("#4caf50"));
                    }
                    else if (i == 4) {
                        optionFive.setBackgroundColor(Color.parseColor("#4caf50"));
                    }
                }
                else if(sel==2){
                    if (i == 0) {
                        optionOne.setBackgroundColor(Color.parseColor("#4caf50"));
                    } else if (i == 2) {
                        optionThree.setBackgroundColor(Color.parseColor("#4caf50"));
                    }
                    else if (i == 3) {
                        optionFour.setBackgroundColor(Color.parseColor("#4caf50"));
                    }
                    else if (i == 4) {
                        optionFive.setBackgroundColor(Color.parseColor("#4caf50"));
                    }
                }
                else if(sel==3){
                    if (i == 0) {
                        optionOne.setBackgroundColor(Color.parseColor("#4caf50"));
                    } else if (i == 1) {
                        optionTwo.setBackgroundColor(Color.parseColor("#4caf50"));
                    }
                    else if (i == 3) {
                        optionFour.setBackgroundColor(Color.parseColor("#4caf50"));
                    }
                    else if (i == 4) {
                        optionFive.setBackgroundColor(Color.parseColor("#4caf50"));
                    }
                }
                else if(sel==4){
                    if (i == 0) {
                        optionOne.setBackgroundColor(Color.parseColor("#4caf50"));
                    } else if (i == 1) {
                        optionTwo.setBackgroundColor(Color.parseColor("#4caf50"));
                    }
                    else if (i == 2) {
                        optionThree.setBackgroundColor(Color.parseColor("#4caf50"));
                    }
                    else if (i == 4) {
                        optionFive.setBackgroundColor(Color.parseColor("#4caf50"));
                    }
                }
                else if(sel==5){
                    if (i == 0) {
                        optionOne.setBackgroundColor(Color.parseColor("#4caf50"));
                    } else if (i == 1) {
                        optionTwo.setBackgroundColor(Color.parseColor("#4caf50"));
                    }
                    else if (i == 2) {
                        optionThree.setBackgroundColor(Color.parseColor("#4caf50"));
                    }
                    else if (i == 3) {
                        optionFour.setBackgroundColor(Color.parseColor("#4caf50"));
                    }
                }
            }
        }
    }

    private void disableOptionButtons(){
        optionOne.setClickable(false);
        optionTwo.setClickable(false);
        optionThree.setClickable(false);
        optionFour.setClickable(false);
        optionFive.setClickable(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_question, menu);
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
      //      return true;
      //  }
        return super.onOptionsItemSelected(item);
    }




private class DownloadImages extends AsyncTask<Question, Integer, Integer>{
    Question myQ;

        protected Integer doInBackground(Question... urls){

            myQ = urls[0];
                try {

                    URL[] imageURLS = new URL[myQ.getImages().length];
                    for(int j=0; j<myQ.getImages().length; j++){
                        URL imageURL = new URL(myQ.getImages()[j]);
                        imageURLS[j] = imageURL;
                    }
                    int count = imageURLS.length;
                    for(int i=0; i< count; i++) {

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
                }
                catch(MalformedURLException e){
                    e.printStackTrace();
                }
                catch(IOException e){
                    e.printStackTrace();
                }
            return 1;
            }

    protected void onPostExecute(Integer result)
    {
        System.out.println("test post execute" + result);
        finalList.add(myQ);
        System.out.println("array size: "+ finalList.size());
        if(finalList.size()==1){
            int choice = (int) (Math.random() * finalList.size());//random question
            displayQ = finalList.get(0);
            System.out.println("Display Question Post Execute");
            displayQuestions(displayQ);
        }
      //  progressDialog.cancel();
        //Call your method that checks if the pictures were downloaded
    }
}
}


