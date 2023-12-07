package ir.maktab.onlineexam.controllers;

import ir.maktab.onlineexam.domains.*;
import ir.maktab.onlineexam.dto.AnswerDTO;
import ir.maktab.onlineexam.services.CourseService;
import ir.maktab.onlineexam.services.ProfileService;
import ir.maktab.onlineexam.services.QuizOperationService;
import ir.maktab.onlineexam.services.QuizService;
import ir.maktab.onlineexam.utility.AutoFinishRunnable;
import ir.maktab.onlineexam.utility.SignedInAccountTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@EnableScheduling
@Controller
@RequestMapping("/student")
@Secured("ROLE_STUDENT_GENERAL_PRIVILEGE")
public class StudentController {

    private ProfileService accountService;
    private CourseService courseService;
    private SignedInAccountTools signedInAccountTools;
    private QuizOperationService quizOperationService;
    private QuizService quizService;

    @Autowired
    public StudentController(CourseService courseService,
                             SignedInAccountTools signedInAccountTools,
                             ProfileService accountService,
                             QuizOperationService quizOperationService,
                             QuizService quizService) {
        this.courseService = courseService;
        this.signedInAccountTools = signedInAccountTools;
        this.accountService = accountService;
        this.quizOperationService = quizOperationService;
        this.quizService = quizService;
    }

    @RequestMapping(value = "")
    public String getStudentPage() {
        return "redirect:/home";
    }

    @GetMapping("/courses")
    public String getCourses(Model model) {

        model.addAttribute("studentCourses", courseService.findCourseByStudentsContains(signedInAccountTools.getAccount()));
        model.addAttribute("currentStudent", signedInAccountTools.getAccount());
        return "student-courses";
    }



    @GetMapping("/{studentId}/course/{courseId}/quizzes")
    public String getCourseItemQuizzes(Model model, @PathVariable Long studentId, @PathVariable Long courseId) {
        Profile p1 = signedInAccountTools.getAccount();
        Profile p2 = accountService.findProfileById(studentId).get();
        if (signedInAccountTools.getAccount().equals(accountService.findProfileById(studentId).get())) {

            model.addAttribute("courseQuizzes", courseService.findCourseById(courseId).getQuizzes());
            model.addAttribute("currentStudent", signedInAccountTools.getAccount());
            model.addAttribute("idsOfFinishedQuizzes",
                    quizOperationService.findAllByStudentIdAndCourseId(studentId, courseId).stream()
                            .filter(quizOperation -> quizOperation.getFinished() != null
                                    && quizOperation.getFinished() == true)
                            .map(QuizOperation::getQuizId)
                            .collect(Collectors.toList()));
            model.addAttribute("idsOfInProgressQuizzes",
                    quizOperationService.findAllByStudentIdAndCourseId(studentId, courseId).stream()
                            .filter(quizOperation -> quizOperation.getFinished() == null
                                    || quizOperation.getFinished() == false)
                            .map(QuizOperation::getQuizId)
                            .collect(Collectors.toList()));
            return "quizzes-of-student-course";
        } else
            return "redirect:/home";
    }

    @GetMapping("/{studentId}/course/{courseId}/quiz/{quizId}/enterQuizOperation/question/{questionNumber}")
    public String startQuizOperation(Model model,
                                     @PathVariable Long studentId,
                                     @PathVariable Long courseId,
                                     @PathVariable Long quizId,
                                     @PathVariable Integer questionNumber){
        if (signedInAccountTools.getAccount().equals(accountService.findProfileById(studentId).get())) {

            QuizOperation quizOperation;
            if (quizOperationService.quizOperationExist(studentId, courseId, quizId)) {
                quizOperation = quizOperationService.findByStudentIdAndCourseIdAndQuizId(studentId, courseId, quizId);
            }
            else {
                // TODO: 1/3/2021 work on cookies an sessions

                quizOperation = new QuizOperation();
                quizOperation.setCourseId(courseId);
                quizOperation.setStudentId(studentId);
                quizOperation.setQuizId(quizId);
                Date startTime = new Date();
                quizOperation.setStartTime(startTime);
                quizOperation.setFinishDate(new Date(startTime.getTime()
                        + quizService.findQuizById(quizId).getTime() * 60000));
                quizOperation = quizOperationService.save(quizOperation);


                Executors.newScheduledThreadPool(1).schedule(
                        new AutoFinishRunnable(quizId,studentId)::run,
                        quizService.findQuizById(quizId).getTime(),
                        TimeUnit.MINUTES
                );

            }


            if (quizOperation.getFinished() != null && quizOperation.getFinished() == true)
                return "quiz-operation-finish-page";

            Question questionItem = quizService.findQuizById(quizId).getQuestions().get(questionNumber-1);
            if (questionItem instanceof DetailedQuestion)
                model.addAttribute("questionItem", (DetailedQuestion) questionItem);
            else if (questionItem instanceof MultiChoiceQuestion)
                model.addAttribute("questionItem", (MultiChoiceQuestion) questionItem);
            //other types of questions...


            AnswerDTO answerDto;
            if (quizOperationService.findById(quizOperation.getId()).getAnswerList() != null
                    && quizOperationService.findById(quizOperation.getId()).getAnswerList().stream()
                    .map(answer -> answer.getQuestionNumberInQuiz()).filter(qNum -> qNum == questionNumber)
                    .count() > 0){
                Answer ans = quizOperationService.findById(quizOperation.getId()).getAnswerList().stream()
                        .filter(answer -> answer.getQuestionNumberInQuiz() == questionNumber).findFirst().get();
                answerDto = new AnswerDTO(
                        ans.getId(),
                        ans.getContent(),
                        ans.getQuestionId(),
                        ans.getQuestionNumberInQuiz());
            }
            else {
                answerDto = new AnswerDTO();
                answerDto.setQuestionId(questionItem.getId());
                answerDto.setQuestionNumberInQuiz(questionNumber);
            }
            model.addAttribute("answerDto", answerDto);

            model.addAttribute("finishTime", quizOperation.getFinishDate().getTime());
            model.addAttribute("allQuestionsCount", quizService.findQuizById(quizId).getQuestions().size());
            return "quiz-operation"; // TODO
        }
        else
            return "redirect:/home";

    }

    @PostMapping(value = "/{studentId}/course/{courseId}/quiz/{quizId}/enterQuizOperation/question/{questionNumber}")
    public String submitQuizOperation(Model model,
                                     @ModelAttribute AnswerDTO bindingAnswerDto,
                                     @PathVariable Long studentId,
                                     @PathVariable Long courseId,
                                     @PathVariable Long quizId,
                                     @PathVariable Integer questionNumber){
        if (signedInAccountTools.getAccount().equals(accountService.findProfileById(studentId).get())) {

            QuizOperation quizOperation = quizOperationService
                    .findByStudentIdAndCourseIdAndQuizId(studentId, courseId, quizId);

            if (quizOperation.getFinished() != null && quizOperation.getFinished() == true)
                return "quiz-operation-finish"; // TODO

            // TODO: 3/14/2020 handle null checkbox if needed
            if (bindingAnswerDto.getQuestionId() != null && bindingAnswerDto.getQuestionNumberInQuiz() != null) {
                Answer savingAnswer = new Answer(
                        bindingAnswerDto.getId(),
                        bindingAnswerDto.getContent(),
                        bindingAnswerDto.getQuestionId(),
                        bindingAnswerDto.getQuestionNumberInQuiz(),
                        quizOperation);

                quizOperation.getAnswerList().add(savingAnswer);
                quizOperationService.save(quizOperation);
            }


            if (questionNumber <= quizService.findQuizById(quizId).getQuestions().size()) {
                //index of next question
                Question questionItem = quizService.findQuizById(quizId).getQuestions().get(questionNumber - 1);
                if (questionItem instanceof DetailedQuestion)
                    model.addAttribute("questionItem", (DetailedQuestion) questionItem);
                else if (questionItem instanceof MultiChoiceQuestion)
                    model.addAttribute("questionItem", (MultiChoiceQuestion) questionItem);


                AnswerDTO answerDto;
                if (quizOperationService.findById(quizOperation.getId()).getAnswerList().stream()
                        .map(answer -> answer.getQuestionNumberInQuiz()).filter(qNum -> qNum == questionNumber)
                        .count() > 0){
                    Answer ans = quizOperationService.findById(quizOperation.getId()).getAnswerList().stream()
                            .filter(answer -> answer.getQuestionNumberInQuiz() == questionNumber).findFirst().get();
                    answerDto = new AnswerDTO(
                            ans.getId(),
                            ans.getContent(),
                            ans.getQuestionId(),
                            ans.getQuestionNumberInQuiz());
                }
                else {
                    answerDto = new AnswerDTO();
                    answerDto.setQuestionId(questionItem.getId());
                    answerDto.setQuestionNumberInQuiz(questionNumber);
                }
                model.addAttribute("answerDto", answerDto);

                model.addAttribute("allQuestionsCount", quizService.findQuizById(quizId).getQuestions().size());

                model.addAttribute("finishTime", quizOperation.getFinishDate().getTime());
                return "quiz-operation";
            }
            else {
                QuizOperation qo = quizOperationService.findByQuizIdAndStudentId(quizId, studentId);
                qo.setFinished(true);
                qo.setFinishDate(new Date());
                quizOperationService.save(qo);

                return "quiz-operation-finish";
            }
        }
        else
            return "redirect:/home";
    }
}
