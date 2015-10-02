package com.mcqs.anita.mcqs_android_version1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.graphics.Color;
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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import us.feras.mdv.MarkdownView;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.view.View.OnClickListener;
import android.widget.Toast;

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
    private ImageView questionImage;
    private TextView actionBarTitle;
    private int count=1;
    private int jsonArraySize1;
    private int myCount=3;
    private Question displayQ = new Question();
    private List<Question> qList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_question);




        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        actionBarTitle = (TextView) findViewById(R.id.action_bar_text);
        actionBarTitle.setText(R.string.title_activity_view_question);


       // Intent i = this.getIntent();
       // questionList = i.getParcelableArrayListExtra("questions");

        checkFiles();//if there don't copy file
        myJSONString =  readFromFile();

        JsonParser jsonParser = new JsonParser();
        jsonArraySize1 = jsonParser.parse(myJSONString).getAsJsonArray().size();

        try {
         qList= LoganSquare.parseList(myJSONString, Question.class);
           System.out.println(qList.size()+" length");
            int choice = (int) (Math.random() * qList.size());//random question

            displayQ = qList.get(choice);


        }
        catch(IOException er){
            er.printStackTrace();
       }
        catch(Exception e){
            e.printStackTrace();
        }


        //parse json as you go
      //  displayQ = parseJSONFile(myJSONString);//parse random question

        displayQuestions(displayQ);
        registerForContextMenu(questionText);
        registerForContextMenu(explainText);

    }





    //parse entire JSON File - first 5 nodes
    private Question parseJSONFile(String myJSONString) {
        //questionList - array of questions
        int jsonArraySize;

        JsonParser jsonParser = new JsonParser();
        jsonArraySize = jsonParser.parse(myJSONString).getAsJsonArray().size();
        int choice = (int) (Math.random() * jsonArraySize);//random question
        System.out.println("JSONArraySize: " + jsonArraySize);




            Question myQuestion = new Question();

            String background = "";
            String question = "";
            String core = "";
            String explanation = "";


            //incorrect answers
            int length = jsonParser.parse(myJSONString)
                    .getAsJsonArray().get(choice).getAsJsonObject().getAsJsonArray("options").size();

            QuestionOptions[] questionOptions = new QuestionOptions[length];
      //  System.out.println("Question Options: "+ length+ " Question No: " +choice);



        for(int j=0; j<length; j++){
            String option = jsonParser.parse(myJSONString)
                    .getAsJsonArray().get(choice).getAsJsonObject().getAsJsonArray("options").get(j).getAsJsonObject().get("answer").getAsString();
            Boolean correct = jsonParser.parse(myJSONString)
                    .getAsJsonArray().get(choice).getAsJsonObject().getAsJsonArray("options").get(j).getAsJsonObject().get("correctAnswer").getAsBoolean();
            //.getAsJsonObject().getAsJsonArray("incorrectAnswers").get(0)
              //      .getAsJsonObject().getAsJsonArray("incorrectAnswer").get(j).getAsJsonObject().get("_").getAsString();
            QuestionOptions incorr = new QuestionOptions(option, correct);
            questionOptions[j] = incorr;
        }

        myQuestion.setQuestionOptions(questionOptions);


        /*
            //correct answer
            String correctAnswer = jsonParser.parse(myJSONString)
                    .getAsJsonArray().get(choice).getAsJsonObject().getAsJsonArray("options").get(0).getAsJsonObject().getAsJsonArray("correctAnswers").get(0)
                    .getAsJsonObject().getAsJsonArray("correctAnswer").get(0).getAsJsonObject().get("_").getAsString();

           //System.out.println("correct: " + correctAnswer);


            QuestionOptions corr = new QuestionOptions(correctAnswer, true);
            questionOptions[0] = corr;



            for(int j=0; j<length; j++){
                String myIncorrectOption = jsonParser.parse(myJSONString)
                        .getAsJsonArray().get(choice).getAsJsonObject().getAsJsonArray("options").get(0).getAsJsonObject().getAsJsonArray("incorrectAnswers").get(0)
                        .getAsJsonObject().getAsJsonArray("incorrectAnswer").get(j).getAsJsonObject().get("_").getAsString();
                QuestionOptions incorr = new QuestionOptions(myIncorrectOption, false);
                questionOptions[j+1] = incorr;
            }

            myQuestion.setQuestionOptions(questionOptions);

*/




            //explanation
            explanation = jsonParser.parse(myJSONString)
                    .getAsJsonArray().get(choice).getAsJsonObject().getAsJsonPrimitive("explanation").getAsString();
            myQuestion.setExplanation(explanation);

            //core
            core = jsonParser.parse(myJSONString)
                    .getAsJsonArray().get(choice).getAsJsonObject().getAsJsonPrimitive("core").getAsString();
            myQuestion.setCore(core);

            //question
            question = jsonParser.parse(myJSONString)
                    .getAsJsonArray().get(choice).getAsJsonObject().getAsJsonPrimitive("question").getAsString();
            myQuestion.setQuestion(question);

            //background
            background = jsonParser.parse(myJSONString)
                    .getAsJsonArray().get(choice).getAsJsonObject().getAsJsonPrimitive("background").getAsString();
            myQuestion.setBackground(background);

      //  System.out.println("explanation: " + explanation);
       // System.out.println("core: "+ core);
      //  System.out.println("question: "+ question);
      //  System.out.println("background: "+ background);

        return myQuestion;
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




    private void checkFiles() {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        AssetManager assetMgr = getAssets();
        InputStream in = null;
        OutputStream out = null;
        String toPath = "/data/data/" + getPackageName()+"/files/";
        String toPathImages = "/data/data/" + getPackageName()+"/files/images";
        Boolean fileThere = fileExistance("myJSON.txt");
        if(fileThere==true)
        {
            // System.out.println("not empty");
        }
        else
        {
            copyAssetFolder(assetMgr, "json", toPath);
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





private void displayQuestions(Question myQ){

    int questionNumbers = questionList.size();
    int choice = (int) (Math.random() * questionNumbers);//random question
    //Question displayQuestion = questionList.get(choice);
    Question displayQuestion = myQ;
 //   Question displayQuestion = parseJSONQuestion(myJSONString);
    String displayBackgroundString = displayQuestion.getBackground();
    String displayQuestionString = displayQuestion.getQuestion();
    String displayCoreString = displayQuestion.getCore();
    String displayExplanationString = displayQuestion.getExplanation();
  //  String displayImagePath = displayQuestion.getImagePath();
    QuestionOptions[] questionOptions = displayQuestion.getQuestionOptions();
    myOptions = new ArrayList<QuestionOptions>(Arrays.asList(questionOptions));
    Collections.shuffle(myOptions);//shuffle options


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
   // explainScroll = (ScrollView) findViewById(R.id.scrollViewEx);
    backgroundScroll = (ScrollView) findViewById(R.id.scrollView);
    questionImage = (ImageView) findViewById(R.id.imageView);
    WebSettings settingsQ = questionText.getSettings();

    questionText.setScrollBarStyle(MarkdownView.SCROLLBARS_OUTSIDE_OVERLAY);
    questionText.setScrollbarFadingEnabled(false);

    WebSettings settingsE = explainText.getSettings();
    explainText.setScrollBarStyle(MarkdownView.SCROLLBARS_OUTSIDE_OVERLAY);
    explainText.setScrollbarFadingEnabled(false);

    final String myQuestion = displayBackgroundString+"\n"+displayQuestionString;
    String myExplanation = displayCoreString+"\n"+displayExplanationString;
    myQuestion.replaceAll("\\s+", "\n");
    myQuestion.replaceAll("\\s+", System.getProperty("line.separator"));
    myExplanation.replaceAll("\\s+", "\n");
    myExplanation.replaceAll("\\s+", System.getProperty("line.separator"));
    settingsQ.setJavaScriptEnabled(true);
    questionText.getSettings().setLoadsImagesAutomatically(true);

   // questionText.setWebViewClient(new MyBrowser());
    questionText.addJavascriptInterface(new WebAppInterface(this), "Android");
   // questionText.addJavascriptInterface(new WebAppInterface(this), "Android");
    questionText.loadMarkdown("<script type=\"text/javascript\">\n" +
            "    function showAndroidToast(toast) {\n" +
            "        Android.showToast(toast);\n" +
            "    }\n" +
            "</script>" +
            "test1" +"<img  name=\"submit\" src=\"file:///data/data/com.mcqs.anita.mcqs_android_coding/files/images/1b.JPG\" onclick=\"showAndroidToast(this.src)\">"+
            " \ntest12   ", "file:///android_asset/markdown_css_themes/foghorn.css");
    //questionText.loadUrl("file:///android_asset/index.html");

//width=""+"100%"+""

//  "test" +"<img name=\"submit\" src=\"file:///data/data/\" + getPackageName() + \"/files/images\"+\"1b.JPG\" onclick=\"showAndroidToast(this.src)\">"+

//"<img name="submit" src="http://mcqs.com/frcr1a/images/f1/1b.JPG" onclick="showAndroidToast(this.src)">

    explainText.loadMarkdown(myExplanation, "file:///android_asset/markdown_css_themes/foghorn.css");


    optionOne.setText(myOptions.get(0).getAnswer());
    //System.out.println(myOptions.get(0).getAnswer());
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
            } else {
                optionOne.setBackgroundColor(Color.parseColor("#F44336"));
                showCorrectAnswer(1);
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
            } else {
                optionTwo.setBackgroundColor(Color.parseColor("#F44336"));
                showCorrectAnswer(2);
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
            }
            else{
                optionThree.setBackgroundColor(Color.parseColor("#F44336"));
                showCorrectAnswer(3);
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
            }
            else{
                optionFour.setBackgroundColor(Color.parseColor("#F44336"));
                showCorrectAnswer(4);
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
            }
            else{
                optionFive.setBackgroundColor(Color.parseColor("#F44336"));
                showCorrectAnswer(5);
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

            int choice = (int) (Math.random() * qList.size());//random question

          //  System.out.println("time difference: ");
            long startTime = System.nanoTime();
           // displayQ = parseJSONFile(myJSONString);
            displayQ = qList.get(choice);
            long endTime = System.nanoTime();
            long timeDifference = (endTime - startTime);
            String time = String.valueOf(timeDifference);
            System.out.println("time difference: " + timeDifference + " Nanoseconds");


            displayQuestions(displayQ);

            explanationButton.setEnabled(false);
            nextButton.setEnabled(false);
            explainText.setVisibility(View.INVISIBLE);
           // explainScroll.setVisibility(View.INVISIBLE);
          //  explainScroll.scrollTo(0, 0);
          //  backgroundScroll.setVisibility(View.VISIBLE);
          //  backgroundScroll.scrollTo(0, 0);
            questionText.setVisibility(View.VISIBLE);
            optionOne.setVisibility(View.VISIBLE);
            optionTwo.setVisibility(View.VISIBLE);
            optionThree.setVisibility(View.VISIBLE);
            optionFour.setVisibility(View.VISIBLE);
            optionFive.setVisibility(View.VISIBLE);
            questionImage.setVisibility(View.INVISIBLE);
            questionButton.setVisibility(View.INVISIBLE);
          //  imageButton.setVisibility(View.VISIBLE);
            explanationButton.setVisibility(View.VISIBLE);
            viewStatus = false;
           // System.out.println("questionList Size: "+questionList.size());
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
        //    explainScroll.setVisibility(View.INVISIBLE);
           // backgroundScroll.setVisibility(View.INVISIBLE);
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
               // explainScroll.setVisibility(View.INVISIBLE);
               // backgroundScroll.setVisibility(View.VISIBLE);
                questionText.setVisibility(View.VISIBLE);
                optionOne.setVisibility(View.VISIBLE);
                optionTwo.setVisibility(View.VISIBLE);
                optionThree.setVisibility(View.VISIBLE);
                optionFour.setVisibility(View.VISIBLE);
                optionFive.setVisibility(View.VISIBLE);
                questionImage.setVisibility(View.INVISIBLE);
                questionButton.setVisibility(View.INVISIBLE);
                imageButton.setVisibility(View.VISIBLE);
                explanationButton.setVisibility(View.INVISIBLE);
            }
            if(viewStatus==true){
                explainText.setVisibility(View.INVISIBLE);
             //   explainScroll.setVisibility(View.INVISIBLE);
             //   backgroundScroll.setVisibility(View.VISIBLE);
                questionText.setVisibility(View.VISIBLE);
                optionOne.setVisibility(View.VISIBLE);
                optionTwo.setVisibility(View.VISIBLE);
                optionThree.setVisibility(View.VISIBLE);
                optionFour.setVisibility(View.VISIBLE);
                optionFive.setVisibility(View.VISIBLE);
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
          //  explainScroll.setVisibility(View.VISIBLE);
           // backgroundScroll.setVisibility(View.INVISIBLE);
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











}


