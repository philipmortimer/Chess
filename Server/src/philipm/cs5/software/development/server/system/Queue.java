/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package philipm.cs5.software.development.server.system;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import philipm.cs5.software.development.user.system.InputPackages;

/**
 * Creates a new Queue which handle the server's requests to write to files and change arrays. Essentially, this object
 * allows threads from an unknown number of client's to interact with a server sequentially. All processing is performed on one thread
 * to avoid issues that may arise from two threads writing simultaneously to the same file. This is justified as the user base is very small.
 * @author mortimer
 */
public class Queue implements Runnable{
    private InputPackages packages[];//stores the queue of input packages
    private final Server SERVER;//stores the server
    private boolean keepThreadRunning=true;//stores whether thread should keep running
    private boolean isRunning=true;
    /**
     * Creates a new queue object
     * @param server The server
     */
    public Queue (Server server){
        this.SERVER=server;
        packages = new InputPackages[0];
    }
    @Override
    public void run(){
        //exectutes all tasks until thread is ended - these tasks are write operations
            while(getKeepThreadRunning()){
                if(getPackages().length>0){//performs any tasks needed to execute
                    handlePackage();
                }
            }
        setIsRunningFalse();
    }
    /**
     * Sets the variable is running to false
     */
    private synchronized  void setIsRunningFalse(){
        isRunning=false;
    }
    /**
     * Searches for the user in the user files. Returns a UserInfo object with appropriate details
     * @param username The desired username
     * @param password The user inputted password
     * @return The UserInfo object
     */
    public synchronized UserInfo getUserDetailsAndTryLogInWithUsername(String username, String password){
        String record[]= Search.searchForRecordWithPrimaryKeyAtNoughtElement(SERVER.userFile, username,UserInfo.WIDTH_OF_USER_DETAILS_FILE);//searches for user record
        return new UserInfo(record,password);
    } 
    /**
     * Looks for a user with email address and attempts to log them in.
     * @param email The email address
     * @param password The password
     * @return A UserInfo object that stores the user data
     */
    public synchronized  UserInfo getEmailAddressAndTryLogIn(String email,String password){
        String emailLinkRecord[] = Search.searchForRecordWithPrimaryKeyAtNoughtElement(SERVER.emailLinkFile, email,UserInfo.WIDTH_OF_EMAIL_LINK_FILE);
        if(emailLinkRecord[0]==null){//if email address is not found, an appropriate UserInfo instance is returned
            return UserInfo.CreateUserInfoEmailNotFound();
        }else{
            return getUserDetailsAndTryLogInWithUsername(emailLinkRecord[1], password);//attempts to log in with found username
        }
    }
    /**
     * Gets all games that contain the specified user. This includes games that are pending approval
     * @param userName The user name
     * @return All games with the user. The array will always be of length 3, even if no games exist
     */
    public synchronized String[][] getAllGamesWithUser(String userName){
        String[][]game = new String[3][Server.WIDTH_OF_ONLINE_FILE];
        for(int y=0;y<3;y++){
            for(int x=0;x<Server.WIDTH_OF_ONLINE_FILE;x++){
                game[y][x]="";
            }
        }
        int i=0;//stores the element of the game array being accessed
        //performs a linear search through file and adds game data where appropriate
        for(int gameIndex=0;gameIndex<SERVER.onlineGames.length;gameIndex++){
            if((userName.equals(SERVER.onlineGames[gameIndex][1])&&SERVER.onlineGames[gameIndex][8].equals("false"))||(userName.equals(SERVER.onlineGames[gameIndex][2]) && SERVER.onlineGames[gameIndex][9].equals("false"))){
                for(int x=0;x<Server.WIDTH_OF_ONLINE_FILE;x++){
                    game[i][x]=SERVER.onlineGames[gameIndex][x];
                }
                if(i==2){
                    break;
                }
                i++;
            }
        }
        return game;
    }
    /**
     * Gets whether the user has requested to join the club
     * @param userName The username
     * @return True if the user has, otherwise false.
     */
    public synchronized boolean hasUserRequestedToJoinClub(String userName){
        //attempts to find user
        int indexOfRecord=Search.searchForRecordIndex(SERVER.userFile, userName);
        if(indexOfRecord==Search.INDEX_NOT_FOUND){
            return false;
        }
      
        //checks to see if user has requested to join the club
        return SERVER.userFile[indexOfRecord][16].equals("true");
    }
    /**
     * Gets the club contact information
     * @return The contact details
     */
    public synchronized  String[] getClubContactInfo(){
        return SERVER.clubContactDetails;
        
    }
    /**
     * Makes a user who is not a club member or leader a member of the chess club
     * @param playerName The name of the player
     */
    public synchronized void makeNonMemberNonLeaderUserClubMember(String playerName){
        //searches for the user record in the user file
        int index= Search.searchForRecordIndex(SERVER.userFile, playerName);
        if(index==Search.INDEX_NOT_FOUND){
            return;
        }
        //updates the variable indicating whether the user is a club member
        SERVER.userFile[index][14]="true";SERVER.userFile[index][13]="false";
        //writes change to text file
        SERVER.writeArrayToFileWithSplit(SERVER.userFile, Server.USER_FILE_NAME);
        //adds user to fixtures if relevant
        try{
            Fixture.addPlayerToFixtures(playerName, getAllClubMembers());
        }catch(Exception e){
            System.out.println("Possible non fatal adding user to chess club: "+e);
            return;
        }
    }
    /**
     * Gets the variable that indicates whether the thread should continue to run
     * @return The variable
     */
    private synchronized boolean getKeepThreadRunning(){
        return keepThreadRunning;
    }
    /**
     * Gets the queue of tasks
     * @return The queue
     */
    private synchronized InputPackages[] getPackages(){
        return packages;
    }
    /**
     * Sets the variable to false - indicating that the thread should not continue to run
     */
    private synchronized void setKeepRunningFalse(){
        keepThreadRunning=false;
    }
    /**
     * Gets whether the thread is currently alive
     * @return Whether the thread is running
     */
    private synchronized boolean getIsRunning(){
        return isRunning;
    }
    /**
     * Removes a single package from the queue and handles it
     */
    private synchronized void handlePackage(){
        String command;
        InputPackages message;
        message=removeFromQueue();
        command=message.getActionName();
        //gets the action command from the packages and performs the appropriate corresponding task
        switch (command) {
            case InputPackages.CREATE_ACCOUNT:
                createAccount(message.getUserRecod(), message.getUserName(), message.getEmail());
                break;
            case InputPackages.CREATE_RANDOM_GAME:
                handleRequestForNewOnlineRandomGame(message.getPackageToSend()[1]);
                break;
            case InputPackages.DELETE_GAME:
                deleteGame(Integer.parseInt(message.getPackageToSend()[1]), Double.parseDouble(message.getPackageToSend()[2]));
                break;
            case InputPackages.UPDATED_ONLINE_GAME_RECORD:
                String record[]=new String[Server.WIDTH_OF_ONLINE_FILE];
                for(int i=1;i<message.getPackageToSend().length;i++){
                    record[i-1]=message.getPackageToSend()[i];
                }   updateOnlineGameRecord(record);
                break;
            case InputPackages.CHANGE_STATUS_REQUEST_TO_JOIN_CLUB:
                updateRequestToJoinClubStatus(message.getPackageToSend()[1]);
                break;
            case InputPackages.REMOVE_USER_FROM_CHESS_CLUB:
                removeUserFromChessClub(message.getPackageToSend()[1]);
                break;
            case InputPackages.UPDATED_FIXTURES_AND_RESULTS:
                updateFixturesAndResults(message.getFixturesAndResultsArray());
                break;
            case InputPackages.CREATE_NEW_SEASON:
                createNewSeason();
                break;
            case InputPackages.SET_CONTACT_DETAILS:
                setContactDetails(message.getPackageToSend()[1],message.getPackageToSend()[2]);
                break;
            case InputPackages.REMOVE_REQUEST_JOIN_CLUB:
                withdrawRequestToJoin(message.getPackageToSend()[1]);
                break;
            case InputPackages.ADD_USER_TO_CLUB:
                makeUserAClubMember(message.getPackageToSend()[1]);
                break;
            case InputPackages.UPDATE_USER_INFO_FILE_RECORD:
                String newRecord[]=new String[UserInfo.WIDTH_OF_USER_DETAILS_FILE];
                for(int i=0;i<newRecord.length;i++){
                    newRecord[i]=message.getPackageToSend()[i+1];
                }   updateUserFileRecord(newRecord);
                break;
            case InputPackages.REMOVE_NON_MEMBER_USER:
                removeNonMemberUser(message.getPackageToSend()[1]);
                break;
            default:
                break;
        }
    }
    /**
     * Deletes a user from the database. This user must not be a member of the West Cross Chess Club.
     * @param userName The name of the user to remove
     */
    private synchronized void removeNonMemberUser(String userName){
        int index=Search.searchForRecordIndex(SERVER.userFile, userName);
        if(index!=Search.INDEX_NOT_FOUND){
            //removes user from user file - by creating a new array that does not include their user record.
            String newUserFile[][]=new String[SERVER.userFile.length-1][UserInfo.WIDTH_OF_USER_DETAILS_FILE];
            for(int i=0;i<index;i++){
                for(int x=0;x<UserInfo.WIDTH_OF_USER_DETAILS_FILE;x++){
                    newUserFile[i][x]=SERVER.userFile[i][x];
                }
            }
            for(int i=index+1;i<SERVER.userFile.length;i++){
                for(int x=0;x<UserInfo.WIDTH_OF_USER_DETAILS_FILE;x++){
                    newUserFile[i-1][x]=SERVER.userFile[i][x];
                }                
            }
            SERVER.userFile=newUserFile;
            SERVER.writeArrayToFileWithSplit(SERVER.userFile, Server.USER_FILE_NAME);
        }
        //removes user from email link file if appropriate
        int indexOfEmail=-1;
        for(int record=0;record<SERVER.emailLinkFile.length;record++){
            if(SERVER.emailLinkFile[record][1].equals(userName)){
                indexOfEmail=record;
                break;
            }
        }
        if(indexOfEmail!=-1){
            //updates email file
            String[][] newEmailFile=new String[SERVER.emailLinkFile.length-1][UserInfo.WIDTH_OF_EMAIL_LINK_FILE];
            for(int i=0;i<indexOfEmail;i++){
                for(int x=0;x<UserInfo.WIDTH_OF_EMAIL_LINK_FILE;x++){
                    newEmailFile[i][x]=SERVER.emailLinkFile[i][x];
                }
            }
            for(int i=indexOfEmail+1;i<SERVER.emailLinkFile.length;i++){
                for(int x=0;x<UserInfo.WIDTH_OF_EMAIL_LINK_FILE;x++){
                    newEmailFile[i-1][x]=SERVER.emailLinkFile[i][x];
                }                
            }
            SERVER.emailLinkFile=newEmailFile;
            SERVER.writeArrayToFileWithSplit(SERVER.emailLinkFile, Server.EMAIL_FILE_NAME);
        }
        //removes user from online games. This is done by appending all games that are not from the user to an ArrayList.
        ArrayList<String[]> onlineGamesNew=new ArrayList<String[]>(SERVER.onlineGames.length);
        String[]recordBuffer;
        for(int game=0;game<SERVER.onlineGames.length;game++){
            //checks to see if user is in the game
            if((SERVER.onlineGames[game][1].equals(userName) || SERVER.onlineGames[game][2].equals(userName)) ==false){
                recordBuffer=new String[Server.WIDTH_OF_ONLINE_FILE];
                for(int x=0;x<recordBuffer.length;x++){
                    recordBuffer[x]=SERVER.onlineGames[game][x];
                }
                onlineGamesNew.add(recordBuffer);
            }
        }
        //creates a new online game array from the array list
        String newOnlineGameArray[][]=new String[onlineGamesNew.size()][Server.WIDTH_OF_ONLINE_FILE];
        for(int i=0;i<newOnlineGameArray.length;i++){
            recordBuffer=onlineGamesNew.get(i);
            for(int x=0;x<Server.WIDTH_OF_ONLINE_FILE;x++){
                newOnlineGameArray[i][x]=recordBuffer[x];
            }
        }
        //changes are saved
        SERVER.onlineGames=newOnlineGameArray;
        SERVER.writeArrayToFileWithSplit(SERVER.onlineGames, Server.ONLINE_GAMES_FILE);
    }
    /**
     * Updates the user record to a new array.
     * @param newRecord The new record
     */
    private synchronized void updateUserFileRecord(String []newRecord){
        int index=Search.searchForRecordIndex(SERVER.userFile, newRecord[0]);//locates user
        if(index==Search.INDEX_NOT_FOUND){
            return;
        }
        //updates data
        for(int x=0;x<UserInfo.WIDTH_OF_USER_DETAILS_FILE;x++){
            SERVER.userFile[index][x]=newRecord[x];
        }
        //writes the new array to the text file
        SERVER.writeArrayToFileWithSplit(SERVER.userFile, Server.USER_FILE_NAME);
    }
    /**
     * Makes the user a club member
     * @param userName The name of the user 
     */
    private synchronized void makeUserAClubMember(String userName){
        int userIndex=Search.searchForRecordIndex(SERVER.userFile, userName);//find the user in the user file
        if(userIndex==Search.INDEX_NOT_FOUND){
            return;
        }
        //updates variables to indicate that they are a club member who is not requesting to join the club and who is not the leader
        SERVER.userFile[userIndex][16]="false";SERVER.userFile[userIndex][14]="true";
        SERVER.userFile[userIndex][13]="false";
        try{
            Fixture.addPlayerToFixtures(userName, getAllClubMembers());//adds the players to the fixtures
        }catch(Exception e){
            System.out.println("Error when adding user to club: "+e);
        }
        
    }
    /**
     * Withdraws a user's request to join the West Cross Chess Club
     * @param userName The name of the user who's request is to be withdrawn
     */
    private synchronized void withdrawRequestToJoin(String userName){
        int userIndex=Search.searchForRecordIndex(SERVER.userFile, userName);//locates user in user file
        if(userIndex==Search.INDEX_NOT_FOUND){
            return;
        }
        SERVER.userFile[userIndex][16]="false";//updates variable indicating that they are not requesting to join the club
        SERVER.writeArrayToFileWithSplit(SERVER.userFile, Server.USER_FILE_NAME);//writes change to text file
    }
    /**
     * Returns the user data of every non club member. (I.e. the user file record of every non chess club member).
     * @return The data
     */
    public synchronized String[][] getAllNonMemberDetails(){
        //searches through every user in the user file to see if they have requested to join the club.
        String currentUser[];
        ArrayList<String[]> allNonMembers=new ArrayList<String[]>();
        for(int user=0;user<SERVER.userFile.length;user++){
            if(SERVER.userFile[user][14].equals("false")){
                currentUser=new String[UserInfo.WIDTH_OF_USER_DETAILS_FILE];
                //copies array
                for(int x=0;x<currentUser.length;x++){
                    currentUser[x]=SERVER.userFile[user][x];
                }
                allNonMembers.add(currentUser);//adds user to array list
            }
        }
        String[][]nonMem = new String[allNonMembers.size()][UserInfo.WIDTH_OF_USER_DETAILS_FILE];//converts array list to an array
        for(int user=0;user<nonMem.length;user++){
            for(int x=0;x<UserInfo.WIDTH_OF_USER_DETAILS_FILE;x++){
                nonMem[user][x]=allNonMembers.get(user)[x];
            }
        }
        return nonMem;        
    }
    /**
     * Returns the user data of every club member. (I.e. the user file record of every chess club member).
     * @return The data
     */
    public synchronized String[][] getAllClubMemberDetails(){
        //searches through every user in the user file to see if they have requested to join the club.
        String currentUser[];
        ArrayList<String[]> allMembers=new ArrayList<String[]>();
        for(int user=0;user<SERVER.userFile.length;user++){
            if(SERVER.userFile[user][14].equals("true")){
                currentUser=new String[UserInfo.WIDTH_OF_USER_DETAILS_FILE];
                //copies array
                for(int x=0;x<currentUser.length;x++){
                    currentUser[x]=SERVER.userFile[user][x];
                }
                allMembers.add(currentUser);//adds user to ArrayList
            }
        }
        String[][]mem = new String[allMembers.size()][UserInfo.WIDTH_OF_USER_DETAILS_FILE];//converts array list to a 2d array
        for(int user=0;user<mem.length;user++){
            for(int x=0;x<UserInfo.WIDTH_OF_USER_DETAILS_FILE;x++){
                mem[user][x]=allMembers.get(user)[x];
            }
        }
        return mem;        
    }
    /**
     * Updates the club contact information.
     * @param phoneNumber The new phone number
     * @param emailAddress The new email address
     */
    private synchronized void setContactDetails(String phoneNumber, String emailAddress){
        try{
            //writes the changes to the files
            FileWriter writePhone = new FileWriter(Server.CLUB_CONTACT_PHONE_NUMBER_FILE,false);
            BufferedWriter buffWritePhone = new BufferedWriter(writePhone);
            buffWritePhone.write(phoneNumber);
            buffWritePhone.flush();writePhone.flush();buffWritePhone.close();writePhone.close();//flushes and closes writers
            FileWriter writeEmail = new FileWriter(Server.CLUB_CONTACT_EMAIL_FILE,false);
            BufferedWriter buffWriteEmail = new BufferedWriter(writeEmail);
            buffWriteEmail.write(emailAddress);
            buffWriteEmail.flush();writeEmail.flush();buffWriteEmail.close();writeEmail.close();//flushes and closes writers
            //updates the array
            SERVER.clubContactDetails[0]=phoneNumber;SERVER.clubContactDetails[1]=emailAddress;
        }catch(IOException e){
            System.out.println("Possible error when set club contact details: "+e);
        }
    }
    /**
     * Generates a new season of games for the chess club.
     */
    private synchronized void createNewSeason(){
        try{
            Fixture.createdNewFixturesFromStart(getAllClubMembers());//generates the new season
        }catch(Exception e){
            System.out.println("Possible non fatal error when creating new season: "+e);
        }
    }
    /**
     * Updates the fixtures and results.
     * @param fixturesAndResults The updated value of the fixtures and results
     */
    private synchronized void updateFixturesAndResults(String[][][]fixturesAndResults){
        try{
         Fixture.writeToFile(fixturesAndResults, Fixture.getListOfPeopleToAddedDuringSeason());
        }catch(IOException e){
            System.out.println("Possibly non fatal error when updating fixtures: "+e);
        }
    }
    /**
     * Returns all requests to join the chess club. This method returns the user file record  of every user who has requested to join the club.
     * @return The requests to join the club
     */
    public synchronized String [][]getAllRequestsToJoinClub(){
        //searches through every user in the user file to see if they have requested to join the club.
        String currentUser[];
        ArrayList<String[]> allRequests=new ArrayList<String[]>();
        for(int user=0;user<SERVER.userFile.length;user++){
            if(SERVER.userFile[user][16].equals("true")){
                currentUser=new String[UserInfo.WIDTH_OF_USER_DETAILS_FILE];
                //copies array
                for(int x=0;x<currentUser.length;x++){
                    currentUser[x]=SERVER.userFile[user][x];
                }
                allRequests.add(currentUser);
            }
        }
        String[][]req = new String[allRequests.size()][UserInfo.WIDTH_OF_USER_DETAILS_FILE];//converts array list into a 2d array of requests
        for(int user=0;user<req.length;user++){
            for(int x=0;x<UserInfo.WIDTH_OF_USER_DETAILS_FILE;x++){
                req[user][x]=allRequests.get(user)[x];
            }
        }
        return req;
    }
    /**
     * Gets the email address of the chosen user
     * @param userName The username
     * @return The email address as a string array of length 1. Element nought (the only element of the array) stores the email address.
     */
    public synchronized String[]getEmailOfUser(String userName){
        String user []=new String[1];user[0]="";
        for(int person=0;person<SERVER.emailLinkFile.length;person++){//searches sequentially through file to find email address
            if(SERVER.emailLinkFile[person][1].equals(userName)){
                user[0]=SERVER.emailLinkFile[person][0];
                return user;
            }
        }
        return user;
    }
    /**
     * Sets the chosen user as the club leader and member.
     * @param userName The username
     * @throws java.lang.Exception An error may occur if the user inputted is not found in the user records
     */
    public synchronized void setUserAsLeaderAndMember(String userName) throws Exception{
        int index=Search.searchForRecordIndex(SERVER.userFile, userName);//find user in user file
        if(index==Search.INDEX_NOT_FOUND){//if user is not found, an appropriate error message is returned
            throw new Exception("User to be leader was not found.");
        }
        try{
            if(SERVER.userFile[index][14].equals("false")){//adds club leader to fixtures if they are not already a member
                Fixture.addPlayerToFixtures(userName, getAllClubMembers());
            }
        }catch(Exception e){
            
        }
        //updates data
        //sets all users in file to not being the leader
        for(int user=0;user<SERVER.userFile.length;user++){
            SERVER.userFile[user][13]="false";
        }
        SERVER.userFile[index][16]="false";//sets user as a leader and club member who is not requesting to join the club
        SERVER.userFile[index][13]="true";SERVER.userFile[index][14]="true";
        SERVER.writeArrayToFileWithSplit(SERVER.userFile, Server.USER_FILE_NAME);//updates text file
    }
    /**
     * Gets the people added midway through the season to the club.
     * @return The list of people
     */
    public synchronized String[]getPeopleAddedMidwayThroughSeason(){
        try{
            return Fixture.getListOfPeopleToAddedDuringSeason();
        }catch(IOException e){
            return new String[0];
        }
    }
    /**
     * Gets all fixtures and results. Returns an empty string array if an error occurs. An error may occur if the fixtures have not yet been generated.
     * @return The results and fixtures
     */
    public synchronized String[][][]getAllFixturesAndResults(){
        try{
            return Fixture.loadFixturesArray();
        }catch(Exception e){
           return new String[0][0][0];
        }
    }
    /**
     * Gets all members of the chess club. This will be returned in lexographical order (i.e., the array is sorted)
     * @return All members of the chess club
     */
    public synchronized String[]getAllClubMembers(){
        //loops through user file to find club members
        ArrayList<String> allMembers=new ArrayList<String>();
        for(int i=0;i<SERVER.userFile.length;i++){
            if(SERVER.userFile[i][14].equals("true")){
                allMembers.add(SERVER.userFile[i][0]);//adds member to list
            }
        }
        return allMembers.toArray(new String[0]);//converts array list to String array
    }
    /**
     * Gets the current league table
     * @return The league table
     */
    public synchronized String[][]getLeagueTable(){
        //table is in the form: username, wins, draws, losses, points, isPartOfLeague
        String[]clubMembers=getAllClubMembers();//gets club members in a sorted array
        final int WIDTH_OF_TABLE=6;
        String[][]leagueTable=new String[clubMembers.length][WIDTH_OF_TABLE];
        int whiteIndex;
        int blackIndex;
        //intialises values of table
        for(int i=0;i<leagueTable.length;i++){
            leagueTable[i][0]=clubMembers[i];
            leagueTable[i][1]="0";leagueTable[i][2]="0";leagueTable[i][3]="0";leagueTable[i][4]="0";
            leagueTable[i][5]="false";
        }
        try{
            String[][][]fixtures=Fixture.loadFixturesArray();//loads fixture array
            //loops through all played games and updates array accordingly. Calculating points and number of wins, draws and losses for each player
            for(int round=0;round<fixtures.length;round++){
                for(int match=0;match<fixtures[0][0].length;match++){
                    if(!(fixtures[round][2][match].equals(Fixture.BYE_GAME_STATE) || fixtures[round][2][match].equals(Fixture.GAME_NOT_YET_PLAYED))){//if the game has been played and is not a bye game, the league table is updated
                        whiteIndex=Search.searchForRecordIndex(leagueTable, fixtures[round][0][match]);//searches for white player in list of players in club
                        blackIndex=Search.searchForRecordIndex(leagueTable, fixtures[round][1][match]);//searches for black player in list of players in club
                        if(fixtures[round][2][match].equals(Fixture.GAME_DRAW)){//indicates that both players have drawn the game
                            leagueTable[whiteIndex][2]=String.valueOf(Integer.parseInt(leagueTable[whiteIndex][2])+1);
                            leagueTable[whiteIndex][4]=String.valueOf(Integer.parseInt(leagueTable[whiteIndex][4])+1);
                            leagueTable[whiteIndex][5]="true";
                            leagueTable[blackIndex][2]=String.valueOf(Integer.parseInt(leagueTable[blackIndex][2])+1);
                            leagueTable[blackIndex][4]=String.valueOf(Integer.parseInt(leagueTable[blackIndex][4])+1);
                            leagueTable[blackIndex][5]="true";
                        }else if(fixtures[round][2][match].equals(Fixture.GAME_WHITE_WIN)){//updates league table for white win
                            leagueTable[whiteIndex][1]=String.valueOf(Integer.parseInt(leagueTable[whiteIndex][1])+1);
                            leagueTable[whiteIndex][4]=String.valueOf(Integer.parseInt(leagueTable[whiteIndex][4])+3);
                            leagueTable[whiteIndex][5]="true";
                            leagueTable[blackIndex][3]=String.valueOf(Integer.parseInt(leagueTable[blackIndex][3])+1);
                            leagueTable[blackIndex][5]="true"; 
                        }else if(fixtures[round][2][match].equals(Fixture.GAME_BLACK_WIN)){//updates league table for black win
                            leagueTable[whiteIndex][3]=String.valueOf(Integer.parseInt(leagueTable[whiteIndex][3])+1);
                            leagueTable[whiteIndex][5]="true";
                            leagueTable[blackIndex][1]=String.valueOf(Integer.parseInt(leagueTable[blackIndex][1])+1);
                            leagueTable[blackIndex][4]=String.valueOf(Integer.parseInt(leagueTable[blackIndex][4])+3);
                            leagueTable[blackIndex][5]="true";                            
                        }
                    }
                }
            }
            //sorts the league table in order of points
            String[][]pointsAndPointer=new String[leagueTable.length][2];//this array stores the points of a user and an index pointing to said user
            for(int i=0;i<pointsAndPointer.length;i++){
                pointsAndPointer[i][0]=leagueTable[i][4];
                pointsAndPointer[i][1]=String.valueOf(i);
            }
            //sorts array    
            Sort.quickSortUsingIntAtFirstElement(pointsAndPointer);  
            //league table is generated in points order. Those with the least points are stored at index 0.
            String [][]newLeagueTable=new String[leagueTable.length][WIDTH_OF_TABLE];
            int indexPointer;
            for(int i=0;i<leagueTable.length;i++){
                indexPointer=Integer.parseInt(pointsAndPointer[i][1]);
                for(int x=0;x<WIDTH_OF_TABLE;x++){
                    newLeagueTable[i][x]=leagueTable[indexPointer][x];
                }
            }
            //reverses array so player with most points is at top
            String temp;
            for(int i = 0; i < newLeagueTable.length / 2; i++){
                for(int x=0;x<WIDTH_OF_TABLE;x++){
                    temp=newLeagueTable[i][x];
                    newLeagueTable[i][x]=newLeagueTable[newLeagueTable.length - i - 1][x];
                    newLeagueTable[newLeagueTable.length - i - 1][x]=temp;
                }
            }
            return newLeagueTable;//returns league table
        }catch(Exception e){
            System.out.println("Error when getting league table: "+e);
            return new String[0][0];
        }
    }
    /**
     * Removes the chosen user from the West Cross Chess Club
     * @param userName The username of the user
     */
    private synchronized  void removeUserFromChessClub(String userName){
        //searches for the user record in the user file
        int index= Search.searchForRecordIndex(SERVER.userFile, userName);
        if(index==Search.INDEX_NOT_FOUND){
            return;
        }
        //updates the variable indicating whether the user is a club member
        SERVER.userFile[index][14]="false";SERVER.userFile[index][13]="false";
        //writes change to text file
        SERVER.writeArrayToFileWithSplit(SERVER.userFile, Server.USER_FILE_NAME);
        //removes user from fixtures if approrpaite
        try{
            Fixture.removePlayerFromFixtures(userName);
        }catch(Exception e){
            System.out.println("Possible non fatal error when removing user from chess club: "+e);
            return;
        }
        
    }
    /**
     * Changes status of request to join club
     * @param userName The name of the user
     */
    private synchronized void updateRequestToJoinClubStatus(String userName){
        //attempts to find user in user file
        int index = Search.searchForRecordIndex(SERVER.userFile, userName);
        if(index==Search.INDEX_NOT_FOUND){
            return;
        }
        //updates status
        if(SERVER.userFile[index][16].equals("true")){
            SERVER.userFile[index][16]="false";
        }else{
            SERVER.userFile[index][16]="true";
        }
        //writes array to text file
        SERVER.writeArrayToFileWithSplit(SERVER.userFile, Server.USER_FILE_NAME);
    }
    /**
     * Searches for the game id and deletes it if is found. Note, this method deletes the record so it must be ensured that both users have actually seen the game results before the game is deleted
     * @param GAME_ID the game id to delete
     * @param RESULT The game result. 1 indicates that player one won, 0.5 indicates a draw and 0 indicates that player two won
     */
    private synchronized void deleteGame(final int GAME_ID,final double RESULT){
        //checks to ensure that game has not already been deleted
        int playerOneElo;//stores the first player's ELO score
        int playerTwoElo;//stores player two's ELO score
        double resultPlayerOne=0.5;//stores the result from player one's perspective
        double resultPlayerTwo=0.5;//stores the result from player two's perspective
        double probabilityPlayerOneWins;//stores the probability that player one wins
        double probabilityPlayerTwoWins;//stores the probability that player two wins
        int recordIndex=Search.searchForRecordIndexInt(SERVER.onlineGames, GAME_ID);//gets index where record is located
        if(recordIndex==Search.INDEX_NOT_FOUND){
            return;//games has already been deleted and hence this method is not needed
        }
        //if a game that has not yet been accepted is being deleted, one of the username may be blank. In this case, no ELO update is needed
        if((SERVER.onlineGames[recordIndex][1]==null || SERVER.onlineGames[recordIndex][2]==null ||SERVER.onlineGames[recordIndex][1].equals("")||SERVER.onlineGames[recordIndex][2].equals(""))==false){
            int playerOneRecord=Search.searchForRecordIndex( SERVER.userFile, SERVER.onlineGames[recordIndex][1]);
            int playerTwoRecord=Search.searchForRecordIndex( SERVER.userFile, SERVER.onlineGames[recordIndex][2]);
            if(playerOneRecord==Search.INDEX_NOT_FOUND||playerTwoRecord==Search.INDEX_NOT_FOUND){
                return;
            }
            //gets the current ELO ratings of the players
            playerOneElo=Integer.parseInt(SERVER.userFile[playerOneRecord][15]);
            playerTwoElo=Integer.parseInt(SERVER.userFile[playerTwoRecord][15]);
            //updates ELO values of users involved
            //gets score of game
            if(RESULT==1){
                resultPlayerTwo=0;
                resultPlayerOne=1;
            }else if(RESULT==0){
                resultPlayerTwo=1;
                resultPlayerOne=0;
            }
            //implements ELO algorithm
            probabilityPlayerOneWins=1.0/(1.0+Math.pow(10.0, (playerTwoElo-playerOneElo)/400.0));
            probabilityPlayerTwoWins=1.0/(1.0+Math.pow(10.0, (playerOneElo-playerTwoElo)/400.0));
            playerOneElo=(int)Math.round(playerOneElo+24.0*(resultPlayerOne-probabilityPlayerOneWins));
            playerTwoElo=(int)Math.round(playerTwoElo+24.0*(resultPlayerTwo-probabilityPlayerTwoWins));
            //updates elo results and writes it to array and file
            SERVER.userFile[playerOneRecord][15]=String.valueOf(playerOneElo);
            SERVER.userFile[playerTwoRecord][15]=String.valueOf(playerTwoElo);
            SERVER.writeArrayToFileWithSplit(SERVER.userFile, Server.USER_FILE_NAME);            
        }
        //now the game details are deleted from the file
        String newOnlineGameFile[][]=new String[SERVER.onlineGames.length-1][Server.WIDTH_OF_ONLINE_FILE];
        //copies the array to a new one, simply deleteing the old record
        for(int i=0;i<recordIndex;i++){
            for(int x=0;x<Server.WIDTH_OF_ONLINE_FILE;x++){
                newOnlineGameFile[i][x]=SERVER.onlineGames[i][x];
            }
        }
        for(int i=recordIndex+1;i<SERVER.onlineGames.length;i++){
            for(int x=0;x<Server.WIDTH_OF_ONLINE_FILE;x++){
                newOnlineGameFile[i-1][x]=SERVER.onlineGames[i][x];
            }
        }
        //sets the array to the updated file
        SERVER.onlineGames=newOnlineGameFile;
        //writes the new file to a text file
        SERVER.writeArrayToFileWithSplit(SERVER.onlineGames, Server.ONLINE_GAMES_FILE);
    }
    /**
     * Updates a record in the online game file. If the record can't be found, nothing happens.
     * @param newRecordValue The new record that should replace the current one.
     */
    private synchronized void updateOnlineGameRecord(String[]newRecordValue){
        int indexOfRecord=Search.searchForRecordIndexInt(SERVER.onlineGames, Integer.parseInt(newRecordValue[0]));//searches for record
        if(indexOfRecord==Search.INDEX_NOT_FOUND){//if the item is not found, nothing happens
            return;
        }
        
        //updates record
        for(int itemInFile=0;itemInFile<Server.WIDTH_OF_ONLINE_FILE;itemInFile++){
            SERVER.onlineGames[indexOfRecord][itemInFile]=newRecordValue[itemInFile];
        }
        SERVER.writeArrayToFileWithSplit(SERVER.onlineGames, Server.ONLINE_GAMES_FILE);//writes new array to file
        if(newRecordValue[9].equals("true")&& newRecordValue[8].equals("true")){//deletes the game if appropraite
            deleteGame(Integer.parseInt(newRecordValue[0]), Double.parseDouble(newRecordValue[11]));
            return;
        }
    }
    /**
     * Stops the queue thread and finishes all remaining tasks
     */
    public  void stopThread(){
        setKeepRunningFalse();
        while(getIsRunning()){//waits for the thread to finish
        }
        while(getPackages().length>0){//finishes remaining tasks queued
            handlePackage();
        }
    }
    /**
     * Adds a new account to the user file.
     * @param record The entire entry to be added to the user file
     * @param userName The username
     * @param emailAddress The email address
     */
    private synchronized void createAccount(String record[],String userName,String emailAddress){
        if(Search.searchForRecordWithPrimaryKeyAtNoughtElement(SERVER.userFile, userName,UserInfo.WIDTH_OF_USER_DETAILS_FILE)[0]!=null||Search.searchForRecordWithPrimaryKeyAtNoughtElement(SERVER.emailLinkFile, emailAddress,UserInfo.WIDTH_OF_EMAIL_LINK_FILE)[0]!=null){//checks to ensure account hasn't already been created
            return;
        }
        String [][]userNew = new String[SERVER.userFile.length+1][UserInfo.WIDTH_OF_USER_DETAILS_FILE];
        for(int y=0;y<SERVER.userFile.length;y++){//copies old user file to new array
            for(int x =0;x<UserInfo.WIDTH_OF_USER_DETAILS_FILE;x++){
                userNew[y][x]=SERVER.userFile[y][x];
            }
        }
        for(int x=0;x<UserInfo.WIDTH_OF_USER_DETAILS_FILE;x++){//adds new record to new array
            userNew[userNew.length-1][x]=record[x];
        }
        Sort.mergeSort(userNew);//sorts the user array using merge sort
        String [][]emailLinkNew=new String[SERVER.emailLinkFile.length+1][UserInfo.WIDTH_OF_EMAIL_LINK_FILE];
        for(int y =0;y<SERVER.emailLinkFile.length;y++){//copies current email file
            for(int x =0;x<UserInfo.WIDTH_OF_EMAIL_LINK_FILE;x++){
                emailLinkNew[y][x]=SERVER.emailLinkFile[y][x];
            }
        }
        emailLinkNew[emailLinkNew.length-1][0]=emailAddress;emailLinkNew[emailLinkNew.length-1][1]=userName;//adds new record to file
        Sort.mergeSort(emailLinkNew);
        SERVER.writeArrayToFileWithSplit(userNew, Server.USER_FILE_NAME);SERVER.writeArrayToFileWithSplit(emailLinkNew, Server.EMAIL_FILE_NAME);
        SERVER.userFile=userNew;SERVER.emailLinkFile=emailLinkNew;
    }        
    /**
     * Adds the input package to the queue
     * @param pack The input package to add to the queue
     */
    public synchronized  void addInputPackageTask(InputPackages pack){
        if(!getKeepThreadRunning()){//doesnt allow any more packages to be added to the queue once the thread is closed
            return;
        }
        InputPackages newPack[]=new InputPackages[packages.length+1];
        //copies old packages to new array
        for(int i=0;i<packages.length;i++){
            newPack[i]=packages[i];
        }
        //adds most recent package to end of array
        newPack[packages.length]=pack;
        packages=newPack;
    }
    /**
     * Removes an InputPackage from the queue and returns it
     * @return The input package removed
     */
    private synchronized InputPackages removeFromQueue(){
        InputPackages[]newPack=new InputPackages[packages.length-1];
        for(int i=1;i<packages.length;i++){//creates new array
            newPack[i-1]=packages[i];
        }
        InputPackages pack=packages[0];
        packages=newPack;
        return pack;
    }
    /**
     * Creates a new online game and adds it to the system
     * @param opponentOne The name of the opponent 
     * @param opponentTwo The name of the other opponent - leave this blank if the opponent name is not yet known
     * @param isUnlimitedTime Whether the game uses unlimited time control
     * @param isTenMins Whether the game uses 10 minute time control
     * @param isGameForFriendCode If the game to be created required a friend code to be enterer
     * @param isPlayerOneWhite Stores whether player one is white or not
     * @return the game id
     */
    private synchronized String createNewOnlineGame(String opponentOne, String opponentTwo, boolean isUnlimitedTime, boolean isTenMins,boolean isGameForFriendCode,boolean isPlayerOneWhite){
        //file is stored: gameId, opponentOne, opponentTwo, gameDetails (FENS(s)), timePlayed, timeLeftWhite, timeLeftBlack, is a friend code needed, has playerOne seen, hasPlayerTwoseen win, isPlayerOneWhite, gameState, drawP1?,drawp2?
        String[] record=new String[Server.WIDTH_OF_ONLINE_FILE];//stores the record to write to the game file
        String gameId="";//stores the gameId
        record[1]=opponentOne;record[2]=opponentTwo;record[3]=Server.STARTING_BOARD_FEN;record[4]="0";record[7]=String.valueOf(isGameForFriendCode);
        record[8]="false";record[9]="false";record[10]=String.valueOf(isPlayerOneWhite);record[11]="-1";record[12]="false";record[13]="false";
        //sets time left for each player
        if(isUnlimitedTime){
            record[5]="-100";record[6]="-100";
        }else if(isTenMins){
            record[5]="600";record[6]="600";
        }else{
            record[5]="300";record[6]="300";
        }
        if(SERVER.onlineGames.length==0){//the first game to be added is done like this
            SERVER.onlineGames=new String[1][Server.WIDTH_OF_ONLINE_FILE];
            gameId="000000";
            record[0]=gameId;
            for(int x=0;x<Server.WIDTH_OF_ONLINE_FILE;x++){
                SERVER.onlineGames[0][x]=record[x];
            }
        }else{
            //gets the game id by looping sequentially through the file and finding the next available integer game id
            //as the file is sorted, if the end of the file is reached, the game is simply the current id plus one
            boolean hasFileEndBeenReachedWithoutId=true;
            for(int i=0;i<SERVER.onlineGames.length;i++){
                if(Integer.parseInt(SERVER.onlineGames[i][0])!=i){
                    //ensures that the integer is not less than six characters
                    gameId=String.valueOf(i);
                    while(gameId.length()<6){
                        gameId="0"+gameId;
                    }
                    record[0]=gameId;
                    hasFileEndBeenReachedWithoutId=false;
                    break;
                }
            }
            if(hasFileEndBeenReachedWithoutId){//if the end of the file is reached without an id being assigned this code runs
                gameId=String.valueOf(SERVER.onlineGames.length);
                while(gameId.length()<6){
                    gameId="0"+gameId;
                }
                record[0]=gameId;
            }
            //updates onlineGames Array
            String newOnlineGames[][]=new String[SERVER.onlineGames.length+1][Server.WIDTH_OF_ONLINE_FILE];
            //copies old file to new one
            for(int i=0;i<SERVER.onlineGames.length;i++){
                for(int x=0;x<Server.WIDTH_OF_ONLINE_FILE;x++){
                    newOnlineGames[i][x]=SERVER.onlineGames[i][x];
                }
            }
            //adds new record
            for(int x=0;x<Server.WIDTH_OF_ONLINE_FILE;x++){
                newOnlineGames[SERVER.onlineGames.length][x]=record[x];
            }
            //sort file
            Sort.quickSortUsingIntAtFirstElement(newOnlineGames);
            SERVER.onlineGames=newOnlineGames;
        }
        SERVER.writeArrayToFileWithSplit(SERVER.onlineGames, Server.ONLINE_GAMES_FILE);//updates the text file
        return gameId;//returns the game id
    }
    /**
     * Handles the request to play a new random online game.
     * @param userName The username of the player making the request
     */
    private synchronized void handleRequestForNewOnlineRandomGame(String userName){
        //checks to see if any requests for online random games exsist - if they do then the user is assigned to one of these requests
        for(int y=0;y<SERVER.onlineGames.length;y++){
            if(SERVER.onlineGames[y][7].equals("false")&&SERVER.onlineGames[y][2].equals("")&&!SERVER.onlineGames[y][1].equals(userName)){//checks to see if a random game has already been created but not accpeted by another player
                SERVER.onlineGames[y][2]=userName;
                SERVER.writeArrayToFileWithSplit(SERVER.onlineGames, Server.ONLINE_GAMES_FILE);//updates the text file
                return;
            }
        }
        //if no existing play request is found, a new one must be created
        Random rnd = new Random();
        int randNum=rnd.nextInt(3);
        boolean isPlayerOneWhite=Math.random()>=0.5;
        //a random time control is chosen for the new game
        switch (randNum) {
            case 0:
                createNewOnlineGame(userName, "", true, false, false,isPlayerOneWhite);
                break;
            case 1:
                createNewOnlineGame(userName, "", false, true, false,isPlayerOneWhite);
                break;
            default:
                createNewOnlineGame(userName, "", false, false, false,isPlayerOneWhite);
                break;
        }
        
    }
    /**
     * Creates a new online game against a friend, checking that the game can legally be created
     * @param playerOne The username of player one
     * @param playerTwo The username of player two -"" should be input if the game code is to be shared
     * @param timeControl The time control used
     * @return The game code. "-1" is returned if the game can't be created
     */
    public synchronized String createOnlineGameAgainstFriend(String playerOne,String playerTwo,String timeControl){
        //checks to ensure that game can legally be created
        if(!getAllGamesWithUser(playerOne)[2][0].equals("") ||(!playerTwo.equals("")&&!getAllGamesWithUser(playerTwo)[2][0].equals(""))){
            return "-1";
        }
        //adds game to file
        switch (timeControl) {
            case "-100":
                return(createNewOnlineGame(playerOne, playerTwo, true, false, playerTwo.equals(""), Math.random()>=0.5));
            case "600":
                return(createNewOnlineGame(playerOne, playerTwo, false, true, playerTwo.equals(""), Math.random()>=0.5));
            default:
                return(createNewOnlineGame(playerOne, playerTwo, false, false, playerTwo.equals(""), Math.random()>=0.5));
        }
    }
    /**
     * Attempts to join an existing game using the code
     * @param userName The username of the player attempting to join
     * @param gameCode The game code
     * @return Whether the game was joined
     */
    public synchronized boolean joinGameUsingFriendCode(String userName,int gameCode){
        int recordIndex=Search.searchForRecordIndexInt(SERVER.onlineGames, gameCode);//attempts to locate game
        if(recordIndex==Search.INDEX_NOT_FOUND){
            return false;
        }
        if(SERVER.onlineGames[recordIndex][1].equals(userName)){//ensures that user can't join a game against themselves
            return false;
        }
        if(SERVER.onlineGames[recordIndex][7].equals("false")){//prevents the user from joining a random game
            return false;
        }
        //allows the user to join the game
        SERVER.onlineGames[recordIndex][2]=userName;
        SERVER.writeArrayToFileWithSplit(SERVER.onlineGames, Server.ONLINE_GAMES_FILE);
        return true;
    }
    /**
     * Gets the game data requested
     * @param gameCode the game code
     * @return The data
     */
    public synchronized String []getGameRecord(int gameCode){
        String []record=new String[Server.WIDTH_OF_ONLINE_FILE];
        int index=Search.searchForRecordIndexInt(SERVER.onlineGames, gameCode);
        if(index==-1){
            return record;
        }
        //copies record to new array
        for(int i=0;i<record.length;i++){
            record[i]=SERVER.onlineGames[index][i];
        }
        return record;
    }
}
