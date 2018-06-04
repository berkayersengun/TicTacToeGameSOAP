/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ttt.berkay.client;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import ttt.berkay.server.TictactoeServer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;




/**
 *
 * @author berkayersengun
 */
public class Gameslist {
    
    private JFrame frame ;
    private JPanel panel, panel1,panel2;
    private JLabel label ;
    private JButton buttonCreate,buttonRefresh,buttonLeague,buttonUserStatistics,buttonExit ;

   
  
    private String[][] data;
    private String openGames ;
    private String[] row ;
    private String[] each ;
           
    private JTable table;
    private JScrollPane scrollPane;
    
    private int pid,side;
    private int gid;
    private  boolean b ;
    private String username,opponent;
    
    private TictactoeServer proxy; 
    
       
    
     public Gameslist(String username, int pid, TictactoeServer proxy) throws RemoteException  {

    	 this.proxy = proxy;
         this.username = username;
         this.pid = pid ; 
         
         
        

        frame = new JFrame("Tic Tac Toe - Games List");
        panel = new JPanel();
        panel1 = new JPanel();
        panel2 = new JPanel();
        label = new JLabel("Available games to join - Player: " + username + " - Player No: " + pid );

        buttonCreate = new JButton("New Game");
        buttonCreate.addActionListener(new createListener());
        
        buttonLeague = new JButton("League");
        buttonLeague.addActionListener(new LeagueTable());
        
        buttonUserStatistics = new JButton("Score Board");
        buttonUserStatistics.addActionListener(new ScoreBoard());
        
        buttonRefresh = new JButton("Refresh");
        buttonRefresh.addActionListener(new refreshListener());
        
        buttonExit = new JButton("Exit");
        buttonExit.addActionListener(new ExitGameList());

        panel1.add(label);
        panel2.add(buttonCreate);
        panel2.add(buttonUserStatistics);
        panel2.add(buttonLeague);
        panel2.add(buttonRefresh);
        panel2.add(buttonExit);

        GridLayout grid = new GridLayout(1, 0);
        panel.setLayout(grid);

        String[] column = {"Game Number", "Username" , "Time Created" , "Join"};
        
        data = getGamesList();
           

         DefaultTableModel dm = new DefaultTableModel();
         dm.setDataVector(data,column);
         table = new JTable(dm);
         scrollPane = new JScrollPane(table);

           

       // table.getColumn("Join").setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());;
        table.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor());

       // table.getColumn()
        panel.add(scrollPane);

        frame.add(BorderLayout.CENTER, panel);
        frame.add(BorderLayout.NORTH,panel1);
        frame.add(BorderLayout.SOUTH,panel2);
        frame.setLocationRelativeTo(null);
        frame.setSize(650,500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        

    }
     
     public String[][] getGamesList() throws RemoteException{
         
          openGames =  proxy.showOpenGames();
            
            if (openGames.equals("ERROR-NOGAMES")){
                data = new String[0][0];
                
                
            }
            else{
            //row array stores each sql line separated by newline '\n'
            row = openGames.split("\n");
            //each array stores each word in row array separated by comma ','
            //length should be row multiplied by 3 because every row has 3 strings
            each = new String[row.length*3]; 
            //data array for implementing jtable
            //it is 2 dimensional data[row][column]
            data = new String[row.length][4];
         
            for (int y=0; y<row.length; y++){
                
               each = row[y].split(",");
               for(int x=0; x<4; x++){
                   
                  if (x==3){
                      
                  data[y][x]= "Join";
                      
                  }
                  else{
                      
                   data[y][x]=each[x];
                   
                  }
               }
                
                
            }            
           }
            return data;
         
         
     }
     
     private class ButtonRenderer extends JButton implements  TableCellRenderer
    {

        public ButtonRenderer() {
            //SET BUTTON PROPERTIES
            setOpaque(true);
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object obj,
                                                       boolean selected, boolean focused, int row, int col) {

            //SET PASSED OBJECT AS BUTTON TEXT
            setText((obj==null) ? "":obj.toString());

            return this;
        }

    }
    private class ButtonEditor extends AbstractCellEditor implements ActionListener, TableCellEditor {
        private JButton btn;
        private String label;
        private int y;


        public ButtonEditor() {

            btn=new JButton();
            btn.setOpaque(true);

            //WHEN BUTTON IS CLICKED
            btn.addActionListener(this) ;

        }
        @Override
        public void actionPerformed(ActionEvent e) {
        	try {

                //y is selected row when button fired at getTableCellEditorComponent
                gid = Integer.parseInt(data[y][0]);
                opponent = data [y][1];
                
                //join game
                String check = proxy.joinGame(pid, gid);
                
                if(check.equals("1") && !opponent.equals(username)){
            
                //new game for second player playing as 'O'
                Game joinGame = new Game(username,pid,"O",gid,opponent,proxy);
                Thread t = new Thread(joinGame);
                t.start();
                
           
                
                //frame.dispose();
                    
                }else if (check.equals("1") && opponent.equals(username)){
                    
                    System.out.println("You cant play against yourself, come on.");
                    
                }
                
                else if (check.equals("ERROR-DB")){
                    
                    System.out.println("Database connection problem, try again.");
                    
                    
                }
                else
                    System.out.println("Try again.");

            fireEditingStopped();
            
        	 } catch (IOException ex) {
                 Logger.getLogger(Gameslist.class.getName()).log(Level.SEVERE, null, ex);
             }
            
        }



        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

            label=(value==null) ? "":value.toString();
            btn.setText(label);

            //y is selected row when button fired
            y= row;


            return btn;
        }

        @Override
        public Object getCellEditorValue() {
            return new String(label);
        }


    }
    
    
    
    private class refreshListener implements ActionListener {
        public void actionPerformed(ActionEvent e ){

        String[] column = {"Game Number", "Username" , "Time Created" , "Join"};
            
        try {
			data= getGamesList();
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

         DefaultTableModel dm = new DefaultTableModel();
         dm.setDataVector(data,column);
         table.setModel(dm);
         table.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());;
         table.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor());
         table.repaint();
               
                
            }            

        }

    
    
      private class createListener implements ActionListener {
        public void actionPerformed(ActionEvent e ){
			try {
                String check;
				
					check = proxy.newGame(pid);
				
                   switch (check){
                   
                   case "ERROR-NOTFOUND": 
                    
                        System.out.println("Game created not found, Please try another one. ");
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
                       
                       System.out.println("Game added to database with the gid number of "+ check);
                      // frame.dispose();
                       
                
                    try {
                        //new game created side X for player 1
                        Game newGame = new Game(username,pid,"X",Integer.parseInt(check),proxy);
                        Thread t = new Thread(newGame);
                        t.start();
                        
                    } catch (IOException ex) {
                        Logger.getLogger(Gameslist.class.getName()).log(Level.SEVERE, null, ex);
                    }
                
                       
                       break;
                        
                }
    
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

        }

    }
    
    
      
      private class LeagueTable implements ActionListener {
        public void actionPerformed(ActionEvent e ){
            
            try {
				new LeagueTableGui();
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        
        
    }
    
}
     private class ScoreBoard implements ActionListener {
        public void actionPerformed(ActionEvent e ){
            
            try {
				new ScoreBoardGui();
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            
        
        
    }
    
}
     private class ScoreBoardGui implements ActionListener{
    
         private JFrame frameScore;
         private JTable tableScore;
         private JPanel panelScore,panelScore1,panelScore2;
         private JScrollPane scrollPaneScore;
         private JButton buttonScoreRefresh, buttonScoreExit;
         private JLabel scoreLabelName,scoreLabelStats;
         
         
         private String scoreTable;
         private String[][] dataScore; 
         private String[] rowScore,eachScore,updatedRowScore;
         private ArrayList arrayScore ;
         private String[] stats;
         
        
    public ScoreBoardGui() throws RemoteException{
        
         frameScore = new JFrame("Score Board Table");
         
         panelScore = new JPanel();
         panelScore1 = new JPanel();
         panelScore2= new JPanel();
         panelScore2.setLayout(new BoxLayout(panelScore2, BoxLayout.Y_AXIS));
        

         //get user statistics
         stats = new String[3];
         stats=getStats();

         scoreLabelName = new JLabel("Player Name: " + username);
         scoreLabelStats = new JLabel("Wins: "+ stats[0] + " Losses: " +stats[1]+ " Draw: " +stats[2]);
         
         scoreLabelName.setAlignmentX(Component.CENTER_ALIGNMENT);
         scoreLabelStats.setAlignmentX(Component.CENTER_ALIGNMENT);
         
         buttonScoreRefresh = new JButton("Refresh");
         buttonScoreRefresh.addActionListener(this);
         
         buttonScoreExit = new JButton("Exit");
         buttonScoreExit.addActionListener(this);
         
         
          GridLayout grid = new GridLayout(1, 1);
          panelScore.setLayout(grid);
         
         String[] columnScore = {"Game Number", "Player X Name" , "Player O Name ","Result" ,  "Time Created" };
 
         dataScore = getScoreBoard();
            
         DefaultTableModel dm = new DefaultTableModel();
         dm.setDataVector(dataScore,columnScore);
         
         tableScore = new JTable(dm);
         
         //tableLeague.setPreferredSize();
         scrollPaneScore = new JScrollPane(tableScore);
       
         panelScore.add(scrollPaneScore);
         panelScore1.add(buttonScoreRefresh);
         panelScore1.add(buttonScoreExit);
         panelScore2.add(scoreLabelName);
         panelScore2.add(scoreLabelStats);
         
         frameScore.add(BorderLayout.NORTH,panelScore2);
         frameScore.add(BorderLayout.CENTER,panelScore);
         frameScore.add(BorderLayout.SOUTH,panelScore1);
         frameScore.setLocationRelativeTo(null);
        frameScore.setSize(775,500);
        frameScore.setVisible(true);
        frameScore.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        
        
        
        
    }

        @Override
        public void actionPerformed(ActionEvent e) {
           if (e.getSource() == buttonScoreExit ) {
                
            frameScore.dispose();

                }

           else if (e.getSource() == buttonScoreRefresh ) {
               
            //refresh stats
            try {
				stats=getStats();
			} catch (RemoteException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
            scoreLabelStats.setText("Wins: "+ stats[0] + " Losses: " +stats[1]+ " Draw: " +stats[2]);
               
            //repaint jtable
            String[] columnScore = {"Game Number", "Player X Name" , "Player O Name ","Result" ,  "Time Created" };

            try {
				dataScore = getScoreBoard();
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

            DefaultTableModel dm = new DefaultTableModel();
            dm.setDataVector(dataScore,columnScore);

            tableScore.setModel(dm);
            tableScore.repaint();
            }
        }
    
        
        public String[] getStats() throws RemoteException{
         
            String[][] d = getScoreBoard();
            int countWin=0;
            int countLose=0;
            int countDraw=0;
            
            for (int i=0; i<d.length; i++){
                
                if(d[i][3].equals("You won."))
                    countWin++;
                    
                else if(d[i][3].equals("You lost."))
                    countLose++;
                    
                else 
                    countDraw++;
                
                
            }
            stats[0]=String.valueOf(countWin);
            stats[1]=String.valueOf(countLose);
            stats[2]=String.valueOf(countDraw);
            
            return stats;
        }
        
        public String[][] getScoreBoard() throws RemoteException{
            
            scoreTable =  proxy.leagueTable();
         //System.out.println(leagueTable);
            
            if (scoreTable.equals("ERROR-NOGAMES")){
                dataScore = new String[0][0];
                
                
            }
            else{
                //row array stores each sql line separated by newline '\n'
                rowScore = scoreTable.split("\n");
                //each array stores each word in row array separated by comma ','
                //length should be row multiplied by 5 because every row has 3 strings
                eachScore = new String[5]; 
                //data array for implementing jtable
                //it is 2 dimensional data[row][column]

                arrayScore = new ArrayList();

                for (int y=0; y<rowScore.length; y++){
                eachScore = rowScore[y].split(",");


                    if(eachScore[1].equals(username) || eachScore[2].equals(username)) {
                    //add only games have finished
                        if((eachScore[3].equals("1") ||eachScore[3].equals("2")||eachScore[3].equals("3")) )  {
                    
                            //dynamic list to avoid blank rows in jtable
                            arrayScore.add(rowScore[y]);


                        }
                    }
                }            
           }
            
            
            dataScore = new String[arrayScore.size()][5];
            //populate the table data from arraylist
            Iterator<String> it = arrayScore.iterator();
            int y = 0;
		while (it.hasNext()) {
                    
                    updatedRowScore = it.next().split(",");
                 
                    for(int x=0; x<5; x++){
                        
                        
                    //set the results to their meanings
                    
                   if((updatedRowScore[3].equals("1") && updatedRowScore[1].equals(username)) 
                           || (updatedRowScore[3].equals("2") && updatedRowScore[2].equals(username))){
                       
                       updatedRowScore[3]= "You won.";
                       
                  }else if ((updatedRowScore[3].equals("2") && updatedRowScore[1].equals(username))
                          || (updatedRowScore[3].equals("1") && updatedRowScore[2].equals(username))){
                      
                        updatedRowScore[3] = "You lost.";
                        
                  }else if (updatedRowScore[3].equals("3")){
                      
                         updatedRowScore[3] = "Draw";
                   }
                   
                   dataScore[y][x]= updatedRowScore[x];
                        
                    
                  }
                        y++;
		}
             return dataScore;
            
            
        }
    
    
    }
     
     
    private class LeagueTableGui implements ActionListener{
    
         private JFrame frameLeague;
         private JTable tableLeague;
         private JPanel panelLeague,panelLeague1;
         private JScrollPane scrollPaneLeague;
         private JButton buttonLeagueRefresh, buttonLeagueExit;
         
         private String leagueTable;
         private String[][] dataLeague; 
         private String[] rowLeague,eachLeague,updatedRowLeague;
         private ArrayList arrayLeague ;
         
        
    public LeagueTableGui() throws RemoteException{
            
        
         frameLeague = new JFrame("League Table");
         panelLeague = new JPanel();
         panelLeague1 = new JPanel();
         
         buttonLeagueRefresh = new JButton("Refresh");
         buttonLeagueRefresh.addActionListener(this);
         
         buttonLeagueExit = new JButton("Exit");
         buttonLeagueExit.addActionListener(this);
         
         
          GridLayout grid = new GridLayout(1, 1);
          panelLeague.setLayout(grid);
         
         //String[] columnLeague = {"Game Number", "Player X Name" , "Player O Name ","Result" ,  "Time Created" };
         String[] columnLeague = {"Player Name", "Win", "Lose", "Draw"};

         dataLeague = getLeagueTable();
            
         DefaultTableModel dm = new DefaultTableModel();
         dm.setDataVector(dataLeague,columnLeague);
         
         tableLeague = new JTable(dm);
         
         //tableLeague.setPreferredSize();
         scrollPaneLeague = new JScrollPane(tableLeague);
         
         panelLeague.add(scrollPaneLeague);
         panelLeague1.add(buttonLeagueRefresh);
         panelLeague1.add(buttonLeagueExit);
         
         frameLeague.add(BorderLayout.CENTER,panelLeague);
         frameLeague.add(BorderLayout.SOUTH,panelLeague1);
         frameLeague.setLocationRelativeTo(null);
        frameLeague.setSize(600,500);
        frameLeague.setVisible(true);
        frameLeague.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        
        
    }

        @Override
        public void actionPerformed(ActionEvent e) {
           if (e.getSource() == buttonLeagueExit ) {
                
            frameLeague.dispose();

                }

           else  if (e.getSource() == buttonLeagueRefresh ) {
    
          String[] columnLeague = {"Player Name", "Win", "Lose", "Draw"};
         
         try {
			dataLeague = getLeagueTable();
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
         
            
         DefaultTableModel dm = new DefaultTableModel();
         dm.setDataVector(dataLeague,columnLeague);
         tableLeague.setModel(dm);
         tableLeague.repaint();
            }
        }
    
    
    public String[][] getLeagueTable() throws RemoteException{
        
        
         leagueTable =  proxy.leagueTable();
         //System.out.println(leagueTable);
            
            if (leagueTable.equals("ERROR-NOGAMES")){
                dataLeague = new String[0][0];
                
                
            }
            else{
            //row array stores each sql line separated by newline '\n'
            rowLeague = leagueTable.split("\n");
            //each array stores each word in row array separated by comma ','
            //length should be row multiplied by 5 because every row has 3 strings
            eachLeague = new String[5]; 
            //data array for implementing jtable
            //it is 2 dimensional data[row][column]
            
            arrayLeague = new ArrayList();
            
            for (int y=0; y<rowLeague.length; y++){
                eachLeague = rowLeague[y].split(",");
                //add only games have finished
                    if(eachLeague[3].equals("1") ||eachLeague[3].equals("2")||eachLeague[3].equals("3") )  {

                           //dynamic list to avoid blank rows in jtable
                        arrayLeague.add(rowLeague[y]);


                    }

                }            
           }
            
          
            //populate the table data from arraylist
            Iterator<String> it = arrayLeague.iterator();
            int y = 0;
            int countWin=0;
            int countLose=0;
            int countDraw=0;
            //hashset collection to avoid duplicate values
            HashSet arrayUser = new HashSet();
            
		while (it.hasNext()) {
                    
                    updatedRowLeague = it.next().split(",");

                    arrayUser.add(updatedRowLeague[1]);
                    arrayUser.add(updatedRowLeague[2]);
                       
		}
                
                 Iterator<String> hashUser = arrayUser.iterator();
                dataLeague = new String[arrayUser.size()][4];
                 //loop through every player in hashset
                while(hashUser.hasNext()){
                    //look for each player if there is a game that played in db 
                    //store the values for each game result and pass it to data[][] for jtable
                    String user = hashUser.next();
                    Iterator<String> it1 = arrayLeague.iterator();
                    //split everyline and search for corresponding game
                    //updateRowLeague is for temporary use only for each line
                    while (it1.hasNext()) {

                        updatedRowLeague = it1.next().split(",");
                       
                        if(updatedRowLeague[1].equals(user)){

                            if(updatedRowLeague[3].equals("1")){
                                countWin++;
                            }else if(updatedRowLeague[3].equals("2")){
                                countLose++;
                            }else if(updatedRowLeague[3].equals("3")){
                                countDraw++;
                            }

                        }
                        else if (updatedRowLeague[2].equals(user)){

                            if(updatedRowLeague[3].equals("1")){
                                countLose++;
                            }else if(updatedRowLeague[3].equals("2")){
                                countWin++;
                            }else if(updatedRowLeague[3].equals("3")){
                                countDraw++;
                            }

                        }   
		  
                    }
                dataLeague[y][0]=user; 
                dataLeague[y][1]=String.valueOf(countWin);
                dataLeague[y][2]=String.valueOf(countLose);
                dataLeague[y][3]=String.valueOf(countDraw);
              
                countDraw=0;
                countLose=0;
                countWin=0;
                y++;
               }
              
                return dataLeague;
    }
    
    }
    
    
     private class ExitGameList implements ActionListener {
        public void actionPerformed(ActionEvent e ){
            
           frame.dispose();
           
        
        
    }
    
}
    
   
    
}
    
    
    
    

