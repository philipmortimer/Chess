/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package philipm.cs5.software.development.user.system;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import javax.imageio.ImageIO;



/**
 * This is the main class used to create the log in form.
 * @author mortimer
 */
public class PhilipMCS5SoftwareDevelopmentUserSystem {

    /**
     * Creates and sets the log in form as visible
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LogIn log = new LogIn();//sets log in form visible
        log.setVisible(true);
    }
    /**
     * This method is used to preprocess images of the chess pieces and convert and save them to a format that can be used.
     */
    private static void getPiecesWrite(){
        String [] IMAGES_NAME = {"White King","White Queen","White Bishop","White Knight","White Rook","White Pawn","Black King","Black Queen","Black Bishop","Black Knight","Black Rook","Black Pawn"};//stores the file names of the images of the pieces
        try{
            BufferedImage img=ImageIO.read(new File("Assets"+File.separator+"allPieces(2).png"));//gets the image of all the pieces
            int j=0;
            System.out.println(img.getWidth()+" "+img.getHeight());//prints the width and height of the image
            //loops through the big image and gets all the sub images which represent indivudal pieces
            BufferedImage subImage;int k=0;
            for(int y=0;y<640;y=y+320){
                for(int i=0;i<1920;i=i+320){
                    subImage=img.getSubimage(i, y, 320, 320);
                    ImageIO.write(subImage, "png", new File("Assets"+File.separator+IMAGES_NAME[k++]+".png"));//writes the image of the chess piece to the file
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }
    /**
     * Processes the opening book used so that it no longer contains duplicate openings and irrelevant data.
     */
    private static void processOpeningBook(){
        try{
            //removes any move numbers from the fen strings if appropriate
            FileReader read = new FileReader("openingMoves.txt");
            BufferedReader buffRead = new BufferedReader(read);
            String data[]=buffRead.lines().toArray(String[]::new);
            buffRead.close();read.close();
            //ensures that the full move number is not included in the FEN as it is irrelevant
            if(data[0].split(",")[0].split(" ").length==65){
                String fen1[];String fen2[];String line[];
                for(int i=0;i<data.length;i++){
                    line= data[i].split(",");
                    fen1=line[0].split(" ");
                    fen2=line[1].split(" ");
                    data[i]=fen1[0]+" "+fen1[1]+" "+fen1[2]+" "+fen1[3]+" "+fen1[4]+","+fen2[0]+" "+fen2[1]+" "+fen2[2]+" "+fen2[3]+" "+fen2[4];
                }
            }
            //removes duplicates from file
            LinkedHashSet<String> linkedHashSet = new LinkedHashSet<>( Arrays.asList(data) );
            System.out.println("Length before removing duplicates "+ data.length);
            data = linkedHashSet.toArray(new String[]{});
            System.out.println("Length after removing duplicates "+ data.length);
            String newData[][]=new String[data.length][1];
            for(int i=0;i<data.length;i++){
                newData[i][0]=data[i];
            }
            Sort.mergeSort(newData);//sorts data
            //writes array back to text file
            FileWriter write = new FileWriter("openingMoves.txt",false);
            BufferedWriter buffWrite =new BufferedWriter(write);
            buffWrite.write(newData[0][0]);
            for(int i=1;i<newData.length;i++){
                buffWrite.newLine();
                buffWrite.write(newData[i][0]);
            }buffWrite.flush();write.flush();
            buffWrite.close();write.close();//flushes and closes writers
        }catch(IOException e){
            System.out.println("Error: "+e);
        }        
    }
}
