/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sources;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Andres
 */
public interface Checksum extends Remote{
    byte[] createChecksum(String filename) throws Exception, RemoteException; 
    String getMD5Checksum(String filename) throws Exception, RemoteException;
    String getLocalCheksum() throws RemoteException;
}
