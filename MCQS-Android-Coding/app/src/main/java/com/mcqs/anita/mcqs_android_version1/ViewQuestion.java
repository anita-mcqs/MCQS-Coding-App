package com.mcqs.anita.mcqs_android_version1;

import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.view.View;
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

import android.view.View.OnClickListener;

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
        //parse json as you go
        parseJSONFile(myJSONString,0,2);//parse first 3 questions



        displayQuestions();

    }





    //parse entire JSON File - first 5 nodes
    private void parseJSONFile(String myJSONString, int start, int finish) {
        //questionList - array of questions
        int jsonArraySize;

        JsonParser jsonParser = new JsonParser();
        jsonArraySize = jsonParser.parse(myJSONString).getAsJsonArray().size();
        System.out.println("JSONArraySize: " + jsonArraySize);


        //JSON - for(int i=0; i<jsonArraySize;i++){
        for(int i=start; i<finish;i++){

            Question myQuestion = new Question();

            String background = "";
            String question = "";
            String core = "";
            String explanation = "";


            //incorrect answers
            int length = jsonParser.parse(myJSONString)
                    .getAsJsonArray().get(i).getAsJsonObject().getAsJsonArray("options").get(0).getAsJsonObject().getAsJsonArray("incorrectAnswers").get(0)
                    .getAsJsonObject().getAsJsonArray("incorrectAnswer").size();

            QuestionOptions[] questionOptions = new QuestionOptions[length+1];
            if(length>4){
                System.out.println("Question Options: "+ length+ " Question No: " +i);
            }
            //correct answer
            String correctAnswer = jsonParser.parse(myJSONString)
                    .getAsJsonArray().get(i).getAsJsonObject().getAsJsonArray("options").get(0).getAsJsonObject().getAsJsonArray("correctAnswers").get(0)
                    .getAsJsonObject().getAsJsonArray("correctAnswer").get(0).getAsJsonObject().get("_").getAsString();

           //System.out.println("correct: " + correctAnswer);


            QuestionOptions corr = new QuestionOptions(correctAnswer, true);
            questionOptions[0] = corr;



            for(int j=0; j<length; j++){
                String myIncorrectOption = jsonParser.parse(myJSONString)
                        .getAsJsonArray().get(i).getAsJsonObject().getAsJsonArray("options").get(0).getAsJsonObject().getAsJsonArray("incorrectAnswers").get(0)
                        .getAsJsonObject().getAsJsonArray("incorrectAnswer").get(j).getAsJsonObject().get("_").getAsString();
                QuestionOptions incorr = new QuestionOptions(myIncorrectOption, false);
                questionOptions[j+1] = incorr;
            }

            myQuestion.setQuestionOptions(questionOptions);

            //explanation
            explanation = jsonParser.parse(myJSONString)
                    .getAsJsonArray().get(i).getAsJsonObject().getAsJsonArray("explanation").get(0).getAsString();
            myQuestion.setExplanation(explanation);

            //core
            core = jsonParser.parse(myJSONString)
                    .getAsJsonArray().get(i).getAsJsonObject().getAsJsonArray("core").get(0).getAsString();
            myQuestion.setCore(core);

            //question
            question = jsonParser.parse(myJSONString)
                    .getAsJsonArray().get(i).getAsJsonObject().getAsJsonArray("question").get(0).getAsString();
            myQuestion.setQuestion(question);

            //background
            background = jsonParser.parse(myJSONString)
                    .getAsJsonArray().get(i).getAsJsonObject().getAsJsonArray("background").get(0).getAsString();
            myQuestion.setBackground(background);

            questionList.add(myQuestion);
          //  System.out.println("questionlistsize: "+ questionList.size());
        }
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
       // String toPathImages = "/data/data/" + getPackageName()+"/files/images";
        Boolean fileThere = fileExistance("myJSON.txt");
        if(fileThere==true)
        {
            // System.out.println("not empty");
        }
        else
        {
            copyAssetFolder(assetMgr, "json", toPath);
          //  copyAssetFolder(assetMgr, "myImages", toPathImages);
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





private void displayQuestions(){

    int questionNumbers = questionList.size();
    int choice = (int) (Math.random() * questionNumbers);//random question
    Question displayQuestion = questionList.get(choice);
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
    explainScroll = (ScrollView) findViewById(R.id.scrollViewEx);
    backgroundScroll = (ScrollView) findViewById(R.id.scrollView);
    questionImage = (ImageView) findViewById(R.id.imageView);


    String myQuestion = displayBackgroundString+"\n"+displayQuestionString;
    String myExplanation = displayCoreString+"\n"+displayExplanationString;
    myQuestion.replaceAll("\\s+", "\n");
    myQuestion.replaceAll("\\s+", System.getProperty("line.separator"));
    myExplanation.replaceAll("\\s+", "\n");
    myExplanation.replaceAll("\\s+", System.getProperty("line.separator"));



    //Markdown View
    questionText.loadMarkdown(myQuestion, "file:///android_asset/markdown_css_themes/foghorn.css");
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
            questionButton.setVisibility(View.INVISIBLE);
            imageButton.setVisibility(View.INVISIBLE);
            explanationButton.setVisibility(View.VISIBLE);
        }
    });

    nextButton.setOnClickListener(new OnClickListener(){
        @Override
        public void onClick(View view) {
            displayQuestions();

            nextParseJson(myCount);


            explanationButton.setEnabled(false);
            explainText.setVisibility(View.INVISIBLE);
            explainScroll.setVisibility(View.INVISIBLE);
            explainScroll.scrollTo(0, 0);
            backgroundScroll.setVisibility(View.VISIBLE);
            backgroundScroll.scrollTo(0, 0);
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
            System.out.println("questionList Size: "+questionList.size());
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
            explainScroll.setVisibility(View.INVISIBLE);
            backgroundScroll.setVisibility(View.INVISIBLE);
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
                explainScroll.setVisibility(View.INVISIBLE);
                backgroundScroll.setVisibility(View.VISIBLE);
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
                explainScroll.setVisibility(View.INVISIBLE);
                backgroundScroll.setVisibility(View.VISIBLE);
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
            explainScroll.setVisibility(View.VISIBLE);
            backgroundScroll.setVisibility(View.INVISIBLE);
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

private void nextParseJson(int count1){

System.out.println("count: "+ myCount);
  //  if(count1==1){
    if(myCount<jsonArraySize1){
        //do nothing - outside json array
        parseJSONFile(myJSONString, myCount, myCount + 1);
        myCount++;
    }else {

    }
        //count=2;//count = 2
   // }
//    else if(count1==2){
//        parseJSONFile(myJSONString, 11,15);
//        count=3;//count = 3
//    }
//    else if(count1==3){
//        parseJSONFile(myJSONString, 16,20);
//        count++;//count = 4
//    }
//    else if(count1==4){
//        parseJSONFile(myJSONString, 21,25);
//        count++;//count = 5
//    }
//    else if(count1==5){
//        parseJSONFile(myJSONString, 26,30);
//        count++;//count = 6
//    }
//    else if(count1==6){
//        parseJSONFile(myJSONString, 31,35);
//        count++;//count = 7
//    }
//    else if(count1==7){
//        parseJSONFile(myJSONString, 36,40);
//        count++;//count = 8
//    }
//    else if(count1==8){
//        parseJSONFile(myJSONString, 41,45);
//        count++;//count = 9
//    }
//    else if(count1==9){
//        parseJSONFile(myJSONString, 46,50);
//        count++;//count = 10
//    }
//    else if(count1==10){
//        parseJSONFile(myJSONString, 51,55);
//        count++;//count = 11
//    }
//    else if(count1==11){
//        parseJSONFile(myJSONString, 56,60);
//        count++;//count = 12
//    }
//    else if(count1==12){
//        parseJSONFile(myJSONString, 61,65);
//        count++;//count = 13
//    }
//    else if(count1==13){
//        parseJSONFile(myJSONString, 66,70);
//        count++;//count = 14
//    }
//    else if(count1==14){
//        parseJSONFile(myJSONString, 71,75);
//        count++;//count = 15
//    }
//    else if(count1==15){
//        parseJSONFile(myJSONString, 76,80);
//        count++;//count = 16
//    }
//    else if(count1==16){
//        parseJSONFile(myJSONString, 81,85);
//        count++;//count = 17
//    }
//    else if(count1==17){
//        parseJSONFile(myJSONString, 86,90);
//        count++;//count = 18
//    }
//    else if(count1==18){
//        parseJSONFile(myJSONString, 91,95);
//        count++;//count = 19
//    }
//    else if(count1==19){
//        parseJSONFile(myJSONString, 96,99);
//        count=0;
//    }
//    else{
//        //do nothing
//    }

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







    private class ParseJSONQuestion extends AsyncTask<String, Integer, ArrayList<Question>> {

        Question myQuestion = new Question();
        @Override
        protected ArrayList<Question> doInBackground(String... urls){

            int jsonArraySize;
           // ArrayList<Question> myQuestions = new ArrayList<Question>();
            JsonParser jsonParser = new JsonParser();
            jsonArraySize = jsonParser.parse(myJSONString).getAsJsonArray().size();
            String choice = urls[0];

            //JSON
            for(int i=10; i<jsonArraySize;i++){

                String background = "";
                String question = "";
                String core = "";
                String explanation = "";

                //incorrect answers
                int length = jsonParser.parse(myJSONString)
                        .getAsJsonArray().get(i).getAsJsonObject().getAsJsonArray("options").get(0).getAsJsonObject().getAsJsonArray("incorrectAnswers").get(0)
                        .getAsJsonObject().getAsJsonArray("incorrectAnswer").size();

                QuestionOptions[] questionOptions = new QuestionOptions[length+1];

                //correct answer
                String correctAnswer = jsonParser.parse(myJSONString)
                        .getAsJsonArray().get(i).getAsJsonObject().getAsJsonArray("options").get(0).getAsJsonObject().getAsJsonArray("correctAnswers").get(0)
                        .getAsJsonObject().getAsJsonArray("correctAnswer").get(0).getAsJsonObject().get("_").getAsString();
                QuestionOptions corr = new QuestionOptions(correctAnswer, true);
                questionOptions[0] = corr;

                for(int j=0; j<length; j++){
                    String myIncorrectOption = jsonParser.parse(myJSONString)
                            .getAsJsonArray().get(i).getAsJsonObject().getAsJsonArray("options").get(0).getAsJsonObject().getAsJsonArray("incorrectAnswers").get(0)
                            .getAsJsonObject().getAsJsonArray("incorrectAnswer").get(j).getAsJsonObject().get("_").getAsString();
                    QuestionOptions incorr = new QuestionOptions(myIncorrectOption, false);
                    questionOptions[j+1] = incorr;
                }


                myQuestion.setQuestionOptions(questionOptions);

                //explanation
                explanation = jsonParser.parse(myJSONString)
                        .getAsJsonArray().get(i).getAsJsonObject().getAsJsonArray("explanation").get(0).getAsString();
                myQuestion.setExplanation(explanation);

                //core
                core = jsonParser.parse(myJSONString)
                        .getAsJsonArray().get(i).getAsJsonObject().getAsJsonArray("core").get(0).getAsString();
                myQuestion.setCore(core);

                //question
                question = jsonParser.parse(myJSONString)
                        .getAsJsonArray().get(i).getAsJsonObject().getAsJsonArray("question").get(0).getAsString();
                myQuestion.setQuestion(question);


                //background
                background = jsonParser.parse(myJSONString)
                        .getAsJsonArray().get(i).getAsJsonObject().getAsJsonArray("background").get(0).getAsString();
                myQuestion.setBackground(background);


                questionList.add(myQuestion);

            }


      //  System.out.println("myQuestion: " + myQuestion.getQuestion());

            return questionList;

        }

      //  protected void onPostExecute(Question result)
      //  {
      //      myQuestion = result;
     //   }





    }







}

