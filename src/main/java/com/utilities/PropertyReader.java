package com.utilities;

import java.util.*;
import java.io.*;
public class PropertyReader {

        public static String getPropertyValue(String propertyName) {

            try {
                FileInputStream reader = new FileInputStream("src/main/resources/properties/environment.properties");
                Properties read=new Properties();
                read.load(reader);
                return read.getProperty(propertyName);
            }catch (IOException e){
                return "File or value not present";
            }
            }

        }

