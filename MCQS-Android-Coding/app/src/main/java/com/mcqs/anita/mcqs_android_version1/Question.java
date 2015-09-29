package com.mcqs.anita.mcqs_android_version1;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.bluelinelabs.logansquare.annotation.OnJsonParseComplete;
import com.bluelinelabs.logansquare.annotation.OnPreJsonSerialize;

/**
 * Created by david-MCQS on 07/09/2015.
 */
@JsonObject
public class Question{

   // @JsonField(name = "options")
    private QuestionOptions[] questionOptions = new QuestionOptions[5];

    @JsonField
    private String background;

    @JsonField
    private String question;

    @JsonField
    private String core;

    @JsonField
    private String explanation;

    private int index;


    private String imagePath;



    public Question(){
        //default constructor
    }
    public Question(QuestionOptions[] questionOptions, String background, String question, String core, String explanation){
        this.questionOptions = questionOptions;
        this.background = background;
        this.question = question;
        this.core = core;
        this.explanation=explanation;
    }



    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public QuestionOptions[] getQuestionOptions() {
        return questionOptions;
    }

    public void setQuestionOptions(QuestionOptions[] questionOptions) {
        this.questionOptions = questionOptions;
    }


    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCore() {
        return core;
    }

    public void setCore(String core) {
        this.core = core;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    @OnJsonParseComplete
    void onParseComplete() {
        // Do some fancy post-processing stuff after parsing here
    }

    /*
     * Optional callback method to do something before your
     * object serializes.
     */
    @OnPreJsonSerialize
    void onPreSerialize() {
        // Do some fancy pre-processing stuff before serializing here
    }
}
