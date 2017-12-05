/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Andres
 */
public interface Checksum extends Remote{
    public String getChecksum(String filename) throws RemoteException;
    public boolean fileExist(String fileName) throws RemoteException;
}
