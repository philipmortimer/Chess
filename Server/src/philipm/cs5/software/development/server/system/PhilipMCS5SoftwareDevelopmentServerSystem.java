/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package philipm.cs5.software.development.server.system;



/**
 * The main class of the server system, used to create the server form.
 * @author mortimer
 */
public class PhilipMCS5SoftwareDevelopmentServerSystem {

    /**
     * The main method that loads the server form
     * @param args the command line arguments
     */
    public static void main(String[] args) {  
        ServerUI server = new ServerUI();//creates a server form
        server.setVisible(true);
    }
    
}
