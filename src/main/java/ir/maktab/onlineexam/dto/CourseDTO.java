package ir.maktab.onlineexam.dto;


public class CourseDTO {

    private Long id;
    private String title;
    private String startDate;
    private String finishDate;
    private String category;
//    private Boolean isActive;
//    private Boolean hasTeacher;

    //NoArgsConstructor
    public CourseDTO() {
    }

    //  AllArgsConstructor
    public CourseDTO(Long id, String title, String startDate, String finishDate/*, String category*/) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.category = category;/**/
    }

    //  Getter and Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
