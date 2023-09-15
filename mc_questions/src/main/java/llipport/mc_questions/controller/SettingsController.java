package llipport.mc_questions.controller;

import llipport.mc_questions.database.entities.Settings;
import llipport.mc_questions.database.repositories.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SettingsController extends GlobalController {

    @Autowired
    SettingsRepository settingsRepository;

    @Override
    public void addAttributes(Model model) {

    }

    @GetMapping("/settings")
    public String loadSettings(Model model) {
        model.addAttribute("defaultSettings", settingsRepository.findByName("default"));
        return "settings/settings";
    }

    @PostMapping("/setSettings")
    public String setSettings(@ModelAttribute Settings defaultSettings, Model model) {
        Settings req = settingsRepository.findByName(defaultSettings.getName());
        req.copyFrom(defaultSettings);
        settingsRepository.save(req);
        model.addAttribute("defaultSettings", settingsRepository.findByName("default"));
        return "settings/settings";
    }
}
