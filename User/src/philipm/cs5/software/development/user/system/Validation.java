/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package philipm.cs5.software.development.user.system;

import philipm.cs5.software.development.server.system.UserInfo;

/**
 * This class performs various validation routines
 * @author mortimer
 */
public class Validation {
    private static final String[]ILLEGAL_CHAR_SEQUENCES={UserInfo.SPLIT_ITEM,OfflinePlay.SPLIT_FEN_STRING, UserInfo.GUEST_ID,UserInfo.GUEST_ID_EMAIL};//stores all illegeal characters sequences
    /**
     * Checks to see whether input string is empty or not. Returns false if length of String is 0 or a NullPointerException occurs (e.g. for newly intialised String arrays java).
     * Otherwise true is returned. Note: this presence check views a String with a (or any) single space(s) in it as 
     * a non-empty input and returns true for it.
     * @param stringToCheck the string to be checked
     * @return returns false if the String is empty (i.e. has a length of 0, using the .length() command or triggers a NullPointerException caused by String arrays that have been initialised but have no value assigned to them) and true if the string is not empty
     */
    public static boolean isPresent(String stringToCheck){//presence check
        try{
            if(stringToCheck.length()==0){//checks to see if number of characters are zero e.g.:""
                return false;
            }else{
                return true;
            }
        }catch(java.lang.NullPointerException e){//nullpointer exception triggered for newly initialised elements in a string array
            return false;
        }
    }
    /**
     * Checks to see if String is of desired length. If String is less than or equal to desired length, true is returned. Otherwise, false is returned.
     * @param maxLengthAllowed the maximum length of the string
     * @param stringToCheck the string to perform the length check on
     * @return returns true if the length of the string is less than or equal to the max length allowed and false if not.
     */
    public static boolean isAppropriateLength(int maxLengthAllowed,String stringToCheck){//length check
        return !(stringToCheck.length()>maxLengthAllowed);
    }
    /**
     * A simple format check to see whether a string is in the form dd/mm/yyyy.It checks to see that there are integers and dashes in the correct places.
     * It also checks to see if the month field is greater than twelve and if the day field is greater than 31 (although invalid inputs can occur, for example if the day is 31 and the month is february. If a valid date is not entered, false is returned.
     * @param stringToCheck the string to check
     * @return true if the inputted string is considered a valid date in the form dd/mm/yyyy.
     */
    public static boolean formatCheckDateOfBirth(String stringToCheck){//format check
        return((stringToCheck.matches("([0-9]{2})/([0-9]{2})/([0-9]{4})"))&&(Integer.parseInt(String.valueOf(stringToCheck.charAt(3))+String.valueOf(stringToCheck.charAt(4)))<=12)&&(Integer.parseInt(String.valueOf(stringToCheck.charAt(0))+String.valueOf(stringToCheck.charAt(1)))<=31));
    }
    /**
     * Tries converting the input string into an integer using the Integer.parseInt(). If a NumberFormatException is thrown, it is not an integer and false is returned, else true is returned.
     * @param stringToCheck the string to check 
     * @return true is returned if the string is an integer, false is returned if the string is not an integer
     */
    public static boolean isInteger(String stringToCheck){//type check
        try{
            int val = Integer.parseInt(stringToCheck);
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }
    /**
     * Checks to see if the integer to check is with in the range specified. True is returned if in the range, else false
     * @param lowerLimitInclusice this is the lower integer limit for range. Checks to see if integerToCheck is greater than OR equal to this number
     * @param upperLimitInclusive this is the upper integer limit for range. Checks to see if integerToCheck is less than OR equal to this number
     * @param integerToCheck the integer number to see if it is with in the specified range
     * @return True is returned if integerToCheck is greater than or equal to lowerLimitInclusive AND if integerToCheck is less than or equal to upperLimitInclusive. Otherwise, false is returned
     */
    public static boolean rangeCheck(int lowerLimitInclusice,int upperLimitInclusive,int integerToCheck){
        return (integerToCheck>=lowerLimitInclusice && integerToCheck<=upperLimitInclusive);
    }
    /**
     * Performs a linear search through all acceptable inputs comparing it to the String input. If a match is found, true is returned, else false is returned.
     * @param acceptableInputs A String array containing all acceptable inputs
     * @param stringToCheck The String to check against the acceptable inputs
     * @return True is returned if stringToCheck matches any item in the array, otherwise false is returned.
     */
    public static boolean lookUpCheck(String[]acceptableInputs,String stringToCheck){//lookup check
        for(int indexInAcceptableInputs=0;indexInAcceptableInputs<acceptableInputs.length;indexInAcceptableInputs++){
            if(stringToCheck.equals(acceptableInputs[indexInAcceptableInputs])){
                return true;
            }
        }
        return false;
    }
    /**
     * Checks to see if the length of the string is equal to the desired length. 
     * @param correctLength The desired length of the string
     * @param stringToCheck the string to have its length checked
     * @return True is returned if the length of the string equals correctLength, else false.
     */
    public static boolean isCorrectLength(int correctLength,String stringToCheck){
        return (stringToCheck.length()==correctLength);
    }
    /**
     * Checks whether an integer is greater than or equal to another integer
     * @param numberThatIntegerShouldBeGreaterThanOrEqual The value that numberToCheck should be greater than or equal to in order to return true
     * @param numberToCheck The number that is being checked to see if it is greater than or equal to numberThatIntegerShouldBeGreaterThanOrEqual
     * @return True is returned if numberToCheck is greater than or equal to numberThatIntegerShouldBeGreaterThanOrEqual, otherwise false is returned
     */
    public static boolean isgreaterThanOrEqual(int numberThatIntegerShouldBeGreaterThanOrEqual,int numberToCheck){
        return(numberToCheck>=numberThatIntegerShouldBeGreaterThanOrEqual);
    }
    /**
     * Checks the string to see if it contains any of strings that are not allowed. For example, if commas are used to separate items, commas may be banned.
     * @param itemToCheck The string to check
     * @return true is returned if it contains any illegal characters / strings. Otherwise, false is returned.
     */
    public static boolean containsIllegalStrings(String itemToCheck){
        for(int i=0;i<ILLEGAL_CHAR_SEQUENCES.length;i++){
            if(itemToCheck.contains(ILLEGAL_CHAR_SEQUENCES[i])){
                return true;
            }
        }
        return false;
    }
    /**
     * Returns all illegal character sequences as a string
     * @return The illegal characters sequence as a presentable string
     */
    public static String getListOfIllegalStrings(){
        String ill="";
        for(int i=0;i<ILLEGAL_CHAR_SEQUENCES.length;i++){
            ill=ill+ILLEGAL_CHAR_SEQUENCES[i];
        }
        return ill;
    }
    /**
     * Checks to see whether the string only contains characters from the English alphabet.
     * @param stringToCheck The string to be checked
     * @return True is returned if the string consists only of letters in the English alphabet. Otherwise, false is returned.
     */
    public static boolean consistsOnlyOfLetters(String stringToCheck){
        return stringToCheck.matches("[a-zA-Z]+");
    }
    /**
     * Checks to see if the postcode is valid.
     * @param postCode The postcode as a string
     * @return True is returned if the postcode is between 6 and 8 characters, contains exactly one space and all characters other than a space character are capitalised.
     */
    public static boolean isValidPostcode(String postCode){
        if(postCode.length()>8 || postCode.length()<6){//length checks
            return false;
        }
        int noOfSpaces=0;//counts number of spaces
        for(int c=0;c<postCode.length();c++){
            if(postCode.charAt(c)==' '){
                if(noOfSpaces==1){
                    return false;
                }
                noOfSpaces++;
            }else{
                if(!Character.isUpperCase(postCode.charAt(c)) && Character.isDigit(postCode.charAt(c))==false){
                    return false;
                }    
            }
        }
        if(noOfSpaces==0){
            return false;
        }
        return true;
    }
    /**
     * Checks if the string only contains +, spaces and numbers.
     * @param phoneNumber The phone number
     * @return True is returned if the string consists only of plusses, spaces and digits. Otehrwise, false is returned.
     */
    public static boolean onlyContainsPhoneNumberCharacters(String phoneNumber){
        for(int c=0;c<phoneNumber.length();c++){
            if(phoneNumber.charAt(c)!=' '&& phoneNumber.charAt(c)!='+'&&!Character.isDigit(phoneNumber.charAt(c))){
                return false;
            }
        }
        return true;
    }
}
