package org.aksw.simba.owl2nl.qr.tools;

import org.aksw.simba.webelements.Link;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by felix on 19.04.2016.
 */
public class CsvMapper {
    public static void main(String[] args) {
        HashMap<String, String> verbalizationsMap = new HashMap<>();
        CsvParser instances, verbalizations;
        try {
            instances = new CsvParser("C:/Git/owl2nl.qr-tool/data/instances.csv");
            verbalizations = new CsvParser("C:/Git/owl2nl.qr-tool/data/instances_verb.csv");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        for (LinkedList<String> row: verbalizations.getRows()) {
            if (row.size() != 2) {
                System.out.println("Wrong verbalization");
                continue;
            }

            verbalizationsMap.put(row.get(0), row.get(1));
        }

        LinkedList<LinkedList<String>> newCsv = new LinkedList<>();
        for (LinkedList<String> row: instances.getRows()) {
            LinkedList<String> newRow = new LinkedList<>();
            for (String cell: row) {
                if (!cell.equals("")) {
                    newRow.add(cell);
                }

                String verbalization = verbalizationsMap.get(cell);
                if (verbalization != null) {
                    newRow.add(verbalization);
                }
            }
            newCsv.add(newRow);
        }

        FileWriter fw;
        try {
            fw = new FileWriter("C:/Git/owl2nl.qr-tool/data/tmp.csv");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        for (LinkedList<String> row: newCsv) {
            for (String cell: row) {
                try {
                    fw.append(cell);
                    fw.append(';');
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }
            }
            try {
                fw.append('\n');
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }

        try {
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }
}
