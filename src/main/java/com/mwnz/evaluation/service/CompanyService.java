package com.mwnz.evaluation.service;

import com.mwnz.evaluation.client.XmlCompanyClient;
import com.mwnz.evaluation.exception.CompanyNotFoundException;
import com.mwnz.evaluation.model.Company;
import com.mwnz.evaluation.model.XmlCompany;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {

    private final XmlCompanyClient client;

    public CompanyService(XmlCompanyClient client) {
        this.client = client;
    }

    public Company getCompany(Integer id) {
        XmlCompany xml = client.fetchCompany(id);

        if (xml == null || xml.getId() == null) {
            throw new CompanyNotFoundException("Company not found: " + id);
        }

        return mapToCompany(xml);
    }

    private Company mapToCompany(XmlCompany xml) {
        Company company = new Company();
        company.setId(xml.getId());
        company.setName(xml.getName());
        company.setDescription(xml.getDescription());
        return company;
    }
}
