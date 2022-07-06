/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package philipm.cs5.software.development.user.system;

import javax.swing.JTable;


/**
 * This form is used to display the league table in the West Cross Chess Club
 * @author mortimer
 */
public class LeagueTable extends javax.swing.JFrame {
    private SessionInfo session;//stores the session information
    private static final String[]COLUMN_NAMES=new String[]{"Position","Username","Wins","Draws","Losses","Points"};//stores the name of the columns for the league table
    /**
     * Creates new form LeagueTable
     */
    private LeagueTable() {
        initComponents();
    }
    /**
     * Creates a form to view the league table. This form is created when the fixtures for the club have been generated.
     * @param session The session info
     * @param leagueTableData The league table data
     */
    public LeagueTable(SessionInfo session,String[][]leagueTableData){
        this.session= session;
        initComponents();
        //sets various details of the table to allow for a clean and intuitive user experience
        leagueTable.getTableHeader().setReorderingAllowed(false);
        leagueTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        leagueTable.setDefaultEditor(Object.class, null);
        setTable(leagueTableData);//sets the league table based on the league table data
    }
    /**
     * Creates a form to view the league table. This form is created when the fixtures for the club have not yet been generated.
     * @param session The session info
     * @param allClubMembers The league table data
     */
    public LeagueTable(SessionInfo session,String[]allClubMembers){
        this.session= session;
        initComponents();
        //sets various details of the table to allow for a clean and intuitive user experience
        leagueTable.getTableHeader().setReorderingAllowed(false);
        leagueTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        leagueTable.setDefaultEditor(Object.class, null);
        setTable(allClubMembers);//sets the table
    }
    /**
     * Sets the league table based on the league table data
     * @param leagueTableData The league table data
     */
    private void setTable(String[][]leagueTableData){
        String[][]leagueDataFormatted=new String[leagueTableData.length][6];//stores the formatted data, which is used to display the table
        int pos=1; //stores the position
        String prevPoints="";//stores the points tally of the player directly above a certain user
        for(int player=0;player<leagueTableData.length;player++){//loops through every player in the league table and calculates their position in the table. Players who have the same number of points are assigned the same position in the table
            if(player==0){
                prevPoints=String.valueOf(leagueTableData[player][4]);
                //copies various attributes from the league table data in the appropriate format
                leagueDataFormatted[player][0]=String.valueOf(pos);
                leagueDataFormatted[player][1]=leagueTableData[player][0];
                leagueDataFormatted[player][2]=leagueTableData[player][1];leagueDataFormatted[player][3]=leagueTableData[player][2];
                leagueDataFormatted[player][4]=leagueTableData[player][3];leagueDataFormatted[player][5]=leagueTableData[player][4];
            }else{
                if(!prevPoints.equals(leagueTableData[player][4])){
                    pos++;//increments the position if the points of this player are not the same as the points of the previous player
                }
                prevPoints=String.valueOf(leagueTableData[player][4]);
                leagueDataFormatted[player][0]=String.valueOf(pos);
                //copies various attributes from the league table data in the appropriate format
                leagueDataFormatted[player][1]=leagueTableData[player][0];
                leagueDataFormatted[player][2]=leagueTableData[player][1];leagueDataFormatted[player][3]=leagueTableData[player][2];
                leagueDataFormatted[player][4]=leagueTableData[player][3];leagueDataFormatted[player][5]=leagueTableData[player][4];
            }
        }
        JTable tab = new JTable(leagueDataFormatted, COLUMN_NAMES);
        leagueTable.setModel(tab.getModel());//sets the league table to the formatted data
    }
    /**
     * Sets the league table based on the members of the club
     * @param allClubMembers The club members
     */
    private void setTable(String[]allClubMembers){
        //this method is called when the fixtures have not yet been generated. Hence, the table just displays all users as being in first place and having
        //0 points
        String [][]tableData=new String[allClubMembers.length][6];
        for(int i=0;i<tableData.length;i++){
            tableData[i][0]="1";tableData[i][1]=allClubMembers[i];
            tableData[i][2]="0";tableData[i][3]="0";tableData[i][4]="0";
            tableData[i][5]="0";
        }
        JTable tab = new JTable(tableData, COLUMN_NAMES);
        leagueTable.setModel(tab.getModel());//sets the table to the formatted value
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
        backBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        leagueTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("League Table");

        backBtn.setText("Back");
        backBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backBtnActionPerformed(evt);
            }
        });

        leagueTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(leagueTable);

        jLabel1.setText("League Table");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 673, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addComponent(backBtn))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(325, 325, 325)
                                .addComponent(jLabel1)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(backBtn)
                .addGap(24, 24, 24))
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

    private void backBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBtnActionPerformed
        //takes the user back to the chess club form they came from
        if(session.getIsClubLeader()){
            //takes the user back to the club leader form
            WestCrossChessClubLeader leader = new WestCrossChessClubLeader(session);
            leader.setVisible(true);this.dispose();
        }else{
            //takes the user back to the member form
            WestCrossChessClubMember memberForm = new WestCrossChessClubMember(session);
            memberForm.setVisible(true);this.dispose();
        }
    }//GEN-LAST:event_backBtnActionPerformed

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
            java.util.logging.Logger.getLogger(LeagueTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LeagueTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LeagueTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LeagueTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LeagueTable().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable leagueTable;
    // End of variables declaration//GEN-END:variables
}
