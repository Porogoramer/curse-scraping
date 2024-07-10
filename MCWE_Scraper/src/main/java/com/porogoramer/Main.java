package com.porogoramer;
import org.apache.commons.text.WordUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        //Getting the data
        File input = new File("src/main/html/output.html");
        Document doc = Jsoup.parse(input, "UTF-8");

        //files-table-container contains table with all values
        Element table = doc.getElementsByClass("files-table-container").get(0);
        //file-row is BALLS
        Elements rows = table.getElementsByClass("file-row");

        //Create class for each worldEdit option
        List<WorldEdit> list = createWEs(rows);

        String filePath = "src/main/output/file.csv";
        try {
            WorldEdit.createCsvFile(list, filePath);
            System.out.println("CSV file created successfully: " + filePath);
        } catch (IOException e) {
            System.err.println("Error creating CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static List<WorldEdit> createWEs(Elements rows){
        List<WorldEdit> list = new ArrayList<>();
        for (Element row : rows){
            list.add(new WorldEdit(getType(row), getLink(row), getVersion(row)));
        }
        return list;
    }
    public static String getType(Element row){
        //All values of row are stored in div with class tooltip-wrapper
        Elements els = row.select(".tooltip-wrapper");
        //First value is always the type
        String version = els.get(0).getElementsByTag("span").get(0).text();
        return version;
    }
    public static List<String> getVersion(Element row){
        //All values of row are stored in div with class tooltip-wrapper,
        // ignore class .more to avoid choosing wrong div
        Elements els = row.select(".tooltip-wrapper:not(.more)");
        //First value is always the type
        String[] version = els.get(1).getElementsByTag("ul").get(0).getElementsByTag("li").text().split(" ");

        return Arrays.asList(version);
    }
    public static String getLink(Element row){
        //Html doesn't include base url
        String baseUrl = "https://www.curseforge.com";

        Elements el = row.select("a");
        //Second a tag always the link to download
        String link = baseUrl + el.get(2).attr("href");

        return link;
    }
}