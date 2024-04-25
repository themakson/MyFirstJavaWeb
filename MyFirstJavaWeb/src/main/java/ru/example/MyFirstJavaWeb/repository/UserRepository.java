package ru.example.MyFirstJavaWeb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.MyFirstJavaWeb.entity.Birthday;
import ru.example.MyFirstJavaWeb.entity.User;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByBirthdays(Birthday birthday);
}
