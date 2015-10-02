package com.mcqs.anita.mcqs_android_version1;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import us.feras.mdv.MarkdownView;

/**
 * Created by david-MCQS on 01/10/2015.
 */
public class WebAppInterface {
    Context mContext;
   // ImageView questionImage = (ImageView) findViewById(R.id.imageView);
    Activity activity;

    WebAppInterface(Activity activity) {
        this.activity = activity;
    }
    /** Instantiate the interface and set the context */
   // WebAppInterface(Context c) {
     //   mContext = c;
   // }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(final String toast) {




        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                ImageView questionImage = (ImageView) activity.findViewById(R.id.imageView);
                MarkdownView explainText = (MarkdownView) activity.findViewById(R.id.textViewExplanation);
                MarkdownView questionText = (MarkdownView) activity.findViewById(R.id.textViewQuestion);
                Button optionOne = (Button) activity.findViewById(R.id.buttonOption1);
                Button optionTwo = (Button) activity.findViewById(R.id.buttonOption2);
                Button optionThree = (Button) activity.findViewById(R.id.buttonOption3);
                Button optionFour = (Button) activity.findViewById(R.id.buttonOption4);
                Button optionFive = (Button) activity.findViewById(R.id.buttonOption5);
                Button explanationButton = (Button) activity.findViewById(R.id.buttonExplanation);
                Button questionButton = (Button) activity.findViewById(R.id.buttonQuestion);
                Button imageButton = (Button) activity.findViewById(R.id.buttonImage);
                // button.setBackgroundResource(R.drawable.practice_ri_wrong_3);
                File file = new File(toast);

                 questionImage.setImageURI(Uri.parse(toast));
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
                if (questionImage != null) {
                    System.out.println("imageViewWebApp");
                }
              //  Toast.makeText(activity, toast, Toast.LENGTH_SHORT).show();
            }
        });





        //ViewQuestion.class.  ImageView questionImage = (ImageView) findViewById(R.id.imageView);



    }
    @JavascriptInterface
    public void performClick(String value){
        System.out.println("perform click: "+ value);
    }
}
