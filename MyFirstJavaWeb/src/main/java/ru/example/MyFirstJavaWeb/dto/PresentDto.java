package ru.example.MyFirstJavaWeb.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.example.MyFirstJavaWeb.entity.User;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PresentDto {
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String number;
        private String idea;
        private String description;
        private Long cost;

        private User user;
        private Long birthdayId;


}
