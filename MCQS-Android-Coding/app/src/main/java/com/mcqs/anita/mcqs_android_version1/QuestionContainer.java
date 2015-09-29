package com.mcqs.anita.mcqs_android_version1;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

/**
 * Created by david-MCQS on 29/09/2015.
 */
@JsonObject
public class QuestionContainer {

    @JsonField
    private List<Question> question;

    public List<Question> getQuestion(){
        return question;
    }
    public void setQuestion(List<Question> question){
        this.question = question;
    }



}
