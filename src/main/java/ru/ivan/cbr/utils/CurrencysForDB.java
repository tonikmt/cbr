/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ivan.cbr.utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;
import ru.ivan.cbr.domain.Currency;
import ru.ivan.cbr.domain.Currencys;
import ru.ivan.cbr.model.CurrencyDB;
import ru.ivan.cbr.model.CurrencysDB;

/**
 *
 * @author ivan
 */
public class CurrencysForDB {
    public static CurrencysDB forDB (Currencys currencys) {
        CurrencysDB cdb = new CurrencysDB();
        cdb.setDate(currencys.getDate());
        cdb.setRequestDate(currencys.getRequestDate());
        cdb.setId(new ObjectId());
        cdb.setName(currencys.getName());
        List <CurrencyDB> curDBList = new ArrayList<>();
        currencys.getCurrencys().stream().map((currency) -> {
            CurrencyDB curDB = new CurrencyDB();
            curDB.setCharCode(currency.getCharCode());
            curDB.setId(currency.getId());
            curDB.setName(currency.getName());
            curDB.setNominal(currency.getNominal());
            curDB.setNumCode(currency.getNumCode());
            curDB.setValue(new BigDecimal(currency.getValue().replace(",", ".")).setScale(4));
            return curDB;
        }).forEachOrdered((curDB) -> {
            curDBList.add(curDB);
        });
        cdb.setCurrencyList(curDBList);
        return cdb;
    }
    public static Currencys currDBForCurr (CurrencysDB currencysDB) {
        Currencys currencys = new Currencys();
        currencys.setDate(currencysDB.getDate());
        currencys.setRequestDate(currencysDB.getRequestDate());
        currencys.setName(currencysDB.getName());
        List <Currency> list = new ArrayList<>();
        currencysDB.getCurrencyList().stream().map((currensyyy) -> {
            Currency c = new Currency();
            c.setCharCode(currensyyy.getCharCode());
            c.setId(currensyyy.getId());
            c.setName(currensyyy.getName());
            c.setNominal(currensyyy.getNominal());
            c.setNumCode(currensyyy.getNumCode());
            c.setValue(currensyyy.getValue().toString());
            return c;
        }).forEachOrdered((c) -> {
            list.add(c);
        });
        currencys.setCurrencys(list);
        return currencys;
    }
    
}
