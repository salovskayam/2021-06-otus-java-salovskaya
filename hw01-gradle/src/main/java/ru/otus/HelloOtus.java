package ru.otus;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;

public class HelloOtus {
    public static void main(String[] args) {
        Table<String, String, Integer> students = HashBasedTable.create();
        students.put("Java Developer", "Ivanov", 68);
        students.put("Java Developer", "Romanov", 50);

        for (Cell<String, String, Integer> cell : students.cellSet()) {
            System.out.println(cell.getRowKey() + ": " + cell.getColumnKey() + " - " + cell.getValue());
        }
    }
}