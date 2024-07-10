package com.porogoramer;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class WorldEdit {

    public WorldEdit(){}

    public WorldEdit(String type, String link, List<String> version){
        this.type = type;
        this.version = version;
        this.link = link;
    }

    public String type;
    public List<String> version;
    public String link;

    @Override
    public String toString() {
        String toReturn = super.toString();

        toReturn += "\n type= " + type;
        toReturn += "\n version= " + version;
        toReturn += "\n link= " + link;

        return toReturn;
    }

    public String[] toCsvRecord() {
        return new String[]{type, String.join(";", version), link};
    }

    // Create CSV file with list of WorldEdit instances
    public static void createCsvFile(List<WorldEdit> worldEdits, String filePath) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            // Write header
            writer.writeNext(new String[]{"Type", "Version", "Link"});

            // Write records
            for (WorldEdit we : worldEdits) {
                writer.writeNext(we.toCsvRecord());
            }
        }
    }
}
