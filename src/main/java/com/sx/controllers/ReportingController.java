package com.sx.controllers;

import com.sx.formatters.DateWithoutTime;
import com.sx.models.SportSession;
import com.sx.service.SessionService;
import com.sx.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Controller
public class ReportingController {

    @Autowired
    private SessionService sessionService;
    @Autowired
    private TrainerService trainerService;

    @RequestMapping(value="report", method= RequestMethod.POST)
    public String report(@RequestParam ("month") @DateWithoutTime Date month , Model model, HttpServletRequest hhtpRequest) {
        List<SportSession>sessionsOfMonth = sessionService.findSessionsOfMonth(trainerService.findByUsername(hhtpRequest.getRemoteUser()), month);
        int NrOfApporvedSessions = 0;
        for (SportSession sportSession : sessionsOfMonth){
            if (sportSession.isApproved()) NrOfApporvedSessions++;
        }
        month.setTime(month.getTime()+(24*60*60*1000));//adds 1 day in miliseconds to correct wrong html output from GUI
        model.addAttribute("month", new SimpleDateFormat("MMMM yyyy").format(month));//MMMM displays full name of month
        model.addAttribute("NrOfApporvedSessions", NrOfApporvedSessions);
        model.addAttribute("sessionsOfMonth", sessionsOfMonth);
        return "/reporting";
    }
}