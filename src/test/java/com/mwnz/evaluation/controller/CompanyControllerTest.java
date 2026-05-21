package com.mwnz.evaluation.controller;

import com.mwnz.evaluation.exception.CompanyNotFoundException;
import com.mwnz.evaluation.model.Company;
import com.mwnz.evaluation.service.CompanyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import com.mwnz.evaluation.exception.UpstreamServiceException;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CompanyController.class)
class CompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompanyService companyService;

    @Test
    void shouldReturnCompanyWhenFound() throws Exception {
        Company company = new Company();
        company.setId(1);
        company.setName("MWNZ");
        company.setDescription("..is awesome");

        when(companyService.getCompany(1)).thenReturn(company);

        mockMvc.perform(get("/companies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("MWNZ"))
                .andExpect(jsonPath("$.description").value("..is awesome"));
    }

    @Test
    void shouldReturnCompany2WhenFound() throws Exception {
        Company company = new Company();
        company.setId(2);
        company.setName("Another Company");
        company.setDescription("This is the second sample company");

        when(companyService.getCompany(2)).thenReturn(company);

        mockMvc.perform(get("/companies/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Another Company"));
    }

    @Test
    void shouldReturn404WhenCompanyNotFound() throws Exception {
        when(companyService.getCompany(999))
                .thenThrow(new CompanyNotFoundException("Company not found: 999"));

        mockMvc.perform(get("/companies/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.error_description").value("Company not found: 999"));
    }

    @Test
    void shouldReturn502WhenUpstreamServiceFails() throws Exception {
        when(companyService.getCompany(1))
                .thenThrow(new UpstreamServiceException("Unable to reach company data source"));

        mockMvc.perform(get("/companies/1"))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.error").value("UPSTREAM_ERROR"))
                .andExpect(jsonPath("$.error_description").value("Unable to reach company data source"));
    }
}