package ru.example.MyFirstJavaWeb.service;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.example.MyFirstJavaWeb.dto.UserDto;
import ru.example.MyFirstJavaWeb.entity.Birthday;
import ru.example.MyFirstJavaWeb.entity.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);
    void deleteUserByEmail(String userEmail, Model model);
    User findByEmail(String email);
    User findByBirthday(Birthday birthday);
    List<Object> findAllUsers();
    void giveAdminRole(@RequestParam String email, ModelAndView mav);

    void giveUserRole(@RequestParam String email, ModelAndView mav);

    void giveReadRole(@RequestParam String email, ModelAndView mav);
}
