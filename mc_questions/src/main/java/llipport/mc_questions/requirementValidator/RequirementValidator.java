package llipport.mc_questions.requirementValidator;

import llipport.mc_questions.database.entities.Exam;
import llipport.mc_questions.database.entities.Question;
import llipport.mc_questions.database.entities.Settings;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.UUID;

public final class RequirementValidator {

    public static QuestionCheckResult check(Question question, Settings settings) {
        int answers = question.getIncorrectAnswers().size() + ((question.getCorrectAnswer() != null) ? 1 : 0);
        int correctAnswers = ((question.getCorrectAnswer() != null) ? 1 : 0);
        boolean valid = answers <= settings.getMaxAnswers()
                && answers >= settings.getMinAnswers()
                && correctAnswers <= settings.getMaxCorrectAnswers();

        return new QuestionCheckResult(answers, correctAnswers, valid);
    }

    public static ExamCheckResult check(Exam exam, Settings settings) {
        int questions = exam.getQuestions().size();
        int notQuestions = 0;
        int usedQuestions = 0;
        boolean valid = true;
        boolean validQuestions = true;
        ArrayList<UUID> invalidQuestionIds = new ArrayList<>();

        for (Question question : exam.getQuestions()) {
            if (!check(question, settings).valid()) {
                validQuestions = false;
                invalidQuestionIds.add(question.getUuid());
            }
            if (question.isNotQuestion()) {
                notQuestions++;
            }
            for (Exam examOfQuestion : question.getInExams()) {
                boolean equal = examOfQuestion.equals(exam);
                long yearsBetween = Math.abs(ChronoUnit.YEARS.between(
                        examOfQuestion.getDate().toLocalDate(),
                        exam.getDate().toLocalDate())
                );
                if (!equal && yearsBetween < settings.getYearsUntilQuestionsAreFree()) {
                    usedQuestions++;
                    break;
                }
            }
        }

        if (!(
                questions <= settings.getMaxQuestions()
                        && questions >= settings.getMinQuestions()
                        && notQuestions <= settings.getMaxNotQuestions()
                        && usedQuestions <= settings.getMaxUsedQuestions()
                        && validQuestions
        )) {
            valid = false;
        }
        return new ExamCheckResult(questions, notQuestions, usedQuestions, valid, validQuestions, invalidQuestionIds);
    }
}
