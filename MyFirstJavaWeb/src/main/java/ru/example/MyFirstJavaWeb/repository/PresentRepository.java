package ru.example.MyFirstJavaWeb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.MyFirstJavaWeb.entity.Present;
import ru.example.MyFirstJavaWeb.entity.Birthday;
import ru.example.MyFirstJavaWeb.entity.User;

import java.util.List;

public interface PresentRepository extends JpaRepository<Present, Long> {
    List<Present> findByBirthday(Birthday birthday);
    List<Present> findByUser(User user);
}
