package ru.example.MyFirstJavaWeb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.MyFirstJavaWeb.entity.Birthday;
import ru.example.MyFirstJavaWeb.entity.User;

import java.util.List;
public interface BirthdayRepository extends JpaRepository<Birthday, Long> {
    List<Birthday> findByUser(User user);
    List<Birthday> findByUserId(Long userId);

}
