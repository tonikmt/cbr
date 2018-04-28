/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ivan.cbr.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author ivan
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document (collection = "currencys")
public class CurrencysDB implements Serializable{
    @Id
    private ObjectId id;
    
    private String date;
    private String name;
    
    private List<CurrencyDB> currencyList;
}
