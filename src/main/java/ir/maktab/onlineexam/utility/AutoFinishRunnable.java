package ir.maktab.onlineexam.utility;

import ir.maktab.onlineexam.domains.QuizOperation;

public class AutoFinishRunnable implements Runnable {

    private Long quizId;
    private Long studentId;

    public AutoFinishRunnable(Long quizId, Long studentId) {
        this.quizId = quizId;
        this.studentId = studentId;
    }

    @Override
    public void run() {
        QuizOperation requestedQuizOperation
                = ServiceTools.getQuizOperationService().findByQuizIdAndStudentId(quizId,studentId);

        if (requestedQuizOperation.getFinished() == null || requestedQuizOperation.getFinished() == false) {
            requestedQuizOperation.setFinished(true);
            ServiceTools.getQuizOperationService().save(requestedQuizOperation);
        }
    }
}