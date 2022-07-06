/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package philipm.cs5.software.development.user.system;

/**
 * This class is used to search through data
 * @author mortimer
 */
public class Search {
    public static final int INDEX_NOT_FOUND=-1;
    /**
     * Performs a binary search on the one d array. Assumes it is sorted lexographically. 
     * @param DATA The data sorted lexographically
     * @param ITEM_TO_FIND the data item to be found
     * @return true is returned if the record is in the array, otherwise false is returned
     */
    public static boolean isRecordInArray(String[]DATA,String ITEM_TO_FIND){
        if(DATA.length==1){
            if(DATA[0].equals(ITEM_TO_FIND)){
                return true;
            }else{
                return false;
            }
        }
        if(DATA.length==0){
            return false;
        }
        String record="";
        int first = 0;
        int last = DATA.length-1;
        int mid;
        int comparison;
        String primaryKeyBuffer;
        do{
            mid = (first+last)/2;
            primaryKeyBuffer=DATA[mid];
            //if pk precedes itemToFind less than 0 
            comparison = primaryKeyBuffer.compareTo(ITEM_TO_FIND);
            if(comparison==0){
                return true;
            }else if(comparison>0){
                last = mid-1;
            }else{
                first = mid + 1;
            }
        }while(last>=first);
        return false;
    }
    /**
     * Performs a binary search on the 2d array inputted. It searches for the itemToFind. It assumes this is a primary key at the nought element
     * of every row. It uses binary search and the java String method .compareTo(). Hence the data must be sorted by the primary key
     * with data of a small lexographic value at the top (i.e. sorted in ascending order).
     * @param DATA The data to be searched through.
     * @param ITEM_TO_FIND The item to find
     * @param WIDTH_OF_DATA The width of a record
     * @return the field containing the item to find. If no item is found, this will be a null array of width equal to the width of a record.
     */
    public static String[]searchForRecordWithPrimaryKeyAtNoughtElement(final String[][]DATA,final String ITEM_TO_FIND,final int WIDTH_OF_DATA){
        String record[]= new String[WIDTH_OF_DATA];
        if(DATA.length==0){
            return record;
        }
        int first = 0;
        int last = DATA.length-1;
        int mid;
        int comparison;
        String primaryKeyBuffer;
        do{
            mid = (first+last)/2;
            primaryKeyBuffer=DATA[mid][0];
            //if pk precedes itemToFind less than 0 
            comparison = primaryKeyBuffer.compareTo(ITEM_TO_FIND);
            if(comparison==0){
                for(int element=0;element<record.length;element++){
                    record[element]=DATA[mid][element];
                }
                break;
            }else if(comparison>0){
                last = mid-1;
            }else{
                first = mid + 1;
            }
        }while(last>=first);
        return record;
    }
     /**
     * Performs a binary search on the 2d array inputted. It searches for the itemToFind. It assumes this is a primary key at the nought element
     * of every row. It uses binary search and assumes that the nought element of every row is an integer. It asssumes that data is sorted by integer
     * @param DATA The data to be searched through.
     * @param ITEM_TO_FIND The item to find
     * @param WIDTH_OF_DATA The width of one record
     * @return the field containing the item to find. If no item is found, this will be a null array of width equal to the width of a record.
     */
    public static String[] searchForRecordWithPrimaryKeyAtNoughtElementInt(final String[][]DATA,final int ITEM_TO_FIND, final int WIDTH_OF_DATA){
        String record[]= new String[WIDTH_OF_DATA];
        if(DATA.length==0){
            return record;
        }
        int first = 0;
        int last = DATA.length-1;
        int mid;
        int primaryKeyBuffer;
        do{
            mid = (first+last)/2;
            primaryKeyBuffer=Integer.parseInt(DATA[mid][0]);
            if(primaryKeyBuffer==ITEM_TO_FIND){
                for(int element=0;element<record.length;element++){
                    record[element]=DATA[mid][element];
                }
                break;
            }else if(primaryKeyBuffer>ITEM_TO_FIND){
                last = mid-1;
            }else{
                first = mid + 1;
            }
        }while(last>=first);
        return record;        
    } 
    /**
     * Performs a binary search on the 2d array inputted. It searches for the itemToFind. It assumes this is a primary key at the nought element
     * of every row. It uses binary search and assumes that the nought element of every row is an integer. It assumes that data is sorted by integer
     * @param DATA The data to be searched through.
     * @param ITEM_TO_FIND The item to find
     * @return The row number is returned if not, -1 is returned
     */
    public static int searchForRecordIndexInt(final String[][]DATA,final int ITEM_TO_FIND){
        if(DATA.length==0){
            return INDEX_NOT_FOUND;
        }
        int first = 0;
        int last = DATA.length-1;
        int mid;
        int primaryKeyBuffer;
        do{
            mid = (first+last)/2;
            primaryKeyBuffer=Integer.parseInt(DATA[mid][0]);
            if(primaryKeyBuffer==ITEM_TO_FIND){
                return mid;
            }else if(primaryKeyBuffer>ITEM_TO_FIND){
                last = mid-1;
            }else{
                first = mid + 1;
            }
        }while(last>=first);
        return INDEX_NOT_FOUND;           
    }
    /**
     * Performs a binary search on the 2d array inputted. It searches for the itemToFind. It assumes this is a primary key at the nought element
     * of every row. It uses binary search and assumes that the data is lexographically sorted.
     * @param DATA The data to be searched through.
     * @param ITEM_TO_FIND The item to find
     * @return The row number is returned if not, -1 is returned
     */
    public static int searchForRecordIndex(final String[][]DATA,final String ITEM_TO_FIND){
        if(DATA.length==0){
            return INDEX_NOT_FOUND;
        }
        int first = 0;
        int last = DATA.length-1;
        int mid;
        int comparison;
        String primaryKeyBuffer;
        do{
            mid = (first+last)/2;
            primaryKeyBuffer=DATA[mid][0];
            //if pk precedes itemToFind less than 0 
            comparison = primaryKeyBuffer.compareTo(ITEM_TO_FIND);
            if(comparison==0){
                return mid;
            }else if(comparison>0){
                last = mid-1;
            }else{
                first = mid + 1;
            }
        }while(last>=first);
        return INDEX_NOT_FOUND;       
    }
}
