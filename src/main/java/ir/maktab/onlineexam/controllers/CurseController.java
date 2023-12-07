package ir.maktab.onlineexam.controllers;

import ir.maktab.onlineexam.domains.Course;
import ir.maktab.onlineexam.domains.MultiChoiceQuestion;
import ir.maktab.onlineexam.domains.Quiz;
import ir.maktab.onlineexam.services.CourseService;
import ir.maktab.onlineexam.services.ProfileService;
import ir.maktab.onlineexam.services.QuizService;
import ir.maktab.onlineexam.utility.QuestionTools;
import ir.maktab.onlineexam.utility.QuestionType;
import ir.maktab.onlineexam.utility.ScoresListTools;
import ir.maktab.onlineexam.utility.SignedInAccountTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Secured("ROLE_TEACHER_GENERAL_PRIVILEGE")
@RequestMapping("/course")
public class CurseController {

    private final CourseService courseService;
    private final ProfileService accountService;
    private final QuizService quizService;
    private final SignedInAccountTools signedInAccountTools;

    @Autowired

    public CurseController(CourseService courseService, ProfileService accountService, QuizService quizService, SignedInAccountTools signedInAccountTools) {
        this.courseService = courseService;
        this.accountService = accountService;
        this.quizService = quizService;
        this.signedInAccountTools = signedInAccountTools;
    }

    @GetMapping("/{courseId}/quizzes")
    public String getCourseQuizzes(Model model, @PathVariable Long courseId){

        if (signedInAccountTools.getAccount().equals(courseService.findCourseById(courseId).getTeacher())){
            List<Quiz> requestedCourseQuizzes = courseService.findCourseById(courseId).getQuizzes();
            model.addAttribute("courseQuizzes", requestedCourseQuizzes);
            model.addAttribute("quiz",new Quiz());
            model.addAttribute("currentTeacherAccount", signedInAccountTools.getAccount());
            return "quizzes-of-course";
        }
        else {
            return "redirect:/home";
        }
    }

    @PostMapping(value = "/{courseId}/addQuiz")
    public String addQuiz(Model model, @ModelAttribute Quiz quiz, @PathVariable Long courseId){

        if (signedInAccountTools.getAccount().equals(courseService.findCourseById(courseId).getTeacher())) {

            Quiz enteredQuiz = quiz;
            if (!enteredQuiz.getTitle().isEmpty() && enteredQuiz.getTime() != null
                    && enteredQuiz.getTime() > 0 && enteredQuiz.getTime() < 1440){
                Course updatingCourse = courseService.findCourseById(courseId);
                if (enteredQuiz.getId() == null) {
                    enteredQuiz.setCourse(updatingCourse);
                    enteredQuiz.setCreatorTeacherId(signedInAccountTools.getAccount().getId());
                    updatingCourse.getQuizzes().add(enteredQuiz);
                }
                else {
                    Quiz updatingQuiz = updatingCourse.getQuizzes().stream()
                            .filter(q -> q.getId() == enteredQuiz.getId()).findFirst().get();
                    updatingQuiz.setTitle(enteredQuiz.getTitle());
                    updatingQuiz.setDescription(enteredQuiz.getDescription());
                    updatingQuiz.setTime(enteredQuiz.getTime());
                    quizService.save(updatingQuiz);
                }
                courseService.save(updatingCourse);
            }
            List<Quiz> requestedCourseQuizzes = courseService.findCourseById(courseId).getQuizzes();
            model.addAttribute("courseQuizzes", requestedCourseQuizzes);
            model.addAttribute("quiz", new Quiz());
            model.addAttribute("currentTeacherAccount", signedInAccountTools.getAccount());
            return "quizzes-of-course";
        }
        else {
            return "redirect:/menu";
        }
    }

    @GetMapping("/{courseId}/deleteQuiz/{quizId}")
    public String deleteQuiz(Model model, @PathVariable Long courseId, @PathVariable Long quizId){
        if (signedInAccountTools.getAccount().equals(courseService.findCourseById(courseId).getTeacher())) {
            Course updatingCourse = courseService.findCourseById(courseId);
            updatingCourse.getQuizzes().remove(quizService.findQuizById(quizId));
            quizService.removeQuizById(quizId);
            courseService.save(updatingCourse);

            List<Quiz> requestedCourseQuizzes = courseService.findCourseById(courseId).getQuizzes();
            model.addAttribute("courseQuizzes", requestedCourseQuizzes);
            model.addAttribute("quiz", new Quiz());
            model.addAttribute("currentTeacherAccount", signedInAccountTools.getAccount());
            return "quizzes-of-course";
        }
        else {
            return "redirect:/home";
        }
    }

    @RequestMapping("/{courseId}/editQuiz/{quizId}")
    public String editQuiz(Model model, @PathVariable Long courseId, @PathVariable Long quizId){
        if (signedInAccountTools.getAccount().equals(courseService.findCourseById(courseId).getTeacher())) {

            List<Quiz> requestedCourseQuizzes = courseService.findCourseById(courseId).getQuizzes();
            model.addAttribute("courseQuizzes", requestedCourseQuizzes);
            model.addAttribute("quiz", quizService.findQuizById(quizId));
            model.addAttribute("currentTeacherAccount", signedInAccountTools.getAccount());
            return "quizzes-of-course";
        }
        else {
            return "redirect:/home";
        }
    }

    @GetMapping("{courseId}/quiz/{quizId}/activation")
    public String quizActivation(@PathVariable Long courseId, @PathVariable Long quizId){
        if (signedInAccountTools.getAccount().equals(courseService.findCourseById(courseId).getTeacher())) {

            Quiz requestedQuiz = quizService.findQuizById(quizId);
            if (requestedQuiz.getActive() != null && requestedQuiz.getActive() == true) {
                requestedQuiz.setActive(false);
                quizService.save(requestedQuiz);

                return "redirect:/course/" + courseId + "/quizzes";
            } else {
                long numberOfMultiChoiceQuestionsOfQuiz = requestedQuiz.getQuestions().stream()
                        .filter(question -> QuestionTools.getQuestionType(question)
                                .equals(QuestionType.MultiChoiceQuestion))
                        .count();
                long numberOfMultiChoiceQuestionsWithTrueChoice = requestedQuiz.getQuestions().stream()
                        .filter(question -> QuestionTools.getQuestionType(question)
                                .equals(QuestionType.MultiChoiceQuestion))
                        .filter(question -> QuestionTools
                                .MultiChoiceQuestionContainsATrueChoice((MultiChoiceQuestion) question))
                        .count();

                boolean containsMultiChoiceQuestionWithoutTrueChoice
                        = numberOfMultiChoiceQuestionsOfQuiz > numberOfMultiChoiceQuestionsWithTrueChoice;
                boolean containsZeroDefaultScore
                        = ScoresListTools.stringToArrayList(requestedQuiz.getDefaultScoresList()).contains(0.0);

                if (containsMultiChoiceQuestionWithoutTrueChoice
                        || containsZeroDefaultScore
                        || requestedQuiz.getQuestions().size() == 0) {

                    String redirectUrl = "redirect:/course/" + courseId + "/quizzes?";
                    if (containsMultiChoiceQuestionWithoutTrueChoice)
                        redirectUrl += "&trueChoiceNotExistError";
                    if (containsZeroDefaultScore)
                        redirectUrl += "&zeroDefaultScoreError";
                    if (requestedQuiz.getQuestions().size() == 0)
                        redirectUrl += "&blankQuizError";

                    return redirectUrl;
                } else {
                    requestedQuiz.setActive(true);
                    quizService.save(requestedQuiz);
                    return "redirect:/course/" + courseId + "/quizzes";

                }
            }
        }
        else
            return "redirect:/home";
    }
}
