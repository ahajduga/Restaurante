package com.restaurante.controllers;

import com.restaurante.persistence.Table;
import com.restaurante.persistence.User;
import com.restaurante.persistence.dao.TableDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by alex on 29.03.16.
 */
@PreAuthorize("permitAll")
@RestController
public class TableController {

    @Autowired
    TableDao tableDao;

    @RequestMapping(value = "/tables", method = RequestMethod.GET)
    public Iterable<Table> getTables(){
        return tableDao.findAll();
    }

    @RequestMapping(value = "/tables", method = RequestMethod.POST)
    public Table createTable(
            @RequestParam(value = "seats", required = true) Integer seats
    ){
        Table table = new Table();
        table.setSeats(seats);
        tableDao.save(table);
        return table;
    }

    @RequestMapping(value = "/tables/{id}", method = RequestMethod.POST)
    public Table updateTable(
            @PathVariable Integer id,
            @RequestParam(value = "seats", required = true) Integer seats
    ){
        Table table = null;
        for (Table p : tableDao.findAll()) {
            if (p.getTableID()==id) {
                table = p;
                break;
            }
        }
        if (seats != null)
            table.setSeats(seats);
        tableDao.save(table);
        return table;
    }

    @RequestMapping(value = "/tables/{id}", method = RequestMethod.DELETE)
    public boolean deleteTable(
            @PathVariable Integer id
    ){
        Table table = null;
        for (Table p : tableDao.findAll()) {
            if (p.getTableID()==id) {
                table = p;
                break;
            }
        }
        tableDao.delete(table);
        return true;
    }
}
