/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ttt.berkay.client;
import javax.swing.*;
import javax.swing.text.DefaultCaret;

import ttt.berkay.server.TictactoeServer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author berkayersengun
 */
public class Game implements Runnable{
    
    private JPanel mainPanel,infoPanel,statusPanel;
    private JFrame frame;
    private JButton buttonExit;
    private JButton[][] button = new JButton[3][3];
    private JTextArea jTextArea ;
    private JLabel infoLabel;
    private JScrollPane scrollPane ;
    private ImageIcon imageO = new ImageIcon("images/tic-tac-toe-o.png");
    private ImageIcon imageX = new ImageIcon("images/tic-tac-toe-x.png");
    private sendButton send ;
    private TictactoeServer proxy;

   
    private int pid,gid ;
    private String side,username,opponent;
    //private int[][] boardArray =  new int[3][3];
   
    
    //private String[] row;
   // private String[] each;
  //  private String[] data;
    
     private boolean wait=false;
    private boolean run=true ;
    private boolean listen=true;
   


    //constructor for new game
    public Game( String username , int pid, String side,int gid, TictactoeServer proxy) throws IOException {
        this.proxy = proxy;
        this.username = username;
        this.side = side;
        this.gid = gid ;
        this.pid=pid;
      

        infoPanel = new JPanel();
        infoLabel = new JLabel("Player: " + username + " - Side: " + side );
        infoPanel.add(infoLabel);
        
        buttonExit = new JButton("Exit");
        buttonExit.addActionListener(new buttonExit());
        

        statusPanel = new JPanel();
        statusPanel.setPreferredSize(new Dimension(500,200));
        jTextArea = new JTextArea();
        jTextArea.setEditable(false);
        //go to nextline when reach end of line text wrap
        jTextArea.setLineWrap(true);
        //autoscrolling as it is written
        DefaultCaret caret = (DefaultCaret)jTextArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        scrollPane = new JScrollPane(jTextArea);
        statusPanel.setLayout(new BoxLayout(statusPanel,BoxLayout.Y_AXIS));
        buttonExit.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusPanel.add(scrollPane);
        statusPanel.add(buttonExit);

        jTextArea.append("You are playing as "+side +"\n");
        jTextArea.append("You created a new game with No: "+ gid+ "\n");
        jTextArea.append("Waiting for other player to enter the game...\n");


        GridLayout grid = new GridLayout(3,3,4,4);
        mainPanel = new JPanel();
        mainPanel.setLayout(grid);
        mainPanel.setBackground(Color.GRAY);
        mainPanel.setVisible(true);
        
        send = new sendButton();
        
        for (int y=0; y<3; y++) {
            for (int x=0; x<3; x++) {

                button[y][x] = new JButton();
                mainPanel.add(button[y][x]);

//                button[y][x].repaint();
                button[y][x].addActionListener(send);
                button[y][x].setActionCommand(String.valueOf(y)+String.valueOf(x));

            }
        }
        
      

        frame = new JFrame("Tic Tac Toe");
        
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
           frame.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
             //stop listening and close threat
              run=false;
              //delete the game if no one entered
              try {
				proxy.deleteGame(gid, pid);
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
                
            }
        });

        frame.setSize(450,600);
        frame.add(BorderLayout.CENTER,mainPanel);
        frame.add(BorderLayout.NORTH,infoPanel);
        frame.add(BorderLayout.SOUTH,statusPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    
}
    
//constructor for a game to join
   public Game( String username , int pid, String side,int gid,String opponent,TictactoeServer proxy) throws IOException {
        
	   this.proxy = proxy;
        this.username = username;
        this.side = side;
        this.gid = gid ;
        this.pid = pid;
        this.opponent = opponent;
    
        infoPanel = new JPanel();
        infoLabel = new JLabel("Player: " + username + " - Side: " + side );
        infoPanel.add(infoLabel);
        
        buttonExit = new JButton("Exit");
        buttonExit.addActionListener(new buttonExit());
  

        statusPanel = new JPanel();
        statusPanel.setLayout(new BorderLayout());
        statusPanel.setPreferredSize(new Dimension(500,200));
        jTextArea = new JTextArea();
        jTextArea.setEditable(false);
        //go to nextline when reach end of line text wrap
        jTextArea.setLineWrap(true);
        //autoscrolling as it is written
        DefaultCaret caret = (DefaultCaret)jTextArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        scrollPane = new JScrollPane(jTextArea);
        statusPanel.setLayout(new BoxLayout(statusPanel,BoxLayout.Y_AXIS));
        buttonExit.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusPanel.add(scrollPane);
        statusPanel.add(buttonExit);

        jTextArea.append("You are playing as "+ side +  "\n");
        jTextArea.append("You entered the game No: " + gid +"\n");
        jTextArea.append("Your opponent is "+ opponent+ "\n");
        jTextArea.append("Game started\n");
        jTextArea.append("Waiting for " + opponent + "'s move.\n");
    

        
        GridLayout grid = new GridLayout(3,3,4,4);
        mainPanel = new JPanel();
        mainPanel.setLayout(grid);
        mainPanel.setBackground(Color.GRAY);
        mainPanel.setVisible(true);
        
        send = new sendButton();
        for (int y=0; y<3; y++) {
            for (int x=0; x<3; x++) {

                button[y][x] = new JButton();
                mainPanel.add(button[y][x]);

                button[y][x].addActionListener(send);
                button[y][x].setActionCommand(String.valueOf(y)+String.valueOf(x));

            }
        }
        
        //entering a game already partly played!!
        
//        String[] board = proxy.getBoard(gid).split("\n");
//        if(board.length != 0){
//            System.out.println(board.length);
//            
//            String[] allRows = proxy.showAllMyGames(pid).split("\n");
//            String[] one = new String[allRows.length];
//            //loop for the opponent username
//                for(int i=0; i<allRows.length; i++){
//                    one = allRows[i].split(",");
//                        //if same gid set the side for the game to join
//                        if(Integer.valueOf(one[0]) == gid){
//                               
//                            if(Integer.valueOf(one[1])==pid)
//                                this.side="X";
//                            else if(Integer.valueOf(one[2])==pid)
//                                this.side="O";
//                              
//                            
//                            }
//                         
//                     }
//            
//            
//            String[] oneRow = new String[board.length];
//            for (int b=0; b<board.length; b++){
//                oneRow = board[b].split(",");
//                    
//                    if(Integer.valueOf(oneRow[0]) == pid){
//                        
//                        if(this.side.equals("X"))
//                            button[Integer.valueOf(oneRow[2])][Integer.valueOf(oneRow[1])].setIcon(imageX);
//                        else if(side.equals("O"))
//                            button[Integer.valueOf(oneRow[2])][Integer.valueOf(oneRow[1])].setIcon(imageO);
//                    }
//                
//            }
//            
//        }

        frame = new JFrame("Tic Tac Toe");
        
           frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
           frame.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
             //stop listening and close threat
              run=false;
              //delete the game if no one entered
              try {
				proxy.deleteGame(gid, pid);
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
                
            }
        });
           
        frame.setSize(450,600);
        frame.add(BorderLayout.CENTER,mainPanel);
        frame.add(BorderLayout.NORTH,infoPanel);
        frame.add(BorderLayout.SOUTH,statusPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


    }

    @Override
    public void run() {
       
       
        boolean t = true;
        int append;
        String[]  data = new String[3];
         
       while(run){  
    	   try {
           
        if (  t && side.equals("X") ){
               append = Integer.parseInt(proxy.getGameState(gid));
                     
               if(append==0){
                    
                     String[] allRows = proxy.showAllMyGames(pid).split("\n");
                     String[] oneRow = new String[allRows.length];
                     //loop for the opponent username
                     for(int i=0; i<allRows.length; i++){
                        oneRow = allRows[i].split(",");
                                //if same gid select the username for the opponent
                                if(Integer.valueOf(oneRow[0]) == gid){
                                    
                                    opponent=oneRow[2];
                                }
                         
                     }
                     
                     jTextArea.append(opponent + " has entered the game.\n");
                     jTextArea.append("Game Started.\n");
                     jTextArea.append("Your move..\n ");
                     
                     wait=true;
                   
                     t=false;
                  
                    
               }
                
            }    

             
          String board = proxy.getBoard(gid);


            if(!board.equals("ERROR-NOMOVES")){
                    
              
                    
                   String[] row = board.split("\n");
                    
                    data = row[row.length-1].split(",");
                  
                    if ( Integer.parseInt(data[0]) != pid ){
                    
                        int x = Integer.parseInt(data[1]);
                        int y = Integer.parseInt(data[2]);
                       // boardArray[y][x] =1 ; 
                        
                         if(listen){
                            if (side.equals("X")){


                             jTextArea.append(opponent+" played.\n");
                             jTextArea.append("Your turn.\n");
                              button[y][x].setIcon(imageO);
                              wait=true;
                              listen=false;

                            }
                            else if (side.equals("O")){

                              jTextArea.append(opponent+" played.\n");
                              jTextArea.append("Your turn.\n");
                               button[y][x].setIcon(imageX);
                              wait=true;
                              listen=false;
                            }
                         }
                    }
                     
                    int checkWin = Integer.parseInt(proxy.checkWin(gid));
                    
                    switch (checkWin){
                        case 3:
                        //jTextArea.append("It is a draw.");
                          proxy.setGameState(gid, 3);
                        JOptionPane.showMessageDialog(frame,"It is a draw.","Game Over",JOptionPane.PLAIN_MESSAGE);
                        run=false;  
                        
                        frame.dispose();
                        break;
                        
                        case 1 : 
                       // jTextArea.append("You won.");
                         proxy.setGameState(gid, 1);
                             
                         if (side.equals("X"))
                        JOptionPane.showMessageDialog(frame,"You won.","Game Over",JOptionPane.PLAIN_MESSAGE);
                          else if (side.equals("O"))
                        JOptionPane.showMessageDialog(frame,"You lost.","Game Over",JOptionPane.PLAIN_MESSAGE);
                             
                        run=false;
                        
                        frame.dispose();
                        break;
                        
                        case 2: 
                        //jTextArea.append("You lost.");
                          proxy.setGameState(gid, 2);
                            
                          if (side.equals("X"))
                        JOptionPane.showMessageDialog(frame,"You lost.","Game Over",JOptionPane.PLAIN_MESSAGE);
                          
                          else if (side.equals("O"))
                        JOptionPane.showMessageDialog(frame,"You won.","Game Over",JOptionPane.PLAIN_MESSAGE);

                            run=false;
                        
                         frame.dispose();
                        break;
                    }
                    
            }
         
       }catch (Exception e) {
		e.printStackTrace();
	}    
   }

}
   
     public class buttonExit implements ActionListener {
    public void actionPerformed(ActionEvent e) {
        
           //clear the game if it hasnt started 
             try {
				proxy.deleteGame(gid, pid);
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
             run=false;
             frame.dispose();
        
    }
    
    }
    
    public class sendButton implements ActionListener {
    public void actionPerformed(ActionEvent e) {
            
        String yx = e.getActionCommand();
        int y = Integer.parseInt(yx.substring(0, 1));
        int x = Integer.parseInt(yx.substring(1, 2));
   
       
            if(wait){
            try {
            String check;
                    
                if(proxy.checkSquare(x, y, gid).equals ("0")){

                           check = proxy.takeSquare(x, y, gid, pid);

                           if (check.equals("1")){
                                if(side.equals("X")){

                                    button[y][x].setIcon(imageX);
                                    //boardArray[y][x]=1;   
                                }
                                else if (side.equals("O")) {
                                     button[y][x].setIcon(imageO);
                                    // boardArray[y][x]=1;
                            //button[0][0].removeActionListener(send);
                                }
                                
                        }else
                               jTextArea.append("error, try again.");
                               
                }
                else if(proxy.checkSquare(x, y, gid).equals ("1")){
                	
                     jTextArea.append("It is taken, try some other maybe.");
                }else
                    jTextArea.append("Connection problem.");
                
                jTextArea.append("You played.\n");
                jTextArea.append("Wait for the "+opponent+"'s move.\n");


                wait=false;
                listen=true;
                
            } catch (Exception e2) {
                	e2.printStackTrace();
					// TODO: handle exception
				}
            }
        

    }

}
    
      
    
}