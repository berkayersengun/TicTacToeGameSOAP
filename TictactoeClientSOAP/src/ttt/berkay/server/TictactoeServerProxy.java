package ttt.berkay.server;

public class TictactoeServerProxy implements ttt.berkay.server.TictactoeServer {
  private String _endpoint = null;
  private ttt.berkay.server.TictactoeServer tictactoeServer = null;
  
  public TictactoeServerProxy() {
    _initTictactoeServerProxy();
  }
  
  public TictactoeServerProxy(String endpoint) {
    _endpoint = endpoint;
    _initTictactoeServerProxy();
  }
  
  private void _initTictactoeServerProxy() {
    try {
      tictactoeServer = (new ttt.berkay.server.TictactoeServerServiceLocator()).getTictactoeServerPort();
      if (tictactoeServer != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)tictactoeServer)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)tictactoeServer)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (tictactoeServer != null)
      ((javax.xml.rpc.Stub)tictactoeServer)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public ttt.berkay.server.TictactoeServer getTictactoeServer() {
    if (tictactoeServer == null)
      _initTictactoeServerProxy();
    return tictactoeServer;
  }
  
  public java.lang.String register(java.lang.String username, java.lang.String password, java.lang.String name, java.lang.String surname) throws java.rmi.RemoteException{
    if (tictactoeServer == null)
      _initTictactoeServerProxy();
    return tictactoeServer.register(username, password, name, surname);
  }
  
  public java.lang.String setGameState(int gid, int gstate) throws java.rmi.RemoteException{
    if (tictactoeServer == null)
      _initTictactoeServerProxy();
    return tictactoeServer.setGameState(gid, gstate);
  }
  
  public java.lang.String joinGame(int uid, int gid) throws java.rmi.RemoteException{
    if (tictactoeServer == null)
      _initTictactoeServerProxy();
    return tictactoeServer.joinGame(uid, gid);
  }
  
  public java.lang.String getBoard(int gid) throws java.rmi.RemoteException{
    if (tictactoeServer == null)
      _initTictactoeServerProxy();
    return tictactoeServer.getBoard(gid);
  }
  
  public java.lang.String leagueTable() throws java.rmi.RemoteException{
    if (tictactoeServer == null)
      _initTictactoeServerProxy();
    return tictactoeServer.leagueTable();
  }
  
  public java.lang.String takeSquare(int x, int y, int gid, int pid) throws java.rmi.RemoteException{
    if (tictactoeServer == null)
      _initTictactoeServerProxy();
    return tictactoeServer.takeSquare(x, y, gid, pid);
  }
  
  public java.lang.String newGame(int uid) throws java.rmi.RemoteException{
    if (tictactoeServer == null)
      _initTictactoeServerProxy();
    return tictactoeServer.newGame(uid);
  }
  
  public java.lang.String checkSquare(int x, int y, int gid) throws java.rmi.RemoteException{
    if (tictactoeServer == null)
      _initTictactoeServerProxy();
    return tictactoeServer.checkSquare(x, y, gid);
  }
  
  public java.lang.String getGameState(int gid) throws java.rmi.RemoteException{
    if (tictactoeServer == null)
      _initTictactoeServerProxy();
    return tictactoeServer.getGameState(gid);
  }
  
  public java.lang.String deleteGame(int gid, int uid) throws java.rmi.RemoteException{
    if (tictactoeServer == null)
      _initTictactoeServerProxy();
    return tictactoeServer.deleteGame(gid, uid);
  }
  
  public java.lang.String checkWin(int gid) throws java.rmi.RemoteException{
    if (tictactoeServer == null)
      _initTictactoeServerProxy();
    return tictactoeServer.checkWin(gid);
  }
  
  public java.lang.String showMyOpenGames(int uid) throws java.rmi.RemoteException{
    if (tictactoeServer == null)
      _initTictactoeServerProxy();
    return tictactoeServer.showMyOpenGames(uid);
  }
  
  public java.lang.String showAllMyGames(int uid) throws java.rmi.RemoteException{
    if (tictactoeServer == null)
      _initTictactoeServerProxy();
    return tictactoeServer.showAllMyGames(uid);
  }
  
  public java.lang.String hellow(java.lang.String arg0) throws java.rmi.RemoteException{
    if (tictactoeServer == null)
      _initTictactoeServerProxy();
    return tictactoeServer.hellow(arg0);
  }
  
  public java.lang.String showOpenGames() throws java.rmi.RemoteException{
    if (tictactoeServer == null)
      _initTictactoeServerProxy();
    return tictactoeServer.showOpenGames();
  }
  
  public int operation(int parameter, int parameter1) throws java.rmi.RemoteException{
    if (tictactoeServer == null)
      _initTictactoeServerProxy();
    return tictactoeServer.operation(parameter, parameter1);
  }
  
  public int login(java.lang.String username, java.lang.String password) throws java.rmi.RemoteException{
    if (tictactoeServer == null)
      _initTictactoeServerProxy();
    return tictactoeServer.login(username, password);
  }
  
  
}