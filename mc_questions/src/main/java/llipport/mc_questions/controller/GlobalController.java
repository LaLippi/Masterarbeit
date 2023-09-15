package llipport.mc_questions.controller;

import llipport.mc_questions.database.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@ControllerAdvice
public abstract class GlobalController {

    @Autowired
    private AppUserRepository userRepository;

    @ModelAttribute
    public abstract void addAttributes(Model model);

    @ModelAttribute
    public void addUser(Model model) {
        model.addAttribute(
                "currentUser",
                userRepository.findByName(SecurityContextHolder.getContext().getAuthentication().getName()));
    }
}
