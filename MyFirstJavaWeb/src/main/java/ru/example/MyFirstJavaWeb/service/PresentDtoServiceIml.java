package ru.example.MyFirstJavaWeb.service;

import org.springframework.stereotype.Service;
import ru.example.MyFirstJavaWeb.dto.PresentDto;
import ru.example.MyFirstJavaWeb.entity.Present;
import ru.example.MyFirstJavaWeb.entity.User;


import java.util.Optional;
@Service
public class PresentDtoServiceIml implements PresentDtoService {
    @Override
    public void setPresentDto(PresentDto presentDto, Optional<Present> optionalPresent, Long presentId) {
        presentDto.setId(optionalPresent.get().getId());
        presentDto.setNumber(optionalPresent.get().getNumber());
        presentDto.setIdea(optionalPresent.get().getIdea());
        presentDto.setDescription(optionalPresent.get().getDescription());
        presentDto.setCost(optionalPresent.get().getCost());
        presentDto.setBirthdayId(optionalPresent.get().getBirthday().getId());
    }
    @Override
    public void setPresent(PresentDto presentDto, Present present, User user) {
        present.setNumber(presentDto.getNumber());
        present.setIdea(presentDto.getIdea());
        present.setDescription(presentDto.getDescription());
        present.setCost(presentDto.getCost());
        present.setUser(user);
    }

}
