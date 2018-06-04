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

import ttt.berkay.server.TictactoeServer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;



public class Registration {

    private JPanel panel;
    private JFrame frame;
    private JButton buttonSubmit,buttonClear;
    private JTextField fieldName,fieldSurname,fieldUsername,fieldPassword ;
    private JLabel labelName,labelSurname,labelUsername,labelPassword,labelRegistration;
    private String name,surname,username,password;

    private Socket socket ;
    private InputStream is ;
    private ObjectInputStream ois;
    private OutputStream os;
    private ObjectOutputStream oos;
    private PrintWriter printWriter;
    private InputStreamReader inputStreamReader;
    private BufferedReader bufferedReader;
    private TictactoeServer proxy;
 
    public Registration(TictactoeServer proxy) throws IOException {
        this.proxy=proxy;
    
        GridBagLayout grid = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        panel = new JPanel(grid);
        panel.setBackground(Color.GRAY);
        panel.setVisible(true);

        labelRegistration = new JLabel("Registration");
        labelName = new JLabel("Name");
        labelSurname = new JLabel("Surname");
        labelUsername = new JLabel("Username");
        labelPassword = new JLabel("Password");
        

        fieldName = new JTextField(15);
        fieldSurname = new JTextField(15);
        fieldUsername = new JTextField(15);
        fieldPassword = new JTextField(15);
        

        buttonSubmit = new JButton("Submit");
        buttonSubmit.addActionListener(new submitListener());

        buttonClear = new JButton( "Clear");
        buttonClear.addActionListener(new clearListener());

        c.insets = new Insets(3,8,3,8);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1 ;
        c.gridy = 0 ;
        c.ipady =10;
        panel.add(labelRegistration,c);

        c.ipady =0;
        c.ipadx = 20 ;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        panel.add(labelName,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 3;
        panel.add(fieldName,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        panel.add(labelSurname,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 2;
        panel.add(fieldSurname,c);

        c.ipady =0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 3;
        panel.add(labelUsername,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 3;
        panel.add(fieldUsername,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 4;
        panel.add(labelPassword,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 4;
        panel.add(fieldPassword,c);

//        c.fill = GridBagConstraints.HORIZONTAL;
//        c.gridx = 0;
//        c.gridy = 5;
//        panel.add(labelEmail,c);
//
//        c.fill = GridBagConstraints.HORIZONTAL;
//        c.gridx = 1;
//        c.gridy = 5;
//        panel.add(fieldEmail,c);

        c.fill = GridBagConstraints.CENTER;
        //c.anchor = GridBagConstraints.CENTER;
        c.ipadx = 15;
        c.gridy = 6 ;
        c.gridx = 1 ;
        c.gridwidth =1;
        panel.add(buttonSubmit,c);
        
        c.fill = GridBagConstraints.CENTER;
        c.ipadx = 15;
        c.gridy = 6 ;
        c.gridx = 2  ;
        c.gridwidth = 1 ;
        panel.add(buttonClear,c);



        frame = new JFrame("Registration Window");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setSize(350,250);
        frame.add(panel);

    }
    private class submitListener implements ActionListener {
        public void actionPerformed(ActionEvent event ) {

            name = fieldName.getText();
            surname = fieldSurname.getText();
            username = fieldUsername.getText();
            password = fieldPassword.getText();

            String check;
			try {
				check = proxy.register(username, password, name, surname);
			

//            if (check.equals("ERROR-REPEAT"))
//                System.out.println("This username already exists, Please try another one. ");
//            
//            else if (check.equals("ERROR-INSERT"))
//                System.out.println("Information couldnt been inserted.");
//            
//             else if (check.equals("ERROR-RETRIEVE"))
//                 System.out.println("Data couldnt retrieved from database.");
//            
//            else if (check.equals("ERROR-DB"))
//                 System.out.println("Database connection problem, please try again later.");
//            
//            else{ 
//                
//                    System.out.println("User added to database with the id number of "+ check);
//                    frame.dispose();
//            }
            
               switch (check){
                   
                   case "ERROR-REPEAT": 
                    
                        System.out.println("This username already exists, Please try another one. ");
                        break;
                   
                   case "ERROR-INSERT":
                       
                       System.out.println("Information couldnt been inserted.");
                       break;
                       
                   case "ERROR-RETRIEVE":
                       
                       System.out.println("Data couldnt retrieved from database.");
                       break;
                       
                   case "ERROR-DB":
                       
                       System.out.println("Database connection problem, please try again later.");
                       break;
                       
                   default:
                       
                       System.out.println("User added to database with the id number of "+ check);
                       frame.dispose();
            {
                try {
                    new Login(proxy);
                } catch (IOException ex) {
                    Logger.getLogger(Registration.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
                       break;
         
                        
        }

			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        }

    }

    private class clearListener implements ActionListener{
        public void actionPerformed(ActionEvent e ){

            fieldName.setText("");
            fieldSurname.setText("");
            fieldUsername.setText("");
            fieldPassword.setText("");
           

        }

    }

  



}

