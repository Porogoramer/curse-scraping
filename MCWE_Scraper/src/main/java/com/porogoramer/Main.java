package com.porogoramer;
import org.apache.commons.text.WordUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.*;
import java.lang.annotation.ElementType;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        String url = args[0];
        String fileName = args[1];

        runScript(url, fileName);

        //Getting the data
        File input = new File(fileName);
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

    public static void runScript(String url, String filename){
        //Be able to check with os is used
        String command = "node";
        if (System.getProperty("os.name").equals("Linux")) command = "xvfb-run " + command;

        //Current script location is rootproject/index.js
        String scriptPath = Paths.get("..", "index.js").toString();

        ProcessBuilder processBuilder = new ProcessBuilder(command, scriptPath, url, filename);
        try{
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            System.out.println("Script executed with exit code : " + exitCode);
        }catch(Exception e){
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