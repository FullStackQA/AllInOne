package com.utilities;


import java.io.*;



public class ReportGenerator {

    public static void  saveReport(String fileName, String content) throws IOException {
        File file=new File("target/"+fileName+".html");
        System.out.println();
        boolean fvar = file.createNewFile();
        if (file.createNewFile()){
            System.out.println("File is created!");
        }else{
            System.out.println("File already exists.");
        }
        Writer writer = null;
        writer = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        bufferedWriter.write(content);
    }

}

