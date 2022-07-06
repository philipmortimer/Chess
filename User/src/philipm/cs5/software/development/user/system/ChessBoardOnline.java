/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package philipm.cs5.software.development.user.system;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 * Creates new form which displays chess board for an online game of chess.
 * @author mortimer
 */
public class ChessBoardOnline extends javax.swing.JFrame {
    private boolean soundIsOn=true;//stores whether sound is on
    SessionInfo session;//stores the current session information
    private boolean isUserWhite;//stores whether the logged in user is playing as white
    private ChessGame game;//this object stores the game state and handle the game logic
    public int currentIndex;//stores the current board index
    private JLabel boardVisual[][]=new JLabel[8][8];
    private final static String [] IMAGES_NAME = {"Black King","Black Queen","Black Rook","Black Bishop","Black Knight","Black Pawn","White King","White Queen","White Rook","White Bishop","White Knight","White Pawn"};//stores the file names of the images of the pieces
    private static ImageIcon imagePiece[] = new ImageIcon[12];//stores images of the the pieces
    private Timer timer;//timer that updates the clock
    private boolean isWhiteAtBottom;//stores whether white should be displayed at bottom of board
    private int secondsWhiteHas;
    private boolean isDefaultLayout=true;
    private static final String BOARD_SETTING="boardSetting.txt";
    private int secondsBlackHas;
    private int currentTime;//stores total time spen
    private int yCoordSquareClicked=-1;private int xCoordSquareClicked=-1;//stores the coordinates of the square previously clicked. If no such square exsists, they are equal to -1 and -1
    private ArrayList <Integer> legalSquaresToMoveTo=new ArrayList<>();//stores legal squares to move to from current square
    private boolean accountForPlayerTime;//stores whether the time each player has spent needs to be tracked
    private ImageIcon muteIcon;private ImageIcon soundOnIcon;//stores icons for sound setting
    private static final String MUTE_PICTURE="mute.png";
    private Thread getOpponentMoveThread=null;//stores the thread that gets an opponents move
    private static final String SOUND_ON_PICTURE="soundOn.png";
    private int eloPlayerOne;//stores the elo rating of player one
    private int eloPlayerTwo;//stores the elo rating of player two
    private String[]gameData;//stores the game data
    private boolean isUserPlayerOne;//stores whether the user is player one in the game data
    private final static Color DEFAULT_BUTTON_BACKGROUND=new JButton().getBackground();
    private boolean killThread=false;
    /**
     * Creates new form ChessBoardOffline
     */
    private ChessBoardOnline() {
        initComponents();
    }
    /**
     * Creates a new chess board.
     * @param session The session information
     * @param chess The chess game to represent
     * @param gameData The record storing the online game
     */
    public ChessBoardOnline(SessionInfo session, ChessGame chess,String[]gameData ){
        updateLayoutVariableBasedOnTextFile();//updates board layout based on which layout the user prefers
        //converts various paramters to object variables
        this.gameData=gameData;
        this.session = session;this.game=chess;
        this.isUserPlayerOne=gameData[1].equals(session.getUsername());//determines whether the user is classed as player one
        this.secondsWhiteHas=Integer.parseInt(gameData[5]);this.secondsBlackHas=Integer.parseInt(gameData[6]);
        currentTime=Integer.parseInt(gameData[4]);
        isUserWhite=(gameData[1].equals(session.getUsername()) && gameData[10].equals("true"))|| (gameData[2].equals(session.getUsername()) && gameData[10].equals("false"));
        initComponents();
        invisibleDrawBtn.setEnabled(false);
        invisibleDrawBtn.setVisible(false);
        //attempts to load the elo of users onto the form
        try{
            eloPlayerOne=(ThreadClient.logInWithUserName(gameData[1], "passw").getElo());
            eloPlayerTwo=(ThreadClient.logInWithUserName(gameData[2], "passw").getElo());
        }catch(Exception e){
            JOptionPane.showMessageDialog(this,"An error occurred when communicating with the server. Error details: "+e,"Error",JOptionPane.OK_OPTION);
            //takes user back to menu
            Menu menu = new Menu(session);
            menu.setVisible(true);
            this.dispose();return;
        }
        accountForPlayerTime=true;
        if(secondsBlackHas==-100){//sets timer labels invisible if player time is not tracked
            blackTimeLbl.setVisible(false);blackTimeLeftLbl.setVisible(false);
            whiteTiimeLbl.setVisible(false);whiteTimeLeftLbl.setVisible(false);
            accountForPlayerTime=false;
        }
        gameTitleLbl.setText("Game Code: "+gameData[0]);//sets the game title
        currentIndex=chess.getLengthOfStatesFoFar()-1;
        isWhiteAtBottom=isUserWhite;
        initialiseBoard();
        setTimers();
        timer = new Timer(1000, new ActionListener() {//timer that updates the timer every second
            @Override
            public void actionPerformed(ActionEvent e) {
                if(accountForPlayerTime){//if player time is  tracked then the timer values are updated
                    if(game.getCurrentState().IS_BLACK_TURN_TO_PLAY_NEXT&&!isUserWhite){
                        currentTime++;
                        secondsBlackHas--;
                        if(secondsBlackHas<=0){
                            secondsBlackHas=0;
                            setTimers();
                            //updates record as user has run out of time and the game is over
                            gameData[4]=String.valueOf(currentTime);gameData[5]=String.valueOf(secondsWhiteHas);gameData[6]=String.valueOf(secondsBlackHas);
                            if(isUserPlayerOne){
                                gameData[8]="true";
                                gameData[11]="0";
                            }else{
                                gameData[11]="1";
                                gameData[9]="true";
                            }
                            JOptionPane.showMessageDialog(null,"Black has run out of time. White wins!","White Wins",JOptionPane.OK_OPTION);
                            updateRecordAndGoToMenu();
                            timer.stop();
                            timer=null;
                        }
                    }else if(!game.getCurrentState().IS_BLACK_TURN_TO_PLAY_NEXT&&isUserWhite){
                        currentTime++;
                        secondsWhiteHas--;
                        if(secondsWhiteHas<=0){//if the user has run out of time, the game is ended
                            secondsWhiteHas=0;
                            gameData[4]=String.valueOf(currentTime);gameData[5]=String.valueOf(secondsWhiteHas);gameData[6]=String.valueOf(secondsBlackHas);
                            if(isUserPlayerOne){
                                gameData[11]="0";
                                gameData[8]="true";
                            }else{
                                gameData[11]="1";
                                gameData[9]="true";
                            }

                            JOptionPane.showMessageDialog(null,"White has run out of time. Black wins!","Black Wins",JOptionPane.OK_OPTION);
                            updateRecordAndGoToMenu();
                            timer.stop();
                            timer=null;
                        }
                    }
                }
                //updates the current time variable if relevant
                if(((game.getCurrentState().IS_BLACK_TURN_TO_PLAY_NEXT&&!isUserWhite)||(!game.getCurrentState().IS_BLACK_TURN_TO_PLAY_NEXT&&isUserWhite)) && accountForPlayerTime==false){
                    currentTime++;
                }else{
                    
                }
                setTimers();
            }
        });timer.start();//starts the timers
        updateFormBasedOnGameDataRecord();//updates the form based on the game data stored
        updateImageArrayBasedOnVaraible();//updates the look and feel of the board depedning for which layout the user has gone for
        updateBoard();//the board is updated to reflect the current board state
        updateSoundIcon();//updates the sound icon depedning on whether the game is on mute or not.
        addSquareListeners();//adds action listeners for every square that is pressed by the user
        this.jPanel1.setDoubleBuffered(true);
        getOpponentMoveIfAppropriate();//look for move from opponent if it's their turn to move
    }
    /**
     * Updates the array of image depending on which layout choice the user has gone for. Basically, the actually images that represent the chess pieces is altered if needed.
     */
    private void updateImageArrayBasedOnVaraible(){
        try{
            File fileOfPiece;//stores file where image is stored
            BufferedImage bufferedImagePiece;//stores a buffered image of the piece
            String fileExt="";
             for(int i = 0; i<IMAGES_NAME.length;i++){
                if(isDefaultLayout){
                    fileExt="oldPieceImages";
                }else{
                    fileExt="test";
                }
                fileOfPiece = new File("Assets"+File.separator+fileExt+File.separator+IMAGES_NAME[i] +".png");//finds file
                bufferedImagePiece = ImageIO.read(fileOfPiece);
                CustomImageIconBoard cust = new CustomImageIconBoard(bufferedImagePiece);
                imagePiece[i]= cust;//stores image
            }         
            //closes and flushes relevant methods
        }catch(IOException e){
            JOptionPane.showMessageDialog(this, "An unexpected error occured when loading the image of pieces: "+e, "Error", JOptionPane.OK_OPTION);
        }         
    }
    /**
     * Gets whether the thread should be killed
     * @return whether the thread should be killed
     */
    private synchronized boolean getKillThread(){
        return killThread;
    }
    /**
     * Updates the game record by requesting the data from the server. If an error occurs, the user is taken to the menu.
     */
    private void updateGameDataArrayFromServerAndGoToMenuIfErrorOccurs(){
        try{
            //updates the relevant fields in the game record
            gameData[5]=String.valueOf(secondsWhiteHas);gameData[6]=String.valueOf(secondsBlackHas);gameData[4]=String.valueOf(currentTime);
            ThreadClient.updateOnlineGameRecord(gameData);//stores the new array to the server
            if(gameData[0]==null){
                JOptionPane.showMessageDialog(this,"An error occurred when communicating the game result with the server." ,"Error",JOptionPane.OK_OPTION);
                Menu menu = new Menu(session);
                menu.setVisible(true);
                this.dispose();return;
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(this,"An error occurred when communicating the game result with the server. Error details: "+e,"Error",JOptionPane.OK_OPTION);
            Menu menu = new Menu(session);
            menu.setVisible(true);
            this.dispose();return;
        }           
    }
    /**
     * Saves the game data array to the server. If an error occurs, then the user is taken to the menu
     */
    private synchronized void saveRecordToServer(){
         try{
             //updates the game record
            gameData[5]=String.valueOf(secondsWhiteHas);gameData[6]=String.valueOf(secondsBlackHas);gameData[4]=String.valueOf(currentTime);
            ThreadClient.updateOnlineGameRecord(gameData);//sends the updated record to the server
        }catch(Exception e){
            JOptionPane.showMessageDialog(this,"An error occurred when communicating the game result with the server. Error details: "+e,"Error",JOptionPane.OK_OPTION);
            Menu menu = new Menu(session);
            menu.setVisible(true);
            this.dispose();return;
        }          
    }
    /**
     * Updates the game record (by storing the game data array in the server) and goes to the menu
     */
    private void updateRecordAndGoToMenu(){
        try{
            //updates the record
            gameData[5]=String.valueOf(secondsWhiteHas);gameData[6]=String.valueOf(secondsBlackHas);gameData[4]=String.valueOf(currentTime);
            ThreadClient.updateOnlineGameRecord(gameData);//saves record to server
            OnlineMenu menu = new OnlineMenu(session);//returns to online menu
            menu.setVisible(true);
            this.dispose();
            return;
        }catch(Exception e){
            JOptionPane.showMessageDialog(this,"An error occurred when communicating the game result with the server. Error details: "+e,"Error",JOptionPane.OK_OPTION);
            Menu menu = new Menu(session);
            menu.setVisible(true);
            this.dispose();return;
        }       
    }
    /**
     * Updates the form after the game data array has been updated
     */
    private void updateFormBasedOnGameDataRecord(){
        //sets labels displaying usernames for each colour and ELO scores
        if(gameData[10].equals("true")){
            whiteLbl.setText("White: "+gameData[1]);
            whiteEloLbl.setText("ELO: "+eloPlayerOne);
            blackLbl.setText("Black: "+gameData[2]);
            blackEloLbl.setText("ELO: "+eloPlayerTwo);
        }else{
            whiteLbl.setText("White: "+gameData[2]);
            whiteEloLbl.setText("ELO: "+eloPlayerTwo);
            blackLbl.setText("Black: "+gameData[1]);
            blackEloLbl.setText("ELO: "+eloPlayerOne);            
        }
        invisibleDrawBtn.setEnabled(false);
        invisibleDrawBtn.setVisible(false);
        //checks to see whether the user has offered a draw or been offered a draw and updates the form correspondingly
        if((isUserPlayerOne&&gameData[12].equals("false") && gameData[13].equals("true")) || (!isUserPlayerOne&&gameData[13].equals("false") && gameData[12].equals("true"))){//if a draw is being offered to the player, this code is executed
            invisibleDrawBtn.setEnabled(true);invisibleDrawBtn.setVisible(true);
            offerDrawBtn.setBackground(Color.red);offerDrawBtn.setText("Reject Draw Offer");
            invisibleDrawBtn.setBackground(Color.green);invisibleDrawBtn.setText("Accept draw offer");
        }else if((isUserPlayerOne&&gameData[12].equals("true") && gameData[13].equals("false")) || (!isUserPlayerOne&&gameData[13].equals("true") && gameData[12].equals("false"))){//checks to see if a draw has been offered but not accepted
            offerDrawBtn.setText("Withdraw draw offer");offerDrawBtn.setBackground(DEFAULT_BUTTON_BACKGROUND);
        }else if(gameData[12].equals("true")&&gameData[13].equals("true")){//checks to see if a draw has occurred
        /*    JOptionPane.showMessageDialog(this, "The game was a draw.","Draw!",JOptionPane.OK_OPTION);
            gameData[11]="0.5";//sets the game state to a draw
            if(isUserPlayerOne){//stores the fact that the user has seen the game result
                gameData[8]="true";
            }else{
                gameData[9]="true";
            }
            updateRecordAndGoToMenu(); */
        }else{//no draw has been offered
            offerDrawBtn.setText("Offer Draw");offerDrawBtn.setBackground(DEFAULT_BUTTON_BACKGROUND);
            invisibleDrawBtn.setEnabled(false);invisibleDrawBtn.setVisible(false);
        }
        //stores all board states -adding any on if relevant
        String boards[]=gameData[3].split(OfflinePlay.SPLIT_FEN_STRING);
        for(int i=game.getLengthOfStatesFoFar();i<boards.length;i++){
            game.addState(new ChessBoardState(boards[i]));currentIndex++;
        }
       // secondsWhiteHas=Integer.parseInt(gameData[5]);
        //secondsBlackHas=Integer.parseInt(gameData[6]);
       // currentTime=Integer.parseInt(gameData[4]);
       //this code used to check whether the board was in a terminal state. However, this led to issues with threads as multiple different methods were declaring the game
       //to be over which led to issues. This code has been left in case it is needed to debug in the future
       /*
        if((gameData[11].equals("1")&&gameData[10].equals("true")) || (gameData[11].equals("0")&&gameData[10].equals("false"))){
            JOptionPane.showMessageDialog(this, "White wins!","White win",JOptionPane.OK_OPTION);
            if(isUserPlayerOne){//stores the fact that the user has seen the game result
                gameData[8]="true";
            }else{
                gameData[9]="true";
            }            
            updateRecordAndGoToMenu();
            return; 
        }else if(gameData[11].equals("0.5") || (gameData[12].equals("true")&&gameData[13].equals("true"))){
            JOptionPane.showMessageDialog(this, "The game was drawn!","Draw",JOptionPane.OK_OPTION);
            if(isUserPlayerOne){//stores the fact that the user has seen the game result
                gameData[8]="true";
            }else{
                gameData[9]="true";
            }            
            updateRecordAndGoToMenu();   
            return;
        }else if((gameData[11].equals("0")&&gameData[10].equals("true")) || (gameData[11].equals("1")&&gameData[10].equals("false"))){
            JOptionPane.showMessageDialog(this, "Black Won!","Black Win",JOptionPane.OK_OPTION);
            if(isUserPlayerOne){//stores the fact that the user has seen the game result
                gameData[8]="true";
            }else{
                gameData[9]="true";
            }            
            updateRecordAndGoToMenu();  
            return;
        }
        */
        
    }
    /**
     * Updates the sound icon based on whether sound is currently on or off
     */
    private void updateSoundIcon(){
        if(soundIsOn){
            speakerLbl.setIcon(soundOnIcon);
        }else{
            speakerLbl.setIcon(muteIcon);
        }
    }
    /**
     * Updates various components of board to reflect currently selected board state
     */
    public void updateBoard(){
        ChessBoardState state = game.getState(currentIndex);//gets state specified
        viewPreviousBoard.setEnabled(true);nextBoardBtn.setEnabled(true);viewPresentBoard.setEnabled(true);
        moveNumberLbl.setText("Move Number: "+String.valueOf(state.FULL_MOVE_NUMBER));
        if(currentIndex==0){//sets it so that no previous states can be accessed
            viewPreviousBoard.setEnabled(false);
        }
        if(currentIndex==game.getLengthOfStatesFoFar()-1){
            nextBoardBtn.setEnabled(false);//sets it so that view next board is not enabled
            viewPresentBoard.setEnabled(false);
        }
        //adds visual mark to display who's turn it is to play
        if(state.IS_BLACK_TURN_TO_PLAY_NEXT){
            isBlackTurnLbl.setText("*");isWhiteTurnLbl.setText("");
        }else{
            isBlackTurnLbl.setText("");isWhiteTurnLbl.setText("*");
        }
        if(game.getCurrentState().IS_BLACK_TURN_TO_PLAY_NEXT){
            blackTimeLbl.setText("*");whiteTiimeLbl.setText("");
        }else{
            blackTimeLbl.setText("");whiteTiimeLbl.setText("*");
        }
        int[][]board=state.getCopyOfBoard();//gets a copy of the game board
        if(isWhiteAtBottom==false){//checks to see whether board needs to be flipped
            //rotates board by 180 degrees
            rotateMatrix(board);
        }
        //disables resign button if game is played against computer and its the computers turn
        yCoordSquareClicked=-1;xCoordSquareClicked=-1;
        legalSquaresToMoveTo=new ArrayList<>();
        //loads the board and the corresponding images
        int yKing=-1;int xKing=-1;
        for(int y=0;y<8;y++){
            for(int x=0;x<8;x++){
               //sets square to the appropriate colour
                if(y%2==x%2){
                    if(isDefaultLayout){
                        boardVisual[y][x].setBackground(Color.white);
                    }else{
                        boardVisual[y][x].setBackground(Color.decode("#F1D9C0"));  
                    }
                }else{
                    if(isDefaultLayout){
                        boardVisual[y][x].setBackground(Color.black);
                    }else{
                        boardVisual[y][x].setBackground(Color.decode("#A97A65")); 
                    }  
                }
                //sets board labels to the appropriate piece image
                switch (board[y][x]) {
                    case ChessBoardState.EMPTY_VALUE:
                        boardVisual[y][x].setIcon(null);
                        break;
                    case ChessBoardState.PAWN_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                        boardVisual[y][x].setIcon(imagePiece[11]);
                        break;
                    case ChessBoardState.PAWN_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                        boardVisual[y][x].setIcon(imagePiece[5]);
                        break;
                    case ChessBoardState.ROOK_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                        boardVisual[y][x].setIcon(imagePiece[8]);
                        break;
                    case ChessBoardState.ROOK_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                        boardVisual[y][x].setIcon(imagePiece[2]);
                        break;
                    case ChessBoardState.KNIGHT_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                        boardVisual[y][x].setIcon(imagePiece[10]);
                        break;
                    case ChessBoardState.KNIGHT_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                        boardVisual[y][x].setIcon(imagePiece[4]);
                        break;
                    case ChessBoardState.BISHOP_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                        boardVisual[y][x].setIcon(imagePiece[9]);
                        break;
                    case ChessBoardState.BISHOP_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                        boardVisual[y][x].setIcon(imagePiece[3]);
                        break;
                    case ChessBoardState.QUEEN_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                        boardVisual[y][x].setIcon(imagePiece[7]);
                        break;
                    case ChessBoardState.QUEEN_VALUE*ChessBoardState.BLACK_MULTIPLIER:
                        boardVisual[y][x].setIcon(imagePiece[1]);
                        break;
                    case ChessBoardState.KING_VALUE*ChessBoardState.WHITE_MULTIPLIER:
                        boardVisual[y][x].setIcon(imagePiece[6]);
                        if(!state.IS_BLACK_TURN_TO_PLAY_NEXT){
                            yKing=y;xKing=x;
                        }
                        break;
                    default:
                        //black king
                        boardVisual[y][x].setIcon(imagePiece[0]);
                        if(state.IS_BLACK_TURN_TO_PLAY_NEXT){
                            yKing=y;xKing=x;
                        }
                        break;
                }
            }
        }
        //sets the king's square to be red if it is in check
        if(ChessGame.isInCheck(!state.IS_BLACK_TURN_TO_PLAY_NEXT, new ChessBoardState(state.BOARD, state.CASTLING_RIGHTS, !state.IS_BLACK_TURN_TO_PLAY_NEXT, state.FULL_MOVE_NUMBER, state.HALF_MOVE_CLOCK, state.SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT))){//if king is in check, its square is coloured red
            boardVisual[yKing][xKing].setBackground(Color.red);
        }
    }
    /**
     * Creates a thread that periodically queries the server for a move from their opponent
     */
    private void getOpponentMoveIfAppropriate(){
        //creates a thread that contsantly looking to see if the opponent has made a move. If they have, the board is updated accordingly
            getOpponentMoveThread=new Thread( new Runnable(){
                @Override
                public void run(){
                    int lengthOfBoard=game.getLengthOfStatesFoFar()-1;
                    while(!getKillThread()){
                       synchronized(this){
                            // refreshses the game
                            setGameDataUpToData();
                            if(getKillThread()){//checks to see if thread is to be killed
                                break;
                            }
                            //updates form based on game data
                            updateFormBasedOnGameDataRecord();
                            if(getKillThread()){
                                break;
                            }                            
                            if(currentIndex!=lengthOfBoard){
                                //updates board and makes a move if appropraite
                                lengthOfBoard=currentIndex;
                                updateBoard();
                                playMoveMadeSound();
                                currentTime=Integer.parseInt(gameData[4]);secondsWhiteHas=Integer.parseInt(gameData[5]);secondsBlackHas=Integer.parseInt(gameData[6]);
                            }
                            if(getKillThread()){
                                break;
                            }                            
                            checkForTerminalState();//checks to see whether the board is in a terminal state
                        }
                        try{
                            Thread.sleep(1000);//pauses the thread to prevent the thread from neeedlessly spamming the server
                        }catch(InterruptedException e){
                            
                        } 
                    }
                }
            });getOpponentMoveThread.start();//starts the thread
     
    }
    /**
     * Updates the variable isDefault layout based on text file contents
     */
    private void updateLayoutVariableBasedOnTextFile(){
        //gets the board configuration to use
        try{
            FileReader read = new FileReader(BOARD_SETTING);
            BufferedReader buffRead = new BufferedReader(read);
            isDefaultLayout=buffRead.readLine().equals("true");
            buffRead.close();read.close(); 
        }catch(Exception e){
            System.out.println(e);
        }
    }
    /**
     * Sets the values for all timers  that are appropriate
     */
    private void setTimers(){
        //sets value of overall timer
        int sec=currentTime%60;
        int hour=currentTime/60;
        int min=hour%60;
        hour=hour/60;
        String s=String.valueOf(sec);if(s.length()==1){s="0"+s;}//makes seconds at least two digits
        String m = String.valueOf(min);if(m.length()==1){m="0"+m;}
        String h=String.valueOf(hour);if(h.length()==1){h="0"+h;} 
        timerLbl.setText(h+":"+m+":"+s);
        if(accountForPlayerTime){//sets time left for each colour if relevant
            //sets time in format of hours, minutes and seconds.
                m=String.valueOf(secondsBlackHas/60);
                if(m.length()==1){
                    m="0"+m;
                }
                s=String.valueOf(secondsBlackHas%60);
                if(s.length()==1){
                    s="0"+s;
                }
                blackTimeLeftLbl.setText("Black Time: "+m+":"+s);
                m=String.valueOf(secondsWhiteHas/60);
                if(m.length()==1){
                    m="0"+m;
                }
                s=String.valueOf(secondsWhiteHas%60);
                if(s.length()==1){
                    s="0"+s;
                }
                whiteTimeLeftLbl.setText("White Time: "+m+":"+s);               
        }
    }
    /**
     * This rotates a chess board array by 180 degrees.
     * @param board The board (must be an 8*8 array)
     */
    private static void rotateMatrix(int[][] board) {
        int temp;//used as a buffer
        for (int i = 0; i <= 3; i++) {
            for (int j = 0; j < 8; j++) {
                temp = board[i][j];
                board[i][j] = board[7 - i ][7- j];
                board[7-i][7- j] = temp;
            }
        }
    }
    /**
     * This methods creates the board pieces and is used to initialise the board. It also changes colour of squares
     * depending on whether the user is white.
     */
    private void initialiseBoard(){
        //creates JLabels and loads them to array
        final int WIDTH_OF_SQUARE=56;//stores the width of the square
        final int HEIGHT_OF_SQUARE=56;//stores the height of the square
        final int SPACE_ABOVE_BOARD=10;//stores the space above the board
        final int SPACE_TO_RIGHT_OF_BOARD=50;//stores the gap between the right edge of the screen and the board
        int xCoordOfSquare=SPACE_TO_RIGHT_OF_BOARD;//stores the coordinate of the square
        int yCoordOfSquare=SPACE_ABOVE_BOARD;//stores the y coordinate of the square
        final String RANK_AND_FILE[]={"a","b","c","d","e","f","g","h","1","2","3","4","5","6","7","8"};//stores the values to be used by labels that indicate the coordinates on the board
        final int HEIGHT_OF_LABEL=16;//stores the height of the label showing the rank or file of the square
        final int DISTANCE_OF_LETTERS_BELOW=3;//stores distance between squares and letters
        //adds colour squares to GUI
        for(int y=0;y<8;y++){
            for(int x=0;x<8;x++){
                boardVisual[y][x]=new BoardJLabel();
                boardVisual[y][x].setSize(WIDTH_OF_SQUARE, HEIGHT_OF_SQUARE);//sets size of label
                boardVisual[y][x].setLocation(xCoordOfSquare, yCoordOfSquare);
                //sets square to the approrpiate colour
                if(y%2==x%2){
                    //boardVisual[y][x].setBackground(Color.white);
                    boardVisual[y][x].setBackground(Color.decode("#F1D9C0"));     
                }else{
                    //boardVisual[y][x].setBackground(Color.black);
                    boardVisual[y][x].setBackground(Color.decode("#A97A65")); 
                }                  
                boardVisual[y][x].setOpaque(true);//allows piece's background to be seen
                boardVisual[y][x].setDoubleBuffered(true);
                this.jPanel1.add(boardVisual[y][x]);
                if(x==7){//updates coordinates of next label
                    xCoordOfSquare=SPACE_TO_RIGHT_OF_BOARD;
                    yCoordOfSquare=yCoordOfSquare+HEIGHT_OF_SQUARE;
                }else{
                    xCoordOfSquare=xCoordOfSquare+WIDTH_OF_SQUARE;
                }
            }
        }
        //adds labels to indicate rank and file of pieces
        xCoordOfSquare=SPACE_TO_RIGHT_OF_BOARD+(WIDTH_OF_SQUARE/2)-(HEIGHT_OF_LABEL/2);
        yCoordOfSquare=yCoordOfSquare+DISTANCE_OF_LETTERS_BELOW;
        JLabel lbl;//stores label to be added
        for(int indexForCoord=0;indexForCoord<8;indexForCoord++){//adds letters to board which indicate file
            lbl=new JLabel(RANK_AND_FILE[indexForCoord]);
            lbl.setSize(HEIGHT_OF_LABEL, HEIGHT_OF_LABEL);
            lbl.setLocation(xCoordOfSquare,yCoordOfSquare);
            xCoordOfSquare=xCoordOfSquare+WIDTH_OF_SQUARE;
            this.jPanel1.add(lbl);
        }
        yCoordOfSquare=SPACE_ABOVE_BOARD+(HEIGHT_OF_SQUARE/2)-(HEIGHT_OF_LABEL/2);
        xCoordOfSquare=SPACE_TO_RIGHT_OF_BOARD-DISTANCE_OF_LETTERS_BELOW-HEIGHT_OF_LABEL;
        for(int indexForCoord=15;indexForCoord>=8;indexForCoord--){
            lbl=new JLabel(RANK_AND_FILE[indexForCoord]);
            lbl.setSize(HEIGHT_OF_LABEL, HEIGHT_OF_LABEL);
            lbl.setLocation(xCoordOfSquare,yCoordOfSquare);
            yCoordOfSquare=yCoordOfSquare+HEIGHT_OF_SQUARE;
            this.jPanel1.add(lbl);           
        }
        //loads all images and stores them
        try{
            File fileOfPiece;//stores file where image is stored
            BufferedImage bufferedImagePiece=null;//stores a buffered image of the piece
            Image imgPiece=null;//stores the actual image of the piece
            ImageIcon imageIconPiece;//stores the icon of the image
             for(int i = 0; i<IMAGES_NAME.length;i++){
                fileOfPiece = new File("Assets"+File.separator+"test"+File.separator+IMAGES_NAME[i] +".png");//finds file
                bufferedImagePiece = ImageIO.read(fileOfPiece);
                //imgPiece = bufferedImagePiece.getScaledInstance(WIDTH_OF_SQUARE, HEIGHT_OF_SQUARE , Image.SCALE_SMOOTH);//loads image of piece and scales it to size
                CustomImageIconBoard cust = new CustomImageIconBoard(bufferedImagePiece);
                //imageIconPiece = new ImageIcon(imgPiece);
                imagePiece[i]= cust;//stores image
            }
             
             //gets speaker icons
            fileOfPiece=new File("Assets"+File.separator +MUTE_PICTURE);
             bufferedImagePiece=ImageIO.read(fileOfPiece);
             imgPiece=bufferedImagePiece.getScaledInstance(speakerLbl.getPreferredSize().width, speakerLbl.getPreferredSize().width, Image.SCALE_SMOOTH);
             muteIcon=new CustomImageIconBoardSpeaker(imgPiece);
             fileOfPiece=new File("Assets"+File.separator +SOUND_ON_PICTURE);
             bufferedImagePiece=ImageIO.read(fileOfPiece);
             imgPiece=bufferedImagePiece.getScaledInstance(speakerLbl.getPreferredSize().width, speakerLbl.getPreferredSize().width, Image.SCALE_SMOOTH);
             soundOnIcon=new CustomImageIconBoardSpeaker(imgPiece);
            //closes and flushes relevant methods
            imgPiece.flush();bufferedImagePiece.flush();
        }catch(IOException e){
            JOptionPane.showMessageDialog(this, "An unexpected error occured when loading the image of pieces: "+e, "Error", JOptionPane.OK_OPTION);
        } 
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        backBtn = new javax.swing.JButton();
        resignBtn = new javax.swing.JButton();
        viewPreviousBoard = new javax.swing.JButton();
        viewPresentBoard = new javax.swing.JButton();
        nextBoardBtn = new javax.swing.JButton();
        timerLbl = new javax.swing.JLabel();
        timerLbl1 = new javax.swing.JLabel();
        blackLbl = new javax.swing.JLabel();
        whiteLbl = new javax.swing.JLabel();
        isBlackTurnLbl = new javax.swing.JLabel();
        isWhiteTurnLbl = new javax.swing.JLabel();
        moveNumberLbl = new javax.swing.JLabel();
        flipBoardBtn = new javax.swing.JButton();
        blackTimeLeftLbl = new javax.swing.JLabel();
        whiteTimeLeftLbl = new javax.swing.JLabel();
        gameTitleLbl = new javax.swing.JLabel();
        blackTimeLbl = new javax.swing.JLabel();
        whiteTiimeLbl = new javax.swing.JLabel();
        speakerLbl = new BoardJLabel();
        whiteEloLbl = new javax.swing.JLabel();
        blackEloLbl = new javax.swing.JLabel();
        offerDrawBtn = new javax.swing.JButton();
        refreshBtn = new javax.swing.JButton();
        invisibleDrawBtn = new javax.swing.JButton();
        changeBoardBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Online Game");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        backBtn.setText("Back");
        backBtn.setDoubleBuffered(true);
        backBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backBtnActionPerformed(evt);
            }
        });

        resignBtn.setText("Resign");
        resignBtn.setDoubleBuffered(true);
        resignBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resignBtnActionPerformed(evt);
            }
        });

        viewPreviousBoard.setText("Previous Board");
        viewPreviousBoard.setDoubleBuffered(true);
        viewPreviousBoard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewPreviousBoardActionPerformed(evt);
            }
        });

        viewPresentBoard.setText("Current Board");
        viewPresentBoard.setDoubleBuffered(true);
        viewPresentBoard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewPresentBoardActionPerformed(evt);
            }
        });

        nextBoardBtn.setText("Next Board");
        nextBoardBtn.setDoubleBuffered(true);
        nextBoardBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextBoardBtnActionPerformed(evt);
            }
        });

        timerLbl.setText(" ");
        timerLbl.setDoubleBuffered(true);

        timerLbl1.setText("Timer:");
        timerLbl1.setDoubleBuffered(true);

        blackLbl.setBackground(new java.awt.Color(204, 204, 204));
        blackLbl.setText("Black:");
        blackLbl.setDoubleBuffered(true);
        blackLbl.setOpaque(true);

        whiteLbl.setBackground(new java.awt.Color(255, 255, 255));
        whiteLbl.setText("White:");
        whiteLbl.setDoubleBuffered(true);
        whiteLbl.setOpaque(true);

        isBlackTurnLbl.setForeground(new java.awt.Color(255, 0, 0));
        isBlackTurnLbl.setText("*");
        isBlackTurnLbl.setDoubleBuffered(true);

        isWhiteTurnLbl.setForeground(new java.awt.Color(255, 0, 0));
        isWhiteTurnLbl.setText("*");
        isWhiteTurnLbl.setDoubleBuffered(true);

        moveNumberLbl.setText("Move Number: ");
        moveNumberLbl.setDoubleBuffered(true);

        flipBoardBtn.setText("Flip Board");
        flipBoardBtn.setDoubleBuffered(true);
        flipBoardBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                flipBoardBtnActionPerformed(evt);
            }
        });

        blackTimeLeftLbl.setBackground(new java.awt.Color(204, 204, 204));
        blackTimeLeftLbl.setText("Black Time: ");
        blackTimeLeftLbl.setDoubleBuffered(true);
        blackTimeLeftLbl.setOpaque(true);

        whiteTimeLeftLbl.setBackground(new java.awt.Color(255, 255, 255));
        whiteTimeLeftLbl.setText("White Time:");
        whiteTimeLeftLbl.setDoubleBuffered(true);
        whiteTimeLeftLbl.setOpaque(true);

        gameTitleLbl.setText("Game Code: ");
        gameTitleLbl.setDoubleBuffered(true);

        blackTimeLbl.setForeground(new java.awt.Color(255, 0, 0));
        blackTimeLbl.setText("*");
        blackTimeLbl.setDoubleBuffered(true);

        whiteTiimeLbl.setForeground(new java.awt.Color(255, 0, 0));
        whiteTiimeLbl.setText("*");
        whiteTiimeLbl.setDoubleBuffered(true);

        speakerLbl.setPreferredSize(new java.awt.Dimension(15, 15));
        speakerLbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                speakerLblMouseClicked(evt);
            }
        });

        whiteEloLbl.setBackground(new java.awt.Color(255, 255, 255));
        whiteEloLbl.setText("ELO: ");
        whiteEloLbl.setDoubleBuffered(true);
        whiteEloLbl.setOpaque(true);

        blackEloLbl.setBackground(new java.awt.Color(204, 204, 204));
        blackEloLbl.setText("ELO:");
        blackEloLbl.setDoubleBuffered(true);
        blackEloLbl.setOpaque(true);

        offerDrawBtn.setText("Offer Draw");
        offerDrawBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                offerDrawBtnActionPerformed(evt);
            }
        });

        refreshBtn.setText("Refresh");
        refreshBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshBtnActionPerformed(evt);
            }
        });

        invisibleDrawBtn.setText("Offer Draw");
        invisibleDrawBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                invisibleDrawBtnActionPerformed(evt);
            }
        });

        changeBoardBtn.setText("Change Board");
        changeBoardBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeBoardBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(572, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(moveNumberLbl)
                                .addGap(107, 107, 107))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(invisibleDrawBtn)
                                        .addGap(18, 18, 18)
                                        .addComponent(offerDrawBtn)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(resignBtn))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(viewPreviousBoard)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(viewPresentBoard)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(nextBoardBtn)))
                                .addGap(25, 25, 25))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(isWhiteTurnLbl)
                                        .addComponent(isBlackTurnLbl))
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(blackLbl)
                                        .addComponent(whiteLbl))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(blackEloLbl)
                                        .addComponent(whiteEloLbl)))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(blackTimeLbl)
                                        .addComponent(whiteTiimeLbl))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(whiteTimeLeftLbl)
                                        .addComponent(blackTimeLeftLbl))
                                    .addGap(253, 253, 253)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(changeBoardBtn)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(backBtn))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(flipBoardBtn)
                                    .addGap(33, 33, 33)
                                    .addComponent(refreshBtn)
                                    .addGap(195, 195, 195)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(gameTitleLbl)
                        .addGap(184, 184, 184)
                        .addComponent(timerLbl1)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(timerLbl)
                        .addGap(27, 27, 27))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(speakerLbl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(timerLbl)
                    .addComponent(timerLbl1)
                    .addComponent(gameTitleLbl))
                .addGap(58, 58, 58)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(blackLbl)
                            .addComponent(isBlackTurnLbl))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(whiteLbl)
                            .addComponent(isWhiteTurnLbl)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(blackEloLbl)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(whiteEloLbl)))
                .addGap(36, 36, 36)
                .addComponent(moveNumberLbl)
                .addGap(52, 52, 52)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(blackTimeLeftLbl)
                    .addComponent(blackTimeLbl))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(whiteTimeLeftLbl)
                    .addComponent(whiteTiimeLbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(viewPreviousBoard)
                            .addComponent(viewPresentBoard)
                            .addComponent(nextBoardBtn))
                        .addGap(31, 31, 31)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(resignBtn)
                            .addComponent(offerDrawBtn)
                            .addComponent(invisibleDrawBtn)))
                    .addComponent(speakerLbl, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(flipBoardBtn)
                    .addComponent(refreshBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(backBtn)
                    .addComponent(changeBoardBtn))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void flipBoardBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_flipBoardBtnActionPerformed
        //flips the board
        isWhiteAtBottom=!isWhiteAtBottom;
        updateBoard();
    }//GEN-LAST:event_flipBoardBtnActionPerformed

    private void backBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBtnActionPerformed
        //saves the game and takes the user back to the online menu
        saveRecordToServer();
        OnlineMenu online = new OnlineMenu(session);
        online.setVisible(true);
        this.dispose();return;
    }//GEN-LAST:event_backBtnActionPerformed

    private void viewPreviousBoardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewPreviousBoardActionPerformed
        // takes user to previous board state
        currentIndex--;
        updateBoard();
    }//GEN-LAST:event_viewPreviousBoardActionPerformed

    private void nextBoardBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextBoardBtnActionPerformed
        // takes user to a more recent board state 
        currentIndex++;
        updateBoard();
    }//GEN-LAST:event_nextBoardBtnActionPerformed

    private void viewPresentBoardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewPresentBoardActionPerformed
        // takes user to current board
        currentIndex=game.getLengthOfStatesFoFar()-1;
        updateBoard();
    }//GEN-LAST:event_viewPresentBoardActionPerformed

    private void resignBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resignBtnActionPerformed
        // gets player to resign
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you would like to resign? Doing so means you will lose the game.","Resign?",JOptionPane.YES_NO_CANCEL_OPTION);
        if(confirm==JOptionPane.YES_OPTION){
            killThread=true;
            if(isUserWhite==false){
                JOptionPane.showMessageDialog(this, "Black resigned - White wins!","White Wins!",JOptionPane.OK_OPTION);
            }else{
                JOptionPane.showMessageDialog(this, "White resigned - Black wins!","Black Wins!",JOptionPane.OK_OPTION);
            }
            if(isUserPlayerOne){
                gameData[11]="0";//sets the game result
                gameData[8]="true";
            }else{
                gameData[11]="1";
                gameData[9]="true";
            }
            saveRecordToServer();//updates game record
            //takes user back to menu
            OnlineMenu online = new OnlineMenu(session);
            online.setVisible(true);
            this.dispose();return;
        }
    }//GEN-LAST:event_resignBtnActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
      //updates file when window is closed and kills the thread that is getting the opponent move
        if(isUserWhite && !game.getCurrentState().IS_BLACK_TURN_TO_PLAY_NEXT){
          gameData[4]=String.valueOf(currentTime);gameData[5]=String.valueOf(secondsWhiteHas);gameData[6]=String.valueOf(secondsBlackHas);
      }else if(!isUserWhite &&game.getCurrentState().IS_BLACK_TURN_TO_PLAY_NEXT){
          gameData[4]=String.valueOf(currentTime);gameData[5]=String.valueOf(secondsWhiteHas);gameData[6]=String.valueOf(secondsBlackHas);
      }
        saveRecordToServer();
        if(getOpponentMoveThread!=null){//kills thread
            getOpponentMoveThread.interrupt();
            killThread=true;
        }
        getOpponentMoveThread=null;
        if(timer!=null){
          timer.stop();
          timer=null;
      }
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
      //if a user closes the window by pressing the cross in the top right corner, this method is invoked. It saves the board state
      if(isUserWhite && !game.getCurrentState().IS_BLACK_TURN_TO_PLAY_NEXT){
          gameData[4]=String.valueOf(currentTime);gameData[5]=String.valueOf(secondsWhiteHas);gameData[6]=String.valueOf(secondsBlackHas);
      }else if(!isUserWhite &&game.getCurrentState().IS_BLACK_TURN_TO_PLAY_NEXT){
          gameData[4]=String.valueOf(currentTime);gameData[5]=String.valueOf(secondsWhiteHas);gameData[6]=String.valueOf(secondsBlackHas);
      }
      saveRecordToServer();
      if(timer!=null){
          timer.stop();
          timer=null;
      }
      if(getOpponentMoveThread!=null){
            getOpponentMoveThread.interrupt();
            killThread=true;
        }
        getOpponentMoveThread=null;
    }//GEN-LAST:event_formWindowClosing

    private void speakerLblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_speakerLblMouseClicked
    //updates whether sound is on or not and sets speaker icon to appropraite value.
    soundIsOn=!soundIsOn;
    updateSoundIcon();
    }//GEN-LAST:event_speakerLblMouseClicked

    private void refreshBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshBtnActionPerformed
        // refreshses the game
        int gameLength=game.getLengthOfStatesFoFar()-1;
        setGameDataUpToData();
        updateFormBasedOnGameDataRecord();
        if(currentIndex!=gameLength){
            updateBoard();
            playMoveMadeSound();
        }
        checkForTerminalState();
    }//GEN-LAST:event_refreshBtnActionPerformed

    private void offerDrawBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_offerDrawBtnActionPerformed
        // handles draw offers
        if(offerDrawBtn.getText().equals("Offer Draw")){
                if(isUserPlayerOne){
                    gameData[12]="true";
                }else{
                    gameData[13]="true";
                }
                //offers a draw and updates the online record
                try{
                    ThreadClient.updateOnlineGameRecord(gameData);
                }catch(Exception e){
                    JOptionPane.showMessageDialog(this,"An unexpected error occurred when communicating with the server. Error details: "+e,"Error",JOptionPane.OK_OPTION);
                    Menu menu = new Menu(session);
                    menu.setVisible(true);
                    this.dispose();return;
                }
                updateGameDataArrayFromServerAndGoToMenuIfErrorOccurs();
                updateFormBasedOnGameDataRecord();
        }else if(offerDrawBtn.getText().equals("Withdraw draw offer")){
            //withdraws the draw offer
                if(isUserPlayerOne){
                    gameData[12]="false";
                }else{
                    gameData[13]="false";
                }  
                //updates game data
                try{
                    ThreadClient.updateOnlineGameRecord(gameData);
                }catch(Exception e){
                    JOptionPane.showMessageDialog(this,"An unexpected error occurred when communicating with the server. Error details: "+e,"Error",JOptionPane.OK_OPTION);
                    Menu menu = new Menu(session);
                    menu.setVisible(true);
                    this.dispose();return;
                }
                updateGameDataArrayFromServerAndGoToMenuIfErrorOccurs();//updates form following draw handling
                updateFormBasedOnGameDataRecord();
        }else{//rejects draw offer
            gameData[12]="false";gameData[13]="false";
                //updates game data
                try{
                    ThreadClient.updateOnlineGameRecord(gameData);
                }catch(Exception e){
                    JOptionPane.showMessageDialog(this,"An unexpected error occurred when communicating with the server. Error details: "+e,"Error",JOptionPane.OK_OPTION);
                    Menu menu = new Menu(session);
                    menu.setVisible(true);
                    this.dispose();return;
                }
                updateGameDataArrayFromServerAndGoToMenuIfErrorOccurs();
                updateFormBasedOnGameDataRecord();
        }
    }//GEN-LAST:event_offerDrawBtnActionPerformed

    private void invisibleDrawBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_invisibleDrawBtnActionPerformed
        //this accepts a draw offer and saves this to the server
        killThread=true;getOpponentMoveThread.interrupt();timer.stop();
        synchronized(this){
            // this button can only be pressed to accept a draw offer
            gameData[11]="0.5";//sets the game to a draw
            gameData[12]="true";gameData[13]="true";
            JOptionPane.showMessageDialog(this,"The game was a draw.","Draw",JOptionPane.OK_OPTION);
            if(isUserPlayerOne){
                gameData[8]="true";
            }else{
                gameData[9]="true";
            }
            //updates record stored by server
            saveRecordToServer();
            //takes user back to menu
            OnlineMenu online = new OnlineMenu(session);
            online.setVisible(true);
            this.dispose(); return;
        }
    }//GEN-LAST:event_invisibleDrawBtnActionPerformed

    private void changeBoardBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeBoardBtnActionPerformed
        // updates the variable is Default layout and stores it to a text file
        isDefaultLayout=!isDefaultLayout;
        try{
            FileWriter write = new FileWriter(BOARD_SETTING,false);
            BufferedWriter buffWrite = new BufferedWriter(write);
            buffWrite.write(String.valueOf(isDefaultLayout));
            buffWrite.flush();write.flush();
            buffWrite.close();write.close();
        }catch(IOException e){
            System.out.println(e);
        }
        updateImageArrayBasedOnVaraible();//updates form following the change in board layout
        updateBoard();
    }//GEN-LAST:event_changeBoardBtnActionPerformed
    /**
     * Updates the game data variable by requesting the game data from the server
     */
    private void setGameDataUpToData(){
        try{
            gameData=ThreadClient.getGameData(Integer.parseInt(gameData[0]));
            if(gameData[0]==null){
                JOptionPane.showMessageDialog(this,"An unexpected error occurred.","Error",JOptionPane.OK_OPTION);
                OnlineMenu menu= new OnlineMenu(session);
                menu.setVisible(true);
                this.dispose();
                return;
            }
            //secondsWhiteHas=Integer.parseInt(gameData[5]);
            //secondsBlackHas=Integer.parseInt(gameData[6]);
            //currentTime=Integer.parseInt(gameData[4]);
        }catch(Exception e){
            JOptionPane.showMessageDialog(this,"An error occurred when communicating with the server. Error details: "+e,"Error",JOptionPane.OK_OPTION);
            OnlineMenu menu= new OnlineMenu(session);
            menu.setVisible(true);
            this.dispose();      
            return;
        }
    }
    /**
     * Checks to see if current board is in a terminal state. If it is, the game result is announced and then the system terminates
     */
    private void checkForTerminalState(){
        if((gameData[12].equals("true") && gameData[13].equals("true"))|| (gameData[11].equals("0.5"))){//checks for a draw
            killThread=true;
            JOptionPane.showMessageDialog(this,"The game was a draw.","Draw",JOptionPane.OK_OPTION);
            gameData[11]="0.5";
            if(isUserPlayerOne){
                gameData[8]="true";
            }else{
                gameData[9]="true";
            }
            saveRecordToServer();//updates record and quits the game
            OnlineMenu online = new OnlineMenu(session);
            online.setVisible(true);this.dispose();
            return;
        }
        if(gameData[11].equals("-1")==false){//checks to see if a game result has been registered by another player
            killThread=true;
            if((gameData[11].equals("0")&& gameData[10].equals("true")) || (gameData[11].equals("1")&&gameData[10].equals("false"))){
                JOptionPane.showMessageDialog(this,"Black wins!","Black win!",JOptionPane.OK_OPTION);
            }else if((gameData[11].equals("1")&& gameData[10].equals("true")) || (gameData[11].equals("0")&&gameData[10].equals("false"))){
                JOptionPane.showMessageDialog(this,"Wins wins!","White win!",JOptionPane.OK_OPTION);
            }else{
                JOptionPane.showMessageDialog(this,"Draw!","Draw",JOptionPane.OK_OPTION);
            }
            if(isUserPlayerOne){//sets the appropriate variable to show that the player has seen the game result
                gameData[8]="true";
            }else{
                gameData[9]="true";
            }
            try{
                ThreadClient.updateOnlineGameRecord(gameData);//updates form to server
            }catch(Exception  e){
                JOptionPane.showMessageDialog(this,"An error occurred when communicating with the server. Error details: "+e,"Error",JOptionPane.OK_OPTION);
                Menu menu = new Menu(session);
                menu.setVisible(true);
                this.dispose();
                return;
            }
            OnlineMenu online = new OnlineMenu(session);
            online.setVisible(true);this.dispose();
            return;
        }
        if(game.getAllLegalBoardsFromState(game.getCurrentState()).size()==0){//checks whether the user can make any more moves
            killThread=true;
            timer.stop();
            if(game.getCurrentState().getIsDraw()){//sets game to a draw and exits
                JOptionPane.showMessageDialog(this, "The game was a draw!","Draw!",JOptionPane.OK_OPTION);
                gameData[11]="0.5";
                if(isUserPlayerOne){
                    gameData[8]="true";
                }else{
                    gameData[9]="true";
                }
                saveRecordToServer();
                OnlineMenu menu = new OnlineMenu(session);
                menu.setVisible(true);
                this.dispose();
                return;
            }
            //checks to see which colour wins the game and updates the game record accordingly. The system then returns to a menu.
            if(ChessGame.isInCheck(!game.getCurrentState().IS_BLACK_TURN_TO_PLAY_NEXT, new ChessBoardState(game.getCurrentState().BOARD, game.getCurrentState().CASTLING_RIGHTS, !game.getCurrentState().IS_BLACK_TURN_TO_PLAY_NEXT, game.getCurrentState().FULL_MOVE_NUMBER, game.getCurrentState().HALF_MOVE_CLOCK, game.getCurrentState().SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT))){
                killThread=true;
                if(game.getCurrentState().IS_BLACK_TURN_TO_PLAY_NEXT){
                    JOptionPane.showMessageDialog(this, "White wins!","Checkmate!",JOptionPane.OK_OPTION);
                    if(isUserPlayerOne && isUserWhite){
                        gameData[11]="1";
                    }else{
                        gameData[11]="0";
                    }
                     if(isUserPlayerOne){
                         gameData[8]="true";
                         
                     }else{
                         gameData[9]="true";
                     }
                     saveRecordToServer();
                     OnlineMenu menu = new OnlineMenu(session);
                     menu.setVisible(true);
                     this.dispose();
                    return;
                }else{
                    killThread=true;
                    JOptionPane.showMessageDialog(this, "Black wins.","Checkmate!",JOptionPane.OK_OPTION);
                    if(isUserPlayerOne && !isUserWhite){
                        gameData[11]="1";
                    }else{
                        gameData[11]="0";
                    }
                     if(isUserPlayerOne){
                         gameData[8]="true";
                         
                     }else{
                         gameData[9]="true";
                     }
                     saveRecordToServer();
                     OnlineMenu menu = new OnlineMenu(session);
                     menu.setVisible(true);
                     this.dispose();
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "The game was a draw!","Draw!",JOptionPane.OK_OPTION);
                gameData[11]="0.5";
                if(isUserPlayerOne){
                    gameData[8]="true";
                }else{
                    gameData[9]="true";
                }
                saveRecordToServer();
                OnlineMenu menu = new OnlineMenu(session);
                menu.setVisible(true);
                this.dispose();
            return;
        }
        //checks to see if opponent has run out of time
        if(accountForPlayerTime){
            if(Integer.parseInt(gameData[5])<=0){
                JOptionPane.showMessageDialog(this,"White has run out of time. Black wins!","Black wins!",JOptionPane.OK_OPTION);
                if(isUserPlayerOne){
                    gameData[8]="true";
                }else{
                    gameData[9]="true";
                }
                if(gameData[10].equals("true")){
                    gameData[11]="0";
                }else{
                    gameData[11]="1";
                }
                saveRecordToServer();
                OnlineMenu menu = new OnlineMenu(session);
                menu.setVisible(true);
                this.dispose();  return;              
            }else if(Integer.parseInt(gameData[6])<=0){
                JOptionPane.showMessageDialog(this,"Black has run out of time. White wins!","White wins!",JOptionPane.OK_OPTION);
                if(isUserPlayerOne){
                    gameData[8]="true";
                }else{
                    gameData[9]="true";
                }
                if(gameData[10].equals("true")){
                    gameData[11]="1";
                }else{
                    gameData[11]="0";
                }
                saveRecordToServer();
                OnlineMenu menu = new OnlineMenu(session);
                menu.setVisible(true);
                this.dispose();  return;                  
            }
        }
    }
    /**
     * Plays a sound to indicate that a move has been made if the game is not muted
     */
    public void playMoveMadeSound(){
        if(soundIsOn){
            Random rnd = new Random();
            Audio.playSound("Assets"+File.separator+String.valueOf(rnd.nextInt(8)+1)+"Sound.wav");
        }
    }
    /**
     * Handles when a square is clicked. This includes highlighting the square and all available moves if appropriate.
     * @param y The y index of the square
     * @param x The x index of the square
     */
    private void squareClicked(int y,int x){
        //if opponent is a computer and its the computer's turn to play, no squares should be highlighted
        if((isUserWhite&&game.getCurrentState().IS_BLACK_TURN_TO_PLAY_NEXT)||(!isUserWhite&&!game.getCurrentState().IS_BLACK_TURN_TO_PLAY_NEXT)){
            return;
        }
        if(currentIndex!=game.getLengthOfStatesFoFar()-1){//if the board is not the current board, no action is needed
            return;
        }
        int yBoard=y;int xBoard=x;//stores the y and x coords regarding to the actual board array stored
        if(isWhiteAtBottom==false){//accounts for a flipped board
            yBoard=7-y;xBoard=7-x;
        }
        //updateBoard();
        //checks to see if square clicked is a legal square to move to. If no square has been clicked, the array is of length 0
        for(int move=0;move<legalSquaresToMoveTo.size();move=move+2){
            if(legalSquaresToMoveTo.get(move)==yBoard&&legalSquaresToMoveTo.get(move+1)==xBoard){//if square choosen is a move, it updates game object and board
                ArrayList<ChessBoardState>states=ChessGame.getAllLegalBoardsFromMove(yCoordSquareClicked, xCoordSquareClicked, yBoard, xBoard, game.getCurrentState());
                if(states.size()==1){
                    game.addState(states.get(0));
                }else{//used to allow user to make choice about pawn promotion
                    Object[] options = {"Queen","Rook","Bishop","Knight"};//stores the possible pieces to promote
                    int choice=JOptionPane.showOptionDialog(this,"Please select piece to promote pawn to.","Piece",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
                    if(choice==-1){//if no option is seleted, the queen is chosen
                        game.addState(states.get(0));
                    }else{
                        game.addState(states.get(choice));
                    }
                }
                //updates the board following the moving having been 
                currentIndex++;
                gameData[3]=gameData[3]+OfflinePlay.SPLIT_FEN_STRING+game.getCurrentState().getFenString();
                saveRecordToServer();
                updateBoard();
                updateFormBasedOnGameDataRecord();
                playMoveMadeSound();
                checkForTerminalState();
                return;
            }
        }
        //if invalid square is selected, nothing occurrs
        if((game.getCurrentState().BOARD[yBoard][xBoard]>=0&&game.getCurrentState().IS_BLACK_TURN_TO_PLAY_NEXT)||(game.getCurrentState().BOARD[yBoard][xBoard]<=0&&!game.getCurrentState().IS_BLACK_TURN_TO_PLAY_NEXT)){
            return;
        }
        if(yCoordSquareClicked==yBoard&&xCoordSquareClicked==xBoard){//if selected square is being clicked again, it will be deselected
            updateBoard();
            return;
        }
        updateBoard();
        //gets all legal moves from square pressed and highlights square pressed as well as any legal moves
        yCoordSquareClicked=yBoard;xCoordSquareClicked=xBoard;
        boardVisual[y][x].setBackground(Color.orange);
        legalSquaresToMoveTo=ChessGame.getAllLegalMovesFromSquare(yBoard, xBoard, game.getCurrentState());
        for(int move=0;move<legalSquaresToMoveTo.size();move=move+2){//goes through each legal move and highlights square
            if(isWhiteAtBottom){//accounts for flipped board
                boardVisual[legalSquaresToMoveTo.get(move)][legalSquaresToMoveTo.get(move+1)].setBackground(Color.blue);
            }else{
                boardVisual[7-legalSquaresToMoveTo.get(move)][7-legalSquaresToMoveTo.get(move+1)].setBackground(Color.blue);
            }
        }
    }
    /**
     * Adds listeners to each square on the board. This code is very long and has been generated by a computer. It has been collapsed as it takes up a large amount of space
     */
    private void addSquareListeners(){
        //<editor-fold defaultstate="collapsed" desc="Computer generated action listeners for each square of the board">
        boardVisual[0][0].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(0,0);}});
        boardVisual[0][1].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(0,1);}});
        boardVisual[0][2].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(0,2);}});
        boardVisual[0][3].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(0,3);}});
        boardVisual[0][4].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(0,4);}});
        boardVisual[0][5].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(0,5);}});
        boardVisual[0][6].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(0,6);}});
        boardVisual[0][7].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(0,7);}});
        boardVisual[1][0].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(1,0);}});
        boardVisual[1][1].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(1,1);}});
        boardVisual[1][2].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(1,2);}});
        boardVisual[1][3].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(1,3);}});
        boardVisual[1][4].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(1,4);}});
        boardVisual[1][5].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(1,5);}});
        boardVisual[1][6].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(1,6);}});
        boardVisual[1][7].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(1,7);}});
        boardVisual[2][0].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(2,0);}});
        boardVisual[2][1].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(2,1);}});
        boardVisual[2][2].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(2,2);}});
        boardVisual[2][3].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(2,3);}});
        boardVisual[2][4].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(2,4);}});
        boardVisual[2][5].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(2,5);}});
        boardVisual[2][6].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(2,6);}});
        boardVisual[2][7].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(2,7);}});
        boardVisual[3][0].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(3,0);}});
        boardVisual[3][1].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(3,1);}});
        boardVisual[3][2].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(3,2);}});
        boardVisual[3][3].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(3,3);}});
        boardVisual[3][4].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(3,4);}});
        boardVisual[3][5].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(3,5);}});
        boardVisual[3][6].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(3,6);}});
        boardVisual[3][7].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(3,7);}});
        boardVisual[4][0].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(4,0);}});
        boardVisual[4][1].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(4,1);}});
        boardVisual[4][2].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(4,2);}});
        boardVisual[4][3].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(4,3);}});
        boardVisual[4][4].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(4,4);}});
        boardVisual[4][5].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(4,5);}});
        boardVisual[4][6].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(4,6);}});
        boardVisual[4][7].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(4,7);}});
        boardVisual[5][0].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(5,0);}});
        boardVisual[5][1].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(5,1);}});
        boardVisual[5][2].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(5,2);}});
        boardVisual[5][3].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(5,3);}});
        boardVisual[5][4].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(5,4);}});
        boardVisual[5][5].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(5,5);}});
        boardVisual[5][6].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(5,6);}});
        boardVisual[5][7].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(5,7);}});
        boardVisual[6][0].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(6,0);}});
        boardVisual[6][1].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(6,1);}});
        boardVisual[6][2].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(6,2);}});
        boardVisual[6][3].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(6,3);}});
        boardVisual[6][4].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(6,4);}});
        boardVisual[6][5].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(6,5);}});
        boardVisual[6][6].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(6,6);}});
        boardVisual[6][7].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(6,7);}});
        boardVisual[7][0].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(7,0);}});
        boardVisual[7][1].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(7,1);}});
        boardVisual[7][2].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(7,2);}});
        boardVisual[7][3].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(7,3);}});
        boardVisual[7][4].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(7,4);}});
        boardVisual[7][5].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(7,5);}});
        boardVisual[7][6].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(7,6);}});
        boardVisual[7][7].addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent evt){squareClicked(7,7);}});        
        //</editor-fold>
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ChessBoardOnline.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChessBoardOnline.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChessBoardOnline.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChessBoardOnline.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ChessBoardOnline().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backBtn;
    private javax.swing.JLabel blackEloLbl;
    private javax.swing.JLabel blackLbl;
    private javax.swing.JLabel blackTimeLbl;
    private javax.swing.JLabel blackTimeLeftLbl;
    private javax.swing.JButton changeBoardBtn;
    private javax.swing.JButton flipBoardBtn;
    private javax.swing.JLabel gameTitleLbl;
    private javax.swing.JButton invisibleDrawBtn;
    private javax.swing.JLabel isBlackTurnLbl;
    private javax.swing.JLabel isWhiteTurnLbl;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel moveNumberLbl;
    private javax.swing.JButton nextBoardBtn;
    private javax.swing.JButton offerDrawBtn;
    private javax.swing.JButton refreshBtn;
    private javax.swing.JButton resignBtn;
    private javax.swing.JLabel speakerLbl;
    private javax.swing.JLabel timerLbl;
    private javax.swing.JLabel timerLbl1;
    private javax.swing.JButton viewPresentBoard;
    private javax.swing.JButton viewPreviousBoard;
    private javax.swing.JLabel whiteEloLbl;
    private javax.swing.JLabel whiteLbl;
    private javax.swing.JLabel whiteTiimeLbl;
    private javax.swing.JLabel whiteTimeLeftLbl;
    // End of variables declaration//GEN-END:variables
}
