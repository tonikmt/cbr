/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ivan.cbr.persistence;

import com.mongodb.BasicDBObject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import static org.springframework.data.mongodb.core.query.Query.query;
import org.springframework.stereotype.Component;
import ru.ivan.cbr.domain.Currencys;
import ru.ivan.cbr.model.CurrencyDB;
import ru.ivan.cbr.model.CurrencysDB;
import ru.ivan.cbr.utils.CurrencysForDB;

/**
 *
 * @author ivan
 */
@Component
public class CurrencyDAO {

    @Autowired
    private MongoTemplate mongoTemplate;

    public Currencys findByDate(String date) {
        CurrencysDB currencysDB = mongoTemplate.findOne(query(where("requestDate").is(date)), CurrencysDB.class);
        if (currencysDB == null) {
            return null;
        } else {
            return CurrencysForDB.currDBForCurr(currencysDB);
        }
    }

    public void save(@NonNull Currencys currencys) {
        CurrencysDB cdb = CurrencysForDB.forDB(currencys);
        mongoTemplate.save(cdb);
    }

    public Currencys findByDateEndCode(String date, Integer code) {
        CurrencysDB currencysDB = mongoTemplate.findOne(query(where("requestDate").is(date)), CurrencysDB.class);
        if (currencysDB != null) {
            for (CurrencyDB curr : currencysDB.getCurrencyList()) {
                if (curr.getNumCode().equals(code)) {
                    currencysDB.getCurrencyList().clear();
                    currencysDB.getCurrencyList().add(curr);
                    return CurrencysForDB.currDBForCurr(currencysDB);
                }
            }
        }
        return null;
    }
}
