package com.mwnz.evaluation.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

@Data
@JacksonXmlRootElement(localName = "Data")
public class XmlCompany {
    private Integer id;
    private String name;
    private String description;
}