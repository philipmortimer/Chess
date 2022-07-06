/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package philipm.cs5.software.development.user.system;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 * Creates a new form that displays the online menu.
 * @author mortimer
 */
public class OnlineMenu extends javax.swing.JFrame {
    private SessionInfo session;//stores the session information
    private String gameData[][]=new String[3][WIDTH_OF_ONLINE_RECORD];//stores game data array
    public static final int WIDTH_OF_ONLINE_RECORD=14;//stores the width of an online game record
    private  TableRenderer renderer;//renders the online games table
    private Timer updateTableTimer;//used to periodically update the online table by requesting data from the server
    /**
     * Creates new form OnlineMenu
     */
    private OnlineMenu() {
        initComponents();
    }
    /**
     * Creates new online form
     * @param sess The session information
     */
    public OnlineMenu(SessionInfo sess){
        session=sess;
        initComponents();
        //intilaises various visual aspects of the form
        playBtn.setEnabled(false);deleteBtn.setEnabled(false);
        onlineGamesTbl.getTableHeader().setReorderingAllowed(false);//prevents columns from being moved by the user
        onlineGamesTbl.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);//ensures only one row can be selected - making it visually easier for the user to use        
        renderer=new TableRenderer(this);//creates a custom table renderer
        onlineGamesTbl.getColumnModel().getColumn(0).setCellRenderer(renderer);
        onlineGamesTbl.getColumnModel().getColumn(1).setCellRenderer(renderer);
        onlineGamesTbl.getColumnModel().getColumn(2).setCellRenderer(renderer);
        updateTable();//updates the table by getting the online records from the server
        //creates a timer that periodically updates the online game table with data from the server
        updateTableTimer=new Timer(10000, new ActionListener(){
            /**
             * Updates the table using the timer every x number of milliseconds.
             * @param e The ActionEvent from the timer indicating that the table should be updated
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTable();//updates thte table
            }
        });updateTableTimer.start();//starts the timer
    }
    /**
     * Sets the play button as enabled or disabled
     * @param enable Whether or not the play button should be enabled
     */
    public void setPlayButtonEnabled(boolean enable){
        playBtn.setEnabled(enable);
    }
    /**
     * Sets the delete button as enabled or disabled
     * @param enable Whether or not the delete button should be enabled
     */
    public void setDeleteButtonEnabled(boolean enable){
        deleteBtn.setEnabled(enable);
    }
    /**
     * Connects to server and attempts to update table of all games
     */
    private void updateTable(){
        try{
            String[]games;
            //sets table to empty first
            for(int y=0;y<3;y++){
                for(int x=0;x<3;x++){
                    onlineGamesTbl.setValueAt("",y,x);
                }
            }
            //stores whether a given row should be highlighted based on whether a game actually exists in that row of the table
            boolean isRowOneHighlighted=true;
            boolean isRowTwoHighlighted=true;
            boolean isRowThreeHighlighted=true;
            gameData=ThreadClient.getOnlineGameData(session.getUsername());//gets game data from server
            //updates table based on game data array
            for(int y=0;y<gameData.length;y++){
                onlineGamesTbl.setValueAt(gameData[y][0],y,0);//sets game code
                //sets the username of the opponent
                if(session.getUsername().equals(gameData[y][1])){
                    onlineGamesTbl.setValueAt(gameData[y][2],y,1);
                }else{
                    onlineGamesTbl.setValueAt(gameData[y][1],y,1);
                }
                games=gameData[y][3].split(OfflinePlay.SPLIT_FEN_STRING);
                if(String.valueOf(onlineGamesTbl.getValueAt(y, 1)).equals("")){//if the game has not yet been created, the field is set as empty and the row is rendered as a different colour
                    switch (y) {
                        case 0:
                            isRowOneHighlighted=false;
                            break;
                        case 1:
                            isRowTwoHighlighted=false;
                            break;
                        default:
                            isRowThreeHighlighted=false;
                            break;
                    }
                    onlineGamesTbl.setValueAt("",y,2);
                }else{
                    onlineGamesTbl.setValueAt(games[games.length-1].split(" ")[5],y,2);//gets the move number
                }
            }
            renderer.updateRowColour(isRowOneHighlighted, isRowTwoHighlighted, isRowThreeHighlighted);//updates highlighter
        }catch(Exception e){
            JOptionPane.showMessageDialog(this,"An error occured when retriving game data from the server. Please check your network settings. Error Details: "+e.getMessage(),"Error",JOptionPane.OK_OPTION);
            Menu menu = new Menu(session);
            menu.setVisible(true);
            this.dispose();return;//displays error message and takes user back to menu
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
        gameAgainstPersonBtn = new javax.swing.JButton();
        gameAgainstPersonBtn1 = new javax.swing.JButton();
        playGameAgainstFriendEnterCodeBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        onlineGamesTbl = new javax.swing.JTable();
        menuBtn = new javax.swing.JButton();
        playBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Title");

        jLabel1.setText("Online Play");

        gameAgainstPersonBtn.setText("New Game Against Random Player");
        gameAgainstPersonBtn.setToolTipText("");
        gameAgainstPersonBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gameAgainstPersonBtnActionPerformed(evt);
            }
        });

        gameAgainstPersonBtn1.setText("Create Game Against Friend");
        gameAgainstPersonBtn1.setToolTipText("");
        gameAgainstPersonBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gameAgainstPersonBtn1ActionPerformed(evt);
            }
        });

        playGameAgainstFriendEnterCodeBtn.setText("Play Against Friend - Enter Game Code");
        playGameAgainstFriendEnterCodeBtn.setToolTipText("");
        playGameAgainstFriendEnterCodeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playGameAgainstFriendEnterCodeBtnActionPerformed(evt);
            }
        });

        onlineGamesTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Game Code", "Opponent", "Move Number"
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
        onlineGamesTbl.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                onlineGamesTblFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                onlineGamesTblFocusLost(evt);
            }
        });
        jScrollPane1.setViewportView(onlineGamesTbl);
        if (onlineGamesTbl.getColumnModel().getColumnCount() > 0) {
            onlineGamesTbl.getColumnModel().getColumn(0).setResizable(false);
            onlineGamesTbl.getColumnModel().getColumn(1).setResizable(false);
            onlineGamesTbl.getColumnModel().getColumn(2).setResizable(false);
        }

        menuBtn.setText("Menu");
        menuBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuBtnActionPerformed(evt);
            }
        });

        playBtn.setText("Play");
        playBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playBtnActionPerformed(evt);
            }
        });

        deleteBtn.setText("Delete");
        deleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(141, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(gameAgainstPersonBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(gameAgainstPersonBtn1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(playGameAgainstFriendEnterCodeBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(304, 304, 304))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 651, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(101, 101, 101))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(391, 391, 391)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(menuBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(deleteBtn)
                .addGap(18, 18, 18)
                .addComponent(playBtn)
                .addGap(50, 50, 50))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(gameAgainstPersonBtn)
                .addGap(18, 18, 18)
                .addComponent(gameAgainstPersonBtn1)
                .addGap(18, 18, 18)
                .addComponent(playGameAgainstFriendEnterCodeBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(53, 53, 53)
                        .addComponent(menuBtn)
                        .addGap(15, 15, 15))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(playBtn)
                            .addComponent(deleteBtn))
                        .addGap(39, 39, 39))))
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

    private void onlineGamesTblFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_onlineGamesTblFocusGained
        //enables play and delete button once a row has been selected
        playBtn.setEnabled(true);deleteBtn.setEnabled(true);
    }//GEN-LAST:event_onlineGamesTblFocusGained

    private void onlineGamesTblFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_onlineGamesTblFocusLost

    }//GEN-LAST:event_onlineGamesTblFocusLost

    private void menuBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBtnActionPerformed
        // takes user back to menu
        Menu menu = new Menu(session);
        menu.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_menuBtnActionPerformed

    private void gameAgainstPersonBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gameAgainstPersonBtnActionPerformed
        //checks to ensure that the user is in two or fewer games currently
        try{
            Integer.parseInt(String.valueOf(onlineGamesTbl.getValueAt(2, 0)));
            JOptionPane.showMessageDialog(this, "You can only play up to three online games at any one time.","Error",JOptionPane.OK_OPTION);
            return;
        }catch(NumberFormatException e){
        }
        try{
            ThreadClient.createNewRandomOnlineGame(session.getUsername());//creates a new random online game
        }catch(Exception e){
            JOptionPane.showMessageDialog(this,"An error occured when communicating with the server. Please check your network settings. Error Details: "+e,"Error",JOptionPane.OK_OPTION);
        }
        updateTable();//updates the table to reflect the change
    }//GEN-LAST:event_gameAgainstPersonBtnActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        //attempts to delete a game that can be deleted.
        //only games where the opponent has net yet joined can be deleted
        if(onlineGamesTbl.getSelectedRow()>=0 && onlineGamesTbl.getSelectedRow()<=2){
            if(onlineGamesTbl.getValueAt(onlineGamesTbl.getSelectedRow(),1).equals("")==false){
                JOptionPane.showMessageDialog(this,"You can only delete games that an opponent has not yet joined. Please go into the game and resign if you wish to remove the game.","Unable to Delete",JOptionPane.OK_OPTION);
                return;
            }
            //asks for confirmation before deleting games
            int confirm=JOptionPane.showConfirmDialog(this,"Are you sure you would like to delete this game?","Delete?",JOptionPane.YES_NO_CANCEL_OPTION);
            if(confirm!=JOptionPane.YES_OPTION){
                return;
            }
            try{
                ThreadClient.deleteOnlineGame(Integer.parseInt(String.valueOf(onlineGamesTbl.getValueAt(onlineGamesTbl.getSelectedRow(),0))), 0.5);//deletes game
                updateTable();//updates the tab;e
            }catch(Exception  e){
                JOptionPane.showMessageDialog(this,"Error when communicating with the server. Please check your network settings. Error details: "+e.getMessage(),"Error",JOptionPane.OK_OPTION);
                updateTable();
                return;
            }
        }else{
            JOptionPane.showMessageDialog(this, "Please select a valid game to delete","Error",JOptionPane.OK_OPTION);
            return;
        }
    }//GEN-LAST:event_deleteBtnActionPerformed

    private void gameAgainstPersonBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gameAgainstPersonBtn1ActionPerformed
        //takes user to form to create game against a friend
        if(onlineGamesTbl.getValueAt(2,0).equals("")==false){
            JOptionPane.showMessageDialog(this, "You can only have three games at any one time.","Error",JOptionPane.OK_OPTION);
            return;
        }
        CreateGameAgainstFriend game = new CreateGameAgainstFriend(session);
        game.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_gameAgainstPersonBtn1ActionPerformed

    private void playGameAgainstFriendEnterCodeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playGameAgainstFriendEnterCodeBtnActionPerformed
        // allows user to input game code and play against friend
        if(onlineGamesTbl.getValueAt(2,0).equals("")==false){//ensures that user does not join more than three games
            JOptionPane.showMessageDialog(this, "You can only have three games at any one time.","Error",JOptionPane.OK_OPTION);
            return;
        }
        String code =JOptionPane.showInputDialog(this,"Please enter the game code provided by a friend:","Enter code",JOptionPane.OK_CANCEL_OPTION);
        if(code==null){//if cancel is pressed, nothing happens
            return;
        }
        try{
            int codeInt=Integer.parseInt(code);//tries converting code to an integer
            if(codeInt<0){
                //type and range check to ensure that the game code is possibly valid
                JOptionPane.showMessageDialog(this,"The game code must be a valid integer greater than 0","Invalid code",JOptionPane.OK_OPTION);
                return;
            }
            try{
                boolean succ = ThreadClient.joinGameUsingFriendCode(codeInt, session.getUsername());//attempts to join the game
                if(succ==false){
                    JOptionPane.showMessageDialog(this,"Unable to join game. The code entered is invalid or can't be used to join a game","Error",JOptionPane.OK_OPTION);
                    updateTable();
                    return;
                }
                updateTable();//updates the table
            }catch(Exception e){
                 JOptionPane.showMessageDialog(this,"Error when communicating with the server. Please check your network settings. Error details: "+e.getMessage(),"Error",JOptionPane.OK_OPTION);
                 updateTable();
                 return;
            }
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(this,"The game code must be a valid integer greater than 0","Invalid code",JOptionPane.OK_OPTION);
            return;
        }
    }//GEN-LAST:event_playGameAgainstFriendEnterCodeBtnActionPerformed

    private void playBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playBtnActionPerformed
        // takes the user to the selected game
        if(onlineGamesTbl.getSelectedRow()<0 || onlineGamesTbl.getSelectedRow()>2){//ensures that a valid game is selected
            JOptionPane.showMessageDialog(this, "Invalid game selected.","Invalid Game",JOptionPane.OK_OPTION);
            return;
        }
        //checks to ensure game selected is playable
        if(onlineGamesTbl.getValueAt(onlineGamesTbl.getSelectedRow(), 0).equals("") || onlineGamesTbl.getValueAt(onlineGamesTbl.getSelectedRow(), 1).equals("")){
            JOptionPane.showMessageDialog(this, "Invalid game selected.","Invalid Game",JOptionPane.OK_OPTION);
            return;            
        }
        //gets the most recent version of the data from the server
        String []data=null;
        try{
            data=ThreadClient.getGameData(Integer.parseInt(String.valueOf(onlineGamesTbl.getValueAt(onlineGamesTbl.getSelectedRow(),0))));//gets updated version of game record before joining
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Error when communicating with the server. Error details: "+e,"Invalid Game",JOptionPane.OK_OPTION);
            return;
        }
        //checks to see if game is already over - if it is, an appropriate message is displayed and the game is deleted
        boolean isUserPlayerOne=data[1].equals(session.getUsername());
        if(isUserPlayerOne && data[8].equals("false") && data[9].equals("true")){
            if(data[11].equals("1")){
                JOptionPane.showMessageDialog(this,"You won this game.","Win",JOptionPane.OK_OPTION);
            }else if(data[11].equals("0.5")){
                JOptionPane.showMessageDialog(this,"This game was a draw.","Draw",JOptionPane.OK_OPTION);
            }else{
                JOptionPane.showMessageDialog(this,"You lost this game.","Loss",JOptionPane.OK_OPTION);
            }
            data[8]="true";
            try{
                ThreadClient.updateOnlineGameRecord(data);
            }catch(Exception e){
                JOptionPane.showMessageDialog(this, "Error when  communicating with the server. Error details: "+e,"Error",JOptionPane.OK_OPTION);
                updateTable();
                return;
            }
            updateTable();
            return;           
        }else if(!isUserPlayerOne && data[9].equals("false") && data[8].equals("true")){
            if(data[11].equals("1")){
                JOptionPane.showMessageDialog(this,"You lost this game.","Loss",JOptionPane.OK_OPTION);
            }else if(data[11].equals("0.5")){
                JOptionPane.showMessageDialog(this,"This game was a draw.","Draw",JOptionPane.OK_OPTION);
            }else{
                JOptionPane.showMessageDialog(this,"You won this game.","Win",JOptionPane.OK_OPTION);
            }
            data[9]="true";
            try{
                ThreadClient.updateOnlineGameRecord(data);
            }catch(Exception e){
                JOptionPane.showMessageDialog(this, "Error when  communicating with the server. Error details: "+e,"Error",JOptionPane.OK_OPTION);
                updateTable();
                return;
            }
            updateTable();
            return;
        }
        //games is playable, hence user is taken to it
        //creates the chess game object
        String fenData[]=data[3].split(OfflinePlay.SPLIT_FEN_STRING);
        ChessBoardState[]statesSoFar=new ChessBoardState[fenData.length];
        for(int board=0;board<fenData.length;board++){
            statesSoFar[board]=new ChessBoardState(fenData[board]);
        }
        //takes the user to play the game
        ChessGame game = new ChessGame(statesSoFar, false);
         ChessBoardOnline   onlineGame = new ChessBoardOnline(session,game,data);onlineGame.setVisible(true);this.dispose();

    }//GEN-LAST:event_playBtnActionPerformed

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
            java.util.logging.Logger.getLogger(OnlineMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(OnlineMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(OnlineMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(OnlineMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new OnlineMenu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton deleteBtn;
    private javax.swing.JButton gameAgainstPersonBtn;
    private javax.swing.JButton gameAgainstPersonBtn1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton menuBtn;
    private javax.swing.JTable onlineGamesTbl;
    private javax.swing.JButton playBtn;
    private javax.swing.JButton playGameAgainstFriendEnterCodeBtn;
    // End of variables declaration//GEN-END:variables
}