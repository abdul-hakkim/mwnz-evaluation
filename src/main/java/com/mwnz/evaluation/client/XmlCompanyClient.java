package com.mwnz.evaluation.client;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.mwnz.evaluation.config.CompanyApiProperties;
import com.mwnz.evaluation.exception.CompanyNotFoundException;
import com.mwnz.evaluation.exception.UpstreamServiceException;
import com.mwnz.evaluation.model.XmlCompany;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
public class XmlCompanyClient {

    private final RestClient restClient;
    private final XmlMapper xmlMapper;
    private final CompanyApiProperties companyApiProperties;

    public XmlCompanyClient(RestClient.Builder restClientBuilder,
                            CompanyApiProperties companyApiProperties) {
        this.restClient = restClientBuilder.build();
        this.xmlMapper = new XmlMapper();
        this.companyApiProperties = companyApiProperties;
    }

    public XmlCompany fetchCompany(Integer id) {
        String url = companyApiProperties.getBaseUrl() + "/" + id + ".xml";

        try {
            String xmlContent = restClient.get()
                    .uri(url)
                    .accept(MediaType.APPLICATION_XML, MediaType.TEXT_XML, MediaType.TEXT_PLAIN)
                    .retrieve()
                    .body(String.class);

            if (xmlContent == null || xmlContent.trim().isEmpty()) {
                throw new CompanyNotFoundException("Company not found: " + id);
            }

            return xmlMapper.readValue(xmlContent, XmlCompany.class);

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new CompanyNotFoundException("Company not found: " + id);
            }
            throw new UpstreamServiceException("Unable to reach company data source", e);
        } catch (RestClientException e) {
            throw new UpstreamServiceException("Unable to reach company data source", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse company " + id, e);
        }
    }
}