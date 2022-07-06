/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package philipm.cs5.software.development.server.system;

import java.io.Serializable;

/**
 * This is a class that creates a UserInfo Object. This object is Serializable, allowing to be sent across a stream.
 * @author mortimer
 */
public class UserInfo implements Serializable{
    public static final int WIDTH_OF_USER_DETAILS_FILE=17;//stores the width of the user file
    private String[]userDetails;//stores the user record
    private boolean isPasswordCorrect=false;//stores whether the password is correct
    private boolean userFound;//stores whether the user with a given name was found in the user file
    public static final int WIDTH_OF_EMAIL_LINK_FILE=2;//stores the width of the email link file
    public static  transient final String GUEST_ID="GUEST_ID9239923";//stores the guest id
    public static transient final String GUEST_ID_EMAIL="GuestEmail9932d3@l.com";//stores the guest email address
    private boolean emailAddressFound=true;//stores whether the email address was found
    public static final String SPLIT_ITEM=";lO9";//used to split data items
    /*structure of user text FILE:
    Username, password, first name, surname, postcode, addressline one, address line two, address line three, address line four, discord username, discord number, date of birth, phone number, isClubLeader, isClubMember, ELO, hasUserRequestedToJoinClub
    */
    /**
     * Creates a new User with the record found and the password entered
     * @param record The user file record
     * @param userEnteredPasssword The password the user entered to access the account
     */
    public UserInfo(String[]record,String userEnteredPasssword){
        userFound = record[0]!=null;//updates whether the user was found
        userDetails = record;//stores the record
        if(userFound){
            //checks to see if the password is correct
            isPasswordCorrect=userEnteredPasssword.equals(record[1]);
        }
    }
    /**
     * Creates a new UserInfo object that indicates that the chosen email address was not found.
     * @return The UserInfoObject
     */
    public static UserInfo CreateUserInfoEmailNotFound(){
        return new UserInfo();
    }
    /**
     * Called privately to create a user info when email address is not found
     */
    private UserInfo(){
        emailAddressFound=false;
    }
    /**
     * Gets whether the email address was found
     * @return whether the email address was found
     */
    public boolean getEmailAddressFound(){
        return emailAddressFound;
    }
    /**
     * Gets the password associated with the user
     * @return Returns the password
     */
    public String getPassword(){
        return userDetails[1];
    }
    /**
     * Gets whether the user was found
     * @return whether the user was found
     */
    public boolean getUserFound(){
        return userFound;
    }
    /**
     * Gets whether the password entered was correct
     * @return whether the password entered was correct
     */
    public boolean getIsPasswordCorrect(){
        return isPasswordCorrect;
    }
    /**
     * Returns the user name stored.
     * @return The username
     */
    public String getUserName(){
        return userDetails[0];
    }
    /**
     * Returns whether the user is a club member.
     * @return Whether the user is  a club member
     */
    public boolean getIsClubMember(){
        return userDetails[14].equals("true");
    }
    /**
     * Returns whether the user is the club leader or not
     * @return Whether the user is club leader or not.
     */
    public boolean getIsClubLeader(){
        return userDetails[13].equals("true");
    }
    /**
     * Returns the ELO rating of the user
     * @return The ELO rating
     */
    public int getElo(){
        return Integer.parseInt(userDetails[15]);
    }
}
