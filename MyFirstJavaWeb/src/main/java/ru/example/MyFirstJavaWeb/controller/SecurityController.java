package ru.example.MyFirstJavaWeb.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.example.MyFirstJavaWeb.dto.UserDto;
import ru.example.MyFirstJavaWeb.entity.User;
import ru.example.MyFirstJavaWeb.entity.UserActions;
import ru.example.MyFirstJavaWeb.repository.UserActionRepository;
import ru.example.MyFirstJavaWeb.service.UserActionService;
import ru.example.MyFirstJavaWeb.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
public class SecurityController {
    Logger log = LogManager.getLogger(BirthdayController.class);
    @Autowired
    private final UserService userService;
    @Autowired
    private final UserActionService userActionService;
    @Autowired
    private final UserActionRepository userActionRepository;


    public SecurityController(UserService userService, UserActionService userActionService,
                              UserActionRepository userActionRepository1) {
        this.userService = userService;
        this.userActionService = userActionService;
        this.userActionRepository = userActionRepository1;
    }

    @GetMapping("/index")
    public String home() {
        return "index";
    }

    @GetMapping("/aboutApp")
    public String about() {
        return "aboutApp";
    }

    @GetMapping("/showLogs")
    public ModelAndView showLogs() {
        ModelAndView mav = new ModelAndView("list-logs");

        List<UserActions> userActions = userActionRepository.findAll();

        mav.addObject("userActions", userActions);
        return mav;
    }


    @GetMapping("/login")
    public String login() {

        return "login";
    }

    @GetMapping("register")
    public String showRegistrationForm(Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        return "register";
    }

    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto,
                               BindingResult result, Model model) {
        log.info("Регистрация пользователя");
        userActionService.writeLog("Регистрация пользователя");

        User existingUser = userService.findByEmail(userDto.getEmail());

        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
            result.rejectValue("email", null,
                    "На этот адрес электронной почты уже зарегистрирована учетная запись");
        }

        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            return "/register";
        }

        userService.saveUser(userDto);
        log.info("Новый зарегистрированный пользователь {}", userDto.getEmail());
        userActionService.writeLog("Новый зарегистрированный пользователь");
        return "redirect:/register?success";
    }

    @GetMapping("/users")
    public String users(Model model, Principal principal) {
        log.info("Список всех пользователей");
        userActionService.writeLog("Список всех пользователей", principal);

        List<Object> users = userService.findAllUsers();

        model.addAttribute("users", users);

        return "users";


    }

    @GetMapping("/deleteUser")
    public String deleteBirthday(@RequestParam String userEmail, Model model) {
        userService.deleteUserByEmail(userEmail, model);
        return "redirect:/users";
    }

    @GetMapping("/addRoleAdmin")
    public String addRoleAdmin(@RequestParam String userEmail, Principal principal, ModelAndView mav) {
        log.info("Выдача роли \"ROLE_ADMIN\" {}", principal.getName());
        userActionService.writeLog("Выдача роли - \"ROLE_ADMIN\"", principal);


        userService.giveAdminRole(userEmail, mav);

        log.info("Роль \"ROLE_ADMIN\" успешно выдана администратором {}", principal.getName());
        userActionService.writeLog("Роль \"ROLE_ADMIN\" успешно выдана администратором", principal);
        return "redirect:/users";
    }

    @GetMapping("/addRoleUser")
    public String addRoleUser(@RequestParam String userEmail, Principal principal, ModelAndView mav) {
        log.info("Выдача роли \"ROLE_USER\" {}", principal.getName());
        userActionService.writeLog("Выдача роли \"ROLE_USER\"", principal);

        userService.giveUserRole(userEmail, mav);

        log.info("Роль \"ROLE_USER\" успешно выдана администратором {}", principal.getName());
        userActionService.writeLog("Роль \"ROLE_USER\" успешно выдана администратором", principal);
        return "redirect:/users";
    }

    @GetMapping("/addRoleRead")
    public String addRoleRead(@RequestParam String userEmail, Principal principal, ModelAndView mav) {
        log.info("Выдача роли \"ROLE_READ\" {}", principal.getName());
        userActionService.writeLog("Выдача роли \"ROLE_READ\"", principal);

        userService.giveReadRole(userEmail, mav);

        log.info("Роль \"ROLE_READ\" успешно выдана администратором {}", principal.getName());
        userActionService.writeLog("Роль \"ROLE_READ\" успешно выдана администратором", principal);
        return "redirect:/users";
    }
}
