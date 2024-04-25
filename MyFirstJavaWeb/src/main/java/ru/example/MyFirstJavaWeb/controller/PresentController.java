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
import ru.example.MyFirstJavaWeb.dto.PresentDto;
import ru.example.MyFirstJavaWeb.entity.Present;
import ru.example.MyFirstJavaWeb.entity.Birthday;
import ru.example.MyFirstJavaWeb.entity.User;
import ru.example.MyFirstJavaWeb.repository.PresentRepository;
import ru.example.MyFirstJavaWeb.repository.BirthdayRepository;
import ru.example.MyFirstJavaWeb.service.PresentDtoService;
import ru.example.MyFirstJavaWeb.service.UserActionService;
import ru.example.MyFirstJavaWeb.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class PresentController {
    private static final Logger log = LogManager.getLogger(PresentController.class);
    @Autowired
    private final PresentRepository presentRepository;
    @Autowired
    private final BirthdayRepository birthdayRepository;
    @Autowired
    private final PresentDtoService presentDtoService;
    @Autowired
    private final UserActionService userActionService;
    @Autowired
    private final UserService userService;

    public PresentController(PresentRepository presentRepository, BirthdayRepository birthdayRepository,
                             PresentDtoService presentDtoService, UserActionService userActionService, UserService userService) {
        this.presentRepository = presentRepository;
        this.birthdayRepository = birthdayRepository;
        this.presentDtoService = presentDtoService;
        this.userActionService = userActionService;
        this.userService = userService;
    }

    @GetMapping("/list/presents")
    public ModelAndView getAllPresents(Principal principal) {
        log.info("/list/presents -> connection");

        ModelAndView mav = new ModelAndView("/list-presents");

        User currentUser = userService.findByEmail(principal.getName());

        List<Present> presents = presentRepository.findByUser(currentUser);
        mav.addObject("presents", presents);

        return mav;
    }

    @GetMapping("/users/listPresentsForAdmin")
    public ModelAndView getPresentsForAdmin(@RequestParam Long birthdayId) {
        log.info("/list/presents -> connection");

        ModelAndView mav = new ModelAndView("/list-presents-for-admin");

        Optional<Birthday> birthday = birthdayRepository.findById(birthdayId);

        User userName = userService.findByBirthday(birthday.get());

        mav.addObject("presents", presentRepository.findByBirthday(birthday.get()));
        mav.addObject("username", userName.getEmail());
        return mav;
    }

    @GetMapping("addPresentForm")
    public ModelAndView addPresentForm(@RequestParam Long birthdayId) {
        ModelAndView mav = new ModelAndView("add-present-form");

        PresentDto presentDto = new PresentDto();
        presentDto.setBirthdayId(birthdayId);

        mav.addObject("presentDto", presentDto);
        return mav;
    }

    @PostMapping("/savePresent")
    public String savePresent(@Valid @ModelAttribute PresentDto presentDto, BindingResult bindingResult,
                               Principal principal) {
        if (bindingResult.hasErrors()) {
            return "add-present-form";
        }

        log.info("Добавление подарка пользователем - {}", principal.getName());
        userActionService.writeLog("Добавление подарка пользователем", principal);

        Present present;
        if (presentDto.getId() != null) {
            present = presentRepository.findById(presentDto.getId())
                    .orElseThrow(() -> new RuntimeException("Подарок не найден"));
        } else {
            present = new Present();
        }
        User currencyUser = userService.findByEmail(principal.getName());
        presentDtoService.setPresent(presentDto, present, currencyUser);

        Birthday birthday = birthdayRepository.findById(presentDto.getBirthdayId())
                .orElseThrow(() -> new RuntimeException("Приятель не найден"));
        present.setBirthday(birthday);

        presentRepository.save(present);

        log.info("Подарок добавлен пользователем - {}", principal.getName());
        userActionService.writeLog("Подарок добавлен пользователем", principal);
        return "redirect:/list";
    }



    @GetMapping("/showUpdatePresentForm")
    public ModelAndView updatePresent(@RequestParam Long presentId, Principal principal) {
        ModelAndView mav = new ModelAndView("add-present-form");

        Optional<Present> optionalPresent = presentRepository.findById(presentId);
        PresentDto presentDto = new PresentDto();

        if (optionalPresent.isPresent()) {
            presentDtoService.setPresentDto(presentDto, optionalPresent, presentId);
        }

        mav.addObject("presentDto", presentDto);
        return mav;
    }

    @GetMapping("/searchPresentByBirthday")
    public ModelAndView searchPresentByBirthday(@RequestParam Long birthdayId) {
        log.info("/list/presents -> connection");

        ModelAndView mav = new ModelAndView("/list-presents");

        Optional<Birthday> birthday = birthdayRepository.findById(birthdayId);

        mav.addObject("presents", presentRepository.findByBirthday(birthday.get()));
        return mav;
    }

    @GetMapping("/deletePresent")
    public String deletePresent(@RequestParam Long presentId) {
        presentRepository.deleteById(presentId);
        return "redirect:/list/presents";
    }

    @GetMapping("/deletePresentByAdmin")
    public String deletePresentByAdmin(@RequestParam Long presentId, Principal principal) {
        log.info("Удаление подарка администратором");
        userActionService.writeLog("Удаление подарка администратором", principal);

        presentRepository.deleteById(presentId);

        log.info("Подарок удален администратором");
        userActionService.writeLog("Подарок удален администратором", principal);
        return "redirect:/users";
    }
}
