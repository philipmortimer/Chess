/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package philipm.cs5.software.development.user.system;

import java.io.IOException;
import philipm.cs5.software.development.server.system.UserInfo;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * This objects consists of a set of static methods which send (and sometimes receive) data from / to the server.
 * @author mortimer
 */
public class ThreadClient {
    /**
     * Tries to log user in with provided details
     * @param userName The username of the user
     * @param password The users password
     * @return The user information
     * @throws Exception If an error occurs, such as failing to connect to the server, an exception is thrown
     */
    public static UserInfo logInWithUserName(String userName, String password) throws Exception{
        Socket socket = new Socket(InetAddress.getByName(ChangeInternetSettings.getServerAddress()),ChangeInternetSettings.getPortNumber());//creates a socket to connect with the server
        ObjectInputStream in =new ObjectInputStream(socket.getInputStream());//gets the object input stream
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());//gets the object output stream
        InputPackages pack = new InputPackages();pack.createLogInRequestForWithUserNameAndPassword(userName, password);//creates the package to send to the server
        out.writeObject(pack);//sends package to server
        return (UserInfo)in.readObject();//gets the UserInfo object from the server and returns it
    }
    /**
     * Tries to log user in with email address. 
    * @param emailAddress The user's email address
     * @param password The user's password
     * @return The user information for the user
     * @throws Exception If a networking error occurs, an exception may be thrown
     */
    public static UserInfo logInWithEmailAddress(String emailAddress, String password) throws Exception{
        Socket socket = new Socket(InetAddress.getByName(ChangeInternetSettings.getServerAddress()),ChangeInternetSettings.getPortNumber());//creates socket that connects to the server
        ObjectInputStream in =new ObjectInputStream(socket.getInputStream());//gets input stream
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());//gets output stream
        InputPackages pack = new InputPackages();pack.createLogInRequestForWithEmailAndPassword(emailAddress, password);//creates package to send to sever
        out.writeObject(pack);//sends package to server
        UserInfo inf=(UserInfo)in.readObject();
        return inf;//gets the UserInfo object from the server and returns it
    }
    /**
     * Checks to see if the provided username is already stored in the user database
     * @param username The username to be fetched
     * @return Returns true if the username if already taken and false otherwise
     * @throws Exception An exception may be thrown due to the online connection required to execute this method.
     */
    public static boolean isUserNameAlreadyTaken(String username)throws Exception{
        try{
            return logInWithUserName(username,"passw").getUserFound();//attempts to log in with the chosen username and an arbitary password
        }catch(Exception e){
            throw e;
        }
    }
    /**
     * Checks to see if the provided email address is already stored in the user database
     * @param emailAddress The email address to be fetched
     * @return Returns true if the email address if already taken and false otherwise
     * @throws Exception An exception may be thrown due to the online connection required to execute this method.
     */
    public static boolean isEmailAddressAlreadyTaken(String emailAddress)throws Exception{
        try{
            return logInWithEmailAddress(emailAddress, "passw").getEmailAddressFound();//attempts to log in with chosen email address and an arbitary password
        }catch(Exception e){
            throw e;
        }
    }
    /**
     * Communicates with the server to create the new user account.
     * @param accountDetails The array containing the account record to be written to the file
     * @param emailAddress The email address of the account
     * @throws Exception An exception may occur due to network issues.
     */
    public static void createAccount(String[]accountDetails,String emailAddress)throws Exception{
        Socket socket = new Socket(InetAddress.getByName(ChangeInternetSettings.getServerAddress()),ChangeInternetSettings.getPortNumber());//creates socket that communicates with server
        InputPackages pack = new InputPackages();pack.createAccountPacket(accountDetails, accountDetails[0], emailAddress);//creates package to send to server
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());//gets output stream
        out.writeObject(pack);//writes package to server
    }
    /**
     * Gets data for online games for a specific user
     * @param userName The username of the user
     * @return The game data
     * @throws java.lang.Exception If a networking error occurs, then an error is thrown
     */
    public static String[][]getOnlineGameData(String userName) throws Exception{
        Socket socket = new Socket(InetAddress.getByName(ChangeInternetSettings.getServerAddress()),ChangeInternetSettings.getPortNumber());//connects to server
        InputPackages pack = new InputPackages();//creates a new input package
        pack.createRequestForAllGameDataForAUser(userName);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(pack);//writes the package to the server
        ObjectInputStream in =new ObjectInputStream(socket.getInputStream());//creates the object input stream   
        return (String[][])in.readObject();
    }
    public static void createNewRandomOnlineGame(String userName)throws Exception{
        Socket socket = new Socket(InetAddress.getByName(ChangeInternetSettings.getServerAddress()),ChangeInternetSettings.getPortNumber());//connects to server
        InputPackages pack = new InputPackages();//creates a new input package
        pack.createRandomGamePackage(userName);//creates server request
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(pack);//writes the package to the server
    }
    /**
     * Communicates with server to delete the specified game
     * @param gameId The game id of the game to delete 
     * @param gameResult The game result. 1 indicates a win for player one. 0.5 indicates a draw. 0 indicates a win for player two
     * @throws java.lang.Exception An exception may occur when communicating with the server
     */
    public static void deleteOnlineGame(int gameId,double gameResult)throws Exception{
        Socket socket = new Socket(InetAddress.getByName(ChangeInternetSettings.getServerAddress()),ChangeInternetSettings.getPortNumber());//connects to server
        InputPackages pack = new InputPackages();//creates a new input package
        pack.createRequestToDeleteGame(String.valueOf(gameId),gameResult);//creates server request
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(pack);//writes the package to the server        
    }
    public static void updateOnlineGameRecord(String[]newRecord)throws Exception{
        Socket socket = new Socket(InetAddress.getByName(ChangeInternetSettings.getServerAddress()),ChangeInternetSettings.getPortNumber());//connects to server
        InputPackages pack = new InputPackages();//creates a new input package
        pack.createRequestToUpdateOnlineGame(newRecord);//creates server request
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(pack);//writes the package to the server          
    }
    public static String createGameAgainstFriend(String playerOneUserName,String playerTwoName,int timeLeftForBoth)throws Exception{
        Socket socket = new Socket(InetAddress.getByName(ChangeInternetSettings.getServerAddress()),ChangeInternetSettings.getPortNumber());//connects to server
        ObjectInputStream in =new ObjectInputStream(socket.getInputStream());//gets input stream
        InputPackages pack = new InputPackages();//creates a new input package
        pack.createNewOnlineGameWithFriend(playerOneUserName, playerTwoName, String.valueOf(timeLeftForBoth));//creates server request
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(pack);//writes the package to the server    
        return (String)in.readObject();
    }
    /**
     * Tries to join a game using a friend code. If the game is found and has been joined, true is returned. Otherwise, false is returned
     * @param code the game code
     * @param userName The username of the player attempting to join the game
     * @return Whether the game has been joined
     * @throws java.lang.Exception An error when communicating with the server
     */
    public static boolean joinGameUsingFriendCode(int code,String userName)throws Exception{
        Socket socket = new Socket(InetAddress.getByName(ChangeInternetSettings.getServerAddress()),ChangeInternetSettings.getPortNumber());//connects to server
        ObjectInputStream in =new ObjectInputStream(socket.getInputStream());//gets input stream
        InputPackages pack = new InputPackages();//creates a new input package
        pack.joinGameUsingGameCode(code,userName);//creates server request
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(pack);//writes the package to the server    
        return (boolean)in.readObject();       
    }
    /**
     * Gets the game data stored in the online game file
     * @param gameCode The game id
     * @return The record - if the record is not found, the array will all be null
     * @throws java.lang.Exception An error when communicating with the server
     */
    public static String[] getGameData(int gameCode) throws Exception{
        Socket socket = new Socket(InetAddress.getByName(ChangeInternetSettings.getServerAddress()),ChangeInternetSettings.getPortNumber());//connects to server
        ObjectInputStream in =new ObjectInputStream(socket.getInputStream());//gets input stream
        InputPackages pack = new InputPackages();//creates a new input package
        pack.getGameRecord(gameCode);//creates server request
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(pack);//writes the package to the server    
        return (String[])in.readObject();           
    }
    /**
     * Gets the contact details of the West Cross chess club.
     * @return The contact details. Element 0 is the phone number and element 1 is the email address to contact.
     * @throws java.lang.Exception An exception may occur when communicating with the server
     */
    public static String[]getChessClubContactDetails() throws Exception{
        Socket socket = new Socket(InetAddress.getByName(ChangeInternetSettings.getServerAddress()),ChangeInternetSettings.getPortNumber());//connects to server
        ObjectInputStream in =new ObjectInputStream(socket.getInputStream());//gets input stream
        InputPackages pack = new InputPackages();//creates a new input package
        pack.requestClubContactInfo();//creates server request
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(pack);//writes the package to the server    
        return (String[])in.readObject();          
    }
    /**
     * Checks to see whether the user has requested to join the club
     * @param userName The username
     * @return True if the user has and false otherwise
     * @throws Exception An exception may occur when communicating with the server
     */
    public static boolean getHasUserRequestedToJoinChessClub(String userName) throws Exception{
        Socket socket = new Socket(InetAddress.getByName(ChangeInternetSettings.getServerAddress()),ChangeInternetSettings.getPortNumber());//connects to server
        ObjectInputStream in =new ObjectInputStream(socket.getInputStream());//gets input stream
        InputPackages pack = new InputPackages();//creates a new input package
        pack.getWhetherUserHasRequestedToJoinClub(userName);//creates server request
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(pack);//writes the package to the server    
        return in.readBoolean();
    }
    /**
     * Alters the status of a user's request to join the club. I.e., if they have requested to join the club, this request is withdrawn and vice versa.
     * @param userName The username of the user
     * @throws Exception An exception may occur when communicating with the server
     */
    public static void changeRequestToJoinClubStatus(String userName)throws Exception{
        Socket socket = new Socket(InetAddress.getByName(ChangeInternetSettings.getServerAddress()),ChangeInternetSettings.getPortNumber());//connects to server
        InputPackages pack = new InputPackages();//creates a new input package
        pack.changeRequestToJoinClubStatus(userName);//creates server request
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(pack);//writes the package to the server             
    }
    /**
     * Removes user from the chess club.
     * @param userName The username of the user
     * @throws Exception An exception may occur when communicating with the server
     */
    public static void removeUserFromChessClub(String userName)throws Exception{
        Socket socket = new Socket(InetAddress.getByName(ChangeInternetSettings.getServerAddress()),ChangeInternetSettings.getPortNumber());//connects to server
        InputPackages pack = new InputPackages();//creates a new input package
        pack.removeUserFromChessClub(userName);//creates server request
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(pack);//writes the package to the server          
    }
    /**
     * Gets the league table
     * @return The table
     * @throws Exception An exception may occur when communicating with the server
     */
    public static String[][]getLeagueTable()throws Exception{
        Socket socket = new Socket(InetAddress.getByName(ChangeInternetSettings.getServerAddress()),ChangeInternetSettings.getPortNumber());//connects to server
        ObjectInputStream in =new ObjectInputStream(socket.getInputStream());//gets input stream
        InputPackages pack = new InputPackages();//creates a new input package
        pack.getLeagueTableRequest();//creates server request
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(pack);//writes the package to the server    
        return (String[][])in.readObject();       
    }
    /**
     * Gets the all members of the chess club
     * @return The members
     * @throws Exception An exception may occur when communicating with the server
     */
    public static String[]getMembersOfChessClub()throws Exception{
        Socket socket = new Socket(InetAddress.getByName(ChangeInternetSettings.getServerAddress()),ChangeInternetSettings.getPortNumber());//connects to server
        ObjectInputStream in =new ObjectInputStream(socket.getInputStream());//gets input stream
        InputPackages pack = new InputPackages();//creates a new input package
        pack.getAllClubMembers();//creates server request
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(pack);//writes the package to the server    
        return (String[])in.readObject();       
    }  
    /**
     * Gets the all fixtures and results of current season. The array will be of length 0 if an error occurred (e.g. no fixtures can be displayed)
     * @return The fixtures and results.
     * @throws Exception An exception may occur when communicating with the server
     */
    public static String[][][]getFixturesAndResults()throws Exception{
        Socket socket = new Socket(InetAddress.getByName(ChangeInternetSettings.getServerAddress()),ChangeInternetSettings.getPortNumber());//connects to server
        ObjectInputStream in =new ObjectInputStream(socket.getInputStream());//gets input stream
        InputPackages pack = new InputPackages();//creates a new input package
        pack.getFixturesAndResults();//creates server request
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(pack);//writes the package to the server    
        return (String[][][])in.readObject();               
    }
    /**
     * Gets the all fixtures and results of current season. The array will be of length 0 if an error occurred (e.g. no fixtures can be displayed)
     * @return The fixtures and results.
     * @throws Exception An exception may occur when communicating with the server
     */
    public static String[]getListOfPeopleAddedMidwayThroughSeason()throws Exception{
        Socket socket = new Socket(InetAddress.getByName(ChangeInternetSettings.getServerAddress()),ChangeInternetSettings.getPortNumber());//connects to server
        ObjectInputStream in =new ObjectInputStream(socket.getInputStream());//gets input stream
        InputPackages pack = new InputPackages();//creates a new input package
        pack.getPeopleAddedMidwayThroughSeason();//creates server request
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(pack);//writes the package to the server    
        return (String[])in.readObject();               
    }
    /**
     * Updates the server stored fixture's and results
     * @param newFixturesAndResults The updated fixture and results
     * @throws java.io.IOException An exception may occur when communicating with the server
     */
    public static void updateFixturesAndResults(String[][][]newFixturesAndResults)throws IOException{
        Socket socket = new Socket(InetAddress.getByName(ChangeInternetSettings.getServerAddress()),ChangeInternetSettings.getPortNumber());//connects to server
        InputPackages pack = new InputPackages();//creates a new input package
        pack.updateFixturesAndResults(newFixturesAndResults);//creates server request
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(pack);//writes the package to the server             
    }
    /**
     * Starts a new season
     * @throws java.io.IOException An exception may occur when communicating with the server
     */
    public static void createNewSeason()throws IOException{
        Socket socket = new Socket(InetAddress.getByName(ChangeInternetSettings.getServerAddress()),ChangeInternetSettings.getPortNumber());//connects to server
        InputPackages pack = new InputPackages();//creates a new input package
        pack.startNewSeason();//creates server request
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(pack);//writes the package to the server             
    }
    /**
     * Updates the chess club contact details
     * @param phoneNumber The chess club phone number
     * @param emailAddress The email address of the club
     * @throws IOException An exception may occur when communicating with the server
     */
    public static void setChessClubContactDetails(String phoneNumber,String emailAddress) throws IOException{
        Socket socket = new Socket(InetAddress.getByName(ChangeInternetSettings.getServerAddress()),ChangeInternetSettings.getPortNumber());//connects to server
        InputPackages pack = new InputPackages();//creates a new input package
        pack.setContactDetails(phoneNumber,emailAddress);//creates server request
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(pack);//writes the package to the server           
    }
    /**
     * Sets the inputted user as both a club member and the club leader
     * @param userName The user name
     * @throws IOException An exception may occur when communicating with the server
     */
    public static void setUserAsClubLeader(String userName) throws IOException{
        Socket socket = new Socket(InetAddress.getByName(ChangeInternetSettings.getServerAddress()),ChangeInternetSettings.getPortNumber());//connects to server
        InputPackages pack = new InputPackages();//creates a new input package
        pack.setClubLeader(userName);//creates server request
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(pack);//writes the package to the server             
    }
    /**
     * Returns all requests to join the west cross chess club.
     * @return The requests. Each row is one record from the user details column
     * @throws java.io.IOException An exception may occur when communicating with the server
     */
    public static String[][]getAllRequestsToJoinClub() throws Exception{
        Socket socket = new Socket(InetAddress.getByName(ChangeInternetSettings.getServerAddress()),ChangeInternetSettings.getPortNumber());//connects to server
        ObjectInputStream in =new ObjectInputStream(socket.getInputStream());//gets input stream
        InputPackages pack = new InputPackages();//creates a new input package
        pack.getAllRequestsToJoinClub();//creates server request
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(pack);//writes the package to the server    
        return (String[][])in.readObject();           
    }
    /**
     * Returns the email address of a chosen user
     * @param userName The username
     * @return The email address
     * @throws java.lang.Exception An exception may occur when communicating with the server
     */
    public static String getEmailAddressOfUser(String userName)throws Exception{
        Socket socket = new Socket(InetAddress.getByName(ChangeInternetSettings.getServerAddress()),ChangeInternetSettings.getPortNumber());//connects to server
        ObjectInputStream in =new ObjectInputStream(socket.getInputStream());//gets input stream
        InputPackages pack = new InputPackages();//creates a new input package
        pack.getEmailAddressOfUser(userName);//creates server request
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(pack);//writes the package to the server    
        return ((String[])in.readObject())[0];               
    }
    /**
     * Makes it so that the user chosen has not requested to join the chess club.
     * @param userName The user name
     * @throws Exception An exception may occur when communicating with the server
     */
    public static void withdrawRequestToJoinClub(String userName) throws Exception{
        Socket socket = new Socket(InetAddress.getByName(ChangeInternetSettings.getServerAddress()),ChangeInternetSettings.getPortNumber());//connects to server
        InputPackages pack = new InputPackages();//creates a new input package
        pack.withdrawRequestToJoinClub(userName);//creates server request
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(pack);//writes the package to the server            
    }
    /**
     * Adds the specified user to the west cross chess club
     * @param userName The user
     * @throws Exception An exception may occur when communicating with the server
     */
    public static void addUserToClubAsMember(String userName)throws Exception{
        Socket socket = new Socket(InetAddress.getByName(ChangeInternetSettings.getServerAddress()),ChangeInternetSettings.getPortNumber());//connects to server
        InputPackages pack = new InputPackages();//creates a new input package
        pack.addUserToClubAsMember(userName);//creates server request
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(pack);//writes the package to the server              
    }
    /**
     * Gets the user info of all members who are part of the club
     * @return The info
     * @throws Exception An exception may occur when communicating with the server
     */
    public static String[][] getAllClubMemberUserInfo()throws Exception{
        Socket socket = new Socket(InetAddress.getByName(ChangeInternetSettings.getServerAddress()),ChangeInternetSettings.getPortNumber());//connects to server
        ObjectInputStream in =new ObjectInputStream(socket.getInputStream());//gets input stream
        InputPackages pack = new InputPackages();//creates a new input package
        pack.getAllClubMembersInfo();//creates server request
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(pack);//writes the package to the server    
        return (String[][])in.readObject();            
    }
    /**
     * Updates the user record of the user who's user name is stored at element 0 of the inputted array.
     * @param userRecord The new record
     * @throws Exception An exception may occur when communicating with the server
     */
    public static void updateUserInfo(String[]userRecord)throws Exception{
        Socket socket = new Socket(InetAddress.getByName(ChangeInternetSettings.getServerAddress()),ChangeInternetSettings.getPortNumber());//connects to server
        InputPackages pack = new InputPackages();//creates a new input package
        pack.updateUserRecord(userRecord);//creates server request
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(pack);//writes the package to the server              
    }
    /**
     * Deletes the data of a non chess club member from the server
     * @param userName The username
     * @throws java.lang.Exception An exception may occur when communicating with the server
     */
    public static void deleteUserInfoOfNonMemberUserFromServer(String userName)throws Exception{
        Socket socket = new Socket(InetAddress.getByName(ChangeInternetSettings.getServerAddress()),ChangeInternetSettings.getPortNumber());//connects to server
        InputPackages pack = new InputPackages();//creates a new input package
        pack.removeNonMemberUserFromRecord(userName);//creates server request
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(pack);//writes the package to the server               
    }
    /**
     * Gets all non chess club members
     * @return The users
     * @throws Exception An exception may occur when communicating with the server
     */
    public static String[][]getAllUsersWhoAreNotMembers() throws Exception{
        Socket socket = new Socket(InetAddress.getByName(ChangeInternetSettings.getServerAddress()),ChangeInternetSettings.getPortNumber());//connects to server
        ObjectInputStream in =new ObjectInputStream(socket.getInputStream());//gets input stream
        InputPackages pack = new InputPackages();//creates a new input package
        pack.getAllNonMembers();//creates server request
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(pack);//writes the package to the server    
        return (String[][])in.readObject();          
    }
}
