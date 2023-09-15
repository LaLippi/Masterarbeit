package llipport.mc_questions.controller;

import llipport.mc_questions.database.entities.Answer;
import llipport.mc_questions.database.entities.AppUser;
import llipport.mc_questions.database.entities.Exam;
import llipport.mc_questions.database.entities.Question;
import llipport.mc_questions.database.repositories.AnswerRepository;
import llipport.mc_questions.database.repositories.AppUserRepository;
import llipport.mc_questions.database.repositories.ExamRepository;
import llipport.mc_questions.database.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


@Controller
public class MainController extends GlobalController {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private AppUserRepository userRepository;

    @ModelAttribute
    public void addAttributes(Model model) {
    }

    @GetMapping("/")
    @PostMapping("/")
    public String home(Model model) {
        return mainPage(model);
    }

    @PostMapping("/main")
    public String mainPage(Model model) {
        model.addAttribute("allQuestions", (List<Question>) questionRepository.findAll());
        model.addAttribute("allAnswers", (List<Answer>) answerRepository.findAll());
        model.addAttribute("allExams", (List<Exam>) examRepository.findAll());
        model.addAttribute("allUsers", (List<AppUser>) userRepository.findAll());
        return "main";
    }

}
