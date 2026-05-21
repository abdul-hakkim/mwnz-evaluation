package com.mwnz.evaluation.controller;

import com.mwnz.evaluation.model.Company;
import com.mwnz.evaluation.service.CompanyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService service;

    public CompanyController(CompanyService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public Company getCompany(@PathVariable Integer id) {
        return service.getCompany(id);
    }
}
