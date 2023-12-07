package ir.maktab.onlineexam.utility;

public enum RoleTitle {
    MANAGER("مدیر"),
    TEACHER("استاد"),
    STUDENT("دانشجو");

    //adding roles must be added in initializer too

    private String persian;
    RoleTitle(String fa){
        persian = fa;
    }
    public String getPersian(){
        return persian;
    }
}