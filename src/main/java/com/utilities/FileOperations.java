package com.utilities;

import java.io.*;


public class FileOperations {


    public void createFileCopyContent(String fileName, String content) throws IOException {
        File file=new File("target/"+fileName+".html");
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


