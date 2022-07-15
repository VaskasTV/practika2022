package com.db.practice_db.controllers;

import com.db.practice_db.entities.Base;
import com.db.practice_db.service.DataBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
public class BDController {
    private final DataBase dataBase;

    @Autowired
    public BDController(DataBase dataBase) {
        this.dataBase = dataBase;
    }

    @PostMapping(value = "/db")
    public ResponseEntity<?> create(@RequestBody Base base) {
        dataBase.create(base);
        return ResponseEntity.ok().header("Base", "New data has been added").body("");
    }

    @GetMapping(value = "/number")
    public ResponseEntity<String> numberAll() {

        return ResponseEntity.ok(dataBase.numberAll()  );
    }

    @GetMapping(value = "/db")
    public ResponseEntity<List<Base>> read() {
        final List<Base> base = dataBase.readAll();

        return base != null && !base.isEmpty()
                ? new ResponseEntity<>(base, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/db/{name}")
    public ResponseEntity<Base> read(@PathVariable(name = "name") String name) {
        final Base base = dataBase.read(name);

        return base != null
                ? new ResponseEntity<>(base, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "")
    public ResponseEntity<String> info() {

        return ResponseEntity.ok(dataBase.toString());
    }

    @DeleteMapping(value = "/db/{name}")
    public ResponseEntity<?> delete(@PathVariable(name = "name") String name) {
        final boolean deleted = dataBase.delete(name);

        return ResponseEntity.ok().header("Base", "Data deleted").body("");
    }

}
