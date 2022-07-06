/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package philipm.cs5.software.development.neural.network.train;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * This class enables the user to make use of stockfish.
 * @author mortimer
 */
public class StockFish {
    private BufferedReader input;//used to input to the engine
    private BufferedWriter output;//used to read the output of the engine
    private Process process;//stores the process (i.e. the execution of the stockfish engine)
    private static final String STOCK_FISH_LOCATION="stockfish_20090216_x64.exe";//stores the name of the stockfish location
    /**
     * Creates a new stock fish object that can run Stockfish 12
     * @throws java.io.IOException An exception may occur when attempting to access Stockfish
     */
    public StockFish()throws IOException{
        try{
            process=Runtime.getRuntime().exec(STOCK_FISH_LOCATION);
            input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            output = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
        }catch(IOException e){
            throw e;//throws any erros that may occur
        }
    }
    /**
     * Gets the value of a given fen board state
     * @param currentFen The board state
     * @param isBlackTurn Is it black's turn to play
     * @return The value in centipawns
     * @throws IOException An exception when using stockfish
     */
    public int getCurrentBoardValue(String currentFen,boolean isBlackTurn)throws IOException, Exception{
        try{
            //sets board state to appropriate FEN
            executeTask("position fen "+currentFen);
            //best move is calculated
            executeTask("go movetime 50");//this gets the best move with a search time of 1000 milliseconds
            String prevLine="";
            String lineRead;
            while((lineRead=input.readLine())!=null){
                if(lineRead.contains("score cp ")){
                    prevLine=lineRead;
                }else if(lineRead.contains("bestmove")){
                    break;
                }
            }
            if(prevLine.equals("")){
                throw new Exception("score format exception");
            }
            prevLine=prevLine.split("score cp ")[1];
            String val="";
            for(int c=0;c<prevLine.length();c++){
                if(Character.isDigit(prevLine.charAt(c)) || prevLine.charAt(c)=='-'){
                    val=val+String.valueOf(prevLine.charAt(c));
                }else{
                    break;
                }
            }
            int multiplier=1;
            if(isBlackTurn){
                multiplier=-1;
            }
            return multiplier*Integer.parseInt(val);
        }catch(IOException e){
            throw e;
        }        
    }
    /**
     * Gets the Stockfish 12's move from the given board state.
     * @param currentFen The FEN of the current board state
     * @return The board state after the computer has made its move
     * @throws IOException An exception may occur when attempting to access Stockfish
     */
    public String getStockFishMove(String currentFen) throws IOException{
        try{
            //sets board state to appropriate FEN
            executeTask("position fen "+currentFen);
            //best move is calculated
            executeTask("go movetime 1000");//this gets the best move with a search time of 1000 milliseconds
            String bestMove= readOutput("bestmove ").split(" ")[1];//gets the computer move
            executeTask("position fen "+currentFen+" moves "+bestMove);//updates the board
            executeTask("d");//gets the computer to draw the current board and output the corresponding FEN
            String lineWithFen=readOutput("Fen: ");//gets the line containing the current Fen String
            String fen="";//stores the Fen to return
            for(int c=5;c<lineWithFen.length();c++){//loops through every character in the FEN and appends it to the String storing the FEN board
                fen = fen+String.valueOf(lineWithFen.charAt(c));
            }
            return fen;
        }catch(IOException e){
            throw e;
        }
    }
    /**
     * Executes the desired instruction
     * @param task The UCI command to execute
     * @throws java.io.IOException An exception may occur when using Stockfish
     */
    private void executeTask(String task) throws IOException{
        try{
            output.write(task+System.lineSeparator());
            output.flush();//writes the command and flushes the output
        }catch(IOException e){
            throw e;
        }
    }
    /**
     * Loops through all new lines and checks to see if they begin with the expectedValue. If they do, the line is returned
     * @param expectedValue The expected start of the line
     * @return The line read
     * @throws IOException An IOException may occur when using Stockfish
     */
    private String readOutput(String expectedValue)throws IOException{
        try{
            String lineRead;//stores the line read
            while((lineRead=input.readLine())!=null){
                if(lineRead.startsWith(expectedValue)){
                    return lineRead;
                }
            }
        }catch(IOException e){
            throw e;
        }
        return null;
    }
    /**
     * Exits the application and closes the Stockfish application. Once this process is called, no further calls can be made to the current StockFish instance
     * @throws IOException An exception may occur when attempting to access Stockfish
     */
    public void exit()throws IOException{
        try{
            executeTask("quit");//attempts to quit the program
        }finally{
            process.destroy();//closes the task
            input.close();
            output.close();
        }
    }
}
