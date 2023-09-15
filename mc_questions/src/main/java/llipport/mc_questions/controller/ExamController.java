package llipport.mc_questions.controller;

import llipport.mc_questions.database.entities.Answer;
import llipport.mc_questions.database.entities.Exam;
import llipport.mc_questions.database.entities.Question;
import llipport.mc_questions.database.entities.Settings;
import llipport.mc_questions.database.repositories.ExamRepository;
import llipport.mc_questions.database.repositories.QuestionRepository;
import llipport.mc_questions.database.repositories.SettingsRepository;
import llipport.mc_questions.protoClasses.ProtoExam;
import llipport.mc_questions.requirementValidator.RequirementValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashSet;
import java.util.UUID;

@Controller
public class ExamController extends GlobalController {
    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private SettingsRepository settingsRepository;

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("allExams", examRepository.findAll());
        model.addAttribute("newExam", new ProtoExam());
    }

    @GetMapping("/exam")
    public String loadExams(Model model) {
        model.addAttribute("allQuestions", questionRepository.findAll());
        return "exam/exam";
    }

    @PostMapping("/createExam")
    public ModelAndView createExam(@ModelAttribute ProtoExam newExam, Model model) {
        for (UUID id : newExam.getQuestionIds()) {
            if (!questionRepository.existsByUuid(id)) {
                return new ModelAndView("exam/error", "examModel", model);
            }
        }

        HashSet<Question> questions = new HashSet<>();
        for (UUID id : newExam.getQuestionIds()) {
            questions.add(questionRepository.findByUuid(id));
        }

        Exam exam = new Exam(newExam.getDate(), questions);
        examRepository.save(exam);

        model.addAttribute("exam", exam);
        model.addAttribute("stringRepresentation", generateStringRepresentation(exam));
        Settings settings = settingsRepository.findByName("default");
        model.addAttribute("settings", settings);
        model.addAttribute("checkResult", RequirementValidator.check(exam, settings));
        model.addAttribute("examStatus", "created");
        return new ModelAndView("exam/examDetail", "examModel", exam);
    }

    @PostMapping("/deleteExam/{id}")
    public String deleteExam(@PathVariable UUID id, Model model) {
        examRepository.deleteByUuid(id);
        model.addAttribute("allExams", examRepository.findAll());
        return "exam/exam";
    }

    @PostMapping("/toUpdateExam/{id}")
    public String toUpdateExam(@PathVariable UUID id, Model model) {
        model.addAttribute("updateExam", examRepository.findByUuid(id));
        model.addAttribute("allQuestions", questionRepository.findAll());
        return "exam/updateExam";
    }

    @PostMapping("/updateExam/{id}")
    public ModelAndView updateExam(@ModelAttribute ProtoExam newExam, @PathVariable UUID id, Model model) {
        Exam updateExam = examRepository.findByUuid(id);

        HashSet<Question> questions = new HashSet<>();
        for (UUID questionId : newExam.getQuestionIds()) {
            questions.add(questionRepository.findByUuid(questionId));
        }

        updateExam.setDate(newExam.getDate());
        updateExam.setQuestions(questions);
        examRepository.save(updateExam);

        model.addAttribute("exam", updateExam);
        model.addAttribute("stringRepresentation", generateStringRepresentation(updateExam));
        Settings settings = settingsRepository.findByName("default"); // possible new feature: change if different settings can be chosen
        model.addAttribute("settings", settings);
        model.addAttribute("checkResult", RequirementValidator.check(updateExam, settings));
        model.addAttribute("examStatus", "updated");
        return new ModelAndView("exam/examDetail", "examModel", model);
    }

    @PostMapping("/examDetail/{id}")
    public String examDetail(@PathVariable UUID id, Model model) {
        Exam exam = examRepository.findByUuid(id);
        model.addAttribute("exam", exam);
        model.addAttribute("stringRepresentation", generateStringRepresentation(exam));
        Settings settings = settingsRepository.findByName("default"); // possible new feature: change if different settings can be chosen
        model.addAttribute("settings", settings);
        model.addAttribute("checkResult", RequirementValidator.check(exam, settings));
        return "exam/examDetail";
    }

    private String generateStringRepresentation(Exam exam) {
        StringBuilder stringRepresentation = new StringBuilder(
                String.format("ID: %s, Datum: %s, Fragen: {", exam.getUuid(), exam.getDate().toString())
        );
        for (Question question : exam.getQuestions()) {
            stringRepresentation.append(String.format(
                    "[ID: %s, Frage: '%s', Info: '%s', \"Nicht\"-Frage: %s, Autor: %s, korrekte Antwort: '%s', inkorrekte Antworten: (",
                    question.getUuid(),
                    question.getQuestionText(),
                    question.getQuestionInfo(),
                    (question.isNotQuestion()) ? "ja" : "nein",
                    question.getAuthor() != null ? question.getAuthor().getName() : "<gelöscht>",
                    question.getCorrectAnswer().getText()
            ));
            for (Answer answer : question.getIncorrectAnswers()) {
                stringRepresentation.append(String.format("'%s', ", answer.getText()));
            }
            stringRepresentation.delete(stringRepresentation.length() - 2, stringRepresentation.length());
            stringRepresentation.append(")], ");
        }
        stringRepresentation.delete(stringRepresentation.length() - 2, stringRepresentation.length());
        stringRepresentation.append("}");
        return stringRepresentation.toString();
    }
}
