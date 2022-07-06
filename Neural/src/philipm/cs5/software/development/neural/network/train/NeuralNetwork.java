/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package philipm.cs5.software.development.neural.network.train;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * This class is used to train my chess neural network
 * @author mortimer
 */
public class NeuralNetwork {
    private final int NET_STRUCTURE_LENGTH;//stores the number of weights and biases in the neural network
    private final int NET_STRUCTURE_LENGTH_MINUS_ONE;
    private final int ALL_LENGTH;//stores the length of all weights and biases array
    private final int[]TOT_NEURON;
    private final int[]CURRENT_ALL;
    private final Random RAND = new Random();//creates a random object to be used in network
    private final int[]INDEX_FOR_ALL_WEIGHTS_ERROR;//stores the indexes to be used in backpropogation to optimise the process
    private final int[]INDEX_FOR_ACTIVS_ERROR;//stores the indexes to be used when calculating the error of the network
    private final int ACTIV_IN;
    private final int[]NET_STRUCTURE;//stores the network structure
    private final double[]ALL_WEIGHTS_AND_BIASES;// stores all weights and biases in the network
    private final int TOTAL_NEURONS;
    private final int TOTAL_NEURONS_MINUS_LAST_LAYER;//stores the number of nodes in the network that are not in the output layer
    private double[] nodeValuePreActivation;//stores the input to nodes in a given layer before the activation function is applied
    private double []nodeValuePostActivation;//stores the post activation values of a nodes in a layer in the network
    private static final String FILE_NAME = "networkInfo.txt";//stores file name where network data is stored
    private double timeStep; //stores the time step
    private double previousMHat[];//stores previous values of MHat - used to allow training using ADAM optimiser over multiple training method calls
    private double previousVHat[];//stores previous values of VHat - used to allow training using ADAM optimiser over multiple training method calls
    private static final String BEST_NET_FILE_NAME="bestNet.txt";//stores the name of the text file that is used to store the best neural network
    private final boolean []NEURONS_DROPPED;//stores which neurons are dropped
    private final int LENGTH_OF_NEURONS_DROPPED_ARRAY;
    private final int INDEX_CACHE[];
    /**
     * Creates a neural network of the desired architecture using reLu in all layer bar the output layer, where the softmax function is used
     * @param netStructure The network structure, with element 0 specifying the number of nodes in the input layer and the final element specifying the number of nodes in the output layer
     */
    public  NeuralNetwork(int[]netStructure){
        //initilaises various constants used to speed up the backpropogation process
        this.NET_STRUCTURE = netStructure;
        this.NET_STRUCTURE_LENGTH=this.NET_STRUCTURE.length;
        this.NET_STRUCTURE_LENGTH_MINUS_ONE=this.NET_STRUCTURE_LENGTH-1;
        int totalWeightsAndBiases=0;
        int totalNeurons=0;
        INDEX_CACHE=new int[netStructure.length-1];
        //calculates the total number of neurons as well as the total number of weights and biases in the network
        for(int i =0;i<netStructure.length;i++){
            if(i!=netStructure.length-1 && i!=0){
                INDEX_CACHE[i]=totalNeurons-netStructure[0];
            }
            totalNeurons=totalNeurons+this.NET_STRUCTURE[i];
            if(i!=0){
                totalWeightsAndBiases = totalWeightsAndBiases+this.NET_STRUCTURE[i]+(this.NET_STRUCTURE[i]*this.NET_STRUCTURE[i-1]);
            }
        }
        NEURONS_DROPPED=new boolean[totalNeurons-netStructure[0]-netStructure[netStructure.length-1]];
        LENGTH_OF_NEURONS_DROPPED_ARRAY=NEURONS_DROPPED.length;
        //initilaises more constants used for optimisation purposes
        this.TOTAL_NEURONS = totalNeurons;
        this.TOTAL_NEURONS_MINUS_LAST_LAYER=this.TOTAL_NEURONS-this.NET_STRUCTURE[this.NET_STRUCTURE_LENGTH_MINUS_ONE];
        this.ALL_WEIGHTS_AND_BIASES=new double[totalWeightsAndBiases];
        this.ALL_LENGTH=totalWeightsAndBiases;
        this.ACTIV_IN=this.TOTAL_NEURONS-this.NET_STRUCTURE[this.NET_STRUCTURE.length-1];
        int currentIndex=0;
        //initialises the value of weights and biases in the network to values that make learning optimal
        for(int layerIndex=0;layerIndex<this.NET_STRUCTURE.length;layerIndex++){
            if(layerIndex!=0){
                for(int currentLayerIndex=0;currentLayerIndex<this.NET_STRUCTURE[layerIndex];currentLayerIndex++){
                    this.ALL_WEIGHTS_AND_BIASES[currentIndex]=0.0;//all biases are initiliased to 0
                    currentIndex++;
                    for(int prevLayerIndex=0;prevLayerIndex<this.NET_STRUCTURE[layerIndex-1];prevLayerIndex++){//weigths leading into bias
                        this.ALL_WEIGHTS_AND_BIASES[currentIndex]=getRandDoubleBetweenOneAndMinusOne(this.NET_STRUCTURE[layerIndex-1],this.NET_STRUCTURE[layerIndex]);//use xavier initialisation to intialise the network
                        currentIndex++;
                    }
                }
            }
        }
        //the backpropogation process is perfromed on made up data to allow for fast backpropogation during the training stage of the network
        this.feedThroughNet(new double[this.NET_STRUCTURE[0]]);//feeds a random input into the network
        double[]desiredOutput=new double[this.NET_STRUCTURE[this.NET_STRUCTURE_LENGTH_MINUS_ONE]];//creates a random desired output
        double[]errorInLayer= new double[1];//stores the network error
        double[]errorInLayerAbove=new double[1];//stores error in layer above
        double sumOfErrorAndWeights;
        double grad[]=new double[this.ALL_WEIGHTS_AND_BIASES.length];
        int indexForAllWhenDoingWeightsAndError;
        int indexWeightErrorChange;
        int indexForActivsError;
        int totalAllExplored;
        int indexForAllGrad;
        int totNeuronsExplored;
        int indexForActivsGrad;
        int activIndex;
        //this process gets the indexs accessed during training to avoid repeating the same calculation over and over again
        int[] arrayIndexForAllWhenDoingWeightsAndError=new int[this.NET_STRUCTURE.length];//these arrays are used to speed of the backpropogation process
        int[]arrayForIndexForActivsError= new int[this.NET_STRUCTURE.length];
        int[]arrayForIndexForAllGrad=new int[this.NET_STRUCTURE.length];
        int []arrayForTotNeuronsExplored=new int[this.NET_STRUCTURE.length];
        for(int layerIndex=this.NET_STRUCTURE.length-1;layerIndex>0;layerIndex--){
            errorInLayer=new double[this.NET_STRUCTURE[layerIndex]];
            if(layerIndex==this.NET_STRUCTURE.length-1){
                activIndex=this.TOTAL_NEURONS-this.NET_STRUCTURE[this.NET_STRUCTURE.length-1];
                for(int neuronInLayer=0;neuronInLayer<this.NET_STRUCTURE[layerIndex];neuronInLayer++){
                    errorInLayer[neuronInLayer]=(this.nodeValuePostActivation[activIndex]-desiredOutput[neuronInLayer])*activationDerivative(this.nodeValuePreActivation[activIndex]);
                    activIndex++;
                }
            }else{
                indexForAllWhenDoingWeightsAndError=0;
                for(int layer=1;layer<layerIndex+1;layer++){//calcs first index in all of next layer
                    indexForAllWhenDoingWeightsAndError=indexForAllWhenDoingWeightsAndError+(this.NET_STRUCTURE[layer]*this.NET_STRUCTURE[layer-1]+this.NET_STRUCTURE[layer]);
                }
                arrayIndexForAllWhenDoingWeightsAndError[layerIndex]=indexForAllWhenDoingWeightsAndError;
                indexForActivsError=0;
                for(int layer=0;layer<layerIndex;layer++){//frist index of activ in current layer
                    indexForActivsError=indexForActivsError+this.NET_STRUCTURE[layer];
                }
                arrayForIndexForActivsError[layerIndex]=indexForActivsError;
                for(int neuronInLayer = 0;neuronInLayer<this.NET_STRUCTURE[layerIndex];neuronInLayer++){
                    indexWeightErrorChange=indexForAllWhenDoingWeightsAndError+1+neuronInLayer;//first index of weight
                    sumOfErrorAndWeights=0;
                    for(int neuronInLayerAfter=0;neuronInLayerAfter<this.NET_STRUCTURE[layerIndex+1];neuronInLayerAfter++){
                        sumOfErrorAndWeights=sumOfErrorAndWeights+(errorInLayerAbove[neuronInLayerAfter]*this.ALL_WEIGHTS_AND_BIASES[indexWeightErrorChange]);
                        indexWeightErrorChange=indexWeightErrorChange+1+this.NET_STRUCTURE[layerIndex];
                    }
                    errorInLayer[neuronInLayer]=sumOfErrorAndWeights*activationDerivative(this.nodeValuePreActivation[indexForActivsError]);
                    indexForActivsError++;
                }
            }
            //errorInLayer has been generated
            totalAllExplored=0;
            for(int layer = this.NET_STRUCTURE.length-1;layer>layerIndex-1;layer=layer-1){
                totalAllExplored=totalAllExplored + (this.NET_STRUCTURE[layer]*this.NET_STRUCTURE[layer-1]+this.NET_STRUCTURE[layer]);
            }
            indexForAllGrad=this.ALL_WEIGHTS_AND_BIASES.length-totalAllExplored;//first all of biases in this layer
            arrayForIndexForAllGrad[layerIndex]=indexForAllGrad;
            totNeuronsExplored=0;
            for(int layer=0;layer<layerIndex-1;layer++){//first index of activs in layer before
                totNeuronsExplored=totNeuronsExplored+this.NET_STRUCTURE[layer];
            }        
            arrayForTotNeuronsExplored[layerIndex]=totNeuronsExplored;
            for(int neuronInLayer=0;neuronInLayer<this.NET_STRUCTURE[layerIndex];neuronInLayer++){
                grad[indexForAllGrad]=errorInLayer[neuronInLayer];//sets grad for bias;
                indexForAllGrad++;
                indexForActivsGrad=totNeuronsExplored;
                for(int neuronInLayerBefore=0;neuronInLayerBefore<this.NET_STRUCTURE[layerIndex-1];neuronInLayerBefore++){
                    grad[indexForAllGrad]=errorInLayer[neuronInLayer]*(this.nodeValuePostActivation[indexForActivsGrad]);
                    indexForActivsGrad++;
                    indexForAllGrad++;
                }
            }
            errorInLayerAbove=new double[errorInLayer.length];
            for(int i =0;i<errorInLayer.length;i++){
                errorInLayerAbove[i]=errorInLayer[i];
            }
        }
        //the backpropogation mock trial run is finished and now the indexes accessed are stored to avoid needless calculations when backpropogation is carried out during the training phase
        this.INDEX_FOR_ALL_WEIGHTS_ERROR=arrayIndexForAllWhenDoingWeightsAndError;
        this.INDEX_FOR_ACTIVS_ERROR=arrayForIndexForActivsError;
        this.CURRENT_ALL=arrayForIndexForAllGrad;
        this.TOT_NEURON=arrayForTotNeuronsExplored;
    }
    /**
     * Loads the best neural network with the architecture of the network that is stored in the network info text file
     * @return The weights and biases for the best network.
     */
    public double[]loadBestNetWithThisArchitecture(){
        double []bestNet=new double[this.ALL_WEIGHTS_AND_BIASES.length];
        double bestAcc=-0.5;
        int lineNoEnd;
        int lineNoStart=0;
        double currentAcc=0;
        boolean isBest=true;
        boolean isSameArch=false;
        try{
            //loads text file into memory
            FileReader read = new FileReader(this.FILE_NAME);
            BufferedReader buff = new BufferedReader(read);
            Object[]a=buff.lines().toArray();
            String []s = new String[a.length];
            for(int i=0;i<a.length;i++){
                s[i]=String.valueOf(a[i]);
            }
            //loops through evert line of the text file
            for(int i=0;i<s.length;i++){
                if(s[i].contains("Network Structure")){//checks to see if line is descirbing neural network structure 
                    isSameArch=false;
                    String str="Network Structure ";
                    for(int index=0;index<this.NET_STRUCTURE.length;index++){
                        str=str+String.valueOf(this.NET_STRUCTURE[index])+"  ";
                    }
                    if(s[i].equals(str)){//checks to see whether the architecture is the same as the current network
                        isSameArch=true;
                    }
                }
                if(s[i].contains("Best network (with accuracy")&&isSameArch==true){//checks to see if new network is appropriate
                    isBest=false;
                    String num="";
                    //gets accuracy of best network
                    for(int c=0;c<s[i].length();c++){
                        boolean isNumOrDot=true;
                        try{
                            double f=Double.parseDouble(String.valueOf((s[i].charAt(c))));
                        }catch(NumberFormatException e){
                            if(s[i].charAt(c)!='.'){
                                isNumOrDot=false;
                            }
                        }
                        if(isNumOrDot==true){
                            num =num+s[i].charAt(c);
                        }
                    }
                    currentAcc=Double.parseDouble(num);
                    if(currentAcc>bestAcc&&isSameArch==true){//updates best accuracy if appropriate
                        bestAcc=currentAcc;
                        isBest=true;
                    }
                    if(isBest==true&&isSameArch==true){//stores network as best network if appropraite
                        lineNoStart=i+1;
                        lineNoEnd=i+bestNet.length;
                        int bestIn=0;
                        for(int element=lineNoStart;element<=lineNoEnd;element++){
                            bestNet[bestIn]=Double.parseDouble(s[element]);
                            bestIn++;
                        }
                    }
                }
            }
        }catch(IOException e){
            System.out.println("errro "+e+" with file "+FILE_NAME);
            e.printStackTrace();
        }
        return bestNet;
    }
    /**
     * Loads the array stored in the best net text file, assuming it is intended for a network with the architecture of the current network.
     * @return The weights and biases array
     * @throws Exception An exception may occur if the best net file does not match the network structure.
     */
    public double[]loadBestNetFile() throws Exception{
        double[]best = new double[ALL_WEIGHTS_AND_BIASES.length];
        FileReader read = new FileReader(BEST_NET_FILE_NAME);
        BufferedReader buffRead = new BufferedReader(read);
        int index=0;
        String lineRead;
        while((lineRead=buffRead.readLine())!=null){
            best[index]=Double.parseDouble(lineRead);index++;
        }
        buffRead.close();read.close();
        return best;
    }
    /**
     * This is the hyperbolic tangent function.
     * @param x The input 
     * @return the output of tanH
     */
     private static double tanH(double x){
         return ((Math.pow(Math.E, x)-Math.pow(Math.E,-x))/(Math.pow(Math.E,x)+Math.pow(Math.E,-x)));
     }
     /**
      * This calculates the the first order derivative of tanH
      * @param x The input
      * @return The output
      */
     private static double gradTanH(double x){
         return(1.0-(Math.pow(tanH(x),2)));
     }
     /**
      * This function executes the reLu function
      * @param x The input
      * @return The output
      */
     private static double reLU(double x){
         if(x>0.0){
             return x;
         }else{
             return 0.0;
         }
     }
     /**
      * This calculates the first oder derivative of reLu. Note ((dy/dx)reLU(0)) is handled as 0 (although reLU is not differentiable at x = 0)
      * @param x The input
      * @return The output
      */
     private static double gradRELU(double x){
         if(x>0.0){
             return 1.0;
         }else{
             return 0.0;
        }
     }
     /**
      * Performs the softmax function on an input vector
      * @param input The input vector
      * @return The output vector
      */
     private static double[]softmax(double[]input){
         double sum=0.0;//calculates the sum of the e^x for each element in the array
         double[]ret=new double[input.length];//stores the vector to return
         double[]exp=new double[input.length];
         for(int i =0;i<input.length;i++){
             exp[i]=Math.exp(input[i]);
             sum =sum+exp[i];
         }
         for(int i =0;i<input.length;i++){
             ret[i]=exp[i]/sum;
         }
         return ret;
     }
     /**
      * This method calculates the gradient of each parameter with respect to the loss function. This is an optimised implementation of the backpropagation algorithm. This method assumes that the training item has already been fed through the network.
      * @param desiredOutput The desired output of the neural network
      * @return The gradient of each parameter's error with respect to the loss function.
      */
    private double[]calcGradientAttemptOptimised(double[]desiredOutput){
        double[]errorInLayer= new double[1];//stores the error in the current layer
        double[]errorInLayerAbove=new double[1];//stores the error in the layer above
        double sumOfErrorAndWeights;//stores the sum of the error multiplied by the weigths in a give layer
        double grad[]=new double[this.ALL_LENGTH];//stores the error gradient for each weight and bias in the network
        //stores various indices used during the backpropogation process
        int indexWeightErrorChange;
        int indexForActivsError;
        int indexForAllGrad;
        int changeD;
        int totNeuronsExplored;
        int indexForActivsGrad;
        int activIndex;
        for(int layerIndex=this.NET_STRUCTURE_LENGTH_MINUS_ONE;layerIndex>0;layerIndex--){//loops through the entire network
            errorInLayer=new double[this.NET_STRUCTURE[layerIndex]];//stores the error of each node in the given layer
            if(layerIndex==this.NET_STRUCTURE_LENGTH_MINUS_ONE){//for the output layer, the error of each neuron is calculated
                activIndex=this.ACTIV_IN;
                for(int neuronInLayer=0;neuronInLayer<this.NET_STRUCTURE[layerIndex];neuronInLayer++){
                    errorInLayer[neuronInLayer]= this.nodeValuePostActivation[activIndex] - desiredOutput[neuronInLayer];//this calcualtes the error of the network when a softmax activation is sued in conjunction with categorical cross entropoy loss
                    //errorInLayer[neuronInLayer]=(this.nodeValuePostActivation[activIndex]-desiredOutput[neuronInLayer])*gradTanH(this.nodeValuePreActivation[activIndex]);//this is the method used to calculate the error of the network when using the mean squared error loss
                    activIndex++;
                }
            }else{
                indexForActivsError=this.INDEX_FOR_ACTIVS_ERROR[layerIndex];
                changeD=1+this.NET_STRUCTURE[layerIndex];
                //loops through each neuron in the current layer and calculates the error of the weights and biase of the neurons
                for(int neuronInLayer = 0;neuronInLayer<this.NET_STRUCTURE[layerIndex];neuronInLayer++){
                    if(NEURONS_DROPPED[neuronInLayer+INDEX_CACHE[layerIndex]]){
                        errorInLayer[neuronInLayer]=0;
                        indexForActivsError++;
                        continue;
                    }
                    indexWeightErrorChange=this.INDEX_FOR_ALL_WEIGHTS_ERROR[layerIndex]+1+neuronInLayer;//first index of weight
                    sumOfErrorAndWeights=0;//sums the weights mulitplied by the error in the layer above
                    for(int neuronInLayerAfter=0;neuronInLayerAfter<this.NET_STRUCTURE[layerIndex+1];neuronInLayerAfter++){
                        sumOfErrorAndWeights=sumOfErrorAndWeights+(errorInLayerAbove[neuronInLayerAfter]*this.ALL_WEIGHTS_AND_BIASES[indexWeightErrorChange]);
                        indexWeightErrorChange=indexWeightErrorChange+changeD;
                    }
                    errorInLayer[neuronInLayer]=sumOfErrorAndWeights*activationDerivative(this.nodeValuePreActivation[indexForActivsError]);//stores the error of the neuron by muliplying the sum of the error and wieghts for the neuron by the pre activation node input
                    indexForActivsError++;
                }
            }
            //errorInLayer has been generated - now the error of each bias and weight needs to be determined
            indexForAllGrad=this.CURRENT_ALL[layerIndex];
            for(int neuronInLayer=0;neuronInLayer<this.NET_STRUCTURE[layerIndex];neuronInLayer++){//loops through all neurons in layer
                if(errorInLayer[neuronInLayer]==0){
                    grad[indexForAllGrad]=0;indexForAllGrad++;
                    for(int neuronInLayerBefore=0;neuronInLayerBefore<this.NET_STRUCTURE[layerIndex-1];neuronInLayerBefore++){//generates error for weights
                        grad[indexForAllGrad]=0;//calculates error for weight
                        indexForAllGrad++;
                    }
                    continue;
                }
                grad[indexForAllGrad]=errorInLayer[neuronInLayer];//sets grad for bias (which is just equal to the error of the node)
                indexForAllGrad++;
                indexForActivsGrad=this.TOT_NEURON[layerIndex];//stores the index to use for POST_ACTIVATION array access
                for(int neuronInLayerBefore=0;neuronInLayerBefore<this.NET_STRUCTURE[layerIndex-1];neuronInLayerBefore++){//generates error for weights
                    grad[indexForAllGrad]=errorInLayer[neuronInLayer]*(this.nodeValuePostActivation[indexForActivsGrad]);//calculates error for weight
                    indexForActivsGrad++;
                    indexForAllGrad++;
                }
            }
            if(layerIndex==1){//added in for optimisation
                return grad;
            }
            //sets the error in the current layer as the error in the layer above (this is used when calculating the error of the next layer)
            errorInLayerAbove=new double[errorInLayer.length];
            for(int i =0;i<errorInLayer.length;i++){
                errorInLayerAbove[i]=errorInLayer[i];
            }
        }
        return grad;//returns the gradients
    }
    /**
     * Shuffles the training data array.
     * @param array The array to shuffle
     */
     private static void shuffleTrainingDataArray(int[][]array){
        Random rnd = new Random();int in;
        int buff;
        for(int y=0;y<array.length;y++){//loops through every row in array and swaps it with another row randomly
            in = rnd.nextInt(array.length);
            for(int x=0;x<array[0].length;x++){
                buff=array[y][x];
                array[y][x]=array[in][x];
                array[in][x]=buff;
            }
        }
        //as objects are passed by reference in java, the array is shuffled without needing to return it
    }
     /**
      * Trains the neural network on a dataset using backpropagation in conjuction with the ADAM optimiser.
      * @param LEARNING_RATE The learning rate of the network - this is recommended to be 0.001 as the ADAM optimiser is used.
      * @param NO_OF_EPOCHS The number of epochs in the training cycle (each epoch is one complete pass of the dataset).
      * @param SAMPLE_SIZE The batch size - the number of training items used per network update.
      * @param TOTAL_TRAIN_SIZE The total size of the training data
      * @param PRINT_AND_TEST_AFTER_THIS_MANY_ITERATIONS How many iterations to test the accuracy and print it. Each iteration is one parameter update of the network.
     * @param DROPOUT_RATE The dropout rate of each neuron
      */
     public void trainNew( final double LEARNING_RATE, final int NO_OF_EPOCHS,final int SAMPLE_SIZE,final int TOTAL_TRAIN_SIZE,final int PRINT_AND_TEST_AFTER_THIS_MANY_ITERATIONS, final double DROPOUT_RATE){
        double changeBy[];//stores how much to change each parameter by
        final long TIME_BEFORE=System.currentTimeMillis();//used to calculate how long the network has been running for
        double timeTemp = System.currentTimeMillis();//used to calculate the time between each testing of the network
        final int NO_OF_ITERATIONS_IN_ONE_EPOCH=(TOTAL_TRAIN_SIZE/SAMPLE_SIZE);//stores number of iterations per epoch
        final double SAMPLE_SIZE_DOUBLE=Double.parseDouble(String.valueOf(SAMPLE_SIZE));
        double[]bestNet = new double[this.ALL_LENGTH];//stores the best network configuration
        double accuracy;//stores the accuracy of the network
        double time = System.currentTimeMillis();
        double mT[]=new double[this.ALL_LENGTH];//stores the mT values - part of the ADAM optimisation formula
        double vT[]=new double[this.ALL_LENGTH];//stores the vT values - part of the ADAM optimisation formula
        double mHatT[]=new double[this.ALL_LENGTH];//stores the mHatT values - part of the ADAM optimisation formula
        double vHatT[]=new double[this.ALL_LENGTH];//stores the vHatT values - part of the ADAM optimisation formula
        double prevMT[]=new double[this.ALL_LENGTH];// stores the pervious MT values - part of the ADAM optimisation formula
        double prevVT[]=new double[this.ALL_LENGTH];// stores the pervious VT values - part of the ADAM optimisation formula
        double t =0.0;//stores the time step - also used in the ADAM forumla
        try{//checks to see if any previous learning iterations have been run with this model and allows for ADAM training to continue
            double l =this.previousVHat[0];
            for(int i =0;i<this.previousMHat.length;i++){
                prevMT[i]=this.previousMHat[i];
                prevVT[i]=this.previousVHat[i];    
            }
            t=this.timeStep;
        }catch(java.lang.NullPointerException e){
        }
        //stores constants used in ADAM optimisation method
        final double BETA_ONE=0.9;
        final double BETA_TWO=0.999;
        Random rnd = new Random();
        final double EPSILON = Math.pow(10,-8);
        double gradOfOne[];//stores the gradient of one item of training data
        int indexOfTrain=0;//stores the index that accesses the appropriate training data
        int testData[][]= PhilipMCS5SoftwareDevelopmentNeuralNetworkTrain.getTestData();//laods the test data
        int[][]trainingData=PhilipMCS5SoftwareDevelopmentNeuralNetworkTrain.getTrainingData();//loads the training data
        int indexPrintAndTest=1;//stores the the number of iterations since the previous print of accuracy
        double start[]=this.testAccuracy(testData);//calculates the initial network accuracy before any training
        double highestAcc=start[0];//stores the highest network accuracy
        for(int i =0;i<this.ALL_LENGTH;i++){
            bestNet[i]=this.ALL_WEIGHTS_AND_BIASES[i];//initialises the best network as the current network
        }
        double desiredOut[]= new double[2];//stores the desired output of the neural network
        double[]inputToNet=new double[this.NET_STRUCTURE[0]];//stores the array used to make an input to the net
        int indexData=0;//stores the index of training data to be accessed within an iteration
        System.out.println("Accuracy before training "+highestAcc+" %"+" loss of "+start[1]);
        for(int epoch=1;epoch<=NO_OF_EPOCHS;epoch++){//performs the specified number of epochs
                shuffleTrainingDataArray(trainingData);//shuffles the training data at the start of every epoch
            indexOfTrain=0;
            for(int iteration=1;iteration<=NO_OF_ITERATIONS_IN_ONE_EPOCH;iteration++){//performs every iteration in the epoch
                changeBy = new double[this.ALL_LENGTH];//sets change by to an empty array of the appropriate length
                indexData=indexOfTrain;
                indexOfTrain=indexOfTrain+SAMPLE_SIZE;
                for (int sampleNo = 0; sampleNo < SAMPLE_SIZE; sampleNo++) {//loops thorugh every data sample in the iteration
                    for(int i=0;i<this.NET_STRUCTURE[0];i++){//loads the network input
                        inputToNet[i]=trainingData[indexData][i+2];
                    }
                    //drops out neurons for regularisation if appropriate
                    for(int node=0;node<LENGTH_OF_NEURONS_DROPPED_ARRAY;node++){
                        NEURONS_DROPPED[node]=rnd.nextFloat()<DROPOUT_RATE;
                    }
                    this.feedThroughNet(inputToNet);//feeds the input through the network
                    desiredOut[0]=trainingData[indexData][0];//stores the desired network output
                    desiredOut[1]=trainingData[indexData][1];
                    indexData++;
                    gradOfOne = this.calcGradientAttemptOptimised(desiredOut);//calculates the error of each parameter
                    for (int i = 0; i < this.ALL_WEIGHTS_AND_BIASES.length; i++) {//stores how much each paramater should be changed by
                        changeBy[i]=changeBy[i]+gradOfOne[i];
                    }
                }
                t=t+1.0;//increments the time step - used in the ADAM formula
                for(int i=0;i<changeBy.length;i++){//updates all weights and biases using the ADAM optimisation technique
                    changeBy[i]=changeBy[i]/SAMPLE_SIZE_DOUBLE;//change by is normalised to reflect the sample size
                    //the ADAM optimisation formula is implemented
                    mT[i]=BETA_ONE*prevMT[i]+(1.0-BETA_ONE) * changeBy[i];
                    vT[i]=BETA_TWO*prevVT[i]+(1.0-BETA_TWO)*(changeBy[i]*changeBy[i]);
                    mHatT[i]=mT[i]/(1.0-Math.pow(BETA_ONE,t));
                    vHatT[i]=vT[i]/(1.0-Math.pow(BETA_TWO,t));
                    prevMT[i]=mT[i];
                    prevVT[i]=vT[i];
                    this.ALL_WEIGHTS_AND_BIASES[i]=this.ALL_WEIGHTS_AND_BIASES[i] - (LEARNING_RATE*mHatT[i]/(Math.sqrt(vHatT[i])+EPSILON));//the network values are configured appropriately  
                   //this.allWeightsAndBiases[i]=this.allWeightsAndBiases[i]-(changeBy[i]*learningRate); // this method is the commented out but is the gradient descent formula
                }
                if(indexPrintAndTest==PRINT_AND_TEST_AFTER_THIS_MANY_ITERATIONS){//checks to see if they system should test the network and print the result of this
                    //tests network, displays this and updates the best accuracy and best network variables if appropriate
                    System.out.println("Time training between training "+((System.currentTimeMillis()-time)/1000.0)+" seconds");
                    //ensures that no nodes are dropped when testing accuracy
                    for(int n=0;n<LENGTH_OF_NEURONS_DROPPED_ARRAY;n++){
                        NEURONS_DROPPED[n]=false;
                    }
                    double[]accAndLoss=this.testAccuracy(testData);
                    time = System.currentTimeMillis();
                    accuracy = accAndLoss[0];
                    if (accuracy >= highestAcc || highestAcc <= 0) {
                        for (int i = 0; i < this.ALL_WEIGHTS_AND_BIASES.length; i++) {
                            bestNet[i] = this.ALL_WEIGHTS_AND_BIASES[i];
                        }
                        highestAcc = accuracy;
                    }
                    System.out.println("Accuracy " + accuracy + "% after " + epoch + " epochs out of " + NO_OF_EPOCHS + " epochs. Sample size of " + SAMPLE_SIZE + " with a learning rate of " + LEARNING_RATE+" loss of "+accAndLoss[1]+ "iteration "+(iteration)+" out of "+NO_OF_ITERATIONS_IN_ONE_EPOCH+" best accuracy of "+highestAcc);
                    indexPrintAndTest=0;
                    if((System.currentTimeMillis()-timeTemp)/1000.0>=36000){//this clause is used to ensure that the network only takes a certain amount of time to train
                        System.out.println("ending training as time has elapsed "+ ((System.currentTimeMillis()-timeTemp)/1000.0));
                        epoch=NO_OF_EPOCHS+1;
                        break;
                    } 
                }
                indexPrintAndTest++;
            }
        }
        //stores the values of the ADAM technique - this allows the same neural network to call the train method multiple times
        this.timeStep=t;
        this.previousMHat=new double[this.ALL_LENGTH];
        this.previousVHat=new double[this.ALL_LENGTH];
        for(int i =0;i<prevMT.length;i++){
            this.previousMHat[i]=prevMT[i];
            this.previousVHat[i]=prevVT[i];
        }
        //calclates the final network accuracy
        accuracy = this.testAccuracy(testData)[0];
        System.out.println("Accuracy "+accuracy+" with loss of "+this.testAccuracy(testData)[1]);
        //writes all network details to a text file
        long timeAtEnd = System.currentTimeMillis();
        long timeTaken=timeAtEnd-TIME_BEFORE;
        try{
            FileWriter writer = new FileWriter(FILE_NAME,true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write("----------------------------");
            bufferedWriter.newLine();
            bufferedWriter.write("Network Structure ");
            for(int i =0;i<this.NET_STRUCTURE.length;i++){
                bufferedWriter.write(String.valueOf(this.NET_STRUCTURE[i])+"  ");
            }
            bufferedWriter.newLine();
            bufferedWriter.write("All weights and biases");
            bufferedWriter.newLine();
            for(int i =0;i<this.ALL_WEIGHTS_AND_BIASES.length;i++){
                bufferedWriter.write(String.valueOf(this.ALL_WEIGHTS_AND_BIASES[i]));
                bufferedWriter.newLine();
            }
            bufferedWriter.write("No of epochs "+NO_OF_EPOCHS);
            bufferedWriter.newLine();
            bufferedWriter.write("Final accuracy of "+accuracy);
            bufferedWriter.newLine();
            bufferedWriter.write("time taken in milliseconds "+timeTaken);
            bufferedWriter.newLine();
            bufferedWriter.write("Learning rate of "+LEARNING_RATE);
            bufferedWriter.newLine();
            bufferedWriter.write("Best network (with accuracy "+highestAcc+"% with this sturcture had a configuration of");
            bufferedWriter.newLine();
            for(int i=0;i<bestNet.length;i++){
                bufferedWriter.write(String.valueOf(bestNet[i]));
                bufferedWriter.newLine();
            }
            bufferedWriter.newLine();
            bufferedWriter.write("Using a sample size of "+SAMPLE_SIZE+" for each epoch out of a total of 60000 samples. Note this doesnt neccesarly apply if this is true:  false");
            bufferedWriter.newLine();
            bufferedWriter.write("----------------------------");
            bufferedWriter.close();
            writer.close();
        }catch(IOException e){
            System.out.println("error with file "+FILE_NAME+" error of "+e);
            System.out.println("Error trace");
            e.printStackTrace();
        }
    }
     /**
      * Gets the training data
      * @return The training data
      */
    private static int[][]getTrainingData(){
        return PhilipMCS5SoftwareDevelopmentNeuralNetworkTrain.getTrainingData();
    }
    /**
     * Gets the test data
     * @return The test data
     */
    private static int[][]getTestData(){
        return PhilipMCS5SoftwareDevelopmentNeuralNetworkTrain.getTestData();      
    }
    /**
     * Tests the network's accuracy
     * @param testData The test data to use when testing accuracy
     * @return The accuracy and loss of the system as an array of length 2. Element 0 corresponds to the accuracy, whilst element 1 is the loss.
     */
    public double[]testAccuracy(int testData[][]){
        double time = System.currentTimeMillis();//used to calculate how long it takes to test the system
        int totalCorrect=0;double[]out;
        double[]desiredOut = new double[2];
        double averageLoss=0.0;int greatestIndex;double err;
        double diff;
        double[]netInput=new double[PhilipMCS5SoftwareDevelopmentNeuralNetworkTrain.WIDTH_OF_DATA-2];//stores the network input
        for(int y=0;y<testData.length;y++){//loops trhough every item in the test dataset
            desiredOut[0]=testData[y][0];//gets the desired output of the neural network
            desiredOut[1]=testData[y][1];
            for(int x=2;x<testData[0].length;x++){//generates the input to the neural network
                netInput[x-2]=testData[y][x];
            }
            out=this.feedThroughNet(netInput);//passes the network input throught the neural network
            greatestIndex=0;//determines whether out[0] or out[1] is biggest
            if(out[1]>out[0]){
                greatestIndex=1;
            }
            if(desiredOut[greatestIndex]==1.0){//checks to see if network is correct
                totalCorrect++;
            }
           /* diff=out[0]-desiredOut[0];
            err=diff*diff;*/
           err=0;
            //calculates the loss of the network
            for(int i=0;i<out.length;i++){
                err = err+(desiredOut[i]*Math.log(out[i]));//categorical cross entropy loss is employed -  this makes the assumption that the desired output is a one hot vector
                
            }err=err*-1.0;
            averageLoss=averageLoss+(err);//adds loss to total loss
        }
        
        double accuracy = (double)totalCorrect/(double)PhilipMCS5SoftwareDevelopmentNeuralNetworkTrain.NO_OF_TEST_BOARDS;//calculates the accuracy of the network
        accuracy= accuracy*100.0;
        averageLoss=averageLoss/(double)PhilipMCS5SoftwareDevelopmentNeuralNetworkTrain.NO_OF_TEST_BOARDS;//calculates the average loss
        System.out.println("Testing time: "+((System.currentTimeMillis()-time)/1000.0)+" seconds . Total correct: "+totalCorrect+" out of "+PhilipMCS5SoftwareDevelopmentNeuralNetworkTrain.NO_OF_TEST_BOARDS);
        double ret[]={accuracy,averageLoss};
        return ret;
    }
    /**
     * This feeds a desired input through the neural network and returns the output. This is sometimes referred to as forward propagation.
     * @param input The input array to the network
     * @return The neural network output
     */
    public double[]feedThroughNet(double[]input){
        int currentAllIndex=0;//stores the current index used to access all weights and biases of the neural network
        double []nodeValuesAfterActivation;//stores the activations in the current layer
        double []out = new double[this.NET_STRUCTURE[this.NET_STRUCTURE_LENGTH_MINUS_ONE]];//stores the output of the neural network
        int indexForNodesInThisLayer;//stores the index used to access the nodes in a given layer
        double valueOfBias;//stores the value of the current bias
        double nodeValPreActivation;//stores the value of the node before the activation function is applied to it
        double[]nodeValuesAfterActivationPrevLayer=input;//stores the value of activated nodes in the previous layer
        int indexForPreActivs=0;//stores the index for pre activation arrays
        int indexForPostActivs=0;//stores the index for post actviation array
        this.nodeValuePreActivation=new double[this.TOTAL_NEURONS];
        this.nodeValuePostActivation=new double[this.TOTAL_NEURONS];
        for(int i=0;i<this.NET_STRUCTURE[0];i++){//sets values for the input layer - all nodes in input layer = input[i]
            this.nodeValuePreActivation[indexForPreActivs]=input[i];
            this.nodeValuePostActivation[indexForPostActivs]=input[i];
            indexForPostActivs++;
            indexForPreActivs++;
        }
        for(int layerIndex=1;layerIndex<this.NET_STRUCTURE_LENGTH;layerIndex++){//loops through network and calculates values of each layer
            indexForNodesInThisLayer=0;
            nodeValuesAfterActivation=new double[this.NET_STRUCTURE[layerIndex]];
            for(int indexOfLayer=0;indexOfLayer<this.NET_STRUCTURE[layerIndex];indexOfLayer++){//neurons in current layer
                if(layerIndex!=NET_STRUCTURE_LENGTH_MINUS_ONE&&NEURONS_DROPPED[INDEX_CACHE[layerIndex]+indexOfLayer]){//check to see if node is dropped out
                    currentAllIndex=currentAllIndex+1+this.NET_STRUCTURE[layerIndex-1];
                    this.nodeValuePreActivation[indexForPreActivs]=0;indexForPreActivs++;
                    this.nodeValuePostActivation[indexForPostActivs]=0;indexForPostActivs++;
                    nodeValuesAfterActivation[indexForNodesInThisLayer]=0;
                    indexForNodesInThisLayer++;
                    continue;
                }
                valueOfBias=this.ALL_WEIGHTS_AND_BIASES[currentAllIndex];//stores bias of node
                currentAllIndex++;
                nodeValPreActivation=0;
                for(int prevLayerIndex=0;prevLayerIndex<this.NET_STRUCTURE[layerIndex-1];prevLayerIndex++){//sums wieghts and previous node values to get preActivation value
                    nodeValPreActivation=nodeValPreActivation + nodeValuesAfterActivationPrevLayer[prevLayerIndex] * this.ALL_WEIGHTS_AND_BIASES[currentAllIndex];
                    currentAllIndex++;

                }
                this.nodeValuePreActivation[indexForPreActivs]=nodeValPreActivation+valueOfBias;//sets value before actviation in this layer
                indexForPreActivs++;
                if(layerIndex!=this.NET_STRUCTURE_LENGTH_MINUS_ONE){//sets  the post activation value
                    nodeValuesAfterActivation[indexForNodesInThisLayer]=activation(this.nodeValuePreActivation[indexForPreActivs-1]);//for non output layers the choosen actviation is used - normally this is reLu
                    indexForNodesInThisLayer++;
                    this.nodeValuePostActivation[indexForPostActivs]=nodeValuesAfterActivation[indexForNodesInThisLayer-1];
                    indexForPostActivs++;
                }else if(indexOfLayer==this.NET_STRUCTURE[layerIndex]-1){//for the output layer, the softmax activation is used
                    double preSigLayer[]=new double[this.NET_STRUCTURE[layerIndex]];
                    for(int indexVal=0;indexVal<preSigLayer.length;indexVal++){
                       preSigLayer[indexVal]=this.nodeValuePreActivation[this.TOTAL_NEURONS_MINUS_LAST_LAYER+indexVal];
                    }
                    nodeValuesAfterActivation=softmax(preSigLayer);
                    for(int i=0;i<nodeValuesAfterActivation.length;i++){
                        this.nodeValuePostActivation[indexForPostActivs]=nodeValuesAfterActivation[i];
                        indexForPostActivs++;
                    }
                }
                /*else{
                    nodeValuesAfterActivation[indexForNodesInThisLayer]=tanH(this.nodeValuePreActivation[indexForPreActivs-1]);//tanh used for output layer of non classification model
                    indexForNodesInThisLayer++;
                    this.nodeValuePostActivation[indexForPostActivs]=nodeValuesAfterActivation[indexForNodesInThisLayer-1];
                    indexForPostActivs++;                    
                }*/
            }
            nodeValuesAfterActivationPrevLayer=nodeValuesAfterActivation;
            if(layerIndex==this.NET_STRUCTURE_LENGTH_MINUS_ONE){
                out = nodeValuesAfterActivation;
            }
        }
        //returns the network output
        return out;
    }
    /**
     * Sets all weights and biases in the network to the chosen value
     * @param allWAndB The new set of weights and biases
     */
    public void setAllWeightsAndBiases(double[]allWAndB){
        if(allWAndB.length!=this.ALL_WEIGHTS_AND_BIASES.length){
            System.out.println("Attempted to change weights and biases to an array of different length ");
        }
        for(int i =0;i<allWAndB.length;i++){
            this.ALL_WEIGHTS_AND_BIASES[i]=allWAndB[i];
        }
    }
    /**
     * Uses the specified activation function. Currently this is reLU.
     * @param x The input 
     * @return The return
     */
     private static double activation(double x){
        if(x>0.0){
            return x;
        }else{
            return 0.0;
        } 
    }
    /**
     * Returns the gradient of the chosen activation. Currently this returns the gradient for reLU.
     * @param x The input
     * @return The return value
     */
    private static double activationDerivative(double x){
       if(x>0.0){
            return 1.0;
        }else{
            return 0.0;
        } 
    }
    /**
     * Gets a random number between one and minus one using the xavier technique.
     * @param numberOfNeuronsInLayerBelow The number of neurons in the layer below
     * @param numberOfNeuronsInLayer The number of neurons in the current layer
     * @return The random number
     */
    private double getRandDoubleBetweenOneAndMinusOne(int numberOfNeuronsInLayerBelow,int numberOfNeuronsInLayer){
        return RAND.nextGaussian()*Math.sqrt(2.0/((double)(numberOfNeuronsInLayer+numberOfNeuronsInLayerBelow)));
       // return (rnd.nextGaussian()*Math.sqrt(1.0/(double)numberOfInputsForWeightsInThisLayer)); //xavier init for tanh/sigmoid
    }
    /**
     * Gets the best network with this structure and loads it to a text file
     */
    public void writeBestNetToBestFile(){
        double[]bestNet=this.loadBestNetWithThisArchitecture();
        try{
            FileWriter w = new FileWriter(BEST_NET_FILE_NAME,false);
            BufferedWriter buffWrite = new BufferedWriter(w);
            buffWrite.write(String.valueOf(bestNet[0]));
            for(int i=1;i<bestNet.length;i++){
                buffWrite.newLine();
                buffWrite.write(String.valueOf(bestNet[i]));
            }
            buffWrite.flush();w.flush();
            buffWrite.close();w.close();
        }catch(IOException e){
            System.out.println("An unexpected error occured "+e);
        }
    }
}
