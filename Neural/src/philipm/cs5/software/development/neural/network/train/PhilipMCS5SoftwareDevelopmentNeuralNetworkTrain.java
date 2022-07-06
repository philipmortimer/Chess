/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package philipm.cs5.software.development.neural.network.train;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * This is the main class used to train my chess neural network.
 * @author mortimer
 */
public class PhilipMCS5SoftwareDevelopmentNeuralNetworkTrain {
    private static final int TOTAL_AMOUNT_OF_DATA=2326888;//stores total data items
    public static final int NO_OF_TEST_BOARDS=80000;//stores the number of boards used in test data    
    public static final int NO_OF_TRAINING_BOARDS=2246888/*TOTAL_AMOUNT_OF_DATA-NO_OF_TEST_BOARDS*/;//stores number of boards used in training data
    public static final int WIDTH_OF_DATA=779;//stores the width of each data item
    public static final String TEST_FILE_NAME="testData.txt";//stores name of test data text file
    public static final String TRAIN_FILE_NAME="trainData.txt";//stores name of training data file
    public static final String SPLIT=",";//stores character used to split items in file
    private static final String LOSE_FILE_NAME="losestates.txt";//stores file name for losing states
    private static final String WIN_FILE_NAME="winstates.txt";//stores file name for winning states
    private static final String DRAW_FILE_NAME="drawstates.txt";//stores file name for drawing states
    private static final int NO_OF_DRAWS=485843*4;//stores number of draws in dataset
    private static final int NO_OF_WINS=414929*4;//stores number of wins in dataset
    private static final int NO_OF_LOSSES=305861*4;//stores number of losses in dataset
    private static final int NO_OF_WINS_WHITE=208767;
    private static final int NO_OF_WINS_BLACK=206162;
    private static final int NO_OF_LOSE_BLACK=152503;
    private static final int NO_OF_LOSE_WHITE=153358;
    
    /**
     * The main method used to train the neural network
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //DataLimitPreProcess.convertToSmallerFiles();
        DataLimitPreProcess.reconstructFiles(); //Used to get round GitHub file size restrictions
        int[][] train = getTrainingData();
        System.out.println("here"); System.exit(0);
        NeuralNetwork net = new NeuralNetwork(new int[]{777,300,200,100,2});//creates a neural network with this structure
        double a []=new double[777];
        double timeBefore=System.currentTimeMillis();
        for(int i=0;i<200000;i++){
            net.feedThroughNet(a);
        }
        System.out.println("Time to do 200,000: "+(System.currentTimeMillis()-timeBefore)/1000.0);
        System.out.println("new system");
        double learningRate=0.001;int noOfEpochs=200000;int batchSize=128;
        int totalSizeOfTrainingData=NO_OF_TRAINING_BOARDS;int printAfterNoIterations =1000;double dropoutRate=0.0;
        net.trainNew(learningRate, noOfEpochs, batchSize,  totalSizeOfTrainingData,printAfterNoIterations,dropoutRate);//trains network
        net.writeBestNetToBestFile(); //writes best network to a text file - this is used for the ChessNeuralNetwork Class
        try{
            //tests the accuracy of the chess neural network
            ChessNeuralNetwork c= new ChessNeuralNetwork(new int[]{777,300,200,100,2});
            double[]aT=c.testAccuracy(getTestData());
            System.out.println(aT[0]+" "+aT[1]);
            net.setAllWeightsAndBiases(net.loadBestNetFile());//sets the neural network to be the network with the best accuracy
        }catch(Exception e){
            System.out.println(e);
        }
        //tests the accuracy of the trained neural network
        double[] aT=net.testAccuracy(getTestData());
        System.out.println(aT[0]+" "+aT[1]);
    }
    /**
     * This method is used to generate a training and test data set using stockfish (the world's strongest chess engine) as the ground truth
     * evaluation of a board state. Essentially, the code loops through all board states stored and stores the stockfish evaluation along with
     * a computer friendly chess board representation. This code was not used as the code would take too long to run (as stockfish needs a minimum amount
     * of time to evaluate each board state with any accuracy). This method would prove effective given more computer resources.
     */
    private static void formatDataForStockfish(){
        try{
            StockFish stock = new StockFish();
            ChessBoardState state;
            int arr[];
            String dat;
            String fens[];
            ArrayList <String> data = new ArrayList <String>(100000);//stores the data to be written to the text files
            FileReader read = new FileReader(WIN_FILE_NAME);
            BufferedReader buffRead =  new BufferedReader(read);            
            fens=buffRead.lines().toArray(String[]::new);
            buffRead.close();read.close();
            for(int i=0;i<fens.length;i++){
                //gets the value and computer version of each board state -this process is repeated for winning, drawing and losing board states
                state = new  ChessBoardState(fens[i]);
                arr=state.getNeuralNetInput();
                try{
                    dat = String.valueOf(stock.getCurrentBoardValue(fens[i],state.IS_BLACK_TURN_TO_PLAY_NEXT));
                }catch(Exception de){
                    continue;
                }
                for(int j=0;j<arr.length;j++){
                    dat=dat+","+String.valueOf(arr[j]);
                }
                if(i%1000==0&& i>1){
                    
                    System.out.println("i "+i);
                }
                data.add(dat);
            }   
            FileReader read2 = new FileReader(DRAW_FILE_NAME);
            BufferedReader buffRead2 =  new BufferedReader(read2);            
            fens=buffRead2.lines().toArray(String[]::new);
            buffRead2.close();read2.close();
            for(int i=0;i<fens.length;i++){
                state = new  ChessBoardState(fens[i]);
                arr=state.getNeuralNetInput();
                try{
                    dat = String.valueOf(stock.getCurrentBoardValue(fens[i],state.IS_BLACK_TURN_TO_PLAY_NEXT));
                }catch(Exception de){
                    continue;
                }
                for(int j=0;j<arr.length;j++){
                    dat=dat+","+String.valueOf(arr[j]);
                }
                if(i%1000==0&&i>1){
                    
                    System.out.println("i "+i);
                }
                data.add(dat);
            }
            FileReader read3 = new FileReader(LOSE_FILE_NAME);
            BufferedReader buffRead3 =  new BufferedReader(read3);            
            fens=buffRead3.lines().toArray(String[]::new);
            buffRead3.close();read3.close();
            for(int i=0;i<fens.length;i++){
                state = new  ChessBoardState(fens[i]);
                arr=state.getNeuralNetInput();
                try{
                    dat = String.valueOf(stock.getCurrentBoardValue(fens[i],state.IS_BLACK_TURN_TO_PLAY_NEXT));
                }catch(Exception de){
                    continue;
                }
                for(int j=0;j<arr.length;j++){
                    dat=dat+","+String.valueOf(arr[j]);
                }
                if(i%1000==0&&i>1){
                   
                    System.out.println("i "+i);
                }
                data.add(dat);
            } 
            System.out.println("Size of data "+data.size());
            stock.exit();
            Collections.shuffle(data);//randomises data
            //writes data to file
            int maxTrainIndex=(data.size()/10)*9;
            FileWriter write = new FileWriter(TRAIN_FILE_NAME,false);
            BufferedWriter buffWrite = new BufferedWriter(write);
            for(int i=0;i<maxTrainIndex;i++){
                if(i!=0){
                    buffWrite.newLine();
                }
                buffWrite.write(data.get(i));
            }buffWrite.flush();write.flush();
            buffWrite.close();write.close();
            FileWriter w = new FileWriter(TEST_FILE_NAME,false);
            BufferedWriter buffW = new BufferedWriter(w);
            for(int i= maxTrainIndex;i<data.size();i++){
                if(i!=maxTrainIndex){
                    buffW.newLine();
                }
                buffW.write(data.get(i));
            }
            buffW.flush();w.flush();
            buffW.close();w.close();
        }catch(IOException e){
            System.out.println("Error "+e);
        }
    }
    /**
     * Gets neural network training data.
     * @return The training data
     */
    public static int[][]getTrainingData(){
        //reads the training data from the training data text file and returns it as an array
        int w=0;int l=0;
        int data[][]=new int[NO_OF_TRAINING_BOARDS][WIDTH_OF_DATA];
        try{
            int i=0;
            String lineRead;String[]line;
            FileReader read = new FileReader(TRAIN_FILE_NAME);
            BufferedReader buffRead =new BufferedReader(read);
            while((lineRead=buffRead.readLine())!=null && i<NO_OF_TRAINING_BOARDS){
                line=lineRead.split(SPLIT);
                for(int ch=0;ch<WIDTH_OF_DATA;ch++){
                    data[i][ch]=Integer.parseInt(line[ch]);
                }
                if(data[i][0]==1){
                    w++;
                }else{
                    l++;
                }
                i++;
            }buffRead.close();
            read.close();
            System.out.println("W: "+w+" l: "+l);
        }catch(IOException e){
            System.out.println("Error "+e);
        }
        return data;
    }
    /**
     * Gets the test data from the test file.
     * @return The test data
     */
    public static int[][]getTestData(){
        //reads the test data from the test file and writes it to an array
        int data[][]=new int[NO_OF_TEST_BOARDS][WIDTH_OF_DATA];
        int w=0;int l=0;
        try{
            int i=0;
            String lineRead;String[]line;
            FileReader read = new FileReader(TEST_FILE_NAME);
            BufferedReader buffRead =new BufferedReader(read);
            while((lineRead=buffRead.readLine())!=null&& i<NO_OF_TEST_BOARDS){
                line=lineRead.split(SPLIT);
                for(int ch=0;ch<WIDTH_OF_DATA;ch++){
                    data[i][ch]=Integer.parseInt(line[ch]);
                }
                if(data[i][0]==1){
                    w++;
                }else{
                    l++;
                }
                i++;
            }buffRead.close();
            read.close();
            System.out.println("Win: "+w+" lose: "+l);
        }catch(IOException e){
            System.out.println("Error "+e);
        }
        return data;        
    }
    /**
     * Formats data for a neural network comparing the value of boards. The idea behind this was that the network would compare 
     * two board states and determine which one was better. This process was not used as it proved too inefficient computationally.
     */
    private static void formatDataCompare(){
        try{
            String[]data = new String[NO_OF_LOSSES];
            int index=0;
            int[]line;
            //loads win data
            FileReader read = new FileReader(WIN_FILE_NAME);
            BufferedReader buffRead = new BufferedReader(read);
            String lineRead;
            ChessBoardState state;
            while((lineRead=buffRead.readLine())!=null){
                state =new ChessBoardState(lineRead);
                line=state.getNeuralNetInput();
                lineRead=String.valueOf(line[0]);
                for(int j=1;j<line.length;j++){
                    lineRead=lineRead+SPLIT+String.valueOf(line[j]);
                }
                if(index==NO_OF_LOSSES){//exits loop once all data is obtained
                    break;
                }
                data[index]=lineRead;
                index++;
                if(index%50000==0){
                    System.out.println(index);
                }
            }buffRead.close();read.close();
            System.out.println("Done with win file");
            //loads loss data
            index=0;
            FileReader readLose = new FileReader(LOSE_FILE_NAME);
            BufferedReader buffReadLose = new BufferedReader(readLose);
            while((lineRead=buffReadLose.readLine())!=null){
                state =new ChessBoardState(lineRead);
                line = state.getNeuralNetInput();
                lineRead= String.valueOf(line[0]);
                for(int j=1;j<line.length;j++){
                    lineRead=lineRead+SPLIT+line[j];
                }
                if(Math.random()>=0.5){//creates random win,loss pairings
                    data[index]="1"+SPLIT+"0"+SPLIT+data[index]+SPLIT+lineRead;
                }else{
                    data[index]="0"+SPLIT+"1"+SPLIT+lineRead+SPLIT+data[index];
                }
                index++;
                if(index%50000==0){
                    System.out.println(index);
                }
            }
            buffReadLose.close();readLose.close();
            System.out.println("Done with lose file");
            //writes data to file
            shuffleArray(data);
            System.out.println("Shuffled");
            FileWriter write = new FileWriter(TRAIN_FILE_NAME,false);
            BufferedWriter buffWrite = new BufferedWriter(write);
            buffWrite.write(data[0]);
            for(int i=1;i<NO_OF_TRAINING_BOARDS;i++){
                buffWrite.newLine();
                buffWrite.write(data[i]);
            }buffWrite.flush();write.flush();
            buffWrite.close();write.close();
            FileWriter w = new FileWriter(TEST_FILE_NAME,false);
            BufferedWriter buff = new BufferedWriter(w);
            buff.write(data[NO_OF_TRAINING_BOARDS]);
            for(int i=1;i<NO_OF_TEST_BOARDS;i++){
                buff.newLine();
                buff.write(data[NO_OF_TRAINING_BOARDS+i]);
            }buff.flush();w.flush();
            buff.close();w.close();
            System.out.println("Data created");
        }catch(IOException e){
            System.out.println("Error "+e);
        }
    }
    /**
     * Raw fen strings from each file are converted into data in a format that can be input to the neural network. This method was used
     * and this method labels board states in a game of chess that was eventually won as positive and vice versa. This approach produced a high accuracy
     * although it was limited by the hardware used to train it. It was also limited by the fact that the dataset of boardstates is not as strong
     * as previously thought.
     */
    private static void formatData(){
        try{
            int[]line;//stores the input to the neural network as a double array
            int i=0;//stores the current record number
            String data[]= new String[TOTAL_AMOUNT_OF_DATA];//stores the data as an array
            ChessBoardState state;//stores the chessboard state
            String lineRead;//stores the lineRead            
            System.out.println("Array created");
            FileReader loseRead = new FileReader(LOSE_FILE_NAME);
            BufferedReader buffReadLose=new BufferedReader(loseRead);
            while((lineRead=buffReadLose.readLine())!=null){
                state = new ChessBoardState(lineRead);//creates a new chess board using the FEN string
                line=state.getNeuralNetInput();
                lineRead="0"+SPLIT+"1";//used to indicate the desired network output
                for(int element=0;element<WIDTH_OF_DATA-2;element++){
                    lineRead=lineRead+SPLIT+String.valueOf(line[element]);
                }
                data[i]=lineRead;
                i++;
                if(i%50000==0){
                    System.out.println(i);
                }                
            }
            buffReadLose.close();loseRead.close();
            System.out.println("done with lose");            
            FileReader winRead = new FileReader(WIN_FILE_NAME);
            BufferedReader buffReadWin = new BufferedReader(winRead);
            while((lineRead=buffReadWin.readLine())!=null){
                state = new ChessBoardState(lineRead);//creates a new chess board using the FEN string
                line=state.getNeuralNetInput();
                lineRead="1"+SPLIT+"0";//used to indicate the desired network output
                for(int element=0;element<WIDTH_OF_DATA-2;element++){
                    lineRead=lineRead+SPLIT+String.valueOf(line[element]);
                }
                data[i]=lineRead;
                i++;
                if(i==TOTAL_AMOUNT_OF_DATA){
                    break;
                }
                if(i%50000==0){
                    System.out.println(i);
                }
            }
            System.out.println("Done with win");
            buffReadWin.close();winRead.close();
          /*  FileReader drawRead = new FileReader(DRAW_FILE_NAME);
            BufferedReader buffReadDraw=new BufferedReader(drawRead);
            while((lineRead=buffReadDraw.readLine())!=null){
                state = new ChessBoardState(lineRead);//creates a new chess board using the FEN string
                line=state.getNeuralNetInput();
                lineRead="0"+SPLIT+"1"+SPLIT+"0";//used to indicate the desired network output
                for(int element=0;element<WIDTH_OF_DATA-3;element++){
                    lineRead=lineRead+SPLIT+String.valueOf(line[element]);
                }
                data[i]=lineRead;
                i++;
                if(i%50000==0){
                    System.out.println(i);
                }
            }
            buffReadDraw.close();drawRead.close();
            System.out.println("Done with draw");*/
            //writes data to file
            shuffleArray(data);//shuffles array randomly
            //writes training and test data to file
            FileWriter writeTrain = new FileWriter(TRAIN_FILE_NAME,false);
            BufferedWriter buffWriteTrain = new BufferedWriter(writeTrain);
            buffWriteTrain.write(data[0]);//writes the first line
            for(i=1;i<NO_OF_TRAINING_BOARDS;i++){
                buffWriteTrain.newLine();
                buffWriteTrain.write(data[i]);
            }
            buffWriteTrain.flush();writeTrain.flush();
            buffWriteTrain.close();writeTrain.close();
            //writes test data to file
            FileWriter writeTest = new FileWriter(TEST_FILE_NAME,false);
            BufferedWriter buffWriteTest = new BufferedWriter(writeTest);
            buffWriteTest.write(data[NO_OF_TRAINING_BOARDS]);
            for(i=NO_OF_TRAINING_BOARDS+1;i<NO_OF_TEST_BOARDS+NO_OF_TRAINING_BOARDS;i++){
                buffWriteTest.newLine();
                buffWriteTest.write(data[i]);
            }
            buffWriteTest.flush();writeTest.flush();
            buffWriteTest.close();writeTest.close();
        }catch(IOException e){
            System.out.println("Error "+e);
        }
        System.out.println("Done with formatting");
    }
    /**
     * Randomly shuffles inputted array
     * @param arr The array to shuffle
     */
    private static void shuffleArray(String[]arr){
        Random rnd = new Random();
        String buff;int el;
        for(int i=0;i<arr.length;i++){
            el=rnd.nextInt(arr.length);
            buff=arr[el];
            arr[el]=arr[i];
            arr[i]=buff;
        }
    }
}
