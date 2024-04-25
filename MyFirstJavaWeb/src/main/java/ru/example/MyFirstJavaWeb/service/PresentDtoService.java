package ru.example.MyFirstJavaWeb.service;

import ru.example.MyFirstJavaWeb.dto.PresentDto;
import ru.example.MyFirstJavaWeb.entity.Present;
import ru.example.MyFirstJavaWeb.entity.User;

import java.util.Optional;

public interface PresentDtoService {
    void setPresentDto(PresentDto presentDto, Optional<Present> optionalPresent, Long presentId);

    void setPresent(PresentDto presentDto, Present present, User user);
}
