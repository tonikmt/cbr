/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ivan.cbr.domain;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author ivan
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Valute")
public class Currency {
    
    @XmlAttribute(name = "ID")
    private String id;

    @XmlElement(name = "NumCode", required = true)
    private int numCode;

    @XmlElement(name = "CharCode", required = true)
    private String charCode;

    @XmlElement(name = "Nominal", required = true)
    private int nominal;

    @XmlElement(name = "Name", required = true)
    private String name;

    @XmlElement(name = "Value", required = true)
    private String value;
}
