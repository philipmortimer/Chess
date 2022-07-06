/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package philipm.cs5.software.development.user.system;

import philipm.cs5.software.development.server.system.UserInfo;

/**
 * This class is used to store various user data to create an object that gives needed information to various system components on the current user.
 * @author mortimer
 */
//stores session Information, such as username if appropraite
public class SessionInfo {
    private boolean isGuest;//stores whether the user is a guest
    private boolean isClubMember;//stores whether user is a member of the club
    private boolean isClubLeader;//stores whether user is the club leader
    private String username;//stores username
    /**
     * Creates a new SessionInfo Object with the appropriate user details
     * @param username The username
     * @param isClubMember Whether the user is currently a club member
     * @param isClubLeader Whether the user is currently a club leader
     */
    public SessionInfo( String username, boolean isClubMember,boolean isClubLeader){
        this.setIsClubLeader(isClubLeader);this.setIsClubMember(isClubMember);this.setUsername(username);this.setIsGuest(false);
    }
    /**
     * Creates a SessionInfo object
     */
    private SessionInfo(){
        
    }
    /**
     * Creates a new SessionInfo object for guests.
     * @return The SessionInfo Object
     */
    public static SessionInfo createGuestSession(){
        SessionInfo sess=  new SessionInfo();
        sess.setIsGuest(true);sess.setUsername(UserInfo.GUEST_ID);sess.setIsClubMember(false);sess.setIsClubLeader(false);
        return sess;
    }
    /**
     * Sets whether the user is a guest
     * @param isGuest whether the user is a guest
     */
    private void setIsGuest(boolean isGuest){
        this.isGuest=isGuest;
    }
    /**
     * Sets the username of the user
     * @param username The username
     */
    private void setUsername(String username){
        this.username=username;
    }
    /**
     * Sets whether the user is a member of the chess club
     * @param isClubMember Whether the user is a member of the chess club
     */
    public void setIsClubMember(boolean isClubMember){
        this.isClubMember=isClubMember;
    }
    /**
     * Sets whether the user is the club leader.
     * @param isClubLeader Whether the user is the club leader.
     */
    public void setIsClubLeader(boolean isClubLeader){
        this.isClubLeader=isClubLeader;
    }
    /**
     * Gets whether the current user is a guest user.
     * @return Whether the current user is a guest user.
     */
    public boolean getIsGuest(){
        return this.isGuest;
    }
    /**
     * Gets whether the current user is a member of the West Cross Chess Club
     * @return Whether the current user is a member of the West Cross Chess Club
     */
    public boolean getIsClubMember(){
        return this.isClubMember;
    }
    /**
     * Gets whether the current is user is the leader of the West Cross Chess Club
     * @return Whether the current is user is the leader of the West Cross Chess Club
     */
    public boolean getIsClubLeader(){
        return this.isClubLeader;
    }
    /**
     * Gets the username of the current user
     * @return The username
     */
    public String getUsername(){
        return this.username;
    }
}
