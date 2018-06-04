/**
 * TictactoeServerService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ttt.berkay.server;

public interface TictactoeServerService extends javax.xml.rpc.Service {
    public java.lang.String getTictactoeServerPortAddress();

    public ttt.berkay.server.TictactoeServer getTictactoeServerPort() throws javax.xml.rpc.ServiceException;

    public ttt.berkay.server.TictactoeServer getTictactoeServerPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
