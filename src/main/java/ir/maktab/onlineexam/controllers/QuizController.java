package ir.maktab.onlineexam.controllers;

import ir.maktab.onlineexam.domains.*;
import ir.maktab.onlineexam.dto.GradingDTO;
import ir.maktab.onlineexam.dto.ScoresOfQuizDTO;
import ir.maktab.onlineexam.dto.SearchQuestionDTO;
import ir.maktab.onlineexam.services.*;
import ir.maktab.onlineexam.utility.*;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/quiz")
@Secured({"ROLE_TEACHER_GENERAL_PRIVILEGE", "ROLE_STUDENT_GENERAL_PRIVILEGE"})
public class QuizController {

    private final QuizService quizService;
    private final QuestionService questionService;
    private final ChoiceService choiceService;
    private final CourseService courseService;
    private final SignedInAccountTools signedInAccountTools;
    private final QuizOperationService quizOperationService;
    private final ProfileService accountService;

    public QuizController(QuizService quizService
                        , QuestionService questionService
                        , ChoiceService choiceService
                        , CourseService courseService
                        , SignedInAccountTools signedInAccountTools
                        , QuizOperationService quizOperationService
                        , ProfileService accountService) {
        this.quizService = quizService;
        this.questionService = questionService;
        this.choiceService = choiceService;
        this.courseService = courseService;
        this.signedInAccountTools = signedInAccountTools;
        this.quizOperationService = quizOperationService;
        this.accountService = accountService;
    }

    @Secured("ROLE_TEACHER_GENERAL_PRIVILEGE")
    @GetMapping("/{quizId}/questions")
    public String getQuizQuestions(Model model, @PathVariable Long quizId){

        if (signedInAccountTools.getAccount().equals(quizService.findQuizById(quizId).getCourse().getTeacher())) {

            Double maxScore = 0.0;
            ScoresOfQuizDTO scores = new ScoresOfQuizDTO();
            for (int i = 0; i < quizService.findQuizById(quizId).getQuestions().size(); i++) {
                if (quizService.findQuizById(quizId).getDefaultScoresList() == null
                        || quizService.findQuizById(quizId).getDefaultScoresList().isEmpty())
                    scores.add(new Score());
                else {
                    String[] stringScores = quizService.findQuizById(quizId).getDefaultScoresList().split("-");
                    scores.add(new Score(Double.parseDouble(stringScores[i])));
                    maxScore += Double.parseDouble(stringScores[i]);
                }

            }

            model.addAttribute("courseId", quizService.findQuizById(quizId).getCourse().getId());
            model.addAttribute("questions", quizService.findQuizById(quizId).getQuestions());
            model.addAttribute("questionTypes", QuestionType.values());
            model.addAttribute("scoresDto", scores);
            model.addAttribute("maxScore", maxScore);
            model.addAttribute("currentTeacherAccount", signedInAccountTools.getAccount());
            return "quiz-questions";

        }
        else
            return "redirect:/home";
    }

    @Secured("ROLE_TEACHER_GENERAL_PRIVILEGE")
    @GetMapping("/{quizId}/addQuestion/fromBank")
    public String addQuestionFromBank(Model model, @PathVariable Long quizId){
        if (signedInAccountTools.getAccount().equals(quizService.findQuizById(quizId).getCourse().getTeacher())) {

            Long currentTeacherId = signedInAccountTools.getAccount().getId();
            Long currentCourseId = quizService.findQuizById(quizId).getCourse().getId();
            List<Question> bankQuestions = questionService.findBankQuestions(currentTeacherId, currentCourseId);
            List<Question> quizQuestions = quizService.findQuizById(quizId).getQuestions();
            model.addAttribute("bankQuestions", bankQuestions);
            model.addAttribute("quizQuestions", quizQuestions);
            model.addAttribute("searchQuestionDto", new SearchQuestionDTO());
            return "question-bank";
        }
        else
            return "redirect:/home";
    }

    @Secured("ROLE_TEACHER_GENERAL_PRIVILEGE")
    @PostMapping(value = "/{quizId}/addQuestion/fromBank")
    public String addFilteredQuestionFromBank(Model model,
                                              @ModelAttribute SearchQuestionDTO searchQuestionDto,
                                              @PathVariable Long quizId){

        if (signedInAccountTools.getAccount().equals(quizService.findQuizById(quizId).getCourse().getTeacher())) {

            Long currentTeacherId = signedInAccountTools.getAccount().getId();
            Long currentCourseId = quizService.findQuizById(quizId).getCourse().getId();
            List<Question> bankFilteredQuestions
                    = questionService.findBankQuestions(currentTeacherId, currentCourseId).stream()
                    .filter(question -> question.getTitle().contains(searchQuestionDto.getTitle()))
                    .collect(Collectors.toList());
            List<Question> quizQuestions = quizService.findQuizById(quizId).getQuestions();
            model.addAttribute("bankQuestions", bankFilteredQuestions);
            model.addAttribute("quizQuestions", quizQuestions);
            model.addAttribute("searchQuestionDto", new SearchQuestionDTO());
            return "question-bank";
        }
        else
            return "redirect:/home";
    }

   //  TODO: 12/28/2020 ids validation in url
    @Secured("ROLE_TEACHER_GENERAL_PRIVILEGE")
    @GetMapping("/{quizId}/addQuestion/fromBank/{questionId}")
    public String addQuestionItemFromBankToQuiz(@PathVariable Long quizId, @PathVariable Long questionId){
        if (signedInAccountTools.getAccount().equals(quizService.findQuizById(quizId).getCourse().getTeacher())) {

            Quiz requestedQuiz = quizService.findQuizById(quizId);
            Question requestedQuestion = questionService.findQuestionById(questionId);

            if (requestedQuiz.getQuestions().contains(requestedQuestion)) {

                int requestedQuestionIndex = requestedQuiz.getQuestions().indexOf(requestedQuestion);
                String defaultScores = requestedQuiz.getDefaultScoresList();
                ArrayList<Double> doubleScores = ScoresListTools.stringToArrayList(defaultScores);
                doubleScores.remove(requestedQuestionIndex);
                String newDefaultScores = ScoresListTools.arrayListToString(doubleScores);

                requestedQuiz.setDefaultScoresList(newDefaultScores);
                requestedQuiz.getQuestions().remove(requestedQuestion);
            } else {
                String defaultScores = requestedQuiz.getDefaultScoresList();
                if (defaultScores == null)
                    defaultScores = "";
                defaultScores += "0-";
                requestedQuiz.setDefaultScoresList(defaultScores);
                requestedQuiz.getQuestions().add(requestedQuestion);
            }

            quizService.save(requestedQuiz);
            return "redirect:/quiz/" + quizId + "/addQuestion/fromBank";
        }
        else
            return "redirect:/home";
    }


    @Secured("ROLE_TEACHER_GENERAL_PRIVILEGE")
    @GetMapping("/{quizId}/addQuestion/DetailedQuestion")
    public String addDetailedQuestion(Model model, @PathVariable Long quizId){
        if (signedInAccountTools.getAccount().equals(quizService.findQuizById(quizId).getCourse().getTeacher())) {

            model.addAttribute("question", new DetailedQuestion());
            return "add-detailed-question";
        }
        else
            return "redirect:/home";
    }

    @Secured("ROLE_TEACHER_GENERAL_PRIVILEGE")
    @PostMapping(value = "/{quizId}/addQuestion/DetailedQuestion")
    public String submitAddDetailedQuestion(@ModelAttribute DetailedQuestion detailedQuestion,
                                            @PathVariable Long quizId){
        if (signedInAccountTools.getAccount().equals(quizService.findQuizById(quizId).getCourse().getTeacher())) {

            Long questionIdBeforeSave = detailedQuestion.getId();
            Question savingQuestion;
            if (!detailedQuestion.getTitle().isEmpty() && !detailedQuestion.getDefinition().isEmpty()) {
//            if (questionIdBeforeSave == null) {
                detailedQuestion.setRelatedCourseId(quizService.findQuizById(quizId).getCourse().getId());
                detailedQuestion.setCreatorTeacherId(signedInAccountTools.getAccount().getId());
//            }
                savingQuestion = questionService.save(detailedQuestion);
                if (questionIdBeforeSave == null) {
                    Quiz updatingQuiz = quizService.findQuizById(quizId);

                    String defaultScores = updatingQuiz.getDefaultScoresList();
                    if (defaultScores == null)
                        defaultScores = "";
                    defaultScores += "0-";

                    updatingQuiz.setDefaultScoresList(defaultScores);
                    updatingQuiz.getQuestions().add(savingQuestion);
                    quizService.save(updatingQuiz);
                }
            }
            return "redirect:/quiz/" + quizId + "/questions";
        }
        else
            return "redirect:/home";
    }

    @Secured("ROLE_TEACHER_GENERAL_PRIVILEGE")
    @GetMapping("/{quizId}/addQuestion/MultiChoiceQuestion")
    public String addMultiChoiceQuestion(Model model, @PathVariable Long quizId){
        if (signedInAccountTools.getAccount().equals(quizService.findQuizById(quizId).getCourse().getTeacher())) {

            model.addAttribute("question", new MultiChoiceQuestion());
            return "add-multi-choice-question";
        }
        else
            return "redirect:/home";
    }

    @Secured("ROLE_TEACHER_GENERAL_PRIVILEGE")
    @PostMapping(value = "/{quizId}/addQuestion/MultiChoiceQuestion")
    public String submitAddMultiChoiceQuestion(Model model,
                                               @ModelAttribute MultiChoiceQuestion multiChoiceQuestion,
                                               @PathVariable Long quizId){

        if (signedInAccountTools.getAccount().equals(quizService.findQuizById(quizId).getCourse().getTeacher())) {

            Long questionIdBeforeSave = multiChoiceQuestion.getId();
            Question savingQuestion;
            if (!multiChoiceQuestion.getTitle().isEmpty() && !multiChoiceQuestion.getDefinition().isEmpty()) {

//            if (questionIdBeforeSave == null) {
                multiChoiceQuestion.setRelatedCourseId(quizService.findQuizById(quizId).getCourse().getId());
                multiChoiceQuestion.setCreatorTeacherId(signedInAccountTools.getAccount().getId());
//            }
                savingQuestion = questionService.save(multiChoiceQuestion);

                Quiz updatingQuiz = quizService.findQuizById(quizId);
                if (questionIdBeforeSave == null) {
                    updatingQuiz.getQuestions().add(savingQuestion);

                    String defaultScores = updatingQuiz.getDefaultScoresList();
                    if (defaultScores == null)
                        defaultScores = "";
                    defaultScores += "0-";
                    updatingQuiz.setDefaultScoresList(defaultScores);

                    quizService.save(updatingQuiz);
                }
                model.addAttribute("question", (MultiChoiceQuestion) savingQuestion);
                return "add-multi-choice-question";
            }

            return "redirect:/quiz/" + quizId + "/addQuestion/MultiChoiceQuestion";
        }
        else
            return "redirect:/home";
    }

    @Secured("ROLE_TEACHER_GENERAL_PRIVILEGE")
    @GetMapping("/{quizId}/question/{questionId}/addChoiceItem")
    public String addChoiceItemToQuestion(Model model, @PathVariable Long quizId, @PathVariable Long questionId){
        if (signedInAccountTools.getAccount().equals(quizService.findQuizById(quizId).getCourse().getTeacher())) {

            model.addAttribute("question", (MultiChoiceQuestion) questionService.findQuestionById(questionId));
            model.addAttribute("choice", new Choice());
            return "add-choice-items";
        }
        else
            return "redirect:/menu";
    }

    @Secured("ROLE_TEACHER_GENERAL_PRIVILEGE")
    @RequestMapping(value = "/{quizId}/question/{questionId}/addChoiceItem", method = RequestMethod.POST)
    public String submitAddChoiceItemToQuestion(Model model,
                                                @ModelAttribute Choice choice,
                                                @PathVariable Long quizId,
                                                @PathVariable Long questionId){

        if (signedInAccountTools.getAccount().equals(quizService.findQuizById(quizId).getCourse().getTeacher())) {

            if (!choice.getTitle().isEmpty()) {
                if (choice.getTrueChoice() != null && choice.getTrueChoice()
                        && QuestionTools.MultiChoiceQuestionContainsATrueChoice((MultiChoiceQuestion) questionService
                        .findQuestionById(questionId))
                        && QuestionTools.getTrueChoiceOfMultiChoiceQuestion((MultiChoiceQuestion) questionService
                        .findQuestionById(questionId)).getId() != choice.getId()) {
                    //dont save
                } else {

                    Long choiceIdBeforeSave = choice.getId();
                    Choice savedChoice = choiceService.save(choice);


                    if (choiceIdBeforeSave == null) {
                        MultiChoiceQuestion updatingQuestion = (MultiChoiceQuestion) questionService
                                .findQuestionById(questionId);
                        updatingQuestion.getChoiceList().add(savedChoice);
                        questionService.save(updatingQuestion);
                    }
                }

            }
            model.addAttribute("question", (MultiChoiceQuestion) questionService.findQuestionById(questionId));
            model.addAttribute("choice", new Choice());
            return "add-choice-items";
        }
        else
            return "redirect:/home";
    }

    @Secured("ROLE_TEACHER_GENERAL_PRIVILEGE")
    @GetMapping("/{quizId}/question/{questionId}/setTrueChoice/{trueChoiceId}")
    public String addTrueChoice(Model model,
                                @PathVariable Long quizId,
                                @PathVariable Long questionId,
                                @PathVariable Long trueChoiceId){

        if (signedInAccountTools.getAccount().equals(quizService.findQuizById(quizId).getCourse().getTeacher())) {

            if (!QuestionTools.MultiChoiceQuestionContainsATrueChoice((MultiChoiceQuestion) questionService
                    .findQuestionById(questionId))) {
                Choice requestedChoice = choiceService.findChoiceById(trueChoiceId);
                requestedChoice.setTrueChoice(true);
                choiceService.save(requestedChoice);
            } else {
                if (choiceService.findChoiceById(trueChoiceId).getTrueChoice()) {
                    Choice requestedChoice = choiceService.findChoiceById(trueChoiceId);
                    requestedChoice.setTrueChoice(false);
                    choiceService.save(requestedChoice);
                }
            }
            model.addAttribute("question", (MultiChoiceQuestion) questionService.findQuestionById(questionId));
            model.addAttribute("choice", new Choice());
            return "add-choice-items";
        }
        else
            return "redirect:/home";
    }

    @Secured("ROLE_TEACHER_GENERAL_PRIVILEGE")
    @GetMapping("/{quizId}/question/{questionId}/editChoiceItem/{choiceId}")
    public String editChoiceItem(Model model,
                                 @PathVariable Long quizId,
                                 @PathVariable Long questionId,
                                 @PathVariable Long choiceId){

        if (signedInAccountTools.getAccount().equals(quizService.findQuizById(quizId).getCourse().getTeacher())) {

            model.addAttribute("question", (MultiChoiceQuestion) questionService.findQuestionById(questionId));
            model.addAttribute("choice", choiceService.findChoiceById(choiceId));
            return "add-choice-items";
        }
        else
            return "redirect:/home";
    }

    @Secured("ROLE_TEACHER_GENERAL_PRIVILEGE")
    @GetMapping("/{quizId}/question/{questionId}/deleteChoiceItem/{choiceId}")
    public String deleteChoiceItem(Model model,
                                   @PathVariable Long quizId,
                                   @PathVariable Long questionId,
                                   @PathVariable Long choiceId){

        if (signedInAccountTools.getAccount().equals(quizService.findQuizById(quizId).getCourse().getTeacher())) {

            MultiChoiceQuestion requestedQuestion = (MultiChoiceQuestion) questionService.findQuestionById(questionId);
            requestedQuestion.getChoiceList().remove(choiceService.findChoiceById(choiceId));
            questionService.save(requestedQuestion);
            choiceService.deleteChoiceById(choiceId);
            model.addAttribute("question", (MultiChoiceQuestion) questionService.findQuestionById(questionId));
            model.addAttribute("choice", new Choice());
            return "add-choice-items";
        }
        else
            return "redirect:/home";
    }

    @Secured("ROLE_TEACHER_GENERAL_PRIVILEGE")
    @GetMapping("/{quizId}/question/{questionId}/view")
    public String viewQuestion(Model model, @PathVariable Long quizId, @PathVariable Long questionId){
        if (signedInAccountTools.getAccount().equals(quizService.findQuizById(quizId).getCourse().getTeacher())) {

            if (questionService.findQuestionById(questionId) instanceof DetailedQuestion)
                model.addAttribute("question", (DetailedQuestion) questionService.findQuestionById(questionId));
            else if (questionService.findQuestionById(questionId) instanceof MultiChoiceQuestion)
                model.addAttribute("question", (MultiChoiceQuestion) questionService.findQuestionById(questionId));
            // TODO: 12/27/2020 add other type of questions here

            return "view-question";
        }
        else
            return "redirect:/home";
    }

    @Secured("ROLE_TEACHER_GENERAL_PRIVILEGE")
    @GetMapping("/{quizId}/question/{questionId}/edit")
    public String editQuestion(Model model, @PathVariable Long quizId, @PathVariable Long questionId){
        if (signedInAccountTools.getAccount().equals(quizService.findQuizById(quizId).getCourse().getTeacher())) {

            if (questionService.findQuestionById(questionId) instanceof DetailedQuestion) {
                model.addAttribute("question", (DetailedQuestion) questionService.findQuestionById(questionId));
                return "add-detailed-question";
            } else if (questionService.findQuestionById(questionId) instanceof MultiChoiceQuestion) {
                model.addAttribute("question", (MultiChoiceQuestion) questionService.findQuestionById(questionId));
                return "add-multi-choice-question";
            }
            // TODO: 12/27/2020 add other type of questions here

            return "not-reachable actually";
        }
        else
            return "redirect:/home";
    }

    @Secured("ROLE_TEACHER_GENERAL_PRIVILEGE")
    @GetMapping("/{quizId}/question/{questionId}/delete")
    public String deleteQuestion(@PathVariable Long quizId, @PathVariable Long questionId){
        if (signedInAccountTools.getAccount().equals(quizService.findQuizById(quizId).getCourse().getTeacher())) {

            Quiz requestedQuiz = quizService.findQuizById(quizId);

            int removingQuestionIndex = requestedQuiz.getQuestions().indexOf(questionService.findQuestionById(questionId));
            ArrayList<Double> requestedQuizDefaultScores
                    = ScoresListTools.stringToArrayList(requestedQuiz.getDefaultScoresList());
            requestedQuizDefaultScores.remove(removingQuestionIndex);
            String newRequestedQuizDefaultScores = ScoresListTools.arrayListToString(requestedQuizDefaultScores);
            requestedQuiz.setDefaultScoresList(newRequestedQuizDefaultScores);

            requestedQuiz.getQuestions().remove(questionService.findQuestionById(questionId));
            quizService.save(requestedQuiz);

            return "redirect:/quiz/" + quizId + "/questions";
        }
        else
            return "redirect:/home";
    }

    @Secured("ROLE_TEACHER_GENERAL_PRIVILEGE")
    @GetMapping("/{quizId}/question/{questionId}/deleteFromBank")
    public String deleteQuestionFromBank(@PathVariable Long quizId, @PathVariable Long questionId){
        if (signedInAccountTools.getAccount().equals(quizService.findQuizById(quizId).getCourse().getTeacher())) {

            Question requestedQuestion = questionService.findQuestionById(questionId);
            Course relatedCourse = courseService.findCourseById(requestedQuestion.getRelatedCourseId());
            for (Quiz quizItem : relatedCourse.getQuizzes()) {
                if (quizItem.getQuestions().contains(requestedQuestion)) {
                    quizItem.getQuestions().remove(requestedQuestion);
                    quizService.save(quizItem);
                }
            }
            questionService.deleteQuestionById(requestedQuestion.getId());
            return "redirect:/quiz/" + quizId + "/addQuestion/fromBank";
        }
        else
            return "redirect:/home";
    }

    @Secured("ROLE_TEACHER_GENERAL_PRIVILEGE")
    @GetMapping("/{quizId}/saveQuizDefaultScores")
    public String saveQuizDefaultScores(@ModelAttribute ScoresOfQuizDTO scoresOfQuizDto, @PathVariable Long quizId){
        if (signedInAccountTools.getAccount().equals(quizService.findQuizById(quizId).getCourse().getTeacher())) {

            // TODO: 12/28/2020 print error message for null score --- check being number
            if (!scoresOfQuizDto.getScores().stream().map(score -> score.getValue())
                    .collect(Collectors.toList()).contains(null)) {
                String defaultScoresOfQuiz = "";
                for (Double i : scoresOfQuizDto.getScores().stream()
                        .map(score -> score.getValue()).collect(Collectors.toList()))
                    defaultScoresOfQuiz += i + "-";

                Quiz requestedQuiz = quizService.findQuizById(quizId);
                requestedQuiz.setDefaultScoresList(defaultScoresOfQuiz);
                quizService.save(requestedQuiz);
            }

            return "redirect:/quiz/" + quizId + "/questions";
        }
        else
            return "redirect:/home";
    }

    @Secured("ROLE_TEACHER_GENERAL_PRIVILEGE")
    @GetMapping("/{quizId}/participants")
    public String getQuizParticipants(Model model, @PathVariable Long quizId){
        if (signedInAccountTools.getAccount().equals(quizService.findQuizById(quizId).getCourse().getTeacher())) {

            List<QuizOperation> quizOperations = quizOperationService.findAllByQuizId(quizId);
            model.addAttribute("quizOperations", quizOperations);
            model.addAttribute("courseId",quizService.findQuizById(quizId).getCourse().getId());
            model.addAttribute("maxScore", ScoresListTools.sum(quizService.findQuizById(quizId).getDefaultScoresList()));

            return "quiz-participants";
        }
        else
            return "redirect:/home";
    }

    @Secured({"ROLE_TEACHER_GENERAL_PRIVILEGE", "ROLE_STUDENT_GENERAL_PRIVILEGE"})
    @GetMapping("/{quizId}/participant/{studentId}/answers/{questionNumberInQuiz}")
    public String getParticipantAnswers(Model model,
                                        @PathVariable Long quizId,
                                        @PathVariable Long studentId,
                                        @PathVariable Integer questionNumberInQuiz){
        if (
                signedInAccountTools.getAccount().equals(quizService.findQuizById(quizId).getCourse().getTeacher())
                        || signedInAccountTools.getAccount().getId() == studentId
        ) {

            Question questionItem = quizService.findQuizById(quizId).getQuestions().get(questionNumberInQuiz - 1);
            if (questionItem instanceof DetailedQuestion)
                model.addAttribute("questionItem", (DetailedQuestion) questionItem);
            else if (questionItem instanceof MultiChoiceQuestion)
                model.addAttribute("questionItem", (MultiChoiceQuestion) questionItem);
            // other types of question...

            Answer answerItem = null;
            QuizOperation quizOperation = quizOperationService.findByQuizIdAndStudentId(quizId, studentId);


            if (quizOperation.getAnswerList().stream()
                    .filter(answer -> answer.getQuestionNumberInQuiz() == questionNumberInQuiz)
                    .count() > 0) {
                answerItem = quizOperation.getAnswerList().stream()
                        .filter(answer -> answer.getQuestionNumberInQuiz() == questionNumberInQuiz)
                        .findFirst().get();
            }
            model.addAttribute("answerItem", answerItem);

            Double defaultScoreOfQuestion = ScoresListTools.stringToArrayList(quizService.findQuizById(quizId)
                    .getDefaultScoresList()).get(questionNumberInQuiz - 1);
            Double participantScoreForQuestion = ScoresListTools
                    .stringToArrayList(quizOperationService.findByQuizIdAndStudentId(quizId, studentId)
                            .getResultScores()).get(questionNumberInQuiz - 1);
            model.addAttribute("gradingDto", new GradingDTO(defaultScoreOfQuestion, participantScoreForQuestion));

            model.addAttribute("quizOperation", quizOperation);

            model.addAttribute("questionsCount", quizService.findQuizById(quizId).getQuestions().size());

            model.addAttribute("courseId", quizService.findQuizById(quizId).getCourse().getId());

            return "quiz-answer";
        }
        else
            return "redirect:/home";

    }

    @Secured("ROLE_TEACHER_GENERAL_PRIVILEGE")
    @PostMapping(value = "/{quizId}/participant/{studentId}/question/{questionNumberInQuiz}/submitScore")
    public String submitParticipantScore(@ModelAttribute GradingDTO gradingDto,
                                         @PathVariable Long quizId,
                                         @PathVariable Long studentId,
                                         @PathVariable Integer questionNumberInQuiz){

        if (signedInAccountTools.getAccount().equals(quizService.findQuizById(quizId).getCourse().getTeacher())) {

            Integer destinationQuestionNumberInQuiz;
            if (gradingDto.getParticipantScoreForQuestion() <= gradingDto.getDefaultScoreForQuestion()) {
                QuizOperation relatedQuizOperation = quizOperationService.findByQuizIdAndStudentId(quizId, studentId);
                List<Double> participantScoresList = ScoresListTools
                        .stringToArrayList(relatedQuizOperation.getResultScores());

                participantScoresList.set(questionNumberInQuiz - 1, gradingDto.getParticipantScoreForQuestion());

                String participantScoresString = ScoresListTools
                        .arrayListToString((ArrayList<Double>) participantScoresList);

                relatedQuizOperation.setResultScores(participantScoresString);

                quizOperationService.save(relatedQuizOperation);

                destinationQuestionNumberInQuiz = questionNumberInQuiz + 1;

                if (questionNumberInQuiz == quizService.findQuizById(quizId).getQuestions().size()) {
                    QuizOperation quizOperation = quizOperationService.findByQuizIdAndStudentId(quizId, studentId);
                    quizOperation.setCustomGraded(true);
                    quizOperationService.save(quizOperation);
                    return "redirect:/quiz/" + quizId + "/participants";
                }
                return "redirect:/quiz/" + quizId + "/participant/" + studentId + "/answers/" + destinationQuestionNumberInQuiz;

            } else {
                destinationQuestionNumberInQuiz = questionNumberInQuiz;
                return "redirect:/quiz/" + quizId + "/participant/" + studentId + "/answers/"
                        + destinationQuestionNumberInQuiz + "?invalidScoreError";
            }
        }
        else
            return "redirect:/home";

    }

    @Secured("ROLE_TEACHER_GENERAL_PRIVILEGE")
    @GetMapping("/{quizId}/participant/{studentId}/answers/autoGrading")
    public String autoGrading(Model model, @PathVariable Long quizId, @PathVariable Long studentId){
        if (signedInAccountTools.getAccount().equals(quizService.findQuizById(quizId).getCourse().getTeacher())) {

            QuizOperation quizOperation = quizOperationService.findByQuizIdAndStudentId(quizId, studentId);
            quizOperation.setResultScores(QuizOperationTools.autoPrepareStudentScores(quizOperation));
            quizOperation.setAutoGraded(true);


            quizOperationService.save(quizOperation);
            return "redirect:/quiz/" + quizId + "/participants";
        }
        else
            return "redirect:/home";
    }

}
