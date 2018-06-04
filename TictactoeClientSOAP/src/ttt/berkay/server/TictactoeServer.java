/**
 * TictactoeServer.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ttt.berkay.server;

public interface TictactoeServer extends java.rmi.Remote {
    public java.lang.String register(java.lang.String username, java.lang.String password, java.lang.String name, java.lang.String surname) throws java.rmi.RemoteException;
    public java.lang.String setGameState(int gid, int gstate) throws java.rmi.RemoteException;
    public java.lang.String joinGame(int uid, int gid) throws java.rmi.RemoteException;
    public java.lang.String getBoard(int gid) throws java.rmi.RemoteException;
    public java.lang.String leagueTable() throws java.rmi.RemoteException;
    public java.lang.String takeSquare(int x, int y, int gid, int pid) throws java.rmi.RemoteException;
    public java.lang.String newGame(int uid) throws java.rmi.RemoteException;
    public java.lang.String checkSquare(int x, int y, int gid) throws java.rmi.RemoteException;
    public java.lang.String getGameState(int gid) throws java.rmi.RemoteException;
    public java.lang.String deleteGame(int gid, int uid) throws java.rmi.RemoteException;
    public java.lang.String checkWin(int gid) throws java.rmi.RemoteException;
    public java.lang.String showMyOpenGames(int uid) throws java.rmi.RemoteException;
    public java.lang.String showAllMyGames(int uid) throws java.rmi.RemoteException;
    public java.lang.String hellow(java.lang.String arg0) throws java.rmi.RemoteException;
    public java.lang.String showOpenGames() throws java.rmi.RemoteException;
    public int operation(int parameter, int parameter1) throws java.rmi.RemoteException;
    public int login(java.lang.String username, java.lang.String password) throws java.rmi.RemoteException;
}
