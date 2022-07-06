/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package philipm.cs5.software.development.user.system;

import java.awt.Color;
import javax.swing.JOptionPane;
import philipm.cs5.software.development.server.system.UserInfo;


/**
 * Creates a form that allows the leader to view the details of non chess club members. The leader can remove these users from the system as well as add them to the chess club.
 * @author mortimer
 */
public class NonMemberDetails extends javax.swing.JFrame {
    private SessionInfo session;//stores the session information
    private String[][]nonMemberInfo;//stores the data of non-members
    private String[]emails;//stores the email addresses of the users
    private int nonMemberIndex=0;//stores the index which represents which user is currently being viewed
    private boolean isUserInfoBeingEdited=false;//stores whether the user data is currently being edited
    /**
     * Creates new form NonMemberDetails
     * @param session The session information
     * @throws java.lang.Exception An exception may occur when attempting to load info.
     */
    public NonMemberDetails(SessionInfo session) throws Exception{
        initComponents();
        this.session=session;//stores the session information
        //sets look and feel of form
        noUsersLbl.setVisible(false);
        nonMemberInfo=ThreadClient.getAllUsersWhoAreNotMembers(); //gets the data from the server
        emails = new String[nonMemberInfo.length];//stores the email address of all users
        removeBtn.setBackground(Color.red);
        prevBtn.setEnabled(false);nextBtn.setEnabled(nonMemberIndex!=nonMemberInfo.length-1);
        //gets email address of users
        for(int req=0;req<emails.length;req++){
            emails[req]=ThreadClient.getEmailAddressOfUser(nonMemberInfo[req][0]);
        }
        loadFormBasedOnIndex();//loads first user to form if appropriate
    }
    /**
     * Creates new form NonMemberDetails
     */
    private NonMemberDetails(){
        initComponents();
    }
    /**
     * Loads user info onto the form based on the index
     */
    private void loadFormBasedOnIndex(){
        clearForm();//clears the form
        if(nonMemberInfo.length==0){//if there are no users who are not members, the form is updated to reflect this
            noUsersLbl.setVisible(true);
            addUserBtn.setEnabled(false);
            prevBtn.setEnabled(false);nextBtn.setEnabled(false);
            removeBtn.setEnabled(false);editBtn.setEnabled(false);
            return;
        }
        //enables or disables the buttons that allow navigation between users based on the user currently being viewed.
        prevBtn.setEnabled(true);nextBtn.setEnabled(true);
        addUserBtn.setEnabled(true);
        if(nonMemberIndex==0){
            prevBtn.setEnabled(false);
        }
        if(nonMemberIndex==nonMemberInfo.length-1){
            nextBtn.setEnabled(false);
        }
        //ensuers that the leader can't remove themselves
        removeBtn.setEnabled(true);
        if(nonMemberInfo[nonMemberIndex][0].equals(session.getUsername())){
            removeBtn.setEnabled(false);
        }
        //Username, password, first name, surname, postcode, addressline one, address line two, address line three, address line four, discord username, discord number, date of birth, phone number, isClubLeader, isClubMember, ELO, hasUserRequestedToJoinClub
        //sets the form fields with the appropriate values
        usernameTxt.setText(nonMemberInfo[nonMemberIndex][0]);
        emailAddressTxt.setText(emails[nonMemberIndex]);
        firstNameTxt.setText(nonMemberInfo[nonMemberIndex][2]);surnameTxt.setText(nonMemberInfo[nonMemberIndex][3]);
        postcodeTxt.setText(nonMemberInfo[nonMemberIndex][4]);addressLineOneTxt.setText(nonMemberInfo[nonMemberIndex][5]);
        addressLineTwoTxt.setText(nonMemberInfo[nonMemberIndex][6]);addressLineThreeTxt.setText(nonMemberInfo[nonMemberIndex][7]);
        addressLineFourTxt.setText(nonMemberInfo[nonMemberIndex][8]);discordUsernameTxt.setText(nonMemberInfo[nonMemberIndex][9]);
        discordNumberTxt.setText(nonMemberInfo[nonMemberIndex][10]);dateOfBirthTxt.setText(nonMemberInfo[nonMemberIndex][11]);
        phoneNumberTxt.setText(nonMemberInfo[nonMemberIndex][12]);eloTxt.setText(nonMemberInfo[nonMemberIndex][15]);
    }
    /**
     * Sets all fields on the form empty
     */
    private void clearForm(){
        usernameTxt.setText("");emailAddressTxt.setText("");
        firstNameTxt.setText("");surnameTxt.setText("");postcodeTxt.setText("");
        eloTxt.setText("");addressLineOneTxt.setText("");addressLineTwoTxt.setText("");
        addressLineThreeTxt.setText("");addressLineFourTxt.setText("");
        discordUsernameTxt.setText("");discordNumberTxt.setText("");
        dateOfBirthTxt.setText("");phoneNumberTxt.setText("");
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
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        usernameTxt = new javax.swing.JTextField();
        emailAddressTxt = new javax.swing.JTextField();
        firstNameTxt = new javax.swing.JTextField();
        surnameTxt = new javax.swing.JTextField();
        postcodeTxt = new javax.swing.JTextField();
        addressLineOneTxt = new javax.swing.JTextField();
        addressLineTwoTxt = new javax.swing.JTextField();
        addressLineThreeTxt = new javax.swing.JTextField();
        addressLineFourTxt = new javax.swing.JTextField();
        discordUsernameTxt = new javax.swing.JTextField();
        discordNumberTxt = new javax.swing.JTextField();
        dateOfBirthTxt = new javax.swing.JTextField();
        phoneNumberTxt = new javax.swing.JTextField();
        backBtn = new javax.swing.JButton();
        editBtn = new javax.swing.JButton();
        passwordLabel = new javax.swing.JLabel();
        confirmPasswordLabel = new javax.swing.JLabel();
        firstNameLabel = new javax.swing.JLabel();
        surnameLabel = new javax.swing.JLabel();
        postcodeLabel = new javax.swing.JLabel();
        addressOneLabel = new javax.swing.JLabel();
        addressTwoLabel = new javax.swing.JLabel();
        addressThreeLabel = new javax.swing.JLabel();
        addressFourLabel = new javax.swing.JLabel();
        discordUsernameLabel = new javax.swing.JLabel();
        discordNumberLabel = new javax.swing.JLabel();
        dateOfBirthLabel = new javax.swing.JLabel();
        phoneNumberLabel = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        eloTxt = new javax.swing.JTextField();
        removeBtn = new javax.swing.JButton();
        nextBtn = new javax.swing.JButton();
        prevBtn = new javax.swing.JButton();
        addUserBtn = new javax.swing.JButton();
        noUsersLbl = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Non Member Details");

        jLabel1.setText("Username");

        jLabel2.setText("Email Address");

        jLabel5.setText("First Name");

        jLabel6.setText("Surname");

        jLabel7.setText("Postcode");

        jLabel8.setText("Address Line 1");

        jLabel9.setText("Address Line 2");

        jLabel10.setText("Address Line 3");

        jLabel11.setText("Address Line 4");

        jLabel12.setText("Discord Username");

        jLabel13.setText("Discord Number");

        jLabel14.setText("Date of Birth");

        jLabel15.setText("Phone Number");

        usernameTxt.setEditable(false);
        usernameTxt.setFocusCycleRoot(true);
        usernameTxt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                usernameTxtFocusLost(evt);
            }
        });
        usernameTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                usernameTxtKeyTyped(evt);
            }
        });

        emailAddressTxt.setEditable(false);
        emailAddressTxt.setFocusCycleRoot(true);
        emailAddressTxt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                emailAddressTxtFocusLost(evt);
            }
        });

        firstNameTxt.setEditable(false);
        firstNameTxt.setFocusCycleRoot(true);

        surnameTxt.setEditable(false);

        postcodeTxt.setEditable(false);

        addressLineOneTxt.setEditable(false);

        addressLineTwoTxt.setEditable(false);

        addressLineThreeTxt.setEditable(false);

        addressLineFourTxt.setEditable(false);

        discordUsernameTxt.setEditable(false);

        discordNumberTxt.setEditable(false);

        dateOfBirthTxt.setEditable(false);
        dateOfBirthTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dateOfBirthTxtActionPerformed(evt);
            }
        });

        phoneNumberTxt.setEditable(false);

        backBtn.setText("Back");
        backBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backBtnActionPerformed(evt);
            }
        });

        editBtn.setText("Edit Details");
        editBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editBtnActionPerformed(evt);
            }
        });

        passwordLabel.setForeground(new java.awt.Color(255, 51, 51));

        confirmPasswordLabel.setForeground(new java.awt.Color(255, 51, 51));

        firstNameLabel.setForeground(new java.awt.Color(255, 51, 51));

        surnameLabel.setForeground(new java.awt.Color(255, 51, 51));

        postcodeLabel.setForeground(new java.awt.Color(255, 51, 51));

        addressOneLabel.setForeground(new java.awt.Color(255, 51, 51));

        addressTwoLabel.setForeground(new java.awt.Color(255, 51, 51));

        addressThreeLabel.setForeground(new java.awt.Color(255, 51, 51));

        addressFourLabel.setForeground(new java.awt.Color(255, 51, 51));

        discordUsernameLabel.setForeground(new java.awt.Color(255, 51, 51));

        discordNumberLabel.setForeground(new java.awt.Color(255, 51, 51));

        dateOfBirthLabel.setForeground(new java.awt.Color(255, 51, 51));

        phoneNumberLabel.setForeground(new java.awt.Color(255, 51, 51));

        jLabel20.setText("Elo");

        eloTxt.setEditable(false);

        removeBtn.setText("Delete User");
        removeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeBtnActionPerformed(evt);
            }
        });

        nextBtn.setText("Next Member");
        nextBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextBtnActionPerformed(evt);
            }
        });

        prevBtn.setText("Previous Member");
        prevBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prevBtnActionPerformed(evt);
            }
        });

        addUserBtn.setText("Add User To Club");
        addUserBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addUserBtnActionPerformed(evt);
            }
        });

        noUsersLbl.setText("There are no users who are not members.");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addGap(125, 125, 125)
                                        .addComponent(usernameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(emailAddressTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 2, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(firstNameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(surnameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(postcodeTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel20)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(eloTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addComponent(backBtn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(addUserBtn)
                                .addGap(101, 101, 101)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(87, 87, 87)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(passwordLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                                    .addComponent(confirmPasswordLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(prevBtn)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(54, 54, 54)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(firstNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(surnameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(postcodeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(nextBtn)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(noUsersLbl)
                        .addGap(41, 41, 41)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(addressLineTwoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(addressLineOneTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel14)
                                    .addComponent(jLabel13))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(discordNumberTxt)
                                    .addComponent(discordUsernameTxt)
                                    .addComponent(addressLineFourTxt)
                                    .addComponent(addressLineThreeTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addGap(29, 29, 29)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(phoneNumberTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(dateOfBirthTxt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(addressOneLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addressTwoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addressThreeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addressFourLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(discordUsernameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(discordNumberLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dateOfBirthLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(phoneNumberLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(removeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(editBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(60, 60, 60)
                                .addComponent(addressOneLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(addressTwoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(addressThreeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(addressFourLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(discordUsernameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(confirmPasswordLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(19, 19, 19)
                                        .addComponent(firstNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(surnameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel1)
                                            .addComponent(usernameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel2)
                                            .addComponent(emailAddressTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addGap(18, 18, 18)
                                                        .addComponent(passwordLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addGap(24, 24, 24)
                                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                            .addComponent(firstNameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(jLabel5))))
                                                .addGap(27, 27, 27)
                                                .addComponent(surnameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel6)))
                                        .addGap(30, 30, 30)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(postcodeTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel7))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)))))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(discordNumberLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(postcodeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel20)
                                .addComponent(eloTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(dateOfBirthLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(phoneNumberLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(44, 44, 44)
                                .addComponent(noUsersLbl))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(addressLineOneTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(addressLineTwoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(addressLineThreeTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(addressLineFourTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(discordUsernameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(discordNumberTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(dateOfBirthTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(phoneNumberTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(backBtn)
                        .addContainerGap(39, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(editBtn)
                            .addComponent(removeBtn)
                            .addComponent(nextBtn)
                            .addComponent(prevBtn)
                            .addComponent(addUserBtn))
                        .addGap(28, 28, 28))))
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

    private void dateOfBirthTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dateOfBirthTxtActionPerformed

    }//GEN-LAST:event_dateOfBirthTxtActionPerformed

    private void backBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBtnActionPerformed
        //goes back to the leader form
        WestCrossChessClubLeader leader = new WestCrossChessClubLeader(session);
        leader.setVisible(true);this.dispose();
    }//GEN-LAST:event_backBtnActionPerformed

    private void editBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editBtnActionPerformed
        if(isUserInfoBeingEdited){
            //saves changes to user to server
            //checks to see that changes are valid
            //presence check
            if(Validation.containsIllegalStrings(usernameTxt.getText()+emailAddressTxt.getText()+firstNameTxt.getText()+surnameTxt.getText()+postcodeTxt.getText()+addressLineOneTxt.getText()
                    +addressLineTwoTxt.getText()+addressLineThreeTxt.getText()+addressLineFourTxt.getText()+discordNumberTxt.getText()+discordUsernameTxt.getText()+dateOfBirthTxt.getText()+phoneNumberTxt.getText())){//performs a look up check to see whether any field contains an illegal character
                JOptionPane.showMessageDialog(this, "One or more fields contain an illegal character sequence. Here is a list of illegal caharacter sequences: "+Validation.getListOfIllegalStrings(),"Invalid Input",JOptionPane.OK_OPTION);
                return;
            }
            if(Validation.isPresent(firstNameTxt.getText())){//only performs validation if first name is not empty
                if(!Validation.isAppropriateLength(35, firstNameTxt.getText())){//length check - first name can't be more than 35 characters
                    JOptionPane.showMessageDialog(this, "The first name can not be longer than 35 characters","Invalid Input",JOptionPane.OK_OPTION);
                    return;                     
                }
                if(!Validation.consistsOnlyOfLetters(firstNameTxt.getText())){
                    JOptionPane.showMessageDialog(this, "The first name can only contain characters that are in the english alphabet","Invalid Input",JOptionPane.OK_OPTION);
                    return;                  
                }
            }
            if(Validation.isPresent(surnameTxt.getText())){//only performs validation if  surname is not empty
                if(!Validation.isAppropriateLength(40, surnameTxt.getText())){//length check - surname can't be more than 40 characters
                    JOptionPane.showMessageDialog(this, "The surname can not be longer than 40 characters","Invalid Input",JOptionPane.OK_OPTION);
                    return;                     
                }
                if(!Validation.consistsOnlyOfLetters(surnameTxt.getText())){
                    JOptionPane.showMessageDialog(this, "The surname can only contain characters that are in the english alphabet","Invalid Input",JOptionPane.OK_OPTION);
                    return;                  
                }
            }
            if(Validation.isPresent(postcodeTxt.getText())){//only performs validation is postcode is not left empty
                if(!Validation.isValidPostcode(postcodeTxt.getText())){
                    JOptionPane.showMessageDialog(this, "The postcode must be between 6-8 characters long, contain exactly one space and have all non-space characters capitalised","Invalid Input",JOptionPane.OK_OPTION);
                    return;      
                }
            }
            if(!Validation.isAppropriateLength(40, addressLineOneTxt.getText())){//length check
                JOptionPane.showMessageDialog(this, "Each address line can not be longer than 40 characters","Invalid Input",JOptionPane.OK_OPTION);
                return;              
            }
            if(!Validation.isAppropriateLength(40, addressLineTwoTxt.getText())){//length check
                JOptionPane.showMessageDialog(this, "Each address line can not be longer than 40 characters","Invalid Input",JOptionPane.OK_OPTION);
                return;              
            }
            if(!Validation.isAppropriateLength(40, addressLineThreeTxt.getText())){//length check
                JOptionPane.showMessageDialog(this, "Each address line can not be longer than 40 characters","Invalid Input",JOptionPane.OK_OPTION);
                return;              
            }
            if(!Validation.isAppropriateLength(40, addressLineFourTxt.getText())){//length check
                JOptionPane.showMessageDialog(this, "Each address line can not be longer than 40 characters","Invalid Input",JOptionPane.OK_OPTION);
                return;              
            }
            if(!Validation.isAppropriateLength(32, discordUsernameTxt.getText())){
                JOptionPane.showMessageDialog(this, "The Discord username can not be longer than 32 characters","Invalid Input",JOptionPane.OK_OPTION);
                return;              
            }
            if(Validation.isPresent(discordNumberTxt.getText())){
                if(!Validation.isInteger(discordNumberTxt.getText())){
                    JOptionPane.showMessageDialog(this, "The Discord number must be an integer","Invalid Input",JOptionPane.OK_OPTION);
                    return;            
                }
                if(!Validation.isAppropriateLength(4, discordNumberTxt.getText())){
                    JOptionPane.showMessageDialog(this, "The Discord number must be no more than 4 characters","Invalid Input",JOptionPane.OK_OPTION);
                    return;                       
                }
                if(!Validation.rangeCheck(0, 9999, Integer.parseInt(discordNumberTxt.getText()))){//range check
                    JOptionPane.showMessageDialog(this, "The Discord number must be between 0000-9999","Invalid Input",JOptionPane.OK_OPTION);
                    return;                   
                }
            }
            if(Validation.isPresent(dateOfBirthTxt.getText())){
                if(!Validation.formatCheckDateOfBirth(dateOfBirthTxt.getText())){
                    JOptionPane.showMessageDialog(this, "The date of birth must be a valid date of birth in the form dd/mm/yyyy","Invalid Input",JOptionPane.OK_OPTION);
                    return;   
                }
            }
            if(Validation.isPresent(phoneNumberTxt.getText())){
                if(!Validation.isAppropriateLength(20, phoneNumberTxt.getText())){
                    JOptionPane.showMessageDialog(this, "Phone number can not be longer than 20 characters.","Invalid Input",JOptionPane.OK_OPTION);
                    return;                   
                }
                if(!Validation.onlyContainsPhoneNumberCharacters(phoneNumberTxt.getText())){
                    JOptionPane.showMessageDialog(this, "Phone number can only contain plusses, spaces and integers.","Invalid Input",JOptionPane.OK_OPTION);
                    return;                     
                }
            }
            //saves changes having validated them
//Username 0, password 1, first name 2, surname 3, postcode 4, addressline one 5, address line two 6, address line three 7, address line four 8, discord username 9, discord number 10, date of birth 11, phone number 12, isClubLeader 13, isClubMember 14, ELO 15, hasUserRequestedToJoinClub 16
            String[]newRecord=new String[]{usernameTxt.getText(),nonMemberInfo[nonMemberIndex][1],firstNameTxt.getText(),surnameTxt.getText(),postcodeTxt.getText(),addressLineOneTxt.getText(),addressLineTwoTxt.getText(),addressLineThreeTxt.getText(),addressLineFourTxt.getText(),discordUsernameTxt.getText(),discordNumberTxt.getText(),dateOfBirthTxt.getText(),phoneNumberTxt.getText(),nonMemberInfo[nonMemberIndex][13],nonMemberInfo[nonMemberIndex][14],nonMemberInfo[nonMemberIndex][15],nonMemberInfo[nonMemberIndex][16]};
            for(int x=0;x<UserInfo.WIDTH_OF_USER_DETAILS_FILE;x++){
                nonMemberInfo[nonMemberIndex][x]=newRecord[x];
            }
            try{
                ThreadClient.updateUserInfo(newRecord);//saves changes made
            }catch(Exception e){
                JOptionPane.showMessageDialog(this,"Error when communicating with the server: "+e,"Error",JOptionPane.OK_OPTION);
                Menu menu = new Menu(session);menu.setVisible(true);this.dispose();return;
            }
            //updates look and feel of form and exits editing mode
            isUserInfoBeingEdited=false;
            //updates components
            prevBtn.setEnabled(true);nextBtn.setEnabled(true);
            removeBtn.setEnabled(true);addUserBtn.setEnabled(true);
            firstNameTxt.setEditable(false);surnameTxt.setEditable(false);
            postcodeTxt.setEditable(false);
            addressLineOneTxt.setEditable(false);addressLineTwoTxt.setEditable(false);addressLineThreeTxt.setEditable(false);
            addressLineFourTxt.setEditable(false);discordUsernameTxt.setEditable(false);discordNumberTxt.setEditable(false);
            dateOfBirthTxt.setEditable(false);phoneNumberTxt.setEditable(false);
            loadFormBasedOnIndex();//updates form
            editBtn.setText("Edit Details");
            removeBtn.setText("Delete User");
            removeBtn.setBackground(Color.red);
        }else{
            //puts form in editing mode - updating various components and variables accordingly.
            editBtn.setText("Save Changes");
            removeBtn.setText("Cancel Changes");
            isUserInfoBeingEdited=true;
            prevBtn.setEnabled(false);nextBtn.setEnabled(false);
            addUserBtn.setEnabled(false);
            removeBtn.setEnabled(true);removeBtn.setBackground(nextBtn.getBackground());
            //sets fields to be editable
            firstNameTxt.setEditable(true);surnameTxt.setEditable(true);
            postcodeTxt.setEditable(true);
            addressLineOneTxt.setEditable(true);addressLineTwoTxt.setEditable(true);addressLineThreeTxt.setEditable(true);
            addressLineFourTxt.setEditable(true);discordUsernameTxt.setEditable(true);discordNumberTxt.setEditable(true);
            dateOfBirthTxt.setEditable(true);phoneNumberTxt.setEditable(true);
        }
    }//GEN-LAST:event_editBtnActionPerformed

    private void usernameTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_usernameTxtKeyTyped
        
    }//GEN-LAST:event_usernameTxtKeyTyped

    private void usernameTxtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_usernameTxtFocusLost

    }//GEN-LAST:event_usernameTxtFocusLost

    private void emailAddressTxtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_emailAddressTxtFocusLost

    }//GEN-LAST:event_emailAddressTxtFocusLost

    private void removeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeBtnActionPerformed
        //removes members or cancels changes made when editing
        if(isUserInfoBeingEdited){
            //exits editing mode and cancels and changes made whilst editing the user
            //updates look and feel of form to how it was before editing started
            isUserInfoBeingEdited=false;
            prevBtn.setEnabled(true);nextBtn.setEnabled(true);
            removeBtn.setEnabled(true);
            firstNameTxt.setEditable(false);surnameTxt.setEditable(false);
            postcodeTxt.setEditable(false);
            addressLineOneTxt.setEditable(false);addressLineTwoTxt.setEditable(false);addressLineThreeTxt.setEditable(false);
            addressLineFourTxt.setEditable(false);discordUsernameTxt.setEditable(false);discordNumberTxt.setEditable(false);
            dateOfBirthTxt.setEditable(false);phoneNumberTxt.setEditable(false);
            loadFormBasedOnIndex();
            editBtn.setText("Edit Details");
            removeBtn.setText("Delete User"); 
            removeBtn.setBackground(Color.red);
            addUserBtn.setEnabled(true);
            return;
        }else{
            //removes the user from the entire system 
            //asks for confirmation to delete user
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you would like to delete this account", "Delete?", JOptionPane.YES_NO_CANCEL_OPTION);
            if(confirm==JOptionPane.YES_OPTION){
                try{
                    ThreadClient.deleteUserInfoOfNonMemberUserFromServer(nonMemberInfo[nonMemberIndex][0]);//deletes the account
                }catch(Exception e){
                    JOptionPane.showMessageDialog(this,"Error when communicating with the server: "+e,"Error",JOptionPane.OK_OPTION);
                    Menu menu = new Menu(session);menu.setVisible(true);this.dispose();return;
                }
                removeCurrentIndexUserFromArrayAndSetIndexToZero();//updates the array without the user
                loadFormBasedOnIndex();//loads the updated form
            }
        }
    }//GEN-LAST:event_removeBtnActionPerformed

    private void prevBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prevBtnActionPerformed
        // takes the leader to the previous user
        nonMemberIndex--;
        loadFormBasedOnIndex();
    }//GEN-LAST:event_prevBtnActionPerformed

    private void nextBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextBtnActionPerformed
        // takes the leader to the next user
        nonMemberIndex++;
        loadFormBasedOnIndex();
    }//GEN-LAST:event_nextBtnActionPerformed

    private void addUserBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addUserBtnActionPerformed
        // adds the user to the club
        //asks for confirmation before adding the user to the club
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you would like to add this user to the club?", "Add?", JOptionPane.YES_NO_CANCEL_OPTION);        
        if(confirm==JOptionPane.YES_OPTION){
            //adds user to the chess club
            try{
                ThreadClient.addUserToClubAsMember(usernameTxt.getText());//adds the user to the club
            }catch(Exception e){
                JOptionPane.showMessageDialog(this,"Error when communicating with the server: "+e,"Error",JOptionPane.OK_OPTION);
                Menu menu = new Menu(session);menu.setVisible(true);this.dispose();return;                    
            }
            removeCurrentIndexUserFromArrayAndSetIndexToZero();//removes the user from the array
            loadFormBasedOnIndex();//updates the form without the user
        }
    }//GEN-LAST:event_addUserBtnActionPerformed
    /**
     * Removes the user info of the user at the current index from the array and form. The index is then set to zero.
     */
    private void removeCurrentIndexUserFromArrayAndSetIndexToZero(){
        //removes user info from array
        String[][]newClubMemberInfo=new String[nonMemberInfo.length-1][UserInfo.WIDTH_OF_USER_DETAILS_FILE];
        for(int i=0;i<nonMemberIndex;i++){
            for(int x=0;x<UserInfo.WIDTH_OF_USER_DETAILS_FILE;x++){
                newClubMemberInfo[i][x]=nonMemberInfo[i][x];
            }
        }
        for(int i=nonMemberIndex+1;i<nonMemberInfo.length;i++){
            for(int x=0;x<UserInfo.WIDTH_OF_USER_DETAILS_FILE;x++){
                newClubMemberInfo[i-1][x]=nonMemberInfo[i][x];
            }                    
        }
        //removes email from array
        String[]newEmail=new String[emails.length-1];
        for(int i=0;i<nonMemberIndex;i++){
            newEmail[i]=emails[i];
        }
        for(int i=nonMemberIndex+1;i<emails.length;i++){
            newEmail[i-1]=emails[i];
        }
        emails=newEmail;
        nonMemberInfo=newClubMemberInfo;
        nonMemberIndex=0;//sets index to zero
    }
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
            java.util.logging.Logger.getLogger(NonMemberDetails.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NonMemberDetails.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NonMemberDetails.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NonMemberDetails.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NonMemberDetails().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addUserBtn;
    private javax.swing.JLabel addressFourLabel;
    private javax.swing.JTextField addressLineFourTxt;
    private javax.swing.JTextField addressLineOneTxt;
    private javax.swing.JTextField addressLineThreeTxt;
    private javax.swing.JTextField addressLineTwoTxt;
    private javax.swing.JLabel addressOneLabel;
    private javax.swing.JLabel addressThreeLabel;
    private javax.swing.JLabel addressTwoLabel;
    private javax.swing.JButton backBtn;
    private javax.swing.JLabel confirmPasswordLabel;
    private javax.swing.JLabel dateOfBirthLabel;
    private javax.swing.JTextField dateOfBirthTxt;
    private javax.swing.JLabel discordNumberLabel;
    private javax.swing.JTextField discordNumberTxt;
    private javax.swing.JLabel discordUsernameLabel;
    private javax.swing.JTextField discordUsernameTxt;
    private javax.swing.JButton editBtn;
    private javax.swing.JTextField eloTxt;
    private javax.swing.JTextField emailAddressTxt;
    private javax.swing.JLabel firstNameLabel;
    private javax.swing.JTextField firstNameTxt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton nextBtn;
    private javax.swing.JLabel noUsersLbl;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JLabel phoneNumberLabel;
    private javax.swing.JTextField phoneNumberTxt;
    private javax.swing.JLabel postcodeLabel;
    private javax.swing.JTextField postcodeTxt;
    private javax.swing.JButton prevBtn;
    private javax.swing.JButton removeBtn;
    private javax.swing.JLabel surnameLabel;
    private javax.swing.JTextField surnameTxt;
    private javax.swing.JTextField usernameTxt;
    // End of variables declaration//GEN-END:variables
}
