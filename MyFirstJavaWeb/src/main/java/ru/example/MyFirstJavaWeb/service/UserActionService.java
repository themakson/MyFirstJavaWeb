package ru.example.MyFirstJavaWeb.service;

import java.security.Principal;

public interface UserActionService {
    void writeLog(String action, Principal principal);
    void writeLog(String action);
}
