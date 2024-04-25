package ru.example.MyFirstJavaWeb.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.example.MyFirstJavaWeb.entity.Birthday;
import ru.example.MyFirstJavaWeb.entity.User;
import ru.example.MyFirstJavaWeb.repository.BirthdayRepository;
import ru.example.MyFirstJavaWeb.service.UserActionService;
import ru.example.MyFirstJavaWeb.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;


@Controller
public class BirthdayController {
    Logger log = LogManager.getLogger(BirthdayController.class);
    @Autowired
    private final BirthdayRepository birthdayRepository;
    @Autowired
    private final UserService userService;
    @Autowired
    private final UserActionService userActionService;

    public BirthdayController(BirthdayRepository birthdayRepository, UserService userService,
                              UserActionService userActionService) {
        this.birthdayRepository = birthdayRepository;
        this.userService = userService;
        this.userActionService = userActionService;
    }


    @GetMapping("/list")
    public ModelAndView getAllBirthdays(Principal principal) {
        log.info("/list -> connection");
        ModelAndView mav = new ModelAndView("/list-birthdays");

        User currentUser = userService.findByEmail(principal.getName());

        List<Birthday> birthdays = birthdayRepository.findByUser(currentUser);

        mav.addObject("birthdays", birthdays);
        return mav;
    }

    @GetMapping("/users/listBirthdaysForAdmin")
    public ModelAndView getBirthdaysForAdmin(@RequestParam String userEmail) {
        log.info("/list -> connection");
        ModelAndView mav = new ModelAndView("/list-birthdays-for-admin");

        User findByEmail = userService.findByEmail(userEmail);

        List<Birthday> birthdays = birthdayRepository.findByUserId(findByEmail.getId());

        mav.addObject("birthdays", birthdays);
        mav.addObject("username", userEmail);
        return mav;
    }

    @GetMapping("addBirthdayForm")
    public ModelAndView addBirthdayForm() {
        ModelAndView mav = new ModelAndView("add-birthday-form");

        Birthday birthday = new Birthday();

        mav.addObject("birthday", birthday);
        return mav;
    }
    @PostMapping("/saveBirthday")
    public String saveBirthday(@Valid @ModelAttribute Birthday birthday, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "add-birthday-form";
        }

        log.info("Добавление приятеля в список дня рождения - {}", principal.getName());
        userActionService.writeLog("Добавление приятеля в список дня рождения", principal);

        User currentUser = userService.findByEmail(principal.getName());

        birthday.setUser(currentUser);
        birthdayRepository.save(birthday);

        log.info("Приятель добален пользователем - {}", principal.getName());
        userActionService.writeLog("Приятель добален пользователем", principal);
        return "redirect:/list";
    }

    @GetMapping("/showUpdateBirthdayForm")
    public ModelAndView showUpdateBirthdayForm(@RequestParam Long birthdayId, Principal principal) {
        log.info("Редактирование списка дня рождения");
        userActionService.writeLog("Редактирование списка дня рождения", principal);

        ModelAndView mav = new ModelAndView("add-birthday-form");

        Optional<Birthday> optionalBirthday = birthdayRepository.findById(birthdayId);
        Birthday birthday = new Birthday();

        if (optionalBirthday.isPresent()) {
            birthday = optionalBirthday.get();
        }

        mav.addObject("birthday", birthday);
        return mav;
    }

    @GetMapping("/deleteBirthday")
    public String deleteBirthday(@RequestParam Long birthdayId, Principal principal) {
        birthdayRepository.deleteById(birthdayId);
        return "redirect:/list";
    }

    @GetMapping("/deleteBirthdayByAdmin")
    public String deleteBirthdayByAdmin(@RequestParam Long birthdayId, Principal principal) {
        log.info("Удаление приятеля из списка дня рождения администратором");
        userActionService.writeLog("Удаление приятеля из списка дня рождения администратором", principal);

        birthdayRepository.deleteById(birthdayId);

        log.info("Приятель удален администратором");
        userActionService.writeLog("Приятель удален администратором", principal);
        return "redirect:/users";
    }
}
