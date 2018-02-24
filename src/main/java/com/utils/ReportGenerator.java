package com.utils;


import java.io.*;


public class ReportGenerator {

    public static void  saveReport(String sFileName, String content) throws IOException {
        File file = new File("target/"+sFileName+".har");
        boolean fvar = file.createNewFile();
        if (file.createNewFile()){
            System.out.println("File is created!");
        }else{
            System.out.println("File already exists.");
        }

        File reportFile = new File("target/"+sFileName+".html");

        Writer writer = new FileWriter(reportFile);
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        bufferedWriter.write(content);

    }
}
