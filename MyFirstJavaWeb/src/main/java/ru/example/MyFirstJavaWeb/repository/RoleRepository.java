package ru.example.MyFirstJavaWeb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.MyFirstJavaWeb.entity.Role;


public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String roleName);
}
