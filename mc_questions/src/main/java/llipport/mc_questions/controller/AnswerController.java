package llipport.mc_questions.controller;

import llipport.mc_questions.database.entities.Answer;
import llipport.mc_questions.database.entities.Question;
import llipport.mc_questions.database.repositories.AnswerRepository;
import llipport.mc_questions.protoClasses.ProtoAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
public class AnswerController extends GlobalController {
    @Autowired
    private AnswerRepository answerRepository;

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("allAnswers", answerRepository.findAll());
        model.addAttribute("newAnswer", new ProtoAnswer());
    }

    @GetMapping("/answer")
    public String loadAnswers(Model model) {
        return "answer/answer";
    }

    @PostMapping("/createAnswer")
    public ModelAndView createAnswer(@ModelAttribute ProtoAnswer newAnswer, Model model) {
        Answer answer = new Answer(newAnswer.getText());

        answerRepository.save(answer);

        model.addAttribute("answer", answer);
        model.addAttribute("answerStatus", "created");
        return new ModelAndView("answer/answerDetail", "answerModel", model);
    }

    @PostMapping("/deleteAnswer/{id}")
    public String deleteAnswer(@PathVariable UUID id, Model model) {
        Answer toBeDeleted = answerRepository.findByUuid(id);
        if (!toBeDeleted.getCorrectInQuestions().isEmpty()) {
            return "answer/error";
        }
        for (Question question : toBeDeleted.getIncorrectInQuestions()) {
            question.getIncorrectAnswers().remove(toBeDeleted);
        }
        answerRepository.delete(toBeDeleted);
        model.addAttribute("allAnswers", answerRepository.findAll());
        return "answer/answer";
    }

    @PostMapping("/toUpdateAnswer/{id}")
    public String toUpdateAnswer(@PathVariable UUID id, Model model) {
        model.addAttribute("updateAnswer", answerRepository.findByUuid(id));
        return "answer/updateAnswer";
    }

    @PostMapping("/updateAnswer/{id}")
    public ModelAndView updateAnswer(@ModelAttribute ProtoAnswer newAnswer, @PathVariable UUID id, Model model) {
        Answer updateAnswer = answerRepository.findByUuid(id);

        updateAnswer.setText(newAnswer.getText());
        answerRepository.save(updateAnswer);

        model.addAttribute("answer", updateAnswer);
        model.addAttribute("answerStatus", "updated");
        return new ModelAndView("answer/answerDetail", "updateModel", model);
    }

    @PostMapping("/answerDetail/{id}")
    public String answerDetail(@PathVariable UUID id, Model model) {
        model.addAttribute("answer", answerRepository.findByUuid(id));
        return "answer/answerDetail";
    }
}
