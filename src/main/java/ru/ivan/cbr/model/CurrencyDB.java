/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ivan.cbr.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author ivan
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class CurrencyDB {
    private String id;
    private Integer numCode;
    private String charCode;
    private Integer nominal;
    private String name;
    private BigDecimal value;
}
