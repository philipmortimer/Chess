/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package philipm.cs5.software.development.user.system;

import java.io.Serializable;
import philipm.cs5.software.development.server.system.UserInfo;

/**
 * This class is used to create a package to send to the server. It creates an object which essentially stores an array. Element 0 of this
 * array is a command which indicates what the server has to do. The command is a constant and stored in this class.
 * @author mortimer
 */
public class InputPackages implements Serializable {
    private String[]packageToSend;//this stores the package to send
    //these are the constant which are inserted at the nought element of the array. These commands indicate which task needs to be performed
    public static final String GET_USER_INFO_WITH_USERNAME_AND_PASSWORD="UserInfWithPassAndUserName9123";
    public static final String GET_USER_INFO_WITH_EMAIL_AND_PASSWORD="UserInfWithPassAndEmail3468";
    public static final String CREATE_ACCOUNT="cReateAccount234s";
    public static final String GET_ONLINE_GAMES_FOR_USER="getOnlinegamedetailsforuser";
    public static final String CREATE_RANDOM_GAME="newRandomGameRequest189249";//constant that stores request to create a new random online game
    public static final String DELETE_GAME="deletegameRequest1224";//stores the constant that signals a request to delete a game
    public static final String UPDATED_ONLINE_GAME_RECORD="updateOnlinerecordRequestConst23";//requests to update online record
    public static final String CREATE_NEW_GAME_AGAINST_FRIEND="createNewGameAgainstFriendReq3924";
    public static final String JOIN_GAME_USING_GAME_CODE="joindWithCode9123";
    public static final String GET_GAME_RECORD="getGameRecordWithCode";
    public static final String GET_CLUB_CONTACT_DETAILS="getClubConatctddata35";
    public static final String GET_WHETHER_USER_HAS_REQUESTED_TO_JOIN_CLUB="hasUserrequestedtojoinclun14";
    public static final String CHANGE_STATUS_REQUEST_TO_JOIN_CLUB="changeREquestStatsu12984";
    public static final String REMOVE_USER_FROM_CHESS_CLUB="removeUserFromChessClub124";
    public static final String GET_LEAGUE_TABLE="getELaegy";
    public static final String GET_ALL_CLUB_MEMBERS="getAllMembers";
    public static final String GET_FIXTURES_AND_RESULTS="fixturesAndRes";
    public static final String GET_PEOPLE_ADDED_MIDWAY_THROUGH_SEASON="getMidwaypead";
    public static final String UPDATED_FIXTURES_AND_RESULTS="updateFixtsAndres";
    private String[][][]fixturesAndResults; //stores the fixtures and results if that is what is being sent to the server
    public static final String CREATE_NEW_SEASON="newSeasonas";
    public static final String SET_CONTACT_DETAILS="setContactdakl";
    public static final String SET_USER_AS_LEADER="setLEaderaf";
    public static final String GET_ALL_REQUESTS="alLRequessf";
    public static final String GET_EMAIL_OF_USER="getEmailUseraf";
    public static final String REMOVE_REQUEST_JOIN_CLUB="setUserRequstClubFalse";
    public static final String ADD_USER_TO_CLUB="addUserToClub";
    public static final String GET_CLUB_MEMBER_DETAILS_ALL_MEMBERS="getAllClubmeiaefd";
    public static final String UPDATE_USER_INFO_FILE_RECORD="updahusasf";
    public static final String REMOVE_NON_MEMBER_USER="sausdf";
    public static final String GET_ALL_NON_MEMBERS="getAllNonMebrsfd";
    /**
     * Creates an input package to get user info by providing log in details
     * @param userName The username
     * @param password The password
     */
    public void createLogInRequestForWithUserNameAndPassword(String userName,String password){
        packageToSend= new String[3];
        packageToSend[0]=GET_USER_INFO_WITH_USERNAME_AND_PASSWORD;packageToSend[1]=userName;packageToSend[2]=password;
    }
    /**
     * Creates an InputPackage object
     */
    public InputPackages(){
        
    }
    /**
     * Creates package that requests the fixtures and results
     */
    public void getFixturesAndResults(){
        packageToSend=new String[1];packageToSend[0]=GET_FIXTURES_AND_RESULTS;
    }
    /**
     * Creates package that requests the the list of people added midway through the season.
     */
    public void getPeopleAddedMidwayThroughSeason(){
        packageToSend=new String[1];packageToSend[0]=GET_PEOPLE_ADDED_MIDWAY_THROUGH_SEASON;
    }
    /**
     * Creates package that requests the fixtures and results be updated to the inputted fixture and results.
     * @param fixturesAndResults The new fixtures and results.
     */
    public void updateFixturesAndResults(String[][][]fixturesAndResults){
        packageToSend=new String[]{UPDATED_FIXTURES_AND_RESULTS};
        this.fixturesAndResults=fixturesAndResults;
    }
    /**
     * Returns the fixtures and results array stored in the package
     * @return The fixtures and results array
     */
    public String[][][]getFixturesAndResultsArray(){
        return this.fixturesAndResults;
    }
    /**
     * Creates a package that requests to add a specified user to the chess club.
     * @param userName The username of the user to add.
     */
    public void addUserToClubAsMember(String userName){
        packageToSend=new String[]{ADD_USER_TO_CLUB,userName};
    }
    /**
     * Creates package that requests that a new season is started on the server.
     */
    public void startNewSeason(){
        packageToSend=new String[]{CREATE_NEW_SEASON};
    }
    /**
     * Creates a package that requests the server alter the club contact details.
     * @param phoneNumber The new phone number
     * @param emailAddress The new email address
     */
    public void setContactDetails(String phoneNumber, String emailAddress){
        packageToSend=new String[]{SET_CONTACT_DETAILS,phoneNumber,emailAddress};
    }
    /**
     * Creates package that requests for the server to make the chosen user the club leader
     * @param userName The name of the user
     */
    public void setClubLeader(String userName){
        packageToSend=new String[]{SET_USER_AS_LEADER,userName};
    }
    /**
     * Creates a package that requests the user details of all users who have requested to join the club.
     */
    public void getAllRequestsToJoinClub(){
        packageToSend=new String[]{GET_ALL_REQUESTS};
    }
    /**
     * Creates a package that requests the email address of a chosen user
     * @param userName The username of the chosen user
     */
    public void getEmailAddressOfUser(String userName){
        packageToSend=new String[]{GET_EMAIL_OF_USER,userName};
    }
    /**
     * Creates a package that requests that the server give the client the user details of all non members.
     */
    public void getAllNonMembers(){
        packageToSend=new String[]{GET_ALL_NON_MEMBERS};
    }
    /**
     * Creates a new input package which requests all online game data on a user
     * @param userName The username
     */
    public void createRequestForAllGameDataForAUser(String userName){
        packageToSend=new String[2];
        packageToSend[1]=userName;
        packageToSend[0]=GET_ONLINE_GAMES_FOR_USER;
    }
    /**
     * Creates a package that tells the server to withdraw the request to join the club from a given user.
     * @param userName The username of the user to withdraw the request from.
     */
    public void withdrawRequestToJoinClub(String userName){
        packageToSend=new String[]{REMOVE_REQUEST_JOIN_CLUB,userName};
    }
    /**
     * Creates package that requests the user information on all club members.
     */
    public void getAllClubMembers(){
        packageToSend=new String[1];packageToSend[0]=GET_ALL_CLUB_MEMBERS;
    }
    /**
     * Creates an input package to get user info by providing log in details
     * @param emailAddress The email Address
     * @param password The password
     */
    public void createLogInRequestForWithEmailAndPassword(String emailAddress,String password){
        packageToSend= new String[3];
        packageToSend[0]=GET_USER_INFO_WITH_EMAIL_AND_PASSWORD;packageToSend[1]=emailAddress;packageToSend[2]=password;      
    }
    /**
     * Creates a new package that requests for an online game to be created
     * @param userName The username of the user who wishes to play a new random online game
     */
    public void createRandomGamePackage(String userName){
        packageToSend=new String[2];
        packageToSend[0]=CREATE_RANDOM_GAME;packageToSend[1]=userName;
    }
    /**
     * Creates a package that requests all data on club members.
     */
    public void getAllClubMembersInfo(){
        packageToSend=new String[]{GET_CLUB_MEMBER_DETAILS_ALL_MEMBERS};
    }
    /**
     * Creates a package to tell server to update a user record.
     * @param newRecord The new record
     */
    public void updateUserRecord(String[]newRecord){
        packageToSend=new String[UserInfo.WIDTH_OF_USER_DETAILS_FILE+1];
        packageToSend[0]=UPDATE_USER_INFO_FILE_RECORD;
        for(int i=0;i<UserInfo.WIDTH_OF_USER_DETAILS_FILE;i++){
            packageToSend[i+1]=newRecord[i];
        }
    }
    /**
     * Gets the package array
     * @return The string array storing the action
     */
    public String[]getPackageToSend(){
        return packageToSend;
    }
    /**
     * Tells server to remove non club member from system.
     * @param userName The name of the user account to delete
     */
    public void removeNonMemberUserFromRecord(String userName){
        packageToSend=new String[]{REMOVE_NON_MEMBER_USER,userName};
    }
    /**
     * Returns the action name
     * @return The nought element of the package to send array is returned
     */
    public String getActionName(){
        return packageToSend[0];
    }
    /**
     * Creates a package that requests for the league table
     */
    public void getLeagueTableRequest(){
        packageToSend=new String[1];
        packageToSend[0]=GET_LEAGUE_TABLE;
    }
    /**
     * Creates a package that requests the server to add a new user record.
     * @param userRecord The user record array
     * @param userName The username of the user
     * @param emailAddress The email address
     */
    public void createAccountPacket(String []userRecord,String userName, String emailAddress){
        packageToSend=new String[userRecord.length+3];
        packageToSend[0]=CREATE_ACCOUNT;
        for(int i=0;i<userRecord.length;i++){
            packageToSend[i+1]=userRecord[i];
        }
        packageToSend[userRecord.length+1]=userName;
        packageToSend[userRecord.length+2]=emailAddress;
    }
    /**
     * Returns the user record. Note: this only works if the command is actually for creating an account.
     * @return The user record.
     */
    public String[]getUserRecod(){
        String[] rec = new String [UserInfo.WIDTH_OF_USER_DETAILS_FILE];
        for(int i=1;i<UserInfo.WIDTH_OF_USER_DETAILS_FILE+1;i++){
            rec[i-1]=packageToSend[i];
        }
        return rec;
    }
    /**
     * Returns the user name. Must be called only when the input package is to create an account.
     * @return The username
     */
    public String getUserName(){
        return packageToSend[UserInfo.WIDTH_OF_USER_DETAILS_FILE+1];
    }
    /**
     * Returns the email address. Must be called only when the input package is to create an account.
     * @return The email address
     */
    public String getEmail(){
        return packageToSend[UserInfo.WIDTH_OF_USER_DETAILS_FILE+2];
    }
    /**
     * Creates a copy of the current input package
     * @return A copy of the package
     */
    public InputPackages getCopyOfInputPackage(){
        String oldArray[]=this.getPackageToSend();
        String newArray[]=new String[oldArray.length];//copies previous array
        for(int i=0;i<newArray.length;i++){
            newArray[i]=oldArray[i];
        }
        return new InputPackages(newArray);
    }
    private InputPackages(String[]messageArray){
        packageToSend=messageArray;
    }
    /**
     * Creates a packet used to request that a game is deleted
     * @param gameId The game id of the game to be deleted
     * @param gameResult The game result: 1 signifies a win for player one. 0.5 indicates a draw and 0 indicates a player two win
     */
    public void createRequestToDeleteGame(String gameId, double gameResult){
        packageToSend=new String[3];packageToSend[0]=DELETE_GAME;packageToSend[1]=gameId;packageToSend[2]=String.valueOf(gameResult);
    }
    /**
     * Updates the online record
     * @param newOnlineGameRecord The updated record
     */
    public void createRequestToUpdateOnlineGame(String []newOnlineGameRecord){
        packageToSend=new String[1+newOnlineGameRecord.length];
        packageToSend[0]=UPDATED_ONLINE_GAME_RECORD;
        for(int i=0;i<newOnlineGameRecord.length;i++){
            packageToSend[i+1]=newOnlineGameRecord[i];
        }
    }
    /**
     * Creates package that tells the server to tell the client whether the chosen user has requested to join the club.
     * @param userName The name of the user
     */
    public void getWhetherUserHasRequestedToJoinClub(String userName){
        packageToSend=new String[2];packageToSend[0]=GET_WHETHER_USER_HAS_REQUESTED_TO_JOIN_CLUB;packageToSend[1]=userName;
    }
    /**
     * Creates a package that requests that the server changes the request status of a user.
     * @param userName The name of the user
     */
    public void changeRequestToJoinClubStatus(String userName){
        packageToSend=new String[2];packageToSend[0]=CHANGE_STATUS_REQUEST_TO_JOIN_CLUB;packageToSend[1]=userName;
    }
    /**
     * Creates a package that requests that the chosen user is removed from the chess club.
     * @param userName The name of the user
     */
    public void removeUserFromChessClub(String userName){
        packageToSend=new String[2];packageToSend[0]=REMOVE_USER_FROM_CHESS_CLUB;packageToSend[1]=userName;
    }
    /**
     * Creates a request to get the contact info of the chess club
     */
    public void requestClubContactInfo(){
        packageToSend=new String[1];packageToSend[0]=GET_CLUB_CONTACT_DETAILS;
    }
    /**
     * Creates a new request to play an online game against a friend
     * @param userNamePlayerOne the username of the first player
     * @param userNamePlayerTwo The second username or "" if the game is to be played by sharing a code
     * @param timeLeftForBoth The time left for both players
     */
    public void createNewOnlineGameWithFriend(String userNamePlayerOne, String userNamePlayerTwo,String timeLeftForBoth){
        packageToSend=new String[4];packageToSend[0]=CREATE_NEW_GAME_AGAINST_FRIEND;packageToSend[1]=userNamePlayerOne;
        packageToSend[2]=userNamePlayerTwo;packageToSend[3]=timeLeftForBoth;
    }
    /**
     * Attempts to join a game using the provided game code
     * @param gameCode The game code
     * @param userName The username of the player
     */
    public void joinGameUsingGameCode(int gameCode,String userName){
        packageToSend=new String[3];packageToSend[0]=JOIN_GAME_USING_GAME_CODE;packageToSend[1]=String.valueOf(gameCode);packageToSend[2]=userName;
    }
    /**
     * Creates a request to get game data
     * @param gameCode The game id
     */
    public void getGameRecord(int gameCode){
        packageToSend=new String[2];packageToSend[0]=GET_GAME_RECORD;packageToSend[1]=String.valueOf(gameCode);
    }
}
