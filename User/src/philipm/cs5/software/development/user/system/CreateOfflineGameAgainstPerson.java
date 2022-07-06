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
 * Creates a form that allows the user to create an offline chess game against a human opponent
 * @author mortimer
 */
public class CreateOfflineGameAgainstPerson extends javax.swing.JFrame {
    SessionInfo session;//stores the session information
    /**
     * Creates new form CreateOfflineGame
     */
    private CreateOfflineGameAgainstPerson() {
        initComponents();
    }
    /**
     * Creates new form CreateOfflineGame
     * @param session The session information of the current user
     */
    public CreateOfflineGameAgainstPerson(SessionInfo session){
        initComponents();
        opponentNameTxt.setText(OfflinePlay.HUMAN_OPPONENT);//sets text field to default value
        this.session=session;
        gameTitleTxt.setText(CreateOfflineGame.suggestedGameTitle(session));//sets the title to a suggested valid game title
    }
    /**
     * Creates a new offline game which is added to the correct text file
     * @param opponentId The opponent id
     * @param isUserWhite whether the user is the white player
     */
    private void addNewGameToFile(String opponentId, boolean isUserWhite){
        try{
            //opponentID, isUserWhite, fen string (s),time game has taken
            //checks to see if file is empty
            FileReader read = new FileReader(OfflinePlay.OFFLINE_GAMES_FOLDER+File.separator+session.getUsername()+".txt");
            BufferedReader buffRead = new BufferedReader(read);
            boolean isEmpty = buffRead.readLine()==null;buffRead.close();read.close();
            //writes new record to text file
            FileWriter write = new FileWriter(OfflinePlay.OFFLINE_GAMES_FOLDER+File.separator+session.getUsername()+".txt",true);
            BufferedWriter buffWrite = new BufferedWriter(write);
            if(isEmpty==false){
                buffWrite.newLine();
            }
            buffWrite.write(opponentId+UserInfo.SPLIT_ITEM+String.valueOf(isUserWhite)+UserInfo.SPLIT_ITEM+OfflinePlay.NEW_BOARD+UserInfo.SPLIT_ITEM+"0");
            buffWrite.flush();write.flush();buffWrite.close();write.close();
        }catch(IOException e){
            JOptionPane.showMessageDialog(this,"An unexpected error has occured "+e,"Error",JOptionPane.OK_OPTION);
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
        jLabel2 = new javax.swing.JLabel();
        whiteBtn = new javax.swing.JToggleButton();
        randomBtn = new javax.swing.JToggleButton();
        blackBtn = new javax.swing.JToggleButton();
        backBtn = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        createGameBtn = new javax.swing.JButton();
        opponentNameTxt = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        gameTitleTxt = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        fiveMinutesBtn = new javax.swing.JToggleButton();
        tenMinutesBtn = new javax.swing.JToggleButton();
        unlimitedBtn = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Create New Game");

        jLabel1.setText("Opponent Name:");

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

        fiveMinutesBtn.setText("5 Minutes");
        fiveMinutesBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fiveMinutesBtnActionPerformed(evt);
            }
        });

        tenMinutesBtn.setText("10 Minutes");
        tenMinutesBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tenMinutesBtnActionPerformed(evt);
            }
        });

        unlimitedBtn.setText("Unlimited");
        unlimitedBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unlimitedBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap(471, Short.MAX_VALUE)
                        .addComponent(createGameBtn)
                        .addGap(37, 37, 37))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(backBtn)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5))
                                .addGap(44, 44, 44)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(whiteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(27, 27, 27)
                                        .addComponent(randomBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(15, 15, 15)
                                        .addComponent(blackBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(fiveMinutesBtn)
                                        .addGap(27, 27, 27)
                                        .addComponent(tenMinutesBtn)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(unlimitedBtn))))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(299, 299, 299)
                        .addComponent(jLabel3))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel4))
                        .addGap(75, 75, 75)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(gameTitleTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(opponentNameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(103, 103, 103))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(gameTitleTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(opponentNameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(whiteBtn)
                    .addComponent(randomBtn)
                    .addComponent(blackBtn))
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(fiveMinutesBtn)
                    .addComponent(tenMinutesBtn)
                    .addComponent(unlimitedBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                .addComponent(createGameBtn)
                .addGap(5, 5, 5)
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
        //gets opponent's name
        String opponentName=opponentNameTxt.getText();
        //ensures that opponent name is not a computer id
        if(opponentName.equals(OfflinePlay.LEVEL_ONE_COMP)||opponentName.equals(OfflinePlay.LEVEL_TWO_COMP)||opponentName.equals(OfflinePlay.LEVEL_THREE_COMP)||opponentName.equals(OfflinePlay.LEVEL_FOUR_COMP)||opponentName.equals(OfflinePlay.LEVEL_FIVE_COMP)||Validation.containsIllegalStrings(opponentName)){
            JOptionPane.showMessageDialog(this,"Invalid opponent name. Please change the opponent name.","Error",JOptionPane.OK_OPTION);
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
        String gameTitle=gameTitleTxt.getText();
        if(CreateOfflineGame.isGameTitleValid(session, gameTitle)==false||Validation.isPresent(gameTitle)==false){
            JOptionPane.showMessageDialog(this,"Please select a valid and unique title.","Error",JOptionPane.OK_OPTION);
            return;
        }
        int timeLeft=-100;
        if(unlimitedBtn.isSelected()){
            timeLeft=-100;
        }else if(fiveMinutesBtn.isSelected()){
            timeLeft=5*60;
        }else if(tenMinutesBtn.isSelected()){
            timeLeft =10*60;
        }else{
            JOptionPane.showMessageDialog(this,"Please select a time control setting.","Error",JOptionPane.OK_OPTION);
            return;
        }
        CreateOfflineGame.addNewGameToFile(gameTitle, opponentName, isUserWhite, timeLeft, session);//writes record to file
        //takes user to game
        ChessBoardState[]state={new ChessBoardState(OfflinePlay.NEW_BOARD)};
        ChessBoardOffline board = new ChessBoardOffline(session, opponentName, new ChessGame(state,false), isUserWhite, 0, timeLeft, timeLeft, gameTitle);
        board.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_createGameBtnActionPerformed

    private void fiveMinutesBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fiveMinutesBtnActionPerformed
        // makes sure only one time control can be selected
        if(fiveMinutesBtn.isSelected()){
            tenMinutesBtn.setSelected(false);
            unlimitedBtn.setSelected(false);
        }
    }//GEN-LAST:event_fiveMinutesBtnActionPerformed

    private void tenMinutesBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tenMinutesBtnActionPerformed
        // makes sure only one time control can be selected
        if(tenMinutesBtn.isSelected()){
            fiveMinutesBtn.setSelected(false);
            unlimitedBtn.setSelected(false);
        }
    }//GEN-LAST:event_tenMinutesBtnActionPerformed

    private void unlimitedBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unlimitedBtnActionPerformed
        // makes sure only one time control can be selected
        if(unlimitedBtn.isSelected()){
            fiveMinutesBtn.setSelected(false);
            tenMinutesBtn.setSelected(false);
        }
    }//GEN-LAST:event_unlimitedBtnActionPerformed

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
            java.util.logging.Logger.getLogger(CreateOfflineGameAgainstPerson.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CreateOfflineGameAgainstPerson.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CreateOfflineGameAgainstPerson.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CreateOfflineGameAgainstPerson.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CreateOfflineGameAgainstPerson().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backBtn;
    private javax.swing.JToggleButton blackBtn;
    private javax.swing.JButton createGameBtn;
    private javax.swing.JToggleButton fiveMinutesBtn;
    private javax.swing.JTextField gameTitleTxt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField opponentNameTxt;
    private javax.swing.JToggleButton randomBtn;
    private javax.swing.JToggleButton tenMinutesBtn;
    private javax.swing.JToggleButton unlimitedBtn;
    private javax.swing.JToggleButton whiteBtn;
    // End of variables declaration//GEN-END:variables
}