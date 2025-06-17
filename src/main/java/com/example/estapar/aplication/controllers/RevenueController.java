package com.example.estapar.aplication.controllers;

import com.example.estapar.aplication.services.RevenueService;
import com.example.estapar.domain.dtos.RevenueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
public class RevenueController {

    @Autowired
    private RevenueService revenueService;

    @GetMapping("/revenue")
    public RevenueDTO getRevenue(@RequestParam String date, @RequestParam String sector) {
        return revenueService.getRevenue(sector, LocalDate.parse(date));
    }
}
