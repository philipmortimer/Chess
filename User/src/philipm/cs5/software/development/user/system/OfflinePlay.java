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
 * This form is used to display all offline games and takes user to other forms which allow for the creation of offline games
 * @author mortimer
 */
public class OfflinePlay extends javax.swing.JFrame {
    SessionInfo sess;//stores the session information
    public static final String OFFLINE_GAMES_FOLDER="offlinegames";//stores the destination of the folder that stores all offline games
    private String[][]gameData=new String[3][4];//stores data of ongoing offline games
    public static final String SPLIT_FEN_STRING="fensplit*";//this is used to split between different FEN's in a given record
    public static final String NEW_BOARD="rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";//stores the FEN state for a new game
    public static final String LEVEL_ONE_COMP = "Level One Computer";//stores id's of various opponents
    public static final String LEVEL_TWO_COMP="Level Two Computer";
    public static final String LEVEL_THREE_COMP="Level Three Computer";
    public static final String LEVEL_FOUR_COMP="Level Four Computer";
    public static final String LEVEL_FIVE_COMP="Level Five Computer";
    public static final String HUMAN_OPPONENT="Person";
    //contents of one record storing game:
    //game id,opponentID, isUserWhite, boardState(s),time game has taken, time white has left, time black has left
    /**
     * Creates new form OfflinePlay
     */
    private OfflinePlay() {
        initComponents();
    }
    /**
     * Creates new form OfflinePlay
     * @param sess The session information of the current user
     */
    public OfflinePlay(SessionInfo sess){
        this.sess=sess;//stores the session
        initComponents();
        updateTable();//updates the table based on the contents of the offline games text file
        //sets various form attirbutes for clean visuals
        deleteGameBtn.setEnabled(false);playGameBtn.setEnabled(false);
        offlineGamesTbl.getTableHeader().setReorderingAllowed(false);//prevents columns from being moved by the user
        offlineGamesTbl.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);//ensures only one row can be selected - making it visually easier for the user to use
    }
    /**
     * Updates the table displaying the three stored games. Also updates the 2d array storing the current data for these three games.
     */
    private void updateTable(){
        try{
            File file = new File(OFFLINE_GAMES_FOLDER+File.separator+sess.getUsername()+".txt");
            file.createNewFile();//this creates a new text file if one does not already exist
        }catch(IOException ex){
            JOptionPane.showMessageDialog(this,"An unexpected error occured "+ex,"Error",JOptionPane.OK_OPTION);
        }
        try{
            //gets game data from text file and loads it global 2d array
            FileReader read = new FileReader(OFFLINE_GAMES_FOLDER+File.separator+sess.getUsername()+".txt");
            BufferedReader buffRead = new BufferedReader(read);
            String lineRead;String lineArr[];
            gameData=new String[3][7];
            int in=0;
            //loads game data into array
            while((lineRead=buffRead.readLine())!=null){
                lineArr = lineRead.split(UserInfo.SPLIT_ITEM);
                for(int i=0;i<lineArr.length;i++){
                    gameData[in][i]=lineArr[i];
                }
                in++;
            }
            buffRead.close();read.close();
        }catch(IOException ex){
            JOptionPane.showMessageDialog(this,"An unexpected error occured "+ex,"Error",JOptionPane.OK_OPTION);
        }
        //updates table contents to reflect gameData array
        String[]games;//stores all games that have been played so far
        for(int y=0;y<gameData.length;y++){
            if(gameData[y][0]==null){
                offlineGamesTbl.setValueAt("", y, 0);//sets game title as empty
                offlineGamesTbl.setValueAt("",y,1);//sets opponent empty
                offlineGamesTbl.setValueAt("",y,2);//sets move number empty
            }else{
                offlineGamesTbl.setValueAt(gameData[y][0], y, 0);//sets game title
                offlineGamesTbl.setValueAt(gameData[y][1],y,1);//sets opponent id
                games=gameData[y][3].split(SPLIT_FEN_STRING);
                offlineGamesTbl.setValueAt(games[games.length-1].split(" ")[5],y,2);//sets move number
            }
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
        newGamePersonBtn = new javax.swing.JButton();
        newGameCompBtn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        offlineGamesTbl = new javax.swing.JTable();
        menuBtn = new javax.swing.JButton();
        playGameBtn = new javax.swing.JButton();
        deleteGameBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Offline");

        newGamePersonBtn.setText("New Game Against Person");
        newGamePersonBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newGamePersonBtnActionPerformed(evt);
            }
        });

        newGameCompBtn.setText("New Game Against Computer");
        newGameCompBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newGameCompBtnActionPerformed(evt);
            }
        });

        jLabel1.setText("Offline Play");

        offlineGamesTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Game Title", "Opponent", "Moves Made"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        offlineGamesTbl.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                offlineGamesTblFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                offlineGamesTblFocusLost(evt);
            }
        });
        jScrollPane1.setViewportView(offlineGamesTbl);
        if (offlineGamesTbl.getColumnModel().getColumnCount() > 0) {
            offlineGamesTbl.getColumnModel().getColumn(0).setResizable(false);
            offlineGamesTbl.getColumnModel().getColumn(1).setResizable(false);
            offlineGamesTbl.getColumnModel().getColumn(2).setResizable(false);
        }

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(239, 239, 239)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(newGamePersonBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(newGameCompBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 367, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(373, 373, 373)
                        .addComponent(jLabel1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 121, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 651, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(84, 84, 84))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(45, 45, 45)
                .addComponent(newGamePersonBtn)
                .addGap(58, 58, 58)
                .addComponent(newGameCompBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                .addContainerGap())
        );

        menuBtn.setText("Menu");
        menuBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuBtnActionPerformed(evt);
            }
        });

        playGameBtn.setText("Play Game");
        playGameBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playGameBtnActionPerformed(evt);
            }
        });

        deleteGameBtn.setText("Delete Game");
        deleteGameBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteGameBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addComponent(menuBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(deleteGameBtn)
                .addGap(37, 37, 37)
                .addComponent(playGameBtn)
                .addGap(71, 71, 71))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                        .addComponent(menuBtn)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(playGameBtn)
                            .addComponent(deleteGameBtn))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBtnActionPerformed
        //takes the user back the main menu
        Menu men = new Menu(sess);
        men.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_menuBtnActionPerformed

    private void newGamePersonBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newGamePersonBtnActionPerformed
        //checks to see if a new game can be created
        if(gameData[2][1]!=null){
            JOptionPane.showMessageDialog(this, "Only three games can be played at any one time. Please delete a game to create a new one.","Too many games",JOptionPane.OK_OPTION);
            return;
        }
        //takes user to form where they can create a new offline game
        CreateOfflineGameAgainstPerson off = new CreateOfflineGameAgainstPerson(sess);
        off.setVisible(true);this.dispose();
    }//GEN-LAST:event_newGamePersonBtnActionPerformed

    private void newGameCompBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newGameCompBtnActionPerformed
        //checks to see if a new game can be created
        if(gameData[2][0]!=null){
            JOptionPane.showMessageDialog(this, "Only three games can be played at any one time. Please delete a game to create a new one.","Too many games",JOptionPane.OK_OPTION);
            return;
        }        
        //takes user to form where they can create a new offline game
        CreateOfflineGame off = new CreateOfflineGame(sess);
        off.setVisible(true);this.dispose();
    }//GEN-LAST:event_newGameCompBtnActionPerformed

    private void offlineGamesTblFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_offlineGamesTblFocusGained
        //enables buttons once a row has been selected
        deleteGameBtn.setEnabled(true);playGameBtn.setEnabled(true);
    }//GEN-LAST:event_offlineGamesTblFocusGained

    private void offlineGamesTblFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_offlineGamesTblFocusLost
     
    }//GEN-LAST:event_offlineGamesTblFocusLost

    private void playGameBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playGameBtnActionPerformed
        //takes user to selected game, if it is a valid game
        if(offlineGamesTbl.getSelectedRow()==-1 || gameData[offlineGamesTbl.getSelectedRow()][0]==null){//checks to make sure a game is actually selected
            JOptionPane.showMessageDialog(this, "Please select a valid game to play.","No Game Selected",JOptionPane.OK_OPTION);
            return;
        }
        String fen[]=gameData[offlineGamesTbl.getSelectedRow()][3].split(SPLIT_FEN_STRING);//stores all fen strings for games so far
        ChessBoardState states[]=new ChessBoardState[fen.length];//stores all boards played so far
        for(int i=0;i<states.length;i++){//loads all states
            states[i]=new ChessBoardState(fen[i]);
        }
        //creates new chess board
        ChessBoardOffline board = new ChessBoardOffline(sess, gameData[offlineGamesTbl.getSelectedRow()][1], new ChessGame(states,gameData[offlineGamesTbl.getSelectedRow()][1].equals(LEVEL_TWO_COMP)), gameData[offlineGamesTbl.getSelectedRow()][2].equals("true"), Integer.parseInt(gameData[offlineGamesTbl.getSelectedRow()][4]),Integer.parseInt(gameData[offlineGamesTbl.getSelectedRow()][5]),Integer.parseInt(gameData[offlineGamesTbl.getSelectedRow()][6]),gameData[offlineGamesTbl.getSelectedRow()][0]);
        board.setVisible(true);this.dispose();

    }//GEN-LAST:event_playGameBtnActionPerformed

    private void deleteGameBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteGameBtnActionPerformed
        //deletes game stored if it is a valid game to delete
        if(offlineGamesTbl.getSelectedRow()==-1 || gameData[offlineGamesTbl.getSelectedRow()][0]==null){//checks to make sure a game is actually selected
            JOptionPane.showMessageDialog(this, "Please select a valid game to delete.","No Game Selected",JOptionPane.OK_OPTION);
            return;
        }
        //seeks user confirmation before deleting game
        if(JOptionPane.showConfirmDialog(this, "Are you sure you would like to delete this game?", "Delete?", JOptionPane.YES_NO_CANCEL_OPTION)!=JOptionPane.YES_OPTION){
            return;
        }
        //deletes game from text file and array and uptables table accordingly
        try{
            //writes old data without the game deleted to the offline game file
            FileWriter write = new FileWriter(OFFLINE_GAMES_FOLDER+File.separator+sess.getUsername()+".txt",false);
            BufferedWriter buffWrite = new BufferedWriter(write);
            boolean isFirstWrite=true;
            String line;//stores line of text to write
            //writes new data to file
            for(int y =0;y<gameData.length;y++){
                if(y!=offlineGamesTbl.getSelectedRow()){
                    if(gameData[y][0]!=null){
                        line=gameData[y][0];
                        for(int i=1;i<gameData[0].length;i++){//creates text to write to file
                            line =line+ UserInfo.SPLIT_ITEM+gameData[y][i];
                        }
                        if(isFirstWrite==false){
                            buffWrite.newLine();
                        }else{
                            isFirstWrite=false;
                        }
                        buffWrite.write(line);
                    }
                }
            }
            buffWrite.flush();write.flush();buffWrite.close();write.close();//closes and flushes writers
        }catch(IOException ex){
            JOptionPane.showMessageDialog(this,"An unexpected error occured "+ex,"Error",JOptionPane.OK_OPTION);
        }
        updateTable();//updates table and gameData array
    }//GEN-LAST:event_deleteGameBtnActionPerformed

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
            java.util.logging.Logger.getLogger(OfflinePlay.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(OfflinePlay.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(OfflinePlay.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(OfflinePlay.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new OfflinePlay().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton deleteGameBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton menuBtn;
    private javax.swing.JButton newGameCompBtn;
    private javax.swing.JButton newGamePersonBtn;
    private javax.swing.JTable offlineGamesTbl;
    private javax.swing.JButton playGameBtn;
    // End of variables declaration//GEN-END:variables
}
