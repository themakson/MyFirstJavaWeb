package ru.example.MyFirstJavaWeb.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.example.MyFirstJavaWeb.dto.UserDto;
import ru.example.MyFirstJavaWeb.entity.Role;
import ru.example.MyFirstJavaWeb.entity.Birthday;
import ru.example.MyFirstJavaWeb.entity.User;
import ru.example.MyFirstJavaWeb.repository.RoleRepository;
import ru.example.MyFirstJavaWeb.repository.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public UserServiceImpl(PasswordEncoder passwordEncoder, RoleRepository roleRepository,
                           UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role roleAdmin = roleRepository.findByName("ROLE_ADMIN");
        Role roleReadOnly = roleRepository.findByName("ROLE_READ");;
        Role roleUser = roleRepository.findByName("ROLE_USER");
        if (roleUser == null) {
            roleUser = checkRoleExist("ROLE_USER");
        }
        if (roleReadOnly == null) {
            roleReadOnly = checkRoleExist("ROLE_READ");
        }
        if (roleAdmin != null) {
            user.setRoles(Arrays.asList(roleReadOnly));
        }
        if (roleAdmin == null) {
            roleAdmin = checkRoleExist("ROLE_ADMIN");
            user.setRoles(Arrays.asList(roleAdmin));
        }
        userRepository.save(user);
    }

    @Override
    public void giveAdminRole(@RequestParam String email, ModelAndView mav) {
        User user = userRepository.findByEmail(email);
        Role roleAdmin = roleRepository.findByName("ROLE_ADMIN");
        if (user != null && roleAdmin != null) {
            user.setRoles(new ArrayList<>(Collections.singletonList(roleAdmin)));
        } else {
            mav.setViewName("/users");
            System.out.println("Невозможно присвоить роль");
        }
        userRepository.save(user);
    }
    @Override
    public void giveUserRole(@RequestParam String email, ModelAndView mav) {
        User user = userRepository.findByEmail(email);
        Role roleUser = roleRepository.findByName("ROLE_USER");
        if (user != null && roleUser != null) {
            user.setRoles(new ArrayList<>(Collections.singletonList(roleUser)));
        } else {
            mav.setViewName("/users");
            System.out.println("Невозможно присвоить роль");
        }
        userRepository.save(user);
    }
    @Override
    public void giveReadRole(@RequestParam String email, ModelAndView mav) {
        User user = userRepository.findByEmail(email);
        Role roleRead = roleRepository.findByName("ROLE_READ");
        if (user != null && roleRead != null) {
            user.setRoles(new ArrayList<>(Collections.singletonList(roleRead)));
        } else {
            mav.setViewName("/users");
            System.out.println("Невозможно присвоить роль");
        }
        userRepository.save(user);
    }

    @Override
    public void deleteUserByEmail(String userEmail, Model model) {
        User user = userRepository.findByEmail(userEmail);
        if (user != null) {
            user.getRoles().clear();
            user.getPresents().clear();
            user.getBirthdays().clear();
            try {
                userRepository.delete(user);
            } catch (Exception e) {
                System.err.println("Ошибка при удалении пользователя " + e.getMessage());
            }
        }
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findByBirthday(Birthday birthday) {
        User user = userRepository.findByBirthdays(birthday);
        return user;
    }

    public List<Object> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> mapToUserDto(user))
                .collect(Collectors.toList());
    }

    private Object mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        String[] str = user.getName().split(" ");
        userDto.setFirstName(str[0]);
        userDto.setLastName(str[1]);
        userDto.setEmail(user.getEmail());
        String roles = user.getRoles()
                .stream()
                .map(role -> role.getName())
                .collect(Collectors.joining(", "));

        userDto.setRole(roles);
        return userDto;
    }

    private Role checkRoleExist(String roleName) {
        Role role = new Role();
        role.setName(roleName);
        return roleRepository.save(role);
    }
}
