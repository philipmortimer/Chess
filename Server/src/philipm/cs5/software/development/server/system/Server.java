/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package philipm.cs5.software.development.server.system;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.Timer;
import philipm.cs5.software.development.user.system.InputPackages;

/**
 * This class stores the server that enables communication with any number of clients.
 * @author mortimer
 */
// class that performs server side commands. This class is a thread to allow for the serverUI form to be used whilst the server is active (hence it implements runnable)
public class Server implements Runnable{
    public static final String STARTING_BOARD_FEN="rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";//stores the string that represents a new chess board
    private final int PORT_NO;//stores the port number for the server to map too
    public String errorMessages[];//stores all errors that have occured in an array. If the length of this array is not equal to zero, some form of error has occured.
    public String[][]userFile;//stores the user text file contents as an arry to speed up file access speed
    public String[][]emailLinkFile;///stores the email link file as an array to speed up file access speed
    public String[][]onlineGames;//stores online games
    public static final String USER_FILE_NAME="userDetails.txt";//stores the file name of the user details text file
    public static final String SPLIT_ITEM=UserInfo.SPLIT_ITEM;
    public static final String EMAIL_FILE_NAME="emailLinkFile.txt";
    public static final int WIDTH_OF_ONLINE_FILE=14;//stores the number of fields in a record in the online file: game code, opp1,opp2, FEN(s), time elapsed, time White, time Black, boolean hasPlayerOneSeenGameOver, hasPlayerTwoSeenGameOver, isPlayerOneWhite, gameState, drawPlayerOne?,drawPlayerTwo?
    public static final String GUEST_ID=UserInfo.GUEST_ID;
    public static final String GUEST_EMAIL=UserInfo.GUEST_ID_EMAIL;
    public static final String GUEST_RECORD_USER_FILE=GUEST_ID+SPLIT_ITEM+SPLIT_ITEM+SPLIT_ITEM+SPLIT_ITEM+SPLIT_ITEM+SPLIT_ITEM+SPLIT_ITEM+SPLIT_ITEM+SPLIT_ITEM+SPLIT_ITEM+SPLIT_ITEM+SPLIT_ITEM+SPLIT_ITEM+SPLIT_ITEM+SPLIT_ITEM+SPLIT_ITEM;
    private ServerSocket serverSocket;
    private Timer timer;
    public static final String ONLINE_GAMES_FILE="onlineGames.txt";//stores the file name of the online games
    public Queue queue;//stores the queue used when writing to text files
    private Thread queueThread;//stores the thread which the queue uses
    public static final String CLUB_CONTACT_PHONE_NUMBER_FILE="phoneNumber.txt";
    public static final String CLUB_CONTACT_EMAIL_FILE="emailClubContact.txt";
    public String[]clubContactDetails;//stores the club's contact details
    /**
     * Constructor method initialises appropriate constants and values.
     * @param errorMessArray The array which stores the error messages. This error should be of length zero but will be set to length zero regardless.
     * @throws Exception If an error occurred, an exception is throw. This exception contains a String message that can be displayed.
     */
    public Server(String[] errorMessArray ) throws Exception{
        errorMessArray=new String[0];
        errorMessages=errorMessArray;
        try{
            //gets the port number for the server to run on
            FileReader read = new FileReader(ChangeServerDetails.SERVER_SETTINGS_TEXT_FILE_NAME);
            BufferedReader buffRead = new BufferedReader(read);
            String portLine=buffRead.readLine();
            buffRead.close();read.close();
            try{
                PORT_NO = Integer.parseInt(portLine);
            }catch(NumberFormatException ex){
                throw new Exception("Port Number stored is not a valid integer. Please change the port number.");
            }
        }catch(IOException e){
            throw new Exception("An error occured when attempting to retrieve the port number from the file "+ChangeServerDetails.SERVER_SETTINGS_TEXT_FILE_NAME+" Error details"+e);
        }
        //loads text files into arrays for faster access
        emailLinkFile=loadFileToArray(UserInfo.WIDTH_OF_EMAIL_LINK_FILE, EMAIL_FILE_NAME);
        userFile=loadFileToArray(UserInfo.WIDTH_OF_USER_DETAILS_FILE, USER_FILE_NAME);
        onlineGames=loadFileToArray(WIDTH_OF_ONLINE_FILE, ONLINE_GAMES_FILE);
        queue = new Queue(this);//creates new queue
        queueThread = new Thread(queue);queueThread.start();//starts the thread for the queue
        clubContactDetails=getClubContactDataFromFile();//loads club contact details
    }
    /**
     * Gets the club contact details from file
     * @return The contact details
     */
    private String[] getClubContactDataFromFile(){
        String details[]=new String[2];details[0]="";details[1]="";
        try{
            FileReader readPhone = new FileReader(CLUB_CONTACT_PHONE_NUMBER_FILE);//read the phone number from the file
            BufferedReader buffReadPhone = new BufferedReader(readPhone);
            String fileCont=buffReadPhone.readLine();
            if(fileCont!=null){
                details[0]=fileCont;
            }
            buffReadPhone.close();readPhone.close();
            FileReader readEmail = new FileReader(CLUB_CONTACT_EMAIL_FILE);//read the email address from the file
            BufferedReader buffReadEmail = new BufferedReader(readEmail);
            fileCont=buffReadEmail.readLine();
            if(fileCont!=null){
                details[1]=fileCont;
            }
            buffReadEmail.close();readEmail.close();
        }catch(IOException e){
            System.out.println("Error with club contact files: "+e);
        }
        return details;
        
    }
    /**
     * This method is used to add the guest id to the user file.
     * This method is no longer used as Guest records are not stored. This has been kept to make the code maintainable and easy to expand.
     */
    private void addGuestIdToFilesIfAppropriate(){
        try{
            //checks to see if GuestId is already in user file
            FileReader r = new FileReader(USER_FILE_NAME);
            BufferedReader buffUser = new BufferedReader(r);
            String line=buffUser.readLine();
            buffUser.close();r.close();
            if(line==null){//this will be true if the file is empty (i.e. no guest)
                FileWriter write = new FileWriter(USER_FILE_NAME,false);
                BufferedWriter buffWrite = new BufferedWriter(write);
                buffWrite.write(GUEST_RECORD_USER_FILE);//adds guest user file record to file
                buffWrite.flush();write.flush();buffWrite.close();write.close();
            }
            //checks to see if GuestId is alraedy in email file
            FileReader rEmail = new FileReader(EMAIL_FILE_NAME);
            BufferedReader buffEmail = new BufferedReader(rEmail);
            line = buffEmail.readLine();
            buffEmail.close();rEmail.close();
            if(line==null){//this will be true if the file is empty (i.e. no guest)
                FileWriter write = new FileWriter(EMAIL_FILE_NAME,false);
                BufferedWriter buffWrite = new BufferedWriter(write);
                buffWrite.write(GUEST_EMAIL+SPLIT_ITEM+GUEST_ID);//adds guest user file record to file
                buffWrite.flush();write.flush();buffWrite.close();write.close();
            }
        }catch(IOException e){
            addErrorToErrorMessages("An error occurred when adding guest details: "+e.getMessage());
        }
    }
    /**
     * Reads a text file and converts into a 2d array.
     * @param widthOfFile The number of fields in the file
     * @param fileName The fileName
     * @return The array that represents the data stored in the file
     */
    private String[][]loadFileToArray(int widthOfFile,String fileName){
        try{
            FileReader read = new FileReader(fileName);
            BufferedReader buffRead = new BufferedReader(read);
            int length =0;
            while(buffRead.readLine()!=null){//caclaulates number of records in file
                length++;
            }
            buffRead.close();read.close();
            String newFile[][]=new String[length][widthOfFile];
            for(int i=0;i<newFile.length;i++){
                for(int j=0;j<widthOfFile;j++){
                    newFile[i][j]="";//intialises all values to "" to avoid problems that can arise with null values
                }
            }
            FileReader readTwo = new FileReader(fileName);
            BufferedReader buffTwo = new BufferedReader(readTwo);
            String line;int i=0;
            String[]lineArr;
            //loads text file to array
            while((line=buffTwo.readLine())!=null){
                lineArr=line.split(SPLIT_ITEM);
                for(int j=0;j<lineArr.length;j++){
                    newFile[i][j]=lineArr[j];
                    if(lineArr[j]==null){
                        newFile[i][j]="";
                    }
                }
                i++;
            }buffTwo.close();readTwo.close();
            return newFile;
        }catch(IOException e){
            addErrorToErrorMessages("An error occured when accessing "+fileName+" Error: "+e.getMessage());//adds error to display
        }
        return new String[0][0];
    }
    /**
     * This thread runs and creates a new thread for every client communication. This allows for any number of clients to communicate with the server
     * simultaneously.
     */
    @Override
    public void run() {
        try{
            serverSocket = new ServerSocket(PORT_NO);//creates the server
            while(true){//continuosly checks for new connections
                Socket clientSocket = serverSocket.accept();//accepts nay client communications
                //creates a new thread that reads the client input and performs an approriate action accoridngly. This involves read and write objects.
                Thread thread = new Thread(){
                    @Override
                    public void run(){
                        InputPackages message=null;
                        try{
                            ObjectOutputStream objOut = new ObjectOutputStream(clientSocket.getOutputStream());//gets the output stream incase a value needs to be returned to the client
                            ObjectInputStream objIn = new ObjectInputStream(clientSocket.getInputStream()); //gets the input of client
                            try {
                                message=(InputPackages)objIn.readObject();//gets the InputPackage
                                String command = message.getActionName();
                                //checks to see the client message and perform various actions depending on the contents
                                switch (command) {
                                    case InputPackages.GET_USER_INFO_WITH_USERNAME_AND_PASSWORD:
                                        {
                                            UserInfo ret =queue.getUserDetailsAndTryLogInWithUsername(message.getPackageToSend()[1],message.getPackageToSend()[2]);
                                            objOut.writeObject(ret);
                                            break;
                                        }
                                    case InputPackages.GET_USER_INFO_WITH_EMAIL_AND_PASSWORD:
                                        {
                                            UserInfo ret = queue.getEmailAddressAndTryLogIn(message.getPackageToSend()[1],message.getPackageToSend()[2]);
                                            objOut.writeObject(ret);
                                            break;
                                        }
                                    case InputPackages.CREATE_ACCOUNT:
                                        queue.addInputPackageTask(message);
                                        break;
                                    case InputPackages.GET_ONLINE_GAMES_FOR_USER:
                                        objOut.writeObject(queue.getAllGamesWithUser(message.getPackageToSend()[1]));//returns all online games with user
                                        break;
                                    case InputPackages.CREATE_RANDOM_GAME:
                                        queue.addInputPackageTask(message);
                                        break;
                                    case InputPackages.DELETE_GAME:
                                        queue.addInputPackageTask(message);
                                        break;
                                    case InputPackages.CREATE_NEW_GAME_AGAINST_FRIEND:
                                        objOut.writeObject(queue.createOnlineGameAgainstFriend(message.getPackageToSend()[1], message.getPackageToSend()[2], message.getPackageToSend()[3]));
                                        break;
                                    case InputPackages.JOIN_GAME_USING_GAME_CODE:
                                        objOut.writeObject(queue.joinGameUsingFriendCode(message.getPackageToSend()[2],Integer.parseInt(message.getPackageToSend()[1])));
                                        break;
                                    case InputPackages.GET_GAME_RECORD:
                                        objOut.writeObject(queue.getGameRecord(Integer.parseInt(message.getPackageToSend()[1])));
                                        break;
                                    case InputPackages.UPDATED_ONLINE_GAME_RECORD:
                                        queue.addInputPackageTask(message);
                                        break;
                                    case InputPackages.GET_CLUB_CONTACT_DETAILS:
                                        objOut.writeObject(queue.getClubContactInfo());
                                        break;
                                    case InputPackages.GET_WHETHER_USER_HAS_REQUESTED_TO_JOIN_CLUB:
                                        objOut.writeBoolean(queue.hasUserRequestedToJoinClub(message.getPackageToSend()[1]));
                                        break;
                                    case InputPackages.CHANGE_STATUS_REQUEST_TO_JOIN_CLUB:
                                        queue.addInputPackageTask(message);
                                        break;
                                    case InputPackages.REMOVE_USER_FROM_CHESS_CLUB:
                                        queue.addInputPackageTask(message);
                                        break;
                                    case InputPackages.GET_LEAGUE_TABLE:
                                        objOut.writeObject(queue.getLeagueTable());
                                        break;
                                    case InputPackages.GET_ALL_CLUB_MEMBERS:
                                        objOut.writeObject(queue.getAllClubMembers());
                                        break;
                                    case InputPackages.GET_FIXTURES_AND_RESULTS:
                                        objOut.writeObject(queue.getAllFixturesAndResults());
                                        break;
                                    case InputPackages.GET_PEOPLE_ADDED_MIDWAY_THROUGH_SEASON:
                                        objOut.writeObject(queue.getPeopleAddedMidwayThroughSeason());
                                        break;
                                    case InputPackages.UPDATED_FIXTURES_AND_RESULTS:
                                        queue.addInputPackageTask(message);
                                        break;
                                    case InputPackages.CREATE_NEW_SEASON:
                                        queue.addInputPackageTask(message);
                                        break;
                                    case InputPackages.SET_CONTACT_DETAILS:
                                        queue.addInputPackageTask(message);
                                        break;
                                    case InputPackages.SET_USER_AS_LEADER:
                                        try{
                                            queue.setUserAsLeaderAndMember(message.getPackageToSend()[1]);
                                        }catch(Exception e){
                                            System.out.println("Error with setting new leader: "+e);
                                        }   break;
                                    case InputPackages.GET_ALL_REQUESTS:
                                        objOut.writeObject(queue.getAllRequestsToJoinClub());
                                        break;
                                    case InputPackages.GET_EMAIL_OF_USER:
                                        objOut.writeObject(queue.getEmailOfUser(message.getPackageToSend()[1]));
                                        break;
                                    case InputPackages.REMOVE_REQUEST_JOIN_CLUB:
                                        queue.addInputPackageTask(message);
                                        break;
                                    case InputPackages.ADD_USER_TO_CLUB:
                                        queue.addInputPackageTask(message);
                                        break;
                                    case InputPackages.GET_CLUB_MEMBER_DETAILS_ALL_MEMBERS:
                                        objOut.writeObject(queue.getAllClubMemberDetails());
                                        break;
                                    case InputPackages.UPDATE_USER_INFO_FILE_RECORD:
                                        queue.addInputPackageTask(message);
                                        break;
                                    case InputPackages.REMOVE_NON_MEMBER_USER:
                                        queue.addInputPackageTask(message);
                                        break;
                                    case InputPackages.GET_ALL_NON_MEMBERS:
                                        objOut.writeObject(queue.getAllNonMemberDetails());
                                        break;
                                    default:
                                        break;
                                }
                            } catch (ClassNotFoundException ex) {
                                addErrorToErrorMessages("There has been an ClassNotFoundException with the server, specifically the object stream. Error details: "+ex);
                                objOut.flush();objOut.close();objIn.close();
                                return;
                            }
                            objOut.flush();objOut.close();//closes and flushes approriate streams before terminating the thread
                            objIn.close();
                        }catch(IOException e){
                            System.out.println("Error that may not be fatal: "+e);
                            try{
                                System.out.println(message.getActionName());
                            }catch(Exception ez){
                                System.out.println(ez);
                            } 
                            //addErrorToErrorMessages("There has been an IOException with the server, specifically the object stream. Error details: "+e);
                            return;
                        }
                    }
                };
                thread.start(); //starts the thread
            }
        }catch(IOException e){
            addErrorToErrorMessages("There has been an IOException with the server. Error details: "+e);
            return;
        }
    }
    /**
     * Writes the array to the specified text file, with each field being split with an appropriate character.
     * @param array The 2d string array
     * @param fileName The file name
     */
    public void writeArrayToFileWithSplit(String[][]array,String fileName){
        try{
            FileWriter write = new FileWriter(fileName,false);
            BufferedWriter buffWrite = new BufferedWriter(write);
            String line;
            for(int y=0;y<array.length;y++){//loops through every line in the array and seperates each filed with a seperation character
                line="";
                for(int x=0;x<array[0].length;x++){
                    if(x==0){
                        if(array[y][x]!=null){//null values are not written to the file
                            line =array[y][x];
                        }
                    }else{
                        if(array[y][x]!=null){
                            line = line +SPLIT_ITEM+array[y][x];
                        }else{
                            line = line +SPLIT_ITEM;
                        }
                    }
                }
                //the appropriate line of text is written to the file
                if(y!=0){
                    buffWrite.newLine();
                }
                buffWrite.write(line);
            }
            //closes and flushes writers
            buffWrite.flush();write.flush();
            buffWrite.close();write.close();
        }catch(IOException e){
            addErrorToErrorMessages("Exception when writing to file "+fileName+" Error "+e.getMessage());
        }
    }
    /**
     * Closes the server socket and stops the thread
     * @throws IOException An IOException may occur
     */
    public void closeServerSocket() throws IOException{
        queue.stopThread();
        serverSocket.close();
    }
    /**
     * Checks to see if any error have occurred in the program
     * @return True is returned if the error messages array length is not equal to one. Otherwise false is returned 
     */
    public boolean haveAnyErrorsOccured(){
        return errorMessages.length!=0;
    }
    /**
     * Gets all error messages.
     * @return The error message. Each element in the array represents an error message.
     */
    public String[]getErrorMessages(){
        return errorMessages;
    }
    /**
     * Adds a new error message to the array that stores all error messages. Also sets the variable hasErrorOccuredToTrue
     * @param newError the new error message to be appended
     */
    private void addErrorToErrorMessages(String newError){
        String updatedArray[] = new String[errorMessages.length+1];
        for(int i=0;i<errorMessages.length;i++){
            updatedArray[i]=errorMessages[i];
        }
        updatedArray[errorMessages.length]=newError;
        errorMessages=updatedArray;
    }
}
