package ir.maktab.onlineexam.utility;

public enum StatusTitle {
    //    PENDING(0), CONFIRMED(1);
//
//    private final int statusCode;
//
//    Status(int statusCode) {
//        this.statusCode = statusCode;
//    }
//
//    public int getStatusCode() {
//        return statusCode;
//    }
    ACTIVE("فعال"),
    INACTIVE("غیرفعال"),
    WAITING_FOR_VERIFY("در انتظار تایید");

    private String persian;

    StatusTitle(String fa) {
        persian = fa;
    }

    public String getPersian() {
        return persian;
    }
}

//class driver{
//    public static void main(String[] args) {
//        /*for (Status c : Status.values()) {
//            System.out.println(c);
//        }*/
//        Status status = Status.CONFIRMED;
//        System.out.println(status.getStatusCode());
//    }
//}
