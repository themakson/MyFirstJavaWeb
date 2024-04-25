package ru.example.MyFirstJavaWeb.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "presents")
public class Present {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "number")
    private String number;
    @Column(name = "idea")
    private String idea;
    @Column(name = "description")
    private String description;
    @Column(name = "cost")
    private Long cost;
    @ManyToOne
    @JoinColumn(name = "birthday_id")
    private Birthday birthday;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
