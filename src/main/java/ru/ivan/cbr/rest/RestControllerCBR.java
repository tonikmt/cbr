/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ivan.cbr.rest;

import java.awt.PageAttributes;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ru.ivan.cbr.domain.Currencys;
import ru.ivan.cbr.persistence.CurrencyDAO;

/**
 *
 * @author ivan
 */
@Slf4j
@RestController
@RequestMapping("/cbr")
public class RestControllerCBR {

    @Autowired
    CurrencyDAO currencyDAO;

    RestTemplate restTemplate = new RestTemplate();

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<Currencys> getAllCurrencys() {
        Currencys c = restTemplate.getForObject("http://www.cbr.ru/scripts/XML_daily.asp", Currencys.class);
        log.info("Currencys for http://www.cbr.ru/scripts/XML_daily.asp: " + c.toString());
        if (currencyDAO.findByDate(c.getDate()) == null) {
            log.info("Таких курсов нет в БД, пишем их в БД!");
            currencyDAO.save(c);
        }
        return new ResponseEntity<>(c, HttpStatus.OK);
    }

    @RequestMapping(value = "{date}", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<Currencys> getCurrencysByDate(@PathVariable("date") String datein) {

        if (datein == null || datein.equals("") || !datein.matches("\\d{2}.\\d{2}.\\d{4}")) {
            
            Currencys c = restTemplate.getForObject("http://www.cbr.ru/scripts/XML_daily.asp", Currencys.class);
            log.info("Currencys for http://www.cbr.ru/scripts/XML_daily.asp: " + c.toString());
            if (currencyDAO.findByDate(c.getDate()) == null) {
                log.info("Таких курсов нет в БД, пишем их в БД!");
                currencyDAO.save(c);
            }
            return new ResponseEntity<>(c, HttpStatus.OK);
        } else {
            log.info("Получена дата: " + datein);
            Currencys currencys = currencyDAO.findByDate(datein);
            if (currencys == null) {
                log.info("В БД нет записи с такой датой!");
                log.info("Отправляем запрос в ЦБ с датой: "+ datein);
                Currencys c = restTemplate.getForObject("http://www.cbr.ru/scripts/XML_daily.asp?date_req=" + datein.replace(".", "/"), Currencys.class);
                log.info("Получили курсы валют: " + c.toString());
                currencyDAO.save(c);
                log.info("Сохранили в БД и возвращаем в ответе на запрос!");
                return new ResponseEntity<>(c, HttpStatus.OK);
            } else {
                log.info("Получили курсы валют из БД: " + currencys.toString());
                return new ResponseEntity<>(currencys, HttpStatus.OK);
            }
        }
    }

}
