/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package philipm.cs5.software.development.user.system;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * This class enables the user to make use of stockfish when playing offline. (Stockfish is a powerful chess engine)
 * @author mortimer
 */
public class StockFish {
    private BufferedReader input;//used to input to the engine
    private BufferedWriter output;//used to read the output of the engine
    private Process process;//stores the process (i.e. the execution of the stockfish engine)
    private static final String STOCK_FISH_LOCATION="stockfish_20090216_x64.exe";//stores the name of the stockfish location
    /**
     * Creates a new stock fish object that can run Stockfish 12
     * @throws java.io.IOException An error may occur when loading Stockfish
     */
    public StockFish()throws IOException{
        try{
            process=Runtime.getRuntime().exec(STOCK_FISH_LOCATION);//creates the execution process
            //gets the input and output streams
            input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            output = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
        }catch(IOException e){
            throw e;//throws any erros that may occur
        }
    }
    /**
     * Gets the Stockfish 12's move from the given board state.
     * @param currentFen The FEN of the current board state
     * @return The board state after the computer has made its move
     * @throws IOException An exception may occur when attempting to execute the Stockfish file.
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
     * @throws java.io.IOException An exception may occur as Stockfish is an executable file.
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
     * @throws IOException An exception may occur as Stockfish is an executable file.
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
     * @throws IOException An exception may occur when attempting to exit Stockfish.
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
