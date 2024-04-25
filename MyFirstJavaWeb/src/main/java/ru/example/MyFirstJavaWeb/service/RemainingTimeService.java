package ru.example.MyFirstJavaWeb.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import ru.example.MyFirstJavaWeb.entity.RemainingTime;

@Service
public interface RemainingTimeService {
    void remainingTime(RemainingTime remainingTime, ModelAndView mav, BindingResult bindingResult);
}
