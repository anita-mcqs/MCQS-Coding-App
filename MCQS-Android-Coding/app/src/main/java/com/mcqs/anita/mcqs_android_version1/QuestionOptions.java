package com.mcqs.anita.mcqs_android_version1;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by david-MCQS on 08/09/2015.
 */
public class QuestionOptions implements Parcelable{

    private boolean correctAnswer;//true - correct
    private String answer;

    public QuestionOptions(){

    }
    public QuestionOptions(Parcel in){
        readFromParcel(in);
    }
    public QuestionOptions(String answer, boolean correctAnswer){
        this.answer = answer;
        this.correctAnswer = correctAnswer;
    }
    public int describeContents(){
        return 0;
    }
    public void writeToParcel(Parcel dest, int flags){
         dest.writeValue(correctAnswer);
         dest.writeString(answer);
    }
    public void readFromParcel(Parcel in){
        correctAnswer = (Boolean) in.readValue(null);
        answer = in.readString();
    }

    public static final Parcelable.Creator<QuestionOptions> CREATOR = new Parcelable.Creator<QuestionOptions>(){
      public QuestionOptions createFromParcel(Parcel in){
          return new QuestionOptions(in);
      }
        public QuestionOptions[] newArray(int size){
            return new QuestionOptions[size];
        }

    };
    public QuestionOptions(boolean correctAnswer){
        this.correctAnswer = correctAnswer;
    }

    public boolean isCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(boolean correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }



}
