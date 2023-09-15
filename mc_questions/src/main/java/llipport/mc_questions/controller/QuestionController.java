package llipport.mc_questions.controller;

import llipport.mc_questions.database.entities.*;
import llipport.mc_questions.database.repositories.AnswerRepository;
import llipport.mc_questions.database.repositories.AppUserRepository;
import llipport.mc_questions.database.repositories.QuestionRepository;
import llipport.mc_questions.database.repositories.SettingsRepository;
import llipport.mc_questions.protoClasses.ProtoQuestion;
import llipport.mc_questions.requirementValidator.RequirementValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class QuestionController extends GlobalController {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private SettingsRepository settingsRepository;

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("allQuestions", questionRepository.findAll());
        model.addAttribute("newQuestion", new ProtoQuestion());
    }

    @GetMapping("/question")
    public String loadQuestions(Model model) {
        model.addAttribute("allUsers", userRepository.findAll());
        model.addAttribute("allAnswers", answerRepository.findAll());
        return "question/question";
    }

    @PostMapping("/createQuestion")
    public ModelAndView createQuestion(@ModelAttribute ProtoQuestion newQuestion,
                                       Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AppUser currentUser = userRepository.findByName(auth.getName());

        HashSet<Answer> incorrectAnswers = new HashSet<>();
        for (UUID answerId : newQuestion.getIncorrectAnswerIds()) {
            incorrectAnswers.add(answerRepository.findByUuid(answerId));
        }

        Question question = new Question(
                newQuestion,
                answerRepository.findByUuid(newQuestion.getCorrectAnswerId()),
                incorrectAnswers,
                currentUser,
                new HashSet<>()
        );
        questionRepository.save(question);

        Settings settings = settingsRepository.findByName("default"); // possible new feature: change if different settings can be chosen
        model.addAttribute("settings", settings);
        model.addAttribute("questionStatus", "created");
        model.addAttribute("question", question);
        model.addAttribute("checkResult", RequirementValidator.check(question, settings));
        return new ModelAndView("question/questionDetail", "questionModel", model);
    }

    @PostMapping("/deleteQuestion/{id}")
    public String deleteQuestion(@PathVariable UUID id, Model model) {
        Question toBeDeleted = questionRepository.findByUuid(id);
        for (Exam exam : toBeDeleted.getInExams()) {
            exam.getQuestions().remove(toBeDeleted);
        }
        questionRepository.delete(toBeDeleted);
        model.addAttribute("allQuestions", questionRepository.findAll());
        return "question/question";
    }

    @PostMapping("/toUpdateQuestion/{id}")
    public String toUpdateQuestion(@PathVariable UUID id, Model model) {
        model.addAttribute("updateQuestion", questionRepository.findByUuid(id));
        model.addAttribute("allAnswers", answerRepository.findAll());
        return "question/updateQuestion";
    }

    @PostMapping("/updateQuestion/{id}")
    public ModelAndView updateQuestion(@ModelAttribute ProtoQuestion newQuestion, @PathVariable UUID id, Model model) {
        Question updateQuestion = questionRepository.findByUuid(id);

        HashSet<Answer> incorrectAnswers = new HashSet<>();
        for (UUID answerId : newQuestion.getIncorrectAnswerIds()) {
            incorrectAnswers.add(answerRepository.findByUuid(answerId));
        }

        updateQuestion.setFromProto(newQuestion);
        updateQuestion.setCorrectAnswer(answerRepository.findByUuid(newQuestion.getCorrectAnswerId()));
        updateQuestion.setIncorrectAnswers(incorrectAnswers);
        questionRepository.save(updateQuestion);

        Settings settings = settingsRepository.findByName("default"); // possible new feature: change if different settings can be chosen
        model.addAttribute("settings", settings);
        model.addAttribute("question", updateQuestion);
        model.addAttribute("questionStatus", "updated");
        model.addAttribute("checkResult", RequirementValidator.check(updateQuestion, settings));
        return new ModelAndView("question/questionDetail", "updateModel", model);
    }

    @PostMapping("/questionDetail/{id}")
    public String questionDetail(@PathVariable UUID id, Model model) {
        Question question = questionRepository.findByUuid(id);
        Settings settings = settingsRepository.findByName("default"); // possible new feature: change if different settings can be chosen
        model.addAttribute("settings", settings);
        model.addAttribute("question", question);
        model.addAttribute("checkResult", RequirementValidator.check(question, settings));
        return "question/questionDetail";
    }
}
