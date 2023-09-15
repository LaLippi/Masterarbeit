package llipport.mc_questions.requirementValidator;

import java.util.List;
import java.util.UUID;

public record ExamCheckResult(int questions, int notQuestions, int usedQuestions, boolean valid,
                              boolean validQuestions, List<UUID> invalidQuestionIds) {
}
