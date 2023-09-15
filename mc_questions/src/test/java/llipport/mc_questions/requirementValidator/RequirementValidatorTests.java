package llipport.mc_questions.requirementValidator;

import llipport.mc_questions.database.entities.*;
import llipport.mc_questions.database.util.ROLE;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RequirementValidatorTests {
    Question noIncorrectAnswers;
    Question fourAnswers;
    Question fiveAnswers;
    Question fiveAnswers2;
    Question sixAnswers;
    Question fiveNot;
    Question sixNot;
    Exam exam2020;
    Exam exam2019;
    Exam exam2018;
    Exam noQuestions;
    Exam invalidQuestions;
    Exam exam2017;
    Settings defaultSettings;
    Settings oneQuestionValidNoUsedQuestions;

    @BeforeEach
    void setUp() {
        noIncorrectAnswers = new Question(
                "",
                "",
                false,
                new Answer("correct"),
                new HashSet<>(),
                new AppUser("", "", ROLE.ADMIN),
                new HashSet<>()
        );
        fourAnswers = new Question(
                "",
                "",
                false,
                new Answer("correct"),
                new HashSet<>(Arrays.asList(
                        new Answer(""), new Answer(""), new Answer("")
                )),
                new AppUser("", "", ROLE.ADMIN),
                new HashSet<>()
        );
        fiveAnswers = new Question(
                "",
                "",
                false,
                new Answer("correct"),
                new HashSet<>(Arrays.asList(
                        new Answer(""), new Answer(""), new Answer(""), new Answer("")
                )),
                new AppUser("", "", ROLE.ADMIN),
                new HashSet<>()
        );
        fiveAnswers2 = new Question(
                "",
                "",
                false,
                new Answer("correct"),
                new HashSet<>(Arrays.asList(
                        new Answer(""), new Answer(""), new Answer(""), new Answer("")
                )),
                new AppUser("", "", ROLE.ADMIN),
                new HashSet<>()
        );
        sixAnswers = new Question(
                "",
                "",
                false,
                new Answer("correct"),
                new HashSet<>(Arrays.asList(
                        new Answer(""), new Answer(""), new Answer(""), new Answer(""), new Answer("")
                )),
                new AppUser("", "", ROLE.ADMIN),
                new HashSet<>()
        );
        fiveNot = new Question(
                "",
                "",
                true,
                new Answer("correct"),
                new HashSet<>(Arrays.asList(
                        new Answer(""), new Answer(""), new Answer(""), new Answer("")
                )),
                new AppUser("", "", ROLE.ADMIN),
                new HashSet<>()
        );
        sixNot = new Question(
                "",
                "",
                true,
                new Answer("correct"),
                new HashSet<>(Arrays.asList(
                        new Answer(""), new Answer(""), new Answer(""), new Answer(""), new Answer("")
                )),
                new AppUser("", "", ROLE.ADMIN),
                new HashSet<>()
        );
        exam2020 = new Exam(new Date(2020, 1, 1), new HashSet<>(Arrays.asList(fiveAnswers)));
        exam2019 = new Exam(new Date(2019, 1, 1), new HashSet<>());
        exam2018 = new Exam(new Date(2018, 1, 1), new HashSet<>());
        exam2017 = new Exam(new Date(2017, 1, 1), new HashSet<>());
        noQuestions = new Exam(new Date(2000, 1, 1), new HashSet<>());
        invalidQuestions = new Exam(new Date(2000, 1, 1), new HashSet<>(Arrays.asList(fourAnswers)));
        defaultSettings = new Settings("", 12, 12, 1, 1,
                2, 5, 5, 1);
        oneQuestionValidNoUsedQuestions = new Settings("", 1, 1, 1,
                0, 2, 5, 5, 1);
    }

    @Test
    @DisplayName("Valid questions")
    public void validQuestions() {
        QuestionCheckResult result5 = RequirementValidator.check(fiveAnswers, defaultSettings);
        assertTrue(result5.valid());
        QuestionCheckResult result5n = RequirementValidator.check(fiveNot, defaultSettings);
        assertTrue(result5n.valid());
    }

    @Test
    @DisplayName("Invalid questions")
    public void invalidQuestions() {
        QuestionCheckResult result0 = RequirementValidator.check(noIncorrectAnswers, defaultSettings);
        assertFalse(result0.valid());
        QuestionCheckResult result4 = RequirementValidator.check(fourAnswers, defaultSettings);
        assertFalse(result4.valid());
        QuestionCheckResult result6 = RequirementValidator.check(sixAnswers, defaultSettings);
        assertFalse(result6.valid());
        QuestionCheckResult result6n = RequirementValidator.check(sixNot, defaultSettings);
        assertFalse(result6n.valid());
    }

    @Test
    @DisplayName("Valid exams")
    public void validExams() {
        fiveAnswers.setInExams(new HashSet<>(Arrays.asList(exam2020, exam2017)));
        ExamCheckResult result3Years = RequirementValidator.check(exam2020, oneQuestionValidNoUsedQuestions);
        assertTrue(result3Years.valid());
        fiveAnswers.setInExams(new HashSet<>(Arrays.asList(exam2020, exam2018)));
        ExamCheckResult result2Years = RequirementValidator.check(exam2020, oneQuestionValidNoUsedQuestions);
        assertTrue(result2Years.valid());
    }

    @Test
    @DisplayName("Exam with too early used question")
    public void tooEarlyUsedQuestion() {
        fiveAnswers.setInExams(new HashSet<>(Arrays.asList(exam2020, exam2019)));
        ExamCheckResult result1Year = RequirementValidator.check(exam2020, oneQuestionValidNoUsedQuestions);
        assertFalse(result1Year.valid());
    }

    @Test
    @DisplayName("Exam with not enough questions")
    public void notEnoughQuestions() {
        ExamCheckResult result0 = RequirementValidator.check(noQuestions, oneQuestionValidNoUsedQuestions);
        assertFalse(result0.valid());
        ExamCheckResult result1 = RequirementValidator.check(exam2020, defaultSettings);
        assertFalse(result1.valid());
    }

    @Test
    @DisplayName("Exam with invalid questions")
    public void examInvalidQuestions() {
        ExamCheckResult result = RequirementValidator.check(invalidQuestions, oneQuestionValidNoUsedQuestions);
        assertFalse(result.valid());
        assertFalse(result.validQuestions());
    }
}
