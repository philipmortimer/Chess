/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package philipm.cs5.software.development.user.system;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javax.swing.JOptionPane;

/**
 * This class stores the chess game (i.e. all board states). It also handles getting any potential computer moves.
 * @author mortimer
 */
public class ChessGame {
    private ChessBoardState [] states;//stores all board states so far
    private static final int BLACK_MULT=-1;final int WHITE_MULT=1;
    //these values are used for level 2 evalaution
    private static final int VALUE_OF_PAWN=1;//stores the relative value of the pawn
    private static final int VALUE_OF_ROOK=5;//stores relative value of rook
    private static final int VALUE_OF_BISHOP=3;//stores relative value of bishop
    private static final int VALUE_OF_KNIGHT=3;//stores relative value of knight
    private static final int VALUE_OF_QUEEN=9;//stores relative value of queen
    private static final int VALUE_OF_KING=600000;//stores the value of the king - which is an arbitarily high value
    private static final int DRAW_VALUE=0;//stores the value assigned to a draw
    private static final int WHITE_WIN_VALUE=(Integer.MAX_VALUE-1)-1;
    private static final int BLACK_WIN_VALUE=(Integer.MAX_VALUE*-1)+1;
    //stores the relative positional value for pieces
    private static final int[][]PAWN_POSITIONAL_WHITE={{0,0,0,0,0,0,0,0},{50,50,50,50,50,50,50,50},{10,10,20,30,30,20,10,10},{5,5,10,25,25,10,5,5},{0,0,0,20,20,0,0,0},{5,-5,-10,0,0,-10,-5,5},{5,10,10,-20,-20,10,10,5},{0,0,0,0,0,0,0,0}};
    private static final int[][]PAWN_POSITIONAL_BLACK={{0,0,0,0,0,0,0,0},{-5,-10,-10,20,20,-10,-10,-5},{-5,5,10,0,0,10,5,-5},{0,0,0,-20,-20,0,0,0},{-5,-5,-10,-25,-25,-10,-5,-5},{-10,-10,-20,-30,-30,-20,-10,-10},{-50,-50,-50,-50,-50,-50,-50,-50},{0,0,0,0,0,0,0,0}};
    private static final int[][]KNIGHT_POSITIONAL_WHITE={{-50,-40,-30,-30,-30,-30,-40,-50},{-40,-20,0,0,0,0,-20,-40},{-30,0,10,15,15,10,0,-30},{-30,5,15,20,20,15,5,-30},{-30,0,15,20,20,15,0,-30},{-30,5,10,15,15,10,5,-30},{-40,-20,0,5,5,0,-20,-40},{-50,-40,-30,-30,-30,-30,-40,-50}};
    private static final int[][]KNIGHT_POSITIONAL_BLACK={{50,40,30,30,30,30,40,50},{40,20,0,-5,-5,0,20,40},{30,-5,-10,-15,-15,-10,-5,30},{30,0,-15,-20,-20,-15,0,30},{30,-5,-15,-20,-20,-15,-5,30},{30,0,-10,-15,-15,-10,0,30},{40,20,0,0,0,0,20,40},{50,40,30,30,30,30,40,50}};
    private static final int[][]QUEEN_POSITIONAL_WHITE={{-20,-10,-10,-5,-5,-10,-10,-20},{-10,0,0,0,0,0,0,-10},{-10,0,5,5,5,5,0,-10},{-5,0,5,5,5,5,0,-5},{0,0,5,5,5,5,0,-5},{-10,5,5,5,5,5,0,-10},{-10,0,5,0,0,0,0,-10},{-20,-10,-10,-5,-5,-10,-10,-20}};
    private static final int[][]QUEEN_POSITIONAL_BLACK={{20,10,10,5,5,10,10,20},{10,0,0,0,0,-5,0,10},{10,0,-5,-5,-5,-5,-5,10},{5,0,-5,-5,-5,-5,0,0},{5,0,-5,-5,-5,-5,0,5},{10,0,-5,-5,-5,-5,0,10},{10,0,0,0,0,0,0,10},{20,10,10,5,5,10,10,20}};
    private static final int[][]BISHOP_POSITIONAL_WHITE={{-20,-10,-10,-10,-10,-10,-10,-20},{-10,0,0,0,0,0,0,-10},{-10,0,5,10,10,5,0,-10},{-10,5,5,10,10,5,5,-10},{-10,0,10,10,10,10,0,-10},{-10,10,10,10,10,10,10,-10},{-10,5,0,0,0,0,5,-10},{-20,-10,-10,-10,-10,-10,-10,-20}};
    private static final int[][]BISHOP_POSITIONAL_BLACK={{20,10,10,10,10,10,10,20},{10,-5,0,0,0,0,-5,10},{10,-10,-10,-10,-10,-10,-10,10},{10,0,-10,-10,-10,-10,0,10},{10,-5,-5,-10,-10,-5,-5,10},{10,0,-5,-10,-10,-5,0,10},{10,0,0,0,0,0,0,10},{20,10,10,10,10,10,10,20}};
    private static final int[][]ROOK_POSITIONAL_WHITE={{0,0,0,0,0,0,0,0},{5,10,10,10,10,10,10,5},{-5,0,0,0,0,0,0,-5},{-5,0,0,0,0,0,0,-5},{-5,0,0,0,0,0,0,-5},{-5,0,0,0,0,0,0,-5},{-5,0,0,0,0,0,0,-5},{0,0,0,5,5,0,0,0}};
    private static final int[][]ROOK_POSITIONAL_BLACK={{0,0,0,-5,-5,0,0,0},{5,0,0,0,0,0,0,5},{5,0,0,0,0,0,0,5},{5,0,0,0,0,0,0,5},{5,0,0,0,0,0,0,5},{5,0,0,0,0,0,0,5},{-5,-10,-10,-10,-10,-10,-10,-5},{0,0,0,0,0,0,0,0}};
    private static final int[][]KING_POSITIONAL_WHITE_MID={{-30,-40,-40,-50,-50,-40,-40,-30},{-30,-40,-40,-50,-50,-40,-40,-30},{-30,-40,-40,-50,-50,-40,-40,-30},{-30,-40,-40,-50,-50,-40,-40,-30},{-20,-30,-30,-40,-40,-30,-30,-20},{-10,-20,-20,-20,-20,-20,-20,-10},{20,20,0,0,0,0,20,20},{20,30,10,0,0,10,30,20}};
    private static final int[][]KING_POSITIONAL_BLACK_MID={{-20,-30,-10,0,0,-10,-30,-20},{-20,-20,0,0,0,0,-20,-20},{10,20,20,20,20,20,20,10},{20,30,30,40,40,30,30,20},{30,40,40,50,50,40,40,30},{30,40,40,50,50,40,40,30},{30,40,40,50,50,40,40,30},{30,40,40,50,50,40,40,30}};
    private static final int[][]KING_POSITIONAL_WHITE_END={{-50,-40,-30,-20,-20,-30,-40,-50},{-30,-20,-10,0,0,-10,-20,-30},{-30,-10,20,30,30,20,-10,-30},{-30,-10,30,40,40,30,-10,-30},{-30,-10,30,40,40,30,-10,-30},{-30,-10,20,30,30,20,-10,-30},{-30,-30,0,0,0,0,-30,-30},{-50,-30,-30,-30,-30,-30,-30,-50}};
    private static final int[][]KING_POSITIONAL_BLACK_END={{50,30,30,30,30,30,30,50},{30,30,0,0,0,0,30,30},{30,10,-20,-30,-30,-20,10,30},{30,10,-30,-40,-40,-30,10,30},{30,10,-30,-40,-40,-30,10,30},{30,10,-20,-30,-30,-20,10,30},{30,20,10,0,0,10,20,30},{50,40,30,20,20,30,40,50}};
    //stores the rough value of each of the chess pieces in centipawns
    private static final int PAWN=100;
    private static final int KNIGHT=320;
    private static final int BISHOP=330;
    private static final int ROOK =500;
    private static final int KING=20000;
    private static final int QUEEN=900;
    private ChessNeuralNetwork net;//stores a neural network trained to evalaute board positions
    private  final String[][]OPENING_BOOK;//stores the opening book - small number of moves made by chess grandmasters early in a game
    /**
     * Creates a chess game object storing all the states played so far
     * @param statesSoFar All chess board states that have already occurred
     * @param loadNeuralNet Whether a neural network should be loaded when initialising the chess game
     */
    public ChessGame(ChessBoardState[]statesSoFar,boolean loadNeuralNet){
        OPENING_BOOK=getOpeningBook();//loads the opening book
        states =statesSoFar;//stores all board states so far in the game
        if(loadNeuralNet){
            //loads the neural network to evaluate the board states
            try{
                net= new ChessNeuralNetwork(new int[]{/*777,610,300,200,100,2*/777,300,200,100,2});
            }catch(IOException e){
                net=null;
                JOptionPane.showMessageDialog(null, "An unexpected error occured when loading the neural network. Setting the computer to level 4 insetad. Error details: "+e,"Error",JOptionPane.OK_OPTION);
            }
        }
        
    }
    /**
     * Reads and loads opening book
     * @return The opening book
     */
    private static String[][] getOpeningBook(){
        String arr[][]=new String[11318][2];//stores the data set of opening moves. It is stored in the form of boardState(t) -> boardState(t+1)
        try{
            FileReader read =new FileReader("openingMoves.txt");//reads all opening moves from the text file that stores them
            BufferedReader buffRead = new BufferedReader(read);
            int i=0;
            String lineRead;String line[];
            while((lineRead=buffRead.readLine())!=null){
                line=lineRead.split(",");
                arr[i][0]=line[0];arr[i][1]=line[1];
                i++;
            }
            buffRead.close();
            read.close();
        }catch(IOException e){
            System.out.println("error with opening book: "+e);
        }
        return arr;
    }
    /**
     * Gets the computer move given the computer level as input.
     * @param computerLevel An integer between 1 and 5 (both inclusive).
     * @return The board state representing the move that has been executed
     */
    public  ChessBoardState getComputerMove(int computerLevel){
        if(getCurrentState().FULL_MOVE_NUMBER<=10 && computerLevel!=5){//plays a move out of the opening book if possible
            String fen =getCurrentState().getFenString();//gets the fen string of the current board state
            String fenData[]=fen.split(" ");
            int i=Search.searchForRecordIndex(OPENING_BOOK, fen);//searches for the current board state
            if(i!=Search.INDEX_NOT_FOUND){
                int start=i;int end=i;
                //finds the lower and upper bound of it
                for(;start>0;start--){
                    if(OPENING_BOOK[start][0].equals(fen)==false){
                        start++;break;
                    }
                }
                for(;end<OPENING_BOOK.length;end++){
                    if(OPENING_BOOK[end][0].equals(fen)==false){
                        end--;
                        break;
                    }
                }
                //returns random board state from possible options
                Random rnd = new Random();
                if(getCurrentState().IS_BLACK_TURN_TO_PLAY_NEXT){
                    fen=" "+String.valueOf(getCurrentState().FULL_MOVE_NUMBER+1);
                }else{
                    fen=" "+String.valueOf(getCurrentState().FULL_MOVE_NUMBER);
                } 
                try{
                    Thread.sleep(500);//doesn't make the move instantly
                }catch(InterruptedException e){
                    
                }
                return new ChessBoardState(OPENING_BOOK[rnd.nextInt((end-start)+1)+start][1]+fen);//creates a new chess board state and returns it
            }
        }
        //gets the computer move depedning on the computer level selected
        switch (computerLevel) {
            case 1:
                //gets a random move and plays it
                ArrayList<ChessBoardState>possibleStates=getAllLegalBoardsFromState(this.getCurrentState());
                Random rnd = new Random();
                try{
                    Thread.sleep(500);//doesn't make the move instantly
                }catch(InterruptedException e){
                    
                }
                return (possibleStates.get(rnd.nextInt(possibleStates.size())));//returns the random move
            case 3:
                //implements a depth limited minimax algorithm looking at the value of pieces and summing them
                return getComputerMoveMinimax(getCurrentState(), computerLevel, 3);
            case 2:
                //uses minimax algorithm alongside a neural network I have trained
                if(net==null){// if a network error occurs then the move for the level four computer is retrieved instead
                    return getComputerMove(4);
                }
                return getComputerMoveMinimax(getCurrentState(), computerLevel, 3);
            case 4:
                //uses an advanced evalaution function
                return getComputerMoveMinimax(getCurrentState(), computerLevel, 3);
            default://computer level five
                //uses StockFish to get a move;
                try{
                    StockFish stock = new StockFish();//loads stockfish
                    ChessBoardState state =new ChessBoardState(stock.getStockFishMove(getCurrentState().getFenString()));//fetches the appropriate board state
                    stock.exit();//exits stock fish
                    return state;
                }catch(IOException e){
                    JOptionPane.showMessageDialog(null, "Failed to load stockfish (the level five computer). Hence, a move is being played by the level four computer. This could be because your system is not a windows 64 bit device.","Error",JOptionPane.OK_OPTION);
                }
                return getComputerMove(4);
        }
    }
    /**
     * Implements the minimax algorithm and returns the board state that is to be played.
     * @param currentState The starting board state
     * @param computerLevel The computer level. Levels from 2-5 are valid.
     * @param searchDepth The search depth for the minimax algorithm.
     * @return The chess board
     */
    private  ChessBoardState getComputerMoveMinimax(ChessBoardState currentState, int computerLevel,int searchDepth){
        ArrayList<ChessBoardState>allStates = getAllLegalBoardsFromState(currentState);//gets all legal board states from the current board
        int bestIndex=0;//stores the index of the state that is best
        int currentBest;
        int valOfMove;
        if(currentState.IS_BLACK_TURN_TO_PLAY_NEXT){
            currentBest=Integer.MAX_VALUE;
        }else{
            currentBest=Integer.MAX_VALUE*-1;
            Collections.reverse(allStates);
        }
        //loops through all board states and finds the board state which is best for the current player
        for(int i=0;i<allStates.size();i++){
            if(currentState.IS_BLACK_TURN_TO_PLAY_NEXT){//if it's black's turn, they are aiming to minimise the final value
                valOfMove=maxTurnMinimax(allStates.get(i), 1, -1*Integer.MAX_VALUE, Integer.MAX_VALUE, searchDepth, computerLevel);//gets value of board state
                if(valOfMove<=currentBest){
                    currentBest=valOfMove;
                    bestIndex=i;
                }
            }else{//white aims to maximise the board value
                valOfMove=minTurnMinimax(allStates.get(i), 1, -1*Integer.MAX_VALUE, Integer.MAX_VALUE, searchDepth, computerLevel);
                if(valOfMove>=currentBest){
                    currentBest=valOfMove;
                    bestIndex=i;
                }
            }
        }
        //returns the best move calculated
        return allStates.get(bestIndex);
    }
    /**
     * Implements a recursive depth limited alpha-beta pruned minimax algorithm to evaluate which move the computer must make next. This is called by the maximising player
     * @param state The board state to evaluate
     * @param depth The current search depth
     * @param alpha The value of alpha
     * @param beta The value of beta
     * @param maxSearchDepth The maximum search depth
     * @param computerLevel The computer level
     * @return The board value
     */
    private  int maxTurnMinimax(ChessBoardState state, int depth, int alpha, int beta, int maxSearchDepth, int computerLevel){
        ArrayList<ChessBoardState>possibleStates = getAllLegalBoardsFromState(state);//gets all possible board from all current board
        Collections.reverse(possibleStates);//reverses the board states as move ordering is employed to maximise the number of nodes pruned. As the moves are odered from lowest points to highest, the move order needs to be reversed
        if(possibleStates.isEmpty()){//if board is in a terminal state, it returns the appropraite value
            if(state.getIsDraw()){
                return DRAW_VALUE;
            }
            if(state.IS_BLACK_TURN_TO_PLAY_NEXT){
                return WHITE_WIN_VALUE;
            }else{
                return BLACK_WIN_VALUE;
            }
        }
        if(depth==maxSearchDepth){//if the maximum search depth has been reached, the appropriate evaluation is called
            return getBoardValue(state,computerLevel);
        }
        //if the search depth and terminal staten haven't been reached, the minimax process continues
        int max=-1*Integer.MAX_VALUE;//stores the max value
        int currentMoveValue;//stores the value of a single move
        for (ChessBoardState possibleState : possibleStates) {//loops through all possible board states
            currentMoveValue= minTurnMinimax(possibleState,depth+1,alpha,beta,maxSearchDepth,computerLevel);
            if(currentMoveValue>max){//sets max value
                max=currentMoveValue;
            }
            //implements alpha beta pruning
            if(currentMoveValue>=beta){
                return max;
            }
            if(currentMoveValue>alpha){
                alpha =currentMoveValue;
            }
        }
        //returns the value of the board state
        return max;
    }
    /**
     * Implements a recursive depth limited alpha-beta pruned minimax algorithm to evaluate which move the computer must make next. This is called by the minimising player
     * @param state The board state to evaluate
     * @param depth The current search depth
     * @param alpha The value of alpha
     * @param beta The value of beta
     * @param maxSearchDepth The maximum search depth
     * @param computerLevel The computer level
     * @return The board value
     */
    private  int minTurnMinimax(ChessBoardState state, int depth, int alpha, int beta, int maxSearchDepth, int computerLevel){
        ArrayList<ChessBoardState>possibleStates =getAllLegalBoardsFromState(state);//gets all possible board from all current board
        if(possibleStates.isEmpty()){//if board is in a terminal state, it returns the appropraite value
            if(state.getIsDraw()){
                return DRAW_VALUE;
            }
            if(state.IS_BLACK_TURN_TO_PLAY_NEXT){
                return WHITE_WIN_VALUE;
            }else{
                return BLACK_WIN_VALUE;
            }
        }
        if(depth==maxSearchDepth){//if the maximum search depth has been reached, the appropriate evaluation is called
            return getBoardValue(state, computerLevel);
        }
        int min=Integer.MAX_VALUE;//stores the minimum value
        int currentMoveValue;//stores the value of a single move
        for (ChessBoardState possibleState : possibleStates) {//loops through all possible board states
            currentMoveValue= maxTurnMinimax(possibleState,depth+1,alpha,beta,maxSearchDepth,computerLevel);
            if(currentMoveValue<min){//sets min value
                min=currentMoveValue;
            }
            //implements alpha beta pruning
            if(currentMoveValue<=alpha){
                return min;
            }
            if(currentMoveValue<beta){
                beta =currentMoveValue;
            }
        }
        //returns value of board state
        return min;       
    }
    /**
     * Gets the value of a non terminal board state using an appropriate heuristic
     * @param state The board state
     * @param computerLevel The computer level
     * @return The value of the board
     */
    private int getBoardValue(ChessBoardState state, int computerLevel){
        int boardValue=0;//stores the board value
        switch(computerLevel){
            case 3:
                //sums the value of the pieces on the board to calculate a board value
                for(int y=0;y<8;y++){
                    for(int x=0;x<8;x++){
                        switch (state.BOARD[y][x]) {
                            case ChessBoardState.PAWN_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                                boardValue=boardValue+(VALUE_OF_PAWN*BLACK_MULT);
                                break;
                            case ChessBoardState.KNIGHT_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                                boardValue=boardValue+(VALUE_OF_KNIGHT*BLACK_MULT);
                                break;
                            case ChessBoardState.BISHOP_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                                boardValue=boardValue+(VALUE_OF_BISHOP*BLACK_MULT);
                                break;
                            case ChessBoardState.QUEEN_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                                boardValue=boardValue+(VALUE_OF_QUEEN*BLACK_MULT);
                                break;
                            case ChessBoardState.ROOK_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                                boardValue=boardValue+(VALUE_OF_ROOK*BLACK_MULT);
                                break;
                            case ChessBoardState.KING_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                                boardValue=boardValue+(VALUE_OF_KING*BLACK_MULT);
                                break;
                            case ChessBoardState.PAWN_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                                boardValue=boardValue+(VALUE_OF_PAWN*WHITE_MULT);
                                break;
                            case ChessBoardState.KNIGHT_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                                boardValue=boardValue+(VALUE_OF_KNIGHT*WHITE_MULT);
                                break;
                            case ChessBoardState.BISHOP_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                                boardValue=boardValue+(WHITE_MULT*VALUE_OF_BISHOP);
                                break;
                            case ChessBoardState.QUEEN_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                                boardValue=boardValue+(WHITE_MULT*VALUE_OF_QUEEN);
                                break;
                            case ChessBoardState.ROOK_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                                boardValue = boardValue+(WHITE_MULT*VALUE_OF_ROOK);
                                break;
                            case ChessBoardState.KING_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                                boardValue=boardValue+(WHITE_MULT*VALUE_OF_KING);
                                break;
                            default:
                                break;
                        }
                    }
                }
                break;
            case 5:
                //this evaluation is used as sometimes users may not be able to use stockfish.
                //stores various variables to determine whether board is in endgame or not
                //evaluation is based heavily on https://www.chessprogramming.org/Simplified_Evaluation_Function
                int []coordBlackKing=new int[2];int[]coordWhiteKing=new int[2];
                int noOfBlackMinorPieces=0;int noOfWhiteMinorPieces=0;
                int noOfPiecesBlack=0;int noOfPiecesWhite=0;
                boolean blackHasQueen=false;boolean whiteHasQueen=false;
                for(int y=0;y<8;y++){
                    for(int x=0;x<8;x++){
                        switch (state.BOARD[y][x]) {
                            case ChessBoardState.PAWN_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                                boardValue=boardValue+(PAWN*BLACK_MULT)+PAWN_POSITIONAL_BLACK[y][x];
                                noOfPiecesBlack++;
                                break;
                            case ChessBoardState.KNIGHT_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                                boardValue=boardValue+(KNIGHT*BLACK_MULT)+KNIGHT_POSITIONAL_BLACK[y][x];
                                noOfBlackMinorPieces++;
                                break;
                            case ChessBoardState.BISHOP_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                                noOfBlackMinorPieces++;
                                boardValue=boardValue+(BISHOP*BLACK_MULT)+BISHOP_POSITIONAL_BLACK[y][x];
                                break;
                            case ChessBoardState.QUEEN_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                                blackHasQueen=true;
                                boardValue=boardValue+(QUEEN*BLACK_MULT)+QUEEN_POSITIONAL_BLACK[y][x];
                                break;
                            case ChessBoardState.ROOK_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                                noOfPiecesBlack++;
                                boardValue=boardValue+(ROOK*BLACK_MULT)+ROOK_POSITIONAL_BLACK[y][x];
                                break;
                            case ChessBoardState.KING_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                                boardValue=boardValue+(KING*BLACK_MULT);
                                coordBlackKing[0]=y;coordBlackKing[1]=x;
                                break;
                            case ChessBoardState.PAWN_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                                noOfPiecesWhite++;
                                boardValue=boardValue+(PAWN*WHITE_MULT)+PAWN_POSITIONAL_WHITE[y][x];
                                break;
                            case ChessBoardState.KNIGHT_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                                noOfWhiteMinorPieces++;
                                boardValue=boardValue+(KNIGHT*WHITE_MULT)+KNIGHT_POSITIONAL_WHITE[y][x];
                                break;
                            case ChessBoardState.BISHOP_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                                noOfWhiteMinorPieces++;
                                boardValue=boardValue+(WHITE_MULT*BISHOP)+BISHOP_POSITIONAL_WHITE[y][x];
                                break;
                            case ChessBoardState.QUEEN_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                                whiteHasQueen=true;
                                boardValue=boardValue+(WHITE_MULT*QUEEN)+QUEEN_POSITIONAL_WHITE[y][x];
                                break;
                            case ChessBoardState.ROOK_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                                noOfPiecesWhite++;
                                boardValue = boardValue+(WHITE_MULT*ROOK)+ROOK_POSITIONAL_WHITE[y][x];
                                break;
                            case ChessBoardState.KING_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                                boardValue=boardValue+(WHITE_MULT*KING);
                                coordWhiteKing[0]=y;coordWhiteKing[1]=x;
                                break;
                            default:
                                break;
                        }
                    }
                }
                if(((noOfPiecesBlack==0&&noOfBlackMinorPieces<=1)||!blackHasQueen)&&((noOfPiecesWhite==0&&noOfWhiteMinorPieces<=1)||!whiteHasQueen)){//this statement is true if the game is in an endgame state
                    boardValue=boardValue+KING_POSITIONAL_BLACK_END[coordBlackKing[0]][coordBlackKing[1]]+KING_POSITIONAL_WHITE_END[coordWhiteKing[0]][coordWhiteKing[1]];
                }else{
                    boardValue = boardValue+KING_POSITIONAL_BLACK_MID[coordBlackKing[0]][coordBlackKing[1]]+KING_POSITIONAL_WHITE_MID[coordWhiteKing[0]][coordWhiteKing[1]];
                }
                break;
            case 4:
                //calculates the board value by summing the value of the pieces and their relative positional values
                //stores various variables to determine whether board is in endgame or not
                //evaluation is based heavily on https://www.chessprogramming.org/Simplified_Evaluation_Function
                coordBlackKing=new int[2];coordWhiteKing=new int[2];
                noOfBlackMinorPieces=0;noOfWhiteMinorPieces=0;
                noOfPiecesBlack=0;noOfPiecesWhite=0;
                blackHasQueen=false;whiteHasQueen=false;
                for(int y=0;y<8;y++){
                    for(int x=0;x<8;x++){
                        switch (state.BOARD[y][x]) {
                            case ChessBoardState.PAWN_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                                boardValue=boardValue+(PAWN*BLACK_MULT)+PAWN_POSITIONAL_BLACK[y][x];
                                noOfPiecesBlack++;
                                break;
                            case ChessBoardState.KNIGHT_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                                boardValue=boardValue+(KNIGHT*BLACK_MULT)+KNIGHT_POSITIONAL_BLACK[y][x];
                                noOfBlackMinorPieces++;
                                break;
                            case ChessBoardState.BISHOP_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                                noOfBlackMinorPieces++;
                                boardValue=boardValue+(BISHOP*BLACK_MULT)+BISHOP_POSITIONAL_BLACK[y][x];
                                break;
                            case ChessBoardState.QUEEN_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                                blackHasQueen=true;
                                boardValue=boardValue+(QUEEN*BLACK_MULT)+QUEEN_POSITIONAL_BLACK[y][x];
                                break;
                            case ChessBoardState.ROOK_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                                noOfPiecesBlack++;
                                boardValue=boardValue+(ROOK*BLACK_MULT)+ROOK_POSITIONAL_BLACK[y][x];
                                break;
                            case ChessBoardState.KING_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                                boardValue=boardValue+(KING*BLACK_MULT);
                                coordBlackKing[0]=y;coordBlackKing[1]=x;
                                break;
                            case ChessBoardState.PAWN_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                                noOfPiecesWhite++;
                                boardValue=boardValue+(PAWN*WHITE_MULT)+PAWN_POSITIONAL_WHITE[y][x];
                                break;
                            case ChessBoardState.KNIGHT_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                                noOfWhiteMinorPieces++;
                                boardValue=boardValue+(KNIGHT*WHITE_MULT)+KNIGHT_POSITIONAL_WHITE[y][x];
                                break;
                            case ChessBoardState.BISHOP_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                                noOfWhiteMinorPieces++;
                                boardValue=boardValue+(WHITE_MULT*BISHOP)+BISHOP_POSITIONAL_WHITE[y][x];
                                break;
                            case ChessBoardState.QUEEN_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                                whiteHasQueen=true;
                                boardValue=boardValue+(WHITE_MULT*QUEEN)+QUEEN_POSITIONAL_WHITE[y][x];
                                break;
                            case ChessBoardState.ROOK_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                                noOfPiecesWhite++;
                                boardValue = boardValue+(WHITE_MULT*ROOK)+ROOK_POSITIONAL_WHITE[y][x];
                                break;
                            case ChessBoardState.KING_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                                boardValue=boardValue+(WHITE_MULT*KING);
                                coordWhiteKing[0]=y;coordWhiteKing[1]=x;
                                break;
                            default:
                                break;
                        }
                    }
                }
                if(((noOfPiecesBlack==0&&noOfBlackMinorPieces<=1)||!blackHasQueen)&&((noOfPiecesWhite==0&&noOfWhiteMinorPieces<=1)||!whiteHasQueen)){//this statement is true if the game is in an endgame state
                    boardValue=boardValue+KING_POSITIONAL_BLACK_END[coordBlackKing[0]][coordBlackKing[1]]+KING_POSITIONAL_WHITE_END[coordWhiteKing[0]][coordWhiteKing[1]];
                }else{
                    boardValue = boardValue+KING_POSITIONAL_BLACK_MID[coordBlackKing[0]][coordBlackKing[1]]+KING_POSITIONAL_WHITE_MID[coordWhiteKing[0]][coordWhiteKing[1]];
                }
                break;
            case 2:
                //feeds the board through a neural network to get the board value
                double ret[]=net.feedThroughNet(state.getNeuralNetInput());
                boardValue = (int)(Math.round((ret[0]-ret[1])*(Integer.MAX_VALUE-10)));
                break;
        }
        
        return boardValue;
    }
    /**
     * Gets the number of states in the current game
     * @return The number of states
     */
    public int getLengthOfStatesFoFar(){
        return states.length;
    }
    /**
     * Returns the state stored at the specified index in the array of all states.
     * @param element The index
     * @return The state
     */
    public ChessBoardState getState(int element){
        return this.states[element];
    }
    /**
     * Adds board state to game.
     * @param state The board state
     */
    public void addState(ChessBoardState state){
        ChessBoardState[]statesNew = new ChessBoardState[states.length+1];
        statesNew[statesNew.length-1]=state;
        for(int i=0;i<statesNew.length-1;i++){
            statesNew[i]=states[i];
        }
        states = statesNew;
    }
    /**
     * Returns all boards that have been played so far
     * @return All chess board states as an array. Note this array is not a copy, so states should not be written to unless appropriate.
     */
    public ChessBoardState[]getAllStates(){
        return this.states;
    }
    /**
     * Updates copy of castling rights if move is made for that square. This is used to ensure that castle rights reflect a potential rook capture.
     * If the y and x coordinate match the starting position of any rook, the castling option for that rook is set to false.
     * @param yCoord The y coordinate of the move
     * @param xCoord The x coordinate of the move
     * @param copyOfCastlingRights A copy of the castling rights. This array will be manipulated.
     */
    private static void getCastlingRightsAfterCapture(int yCoord, int xCoord,boolean copyOfCastlingRights[]){
        if(yCoord==0&&xCoord==0){//castle used for black queenside
            copyOfCastlingRights[3]=false;
        }else if(yCoord==0&&xCoord==7){//castle used for black kingside
            copyOfCastlingRights[2]=false;
        }else if(yCoord==7&& xCoord==0){//white queenside
            copyOfCastlingRights[1]=false;
        }else if(yCoord==7&&xCoord==7){//white kingside
            copyOfCastlingRights[0]=false;
        }
    }
    /**
     * Gets all legal board states from the chosen position. Note: normally only one legal board state will be returned but
     * pawn promotion will return four states (one for each possible choice of piece). DOES NOT handle 50 move rule
     * or other draw / win /loss conditions for current board, these must be handled elsewhere where appropriate.Note moves input must be legal
     * @param yStart The y coordinate of the piece
     * @param xStart The x coordinate of the piece
     * @param yFinal The y coordinate of the move
     * @param xFinal The x coordinate of the move
     * @param state The board state to analyse
     * @return All possible states. If no states exist, an array of length 0 is returned
     */
    public static  ArrayList<ChessBoardState> getAllLegalBoardsFromMove(int yStart,int xStart, int yFinal,int xFinal,ChessBoardState state){
        int[][]boardNew=state.getCopyOfBoard();//stores the new board
        boolean []castlingRights=state.getCopyOfCastlingRights();//stores the new castling rights
        ArrayList<ChessBoardState> legalBoard=new ArrayList<>(1);
        int[]enPassantSquare;//stores the en passsant square
        int halfMove=state.HALF_MOVE_CLOCK;//stores the number of half moves;
        int fullMoveNo=state.FULL_MOVE_NUMBER;//stores the number of moves made by black
        switch (state.BOARD[yStart][xStart]) {
            case ChessBoardState.PAWN_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                legalBoard=new ArrayList<>(5);
                fullMoveNo++;//increments moves as black has moved
                halfMove=0;//as pawn advance resets half move clock, half move will always be at 0 after a pawn move
                //checks to see if pawn has moved two forward (and updates en passant rights accoridngly)
                enPassantSquare=new int[0];
                if(yFinal == yStart+2){
                    enPassantSquare =new int[2];enPassantSquare[0]=yStart+1;enPassantSquare[1]=xStart;
                }
                getCastlingRightsAfterCapture(yFinal, xFinal, castlingRights);//updates castling rights where appropriate
                //processes pawn move
                //processes possible pawn promotion
                boardNew[yStart][xStart]=ChessBoardState.EMPTY_VALUE;
                if(yFinal==7){//can create four possible boards
                    //creates queen board
                    boardNew[7][xFinal]=ChessBoardState.QUEEN_VALUE*ChessBoardState.BLACK_MULTIPLIER;
                    legalBoard.add(new ChessBoardState(boardNew, castlingRights, !state.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMove, enPassantSquare));
                    //handles bishop
                    boardNew[7][xFinal]=ChessBoardState.ROOK_VALUE*ChessBoardState.BLACK_MULTIPLIER;
                    legalBoard.add(new ChessBoardState(boardNew, castlingRights, !state.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMove, enPassantSquare));
                    //handles knight
                    boardNew[7][xFinal]=ChessBoardState.BISHOP_VALUE*ChessBoardState.BLACK_MULTIPLIER;
                    legalBoard.add(new ChessBoardState(boardNew, castlingRights, !state.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMove, enPassantSquare));
                    //handles rook
                    boardNew[7][xFinal]=ChessBoardState.KNIGHT_VALUE*ChessBoardState.BLACK_MULTIPLIER;
                    legalBoard.add(new ChessBoardState(boardNew, castlingRights, !state.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMove, enPassantSquare));
                    return legalBoard;//returns states
                }
                boardNew[yFinal][xFinal]=ChessBoardState.PAWN_VALUE*ChessBoardState.BLACK_MULTIPLIER;
                if(state.SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT.length==2){//checks for en passant capture
                    if(yFinal==state.SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT[0]&&xFinal==state.SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT[1]){
                        boardNew[yStart][xFinal]=ChessBoardState.EMPTY_VALUE;//captures pawn via en passant
                    }
                }
                legalBoard.add(new ChessBoardState(boardNew, castlingRights,!state.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMove, enPassantSquare));
                return legalBoard;//returns move
            case ChessBoardState.KNIGHT_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                getCastlingRightsAfterCapture(yFinal, xFinal, castlingRights);//updates castling rights where appropriate
                fullMoveNo++;//increments moves as black has moved
                //simply replaces moves knight to square
                boardNew[yStart][xStart]=ChessBoardState.EMPTY_VALUE;boardNew[yFinal][xFinal]=ChessBoardState.KNIGHT_VALUE*ChessBoardState.BLACK_MULTIPLIER;
                //checks to see if a capture has been made
                if(state.BOARD[yFinal][xFinal]!=ChessBoardState.EMPTY_VALUE){
                    halfMove=0;
                }else{
                    halfMove++;
                }
                enPassantSquare=new int[0];//as pawn is not moved, no squares can be avaliable for en passant
                legalBoard.add(new ChessBoardState(boardNew, castlingRights,!state.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMove, enPassantSquare));//returns move
                return legalBoard;
            case ChessBoardState.BISHOP_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                getCastlingRightsAfterCapture(yFinal, xFinal, castlingRights);//updates castling rights where appropriate
                fullMoveNo++;//increments moves as black has moved
                enPassantSquare=new int[0];//as pawn is not moved, no squares can be avaliable for en passant
                //simply replaces moves bishop to square
                boardNew[yStart][xStart]=ChessBoardState.EMPTY_VALUE;boardNew[yFinal][xFinal]=ChessBoardState.BISHOP_VALUE*ChessBoardState.BLACK_MULTIPLIER;
                //checks to see if a capture has been made
                if(state.BOARD[yFinal][xFinal]!=ChessBoardState.EMPTY_VALUE){
                    halfMove=0;
                }else{
                    halfMove++;
                }
                enPassantSquare=new int[0];//as pawn is not moved, no squares can be avaliable for en passant
                legalBoard.add(new ChessBoardState(boardNew, castlingRights,!state.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMove, enPassantSquare));//returns move
                return legalBoard;
            case ChessBoardState.ROOK_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                fullMoveNo++;//increments moves as black has moved
                enPassantSquare=new int[0];//as pawn is not moved, no squares can be avaliable for en passant
                //simply replaces moves rook to square
                boardNew[yStart][xStart]=ChessBoardState.EMPTY_VALUE;boardNew[yFinal][xFinal]=ChessBoardState.ROOK_VALUE*ChessBoardState.BLACK_MULTIPLIER;
                //checks to see if a capture has been made
                if(state.BOARD[yFinal][xFinal]!=ChessBoardState.EMPTY_VALUE){
                    halfMove=0;
                }else{
                    halfMove++;
                }
                //updates castling rights if appropriate
                if(yStart==0){
                    if(xStart==0){//black queenside
                        castlingRights[3]=false;
                    }else if(xStart==7){//black kingside
                        castlingRights[2]=false;
                    }
                }else if(yStart==7){
                    if(xStart==0){//white queenside
                        castlingRights[1]=false;
                    }else if(xStart==7){//white kingside
                        castlingRights[0]=false;
                    }
                }
                enPassantSquare=new int[0];//as pawn is not moved, no squares can be avaliable for en passant
                legalBoard.add(new ChessBoardState(boardNew, castlingRights,!state.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMove, enPassantSquare));//returns move
                return legalBoard;
            case ChessBoardState.QUEEN_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                getCastlingRightsAfterCapture(yFinal, xFinal, castlingRights);//updates castling rights where appropriate
                fullMoveNo++;//increments moves as black has moved
                enPassantSquare=new int[0];//as pawn is not moved, no squares can be avaliable for en passant
                //simply replaces moves queen to square
                boardNew[yStart][xStart]=ChessBoardState.EMPTY_VALUE;boardNew[yFinal][xFinal]=ChessBoardState.QUEEN_VALUE*ChessBoardState.BLACK_MULTIPLIER;
                //checks to see if a capture has been made
                if(state.BOARD[yFinal][xFinal]!=ChessBoardState.EMPTY_VALUE){
                    halfMove=0;
                }else{
                    halfMove++;
                }
                enPassantSquare=new int[0];//as pawn is not moved, no squares can be avaliable for en passant
                legalBoard.add(new ChessBoardState(boardNew, castlingRights,!state.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMove, enPassantSquare));//returns move
                return legalBoard;
            case ChessBoardState.KING_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                getCastlingRightsAfterCapture(yFinal, xFinal, castlingRights);//updates castling rights where appropriate
                fullMoveNo++;//increments moves as black has moved
                enPassantSquare=new int[0];//as pawn is not moved, no squares can be avaliable for en passant
                //moves king
                boardNew[yStart][xStart]=ChessBoardState.EMPTY_VALUE;boardNew[yFinal][xFinal]=ChessBoardState.KING_VALUE*ChessBoardState.BLACK_MULTIPLIER;
                //checks to see if castling is happening and updates rook as appropraite
                if(xFinal==xStart+2){//kingside castling
                    boardNew[0][5]=ChessBoardState.BLACK_MULTIPLIER*ChessBoardState.ROOK_VALUE;boardNew[0][7]=ChessBoardState.EMPTY_VALUE;
                }else if(xFinal==xStart-2){//queenside castling
                    boardNew[0][3]=ChessBoardState.BLACK_MULTIPLIER*ChessBoardState.ROOK_VALUE;boardNew[0][0]=ChessBoardState.EMPTY_VALUE;
                }
                //updates castling rights
                castlingRights[2]=false;castlingRights[3]=false;
                legalBoard.add(new ChessBoardState(boardNew, castlingRights,!state.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMove, enPassantSquare));//returns move
                return legalBoard;
            case ChessBoardState.PAWN_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                legalBoard=new ArrayList<>(5);
                getCastlingRightsAfterCapture(yFinal, xFinal, castlingRights);//updates castling rights where appropriate
                halfMove=0;//as pawn advance resets half move clock, half move will always be at 0 after a pawn move
                //checks to see if pawn has moved two forward (and updates en passant rights accoridngly)
                enPassantSquare=new int[0];
                if(yFinal == yStart-2){
                    enPassantSquare =new int[2];enPassantSquare[0]=yStart-1;enPassantSquare[1]=xStart;
                }
                //processes pawn move
                //processes possible pawn promotion
                boardNew[yStart][xStart]=ChessBoardState.EMPTY_VALUE;
                if(yFinal==0){//can create four possible boards
                    //creates queen board
                    boardNew[0][xFinal]=ChessBoardState.QUEEN_VALUE*ChessBoardState.WHITE_MULTIPLIER;
                    legalBoard.add(new ChessBoardState(boardNew, castlingRights,!state.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMove, enPassantSquare));
                    //handles bishop
                    boardNew[0][xFinal]=ChessBoardState.ROOK_VALUE*ChessBoardState.WHITE_MULTIPLIER;
                    legalBoard.add(new ChessBoardState(boardNew, castlingRights,!state.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMove, enPassantSquare));
                    //handles knight
                    boardNew[0][xFinal]=ChessBoardState.BISHOP_VALUE*ChessBoardState.WHITE_MULTIPLIER;
                    legalBoard.add(new ChessBoardState(boardNew, castlingRights,!state.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMove, enPassantSquare));
                    //handles rook
                    boardNew[0][xFinal]=ChessBoardState.KNIGHT_VALUE*ChessBoardState.WHITE_MULTIPLIER;
                    legalBoard.add(new ChessBoardState(boardNew, castlingRights,!state.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMove, enPassantSquare));
                    return legalBoard;//returns states
                }
                boardNew[yFinal][xFinal]=ChessBoardState.PAWN_VALUE*ChessBoardState.WHITE_MULTIPLIER;
                if(state.SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT.length==2){//checks for en passant capture
                    if(yFinal==state.SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT[0]&&xFinal==state.SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT[1]){
                        boardNew[yStart][xFinal]=ChessBoardState.EMPTY_VALUE;//captures pawn via en passant
                    }
                }
                legalBoard.add(new ChessBoardState(boardNew, castlingRights,!state.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMove, enPassantSquare));//returns move            
                return legalBoard;
            case ChessBoardState.KNIGHT_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                getCastlingRightsAfterCapture(yFinal, xFinal, castlingRights);//updates castling rights where appropriate
                //simply replaces moves knight to square
                boardNew[yStart][xStart]=ChessBoardState.EMPTY_VALUE;boardNew[yFinal][xFinal]=ChessBoardState.KNIGHT_VALUE*ChessBoardState.WHITE_MULTIPLIER;
                //checks to see if a capture has been made
                if(state.BOARD[yFinal][xFinal]!=ChessBoardState.EMPTY_VALUE){
                    halfMove=0;
                }else{
                    halfMove++;
                }
                enPassantSquare=new int[0];//as pawn is not moved, no squares can be avaliable for en passant
                legalBoard.add(new ChessBoardState(boardNew, castlingRights,!state.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMove, enPassantSquare));//returns move
                return legalBoard;
            case ChessBoardState.BISHOP_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                getCastlingRightsAfterCapture(yFinal, xFinal, castlingRights);//updates castling rights where appropriate
                enPassantSquare=new int[0];//as pawn is not moved, no squares can be avaliable for en passant
                //simply replaces moves bishop to square
                boardNew[yStart][xStart]=ChessBoardState.EMPTY_VALUE;boardNew[yFinal][xFinal]=ChessBoardState.BISHOP_VALUE*ChessBoardState.WHITE_MULTIPLIER;
                //checks to see if a capture has been made
                if(state.BOARD[yFinal][xFinal]!=ChessBoardState.EMPTY_VALUE){
                    halfMove=0;
                }else{
                    halfMove++;
                }
                enPassantSquare=new int[0];//as pawn is not moved, no squares can be avaliable for en passant
                legalBoard.add(new ChessBoardState(boardNew, castlingRights,!state.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMove, enPassantSquare));//returns move
                return legalBoard;
            case ChessBoardState.ROOK_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                enPassantSquare=new int[0];//as pawn is not moved, no squares can be avaliable for en passant
                //simply replaces moves rook to square
                boardNew[yStart][xStart]=ChessBoardState.EMPTY_VALUE;boardNew[yFinal][xFinal]=ChessBoardState.ROOK_VALUE*ChessBoardState.WHITE_MULTIPLIER;
                //checks to see if a capture has been made
                if(state.BOARD[yFinal][xFinal]!=ChessBoardState.EMPTY_VALUE){
                    halfMove=0;
                }else{
                    halfMove++;
                }
                //updates castling rights if appropriate
                if(yStart==0){
                    if(xStart==0){//black queenside
                        castlingRights[3]=false;
                    }else if(xStart==7){//black kingside
                        castlingRights[2]=false;
                    }
                }else if(yStart==7){
                    if(xStart==0){//white queenside
                        castlingRights[1]=false;
                    }else if(xStart==7){//white kingside
                        castlingRights[0]=false;
                    }
                }
                enPassantSquare=new int[0];//as pawn is not moved, no squares can be avaliable for en passant
                legalBoard.add(new ChessBoardState(boardNew, castlingRights,!state.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMove, enPassantSquare));//returns move
                return legalBoard;
            case ChessBoardState.QUEEN_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                getCastlingRightsAfterCapture(yFinal, xFinal, castlingRights);//updates castling rights where appropriate
                enPassantSquare=new int[0];//as pawn is not moved, no squares can be avaliable for en passant
                //simply replaces moves queen to square
                boardNew[yStart][xStart]=ChessBoardState.EMPTY_VALUE;boardNew[yFinal][xFinal]=ChessBoardState.QUEEN_VALUE*ChessBoardState.WHITE_MULTIPLIER;
                //checks to see if a capture has been made
                if(state.BOARD[yFinal][xFinal]!=ChessBoardState.EMPTY_VALUE){
                    halfMove=0;
                }else{
                    halfMove++;
                }
                enPassantSquare=new int[0];//as pawn is not moved, no squares can be avaliable for en passant
                legalBoard.add(new ChessBoardState(boardNew, castlingRights,!state.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMove, enPassantSquare));//returns move
                return legalBoard;
            default:
                //white king
                getCastlingRightsAfterCapture(yFinal, xFinal, castlingRights);//updates castling rights where appropriate
                enPassantSquare=new int[0];//as pawn is not moved, no squares can be avaliable for en passant
                //moves king
                boardNew[yStart][xStart]=ChessBoardState.EMPTY_VALUE;boardNew[yFinal][xFinal]=ChessBoardState.KING_VALUE*ChessBoardState.WHITE_MULTIPLIER;
                //checks to see if castling is happening and updates rook as appropraite
                if(xFinal==xStart+2){//kingside castling
                    boardNew[7][5]=ChessBoardState.WHITE_MULTIPLIER*ChessBoardState.ROOK_VALUE;boardNew[7][7]=ChessBoardState.EMPTY_VALUE;
                }else if(xFinal==xStart-2){//queenside castling
                    boardNew[7][3]=ChessBoardState.WHITE_MULTIPLIER*ChessBoardState.ROOK_VALUE;boardNew[7][0]=ChessBoardState.EMPTY_VALUE;
                }
                //updates castling rights
                castlingRights[0]=false;castlingRights[1]=false;
                legalBoard.add(new ChessBoardState(boardNew, castlingRights,!state.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMove, enPassantSquare));//returns move
                return legalBoard;
        }
    }
    /**
     * Gets all legal board states from the input current board state. DOES handle 50 move rule
     * @param currentBoard The board state
     * @return An array of all legal board states. If this array is of length 0, there are no legal board states from this board state
     */
    public ArrayList<ChessBoardState> getAllLegalBoardsFromState(ChessBoardState currentBoard){
        ArrayList<ChessBoardState> allNewStates=new ArrayList<>(35);//stores all legal board states resulting from current board state
        //checks for draw due to 50 move rule - players are automatically drawn
        if(currentBoard.HALF_MOVE_CLOCK>=100){
            currentBoard.setIsDrawTrue();
            return allNewStates;//returns no legal moves
        }
        //checks for a draw due to insufficient pieces
        /*draw condition with insufficient pieces: 
        only king and king, only king-bishop and king, king-knight and king, king-bishop and bishop-king with bishops on same colour square
        */
        int noOfWhiteKnights=0;int noOfBlackKnights=0;int noOfWhiteBishops=0;int noOfBlackBishops=0; boolean isBlackBishopOnBlack=false;
        int noOfOtherPieces=0;//stores number of pieces that are not king, bishop or knights
        boolean isWhiteBishopOnBlack=false;boolean isBlackBishopOnWhite=false;boolean isWhiteBishopOnWhite=false;boolean buff;
        for(int y=0;y<8;y++){
            for(int x=0;x<8;x++){
                switch (currentBoard.BOARD[y][x]) {
                    case ChessBoardState.KNIGHT_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                        noOfWhiteKnights++;
                        break;
                    case ChessBoardState.KNIGHT_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                        noOfBlackKnights++;
                        break;
                    case ChessBoardState.BISHOP_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                        noOfBlackBishops++;
                        buff=(y%2==0&&x%2==0)||(y%2==1&&x%2==1);//calculates what colour bishop is on
                        isBlackBishopOnWhite=buff||isBlackBishopOnWhite;
                        isBlackBishopOnBlack=!buff ||isBlackBishopOnBlack;
                        break;
                    case ChessBoardState.BISHOP_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                        noOfWhiteBishops++;
                        buff=(y%2==0&&x%2==0)||(y%2==1&&x%2==1);//calculates what colur bishop is on
                        isWhiteBishopOnWhite=buff||isWhiteBishopOnWhite;
                        isWhiteBishopOnBlack=!buff ||isWhiteBishopOnBlack;
                        break;
                    case ChessBoardState.EMPTY_VALUE:
                        break;
                    case ChessBoardState.KING_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                            break;
                    case ChessBoardState.KING_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                        break;
                    default:
                        noOfOtherPieces++;
                        break;
                }
            }
        }
        //checks for draw due to insufficient material
        if(!(noOfOtherPieces>0 || ((noOfBlackKnights+noOfWhiteKnights)>1) || ((noOfBlackKnights+noOfWhiteKnights==1) && (noOfBlackBishops+noOfWhiteBishops>0)) || (noOfBlackBishops>1)||(noOfWhiteBishops>1)||(noOfBlackBishops==1 && noOfWhiteBishops==1 && ((isBlackBishopOnBlack&&isWhiteBishopOnWhite)||(isBlackBishopOnWhite&&isWhiteBishopOnBlack))))){
            currentBoard.setIsDrawTrue();//sets game as draw
            return allNewStates;//returns no valid options
        }
        ArrayList<Integer>allLegalMoves=getAllLegalMoves(currentBoard);
        if(allLegalMoves.isEmpty()){//when no moves can be made, need to check whether it's checkmate or stalemate
            if(isInCheck(!currentBoard.IS_BLACK_TURN_TO_PLAY_NEXT,  new ChessBoardState(currentBoard.BOARD, currentBoard.CASTLING_RIGHTS, !currentBoard.IS_BLACK_TURN_TO_PLAY_NEXT, currentBoard.FULL_MOVE_NUMBER, currentBoard.HALF_MOVE_CLOCK, currentBoard.SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT))){
                currentBoard.setIsLossTrue();//checkmate
            }else{
                currentBoard.setIsDrawTrue();//stalemate
            }
            return allNewStates;
        }
        for(int move=0;move<allLegalMoves.size();move=move+4){//loops through all legal moves and generates all resulting possible board states
            allNewStates.addAll(getAllLegalBoardsFromMove(allLegalMoves.get(move), allLegalMoves.get(move+1), allLegalMoves.get(move+2), allLegalMoves.get(move+3), currentBoard));
        }
        return allNewStates;
    }
    /**
     * Creates new array of chessboards. Note this should only be called in the getAllLegalBoardsFromState method.
     * @param array The array
     * @param stateToAdd The new state
     * @return the new array
     */
    private static ChessBoardState[]addChessBoardStateToArray(ChessBoardState[]array,ChessBoardState stateToAdd){
        ChessBoardState newStates[]=new ChessBoardState[array.length+1];
        for(int i=0;i<newStates.length;i++){//copies old states
            newStates[i]=array[i];
        }
        newStates[array.length]=stateToAdd;//adds new state
        return newStates;
    }
    /**
     * Creates new array of chessboards. Note this should only be called in the getAllLegalBoardsFromState method.
     * @param array The array
     * @param statesToAdd The new states
     * @return the new array
     */    
    private static ChessBoardState[]addChessBoardStateToArray(ChessBoardState[]array,ChessBoardState[]statesToAdd){
        ChessBoardState newStates[]=new ChessBoardState[array.length+statesToAdd.length];
        for(int i=0;i<array.length;i++){//copies old states to array
            newStates[i]=array[i];
        }
        for(int i=0;i<statesToAdd.length;i++){//adds new states to array
            newStates[i+array.length]=statesToAdd[i];
        }
        return newStates;
    }
    /**
     * Calls getAllLegalMovesFromSquare method on every square of the chess board and returns all moves. Read documentation of that method for details on moves
     * @param board The board to analyse
     * @return An ArrayList with all legal moves from the current board.
     */
    private  ArrayList <Integer> getAllLegalMoves(ChessBoardState board){
        ArrayList<Integer> allMoves=new ArrayList<>(140);//stores all moves in the form yStart, xStart, yEnd, xEnd
        ArrayList<Integer>movesFromSquare;
        int index=-1;
        //gets all legal moves from each square on the board
        for(int y=0;y<8;y++){
            for(int x=0;x<8;x++){
                movesFromSquare=getAllLegalMovesFromSquare(y, x, board);
                for(int move=0;move<movesFromSquare.size();move=move+2){
                    allMoves.add(++index,y);allMoves.add(++index,x);allMoves.add(++index,movesFromSquare.get(move));allMoves.add(++index,movesFromSquare.get(move+1));
                }
            }
        }
        //moves ordering is employed
        final int SIZE=allMoves.size();
        int allMovesIndex=-1;
        int moveVal[][]=new int[SIZE][2];//stores the approximate value of the move and the pointer to where it is stored - this is used to order the moves rougly in order of best to worst
        for(int move=0;move<SIZE/4;move++){
            allMovesIndex++;
            moveVal[move][1]=allMovesIndex;
            moveVal[move][0]=getValueOfMove(board, allMoves.get(allMovesIndex), allMoves.get(++allMovesIndex), allMoves.get(++allMovesIndex), allMoves.get(++allMovesIndex));
        }
        //sorts moves by value
        Sort.mergeSortInt(moveVal);
        //swaps moves around so they are sorted from best to worst
        allMovesIndex=-1;
        for(int move=0;move<SIZE/4;move++){
            //swaps moves so they are ordered
            Collections.swap(allMoves,++allMovesIndex,moveVal[move][1]);
            Collections.swap(allMoves,++allMovesIndex,moveVal[move][1]+1);
            Collections.swap(allMoves,++allMovesIndex,moveVal[move][1]+2);
            Collections.swap(allMoves,++allMovesIndex,moveVal[move][1]+3);
        } 
        return allMoves;//returns all legal moves
    }
    /**
     * Estimates the value of a move, in order to rank the move values
     * @param state The board state
     * @param yStart The starting y coordinate
     * @param xStart The starting x coordinate
     * @param yEnd The final y coordinate
     * @param xEnd The final x coordinate
     * @return The value of the move
     */
    private int getValueOfMove(ChessBoardState state, int yStart, int xStart, int yEnd, int xEnd){
        int val=0;
        //gets the move value by looking how the value of the piece changes due to it's positional change. The value aslo changes depending on whether captures are made.
        //alters value for square
        switch (state.BOARD[yStart][xStart]) {
            case ChessBoardState.PAWN_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                val=PAWN_POSITIONAL_WHITE[yEnd][xEnd]- PAWN_POSITIONAL_WHITE[yStart][xStart];
                break;
            case ChessBoardState.BISHOP_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                val=BISHOP_POSITIONAL_WHITE[yEnd][xEnd]- BISHOP_POSITIONAL_WHITE[yStart][xStart];
                break;
            case ChessBoardState.KNIGHT_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                val=KNIGHT_POSITIONAL_WHITE[yEnd][xEnd]- KNIGHT_POSITIONAL_WHITE[yStart][xStart];
                break;
            case ChessBoardState.ROOK_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                val=ROOK_POSITIONAL_WHITE[yEnd][xEnd]- ROOK_POSITIONAL_WHITE[yStart][xStart];
                break;
            case ChessBoardState.QUEEN_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                val=QUEEN_POSITIONAL_WHITE[yEnd][xEnd]- QUEEN_POSITIONAL_WHITE[yStart][xStart];
                break;
            case ChessBoardState.KING_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                val=KING_POSITIONAL_WHITE_MID[yEnd][xEnd]-KING_POSITIONAL_WHITE_MID[yStart][xStart];
                break;
            case ChessBoardState.PAWN_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                val=PAWN_POSITIONAL_BLACK[yEnd][xEnd]- PAWN_POSITIONAL_BLACK[yStart][xStart];
                break;
            case ChessBoardState.BISHOP_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                val=BISHOP_POSITIONAL_BLACK[yEnd][xEnd]- BISHOP_POSITIONAL_BLACK[yStart][xStart];
                break;
            case ChessBoardState.KNIGHT_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                val=KNIGHT_POSITIONAL_BLACK[yEnd][xEnd]- KNIGHT_POSITIONAL_BLACK[yStart][xStart];
                break;
            case ChessBoardState.ROOK_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                val=ROOK_POSITIONAL_BLACK[yEnd][xEnd]- ROOK_POSITIONAL_BLACK[yStart][xStart];
                break;
            case ChessBoardState.QUEEN_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                val=QUEEN_POSITIONAL_BLACK[yEnd][xEnd]- QUEEN_POSITIONAL_BLACK[yStart][xStart];
                break;
            default:
                val=KING_POSITIONAL_BLACK_MID[yEnd][xEnd]-KING_POSITIONAL_BLACK_MID[yStart][xStart];
                break;
        }
        switch (state.BOARD[yEnd][xEnd]) {
            case ChessBoardState.PAWN_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                val=val-(PAWN*ChessBoardState.WHITE_MULTIPLIER + PAWN_POSITIONAL_WHITE[yEnd][xEnd]);
                break;
            case ChessBoardState.BISHOP_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                val=val-(BISHOP*ChessBoardState.WHITE_MULTIPLIER + BISHOP_POSITIONAL_WHITE[yEnd][xEnd]);
                break;
            case ChessBoardState.KNIGHT_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                val=val-(KNIGHT*ChessBoardState.WHITE_MULTIPLIER + KNIGHT_POSITIONAL_WHITE[yEnd][xEnd]);
                break;
            case ChessBoardState.ROOK_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                val=val-(ROOK*ChessBoardState.WHITE_MULTIPLIER + ROOK_POSITIONAL_WHITE[yEnd][xEnd]);
                break;
            case ChessBoardState.QUEEN_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                val=val-(QUEEN*ChessBoardState.WHITE_MULTIPLIER + QUEEN_POSITIONAL_WHITE[yEnd][xEnd]);
                break;
            case ChessBoardState.KING_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                val=val-(KING*ChessBoardState.WHITE_MULTIPLIER + KING_POSITIONAL_WHITE_MID[yEnd][xEnd]);
                break;
            case ChessBoardState.PAWN_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                val=PAWN_POSITIONAL_BLACK[yEnd][xEnd]- PAWN_POSITIONAL_BLACK[yStart][xStart];
                val=val-(PAWN*ChessBoardState.BLACK_MULTIPLIER + PAWN_POSITIONAL_BLACK[yEnd][xEnd]);
                break;
            case ChessBoardState.BISHOP_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                val=val-(BISHOP*ChessBoardState.BLACK_MULTIPLIER + BISHOP_POSITIONAL_BLACK[yEnd][xEnd]);
                break;
            case ChessBoardState.KNIGHT_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                val=val-(KNIGHT*ChessBoardState.BLACK_MULTIPLIER + KNIGHT_POSITIONAL_BLACK[yEnd][xEnd]);
                break;
            case ChessBoardState.ROOK_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                val=val-(ROOK*ChessBoardState.BLACK_MULTIPLIER + ROOK_POSITIONAL_BLACK[yEnd][xEnd]);
                break;
            case ChessBoardState.QUEEN_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                val=val-(QUEEN*ChessBoardState.BLACK_MULTIPLIER + QUEEN_POSITIONAL_BLACK[yEnd][xEnd]);
                break;
            case ChessBoardState.KING_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                val=val-(KING*ChessBoardState.BLACK_MULTIPLIER + KING_POSITIONAL_BLACK_MID[yEnd][xEnd]);
                break;
            default:
                break;
        }    
        return val;
    }
    /**
     * gets the value of a piece and returns it
     * @param PIECE The piece
     * @return The relative value of the piece
     */
    private static int getPieceValue(final int PIECE){
       //gets the value of the piece
        switch (PIECE) {
            case ChessBoardState.PAWN_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                return PAWN;
            case ChessBoardState.KNIGHT_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                return KNIGHT;
            case ChessBoardState.ROOK_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                return ROOK;
            case ChessBoardState.BISHOP_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                return BISHOP;
            case ChessBoardState.QUEEN_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                return QUEEN;
            case ChessBoardState.KING_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                return KING;
            case ChessBoardState.PAWN_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                return PAWN;
            case ChessBoardState.KNIGHT_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                return KNIGHT;
            case ChessBoardState.ROOK_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                return ROOK;
            case ChessBoardState.BISHOP_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                return BISHOP;
            case ChessBoardState.QUEEN_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                return QUEEN;
            case ChessBoardState.KING_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                return KING;
            default:
                return 0;
        }
    }
    /**
     * Gets all legal moves that can be made from the square
     * Notes: pawn promotion is treated as moving a pawn to the final rank. However, the actual piece chosen needs to be handled.
     * Notes: half move clock is NOT handled by this method
     * Notes: castling is treated as king moving two spaces to the left or right
     * @param yStartingSquare The y index of the starting square
     * @param xStartingSquare The x index of the starting square
     * @param board The board state to be analysed
     * @return An array list returning all legal moves from a square
     */
    public static ArrayList <Integer> getAllLegalMovesFromSquare(int yStartingSquare, int xStartingSquare,ChessBoardState board){
        //checks to see if square selected is empty or a piece of the wrong colour
        int xVal;
        if(board.BOARD[yStartingSquare][xStartingSquare]==ChessBoardState.EMPTY_VALUE || (board.IS_BLACK_TURN_TO_PLAY_NEXT&&board.BOARD[yStartingSquare][xStartingSquare]>0)|| (!board.IS_BLACK_TURN_TO_PLAY_NEXT&&board.BOARD[yStartingSquare][xStartingSquare]<0)){
            return new ArrayList<>(0);//returns no legal moves
        }
        ArrayList<Integer> moves;
        switch (board.BOARD[yStartingSquare][xStartingSquare]) {
            case ChessBoardState.PAWN_VALUE*ChessBoardState.WHITE_MULTIPLIER:
            {
                moves=getAllLegalMovesForPawnIgnoringCheck(yStartingSquare, xStartingSquare, board);
                boolean castlingRights[]=board.getCopyOfCastlingRights();//stores castling rights
                int[][]newBoard;//stores copy of board after move made;
                int[]enPassantRightsNew;//stores possible en passant squares
                int fullMoveNo=board.FULL_MOVE_NUMBER;//stores the full move number of the next move
                fullMoveNo++;
                int halfMoveNo=0;//stores half move number -will always be zero for pawn moves
                for(int i=0;i<moves.size();i=i+2){//goes through all moves
                    xVal=i+1;
                    if(moves.get(i)==yStartingSquare+2){//checks for black pawn moving two squares forward
                        enPassantRightsNew=new int[]{yStartingSquare+1,xStartingSquare};
                        newBoard=board.getCopyOfBoard();
                        //adds move to board
                        newBoard[yStartingSquare][xStartingSquare]=ChessBoardState.EMPTY_VALUE;newBoard[yStartingSquare+2][xStartingSquare]=ChessBoardState.PAWN_VALUE*ChessBoardState.WHITE_MULTIPLIER;
                        if(isInCheck(!board.IS_BLACK_TURN_TO_PLAY_NEXT, new ChessBoardState(newBoard, castlingRights, !board.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMoveNo, enPassantRightsNew))==true){
                            moves.remove(i);moves.remove(i);//removes invalid board states from list
                            i=i-2;
                        }
                    }else if(moves.get(i)==yStartingSquare-2){//checks for white pawn moving two squares forward
                        enPassantRightsNew=new int[]{yStartingSquare-1,xStartingSquare};
                        newBoard=board.getCopyOfBoard();
                        //adds move to board
                        newBoard[yStartingSquare][xStartingSquare]=ChessBoardState.EMPTY_VALUE;newBoard[yStartingSquare-2][xStartingSquare]=ChessBoardState.PAWN_VALUE*ChessBoardState.WHITE_MULTIPLIER;
                        if(isInCheck(!board.IS_BLACK_TURN_TO_PLAY_NEXT, new ChessBoardState(newBoard, castlingRights, !board.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMoveNo, enPassantRightsNew))==true){
                            moves.remove(i);moves.remove(i);//removes invalid board states from list
                            i=i-2;
                        }
                    }else if((moves.get(i)==yStartingSquare-1 && moves.get(xVal)==xStartingSquare)||(moves.get(i)==yStartingSquare+1 && moves.get(xVal)==xStartingSquare)){//checks for white or black pawn moving one square forward
                        enPassantRightsNew= new int[0];//no en passant rights as piece is moving one square
                        newBoard=board.getCopyOfBoard();//gets copy of board
                        //adds move
                        newBoard[moves.get(i)][moves.get(xVal)]=ChessBoardState.PAWN_VALUE*ChessBoardState.WHITE_MULTIPLIER;newBoard[yStartingSquare][xStartingSquare]=ChessBoardState.EMPTY_VALUE;
                        if(isInCheck(!board.IS_BLACK_TURN_TO_PLAY_NEXT, new ChessBoardState(newBoard, castlingRights, !board.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMoveNo, enPassantRightsNew))==true){
                            moves.remove(i);moves.remove(i);//removes invalid board states from list
                            i=i-2;
                        }                          
                    }else{//must be a diagonal capture
                        enPassantRightsNew=new int[0];
                        if(board.BOARD[moves.get(i)][moves.get(xVal)]==ChessBoardState.EMPTY_VALUE){//en passant capture
                            newBoard=board.getCopyOfBoard();//gets copy of board
                            //adds move
                            newBoard[yStartingSquare][moves.get(xVal)]=ChessBoardState.EMPTY_VALUE;
                            newBoard[yStartingSquare][xStartingSquare]=ChessBoardState.EMPTY_VALUE;
                            newBoard[moves.get(i)][moves.get(xVal)]=ChessBoardState.PAWN_VALUE*ChessBoardState.WHITE_MULTIPLIER;
                            if(isInCheck(!board.IS_BLACK_TURN_TO_PLAY_NEXT, new ChessBoardState(newBoard, castlingRights, !board.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMoveNo, enPassantRightsNew))==true){
                                moves.remove(i);moves.remove(i);//removes invalid board states from list
                                i=i-2;
                            }
                        }else{//normal capture
                            newBoard=board.getCopyOfBoard();//gets copy of board
                            //adds move
                            newBoard[yStartingSquare][xStartingSquare]=ChessBoardState.EMPTY_VALUE;newBoard[moves.get(i)][moves.get(xVal)]=ChessBoardState.PAWN_VALUE*ChessBoardState.WHITE_MULTIPLIER;
                            if(isInCheck(!board.IS_BLACK_TURN_TO_PLAY_NEXT, new ChessBoardState(newBoard, castlingRights, !board.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMoveNo, enPassantRightsNew))==true){
                                moves.remove(i);moves.remove(i);//removes invalid board states from list
                                i=i-2;
                            }
                        }
                    }
                }
                return moves;
            }
            case ChessBoardState.ROOK_VALUE*ChessBoardState.WHITE_MULTIPLIER:
            {
                moves=getLegalMovesForRookIgnoringCheck(yStartingSquare, xStartingSquare, board);
                boolean castlingRights[]=board.getCopyOfCastlingRights();//stores castling rights
                int[][]newBoard;//stores copy of board after move made;
                int[]enPassantRightsNew=new int[0];//no square will be available for en passant after rook move
                int fullMoveNo=board.FULL_MOVE_NUMBER;//stores the full move number of the next move
                if(board.IS_BLACK_TURN_TO_PLAY_NEXT){//updates move number and en passant rights
                    fullMoveNo++;
                    if(yStartingSquare==0&&xStartingSquare==0){
                        castlingRights[1]=false;
                    }else if(yStartingSquare==0&&xStartingSquare==7){
                        castlingRights[0]=false;
                    }
                }else{
                    if(yStartingSquare==7&&xStartingSquare==0){
                        castlingRights[3]=false;
                    }else if(yStartingSquare==7&&xStartingSquare==7){
                        castlingRights[2]=false;
                    }
                }
                int halfMoveNo;//stores half move number
                for(int i=0;i<moves.size();i=i+2){//goes through all moves
                    xVal=i+1;
                    //generates new board
                    newBoard=board.getCopyOfBoard();
                    newBoard[moves.get(i)][moves.get(xVal)]=ChessBoardState.ROOK_VALUE*ChessBoardState.WHITE_MULTIPLIER;//moves castle to new square
                    halfMoveNo=board.HALF_MOVE_CLOCK+1;
                    newBoard[yStartingSquare][xStartingSquare]=ChessBoardState.EMPTY_VALUE;//sets old square to empty
                    if(board.BOARD[moves.get(i)][moves.get(xVal)]!=ChessBoardState.EMPTY_VALUE){//checks to see if rook has captured a piece
                        halfMoveNo=0;
                    }
                    if(isInCheck(!board.IS_BLACK_TURN_TO_PLAY_NEXT, new ChessBoardState(newBoard, castlingRights, !board.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMoveNo, enPassantRightsNew))==true){//checks to make sure move does not result in own check getting in check
                        moves.remove(i);moves.remove(i);//removes invalid board states from list
                        i=i-2;
                    }
                }
                return moves;
            }
            case ChessBoardState.KNIGHT_VALUE*ChessBoardState.WHITE_MULTIPLIER:
            {
                moves = getAllLegalMovesForKnightIgnoringCheck(yStartingSquare, xStartingSquare, board);
                boolean castlingRights[]=board.getCopyOfCastlingRights();//stores castling rights - this wont be changed by any knight move
                int[][]newBoard;//stores copy of board after move made;
                int[]enPassantRightsNew=new int[0];//no square will be available for en passant after knight move
                int fullMoveNo=board.FULL_MOVE_NUMBER;//stores the full move number of the next move
                if(board.IS_BLACK_TURN_TO_PLAY_NEXT){
                    fullMoveNo++;
                }
                int halfMoveNo;//stores half move number
                for(int i=0;i<moves.size();i=i+2){//goes through all moves
                    xVal=i+1;
                    //generates new board
                    newBoard=board.getCopyOfBoard();
                    newBoard[moves.get(i)][moves.get(xVal)]=ChessBoardState.KNIGHT_VALUE*ChessBoardState.WHITE_MULTIPLIER;//moves knight to new square
                    halfMoveNo=board.HALF_MOVE_CLOCK+1;
                    newBoard[yStartingSquare][xStartingSquare]=ChessBoardState.EMPTY_VALUE;//sets old square to empty
                    if(board.BOARD[moves.get(i)][moves.get(xVal)]!=ChessBoardState.EMPTY_VALUE){//checks to see if knight has captured a piece
                        halfMoveNo=0;
                    }
                    if(isInCheck(!board.IS_BLACK_TURN_TO_PLAY_NEXT, new ChessBoardState(newBoard, castlingRights, !board.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMoveNo, enPassantRightsNew))==true){//checks to make sure move does not result in own check getting in check
                        moves.remove(i);moves.remove(i);//removes invalid board states from list
                        i=i-2;
                    }
                }
                return moves;
            }
            case ChessBoardState.BISHOP_VALUE*ChessBoardState.WHITE_MULTIPLIER:
            {
                moves = getLegalMovesForBishopIgnoringCheck(yStartingSquare, xStartingSquare, board);
                boolean castlingRights[]=board.getCopyOfCastlingRights();//stores castling rights - this wont be changed by any bishop move
                int[][]newBoard;//stores copy of board after move made;
                int[]enPassantRightsNew=new int[0];//no square will be available for en passant after bishop move
                int fullMoveNo=board.FULL_MOVE_NUMBER;//stores the full move number of the next move
                if(board.IS_BLACK_TURN_TO_PLAY_NEXT){
                    fullMoveNo++;
                }
                int halfMoveNo;//stores half move number
                for(int i=0;i<moves.size();i=i+2){//goes through all moves
                    xVal=i+1;
                    //generates new board
                    newBoard=board.getCopyOfBoard();
                    newBoard[moves.get(i)][moves.get(xVal)]=ChessBoardState.BISHOP_VALUE*ChessBoardState.WHITE_MULTIPLIER;//moves bishop to new square
                    halfMoveNo=board.HALF_MOVE_CLOCK+1;
                    newBoard[yStartingSquare][xStartingSquare]=ChessBoardState.EMPTY_VALUE;//sets old square to empty
                    if(board.BOARD[moves.get(i)][moves.get(xVal)]!=ChessBoardState.EMPTY_VALUE){//checks to see if bishop has captured a piece
                        halfMoveNo=0;
                    }
                    if(isInCheck(!board.IS_BLACK_TURN_TO_PLAY_NEXT, new ChessBoardState(newBoard, castlingRights, !board.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMoveNo, enPassantRightsNew))==true){//checks to make sure move does not result in own check getting in check
                        moves.remove(i);moves.remove(i);//removes invalid board states from list
                        i=i-2;
                    }
                }
                return moves;
            }
            case ChessBoardState.QUEEN_VALUE*ChessBoardState.WHITE_MULTIPLIER:
            {
                moves=getLegalMovesForBishopIgnoringCheck(yStartingSquare, xStartingSquare, board);
                moves.addAll(getLegalMovesForRookIgnoringCheck(yStartingSquare, xStartingSquare, board));
                boolean castlingRights[]=board.getCopyOfCastlingRights();//stores castling rights - this wont be changed by any queen move
                int[][]newBoard;//stores copy of board after move made;
                int[]enPassantRightsNew=new int[0];//no square will be available for en passant after queen move
                int fullMoveNo=board.FULL_MOVE_NUMBER;//stores the full move number of the next move
                if(board.IS_BLACK_TURN_TO_PLAY_NEXT){
                    fullMoveNo++;
                }
                int halfMoveNo;//stores half move number
                for(int i=0;i<moves.size();i=i+2){//goes through all moves
                    xVal=i+1;
                    //generates new board
                    newBoard=board.getCopyOfBoard();
                    newBoard[moves.get(i)][moves.get(xVal)]=ChessBoardState.QUEEN_VALUE*ChessBoardState.WHITE_MULTIPLIER;//moves queen to new square
                    halfMoveNo=board.HALF_MOVE_CLOCK+1;
                    newBoard[yStartingSquare][xStartingSquare]=ChessBoardState.EMPTY_VALUE;//sets old square to empty
                    if(board.BOARD[moves.get(i)][moves.get(xVal)]!=ChessBoardState.EMPTY_VALUE){//checks to see if queen has captured a piece
                        halfMoveNo=0;
                    }
                    if(isInCheck(!board.IS_BLACK_TURN_TO_PLAY_NEXT, new ChessBoardState(newBoard, castlingRights, !board.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMoveNo, enPassantRightsNew))==true){//checks to make sure move does not result in own check getting in check
                        moves.remove(i);moves.remove(i);//removes invalid board states from list
                        i=i-2;
                    }
                }
                return moves;
            }
            case ChessBoardState.KING_VALUE*ChessBoardState.WHITE_MULTIPLIER:
            {//king move
                moves=getAllLegalMovesForKingIgnoringCheck(yStartingSquare, xStartingSquare, board);
                boolean []castlingRights;//stores castling rights of new boards;
                int[][]newBoard;//stores new board;
                int[]enPassantSquares;//stores squares for en passant
                int fullMoveNo;//stores the move number
                int halfMoveNo;//stores the half move no
                for(int i=0;i<moves.size();i=i+2){//loops through every move, updating the board with the move and seeing whether the move would be legal
                    xVal=i+1;
                    if(moves.get(xVal)==xStartingSquare+2 ){//checks for kingside castling
                        if(isInCheck(!board.IS_BLACK_TURN_TO_PLAY_NEXT, new ChessBoardState(board.BOARD,board.CASTLING_RIGHTS,!board.IS_BLACK_TURN_TO_PLAY_NEXT,board.FULL_MOVE_NUMBER,board.HALF_MOVE_CLOCK,board.SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT))==true){//king can't castle out of check
                            moves.remove(i);moves.remove(i);//removes invalid board states from list
                            i=i-2;
                        }else{
                            //checks whether king would be in check for either of the two squares moved
                            //checks for check for move one across
                            newBoard = board.getCopyOfBoard();//gets and updates board with king moving across
                            newBoard[yStartingSquare][xStartingSquare]=ChessBoardState.EMPTY_VALUE;newBoard[yStartingSquare][xStartingSquare+1]=ChessBoardState.KING_VALUE*ChessBoardState.WHITE_MULTIPLIER;
                            fullMoveNo=board.FULL_MOVE_NUMBER;//updates move number if required
                            halfMoveNo=board.HALF_MOVE_CLOCK+1;//updates half move number
                            enPassantSquares=new int[0];//no en passant square available as king has been moved
                            castlingRights=board.getCopyOfCastlingRights();//stores castling rights
                            if(board.IS_BLACK_TURN_TO_PLAY_NEXT){
                                castlingRights[2]=false;castlingRights[3]=false;
                                fullMoveNo++;
                            }else{
                                castlingRights[0]=false;castlingRights[1]=false;
                            }
                            if(isInCheck(!board.IS_BLACK_TURN_TO_PLAY_NEXT, new ChessBoardState(newBoard, castlingRights, !board.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMoveNo, enPassantSquares))==true){//king can't castle through squares where he would be in check
                                moves.remove(i);moves.remove(i);//removes invalid board states from list
                                i=i-2;
                            }else{
                                newBoard[yStartingSquare][xStartingSquare+1]=ChessBoardState.WHITE_MULTIPLIER*ChessBoardState.ROOK_VALUE;newBoard[yStartingSquare][7]=ChessBoardState.EMPTY_VALUE;
                                newBoard[yStartingSquare][xStartingSquare+2]=ChessBoardState.WHITE_MULTIPLIER*ChessBoardState.KING_VALUE;
                                if(isInCheck(!board.IS_BLACK_TURN_TO_PLAY_NEXT, new ChessBoardState(newBoard, castlingRights, !board.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMoveNo, enPassantSquares))==true){
                                    moves.remove(i);moves.remove(i);//removes invalid board states from list
                                    i=i-2;
                                }
                            }
                        }
                    }else if(moves.get(xVal)==xStartingSquare-2){//checks for queenside castling
                        if(isInCheck(!board.IS_BLACK_TURN_TO_PLAY_NEXT, new ChessBoardState(board.BOARD,board.CASTLING_RIGHTS,!board.IS_BLACK_TURN_TO_PLAY_NEXT,board.FULL_MOVE_NUMBER,board.HALF_MOVE_CLOCK,board.SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT))==true){//king can't castle out of check
                            moves.remove(i);moves.remove(i);//removes invalid board states from list
                            i=i-2;
                        }else{
                            //checks whether king would be in check for either of the two squares moved
                            //checks for check for move one across
                            newBoard = board.getCopyOfBoard();//gets and updates board with king moving across
                            newBoard[yStartingSquare][xStartingSquare]=ChessBoardState.EMPTY_VALUE;newBoard[yStartingSquare][xStartingSquare-1]=ChessBoardState.KING_VALUE*ChessBoardState.WHITE_MULTIPLIER;
                            fullMoveNo=board.FULL_MOVE_NUMBER;//updates move number if required
                            halfMoveNo=board.HALF_MOVE_CLOCK+1;//updates half move number
                            enPassantSquares=new int[0];//no en passant square available as king has been moved
                            castlingRights=board.getCopyOfCastlingRights();//stores castling rights
                            if(board.IS_BLACK_TURN_TO_PLAY_NEXT){
                                castlingRights[2]=false;castlingRights[3]=false;
                                fullMoveNo++;
                            }else{
                                castlingRights[0]=false;castlingRights[1]=false;
                            }
                            if(isInCheck(!board.IS_BLACK_TURN_TO_PLAY_NEXT, new ChessBoardState(newBoard, castlingRights, !board.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMoveNo, enPassantSquares))==true){//king can't castle trhough squares where he would be in check
                                moves.remove(i);moves.remove(i);//removes invalid board states from list
                                i=i-2;
                            }else{ 
                                newBoard[yStartingSquare][xStartingSquare-1]=ChessBoardState.WHITE_MULTIPLIER*ChessBoardState.ROOK_VALUE;newBoard[yStartingSquare][0]=ChessBoardState.EMPTY_VALUE;
                                newBoard[yStartingSquare][xStartingSquare-2]=ChessBoardState.WHITE_MULTIPLIER*ChessBoardState.KING_VALUE;
                                if(isInCheck(!board.IS_BLACK_TURN_TO_PLAY_NEXT, new ChessBoardState(newBoard, castlingRights, !board.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMoveNo, enPassantSquares))==true){
                                    moves.remove(i);moves.remove(i);//removes invalid board states from list
                                    i=i-2;
                                }
                            }
                        }
                    }else{//checks for moves where the king moves one square
                        newBoard=board.getCopyOfBoard();
                        //creates a new board where king has been moved
                        newBoard[yStartingSquare][xStartingSquare]=ChessBoardState.EMPTY_VALUE;newBoard[moves.get(i)][moves.get(xVal)]=ChessBoardState.KING_VALUE*ChessBoardState.WHITE_MULTIPLIER;
                        castlingRights=board.getCopyOfCastlingRights();
                        fullMoveNo=board.FULL_MOVE_NUMBER;
                        halfMoveNo=board.HALF_MOVE_CLOCK+1;
                        if(board.BOARD[moves.get(i)][moves.get(xVal)]!=ChessBoardState.EMPTY_VALUE){//checks to see if king has made a capture
                            halfMoveNo=0;
                        }
                        enPassantSquares = new int[0];//no en passant rights as move is made by king
                        if(board.IS_BLACK_TURN_TO_PLAY_NEXT){
                            fullMoveNo++;
                            castlingRights[2]=false;castlingRights[3]=false;
                        }else{
                            castlingRights[0]=false;castlingRights[1]=false;
                        }
                        if(isInCheck(!board.IS_BLACK_TURN_TO_PLAY_NEXT,new ChessBoardState(newBoard, castlingRights, !board.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMoveNo, enPassantSquares))==true){
                            moves.remove(i);moves.remove(i);//removes invalid board states from list
                            i=i-2;
                        }
                    }
                }
                return moves;//returns legal moves
            }
            case ChessBoardState.PAWN_VALUE*ChessBoardState.BLACK_MULTIPLIER:
            {
                moves=getAllLegalMovesForPawnIgnoringCheck(yStartingSquare, xStartingSquare, board);
                boolean castlingRights[]=board.getCopyOfCastlingRights();//stores castling rights
                int[][]newBoard;//stores copy of board after move made;
                int[]enPassantRightsNew;//stores possible en passant squares
                int fullMoveNo=board.FULL_MOVE_NUMBER;//stores the full move number of the next move
                if(board.IS_BLACK_TURN_TO_PLAY_NEXT){//updates move number and en passant rights
                    fullMoveNo++;
                }
                int halfMoveNo=0;//stores half move number -will always be zero for pawn moves
                for(int i=0;i<moves.size();i=i+2){//goes through all moves
                    xVal=i+1;
                    if(moves.get(i)==yStartingSquare+2){//checks for black pawn moving two squares forward
                        enPassantRightsNew=new int[]{yStartingSquare+1,xStartingSquare};
                        newBoard=board.getCopyOfBoard();
                        //adds move to board
                        newBoard[yStartingSquare][xStartingSquare]=ChessBoardState.EMPTY_VALUE;newBoard[yStartingSquare+2][xStartingSquare]=ChessBoardState.PAWN_VALUE*ChessBoardState.BLACK_MULTIPLIER;
                        if(isInCheck(!board.IS_BLACK_TURN_TO_PLAY_NEXT, new ChessBoardState(newBoard, castlingRights, !board.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMoveNo, enPassantRightsNew))==true){
                            moves.remove(i);moves.remove(i);//removes invalid board states from list
                            i=i-2;
                        }
                    }else if(moves.get(i)==yStartingSquare-2){//checks for white pawn moving two squares forward
                        enPassantRightsNew=new int[]{yStartingSquare-1,xStartingSquare};
                        newBoard=board.getCopyOfBoard();
                        //adds move to board
                        newBoard[yStartingSquare][xStartingSquare]=ChessBoardState.EMPTY_VALUE;newBoard[yStartingSquare-2][xStartingSquare]=ChessBoardState.PAWN_VALUE*ChessBoardState.BLACK_MULTIPLIER;
                        if(isInCheck(!board.IS_BLACK_TURN_TO_PLAY_NEXT, new ChessBoardState(newBoard, castlingRights, !board.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMoveNo, enPassantRightsNew))==true){
                            moves.remove(i);moves.remove(i);//removes invalid board states from list
                            i=i-2;
                        }
                    }else if((moves.get(i)==yStartingSquare-1 && moves.get(xVal)==xStartingSquare)||(moves.get(i)==yStartingSquare+1 && moves.get(xVal)==xStartingSquare)){//checks for white or black pawn moving one square forward
                        enPassantRightsNew= new int[0];//no en passant rights as piece is moving one square
                        newBoard=board.getCopyOfBoard();//gets copy of board
                        //adds move
                        newBoard[moves.get(i)][moves.get(xVal)]=ChessBoardState.PAWN_VALUE*ChessBoardState.BLACK_MULTIPLIER;newBoard[yStartingSquare][xStartingSquare]=ChessBoardState.EMPTY_VALUE;
                        if(isInCheck(!board.IS_BLACK_TURN_TO_PLAY_NEXT, new ChessBoardState(newBoard, castlingRights, !board.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMoveNo, enPassantRightsNew))==true){
                            moves.remove(i);moves.remove(i);//removes invalid board states from list
                            i=i-2;
                        }
                    }else{//must be a diagonal capture
                        enPassantRightsNew=new int[0];
                        if(board.BOARD[moves.get(i)][moves.get(xVal)]==ChessBoardState.EMPTY_VALUE){//en passant capture
                            newBoard=board.getCopyOfBoard();//gets copy of board
                            //adds move
                            newBoard[yStartingSquare][moves.get(xVal)]=ChessBoardState.EMPTY_VALUE;
                            newBoard[yStartingSquare][xStartingSquare]=ChessBoardState.EMPTY_VALUE;
                            newBoard[moves.get(i)][moves.get(xVal)]=ChessBoardState.PAWN_VALUE*ChessBoardState.BLACK_MULTIPLIER;
                            if(isInCheck(!board.IS_BLACK_TURN_TO_PLAY_NEXT, new ChessBoardState(newBoard, castlingRights, !board.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMoveNo, enPassantRightsNew))==true){
                                moves.remove(i);moves.remove(i);//removes invalid board states from list
                                i=i-2;
                            }
                        }else{//normal capture
                            newBoard=board.getCopyOfBoard();//gets copy of board
                            //adds move
                            newBoard[yStartingSquare][xStartingSquare]=ChessBoardState.EMPTY_VALUE;newBoard[moves.get(i)][moves.get(xVal)]=ChessBoardState.PAWN_VALUE*ChessBoardState.BLACK_MULTIPLIER;
                            if(isInCheck(!board.IS_BLACK_TURN_TO_PLAY_NEXT, new ChessBoardState(newBoard, castlingRights, !board.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMoveNo, enPassantRightsNew))==true){
                                moves.remove(i);moves.remove(i);//removes invalid board states from list
                                i=i-2;
                            }
                        }
                    }
                }
                return moves;
            }
            case ChessBoardState.ROOK_VALUE*ChessBoardState.BLACK_MULTIPLIER:
            {
                moves=getLegalMovesForRookIgnoringCheck(yStartingSquare, xStartingSquare, board);
                boolean castlingRights[]=board.getCopyOfCastlingRights();//stores castling rights
                int[][]newBoard;//stores copy of board after move made;
                int[]enPassantRightsNew=new int[0];//no square will be available for en passant after rook move
                int fullMoveNo=board.FULL_MOVE_NUMBER;//stores the full move number of the next move
                if(board.IS_BLACK_TURN_TO_PLAY_NEXT){//updates move number and en passant rights
                    fullMoveNo++;
                    if(yStartingSquare==0&&xStartingSquare==0){
                        castlingRights[1]=false;
                    }else if(yStartingSquare==0&&xStartingSquare==7){
                        castlingRights[0]=false;
                    }
                }else{
                    if(yStartingSquare==7&&xStartingSquare==0){
                        castlingRights[3]=false;
                    }else if(yStartingSquare==7&&xStartingSquare==7){
                        castlingRights[2]=false;
                    }
                }
                int halfMoveNo;//stores half move number
                for(int i=0;i<moves.size();i=i+2){//goes through all moves
                    xVal=i+1;
                    //generates new board
                    newBoard=board.getCopyOfBoard();
                    newBoard[moves.get(i)][moves.get(xVal)]=ChessBoardState.ROOK_VALUE*ChessBoardState.BLACK_MULTIPLIER;//moves castle to new square
                    halfMoveNo=board.HALF_MOVE_CLOCK+1;
                    newBoard[yStartingSquare][xStartingSquare]=ChessBoardState.EMPTY_VALUE;//sets old square to empty
                    if(board.BOARD[moves.get(i)][moves.get(xVal)]!=ChessBoardState.EMPTY_VALUE){//checks to see if rook has captured a piece
                        halfMoveNo=0;
                    }
                    if(isInCheck(!board.IS_BLACK_TURN_TO_PLAY_NEXT, new ChessBoardState(newBoard, castlingRights, !board.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMoveNo, enPassantRightsNew))==true){//checks to make sure move does not result in own check getting in check
                        moves.remove(i);moves.remove(i);//removes invalid board states from list
                        i=i-2;
                    }
                }
                return moves;
            }
            case ChessBoardState.KNIGHT_VALUE*ChessBoardState.BLACK_MULTIPLIER:
            {
                moves = getAllLegalMovesForKnightIgnoringCheck(yStartingSquare, xStartingSquare, board);
                boolean castlingRights[]=board.getCopyOfCastlingRights();//stores castling rights - this wont be changed by any knight move
                int[][]newBoard;//stores copy of board after move made;
                int[]enPassantRightsNew=new int[0];//no square will be available for en passant after knight move
                int fullMoveNo=board.FULL_MOVE_NUMBER;//stores the full move number of the next move
                if(board.IS_BLACK_TURN_TO_PLAY_NEXT){
                    fullMoveNo++;
                }
                int halfMoveNo;//stores half move number
                for(int i=0;i<moves.size();i=i+2){//goes through all moves
                    xVal=i+1;
                    //generates new board
                    newBoard=board.getCopyOfBoard();
                    newBoard[moves.get(i)][moves.get(xVal)]=ChessBoardState.KNIGHT_VALUE*ChessBoardState.BLACK_MULTIPLIER;//moves knight to new square
                    halfMoveNo=board.HALF_MOVE_CLOCK+1;
                    newBoard[yStartingSquare][xStartingSquare]=ChessBoardState.EMPTY_VALUE;//sets old square to empty
                    if(board.BOARD[moves.get(i)][moves.get(xVal)]!=ChessBoardState.EMPTY_VALUE){//checks to see if knight has captured a piece
                        halfMoveNo=0;
                    }
                    if(isInCheck(!board.IS_BLACK_TURN_TO_PLAY_NEXT, new ChessBoardState(newBoard, castlingRights, !board.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMoveNo, enPassantRightsNew))==true){//checks to make sure move does not result in own check getting in check
                        moves.remove(i);moves.remove(i);//removes invalid board states from list
                        i=i-2;
                    }
                }
                return moves;
            }
            case ChessBoardState.BISHOP_VALUE*ChessBoardState.BLACK_MULTIPLIER:
            {
                moves = getLegalMovesForBishopIgnoringCheck(yStartingSquare, xStartingSquare, board);
                boolean castlingRights[]=board.getCopyOfCastlingRights();//stores castling rights - this wont be changed by any bishop move
                int[][]newBoard;//stores copy of board after move made;
                int[]enPassantRightsNew=new int[0];//no square will be available for en passant after bishop move
                int fullMoveNo=board.FULL_MOVE_NUMBER;//stores the full move number of the next move
                if(board.IS_BLACK_TURN_TO_PLAY_NEXT){
                    fullMoveNo++;
                }
                int halfMoveNo;//stores half move number
                for(int i=0;i<moves.size();i=i+2){//goes through all moves
                    xVal=i+1;
                    //generates new board
                    newBoard=board.getCopyOfBoard();
                    newBoard[moves.get(i)][moves.get(xVal)]=ChessBoardState.BISHOP_VALUE*ChessBoardState.BLACK_MULTIPLIER;//moves bishop to new square
                    halfMoveNo=board.HALF_MOVE_CLOCK+1;
                    newBoard[yStartingSquare][xStartingSquare]=ChessBoardState.EMPTY_VALUE;//sets old square to empty
                    if(board.BOARD[moves.get(i)][moves.get(xVal)]!=ChessBoardState.EMPTY_VALUE){//checks to see if bishop has captured a piece
                        halfMoveNo=0;
                    }
                    if(isInCheck(!board.IS_BLACK_TURN_TO_PLAY_NEXT, new ChessBoardState(newBoard, castlingRights, !board.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMoveNo, enPassantRightsNew))==true){//checks to make sure move does not result in own check getting in check
                        moves.remove(i);moves.remove(i);//removes invalid board states from list
                        i=i-2;
                    }
                }
                return moves;
            }
            case ChessBoardState.QUEEN_VALUE*ChessBoardState.BLACK_MULTIPLIER:
            {
                moves=getLegalMovesForBishopIgnoringCheck(yStartingSquare, xStartingSquare, board);
                moves.addAll(getLegalMovesForRookIgnoringCheck(yStartingSquare, xStartingSquare, board));
                boolean castlingRights[]=board.getCopyOfCastlingRights();//stores castling rights - this wont be changed by any queen move
                int[][]newBoard;//stores copy of board after move made;
                int[]enPassantRightsNew=new int[0];//no square will be available for en passant after queen move
                int fullMoveNo=board.FULL_MOVE_NUMBER;//stores the full move number of the next move
                if(board.IS_BLACK_TURN_TO_PLAY_NEXT){
                    fullMoveNo++;
                }
                int halfMoveNo;//stores half move number
                for(int i=0;i<moves.size();i=i+2){//goes through all moves
                    xVal=i+1;
                    //generates new board
                    newBoard=board.getCopyOfBoard();
                    newBoard[moves.get(i)][moves.get(xVal)]=ChessBoardState.QUEEN_VALUE*ChessBoardState.BLACK_MULTIPLIER;//moves queen to new square
                    halfMoveNo=board.HALF_MOVE_CLOCK+1;
                    newBoard[yStartingSquare][xStartingSquare]=ChessBoardState.EMPTY_VALUE;//sets old square to empty
                    if(board.BOARD[moves.get(i)][moves.get(xVal)]!=ChessBoardState.EMPTY_VALUE){//checks to see if queen has captured a piece
                        halfMoveNo=0;
                    }
                    if(isInCheck(!board.IS_BLACK_TURN_TO_PLAY_NEXT, new ChessBoardState(newBoard, castlingRights, !board.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMoveNo, enPassantRightsNew))==true){//checks to make sure move does not result in own check getting in check
                        moves.remove(i);moves.remove(i);//removes invalid board states from list
                        i=i-2;
                    }
                }
                return moves;
            }
            default:
            {//king move
                moves=getAllLegalMovesForKingIgnoringCheck(yStartingSquare, xStartingSquare, board);
                boolean []castlingRights;//stores castling rights of new boards;
                int[][]newBoard;//stores new board;
                int[]enPassantSquares;//stores squares for en passant
                int fullMoveNo;//stores the move number
                int halfMoveNo;//stores the half move no
                for(int i=0;i<moves.size();i=i+2){//loops through every move, updating the board with the move and seeing whether the move would be legal
                    xVal=i+1;
                    if(moves.get(xVal)==xStartingSquare+2 ){//checks for kingside castling
                        if(isInCheck(!board.IS_BLACK_TURN_TO_PLAY_NEXT, new ChessBoardState(board.BOARD,board.CASTLING_RIGHTS,!board.IS_BLACK_TURN_TO_PLAY_NEXT,board.FULL_MOVE_NUMBER,board.HALF_MOVE_CLOCK,board.SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT))==true){//king can't castle out of check
                            moves.remove(i);moves.remove(i);//removes invalid board states from list
                            i=i-2;
                        }else{
                            //checks whether king would be in check for either of the two squares moved
                            //checks for check for move one across
                            newBoard = board.getCopyOfBoard();//gets and updates board with king moving across
                            newBoard[yStartingSquare][xStartingSquare]=ChessBoardState.EMPTY_VALUE;newBoard[yStartingSquare][xStartingSquare+1]=ChessBoardState.KING_VALUE*ChessBoardState.BLACK_MULTIPLIER;
                            fullMoveNo=board.FULL_MOVE_NUMBER;//updates move number if required
                            halfMoveNo=board.HALF_MOVE_CLOCK+1;//updates half move number
                            enPassantSquares=new int[0];//no en passant square available as king has been moved
                            castlingRights=board.getCopyOfCastlingRights();//stores castling rights
                            if(board.IS_BLACK_TURN_TO_PLAY_NEXT){
                                castlingRights[2]=false;castlingRights[3]=false;
                                fullMoveNo++;
                            }else{
                                castlingRights[0]=false;castlingRights[1]=false;
                            }
                            if(isInCheck(!board.IS_BLACK_TURN_TO_PLAY_NEXT, new ChessBoardState(newBoard, castlingRights, !board.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMoveNo, enPassantSquares))==true){//king can't castle through squares where he would be in check
                                moves.remove(i);moves.remove(i);//removes invalid board states from list
                                i=i-2;
                            }else{
                                newBoard[yStartingSquare][xStartingSquare+1]=ChessBoardState.BLACK_MULTIPLIER*ChessBoardState.ROOK_VALUE;newBoard[yStartingSquare][7]=ChessBoardState.EMPTY_VALUE;
                                newBoard[yStartingSquare][xStartingSquare+2]=ChessBoardState.BLACK_MULTIPLIER*ChessBoardState.KING_VALUE;
                                if(isInCheck(!board.IS_BLACK_TURN_TO_PLAY_NEXT, new ChessBoardState(newBoard, castlingRights, !board.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMoveNo, enPassantSquares))==true){
                                    moves.remove(i);moves.remove(i);//removes invalid board states from list
                                    i=i-2;
                                }
                            }
                        }
                    }else if(moves.get(xVal)==xStartingSquare-2){//checks for queenside castling
                        if(isInCheck(!board.IS_BLACK_TURN_TO_PLAY_NEXT, new ChessBoardState(board.BOARD,board.CASTLING_RIGHTS,!board.IS_BLACK_TURN_TO_PLAY_NEXT,board.FULL_MOVE_NUMBER,board.HALF_MOVE_CLOCK,board.SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT))==true){//king can't castle out of check
                            moves.remove(i);moves.remove(i);//removes invalid board states from list
                            i=i-2;
                        }else{
                            //checks whether king would be in check for either of the two squares moved
                            //checks for check for move one across
                            newBoard = board.getCopyOfBoard();//gets and updates board with king moving across
                            newBoard[yStartingSquare][xStartingSquare]=ChessBoardState.EMPTY_VALUE;newBoard[yStartingSquare][xStartingSquare-1]=ChessBoardState.KING_VALUE*ChessBoardState.BLACK_MULTIPLIER;
                            fullMoveNo=board.FULL_MOVE_NUMBER;//updates move number if required
                            halfMoveNo=board.HALF_MOVE_CLOCK+1;//updates half move number
                            enPassantSquares=new int[0];//no en passant square available as king has been moved
                            castlingRights=board.getCopyOfCastlingRights();//stores castling rights
                            if(board.IS_BLACK_TURN_TO_PLAY_NEXT){
                                castlingRights[2]=false;castlingRights[3]=false;
                                fullMoveNo++;
                            }else{
                                castlingRights[0]=false;castlingRights[1]=false;
                            }
                            if(isInCheck(!board.IS_BLACK_TURN_TO_PLAY_NEXT, new ChessBoardState(newBoard, castlingRights, !board.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMoveNo, enPassantSquares))==true){//king can't castle trhough squares where he would be in check
                                moves.remove(i);moves.remove(i);//removes invalid board states from list
                                i=i-2;
                            }else{ 
                                newBoard[yStartingSquare][xStartingSquare-1]=ChessBoardState.BLACK_MULTIPLIER*ChessBoardState.ROOK_VALUE;newBoard[yStartingSquare][0]=ChessBoardState.EMPTY_VALUE;
                                newBoard[yStartingSquare][xStartingSquare-2]=ChessBoardState.BLACK_MULTIPLIER*ChessBoardState.KING_VALUE;
                                if(isInCheck(!board.IS_BLACK_TURN_TO_PLAY_NEXT, new ChessBoardState(newBoard, castlingRights, !board.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMoveNo, enPassantSquares))==true){
                                    moves.remove(i);moves.remove(i);//removes invalid board states from list
                                    i=i-2;
                                }
                            }
                        }
                    }else{//checks for moves where the king moves one square
                        newBoard=board.getCopyOfBoard();
                        //creates a new board where king has been moved
                        newBoard[yStartingSquare][xStartingSquare]=ChessBoardState.EMPTY_VALUE;newBoard[moves.get(i)][moves.get(xVal)]=ChessBoardState.KING_VALUE*ChessBoardState.BLACK_MULTIPLIER;
                        castlingRights=board.getCopyOfCastlingRights();
                        fullMoveNo=board.FULL_MOVE_NUMBER;
                        halfMoveNo=board.HALF_MOVE_CLOCK+1;
                        if(board.BOARD[moves.get(i)][moves.get(xVal)]!=ChessBoardState.EMPTY_VALUE){//checks to see if king has made a capture
                            halfMoveNo=0;
                        }
                        enPassantSquares = new int[0];//no en passant rights as move is made by king
                        if(board.IS_BLACK_TURN_TO_PLAY_NEXT){
                            fullMoveNo++;
                            castlingRights[2]=false;castlingRights[3]=false;
                        }else{
                            castlingRights[0]=false;castlingRights[1]=false;
                        }
                        if(isInCheck(!board.IS_BLACK_TURN_TO_PLAY_NEXT,new ChessBoardState(newBoard, castlingRights, !board.IS_BLACK_TURN_TO_PLAY_NEXT, fullMoveNo, halfMoveNo, enPassantSquares))==true){
                            moves.remove(i);moves.remove(i);//removes invalid board states from list
                            i=i-2;
                        }
                    }
                }
                return moves;//returns legal moves
            }
        }
    }
    /**
     * Checks to see if king of input colour is in check in the current state. This method is used as part of the process to obtain all legal moves.
     * Must be called on the correct king given who is about to move.
     * @param isKingWhite The colour of the king to check
     * @param state The board state to check
     * @return True is returned if the king chosen is in check and false is returned otherwise
     */
    public static boolean isInCheck (boolean isKingWhite,ChessBoardState state){
        //checks to see whether the king square is being targeted by pieces of the opposing colour
        int yCoordKing=-1;int xCoordKing=-1;//stores the coordinate of the king
        ArrayList<Integer> allAvailableMoves=new ArrayList<>(70);//stores any array list storing all legal moves
        //int[][]allAvailableMoves = new int[0][2];//stores all moves that can be got from a square
        if(isKingWhite){
            for(int y=0;y<8;y++){//loops through board to find king and get all available moves from other colour
                for(int x=0;x<8;x++){
                    switch (state.BOARD[y][x]) {
                        case ChessBoardState.PAWN_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                            allAvailableMoves.addAll(getAllLegalMovesForPawnIgnoringCheck(y, x, state));
                            break;
                        case ChessBoardState.ROOK_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                            allAvailableMoves.addAll(getLegalMovesForRookIgnoringCheck(y, x, state));
                            break;
                        case ChessBoardState.KNIGHT_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                            allAvailableMoves.addAll(getAllLegalMovesForKnightIgnoringCheck(y, x, state));
                            break;
                        case ChessBoardState.BISHOP_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                            allAvailableMoves.addAll(getLegalMovesForBishopIgnoringCheck(y, x, state));
                            break;
                        case ChessBoardState.QUEEN_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                            allAvailableMoves.addAll(getLegalMovesForRookIgnoringCheck(y, x, state));allAvailableMoves.addAll(getLegalMovesForBishopIgnoringCheck(y, x, state));
                            break;
                        case ChessBoardState.KING_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                            allAvailableMoves.addAll(getAllLegalMovesForKingIgnoringCheck(y, x, state));
                            break;
                        case ChessBoardState.KING_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                            yCoordKing=y;
                            xCoordKing=x;
                            break;
                        default:
                            break;
                    }
                }
            }
            
        }else{
            for(int y=0;y<8;y++){//loops through board to find king and get all available moves from other colour
                for(int x=0;x<8;x++){
                    switch (state.BOARD[y][x]) {
                        case ChessBoardState.PAWN_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                            allAvailableMoves.addAll(getAllLegalMovesForPawnIgnoringCheck(y, x, state));
                            break;
                        case ChessBoardState.ROOK_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                            allAvailableMoves.addAll(getLegalMovesForRookIgnoringCheck(y, x, state));
                            break;
                        case ChessBoardState.KNIGHT_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                            allAvailableMoves.addAll(getAllLegalMovesForKnightIgnoringCheck(y, x, state));
                            break;
                        case ChessBoardState.BISHOP_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                            allAvailableMoves.addAll(getLegalMovesForBishopIgnoringCheck(y, x, state));
                            break;
                        case ChessBoardState.QUEEN_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                            allAvailableMoves.addAll(getLegalMovesForRookIgnoringCheck(y, x, state));allAvailableMoves.addAll(getLegalMovesForBishopIgnoringCheck(y, x, state));
                            break;
                        case ChessBoardState.KING_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                            allAvailableMoves.addAll(getAllLegalMovesForKingIgnoringCheck(y, x, state));
                            break;
                        case ChessBoardState.KING_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                            yCoordKing=y;
                            xCoordKing=x;
                            break;
                        default:
                            break;
                    }
                }
            }            
        }
        for(int move=0;move<allAvailableMoves.size();move=move+2){//loops through all moves to see if king is in check 
            if(allAvailableMoves.get(move)==yCoordKing && allAvailableMoves.get(move+1)==xCoordKing){
                return true;
            }
        }
        return false;
    }
    /**
     * Gets all legal moves for king in square. Note king must be of correct colour. Castling is represented as the king moving two squares to the left or right.
     * NOTE: You can't castle out of check but as this method ignores check, this will need to be handled.
     * @param ySquare The y index of the king
     * @param xSquare The x index of the king
     * @param state The board state
     * @return The moves
     */
    private static ArrayList <Integer> getAllLegalMovesForKingIgnoringCheck(int ySquare, int xSquare ,ChessBoardState state){
        ArrayList<Integer>moves = new ArrayList<>();//array list storing all moves. Y then x moves are added alternatingly 
        if(state.IS_BLACK_TURN_TO_PLAY_NEXT){//if the king is black
            //checks for move one square up
            if(ySquare!=0){
                if(state.BOARD[ySquare-1][xSquare]>=0){//checks if square is empty or of opposite colour
                    //moves=addToEndOfArray(moves, new int[]{ySquare-1,xSquare});
                    moves.add(ySquare-1);moves.add(xSquare);
                }
            }
            //checks for one move down
            if(ySquare!=7){
                if(state.BOARD[ySquare+1][xSquare]>=0){//checks if square is empty or of opposite colour
                    //moves=addToEndOfArray(moves, new int[]{ySquare+1,xSquare});
                    moves.add(ySquare+1);moves.add(xSquare);
                }                
            }
            //checks for one move to right
            if(xSquare!=7){
                if(state.BOARD[ySquare][xSquare+1]>=0){//checks if square is empty or of opposite colour
                   // moves=addToEndOfArray(moves, new int[]{ySquare,xSquare+1});
                    moves.add(ySquare);moves.add(xSquare+1);
                } 
                if(ySquare!=0 && state.BOARD[ySquare-1][xSquare+1]>=0){//checks for right and up
                    //moves=addToEndOfArray(moves, new int[]{ySquare-1,xSquare+1});
                    moves.add(ySquare-1);moves.add(xSquare+1);
                }
                if(ySquare!=7 && state.BOARD[ySquare+1][xSquare+1]>=0){//checks for right and down
                    //moves=addToEndOfArray(moves, new int[]{ySquare+1,xSquare+1});
                    moves.add(ySquare+1);moves.add(xSquare+1);
                }
            }
            //checks for one move to left
            if(xSquare!=0){
                if(state.BOARD[ySquare][xSquare-1]>=0){//checks if square is empty or of opposite colour
                   // moves=addToEndOfArray(moves, new int[]{ySquare,xSquare-1});
                    moves.add(ySquare);moves.add(xSquare-1);
                }       
                if(ySquare!=0 && state.BOARD[ySquare-1][xSquare-1]>=0){//checks for left and up
                    //moves=addToEndOfArray(moves, new int[]{ySquare-1,xSquare-1});
                    moves.add(ySquare-1);moves.add(xSquare-1);
                }
                if(ySquare!=7 && state.BOARD[ySquare+1][xSquare-1]>=0){//checks for left and down
                    //moves=addToEndOfArray(moves, new int[]{ySquare+1,xSquare-1});
                    moves.add(ySquare+1);moves.add(xSquare-1);
                }
            }
            //checks for kingside castling
            if(state.CASTLING_RIGHTS[2]&&state.BOARD[0][5]==ChessBoardState.EMPTY_VALUE&&state.BOARD[0][6]==ChessBoardState.EMPTY_VALUE){
                //moves=addToEndOfArray(moves, new int[]{ySquare,xSquare+2});
                moves.add(ySquare);moves.add(xSquare+2);
            }
            //checks for queenside castling
            if(state.CASTLING_RIGHTS[3]&&state.BOARD[0][3]==ChessBoardState.EMPTY_VALUE&&state.BOARD[0][2]==ChessBoardState.EMPTY_VALUE&&state.BOARD[0][1]==ChessBoardState.EMPTY_VALUE){
                //moves=addToEndOfArray(moves, new int[]{ySquare,xSquare-2});
                moves.add(ySquare);moves.add(xSquare-2);
            }
        }else{//if the king is white
            //checks for move one square up
            if(ySquare!=0){
                if(state.BOARD[ySquare-1][xSquare]<=0){//checks if square is empty or of opposite colour
                    //moves=addToEndOfArray(moves, new int[]{ySquare-1,xSquare});
                    moves.add(ySquare-1);moves.add(xSquare);
                }
            }
            //checks for one move down
            if(ySquare!=7){
                if(state.BOARD[ySquare+1][xSquare]<=0){//checks if square is empty or of opposite colour
                    //moves=addToEndOfArray(moves, new int[]{ySquare+1,xSquare});
                    moves.add(ySquare+1);moves.add(xSquare);
                }                
            }
            //checks for one move to right
            if(xSquare!=7){
                if(state.BOARD[ySquare][xSquare+1]<=0){//checks if square is empty or of opposite colour
                    //moves=addToEndOfArray(moves, new int[]{ySquare,xSquare+1});
                    moves.add(ySquare);moves.add(xSquare+1);
                }  
                if(ySquare!=0 && state.BOARD[ySquare-1][xSquare+1]<=0){//checks for right and up
                    //moves=addToEndOfArray(moves, new int[]{ySquare-1,xSquare+1});
                    moves.add(ySquare-1);moves.add(xSquare+1);
                }
                if(ySquare!=7 && state.BOARD[ySquare+1][xSquare+1]<=0){//checks for right and down
                    //moves=addToEndOfArray(moves, new int[]{ySquare+1,xSquare+1});
                    moves.add(ySquare+1);moves.add(xSquare+1);
                }
            }
            //checks for one move to left
            if(xSquare!=0){
                if(state.BOARD[ySquare][xSquare-1]<=0){//checks if square is empty or of opposite colour
                    //moves=addToEndOfArray(moves, new int[]{ySquare,xSquare-1});
                    moves.add(ySquare);moves.add(xSquare-1);
                }    
                if(ySquare!=0 && state.BOARD[ySquare-1][xSquare-1]<=0){//checks for left and up
                    //moves=addToEndOfArray(moves, new int[]{ySquare-1,xSquare-1});
                    moves.add(ySquare-1);moves.add(xSquare-1);
                }
                if(ySquare!=7 && state.BOARD[ySquare+1][xSquare-1]<=0){//checks for left and down
                    //moves=addToEndOfArray(moves, new int[]{ySquare+1,xSquare-1});
                    moves.add(ySquare+1);moves.add(xSquare-1);
                }
            }
            //checks for kingside castling
            if(state.CASTLING_RIGHTS[0]&&state.BOARD[7][5]==ChessBoardState.EMPTY_VALUE&&state.BOARD[7][6]==ChessBoardState.EMPTY_VALUE){
                //moves=addToEndOfArray(moves, new int[]{ySquare,xSquare+2});
                moves.add(ySquare);moves.add(xSquare+2);
            }
            //checks for queenside castling
            if(state.CASTLING_RIGHTS[1]&&state.BOARD[7][3]==ChessBoardState.EMPTY_VALUE&&state.BOARD[7][2]==ChessBoardState.EMPTY_VALUE&&state.BOARD[7][1]==ChessBoardState.EMPTY_VALUE){
                //moves=addToEndOfArray(moves, new int[]{ySquare,xSquare-2});
                moves.add(ySquare);moves.add(xSquare-2);
            }            
        }
        return moves;
    }
    /**
     * Gets all legal moves for pawn at the square input. Note check is ignored and pawn promotion is treated as the pawn
     * sitting on the final rank. En passant is supported but must be checked carefully. Note: must be called on pawn of the right colour given the board state
     * @param ySquare The y index of the pawn
     * @param xSquare The x index of the pawn
     * @param state The board state
     * @return All moves
     */
    private static ArrayList <Integer> getAllLegalMovesForPawnIgnoringCheck(int ySquare, int xSquare, ChessBoardState state){
        ArrayList<Integer> moves = new ArrayList<>(8);
        //checks to see if pawn can move forward twice
        if(state.IS_BLACK_TURN_TO_PLAY_NEXT&&ySquare==1){//checks to see if black pawn can move forward two moves
            if(state.BOARD[ySquare+2][xSquare]==ChessBoardState.EMPTY_VALUE&& state.BOARD[ySquare+1][xSquare]==ChessBoardState.EMPTY_VALUE){
                moves.add(ySquare+2);moves.add(xSquare);
            }
        }else if(!state.IS_BLACK_TURN_TO_PLAY_NEXT&&ySquare==6){//checks to see if white can move forward two
            if(state.BOARD[ySquare-2][xSquare]==ChessBoardState.EMPTY_VALUE && state.BOARD[ySquare-1][xSquare]==ChessBoardState.EMPTY_VALUE){
                moves.add(ySquare-2);moves.add(xSquare);
            }
        }
        //checks to see if pawn can move forward once
        if(state.IS_BLACK_TURN_TO_PLAY_NEXT&&ySquare!=7){//the pawn is black
            if(state.BOARD[ySquare+1][xSquare]==ChessBoardState.EMPTY_VALUE){//checks to move forward once
                moves.add(ySquare+1);moves.add(xSquare);
            }
            if(xSquare!=7){//checks for diagonal capture to the right as black pawn
                if(state.BOARD[ySquare+1][xSquare+1]>0){//checks to see if there is a white piece on the diagnoal
                    moves.add(ySquare+1);moves.add(xSquare+1);
                }
            }
            if(xSquare!=0){//checks for left diagnoal capture
                if(state.BOARD[ySquare+1][xSquare-1]>0){//checks to see if white piece is on diagonal
                    moves.add(ySquare+1);moves.add(xSquare-1);
                }
            }
        }else if(!state.IS_BLACK_TURN_TO_PLAY_NEXT&&ySquare!=0){
            if(state.BOARD[ySquare-1][xSquare]==ChessBoardState.EMPTY_VALUE){
                moves.add(ySquare-1);moves.add(xSquare);
            }
            if(xSquare!=7){//checks for diagonal capture to the right as white pawn
                if(state.BOARD[ySquare-1][xSquare+1]<0){//checks to see if there is a black piece on the diagnoal
                    moves.add(ySquare-1);moves.add(xSquare+1);
                }
            }
            if(xSquare!=0){//checks for left diagnoal capture
                if(state.BOARD[ySquare-1][xSquare-1]<0){//checks to see if black piece is on diagonal
                    moves.add(ySquare-1);moves.add(xSquare-1);
                }
            }
        }
        //checks for en passant captures
        if(state.SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT.length!=0){//checks for en passant
            if(state.IS_BLACK_TURN_TO_PLAY_NEXT){//checks for en passant captures that can be made by black pawn
                if((state.SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT[0]==ySquare+1) && ((state.SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT[1]==xSquare-1)||(state.SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT[1]==xSquare+1))){
                    moves.add(state.SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT[0]);moves.add(state.SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT[1]);
                }
            }else{
                if((state.SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT[0]==ySquare-1) && ((state.SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT[1]==xSquare-1)||(state.SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT[1]==xSquare+1))){
                    moves.add(state.SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT[0]);moves.add(state.SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT[1]);
                }                
            }
        }
        return moves;
    }
    /**
     * Adds new records to the end of an array
     * @param currentArray The current array
     * @param newRecords The new records
     * @param arrayWidth The array width
     * @return The new array
     */
    private static int[][]addToEndOfArray(int[][]currentArray,int[][]newRecords, int arrayWidth){
        int[][]newArray=new int[currentArray.length+newRecords.length][arrayWidth];
        for(int i=0;i<currentArray.length;i++){
            for(int x=0;x<arrayWidth;x++){
                newArray[i][x]=currentArray[i][x];
            }
        }
        for(int i=0;i<newRecords.length;i++){
            for(int x=0;x<arrayWidth;x++){
                newArray[i+currentArray.length][x]=newRecords[i][x];
            }
        }
        return newArray;
    }
    /**
     * Adds new record to two 2d array. This record is appended to the bottom of the new array. Note newRecord must be of the same width as the current array
     * @param currentArray the array
     * @param newRecord the record to add
     * @return the new array
     */
    private static int[][] addToEndOfArray(int[][]currentArray,int[]newRecord){
        int[][]newArray=new int[currentArray.length+1][newRecord.length];//creates new array one longer than last one
        for(int i=0;i<newRecord.length;i++){//adds new record to end of new array
            newArray[currentArray.length][i]=newRecord[i];
        }
        for(int i=0;i<currentArray.length;i++){//copies old array items to new array
            for(int x=0;x<newRecord.length;x++){
                newArray[i][x]=currentArray[i][x];
            }
        }
        return newArray;//returns new array
    }
    /**
     * Gets all legal moves for a knight ignoring check. Note knight in square MUST be of the same colour as the colour that moves next
     * @param ySquare The y coordinate of the knight
     * @param xSquare The x coordinate of the knight
     * @param state The board state
     * @return All moves
     */
    private static ArrayList <Integer> getAllLegalMovesForKnightIgnoringCheck(int ySquare,int xSquare, ChessBoardState state){
        ArrayList<Integer>moves=new ArrayList<>();
        /* 8 squares to check
        y+2 x+1,y+1 x+2,y-1 x+2,y-2 x+1,y-2 x-1,y-1 x-2,y+1 x-2,y+2 x-2
        */
        //check for y+1 and y+2 moves
        if(ySquare<=6){//check if y+1 is a legal square
            if(xSquare<=5){//checking for Y+1 and x+2
                if((state.IS_BLACK_TURN_TO_PLAY_NEXT&&state.BOARD[ySquare+1][xSquare+2]>=0)||(!state.IS_BLACK_TURN_TO_PLAY_NEXT&&state.BOARD[ySquare+1][xSquare+2]<=0)){
                    moves.add(ySquare+1);moves.add(xSquare+2);
                }
            }
            if(xSquare>=2){//checking for y+1 and x-2
                if((state.IS_BLACK_TURN_TO_PLAY_NEXT&&state.BOARD[ySquare+1][xSquare-2]>=0)||(!state.IS_BLACK_TURN_TO_PLAY_NEXT&&state.BOARD[ySquare+1][xSquare-2]<=0)){
                    moves.add(ySquare+1);moves.add(xSquare-2);
                }                
            }
            if(ySquare<=5){//checking for y+2
                if(xSquare>=1){//checking for Y+2 and x-1
                    if((state.IS_BLACK_TURN_TO_PLAY_NEXT&&state.BOARD[ySquare+2][xSquare-1]>=0)||(!state.IS_BLACK_TURN_TO_PLAY_NEXT&&state.BOARD[ySquare+2][xSquare-1]<=0)){
                        moves.add(ySquare+2);moves.add(xSquare-1);
                    }                   
                }
                if(xSquare<=6){//checking for y+2 and x+1
                    if((state.IS_BLACK_TURN_TO_PLAY_NEXT&&state.BOARD[ySquare+2][xSquare+1]>=0)||(!state.IS_BLACK_TURN_TO_PLAY_NEXT&&state.BOARD[ySquare+2][xSquare+1]<=0)){
                        moves.add(ySquare+2);moves.add(xSquare+1);
                    }                     
                }
            }
        }
        if(ySquare>=1){//checking for y-1
            if(xSquare<=5){//checking for Y-1 and x+2
                if((state.IS_BLACK_TURN_TO_PLAY_NEXT&&state.BOARD[ySquare-1][xSquare+2]>=0)||(!state.IS_BLACK_TURN_TO_PLAY_NEXT&&state.BOARD[ySquare-1][xSquare+2]<=0)){
                    moves.add(ySquare-1);moves.add(xSquare+2);
                }               
            }
            if(xSquare>=2){//checking for y-1 and x-2
                if((state.IS_BLACK_TURN_TO_PLAY_NEXT&&state.BOARD[ySquare-1][xSquare-2]>=0)||(!state.IS_BLACK_TURN_TO_PLAY_NEXT&&state.BOARD[ySquare-1][xSquare-2]<=0)){
                    moves.add(ySquare-1);moves.add(xSquare-2);
                }                    
            }
            if(ySquare>=2){//checking for y-2
                if(xSquare>=1){//checking for Y-2 and x-1
                    if((state.IS_BLACK_TURN_TO_PLAY_NEXT&&state.BOARD[ySquare-2][xSquare-1]>=0)||(!state.IS_BLACK_TURN_TO_PLAY_NEXT&&state.BOARD[ySquare-2][xSquare-1]<=0)){
                        moves.add(ySquare-2);moves.add(xSquare-1);
                    }                     
                }
                if(xSquare<=6){//checking for y-2 and x+1
                    if((state.IS_BLACK_TURN_TO_PLAY_NEXT&&state.BOARD[ySquare-2][xSquare+1]>=0)||(!state.IS_BLACK_TURN_TO_PLAY_NEXT&&state.BOARD[ySquare-2][xSquare+1]<=0)){
                        moves.add(ySquare-2);moves.add(xSquare+1);
                    }                       
                }
            }            
        }
        return moves;
    }
    /**
     * Gets all legal moves for rook (or queen) in position, ignoring check. This method must be called in a piece of the right colour
     * @param ySquare The y coordinate of the rook (or queen).
     * @param xSquare The x coordinate of the rook (or queen).
     * @param state The board state
     * @return An ArrayList containing all legal moves of width 2. If no moves are found, the ArrayList is of length 0.
     */
    private static ArrayList <Integer> getLegalMovesForRookIgnoringCheck(int ySquare, int xSquare, ChessBoardState state){
        ArrayList<Integer> moves = new ArrayList<>();//stores all legal moves for the rook
        //checks for moves up
        for(int inc=1;inc<=ySquare;inc++){
            if(state.BOARD[ySquare-inc][xSquare]==ChessBoardState.EMPTY_VALUE){//if square is empty
                moves.add(ySquare-inc);moves.add(xSquare);
            }else if((state.IS_BLACK_TURN_TO_PLAY_NEXT&&state.BOARD[ySquare-inc][xSquare]<0) || (!state.IS_BLACK_TURN_TO_PLAY_NEXT && state.BOARD[ySquare-inc][xSquare]>0)){//if collides with piece of same colour
                break;
            }else{//if collides with piece of different colours
                moves.add(ySquare-inc);moves.add(xSquare);
                break;
            }
        }
        //checks for moves down
        for(int inc=1;inc<=7-ySquare;inc++){
            if(state.BOARD[ySquare+inc][xSquare]==ChessBoardState.EMPTY_VALUE){//if square is empty
                moves.add(ySquare+inc);moves.add(xSquare);
            }else if((state.IS_BLACK_TURN_TO_PLAY_NEXT&&state.BOARD[ySquare+inc][xSquare]<0) || (!state.IS_BLACK_TURN_TO_PLAY_NEXT && state.BOARD[ySquare+inc][xSquare]>0)){//if collides with piece of same colour
                break;
            }else{//if collides with piece of different colours
                moves.add(ySquare+inc);moves.add(xSquare);
                break;
            }
        }
        //checks for moves to right
        for(int inc=1;inc<=7-xSquare;inc++){
            if(state.BOARD[ySquare][xSquare+inc]==ChessBoardState.EMPTY_VALUE){//if square is empty
                moves.add(ySquare);moves.add(xSquare+inc);
            }else if((state.IS_BLACK_TURN_TO_PLAY_NEXT&&state.BOARD[ySquare][xSquare+inc]<0) || (!state.IS_BLACK_TURN_TO_PLAY_NEXT && state.BOARD[ySquare][xSquare+inc]>0)){//if collides with piece of same colour
                break;
            }else{//if collides with piece of different colours
                moves.add(ySquare);moves.add(xSquare+inc);
                break;
            }
        }
        //checks for moves to left
        for(int inc=1;inc<=xSquare;inc++){
            if(state.BOARD[ySquare][xSquare-inc]==ChessBoardState.EMPTY_VALUE){//if square is empty
                moves.add(ySquare);moves.add(xSquare-inc);
            }else if((state.IS_BLACK_TURN_TO_PLAY_NEXT&&state.BOARD[ySquare][xSquare-inc]<0) || (!state.IS_BLACK_TURN_TO_PLAY_NEXT && state.BOARD[ySquare][xSquare-inc]>0)){//if collides with piece of same colour
                break;
            }else{//if collides with piece of different colours
                moves.add(ySquare);moves.add(xSquare-inc);
                break;
            }
        }
        return moves;
    }
    /**
     * Gets all legal moves for the bishop from the current state, ignoring whether the king is in check. This method must be called on a bishop (or queen) of the correct colour.
     * @param ySquare The y index of the bishop (or queen)
     * @param xSquare The x index of the bishop (or queen)
     * @param state The chess board state to get the legal moves from
     * @return An ArrayList containing all legal moves of width 2. If no moves are found, the ArrayList is of length 0.
     */
    private static ArrayList <Integer> getLegalMovesForBishopIgnoringCheck(int ySquare, int xSquare, ChessBoardState state){
        ArrayList<Integer> moves = new ArrayList<>();
        //check for moving down and to the right
        for(int inc=1;inc<=7-Math.max(ySquare, xSquare);inc++){
            if(state.BOARD[ySquare+inc][xSquare+inc]==ChessBoardState.EMPTY_VALUE){
                moves.add(ySquare+inc);moves.add(xSquare+inc);
            }else if((state.IS_BLACK_TURN_TO_PLAY_NEXT&&state.BOARD[ySquare+inc][xSquare+inc]<0)||(!state.IS_BLACK_TURN_TO_PLAY_NEXT&&state.BOARD[ySquare+inc][xSquare+inc]>0)){//if it collides with own piece
                break;
            }else{//collides with enemy piece
                moves.add(ySquare+inc);moves.add(xSquare+inc);
                break;
            }
        }
        //checking for move down and left
        for(int inc=1;inc<=Math.min((7-ySquare),xSquare);inc++){
            if(state.BOARD[ySquare+inc][xSquare-inc]==ChessBoardState.EMPTY_VALUE){
                moves.add(ySquare+inc);moves.add(xSquare-inc);
            }else if((state.IS_BLACK_TURN_TO_PLAY_NEXT&&state.BOARD[ySquare+inc][xSquare-inc]<0)||(!state.IS_BLACK_TURN_TO_PLAY_NEXT&&state.BOARD[ySquare+inc][xSquare-inc]>0)){//if it collides with own piece
                break;
            }else{//collides with enemy piece
               moves.add(ySquare+inc);moves.add(xSquare-inc);
                break;
            }
        }
        //checking for move up and right
        for(int inc=1;inc<=Math.min(ySquare,(7-xSquare));inc++){
            if(state.BOARD[ySquare-inc][xSquare+inc]==ChessBoardState.EMPTY_VALUE){
                moves.add(ySquare-inc);moves.add(xSquare+inc);
            }else if((state.IS_BLACK_TURN_TO_PLAY_NEXT&&state.BOARD[ySquare-inc][xSquare+inc]<0)||(!state.IS_BLACK_TURN_TO_PLAY_NEXT&&state.BOARD[ySquare-inc][xSquare+inc]>0)){//if it collides with own piece
                break;
            }else{//collides with enemy piece
                moves.add(ySquare-inc);moves.add(xSquare+inc);
                break;
            }
        }
        //checking for up and left
        for(int inc=1;inc<=Math.min(ySquare,xSquare);inc++){
            if(state.BOARD[ySquare-inc][xSquare-inc]==ChessBoardState.EMPTY_VALUE){
               moves.add(ySquare-inc);moves.add(xSquare-inc);
            }else if((state.IS_BLACK_TURN_TO_PLAY_NEXT&&state.BOARD[ySquare-inc][xSquare-inc]<0)||(!state.IS_BLACK_TURN_TO_PLAY_NEXT&&state.BOARD[ySquare-inc][xSquare-inc]>0)){//if it collides with own piece
                break;
            }else{//collides with enemy piece
                moves.add(ySquare-inc);moves.add(xSquare-inc);
                break;
            }
        }
        return moves;
    }
    /**
     * Returns the current board state.
     * @return The current board state
     */
    public ChessBoardState getCurrentState(){
        return states[states.length-1];
    }  
}
