/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package philipm.cs5.software.development.user.system;

/**
 * This class is used to store all data needed for a single board state
 * @author mortimer
 */
public class ChessBoardState {
    public final int BOARD[][]= new int[8][8];//stores chess board  white always starts at bottom
    public boolean IS_BLACK_TURN_TO_PLAY_NEXT;//stores which colour is to move next
    public final boolean[] CASTLING_RIGHTS=new boolean[4];//stores the castling rights. Stored in format: White kingside, white queenside, black kingside, black queenside
    public final int FULL_MOVE_NUMBER;//the numbe of moves black has made. This value starts at one
    public final int HALF_MOVE_CLOCK;//the number of moves made since a piece was capture or a pawn advanced a square. This is used to enforce the fifty move rule. This value starts at 0
    public final  int[]SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT;//stores sqaure that can be captured via en passant. Has a length of 0 if no such squares are available
    private  String fenString;//stores fen string of board state
    private boolean isDraw;//stores whether the state is a draw. Note this must be set externally
    private boolean isWin;//stores whether state is a win for player currently moving. Note this must be set externally
    private boolean isLoss;//stores whether state is a loss for player currently moving. Note this must be set externally 
    private boolean hasFenBeenCreated=false;
    public static final int WHITE_MULTIPLIER=1;public static final int BLACK_MULTIPLIER=-1;
    public static final int ROOK_VALUE=5;public static final int BISHOP_VALUE =4;public static final int KNIGHT_VALUE=3;
    public static final int PAWN_VALUE=1;
    public static final int QUEEN_VALUE=8;public static final int KING_VALUE=100; public static final int EMPTY_VALUE=0;
    /**
     * Takes the FEN String (standard representation of chess board as input)
     * @param fenString The FEN string. For explanation of FEN format, view https://www.chess.com/terms/fen-chess
     */
    public ChessBoardState (String fenString){
        this.fenString=fenString;hasFenBeenCreated=true;
        String[]fenSplit = fenString.split(" ");//splits the FEN String into its fields
        //generates board based on FEN String
        String board[]=fenSplit[0].split("/");//splits the board data into ranks
        int xIndex;//stores current x index of piece
        //converts fen representation of board to an 8 by 8 two dimensional array which represents the board
        for(int y=0;y<8;y++){//generates board data for each rank
            xIndex=0;
            for(int ch=0;ch<board[y].length();ch++){
                switch (board[y].charAt(ch)) {
                    case 'r':
                        BOARD[y][xIndex]=BLACK_MULTIPLIER*ROOK_VALUE;
                        xIndex++;
                        break;
                    case 'n':
                        BOARD[y][xIndex]=BLACK_MULTIPLIER*KNIGHT_VALUE;
                        xIndex++;
                        break;
                    case 'b':
                        BOARD[y][xIndex]=BLACK_MULTIPLIER*BISHOP_VALUE;
                        xIndex++;
                        break;
                    case 'q':
                        BOARD[y][xIndex]=BLACK_MULTIPLIER*QUEEN_VALUE;
                        xIndex++;
                        break;
                    case 'k':
                        BOARD[y][xIndex]=BLACK_MULTIPLIER*KING_VALUE;
                        xIndex++;
                        break;
                    case 'p':
                        BOARD[y][xIndex]=BLACK_MULTIPLIER*PAWN_VALUE;
                        xIndex++;
                        break;
                    case 'N':
                        BOARD[y][xIndex]=WHITE_MULTIPLIER*KNIGHT_VALUE;
                        xIndex++;
                        break;
                    case 'B':
                        BOARD[y][xIndex]=WHITE_MULTIPLIER*BISHOP_VALUE;
                        xIndex++;
                        break;
                    case 'Q':
                        BOARD[y][xIndex]=WHITE_MULTIPLIER*QUEEN_VALUE;
                        xIndex++;
                        break;
                    case 'K':
                        BOARD[y][xIndex]=WHITE_MULTIPLIER*KING_VALUE;
                        xIndex++;
                        break;
                    case 'R':
                        BOARD[y][xIndex]=WHITE_MULTIPLIER*ROOK_VALUE;
                        xIndex++;
                        break;
                    case 'P':
                        BOARD[y][xIndex]=WHITE_MULTIPLIER*PAWN_VALUE;
                        xIndex++;
                        break;
                    default:
                        for(int emp=0;emp<Character.getNumericValue(board[y].charAt(ch));emp++){
                            BOARD[y][xIndex]=EMPTY_VALUE;
                            xIndex++;
                        }   break;
                }
            }
        }
        IS_BLACK_TURN_TO_PLAY_NEXT=fenSplit[1].equals("b");//gets who's move it is
        //gets castling rights
        //stored in format White kingside, white queenside, black kingside, black queenside
        CASTLING_RIGHTS[0]=false;CASTLING_RIGHTS[1]=false;CASTLING_RIGHTS[2]=false;CASTLING_RIGHTS[3]=false;
        if(!fenSplit[2].equals("-")){
            for(int ch =0;ch<fenSplit[2].length();ch++){
                switch (fenSplit[2].charAt(ch)) {
                    case 'K':
                        CASTLING_RIGHTS[0]=true;
                        break;
                    case 'Q':
                        CASTLING_RIGHTS[1]=true;
                        break;
                    case 'k':
                        CASTLING_RIGHTS[2]=true;
                        break;
                    default:
                        CASTLING_RIGHTS[3]=true;
                        break;
                }
            }
        }
        //gets possible en passant targets
        if(fenSplit[3].equals("-")){
            SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT=new int[0];//no available squares
        }else{
            SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT = getYAndXOfSquare(fenSplit[3]);//one available en passant square
        }
        //gets half move clock
        HALF_MOVE_CLOCK=Integer.parseInt(fenSplit[4]);
        //gets full move number
        FULL_MOVE_NUMBER=Integer.parseInt(fenSplit[5]);
    }
    /**
     * Creates a new chess board state
     * @param gameBoard The board array
     * @param castlingRights The castling rights. In the form, white kingside, white queen side, black kingside, black queen side
     * @param isBlackToPlayNext whether black is to make the next move
     * @param fullMoveNumber the number of moves black has played
     * @param halfMoveClock the number of moves both players have made since the last pawn advance or capture
     * @param enPassantSquare the y and x coordinates of a square that can be captured via en passant. If no such square exists, use new int[0]
     */
    public ChessBoardState(int[][]gameBoard,boolean castlingRights[],boolean isBlackToPlayNext, int fullMoveNumber,int halfMoveClock,int[]enPassantSquare){
        //stores board
        for(int y=0;y<8;y++){//generates board
            for(int x =0;x<8;x++){
                BOARD[y][x]=gameBoard[y][x];
            }
        }
        //creates castling rights
        CASTLING_RIGHTS[0]=castlingRights[0];CASTLING_RIGHTS[1]=castlingRights[1];CASTLING_RIGHTS[2]=castlingRights[2];CASTLING_RIGHTS[3]=castlingRights[3];
        IS_BLACK_TURN_TO_PLAY_NEXT=isBlackToPlayNext;
        FULL_MOVE_NUMBER=fullMoveNumber;
        HALF_MOVE_CLOCK=halfMoveClock;
        //creates en passant data
        if(enPassantSquare.length==0){
            SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT= new int[0];
        }else{
            SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT=new int [2];
            SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT[0]=enPassantSquare[0];
            SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT[1]=enPassantSquare[1];
        }
    }
    /**
     * Converts the current board state to a form where it can be input to the neural network.
     * @return The double array that can be input to the trained neural network.
     */
    public double[]getNeuralNetInputDouble(){
        double data[]= new double [777];//creates the array that stores the neural network attributes
        //stores castling rights
        if(CASTLING_RIGHTS[0]){
            data[768]=1;
        }
        if(CASTLING_RIGHTS[1]){
            data[769]=1;
        }
        if(CASTLING_RIGHTS[2]){
            data[770]=1;
        }
        if(CASTLING_RIGHTS[3]){
            data[771]=1;
        }
        //stores who is going to play next
        if(IS_BLACK_TURN_TO_PLAY_NEXT){
            data[772]=1;
        }
        //sets half move data
        data[773]=HALF_MOVE_CLOCK;
        //sets en passant data
        data[775]=9;data[776]=9;
        if(SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT.length==2){
            data[774]=1;data[775]=SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT[0];data[776]=SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT[1];
        }
        //generates bitboard data
        for(int y=0;y<8;y++){
            for(int x=0;x<8;x++){
                switch (BOARD[y][x]) {
                    case WHITE_MULTIPLIER*KING_VALUE:
                        data[x+(8*y)]=1;
                        break;
                    case BLACK_MULTIPLIER*KING_VALUE:
                        data[x+(8*y)+64]=1;
                        break;
                    case WHITE_MULTIPLIER*ROOK_VALUE:
                        data[x+(8*y)+128]=1;
                        break;
                    case BLACK_MULTIPLIER*ROOK_VALUE:
                        data[x+(8*y)+192]=1;
                        break;
                    case WHITE_MULTIPLIER*BISHOP_VALUE:
                        data[x+(8*y)+256]=1;
                        break;
                    case BLACK_MULTIPLIER*BISHOP_VALUE:
                        data[x+(8*y)+320]=1;
                        break;
                    case WHITE_MULTIPLIER*QUEEN_VALUE:
                        data[x+(8*y)+384]=1;
                        break;
                    case BLACK_MULTIPLIER*QUEEN_VALUE:
                        data[x+(8*y)+448]=1;
                        break;
                    case WHITE_MULTIPLIER*PAWN_VALUE:
                        data[x+(8*y)+512]=1;
                        break;
                    case BLACK_MULTIPLIER*PAWN_VALUE:
                        data[x+(8*y)+576]=1;
                        break;
                    case WHITE_MULTIPLIER*KNIGHT_VALUE:
                        data[x+(8*y)+640]=1;
                        break;
                    case BLACK_MULTIPLIER*KNIGHT_VALUE:
                        data[x+(8*y)+704]=1;
                        break;
                    default:
                        break;
                }
            }
        }
        return data;        
    }
    /**
     * Gets the string used for the neural network input given the current board state
     * @return The input to the neural network
     */
    public int[] getNeuralNetInput(){
        int data[]= new int [777];//creates the array that stores the neural network attributes
        //stores castling rights
        if(CASTLING_RIGHTS[0]){
            data[768]=1;
        }
        if(CASTLING_RIGHTS[1]){
            data[769]=1;
        }
        if(CASTLING_RIGHTS[2]){
            data[770]=1;
        }
        if(CASTLING_RIGHTS[3]){
            data[771]=1;
        }
        //stores who is going to play next
        if(IS_BLACK_TURN_TO_PLAY_NEXT){
            data[772]=1;
        }
        //sets half move data
        data[773]=HALF_MOVE_CLOCK;
        //sets en passant data
        data[775]=9;data[776]=9;
        if(SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT.length==2){
            data[774]=1;data[775]=SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT[0];data[776]=SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT[1];
        }
        //generates bitboard data
        for(int y=0;y<8;y++){
            for(int x=0;x<8;x++){
                switch (BOARD[y][x]) {
                    case WHITE_MULTIPLIER*KING_VALUE:
                        data[x+(8*y)]=1;
                        break;
                    case BLACK_MULTIPLIER*KING_VALUE:
                        data[x+(8*y)+64]=1;
                        break;
                    case WHITE_MULTIPLIER*ROOK_VALUE:
                        data[x+(8*y)+128]=1;
                        break;
                    case BLACK_MULTIPLIER*ROOK_VALUE:
                        data[x+(8*y)+192]=1;
                        break;
                    case WHITE_MULTIPLIER*BISHOP_VALUE:
                        data[x+(8*y)+256]=1;
                        break;
                    case BLACK_MULTIPLIER*BISHOP_VALUE:
                        data[x+(8*y)+320]=1;
                        break;
                    case WHITE_MULTIPLIER*QUEEN_VALUE:
                        data[x+(8*y)+384]=1;
                        break;
                    case BLACK_MULTIPLIER*QUEEN_VALUE:
                        data[x+(8*y)+448]=1;
                        break;
                    case WHITE_MULTIPLIER*PAWN_VALUE:
                        data[x+(8*y)+512]=1;
                        break;
                    case BLACK_MULTIPLIER*PAWN_VALUE:
                        data[x+(8*y)+576]=1;
                        break;
                    case WHITE_MULTIPLIER*KNIGHT_VALUE:
                        data[x+(8*y)+640]=1;
                        break;
                    case BLACK_MULTIPLIER*KNIGHT_VALUE:
                        data[x+(8*y)+704]=1;
                        break;
                    default:
                        break;
                }
            }
        }
        return data;
    }
    /**
     * Gets FEN string from board state
     * @return The FEN String
     */
    public String getFenString(){
        if(this.hasFenBeenCreated){//if a fen value has already been created then it is simply returned
            return this.fenString;
        }
        //creates a fen string from scratch
        String fenStringBoard="";//stores board as FEN string
        int noOfEmptySquares;//stores the number of empty squares next to each other in a row
        for(int y=0;y<8;y++){//generates board and FEN String for the board
            if(y!=0){
                fenStringBoard=fenStringBoard+"/";//splits the ranks
            }
            noOfEmptySquares=0;
            //for each square in the rank, the correct fen symbol is calculated
            for(int x =0;x<8;x++){
                switch (BOARD[y][x]) {
                    case EMPTY_VALUE:
                        noOfEmptySquares++;
                        if(x==7){
                            fenStringBoard=fenStringBoard+String.valueOf(noOfEmptySquares);
                        }   break;
                    case BLACK_MULTIPLIER*ROOK_VALUE:
                        if(noOfEmptySquares!=0){
                            fenStringBoard=fenStringBoard+String.valueOf(noOfEmptySquares);
                            noOfEmptySquares=0;
                        }   fenStringBoard=fenStringBoard+"r";
                        break;
                    case BLACK_MULTIPLIER*KNIGHT_VALUE:
                        if(noOfEmptySquares!=0){
                            fenStringBoard=fenStringBoard+String.valueOf(noOfEmptySquares);
                            noOfEmptySquares=0;
                        }   fenStringBoard=fenStringBoard+"n";
                        break;
                    case BLACK_MULTIPLIER*BISHOP_VALUE:
                        if(noOfEmptySquares!=0){
                            fenStringBoard=fenStringBoard+String.valueOf(noOfEmptySquares);
                            noOfEmptySquares=0;
                        }   fenStringBoard=fenStringBoard+"b";
                        break;
                    case BLACK_MULTIPLIER*QUEEN_VALUE:
                        if(noOfEmptySquares!=0){
                            fenStringBoard=fenStringBoard+String.valueOf(noOfEmptySquares);
                            noOfEmptySquares=0;
                        }   fenStringBoard=fenStringBoard+"q";
                        break;
                    case BLACK_MULTIPLIER*KING_VALUE:
                        if(noOfEmptySquares!=0){
                            fenStringBoard=fenStringBoard+String.valueOf(noOfEmptySquares);
                            noOfEmptySquares=0;
                        }   fenStringBoard=fenStringBoard+"k";
                        break;
                    case BLACK_MULTIPLIER*PAWN_VALUE:
                        if(noOfEmptySquares!=0){
                            fenStringBoard=fenStringBoard+String.valueOf(noOfEmptySquares);
                            noOfEmptySquares=0;
                        }   fenStringBoard=fenStringBoard+"p";
                        break;
                    case WHITE_MULTIPLIER*ROOK_VALUE:
                        if(noOfEmptySquares!=0){
                            fenStringBoard=fenStringBoard+String.valueOf(noOfEmptySquares);
                            noOfEmptySquares=0;
                        }   fenStringBoard=fenStringBoard+"R";
                        break;
                    case WHITE_MULTIPLIER*KNIGHT_VALUE:
                        if(noOfEmptySquares!=0){
                            fenStringBoard=fenStringBoard+String.valueOf(noOfEmptySquares);
                            noOfEmptySquares=0;
                        }   fenStringBoard=fenStringBoard+"N";
                        break;
                    case WHITE_MULTIPLIER*BISHOP_VALUE:
                        if(noOfEmptySquares!=0){
                            fenStringBoard=fenStringBoard+String.valueOf(noOfEmptySquares);
                            noOfEmptySquares=0;
                        }   fenStringBoard=fenStringBoard+"B";
                        break;
                    case WHITE_MULTIPLIER*QUEEN_VALUE:
                        if(noOfEmptySquares!=0){
                            fenStringBoard=fenStringBoard+String.valueOf(noOfEmptySquares);
                            noOfEmptySquares=0;
                        }   fenStringBoard=fenStringBoard+"Q";
                        break;
                    case WHITE_MULTIPLIER*KING_VALUE:
                        if(noOfEmptySquares!=0){
                            fenStringBoard=fenStringBoard+String.valueOf(noOfEmptySquares);
                            noOfEmptySquares=0;
                        }   fenStringBoard=fenStringBoard+"K";
                        break;
                    case WHITE_MULTIPLIER*PAWN_VALUE:
                        if(noOfEmptySquares!=0){
                            fenStringBoard=fenStringBoard+String.valueOf(noOfEmptySquares);
                            noOfEmptySquares=0;
                        }   fenStringBoard=fenStringBoard+"P";
                        break;
                    default:
                        break;
                }
            }
        }
        //creates the castling rights
        String fenCastlingRights="";
        if(CASTLING_RIGHTS[0]){
            fenCastlingRights="K";
        }
        if(CASTLING_RIGHTS[1]){
            fenCastlingRights=fenCastlingRights+"Q";
        }
        if(CASTLING_RIGHTS[2]){
            fenCastlingRights=fenCastlingRights+"k";
        }
        if(CASTLING_RIGHTS[3]){
            fenCastlingRights=fenCastlingRights+"q";
        }
        if(fenCastlingRights.equals("")){
            fenCastlingRights="-";
        }
        String fenColourToPlay="";
        //sets who's turn it is to play next
        if(IS_BLACK_TURN_TO_PLAY_NEXT){
            fenColourToPlay="b";
        }else{
            fenColourToPlay="w";
        }
        //stores the square (if any) on which en passant can be performed on
        String enPassantSquareFen="-";
        if(SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT.length==2){
            enPassantSquareFen=getStringOfSquare(SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT[0], SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT[1]);
        }
        //creates the final fen string
        this.fenString=fenStringBoard+" "+fenColourToPlay+" "+fenCastlingRights+" "+enPassantSquareFen+" "+String.valueOf(HALF_MOVE_CLOCK)+" "+String.valueOf(FULL_MOVE_NUMBER); 
        this.hasFenBeenCreated=true;
        return this.fenString;
    }
    /**
     * Gets the string value of a square given its y and x coordinates. E.g. getStringOfSquare(1,0) = "a7"
     * @param y The y index in the board array
     * @param x The x index in the board array
     * @return The square
     */
    private static String getStringOfSquare(int y, int x){
        switch (x) {
            case 0:
                return "a"+String.valueOf(8-y);
            case 1:
                return "b"+String.valueOf(8-y);
            case 2:
                return "c"+String.valueOf(8-y);
            case 3:
                return "d"+String.valueOf(8-y);
            case 4:
                return "e"+String.valueOf(8-y);
            case 5:
                return "f"+String.valueOf(8-y);
            case 6:
                return "g"+String.valueOf(8-y);
            default:
                return "h"+String.valueOf(8-y);
        }
    }
    /**
     * Gets the neural network input for a reduced size neural network that does not use the bitboard representation.
     * @return The network input
     */
    public int[]getNeuralNetInputReducedSize(){
        int data[]=new int[64+4+2+1];
        for(int y=0;y<8;y++){
            for(int x=0;x<8;x++){
                data[8*y +x]=BOARD[y][x];
            }
        }
        if(IS_BLACK_TURN_TO_PLAY_NEXT){
            data[64]=1;
        }
        if(CASTLING_RIGHTS[0]){
            data[65]=1;
        }
        if(CASTLING_RIGHTS[1]){
            data[66]=1;
        }
        if(CASTLING_RIGHTS[2]){
            data[67]=1;
        }
        if(CASTLING_RIGHTS[3]){
            data[68]=1;
        }
        if(SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT.length==2){
            data[69]=SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT[0];
            data[70]=SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT[1];
        }
        return data;
    }
    /**
     * Gets a reduced size neural network input as a double array.
     * @return The neural network input
     */
    public double[]getNeuralNetInputReducedSizeDouble(){
        double data[]=new double[71];
        for(int y=0;y<8;y++){
            for(int x=0;x<8;x++){
                data[8*y +x]=BOARD[y][x];
            }
        }
        if(IS_BLACK_TURN_TO_PLAY_NEXT){
            data[64]=1;
        }
        if(CASTLING_RIGHTS[0]){
            data[65]=1;
        }
        if(CASTLING_RIGHTS[1]){
            data[66]=1;
        }
        if(CASTLING_RIGHTS[2]){
            data[67]=1;
        }
        if(CASTLING_RIGHTS[3]){
            data[68]=1;
        }
        if(SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT.length==2){
            data[69]=SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT[0];
            data[70]=SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT[1];
        }
        return data;
    }
    /**
     * Takes a chess square, such as "a3" as input and returns the y and x coordinates of the square on the board
     * @param square The square
     * @return An int array of length [2], with the y and x index for the board array
     */
    private static int[] getYAndXOfSquare(String square){
        int[]ret = new int[2];
        ret[0]=8-Character.getNumericValue(square.charAt(1));
        switch (square.charAt(0)) {
            case 'a':
                ret[1]=0;
                break;
            case 'b':
                ret[1]=1;
                break;
            case 'c':
                ret[1]=2;
                break;
            case 'd':
                ret[1]=3;
                break;
            case 'e':
                ret[1]=4;
                break;
            case 'f':
                ret[1]=5;
                break;
            case 'g':
                ret[1]=6;
                break;
            default:
                ret[1]=7;
                break;
        }
        return ret;
    }
    /**
     * Gets a copy of the board array. use this when writing with board
     * @return A copy of the board array
     */
    public int[][]getCopyOfBoard(){
        int[][]board=new int[8][8];
        for(int y=0;y<8;y++){
            for(int x=0;x<8;x++){
                board[y][x]=BOARD[y][x];
            }
        }
        return board;
    }
    /**
     * Returns a copy of the castling rights array
     * @return The castling rights
     */
    public boolean []getCopyOfCastlingRights(){
        boolean rights []=new boolean[4];
        for(int i=0;i<4;i++){
            rights[i]=CASTLING_RIGHTS[i];
        }
        return rights;
    }
    /**
     * Gets a copy of the en passant square array and returns it.
     * @return The copy of the array
     */
    public int[]getCopyOfEnPassantSquare(){
        int ret[]=new int[SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT.length];
        for(int i=0;i<ret.length;i++){
            ret[i]=SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT[i];
        }
        return ret;
    }
    /**
     * Returns whether this game is a win. Note this variable may not be initialised and can only be set by external methods. Hence, only
     * call this method if you are certain that the board state has been set.
     * @return Whether the board is a win for the current user
     */
    public boolean getIsWin(){
        return this.isWin;
    }
    /**
     * Returns whether this game is a draw. Note: this variable is not automatically initialised and is set externally.
     * @return Whether the game is a draw
     */
    public boolean getIsDraw(){
        return this.isDraw;
    }
    /**
     * Returns whether this game is a loss for the user to play currently. Note this variable is not automatically initialised, so only call this method if the variable has been initialised.
     * @return Whether the game is a loss
     */
    public boolean getIsLoss(){
        return this.isLoss;
    }
    /**
     * Sets the game as win
     */
    public void setIsWinTrue(){
        this.isWin=true;this.isLoss=false;this.isDraw=false;
    }
    /**
     * Sets the game as a draw
     */
    public void setIsDrawTrue(){
        this.isDraw=true;this.isLoss=false;this.isWin=false;
    }
    /**
     * Sets the game as a loss
     */
    public void setIsLossTrue(){
        this.isLoss=true;this.isDraw=false;this.isWin=false;
    }
    /**
     * Sets the FEN string if it has already been created
     * @param fen The FEN 
     */
    private void setFen(String fen){
        this.fenString=fen;
    }
    /**
     * Sets whether the FEN has been created
     * @param hasFenBeenCreated Has the FEN been created
     */
    private void setHasFenBeenCreated(boolean hasFenBeenCreated){
        this.hasFenBeenCreated=hasFenBeenCreated;
    }
    /**
     * Sets whether the game is a loss
     * @param isLoss Whether the game is a loss
     */
    private void setIsLoss(boolean isLoss){
        this.isLoss=isLoss;
    }
    /**
     * Sets whether the game is a draw
     * @param isDraw Whether the game is a draw
     */
    private void setIsDraw(boolean isDraw){
        this.isDraw=isDraw;
    }
    /**
     * Sets whether the game is a win
     * @param isWin Whether the game is a win
     */
    private void setIsWin(boolean isWin){
        this.isWin=isWin;
    }
    /**
     * Gets a copy of the current chess board state.
     * @return The copy of the current board state
     */
    public ChessBoardState getCopyOfState(){
        int[][]board=new int[8][8];//creates a copy of the board
        for(int y=0;y<8;y++){
            for(int x=0;x<8;x++){
                board[y][x]=BOARD[y][x];
            }
        }
        //copies all values from current state and use them to create a new board state object
        int[]enPassant = new int[SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT.length];
        for(int i=0;i<SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT.length;i++){
            enPassant[i]=SQUARE_THAT_CAN_BE_CAPTURED_EN_PASSANT[i];
        }
        boolean castling[]=new boolean[CASTLING_RIGHTS.length];
        for(int i=0;i<CASTLING_RIGHTS.length;i++){
            castling[i]=CASTLING_RIGHTS[i];
        }
        //creates a new object copying the board
        ChessBoardState newState = new ChessBoardState(board, castling, IS_BLACK_TURN_TO_PLAY_NEXT, FULL_MOVE_NUMBER, HALF_MOVE_CLOCK, enPassant);
        newState.setHasFenBeenCreated(hasFenBeenCreated);
        newState.setFen(fenString);
        newState.setIsWin(isWin);
        newState.setIsLoss(isLoss);
        newState.setIsDraw(isDraw);
        return newState;
    }
}
