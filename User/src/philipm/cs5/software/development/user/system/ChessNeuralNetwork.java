/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package philipm.cs5.software.development.user.system;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * This is a neural network trained by me to play chess.
 * @author mortimer
 */
public class ChessNeuralNetwork {
    private static final String BEST_NET_FILE_NAME="bestNet.txt";//stores the location of the text file that stores the best network architecture
    private final double[] ALL_WEIGHTS_AND_BIASES;//stores all weights and biases of the neural network
    private final int[]NETWORK_STRUCTURE;//stores the network structure
    private final int NET_LENGTH;//stores the length of the net architecture
    private final double[] NET_ACTIVATIONS;//stores the network activation values
    /**
     * Loads the neural network stored in the "bestNet.txt" file.
     * @param NETWORK_STRUCTURE The network structure of the neural network
     * @throws java.io.IOException An IOException may be thrown when reading the values of the network's weights and biases from "bestNet.txt"
     */
    public ChessNeuralNetwork(final int[] NETWORK_STRUCTURE)throws IOException{
        this.NETWORK_STRUCTURE= NETWORK_STRUCTURE;
        NET_LENGTH=NETWORK_STRUCTURE.length;
        int totNeurons=NETWORK_STRUCTURE[0]+NETWORK_STRUCTURE[1];
        int noOfWeightsAndBiases=NETWORK_STRUCTURE[0]*NETWORK_STRUCTURE[1]+NETWORK_STRUCTURE[1];//stores the number of weights and biases in the network
        for(int i=2;i<NETWORK_STRUCTURE.length;i++){//calculates the total number of weights and biases
            noOfWeightsAndBiases=noOfWeightsAndBiases+NETWORK_STRUCTURE[i]*NETWORK_STRUCTURE[i-1]+NETWORK_STRUCTURE[i];
            totNeurons=totNeurons+NETWORK_STRUCTURE[i];
        }
        if(NETWORK_STRUCTURE.length>2){
            totNeurons=totNeurons-NETWORK_STRUCTURE[NET_LENGTH-1];
        }
        NET_ACTIVATIONS=new double[totNeurons];
        ALL_WEIGHTS_AND_BIASES=new double[noOfWeightsAndBiases];//creates array storing all weights and biases
        //reads network architecture from text file
        FileReader read = new FileReader(BEST_NET_FILE_NAME);
        int i=0;//stores the index of all weights and biases to be accessed
        BufferedReader buffRead = new BufferedReader(read);
        String lineRead;//stores the line read
        while((lineRead=buffRead.readLine())!=null){//loads values of neural network
            ALL_WEIGHTS_AND_BIASES[i]=Double.parseDouble(lineRead);
            i++;
        }
        buffRead.close();read.close();
    }
    /**
     * Feeds data through the network and returns the network's output
     * @param input The input
     * @return The output
     */
    public double[] feedThroughNet(int[]input){
        int currentIndexForAll=0;//stores the current index for all weights and biases
        int activsIndex=0;
        double out[]=new double[NETWORK_STRUCTURE[NET_LENGTH-1]];
        int startOfPrevLayer=0;
        for(;activsIndex<input.length;activsIndex++){
           NET_ACTIVATIONS[activsIndex]=input[activsIndex];
        }
        double sum;//stores the sum that amounts to the input to the node
        for(int layerIndex=1;layerIndex<NET_LENGTH-1;layerIndex++){
            for(int neuronInLayer=0;neuronInLayer<NETWORK_STRUCTURE[layerIndex];neuronInLayer++){//loops through every neuron in current layer
                sum=ALL_WEIGHTS_AND_BIASES[currentIndexForAll];//sets the sum equal to the bias
                currentIndexForAll++;
                for(int neuronInPrevLayer=0;neuronInPrevLayer<NETWORK_STRUCTURE[layerIndex-1];neuronInPrevLayer++){//sums weights in previous layer to calculate input value to node
                    sum=sum+NET_ACTIVATIONS[startOfPrevLayer+neuronInPrevLayer]*ALL_WEIGHTS_AND_BIASES[currentIndexForAll];
                    currentIndexForAll++;
                }
                NET_ACTIVATIONS[activsIndex]=reLu(sum);
                activsIndex++;
            }
            startOfPrevLayer=startOfPrevLayer+NETWORK_STRUCTURE[layerIndex-1];
        }
        //calculates output
        for(int neuronInLayer=0;neuronInLayer<NETWORK_STRUCTURE[NET_LENGTH-1];neuronInLayer++){//loops through every neuron in output layer
            sum=ALL_WEIGHTS_AND_BIASES[currentIndexForAll];//sets the sum equal to the bias
            currentIndexForAll++;
            for(int neuronInPrevLayer=0;neuronInPrevLayer<NETWORK_STRUCTURE[NET_LENGTH-2];neuronInPrevLayer++){//sums weights in previous layer to calculate input value to node
                sum=sum+NET_ACTIVATIONS[startOfPrevLayer+neuronInPrevLayer]*ALL_WEIGHTS_AND_BIASES[currentIndexForAll];
                currentIndexForAll++;
            }
            out[neuronInLayer]=sum;
        }
        softmax(out);
        return out;   
    }
    /**
     * Executes the reLu function
     * @param x The input
     * @return The output
     */
    private static double reLu(double x){
        if(x>0.0){
            return x;
        }else{
            return 0.0;
        }
    }
    /**
     * Performs the softmax function on the inputted array
     * @param input The array to perform the softmax function on
     */
    private static void softmax(double[]input){
        input[0]=Math.exp(input[0]);
        double sum=input[0];//stores the sum of the exponentials
        for(int i=1;i<input.length;i++){
            input[i]=Math.exp(input[i]);
            sum=sum+input[i];
        }
        for(int i=0;i<input.length;i++){
            input[i]=input[i]/sum;
        }
    }
}
