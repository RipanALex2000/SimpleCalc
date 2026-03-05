/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.SimpleCalc;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author tempus
 */
public class readWrite {
    public void writeToFile(String content, String fileName, boolean override){
        try {
            FileWriter myWriter = new FileWriter(fileName, override);
            myWriter.write(content);
            myWriter.close();
        } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
           }
    }
    public ArrayList<String> readToFile(String fileName){
        ArrayList<String> myArray = new ArrayList<String>();
        File myObj = new File(fileName);
        
        // try-with-resources: Scanner will be closed automatically
        try (Scanner myReader = new Scanner(myObj)) {
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                myArray.add(data);
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
       }
       return myArray;
    }
}
