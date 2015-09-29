package com.mcqs.anita.mcqs_android_version1;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by david-MCQS on 07/09/2015.
 */
public class Question implements Parcelable{

    private int index;


    private String background;
    private String question;
    private QuestionOptions[] questionOptions = new QuestionOptions[5];
    private String core;
    private String explanation;
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
    public Question(Parcel in){
        readFromParcel(in);
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(background);
        dest.writeString(question);
        //dest.writeArray(questionOptions);
       // dest.writeParcelable(questionOptions, flags);
       // dest.writeParcelableArray(questionOptions, flags);
        dest.writeTypedArray(questionOptions, flags);
        dest.writeString(core);
        dest.writeString(explanation);
        dest.writeString(imagePath);
    }
    private void readFromParcel(Parcel in){
        background = in.readString();
        question = in.readString();
        questionOptions = in.createTypedArray(QuestionOptions.CREATOR);
        //questionOptions = in.readParcelableArray(QuestionOptions.class.getClassLoader());
        core = in.readString();
        explanation = in.readString();
        imagePath = in.readString();
    }

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public Question createFromParcel(Parcel in) {
                    return new Question(in);
                }

                public Question[] newArray(int size) {
                    return new Question[size];
                }
            };

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
}
