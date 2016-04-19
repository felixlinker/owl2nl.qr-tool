package org.aksw.simba.owl2nl.qr.tools;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.*;
import java.util.LinkedList;

/**
 * Created by felix on 15.04.2016.
 */
public class CsvParser {
    private File file;
    private BufferedReader fileReader;
    private LinkedList<LinkedList<String>> rows = new LinkedList<>();
    private static final char delimiter = ';';

    public CsvParser(File file) throws FileNotFoundException {
        this.file = file;
        this.initParser();
    }

    public CsvParser(String fileName) throws FileNotFoundException {
        this.file = new File(fileName);
        this.initParser();
    }

    private void initParser() throws FileNotFoundException {
        fileReader = new BufferedReader(new FileReader(file));
        this.parseCsv();
    }

    private void parseCsv() {
        try {
            String row;
            while ((row = fileReader.readLine()) != null) {
                rows.add(splitCells(row));
            }
        } catch (IOException e) {}
    }

    private LinkedList<String> splitCells(String row) {
        LinkedList<String> cells = new LinkedList<>();

        StringBuilder cell = new StringBuilder();
        boolean escaped = false;
        boolean subString = false;
        for (char c: row.toCharArray()) {
            if (escaped) {
                cell.append(c);
                escaped = false;
            } else if (subString) {
                switch (c) {
                    case '\"': subString = false; break;
                    case '\\': escaped = true;
                    default: cell.append(c);
                }
            } else {
                switch (c) {
                    case '"': subString = true; break;
                    case delimiter: {
                        cells.add(StringEscapeUtils.unescapeJava(cell.toString()));
                        cell = new StringBuilder();
                        break;
                    }
                    case '\n': {
                        cells.add(StringEscapeUtils.unescapeJava(cell.toString()));
                        return cells;
                    }
                    case '\\': escaped = true;
                    default: cell.append(c);
                }
            }
        }

        cells.add(StringEscapeUtils.unescapeJava(cell.toString()));
        return cells;
    }

    public LinkedList<LinkedList<String>> getRows() {
        return rows;
    }

    public String getCell(int row, int column) {
        return rows.get(row).get(column);
    }

    public static void main(String[] args) {
        CsvParser csv;
        try {
            csv = new CsvParser("C:/Git/owl2nl.qr-tool/data/axioms.csv");
        } catch (FileNotFoundException e) { return; }

        LinkedList<LinkedList<String>> cells = csv.getRows();
        for (LinkedList<String> row: cells) {
            System.out.println("Starting a new row:");
            for (String cell: row) {
                System.out.println(cell);
            }
            System.out.println();
        }
    }
}
