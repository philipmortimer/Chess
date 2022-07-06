/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package philipm.cs5.software.development.neural.network.train;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This is NOT a part of the original project. This is implemented to allow for
 * the code to be uploaded to GitHub. To do this, training and test data
 * has been split into files below 100 mb in size. These files need to be
 * pieced together to allow the original code to function.
 * @author phili
 */
public class DataLimitPreProcess {
    //File indicates whether training and test data has been remade.
    private static final String PRE_PROCESSING_FILE = 
            "preProcessGithubOccurred.txt";
    private static final String PRE_PROCESSED_MESSAGE = "true";
    private static final int NO_TEST_FILES = 3;
    private static final int NO_TRAIN_FILES = 35;
    private static final char TRAIN_POST = 'r';
    private static final char TEST_POST = 'e';
    /**
     * Creates training data and test data files from smaller group of files
     */
    public static void reconstructFiles(){
        try{
            FileReader read = new FileReader(PRE_PROCESSING_FILE);
            BufferedReader buffRead = new BufferedReader(read);
            boolean alreadyRecon = PRE_PROCESSED_MESSAGE.equals(buffRead.readLine());
            buffRead.close(); read.close();
            //Checks to see whether preprocessing has already occurred
            if(alreadyRecon){
                return;
            }
            reconFile(PhilipMCS5SoftwareDevelopmentNeuralNetworkTrain.TEST_FILE_NAME,
                    TEST_POST, NO_TEST_FILES);
            reconFile(PhilipMCS5SoftwareDevelopmentNeuralNetworkTrain.TRAIN_FILE_NAME,
                    TRAIN_POST, NO_TRAIN_FILES);
            FileWriter w = new FileWriter(PRE_PROCESSING_FILE, false);
            BufferedWriter buffW = new BufferedWriter(w);
            buffW.write(PRE_PROCESSED_MESSAGE);
            buffW.flush(); w.flush();
            buffW.close(); w.close();
        }catch(IOException e){
            System.out.println("Error " + e);
        }
    }
    
    /**
     * Converts all smaller training or test files into one larger files
     * @param fileName The file name of the new file (e.g. "training.txt")
     * @param postFix The postfix for the smaller file
     * @param noFiles The number of smaller files
     */
    private static void reconFile(String fileName, char postFix, int noFiles) throws IOException{
        List<String> fileCont = new ArrayList<>(postFix == TEST_POST? PhilipMCS5SoftwareDevelopmentNeuralNetworkTrain.NO_OF_TEST_BOARDS :
                PhilipMCS5SoftwareDevelopmentNeuralNetworkTrain.NO_OF_TRAINING_BOARDS);
        //Reads file contents
        String lineRead = null;
        for(int file = 0; file < noFiles; file++){
            FileReader read = new FileReader(String.valueOf(file) + postFix + ".txt");
            BufferedReader buffRead = new BufferedReader(read);
            while ((lineRead = buffRead.readLine()) != null){
                fileCont.add(lineRead);
            }
            buffRead.close(); read.close();
        }
        //Writes to file
        FileWriter w = new FileWriter(fileName, false);
        BufferedWriter buffW = new BufferedWriter(w);
        boolean firstLine = true;
        for(String line : fileCont) {
            if(!firstLine) buffW.write('\n');
            else firstLine = false;
            buffW.write(line);
        }
        buffW.flush(); w.flush();
        buffW.close(); w.close();
    }
    
    /**
     * Converts training and test data into a series of smaller files to
     * overcome GitHub's file size limit
     */
    public static void convertToSmallerFiles(){
        try{
            splitData(PhilipMCS5SoftwareDevelopmentNeuralNetworkTrain.TEST_FILE_NAME,
                    TEST_POST, NO_TEST_FILES);
            splitData(PhilipMCS5SoftwareDevelopmentNeuralNetworkTrain.TRAIN_FILE_NAME,
                    TRAIN_POST, NO_TRAIN_FILES);
        }catch(IOException e){
            System.out.println("Error " + e);
        }
    }
    
    /**
     * Converts data from file to an array
     * @param fileName The file name
     * @return The data
     */
    private static List<String> getData(String fileName) throws IOException{
        FileReader read = new FileReader(fileName);
        BufferedReader buffRead = new BufferedReader(read);
        List<String> data = new ArrayList(PhilipMCS5SoftwareDevelopmentNeuralNetworkTrain.TEST_FILE_NAME.equals(fileName)?
                PhilipMCS5SoftwareDevelopmentNeuralNetworkTrain.NO_OF_TEST_BOARDS :
                PhilipMCS5SoftwareDevelopmentNeuralNetworkTrain.NO_OF_TRAINING_BOARDS);
        String lineRead = null;
        int i = 0;
        while ((lineRead = buffRead.readLine()) != null){
            data.add(lineRead);
        }
        buffRead.close(); read.close();
        return data;
    }
    
    /**
     * Splits data provided into smaller files
     * @param source The file to get data from
     * @param post The postfix to add to end of file e.g. post = 'z' would 
     * lead to files called "1z.txt"
     * @param noFiles The number files to split the data into
     */
    private static void splitData(String source, char post, int noFiles) throws IOException{
        List<String> data = getData(source);
        final int linesPerFile = (int)Math.ceil((double)data.size() / (double) noFiles);
        System.out.println("lines per " + linesPerFile);
        System.out.println("data len " + data.size());
        final int noCharsOnLine = (PhilipMCS5SoftwareDevelopmentNeuralNetworkTrain.WIDTH_OF_DATA * 2);
        for(int file = 0; file < noFiles; file++){
            StringBuilder fileCont = new StringBuilder((noCharsOnLine * linesPerFile) - 1);
            for(int currentIndex = file * linesPerFile; 
                    currentIndex < data.size() && currentIndex < (file + 1) * linesPerFile;
                    currentIndex++){
                if(currentIndex != file * linesPerFile) fileCont.append('\n');
                fileCont.append(data.get(currentIndex));
                if (currentIndex % 100 == 0) System.out.println("in " + currentIndex);
            }
            FileWriter w = new FileWriter(String.valueOf(file) + post + ".txt", false);
            BufferedWriter buffW = new BufferedWriter(w);
            buffW.write(fileCont.toString());
            buffW.flush();w.flush();
            buffW.close(); w.close();
        }
    }
}
