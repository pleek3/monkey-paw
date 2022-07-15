package com.minequest.minecraft.parser;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.*;

/**
 * Created by YannicK S. on 14.07.2022
 */
public class FileParser {

    private final static String PATH = "src" + File.separator + "main" + File.separator + "resources";
    private final static String FILE_NAME = "Custom-Head-DB.csv";

    public FileParser() {

    }

    public Iterable<CSVRecord> parseFile() {
        File file = new File(PATH + File.separator + FILE_NAME);

        if (!file.exists()) {
            System.out.println("File ist null");
            return null;
        }

        try {
            Reader reader = new FileReader(file);
            return CSVFormat.newFormat(';')
                    .builder()
                    .setHeader("category", "id", "displayname", "skin", "idk", "name")
                    .build()
                    .parse(reader);
        } catch (FileNotFoundException e) {
            //  Bukkit.getLogger().warning("Die Custom Head Datei wurde nicht gefunden:" + e.getMessage());
        } catch (IOException e) {
            //  Bukkit.getLogger().warning("Die Custom Head Datei konnte nicht geladen werden:" + e.getMessage());
        }
        return null;
    }


}
