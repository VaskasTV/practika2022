package com.db.practice_db.service;

import com.db.practice_db.entities.Base;
import org.springframework.stereotype.Service;
import org.supercsv.cellprocessor.FmtDate;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;


import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DataBaseImpl implements DataBase {
    private final String file = "file.csv";

    @Override
    public void create(Base base) {
        List<Base> bases = new ArrayList<>(readFromCsv());
        bases.add(base);
        writeToCsv(bases);
    }

    @Override
    public List<Base> readAll() {
        return readFromCsv();
    }

    @Override
    public String  numberAll() {
        return numberFromCsV();
    }

    @Override
    public Base read(String name) {
        return readFromCsv().stream()
                .filter(base -> base.getName().equals(name)).findFirst().orElse(null);
    }

    @Override
    public String toString () {
        return "Данное приложение разработано для чтения, записи и удаления данных в/из CSV файла. \n" +
                "Для чтения всех данных из файла пропишите в поисковой строке запрос 'http://localhost:8080/db', и выберете метод GET. \n" +
                "Если вы хотите вывести данные о определенном человеке, допишите в запрос '/{name}'(имя человека которого хотите вывести)" +
                "'http://localhost:8080/db/{name}, и так же выберете метод GET.'  \n" +
                "Если вы хотите добавить человека в базу данных впишите запрос как для чтения 'http://localhost:8080/db', и выберете метод POST " +
                "и впишите данные которые вы хотите ввести в базу данных. \n" +
                "Если вы хотите удалить человека из базы данных, впишите запрос как для чтения определенного человека с припиской '/{name}' " +
                "('http://localhost:8080/db/{name}'), и выберете метод DELETE. \n" +
                "Если вы хотите вывести количество данных в базе данных, то напишите запрос 'http://localhost:8080/number', и выберете метод GET. \n" +
                "Приложение разработано на учебной практике, студентом 2-го курса группы УИС-211 Российского Университета Транспорта (РУТ(МИИТ))\n" +
                "Кандауровым Василием Александровичем.";
    }
    @Override
    public boolean delete(String name) {
        List<Base> bases = readFromCsv();
        Optional<Base> buildingOptional = bases.stream()
                .filter(base -> base.getName().equals(name)).findFirst();
        if(buildingOptional.isEmpty())
            return false;
        bases.remove(buildingOptional.get());
        writeToCsv(bases);
        return true;
    }

    private String numberFromCsV() {
        try(ICsvBeanReader beanReader = new CsvBeanReader(new FileReader(file), CsvPreference.STANDARD_PREFERENCE)) {
            final String[] header = beanReader.getHeader(true);
            final CellProcessor[] processors = getReadProcessors();

            Base base;
            String number;
            Integer i = 0 ;
            while((base = beanReader.read(Base.class, header, processors)) != null) {
                i = i + 1;
            }
            number = "Количество данных: " + Integer.toString(i);
            return number;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Base> readFromCsv() {
        try(ICsvBeanReader beanReader = new CsvBeanReader(new FileReader(file), CsvPreference.STANDARD_PREFERENCE)) {
            final String[] header = beanReader.getHeader(true);
            final CellProcessor[] processors = getReadProcessors();

            List<Base> bases = new ArrayList<>();
            Base base;
            while((base = beanReader.read(Base.class, header, processors)) != null) {
                bases.add(base);
            }
            return bases;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeToCsv(List<Base> bases) {
        try(ICsvBeanWriter beanWriter = new CsvBeanWriter(new FileWriter(file), CsvPreference.STANDARD_PREFERENCE)) {
            final String[] header = new String[] { "name", "description", "date"};
            final CellProcessor[] processors = getWriteProcessors();

            beanWriter.writeHeader(header);
            for(final Base base : bases) {
                beanWriter.write(base, header, processors);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static CellProcessor[] getReadProcessors() {
        return new CellProcessor[] {
                new NotNull(), // id
                new NotNull(), // description
                new ParseDate("yyyy-MM-dd HH:mm"), // date
        };
    }

    private static CellProcessor[] getWriteProcessors() {
        return new CellProcessor[] {
                new NotNull(), // id
                new NotNull(), // description
                new FmtDate("yyyy-MM-dd HH:mm"), // date
        };
    }

}
