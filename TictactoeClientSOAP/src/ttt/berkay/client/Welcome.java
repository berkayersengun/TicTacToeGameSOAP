/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ttt.berkay.client;

/**
 *
 * @author berkayersengun
 */

import javax.swing.*;
import javax.xml.rpc.ServiceException;

import ttt.berkay.server.TictactoeServer;
import ttt.berkay.server.TictactoeServerServiceLocator;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Welcome  {

    private JPanel panel;
    private JFrame frame ;
    private JButton newButton;
    private JButton existingButton;
    private JLabel label;
    
    private TictactoeServer proxy;
    private TictactoeServerServiceLocator tictactoeServerServiceLocator;

    //welcome window to choose existing user or new user
    public Welcome() {
        
    	  //Web service connection
    	tictactoeServerServiceLocator = new TictactoeServerServiceLocator();
    	try {
			proxy = tictactoeServerServiceLocator.getTictactoeServerPort();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
        GridBagLayout grid = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        panel = new JPanel(grid);
        panel.setBackground(Color.GRAY);
        panel.setVisible(true);
        label= new JLabel("Tic Tac Toe");

        newButton = new JButton("New User");
        newButton.addActionListener(new newUserListener());

        existingButton = new JButton("Existing User");
        existingButton.addActionListener(new existingUserListener());
        c.insets = new Insets(2,2,2,2);

        c.gridx = 0 ;
        c.ipady = 10 ;
        c.gridy = 0;
        panel.add(label,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 1;
        c.ipady = 6 ;
        panel.add(newButton,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 2;
        panel.add(existingButton,c);

        c.gridy = 4;

        frame = new JFrame("Tic Tac Toe");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(200,175);
        frame.add(panel);


    }


     private class newUserListener implements ActionListener{
        public void actionPerformed(ActionEvent e ){

            frame.dispose();
                
            
            try {
                new Registration(proxy);
            } catch (IOException ex) {
                Logger.getLogger(Welcome.class.getName()).log(Level.SEVERE, null, ex);
            }
                
          
               
            
           
        }

    }

    private class existingUserListener implements ActionListener{
        public void actionPerformed(ActionEvent e ){
            frame.dispose();
      
            try {
                new Login(proxy);
            } catch (IOException ex) {
                Logger.getLogger(Welcome.class.getName()).log(Level.SEVERE, null, ex);
            }
               
          

        }

    }

    public static void main (String[] arg) throws IOException, ClassNotFoundException{

       new Welcome();



    }
}
