package com.mwnz.evaluation.service;

import com.mwnz.evaluation.client.XmlCompanyClient;
import com.mwnz.evaluation.exception.CompanyNotFoundException;
import com.mwnz.evaluation.model.Company;
import com.mwnz.evaluation.model.XmlCompany;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.mwnz.evaluation.exception.UpstreamServiceException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @Mock
    private XmlCompanyClient xmlCompanyClient;

    @InjectMocks
    private CompanyService companyService;

    @Test
    void shouldReturnMappedCompany() {
        XmlCompany xml = new XmlCompany();
        xml.setId(1);
        xml.setName("MWNZ");
        xml.setDescription("..is awesome");

        when(xmlCompanyClient.fetchCompany(1)).thenReturn(xml);

        Company result = companyService.getCompany(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("MWNZ", result.getName());
        assertEquals("..is awesome", result.getDescription());
    }

    @Test
    void shouldThrowExceptionWhenCompanyNotFound() {
        when(xmlCompanyClient.fetchCompany(999))
                .thenThrow(new CompanyNotFoundException("Company not found: 999"));

        assertThrows(CompanyNotFoundException.class, () -> companyService.getCompany(999));
    }

        @Test
    void shouldPropagateUpstreamServiceException() {
        when(xmlCompanyClient.fetchCompany(1))
                .thenThrow(new UpstreamServiceException("Unable to reach company data source"));
    
        UpstreamServiceException ex = assertThrows(
                UpstreamServiceException.class,
                () -> companyService.getCompany(1)
        );
    
        assertEquals("Unable to reach company data source", ex.getMessage());
    }

}