/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package philipm.cs5.software.development.user.system;


/**
 * Creates a thread which gets the computer move in the chess game. This is done as its own thread to allow the form to render whilst the computer move is retrieved.
 * @author mortimer
 */
public class GetComputerMove extends Thread{
    ChessBoardOffline b;//stores the chess board form
    ChessGame game;//stores the chess game
    int lvl;//stores the computer level
    /**
     * Creates a new object that gets and make the computer move in an offline chess game.
     * @param gam The chess game object for the game in question
     * @param board The chess board form for which the move is being retrieved.
     * @param lvl The difficulty level of the computer
     */
    public GetComputerMove(ChessGame gam, ChessBoardOffline board,int lvl){
        //sets the parameters as object variables
        b=board;game=gam;this.lvl=lvl;
    }
    @Override
    /**
     * Gets the computer move and adds it to the chess board form.
     */
       public void run() {
           try{
            //gets move and updates board
            game.addState(game.getComputerMove(lvl));
            b.currentIndex++;
            b.updateBoard();
            b.playMoveMadeSound();
            b.checkForTerminalState();
           }catch(Exception e){//when checkmate is reached, this method may still be called
               
           }
       }
}
