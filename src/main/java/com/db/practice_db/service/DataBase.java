package com.db.practice_db.service;

import com.db.practice_db.entities.Base;

import java.util.List;

public interface DataBase {
    void create(Base base);

    List<Base> readAll();

    Base read(String name);

    String  numberAll();

    boolean delete(String name);

}
