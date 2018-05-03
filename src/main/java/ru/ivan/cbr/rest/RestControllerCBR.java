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
import ru.ivan.cbr.model.CurrencyDB;
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
        log.info("Получен запрос без даты.");
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String date = now.format(formatter);
        log.info("Ищем курсы в БД за текущую дату: " + date);
        Currencys currencys = currencyDAO.findByDate(date);
        CurrencyDB currencys2 = currencyDAO.findByDateEndCode(date, 944);
        if (currencys2!=null)
            log.info("currencys2: " + currencys2.toString());
        
        if (currencys != null) {
            log.info("В БД есть курсы да текущую дату " + date + " возвращаем курсы из БД.");
            log.info("Курсы из БД: " + currencys.toString());
            return new ResponseEntity<>(currencys, HttpStatus.OK);

        } else {
            log.info("В БД нет курсов за текущую дату: " + date);
            log.info("Берем курсы с сайта ЦБ.");
            currencys = restTemplate.getForObject("http://www.cbr.ru/scripts/XML_daily.asp", Currencys.class);
            log.info("Получили курсы с ЦБ: " + currencys.toString());
            log.info("Пишем их в БД и выдаем в ответе.");
            currencys.setRequestDate(date);
            currencyDAO.save(currencys);
            return new ResponseEntity<>(currencys, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "{date}", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<Currencys> getCurrencysByDate(@PathVariable("date") String datein) {

        if (datein == null || datein.equals("") || !datein.matches("\\d{2}.\\d{2}.\\d{4}")) {
            log.info("Получен запрос с некорректной датой.");
            LocalDate now = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            String date = now.format(formatter);
            log.info("Ищем курсы в БД за текущую дату: " + date);
            Currencys currencys = currencyDAO.findByDate(date);
            if (currencys != null) {
                log.info("В БД есть курсы да текущую дату " + date + " возвращаем курсы из БД.");
                log.info("Курсы из БД: " + currencys.toString());
                return new ResponseEntity<>(currencys, HttpStatus.OK);
            } else {
                log.info("В БД нет курсов за текущую дату: " + date);
                log.info("Берем курсы с сайта ЦБ.");
                currencys = restTemplate.getForObject("http://www.cbr.ru/scripts/XML_daily.asp", Currencys.class);
                log.info("Получили курсы с ЦБ: " + currencys.toString());
                log.info("Пишем их в БД и выдаем в ответе.");
                currencys.setRequestDate(date);
                currencyDAO.save(currencys);
                return new ResponseEntity<>(currencys, HttpStatus.OK);
            }
        } else {
            log.info("Получен запрос с дата: " + datein);
            Currencys currencys = currencyDAO.findByDate(datein);
            if (currencys == null) {
                log.info("В БД нет записи с такой датой!");
                log.info("Отправляем запрос в ЦБ с датой: " + datein);
                currencys = restTemplate.getForObject("http://www.cbr.ru/scripts/XML_daily.asp?date_req=" + datein.replace(".", "/"), Currencys.class);
                log.info("Получили курсы валют: " + currencys.toString());
                currencys.setRequestDate(datein);
                currencyDAO.save(currencys);
                log.info("Сохранили в БД и возвращаем в ответе на запрос!");
                return new ResponseEntity<>(currencys, HttpStatus.OK);
            } else {
                log.info("Получили курсы валют из БД: " + currencys.toString());
                return new ResponseEntity<>(currencys, HttpStatus.OK);
            }
        }
    }

}
