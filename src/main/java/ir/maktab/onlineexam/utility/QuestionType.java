package ir.maktab.onlineexam.utility;

public enum QuestionType {
    DetailedQuestion("سوال تشریحی"),
    MultiChoiceQuestion("سوال تستی");

    //adding question types must be added here

    private String persian;
    QuestionType(String fa){
        persian = fa;
    }
    public String getPersian(){
        return persian;
    }
}