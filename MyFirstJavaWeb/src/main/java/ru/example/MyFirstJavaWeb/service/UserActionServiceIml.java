package ru.example.MyFirstJavaWeb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.example.MyFirstJavaWeb.entity.UserActions;
import ru.example.MyFirstJavaWeb.repository.UserActionRepository;

import java.security.Principal;
import java.time.LocalDateTime;

@Service
public class UserActionServiceIml implements UserActionService {
    @Autowired
    private final UserActionRepository userActionRepository;

    public UserActionServiceIml(UserActionRepository userActionRepository) {
        this.userActionRepository = userActionRepository;
    }

    public void writeLog(String action, Principal principal) {
        String userEmail = principal.getName();
        if (userEmail != null) {
            UserActions userActions = new UserActions();
            userActions.setUserEmail(userEmail);
            userActions.setDateActions(LocalDateTime.now());
            userActions.setDescription(action);

            userActionRepository.save(userActions);
        } else {
            throw new RuntimeException(String.format("Пользователь %s не найден", userEmail));
        }
    }
    public void writeLog(String action) {
        UserActions userActions = new UserActions();
        userActions.setDateActions(LocalDateTime.now());
        userActions.setDescription(action);
        userActionRepository.save(userActions);
    }
}
