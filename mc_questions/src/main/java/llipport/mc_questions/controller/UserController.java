package llipport.mc_questions.controller;

import llipport.mc_questions.database.entities.AppUser;
import llipport.mc_questions.database.repositories.AppUserRepository;
import llipport.mc_questions.database.util.ROLE;
import llipport.mc_questions.protoClasses.ProtoUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
public class UserController extends GlobalController {
    @Autowired
    private AppUserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("allUsers", userRepository.findAll());
        model.addAttribute("newUser", new ProtoUser());
    }

    @GetMapping("/user")
    public String loadUsers(Model model) {
        return "user/user";
    }

    @PostMapping("/createUser")
    public ModelAndView createUser(@ModelAttribute ProtoUser newUser, Model model) {
        ROLE userRole;
        switch (newUser.getRoleIndex()) {
            case 1 -> userRole = ROLE.MODERATOR;
            case 2 -> userRole = ROLE.ADMIN;
            default -> userRole = ROLE.USER;
        }
        AppUser user = new AppUser(newUser.getName(), passwordEncoder.encode(newUser.getPassword()), userRole);
        userRepository.save(user);

        model.addAttribute("user", user);
        model.addAttribute("userStatus", "created");
        return new ModelAndView("user/userDetail", "userModel", model);
    }

    @PostMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable UUID id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AppUser currentUser = userRepository.findByName(auth.getName());

        if (currentUser.getUuid() == id) {
            return "user/error";
        }

        userRepository.deleteByUuid(id);
        model.addAttribute("allUsers", userRepository.findAll());
        return "user/user";
    }

    @PostMapping("/toUpdateUser/{id}")
    public String toUpdateUser(@PathVariable UUID id, Model model) {
        model.addAttribute("updateUser", userRepository.findByUuid(id));
        return "user/updateUser";
    }

    @PostMapping("/updateUser/{id}")
    public ModelAndView updateUser(@PathVariable UUID id, @ModelAttribute ProtoUser newUser, Model model) {
        AppUser updateUser = userRepository.findByUuid(id);

        ROLE userRole;
        switch (newUser.getRoleIndex()) {
            case 1 -> userRole = ROLE.MODERATOR;
            case 2 -> userRole = ROLE.ADMIN;
            default -> userRole = ROLE.USER;
        }
        updateUser.setName(newUser.getName());
        updateUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        updateUser.setRole(userRole);
        userRepository.save(updateUser);

        model.addAttribute("user", updateUser);
        model.addAttribute("userStatus", "updated");
        return new ModelAndView("user/userDetail", "userModel", model);
    }

    @PostMapping("/userDetail/{id}")
    public String userDetail(@PathVariable UUID id, Model model) {
        model.addAttribute("user", userRepository.findByUuid(id));
        return "user/userDetail";
    }
}
