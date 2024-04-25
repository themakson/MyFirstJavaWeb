package ru.example.MyFirstJavaWeb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.MyFirstJavaWeb.entity.UserActions;

public interface UserActionRepository extends JpaRepository<UserActions, Long> {
}
