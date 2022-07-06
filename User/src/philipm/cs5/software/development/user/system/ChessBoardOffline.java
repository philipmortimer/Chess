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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import philipm.cs5.software.development.server.system.UserInfo;

/**
 * Creates a form that displays and handles the offline chess game.
 * @author mortimer
 */
public class ChessBoardOffline extends javax.swing.JFrame {
    private boolean soundIsOn=true;//stores whether sound is on
    SessionInfo session;//stores the current session information
    private boolean isUserWhite;//stores whether the logged in user is playing as white
    private String opponentId;//stores the name of the opponent
    private ChessGame game;//this object stores the game state and handle the game logic
    public int currentIndex;//stores the current board index
    private int computerLevel;//stores the computer level, this is -1 if its against a person
    private JLabel boardVisual[][]=new JLabel[8][8];
    private final static String [] IMAGES_NAME = {"Black King","Black Queen","Black Rook","Black Bishop","Black Knight","Black Pawn","White King","White Queen","White Rook","White Bishop","White Knight","White Pawn"};//stores the file names of the images of the pieces
    private boolean isDefaultLayout=true;//stores which GUI layout the user is using
    private static ImageIcon imagePiece[] = new ImageIcon[12];//stores images of the the pieces
    private Timer timer;//timer that updates the clock
    private boolean isWhiteAtBottom;//stores whether white should be displayed at bottom of board
    private int secondsWhiteHas;
    private int secondsBlackHas;
    private String gameId;//stores the game title
    private int currentTime;//stores total time spen
    private Thread compMove=null;//thread that gets computer move
    private int yCoordSquareClicked=-1;private int xCoordSquareClicked=-1;//stores the coordinates of the square previously clicked. If no such square exsists, they are equal to -1 and -1
    private ArrayList<Integer>legalSquaresToMoveTo = new ArrayList<>();//stores legal squares to move to from current square
    private boolean accountForPlayerTime;//stores whether the time each player has spent needs to be tracked
    private ImageIcon muteIcon;private ImageIcon soundOnIcon;//stores icons for sound setting
    private static final String MUTE_PICTURE="mute.png";//stores file name for mute icon
    private static final String SOUND_ON_PICTURE="soundOn.png";//stores file name for sound on image
    private static final String BOARD_SETTING="boardSetting.txt";//stores file name for board settings
    /**
     * Creates new form ChessBoardOffline
     */
    private ChessBoardOffline() {
        initComponents();
        //game id,opponentID, isUserWhite, boardState(s),time game has taken, time white has left, time black has left
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
     * Creates a new chess board.
     * @param session The session information
     * @param opponentId The type of opponent. This is used to display their name and to determine whether the computer must generate moves
     * @param chess The chess game to represent
     * @param isUserWhite Stores whether the user is white. This is used to determine when computer moves are needed. If this variable is true, the board will be displayed so that white is on the bottom.
     * @param currentTimeGame The number of seconds the game has been played for
     * @param secondsWhiteHasLeft The number of seconds white has left. If this is unlimited, set this to -100
     * @param secondsBlackHasLeft The number of seconds black has left. If this is unlimited, set this to -100
     * @param gameId The game id
     */
    public ChessBoardOffline(SessionInfo session, String opponentId, ChessGame chess, boolean isUserWhite,int currentTimeGame,int secondsWhiteHasLeft, int secondsBlackHasLeft, String gameId){
        updateLayoutVariableBasedOnTextFile();//sets variable that controls the look of the chess board
        //sets certain variable from the parameter to object variables
        this.session = session;this.opponentId=opponentId;this.game=chess;this.isUserWhite=isUserWhite;
        this.secondsWhiteHas=secondsWhiteHasLeft;this.secondsBlackHas=secondsBlackHasLeft;
        this.gameId=gameId;
        computerLevel=-1;//-1 indicates that the opponent is a person
        //calculates the computer level
        if(opponentId.equals(OfflinePlay.LEVEL_ONE_COMP)){
            computerLevel=1;
        }else if(opponentId.equals(OfflinePlay.LEVEL_TWO_COMP)){
            computerLevel=2;
        }else if(opponentId.equals(OfflinePlay.LEVEL_THREE_COMP)){
            computerLevel=3;
        }else if(opponentId.equals(OfflinePlay.LEVEL_FOUR_COMP)){
            computerLevel=4;
        }else if(opponentId.equals(OfflinePlay.LEVEL_FIVE_COMP)){
            computerLevel=5;
        }
        currentTime=currentTimeGame;//stores the current time
        initComponents();
        //decides whether or not certain time components should be displayed depedning on whether time control is being used
        if(secondsBlackHas==-100){
            blackTimeLbl.setVisible(false);blackTimeLeftLbl.setVisible(false);
            whiteTiimeLbl.setVisible(false);whiteTimeLeftLbl.setVisible(false);
        }
        gameTitleLbl.setText("Game Title: "+gameId);//sets the game title
        currentIndex=chess.getLengthOfStatesFoFar()-1;
        isWhiteAtBottom=isUserWhite;
        initialiseBoard();
        if(secondsBlackHasLeft==-100){//stores whether time is limited or not
            accountForPlayerTime=false;
        }else{
            accountForPlayerTime=true;
        }
        setTimers();
        timer = new Timer(1000, new ActionListener() {//timer that updates the timer every second
            @Override
            public void actionPerformed(ActionEvent e) {
                //this timer updates the overall time and also the time each individual player has left if relevant
                currentTime++;
                if(accountForPlayerTime){
                    if(game.getCurrentState().IS_BLACK_TURN_TO_PLAY_NEXT){
                        secondsBlackHas--;
                        if(secondsBlackHas<=0){
                            //deletes game and returns to menu as player has run out of time
                            secondsBlackHas=0;
                            timer.stop();
                            timer =null;
                            setTimers();
                            JOptionPane.showMessageDialog(null,"Black has run out of time. White wins!","White Wins",JOptionPane.OK_OPTION);
                            deleteGameAndReturnToMenu();
                        }
                    }else{
                        secondsWhiteHas--;
                        if(secondsWhiteHas<=0){
                            //deletes game and returns to menu as player has run out of time
                            secondsWhiteHas=0;
                            timer.stop();
                            timer=null;
                            JOptionPane.showMessageDialog(null,"White has run out of time. Black wins!","Black Wins",JOptionPane.OK_OPTION);
                            deleteGameAndReturnToMenu();
                        }
                    }
                }
                setTimers();//updates timers
            }
        });timer.start();
        if(this.isUserWhite){//sets label illustrating who is who
            whiteLbl.setText("White: You");
            blackLbl.setText("Black: "+this.opponentId);
        }else{
            whiteLbl.setText("White: "+this.opponentId);
            blackLbl.setText("Black: You");
        }
        updateImageArrayBasedOnVaraible();//updates the look of board based on which style the user is currently using
        updateBoard();
        updateSoundIcon();
        addSquareListeners();
        this.jPanel1.setDoubleBuffered(true);
        getComputerToMoveIfAppropriate();//gets the computer move if this is a game against a computer
    }
    /**
     * Changes piece image array based on what visual style has been chosen.
     */
    private void updateImageArrayBasedOnVaraible(){
        try{
            File fileOfPiece=null;//stores file where image is stored
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
        }catch(IOException e){
            JOptionPane.showMessageDialog(this, "An unexpected error occured when loading the image of pieces: "+e, "Error", JOptionPane.OK_OPTION);
        }         
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
        resignBtn.setEnabled(true);
        if(computerLevel!=-1){
            if((isUserWhite&&game.getCurrentState().IS_BLACK_TURN_TO_PLAY_NEXT)||(!isUserWhite&&!game.getCurrentState().IS_BLACK_TURN_TO_PLAY_NEXT)){
                resignBtn.setEnabled(false);
            }
        }
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
                //sets the icons of the chess board depending on what piece is currently on the square
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
        //if the king is in check, the square of the king is highlighted red
        if(ChessGame.isInCheck(!state.IS_BLACK_TURN_TO_PLAY_NEXT, new ChessBoardState(state.BOARD, state.CASTLING_RIGHTS, !state.IS_BLACK_TURN_TO_PLAY_NEXT, state.FULL_MOVE_NUMBER, state.HALF_MOVE_CLOCK, state.SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT))){//if king is in check, its square is coloured red
            boardVisual[yKing][xKing].setBackground(Color.red);
        }
    }
    /**
     * Checks to see if a computer move is needed. If it is, the computer makes a move and updates the board
     */
    private void getComputerToMoveIfAppropriate(){
        if(computerLevel!=-1 && ((isUserWhite&&game.getCurrentState().IS_BLACK_TURN_TO_PLAY_NEXT)||(!isUserWhite&&!game.getCurrentState().IS_BLACK_TURN_TO_PLAY_NEXT))){
            //creates a new thread and updates the board once move has been calculated
            GetComputerMove com = new GetComputerMove(game, this, computerLevel);//creates a thread that processes the move
            compMove = new Thread(com);compMove.start();
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
        timerLbl.setText(h+":"+m+":"+s);//sets time in hours, minutes and seconds format
        if(accountForPlayerTime){//sets time left for each colour if relevant
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
     * @param board The board (must be an 8*8 array for this rotation method to work)
     */
    private static void rotateMatrix(int[][] board) {
        int temp;//used as a buffer
        //rotates 8*8 board
        for (int i = 0; i <= 3; i++) {
            for (int j = 0; j < 8; j++) {
                temp = board[i][j];
                board[i][j] = board[7 - i ][7- j];
                board[7-i][7- j] = temp;
            }
        }
    }
    /**
     * This methods creates the board pieces, adds appropriate action listeners and is used to initialise the board. It also changes colour of squares
     * depending on whether the user is white.
     */
    private void initialiseBoard(){
        //creates JLabels and loads them to array
        final int WIDTH_OF_SQUARE=56;//stores the width of the square 56
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
                boardVisual[y][x]=new BoardJLabel();//creates the BoardJLabel that will display a square of the chess board
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
        //loads all images and stores them (i.e. of the pieces and sound icons)
        try{
            File fileOfPiece;//stores file where image is stored
            BufferedImage bufferedImagePiece=null;//stores a buffered image of the piece
            Image imgPiece=null;//stores the actual image of the piece
             for(int i = 0; i<IMAGES_NAME.length;i++){
                fileOfPiece = new File("Assets"+File.separator+"oldPieceImages"+File.separator+IMAGES_NAME[i] +".png");//finds file
                bufferedImagePiece = ImageIO.read(fileOfPiece);
                //imgPiece = bufferedImagePiece.getScaledInstance(WIDTH_OF_SQUARE, HEIGHT_OF_SQUARE , Image.SCALE_SMOOTH);//loads image of piece and scales it to size
                //imageIconPiece = new ImageIcon(bufferedImagePiece); 
                CustomImageIconBoard cust = new CustomImageIconBoard(bufferedImagePiece);
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
        speakerLbl = new BoardJLabel()
        ;
        changeBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Offline Game");
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

        gameTitleLbl.setText("Game Title: ");
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

        changeBtn.setText("Change Board");
        changeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(557, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(isWhiteTurnLbl)
                            .addComponent(isBlackTurnLbl))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(blackLbl)
                            .addComponent(whiteLbl)
                            .addComponent(moveNumberLbl))
                        .addGap(107, 107, 107))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(blackTimeLbl)
                            .addComponent(whiteTiimeLbl))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(whiteTimeLeftLbl)
                            .addComponent(blackTimeLeftLbl))
                        .addGap(253, 253, 253))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(resignBtn)
                                .addGap(60, 60, 60)
                                .addComponent(speakerLbl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(viewPreviousBoard)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(viewPresentBoard)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(nextBoardBtn)))
                        .addGap(25, 25, 25))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addComponent(changeBtn)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(backBtn)
                            .addContainerGap())
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addComponent(flipBoardBtn)
                            .addGap(321, 321, 321)))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(gameTitleLbl)
                .addGap(184, 184, 184)
                .addComponent(timerLbl1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(timerLbl)
                .addGap(49, 49, 49))
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
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(blackLbl)
                    .addComponent(isBlackTurnLbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(whiteLbl)
                    .addComponent(isWhiteTurnLbl))
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
                        .addComponent(resignBtn))
                    .addComponent(speakerLbl, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addComponent(flipBoardBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(backBtn)
                    .addComponent(changeBtn))
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
        //saves the game and takes the user back to the menu
        saveBoardToFile();
        OfflinePlay offline = new OfflinePlay(session);
        offline.setVisible(true);
        this.dispose();
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
            if(game.getCurrentState().IS_BLACK_TURN_TO_PLAY_NEXT){
                JOptionPane.showMessageDialog(this, "Black resigned - White wins!","White Wins!",JOptionPane.OK_OPTION);
            }else{
                JOptionPane.showMessageDialog(this, "White resigned - Black wins!","Black Wins!",JOptionPane.OK_OPTION);
            }
            deleteGameAndReturnToMenu();
        }
    }//GEN-LAST:event_resignBtnActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        if(compMove!=null){//kills thread that gets computer move if appropriate when closing the window
            compMove.interrupt();
        }
        compMove=null;
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
      //if a user closes the window by pressing the cross in the top right corner, this method is invoked. It saves the board state
      saveBoardToFile();
      if(compMove!=null){
            compMove.interrupt();
        }
        compMove=null;
    }//GEN-LAST:event_formWindowClosing

    private void speakerLblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_speakerLblMouseClicked
    //updates whether the sound is on or not. It also changes the relevant sound icon to show this.
    soundIsOn=!soundIsOn;
    updateSoundIcon();
    }//GEN-LAST:event_speakerLblMouseClicked

    private void changeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeBtnActionPerformed
        // updates the variable is Default layout and stores it to a text file. This basically changes how the pieces and squares look
        isDefaultLayout=!isDefaultLayout;
        try{
            FileWriter write = new FileWriter(BOARD_SETTING,false);//saves the user's prefered board style to a text file
            BufferedWriter buffWrite = new BufferedWriter(write);
            buffWrite.write(String.valueOf(isDefaultLayout));
            buffWrite.flush();write.flush();
            buffWrite.close();write.close();
        }catch(IOException e){
            System.out.println(e);
        }
        updateImageArrayBasedOnVaraible();
        updateBoard();//updates board
    }//GEN-LAST:event_changeBtnActionPerformed
    /**
     * Saves the current board state to the offline game text file.
     */
    private void saveBoardToFile(){
        String fileContents="";//stores the contents of the offline game text file
        String updatedRecord=gameId+UserInfo.SPLIT_ITEM+opponentId+UserInfo.SPLIT_ITEM+String.valueOf(isUserWhite)+UserInfo.SPLIT_ITEM+game.getState(0).getFenString();//stores updated record
        for(int i=1;i<game.getLengthOfStatesFoFar();i++){//adds all board states to file
            updatedRecord=updatedRecord+OfflinePlay.SPLIT_FEN_STRING+game.getState(i).getFenString();
        }
        updatedRecord=updatedRecord+UserInfo.SPLIT_ITEM+String.valueOf(currentTime)+UserInfo.SPLIT_ITEM+String.valueOf(secondsWhiteHas)+UserInfo.SPLIT_ITEM+String.valueOf(secondsBlackHas);
        try{
            FileReader read =new FileReader(OfflinePlay.OFFLINE_GAMES_FOLDER+File.separator+session.getUsername()+".txt");
            BufferedReader buffRead = new BufferedReader(read);String lineRead;
            while((lineRead=buffRead.readLine())!=null){
                if(lineRead.split(UserInfo.SPLIT_ITEM)[0].equals(gameId)){//if record is the same as the current game
                    lineRead=updatedRecord;
                }
                if(fileContents.equals("")){//writes updated content to file
                    fileContents=lineRead;
                }else{
                    fileContents=fileContents+System.lineSeparator()+lineRead;
                }
            }buffRead.close();read.close();
            //writes the new text file
            FileWriter write = new FileWriter(OfflinePlay.OFFLINE_GAMES_FOLDER+File.separator+session.getUsername()+".txt",false);
            BufferedWriter buffWrite = new BufferedWriter(write);
            buffWrite.write(fileContents);
            buffWrite.flush();write.flush();
            buffWrite.close();write.close();
        }catch(IOException e){
            JOptionPane.showMessageDialog(this, "An unexpected error occured: "+e,"Error",JOptionPane.OK_OPTION);
        }        
    }
    /**
     * Checks to see if current board is in a terminal state. If it is, the game result is announced and then the system terminates
     */
    public void checkForTerminalState(){
        if(game.getAllLegalBoardsFromState(game.getCurrentState()).size()==0){//checks to see if there are any legal moves from the current board state - if there are not then the game must be over
            timer.stop();//stops the timers
            if(game.getCurrentState().getIsDraw()){
                //displays that the game was a draw
                JOptionPane.showMessageDialog(this, "The game was a draw!","Draw!",JOptionPane.OK_OPTION);
                deleteGameAndReturnToMenu();
                return;
            }
            if(ChessGame.isInCheck(!game.getCurrentState().IS_BLACK_TURN_TO_PLAY_NEXT, new ChessBoardState(game.getCurrentState().BOARD, game.getCurrentState().CASTLING_RIGHTS, !game.getCurrentState().IS_BLACK_TURN_TO_PLAY_NEXT, game.getCurrentState().FULL_MOVE_NUMBER, game.getCurrentState().HALF_MOVE_CLOCK, game.getCurrentState().SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT))){
                //if the user is in check, then the outcome of the game must be checkmate
                if(game.getCurrentState().IS_BLACK_TURN_TO_PLAY_NEXT){
                    JOptionPane.showMessageDialog(this, "White wins!","Checkmate!",JOptionPane.OK_OPTION);
                    deleteGameAndReturnToMenu();
                    return;
                }else{
                    JOptionPane.showMessageDialog(this, "Black wins.","Checkmate!",JOptionPane.OK_OPTION);
                    deleteGameAndReturnToMenu();
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "The game was a draw!","Draw!",JOptionPane.OK_OPTION);
            deleteGameAndReturnToMenu();
            return;
        }
    }
    /**
     * This method deletes the game and returns to the offline play menu.
     */
    private void deleteGameAndReturnToMenu(){
        String fileContents="";//stores contents of new file
        String lineRead;
        try{
            //gets new file contents - which is the same as the old file contents just without the current game record
            FileReader read = new FileReader(OfflinePlay.OFFLINE_GAMES_FOLDER+File.separator+session.getUsername()+".txt");
            BufferedReader buffRead = new BufferedReader(read);
            while((lineRead=buffRead.readLine())!=null){
                //loops through the file and appends the line to the file if it is not about the current game
                if(!lineRead.split(UserInfo.SPLIT_ITEM)[0].equals(gameId)){
                    if(fileContents.equals("")){
                        fileContents=lineRead;
                    }else{
                        fileContents=fileContents+System.lineSeparator()+lineRead;
                    }
                }
            }buffRead.close();read.close();
            FileWriter write = new FileWriter(OfflinePlay.OFFLINE_GAMES_FOLDER+File.separator+session.getUsername()+".txt",false);//writes the updated offline game records to the file
            BufferedWriter buffWrite = new BufferedWriter(write);
            buffWrite.write(fileContents);
            buffWrite.flush();write.flush();
            buffWrite.close();write.close();//closes and flushes writers
        }catch(IOException e){
            JOptionPane.showMessageDialog(this,"An unexpected error occurred. "+e,"Error",JOptionPane.OK_OPTION);
        }
        //takes user back to menu
        OfflinePlay off = new OfflinePlay(session);
        off.setVisible(true);this.dispose();return;
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
        if((computerLevel!=-1)&&((isUserWhite&&game.getCurrentState().IS_BLACK_TURN_TO_PLAY_NEXT)||(!isUserWhite&&!game.getCurrentState().IS_BLACK_TURN_TO_PLAY_NEXT))){
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
                //updates board following the move having been made
                currentIndex++;
                updateBoard();
                playMoveMadeSound();
                checkForTerminalState();
                getComputerToMoveIfAppropriate();
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
            java.util.logging.Logger.getLogger(ChessBoardOffline.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChessBoardOffline.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChessBoardOffline.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChessBoardOffline.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ChessBoardOffline().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backBtn;
    private javax.swing.JLabel blackLbl;
    private javax.swing.JLabel blackTimeLbl;
    private javax.swing.JLabel blackTimeLeftLbl;
    private javax.swing.JButton changeBtn;
    private javax.swing.JButton flipBoardBtn;
    private javax.swing.JLabel gameTitleLbl;
    private javax.swing.JLabel isBlackTurnLbl;
    private javax.swing.JLabel isWhiteTurnLbl;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel moveNumberLbl;
    private javax.swing.JButton nextBoardBtn;
    private javax.swing.JButton resignBtn;
    private javax.swing.JLabel speakerLbl;
    private javax.swing.JLabel timerLbl;
    private javax.swing.JLabel timerLbl1;
    private javax.swing.JButton viewPresentBoard;
    private javax.swing.JButton viewPreviousBoard;
    private javax.swing.JLabel whiteLbl;
    private javax.swing.JLabel whiteTiimeLbl;
    private javax.swing.JLabel whiteTimeLeftLbl;
    // End of variables declaration//GEN-END:variables
}
