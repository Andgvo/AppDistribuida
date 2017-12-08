package RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Checksum extends Remote{
    public String getChecksum(String filename) throws RemoteException;
    public boolean fileExist(String fileName) throws RemoteException;
    public ArrayList<String> findFiles(String fileName) throws RemoteException;
    public long getFileSize(String fileName) throws RemoteException;
}
