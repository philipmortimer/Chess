/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package philipm.cs5.software.development.user.system;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 * Creates a new form which allows the user to change the Internet settings (i.e. the server address and port number).
 * @author mortimer
 */
public class ChangeInternetSettings extends javax.swing.JFrame {

    /**
     * Creates new form ChangeInternetSettings
     */
    private boolean isInEditMode=false;//stores whether the values are currently being edited
    private static final String DEFAULT_PORT_NUMBER="80";//stores default port number
    private static final String DEFUALT_SERVER_ADDRESS="86.129.93.149";//stores defualt address of server
    private String bufferPortNumber;//stores the most recent port number
    private String bufferServerAddress;//stores the most recent server address
    private static final String PORT_NUMBER_FILE_NAME="portNumber.txt";
    private static final String SERVER_ADDRESS_FILE_NAME="serverAddress.txt";
    public ChangeInternetSettings() {
        //initilaises components and updates form based on stored address and port number
        initComponents();
        updateFormBasedOnWhetherInEditingMode();
        loadPortNumAndServerAddressOnForm();
        bufferPortNumber=portNumberTxt.getText();bufferServerAddress=serverAddressTxt.getText();
    }
    /**
     * Loads the port number and server address onto the form
     */
    private void loadPortNumAndServerAddressOnForm(){
        portNumberTxt.setText(getPortNumberString());
        serverAddressTxt.setText(getServerAddressNonStatic());
    }
    /**
     * Gets the port number as a string
     * @return returns the port number stored in the text file. An empty string is returned if an error occurs.
     */
    public String getPortNumberString(){
        try{
            FileReader read = new FileReader(PORT_NUMBER_FILE_NAME);//reads the port number from the file
            BufferedReader buffRead = new BufferedReader(read);
            String ret = buffRead.readLine();
            buffRead.close();read.close();
            return ret;
        }catch(IOException e){
            JOptionPane.showMessageDialog(this, "An error occured with "+PORT_NUMBER_FILE_NAME, "Error", JOptionPane.OK_OPTION);
        }
        return "";
    }
    /**
     * Gets the port number
     * @return The port number as an integer. If an error occurs, an error message is displayed and -1 is returned.
     */
    public static int getPortNumber(){
        try{
            FileReader read = new FileReader(PORT_NUMBER_FILE_NAME);//reads the port number from the file
            BufferedReader buffRead = new BufferedReader(read);
            String portStr=buffRead.readLine();
            buffRead.close();read.close();
            try{
                int port = Integer.parseInt(portStr);
                return port;
            }catch(NumberFormatException ex){
                JOptionPane.showMessageDialog(null, "The port number stored is not an integer. Please fix this by going into settings.", "Error", JOptionPane.OK_OPTION);
                return -1;
            }
        }catch(IOException e){
            JOptionPane.showMessageDialog(null, "An error occured with "+PORT_NUMBER_FILE_NAME+". Error details: "+e, "Error", JOptionPane.OK_OPTION);
        }
        return -1;
    }
    /**
     * Gets the server address
     * @return The server address. If an error occurs, an appropriate message is displayed and an empty string is returned.
     */
    public static String getServerAddress(){
        try{
            FileReader read = new FileReader(SERVER_ADDRESS_FILE_NAME);//reads the server address from the file
            BufferedReader buffRead = new BufferedReader(read);
            String ret = buffRead.readLine();
            buffRead.close();read.close();
            return ret;
        }catch(IOException e){
            JOptionPane.showMessageDialog(null, "An error occured with "+SERVER_ADDRESS_FILE_NAME+". Error details: "+e, "Error", JOptionPane.OK_OPTION);
            return "";
        }
    }
    /**
     * Gets the server address
     * @return The server address. If an error occurs, an appropriate message is displayed and an empty string is returned.
     */
    private String getServerAddressNonStatic(){
        try{
            FileReader read = new FileReader(SERVER_ADDRESS_FILE_NAME);//reads the server address from the file
            BufferedReader buffRead = new BufferedReader(read);
            String ret = buffRead.readLine();
            buffRead.close();read.close();
            return ret;
        }catch(IOException e){
            JOptionPane.showMessageDialog(this, "An error occured with "+SERVER_ADDRESS_FILE_NAME+". Error details: "+e, "Error", JOptionPane.OK_OPTION);
            return "";
        }        
    }
    /**
     * Alters relevant attributes of various buttons and text fields based on whether the system is currently in editing mode
     */
    private void updateFormBasedOnWhetherInEditingMode(){
        editBtn.setEnabled(!isInEditMode);editBtn.setVisible(!isInEditMode);
        cancelBtn.setEnabled(isInEditMode);cancelBtn.setVisible(isInEditMode);
        saveChangesBtn.setEnabled(isInEditMode);saveChangesBtn.setVisible(isInEditMode);
        revertToDefaultBtn.setEnabled(isInEditMode);revertToDefaultBtn.setVisible(isInEditMode);
        portNumberTxt.setEditable(isInEditMode);serverAddressTxt.setEditable(isInEditMode);
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
        portNumberTxt = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        serverAddressTxt = new javax.swing.JTextField();
        backBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();
        saveChangesBtn = new javax.swing.JButton();
        editBtn = new javax.swing.JButton();
        revertToDefaultBtn = new javax.swing.JButton();
        moreInfoBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Change Online Settings");

        jLabel1.setText("Port Number");

        portNumberTxt.setEditable(false);

        jLabel2.setText("Server Address");

        serverAddressTxt.setEditable(false);

        backBtn.setText("Back");
        backBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backBtnActionPerformed(evt);
            }
        });

        cancelBtn.setText("Cancel Changes");
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });

        saveChangesBtn.setText("Save Changes");
        saveChangesBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveChangesBtnActionPerformed(evt);
            }
        });

        editBtn.setText("Edit");
        editBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editBtnActionPerformed(evt);
            }
        });

        revertToDefaultBtn.setText("Revert to Default Values");
        revertToDefaultBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                revertToDefaultBtnActionPerformed(evt);
            }
        });

        moreInfoBtn.setText("Further Information");
        moreInfoBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moreInfoBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(backBtn)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(46, 46, 46)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(portNumberTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(serverAddressTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(87, 87, 87)
                                .addComponent(editBtn)
                                .addGap(56, 56, 56)
                                .addComponent(moreInfoBtn))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(cancelBtn)
                        .addGap(29, 29, 29)
                        .addComponent(saveChangesBtn)
                        .addGap(18, 18, 18)
                        .addComponent(revertToDefaultBtn)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(portNumberTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(45, 45, 45)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(serverAddressTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 86, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelBtn)
                    .addComponent(saveChangesBtn)
                    .addComponent(revertToDefaultBtn))
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(backBtn)
                    .addComponent(editBtn)
                    .addComponent(moreInfoBtn))
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
    /**
     * changes the editing mode of the form and updates the form accordingly
     * @param evt The edit button being pressed
     */
    private void editBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editBtnActionPerformed
        //updates editing mode and form accordingly
        bufferPortNumber=portNumberTxt.getText();bufferServerAddress=serverAddressTxt.getText();
        isInEditMode=!isInEditMode;
        updateFormBasedOnWhetherInEditingMode();
    }//GEN-LAST:event_editBtnActionPerformed
    /**
     * Sets the port number and server address text fields to their default values
     * @param evt The revert button being pressed
     */
    private void revertToDefaultBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_revertToDefaultBtnActionPerformed
       //reverts the server to the hard coded default values
        portNumberTxt.setText(DEFAULT_PORT_NUMBER);
        serverAddressTxt.setText(DEFUALT_SERVER_ADDRESS);
    }//GEN-LAST:event_revertToDefaultBtnActionPerformed
    /**
     * Displays further information about what port number and server address mean and how a user may go about fixing issues involving these.
     * @param evt The more information button being pressed
     */
    private void moreInfoBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moreInfoBtnActionPerformed
        //displays some info on what the port number and server address actually mean.
        JOptionPane.showMessageDialog(this, "The port number and server address are what allow users to access online features. Please speak to the server owner to find the correct details. Normally, this will be the chess club leader. ","Further Information",JOptionPane.OK_OPTION);
    }//GEN-LAST:event_moreInfoBtnActionPerformed
/**
 * Saves the inputted changes after appropriate validation.
 * @param evt The save button being pressed
 */
    private void saveChangesBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveChangesBtnActionPerformed
        try{
            int port = Integer.parseInt(portNumberTxt.getText());
            if(port<=0){//range check
                JOptionPane.showMessageDialog(this, "The port number must be a positive integer greater than zero.","Error",JOptionPane.OK_OPTION);
                return;
            }
        }catch(NumberFormatException ex){//type check
            JOptionPane.showMessageDialog(this, "The port number must be a positive integer greater than zero.","Error",JOptionPane.OK_OPTION);
            return;
        }
        try{
            FileWriter writePort = new FileWriter(PORT_NUMBER_FILE_NAME,false);//writes port number and server address to their files
            BufferedWriter buffWritePort = new BufferedWriter(writePort);
            buffWritePort.write(portNumberTxt.getText());
            buffWritePort.flush();writePort.flush();
            buffWritePort.close();writePort.close();
            FileWriter writeServ = new FileWriter(SERVER_ADDRESS_FILE_NAME,false);
            BufferedWriter buffWriteServ = new BufferedWriter(writeServ);
            buffWriteServ.write(serverAddressTxt.getText());
            buffWriteServ.flush();writeServ.flush();
            buffWriteServ.close();writeServ.close();
        }catch(IOException e){
            JOptionPane.showMessageDialog(this, "An unexpected error occured with either "+SERVER_ADDRESS_FILE_NAME+" or "+PORT_NUMBER_FILE_NAME+". Error Details "+e,"Error",JOptionPane.OK_OPTION);
        }
        isInEditMode=!isInEditMode;updateFormBasedOnWhetherInEditingMode();//changes editing mode and updates form accordingly
    }//GEN-LAST:event_saveChangesBtnActionPerformed
    /**
     * Sets the text fields to the values they were set at before editing mode was activated
     * @param evt The cancel button being pressed
     */
    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        //exits editing mode and reverts the values on the form to what they were before editing began
        isInEditMode=!isInEditMode;updateFormBasedOnWhetherInEditingMode();
        portNumberTxt.setText(bufferPortNumber);serverAddressTxt.setText(bufferServerAddress);
    }//GEN-LAST:event_cancelBtnActionPerformed

    private void backBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBtnActionPerformed
        //takes the user back to the log in page, reminding them that they may wish to save any changes made first.
        if(isInEditMode){
            int confirm= JOptionPane.showConfirmDialog(this, "Are you sure you would like to go back. Your unsaved changes will be lost if you go back now. Are you sure you would like to continue?","Continue",JOptionPane.OK_OPTION);
            if(confirm==JOptionPane.YES_OPTION){
                LogIn log = new LogIn();
                log.setVisible(true);
                this.dispose();
            }else{
                return;
            }
        }else{
            LogIn log = new LogIn();
            log.setVisible(true);
            this.dispose();            
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
            java.util.logging.Logger.getLogger(ChangeInternetSettings.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChangeInternetSettings.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChangeInternetSettings.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChangeInternetSettings.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ChangeInternetSettings().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backBtn;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JButton editBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton moreInfoBtn;
    private javax.swing.JTextField portNumberTxt;
    private javax.swing.JButton revertToDefaultBtn;
    private javax.swing.JButton saveChangesBtn;
    private javax.swing.JTextField serverAddressTxt;
    // End of variables declaration//GEN-END:variables
}
