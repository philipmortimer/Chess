/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package philipm.cs5.software.development.user.system;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;
import philipm.cs5.software.development.server.system.UserInfo;

/**
 * Creates a form that allows the user to play an offline game against a computer opponent
 * @author mortimer
 */
public class CreateOfflineGame extends javax.swing.JFrame {
    SessionInfo session;//stores the session information
    /**
     * Creates new form CreateOfflineGame
     */
    private CreateOfflineGame() {
        initComponents();
    }
    /**
     * Creates new form CreateOfflineGame
     * @param session The session information of the current user
     */
    public CreateOfflineGame(SessionInfo session){
        initComponents();
        this.session=session;
        gameTitleTxt.setText(suggestedGameTitle(session));//sets the game title to a suggested title that has not yet been taken
    }
    /**
     * Suggests a suitable game id. This will be either Game 1, Game 2 or Game 3.
     * @param ses The session info object used to get the appropriate username
     * @return The title
     */
    public static String suggestedGameTitle(SessionInfo ses){
        String title="Game ";int num=1;//stores the number to put at the end of the title
        String titlesTaken[]=new String[3];int noOfGames=0;//stores all game titles
        boolean isTaken=false;
        try{
            //gets the title of all current games
            FileReader read = new FileReader(OfflinePlay.OFFLINE_GAMES_FOLDER+File.separator+ses.getUsername()+".txt");
            BufferedReader buffRead = new BufferedReader(read);
            String lineRead;
            while((lineRead=buffRead.readLine())!=null){
                titlesTaken[noOfGames]=lineRead.split(UserInfo.SPLIT_ITEM)[0];
                noOfGames++;
            }buffRead.close();read.close();
            //tries game1
            for(int i=0;i<noOfGames;i++){
                if(titlesTaken[i].equals("Game1")){
                    isTaken=true;
                    break;
                }
            }
            if(isTaken){
                isTaken=false;
                for(int i=0;i<noOfGames;i++){//tries Game2
                    if(titlesTaken[i].equals("Game2")){
                        isTaken=true;
                        break;
                    }
                }
                if(isTaken){
                    return "Game3";
                }else{
                    return "Game2";
                }
            }else{
                return "Game1";
            }
        }catch(IOException e){
            JOptionPane.showMessageDialog(null,"An unexpected error occured "+e);
        }
        return title+String.valueOf(num);
    }
    /**
     * Checks to see if the game title chosen is valid (i.e. unique and not containing any illegal characters)
     * @param ses The session information
     * @param title The game title
     * @return True if valid, otherwise false
     */
    public static boolean isGameTitleValid(SessionInfo ses,String title){
        if(Validation.containsIllegalStrings(title)){//checks that the title does not contain illegal characters
            return false;
        }
        //searches to ensure that game title is unique
        try{
            FileReader read = new FileReader(OfflinePlay.OFFLINE_GAMES_FOLDER+File.separator+ses.getUsername()+".txt");
            BufferedReader buffRead = new BufferedReader(read);
            String lineRead;
            while((lineRead=buffRead.readLine())!=null){
                if(lineRead.split(UserInfo.SPLIT_ITEM)[0].equals(title)){
                    return false;
                }
            }buffRead.close();read.close();
        }catch(IOException e){
            JOptionPane.showMessageDialog(null,"An unexpected error occured "+e);
        }
        return true;
    }
    /**
     * Adds new offline game to text file
     * @param gameTitle The game title
     * @param opponentId The opponent id
     * @param isUserWhite Whether the user plays as white
     * @param secondsWhiteAndBlackHave The time control setting. This equals -100 if no time control
     * @param sess The session info
     */
    public static void addNewGameToFile(String gameTitle,String opponentId, boolean isUserWhite,int secondsWhiteAndBlackHave,SessionInfo sess){
        try{
            //game id,opponentID, isUserWhite, boardState(s),time game has taken, time white has left, time black has left
            //checks to see if file is empty
            FileReader read = new FileReader(OfflinePlay.OFFLINE_GAMES_FOLDER+File.separator+sess.getUsername()+".txt");
            BufferedReader buffRead = new BufferedReader(read);
            boolean isEmpty = buffRead.readLine()==null;buffRead.close();read.close();
            //writes new record to text file
            FileWriter write = new FileWriter(OfflinePlay.OFFLINE_GAMES_FOLDER+File.separator+sess.getUsername()+".txt",true);
            BufferedWriter buffWrite = new BufferedWriter(write);
            if(isEmpty==false){
                buffWrite.newLine();
            }
            buffWrite.write(gameTitle+UserInfo.SPLIT_ITEM+opponentId+UserInfo.SPLIT_ITEM+String.valueOf(isUserWhite)+UserInfo.SPLIT_ITEM+OfflinePlay.NEW_BOARD+UserInfo.SPLIT_ITEM+"0"+UserInfo.SPLIT_ITEM+String.valueOf(secondsWhiteAndBlackHave)+UserInfo.SPLIT_ITEM+String.valueOf(secondsWhiteAndBlackHave));
            buffWrite.flush();write.flush();buffWrite.close();write.close();
        }catch(IOException e){
            JOptionPane.showMessageDialog(null,"An unexpected error has occured "+e,"Error",JOptionPane.OK_OPTION);
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        difficultyOneBtn = new javax.swing.JToggleButton();
        difficultyTwoBtn = new javax.swing.JToggleButton();
        difficultyThreeBtn = new javax.swing.JToggleButton();
        difficultyFourBtn = new javax.swing.JToggleButton();
        difficultyFiveBtn = new javax.swing.JToggleButton();
        jLabel2 = new javax.swing.JLabel();
        whiteBtn = new javax.swing.JToggleButton();
        randomBtn = new javax.swing.JToggleButton();
        blackBtn = new javax.swing.JToggleButton();
        backBtn = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        createGameBtn = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        gameTitleTxt = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        unlimitedTimeBtn = new javax.swing.JToggleButton();
        tenMinsBtn = new javax.swing.JToggleButton();
        fiveMinutesBtn = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Create New Game");

        jLabel1.setText("Computer Difficulty");

        difficultyOneBtn.setText("1 (Easiest)");
        difficultyOneBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                difficultyOneBtnActionPerformed(evt);
            }
        });

        difficultyTwoBtn.setText("2");
        difficultyTwoBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                difficultyTwoBtnActionPerformed(evt);
            }
        });

        difficultyThreeBtn.setText("3");
        difficultyThreeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                difficultyThreeBtnActionPerformed(evt);
            }
        });

        difficultyFourBtn.setText("4");
        difficultyFourBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                difficultyFourBtnActionPerformed(evt);
            }
        });

        difficultyFiveBtn.setText("5 (Hardest)");
        difficultyFiveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                difficultyFiveBtnActionPerformed(evt);
            }
        });

        jLabel2.setText("You play as");

        whiteBtn.setText("White");
        whiteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                whiteBtnActionPerformed(evt);
            }
        });

        randomBtn.setText("Random");
        randomBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                randomBtnActionPerformed(evt);
            }
        });

        blackBtn.setText("Black");
        blackBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                blackBtnActionPerformed(evt);
            }
        });

        backBtn.setText("Back");
        backBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backBtnActionPerformed(evt);
            }
        });

        jLabel3.setText("Create New Game");
        jLabel3.setToolTipText("");

        createGameBtn.setText("Create Game");
        createGameBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createGameBtnActionPerformed(evt);
            }
        });

        jLabel4.setText("Game Title");

        jLabel5.setText("Time Control");

        unlimitedTimeBtn.setText("Unlimited");
        unlimitedTimeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unlimitedTimeBtnActionPerformed(evt);
            }
        });

        tenMinsBtn.setText("10 Minutes");
        tenMinsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tenMinsBtnActionPerformed(evt);
            }
        });

        fiveMinutesBtn.setText("5 Minutes");
        fiveMinutesBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fiveMinutesBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(createGameBtn)
                .addGap(140, 140, 140))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(55, 55, 55)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(whiteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(7, 7, 7)
                                                .addComponent(randomBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(difficultyTwoBtn)
                                                .addGap(0, 0, 0)
                                                .addComponent(difficultyThreeBtn)
                                                .addGap(1, 1, 1)
                                                .addComponent(difficultyFourBtn)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(difficultyFiveBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                                            .addComponent(blackBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addComponent(difficultyOneBtn)
                                    .addComponent(gameTitleTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(fiveMinutesBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(tenMinsBtn)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(unlimitedTimeBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE))))
                            .addComponent(backBtn)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(299, 299, 299)
                        .addComponent(jLabel3))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel4))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel5)))
                .addContainerGap(166, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addComponent(gameTitleTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(difficultyOneBtn)
                    .addComponent(difficultyTwoBtn)
                    .addComponent(difficultyThreeBtn)
                    .addComponent(difficultyFourBtn)
                    .addComponent(difficultyFiveBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(whiteBtn)
                    .addComponent(randomBtn)
                    .addComponent(blackBtn))
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(unlimitedTimeBtn)
                    .addComponent(tenMinsBtn)
                    .addComponent(fiveMinutesBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addComponent(createGameBtn)
                .addGap(1, 1, 1)
                .addComponent(backBtn)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void difficultyOneBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_difficultyOneBtnActionPerformed
        // ensures that only difficulty option can be selected
        if(difficultyOneBtn.isSelected()){
            difficultyFiveBtn.setSelected(false);
            difficultyFourBtn.setSelected(false);
            difficultyThreeBtn.setSelected(false);
            difficultyTwoBtn.setSelected(false);
        }
    }//GEN-LAST:event_difficultyOneBtnActionPerformed

    private void difficultyTwoBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_difficultyTwoBtnActionPerformed
        // ensures that only difficulty option can be selected
        if(difficultyTwoBtn.isSelected()){
            difficultyFiveBtn.setSelected(false);
            difficultyFourBtn.setSelected(false);
            difficultyThreeBtn.setSelected(false);
            difficultyOneBtn.setSelected(false);
        }
    }//GEN-LAST:event_difficultyTwoBtnActionPerformed

    private void difficultyThreeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_difficultyThreeBtnActionPerformed
        // ensures that only difficulty option can be selected
        if(difficultyThreeBtn.isSelected()){
            difficultyFiveBtn.setSelected(false);
            difficultyFourBtn.setSelected(false);
            difficultyOneBtn.setSelected(false);
            difficultyTwoBtn.setSelected(false);
        }
    }//GEN-LAST:event_difficultyThreeBtnActionPerformed

    private void difficultyFourBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_difficultyFourBtnActionPerformed
        // ensures that only difficulty option can be selected
        if(difficultyFourBtn.isSelected()){
            difficultyFiveBtn.setSelected(false);
            difficultyOneBtn.setSelected(false);
            difficultyThreeBtn.setSelected(false);
            difficultyTwoBtn.setSelected(false);
        }
    }//GEN-LAST:event_difficultyFourBtnActionPerformed

    private void difficultyFiveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_difficultyFiveBtnActionPerformed
        // ensures that only difficulty option can be selected
        if(difficultyFiveBtn.isSelected()){
            difficultyOneBtn.setSelected(false);
            difficultyFourBtn.setSelected(false);
            difficultyThreeBtn.setSelected(false);
            difficultyTwoBtn.setSelected(false);
        }
    }//GEN-LAST:event_difficultyFiveBtnActionPerformed

    private void whiteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_whiteBtnActionPerformed
        // ensures that only one colour can be choosen
        if(whiteBtn.isSelected()){
            blackBtn.setSelected(false);
            randomBtn.setSelected(false);
        }
    }//GEN-LAST:event_whiteBtnActionPerformed

    private void randomBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_randomBtnActionPerformed
        // ensures that only one colour can be choosen
        if(randomBtn.isSelected()){
            blackBtn.setSelected(false);
            whiteBtn.setSelected(false);
        }
    }//GEN-LAST:event_randomBtnActionPerformed

    private void blackBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_blackBtnActionPerformed
        // ensures that only one colour can be choosen
        if(blackBtn.isSelected()){
            whiteBtn.setSelected(false);
            randomBtn.setSelected(false);
        }
    }//GEN-LAST:event_blackBtnActionPerformed

    private void backBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBtnActionPerformed
        //takes user back to offline menu
        OfflinePlay offline = new OfflinePlay(session);
        offline.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_backBtnActionPerformed

    private void createGameBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createGameBtnActionPerformed
        // creates a new game after checking that game settings have actually been selected;
        //gets selected difficulty setting
        String computerDifficulty="";
        if(difficultyOneBtn.isSelected()){
            computerDifficulty=OfflinePlay.LEVEL_ONE_COMP;
        }else if(difficultyTwoBtn.isSelected()){
            computerDifficulty=OfflinePlay.LEVEL_TWO_COMP;
        }else if(difficultyThreeBtn.isSelected()){
            computerDifficulty=OfflinePlay.LEVEL_THREE_COMP;
        }else if(difficultyFourBtn.isSelected()){
            computerDifficulty=OfflinePlay.LEVEL_FOUR_COMP;
        }else if(difficultyFiveBtn.isSelected()){
            computerDifficulty=OfflinePlay.LEVEL_FIVE_COMP;
        }else{
            //error message if no difficulty setting selected
            JOptionPane.showMessageDialog(this,"Please select a difficulty setting.","Error",JOptionPane.OK_OPTION);
            return;
        }
        String gameTitle=gameTitleTxt.getText();//stores the game title
        if(isGameTitleValid(session, gameTitle)==false||Validation.isPresent(gameTitle)==false){
            JOptionPane.showMessageDialog(this,"Please choose a valid, unique game title.","Error",JOptionPane.OK_OPTION);
            return;
        }
        boolean isUserWhite=false;//stores which colour the user will play as
        if(whiteBtn.isSelected()){
            isUserWhite=true;
        }else if(blackBtn.isSelected()){
            isUserWhite=false;
        }else if(randomBtn.isSelected()){
            //randomly assings user a colour
            if(Math.random()>=0.5){
                isUserWhite=true;
            }else{
                isUserWhite=false;
            }
        }else{
            //error message if no colour chosen
            JOptionPane.showMessageDialog(this,"Please select a colour to play as.","Error",JOptionPane.OK_OPTION);
            return;           
        }
        int timeControl=-100;
        if(unlimitedTimeBtn.isSelected()){
            timeControl=-100;
        }else if(fiveMinutesBtn.isSelected()){
            timeControl=5*60;
        }else if(tenMinsBtn.isSelected()){
            timeControl=10*60;
        }else{
            JOptionPane.showMessageDialog(this,"Please select a time control setting.","Error",JOptionPane.OK_OPTION);
            return;                   
        }
        addNewGameToFile(gameTitle,computerDifficulty,isUserWhite,timeControl,session);//adds game to file
        //takes user to game
        ChessBoardState[]state={new ChessBoardState(OfflinePlay.NEW_BOARD)};
        ChessBoardOffline board = new ChessBoardOffline(session, computerDifficulty, new ChessGame(state,computerDifficulty.equals(OfflinePlay.LEVEL_TWO_COMP)), isUserWhite, 0, timeControl, timeControl, gameTitle);
        board.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_createGameBtnActionPerformed

    private void unlimitedTimeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unlimitedTimeBtnActionPerformed
        // ensures only one time control can be selected
        if(unlimitedTimeBtn.isSelected()){
            fiveMinutesBtn.setSelected(false);
            tenMinsBtn.setSelected(false);
        }
    }//GEN-LAST:event_unlimitedTimeBtnActionPerformed

    private void tenMinsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tenMinsBtnActionPerformed
        // ensures only one time control can be selected
        if(tenMinsBtn.isSelected()){
            fiveMinutesBtn.setSelected(false);
            unlimitedTimeBtn.setSelected(false);
        }
    }//GEN-LAST:event_tenMinsBtnActionPerformed

    private void fiveMinutesBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fiveMinutesBtnActionPerformed
        // ensures only one time control can be selected
        if(fiveMinutesBtn.isSelected()){
            tenMinsBtn.setSelected(false);
            unlimitedTimeBtn.setSelected(false);
        }
    }//GEN-LAST:event_fiveMinutesBtnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CreateOfflineGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CreateOfflineGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CreateOfflineGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CreateOfflineGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CreateOfflineGame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backBtn;
    private javax.swing.JToggleButton blackBtn;
    private javax.swing.JButton createGameBtn;
    private javax.swing.JToggleButton difficultyFiveBtn;
    private javax.swing.JToggleButton difficultyFourBtn;
    private javax.swing.JToggleButton difficultyOneBtn;
    private javax.swing.JToggleButton difficultyThreeBtn;
    private javax.swing.JToggleButton difficultyTwoBtn;
    private javax.swing.JToggleButton fiveMinutesBtn;
    private javax.swing.JTextField gameTitleTxt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JToggleButton randomBtn;
    private javax.swing.JToggleButton tenMinsBtn;
    private javax.swing.JToggleButton unlimitedTimeBtn;
    private javax.swing.JToggleButton whiteBtn;
    // End of variables declaration//GEN-END:variables
}
