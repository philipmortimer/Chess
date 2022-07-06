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
import java.util.Random;

/**
 * This static class is used to interact with the fixture and results stored.
 * @author mortimer
 */
public class Fixture {
    //stores various constants used thorughout the fixture code- such as values that represent a win or draw
    public static final String FILE_NAME="fixtures.txt";
    public static final String SPLIT=UserInfo.SPLIT_ITEM;
    public static final String GAME_NOT_YET_PLAYED="-1";
    private static final String UNABLE_TO_CREATE_GAME="cantCreateGame";
    public static final String BYE_GAME_STATE="-2";//indicates that the game is a bye. Does not necesarily mean that game is scheduled against a bye opponent
    public static final String BY_NAME="@";
    private static final String NO_PLAYERS_TO_ADD="aasifoakofako325";
    public static final String TOO_FEW_PLAYERS_ARRAY="Unable to load array too few players";
    public static final String GAME_WHITE_WIN="whiteW";
    public static final String GAME_BLACK_WIN="blackW";
    public static final String GAME_DRAW="draww";
    /**
     * Creates a new set of fixtures from scratch with the status of not having been played
     * @param players The name of all players. They must all be unique.
     * @throws Exception An exception if the fixtures can't be generated due to player size
     */
    public static void createdNewFixturesFromStart(String[]players) throws Exception{
        if(players.length==0 || players.length==1){
            //stores the fact that there are too few players to play a game
            try{
                FileWriter w = new FileWriter(FILE_NAME,false);
                BufferedWriter buffW = new BufferedWriter(w);
                buffW.write(UNABLE_TO_CREATE_GAME);
                buffW.flush();w.flush();
                buffW.close();w.close();
                throw new Exception("Unable to create game as too few players are present");
            }catch(IOException e){
                System.out.println(e);
            }
        }
        if(players.length % 2 == 1){
            String newNames[]=new String[players.length+1];//creates a new array and adds a bye to the list (as only an even number of players can exist in a head-to-head league.
            //copies old array to new one
            for(int i=0;i<players.length;i++){
                newNames[i]=players[i];
            }
            newNames[players.length]=BY_NAME;
            players=newNames;
        }
        //randomly swaps names in list
        String buffer;int index;
        Random rnd = new Random();
        for(int i=0;i<players.length;i++){
            index=rnd.nextInt(players.length);
            buffer = players[index];
            players[index]=players[i];
            players[i]=buffer;
        }
        String[][][]fixtures=new String[2*(players.length  -1)][3][players.length/2];//stores all fixtures. 
        //the 3d array is indexed fixtures[z][y][x]. z represents the round number, y represents some attribute in the round.
        //For example, y = 0 represents all white players. y=1 represents black players. y=2 represents the game state.
        //x represents the match element in the round. E.g. fixtures[0][0][0] return the player playing as white in the nought round and in the nought fixture
        //sets all games to have not yet been played
        for(int round=0;round<fixtures.length;round++){
            for(int game=0;game<players.length/2;game++){
                fixtures[round][2][game]=GAME_NOT_YET_PLAYED;
            }
        }
        //generates fixtures for first round
        for(int y=0;y<2;y++){
            for(int x=0;x<players.length/2;x++){
                fixtures[0][y][x]=players[y*(players.length/2) +x];
            }
        }
        //loads first half of fixtures
        for(int round=1;round<fixtures.length/2;round++){//fills in the rest of the round
            //pushes all of top to the right apart from first element, which is fixed
            fixtures[round][0][0]=fixtures[0][0][0];
            for(int x=1;x<players.length/2 -1;x++){
                fixtures[round][0][x+1]=fixtures[round-1][0][x];
            }
            //adds in buffer at appropriate spot
            fixtures[round][0][1]=fixtures[round-1][1][0];
            //pushes all of bottom to the left
            for(int x=0;x<players.length/2 -1;x++){
                fixtures[round][1][x]=fixtures[round-1][1][x+1];
            }
            fixtures[round][1][players.length/2 -1]=fixtures[round-1][0][players.length/2 -1];
        }
        //second half of fixtures is the same as the first half just swapping them around
        for(int round=fixtures.length/2;round<fixtures.length;round++){
            for(int x=0;x<players.length/2;x++){
                fixtures[round][0][x]=fixtures[round-(fixtures.length/2)][1][x];
                fixtures[round][1][x]=fixtures[round-(fixtures.length/2)][0][x];
            }
        }
        //shuffles the games in each round randomly and then shuffles the rounds randomly
        String[][]bufferArr;
        for(int round=0;round<fixtures.length;round++){
            //shuffles games within round randomly
            for(int game=0;game<players.length/2;game++){
                index=rnd.nextInt(players.length/2);
                buffer=fixtures[round][0][game];
                fixtures[round][0][game]=fixtures[round][0][index];
                fixtures[round][0][index]=buffer;
                buffer=fixtures[round][1][game];
                fixtures[round][1][game]=fixtures[round][1][index];
                fixtures[round][1][index]=buffer;
            }
            //shuffles the rounds randomly
            index=rnd.nextInt(fixtures.length);
            bufferArr=fixtures[index];
            fixtures[index]=fixtures[round];
            fixtures[round]=bufferArr;
        }
        for(int round=0;round<fixtures.length;round++){
            for(int game=0;game<players.length/2;game++){
                if(fixtures[round][0][game].equals(BY_NAME) || fixtures[round][1][game].equals(BY_NAME)){//stores games which are byes
                    fixtures[round][2][game]=BYE_GAME_STATE;
                }
            }
            
        }
        //writes generated fixtures to a file, overwriting anything that was previously there, in a form that the computer can intepret
        writeToFile(fixtures, new String[0]);
    }
    /**
     * Writes the fixtures list to the file.
     * @param fixtures The fixtures array to write
     * @param nameOfPlayersAddedLate The players added halfway through the season
     * @throws IOException An IOException may occur when writing to file
     */
    public static void writeToFile(String fixtures[][][], String[] nameOfPlayersAddedLate) throws IOException{
            int playerLength=fixtures[0][0].length*2;
            FileWriter write = new FileWriter(FILE_NAME,false);
            BufferedWriter buffWrite = new BufferedWriter(write);
            buffWrite.write(String.valueOf(playerLength));buffWrite.newLine();
            //writes fixtures for white
            for(int round=0;round<fixtures.length;round++){
                for(int game=0;game<playerLength/2;game++){
                    if(round==0 && game==0){
                        buffWrite.write(fixtures[round][0][game]);
                    }else{
                        buffWrite.write(SPLIT+fixtures[round][0][game]);
                    }
                }
            }
            buffWrite.newLine();
            //writes fixtures for black
            for(int round=0;round<fixtures.length;round++){
                for(int game=0;game<playerLength/2;game++){
                    if(round==0 && game==0){
                        buffWrite.write(fixtures[round][1][game]);
                    }else{
                        buffWrite.write(SPLIT+fixtures[round][1][game]);
                    }
                }
            } 
            buffWrite.newLine();
            //writes game states to file
            for(int round=0;round<fixtures.length;round++){
                for(int game=0;game<playerLength/2;game++){
                     if(round==0 && game==0){
                        buffWrite.write(fixtures[round][2][game]);
                    }else{
                        buffWrite.write(SPLIT+fixtures[round][2][game]);
                    }
                }
            } 
            //writes queue of players to add to file
            buffWrite.newLine();
            if(nameOfPlayersAddedLate.length==0){
                buffWrite.write(NO_PLAYERS_TO_ADD);
            }else{
                String lineWrite=nameOfPlayersAddedLate[0];
                for(String name: nameOfPlayersAddedLate){
                    lineWrite = lineWrite+SPLIT+name;
                }
                buffWrite.write(lineWrite);
            }
            buffWrite.flush();write.flush();
            buffWrite.close();write.close();     
    }
    /**
     * Gets an array containing the people who wish to play in the league
     * @return The array
     * @throws IOException An exception when reading from the text file
     */
    public static String[]getListOfPeopleToAddedDuringSeason()throws IOException{
        FileReader read = new FileReader(FILE_NAME);
        BufferedReader buffRead = new BufferedReader(read);
        buffRead.readLine();buffRead.readLine();buffRead.readLine();buffRead.readLine();
        String line = buffRead.readLine();
        buffRead.close();read.close();
        if(NO_PLAYERS_TO_ADD.equals(line)){
            return new String[0];
        }
        return line.split(SPLIT);
    }
    /**
     * Loads the fixtures array from the text file.
     * @return The array of fixtures
     * @throws java.lang.Exception An exception if the array can't be loaded due to the number of players being too small.
     */
    public static String[][][]loadFixturesArray() throws Exception{
        try{
            FileReader read = new FileReader(FILE_NAME);
            BufferedReader buffRead = new BufferedReader(read);
            String lin=buffRead.readLine();
            if(lin.equals(UNABLE_TO_CREATE_GAME)){//if there are too few players and hence no fixture has been created, an exception is thrown
                buffRead.close();read.close();
                throw new Exception(TOO_FEW_PLAYERS_ARRAY);
            }
            int lengthOfFile = Integer.parseInt(lin);
            String[][][]fixtures = new String[2*(lengthOfFile -1)][3][lengthOfFile/2];//stores the fixtures / results
            String line[]=buffRead.readLine().split(SPLIT);int lineIndex=0;//stores the white players
            for(int round=0;round<fixtures.length;round++){//loops through file updating all white players
                for(int game=0;game<lengthOfFile/2;game++){
                    fixtures[round][0][game]=line[lineIndex++];
                }
            }
            line=buffRead.readLine().split(SPLIT);lineIndex=0;//stores all black players
            for(int round=0;round<fixtures.length;round++){//loops through array to store all black players
                for(int game=0;game<lengthOfFile/2;game++){
                    fixtures[round][1][game]=line[lineIndex++];
                }
            }
            line=buffRead.readLine().split(SPLIT);lineIndex=0;//stores the game states of each game
            for(int round=0;round<fixtures.length;round++){
                for(int game=0;game<lengthOfFile/2;game++){
                    fixtures[round][2][game]=line[lineIndex++];
                }
            }
            buffRead.close();read.close();
            return fixtures;
        }catch(IOException e){
            System.out.println("Error "+e);
            throw new Exception("Error "+e);
        }
    }
    /**
     * Adds a player who is not part of the fixtures already to the league. If no games have been played, the fixtures will be generated from scratch. Otherwise, they cannot actually play until a new season begins.
     * @param playerName The player name
     * @param allClubMembers An array of all club member names (INCLUDING PLAYER TO BE ADDED)
     * @throws Exception An exception may occur when reading and writing to text files
     */
    public static void addPlayerToFixtures(String playerName, String[] allClubMembers) throws Exception{
        /*
        If no games have been played yet, then just regernate fixtures from scratch. If the data is stored as too few players
        then generate fixtures from scratch also.
        
        Else, player is added to list of players who cannot play until next season.
        */
        try{
            String[][][]fixtures = loadFixturesArray();
            //checks to see if any games have been played
            boolean hasMatchBeenPlayed=false;
            for(int round=0;round<fixtures.length;round++){
                for(int match=0;match<fixtures[0][0].length;match++){
                    if((fixtures[round][2][match].equals(BYE_GAME_STATE) || fixtures[round][2][match].equals(GAME_NOT_YET_PLAYED))==false){
                        hasMatchBeenPlayed=true;
                        break;
                    }
                }
                if(hasMatchBeenPlayed){
                    break;
                }
            }
            if(hasMatchBeenPlayed){
                //adds player to a list of people added halfway through season
                String[]playersAdded=getListOfPeopleToAddedDuringSeason();
                String playersNew[]=new String[playersAdded.length+1];
                for(int i=0;i<playersAdded.length;i++){
                    playersNew[i]=playersAdded[i];
                }
                playersNew[playersAdded.length]=playerName;
                writeToFile(fixtures, playersNew);
                return;
            }else{
                //generates the fixtures from scratch as no games have been played
                createdNewFixturesFromStart(allClubMembers);
                return;
            }
        }catch(Exception e){
            //catches exceptions. An exception may be thrown due to file handling. If this is the case, this is a genuine error and needs to be thrown.
            //The error could simply be that fixtures cannot be loaded as there are too few players to generate any fixtures. In this case, fixtures are generated again.
            if(TOO_FEW_PLAYERS_ARRAY.equals(e.getMessage())){
                createdNewFixturesFromStart(allClubMembers);
                return;
            }else{
                throw e;
            }
        }
        
    }
    /**
     * Checks to see if the player is part of the league (or waiting to be) and removes them if they are. If they appear in the fixtures, those games will be marked as bye games.
     * @param playerName The name of the player
     * @throws Exception An exception may occur when reading to and from a text file
     */
    public static void removePlayerFromFixtures(String playerName)throws Exception{
        try{
            String fixtures[][][]=loadFixturesArray();
            //checks to see if player is in fixture. If they are, all their matches are set to bye games (even the ones already played)
            boolean playerFound=false;
            for(int round=0;round<fixtures.length;round++){
                for(int match=0;match<fixtures[0][0].length;match++){
                    if(fixtures[round][0][match].equals(playerName)){
                        fixtures[round][0][match]=BY_NAME;
                        fixtures[round][2][match]=BYE_GAME_STATE;
                        playerFound=true;                        
                    }else if(fixtures[round][1][match].equals(playerName)){
                        fixtures[round][1][match]=BY_NAME;
                        fixtures[round][2][match]=BYE_GAME_STATE;
                        playerFound=true;
                    }
                }   
            }
            if(playerFound){
                //writes the new fixtures to file
                writeToFile(fixtures, getListOfPeopleToAddedDuringSeason());
                return;
            }else{
                //checks to see if player was added mid way through season.
                int indexOfPlayer=-1;
                String[]people=getListOfPeopleToAddedDuringSeason();
                for(int i=0;i<people.length;i++){
                    if(people[i].equals(playerName)){
                        indexOfPlayer=i;
                        break;
                    }
                }
                if(indexOfPlayer==-1){
                    return ;//no action is needed
                }else{
                    //removes player from list of people waiting
                    String peopleNew[]=new String[people.length-1];
                    for(int i=0;i<indexOfPlayer;i++){
                        peopleNew[i]=people[i];
                    }
                    for(int i=indexOfPlayer+1;i<people.length;i++){
                        peopleNew[i-1]=people[i];
                    }
                    //writes updated file
                    writeToFile(fixtures, peopleNew);
                    return;
                }
            }
        }catch(Exception e){
            //an exception may occur if there are too few players to create fixtures. In this case, no action is needed.
            if(TOO_FEW_PLAYERS_ARRAY.equals(e.getMessage())){
                return;
            }else{
                throw e;
            }
        }
                
    } 
}
