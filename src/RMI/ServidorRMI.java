package RMI;

import RMI.Checksum;
import sources.MD5Checksum;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServidorRMI implements Checksum{
    private String dirUser;
    
    public ServidorRMI(String nick) {
        dirUser = "archivos_" + nick;
        File dirUserF = new File(dirUser);
        dirUserF.mkdir();
    }
    
    @Override
    public String getChecksum(String filename) throws RemoteException {
        try {
            return MD5Checksum.getMD5Checksum(dirUser + File.separator + filename);
        } catch (Exception ex) {
            return "";
        }
    }
    
    @Override
    public boolean fileExist(String fileName) throws RemoteException {
        File f = new File(dirUser + File.separator + fileName);
        return f.exists();
    }

    @Override
    public long getFileSize(String fileName) throws RemoteException {
        File f = new File(dirUser + File.separator + fileName);
        return f.length();
    }
    
    @Override
    public ArrayList<String> findFiles(String fileName) throws RemoteException {
        File dirF = new File(dirUser);
        File[] files = dirF.listFiles();
        ArrayList<String> coincidencias = new ArrayList<>();
        for (File file : files) {
            if(file.getName().contains(fileName)){
                coincidencias.add(file.getName());
            }
        }
        return coincidencias;
    }

    public static void iniciarServidorRMI(String nick){
        try {
            java.rmi.registry.LocateRegistry.createRegistry(1099); //puerto default del rmiregistry
            System.out.println("RMI registry ready.");
        } catch (Exception e) {
            System.out.println("Exception starting RMI registry:");
        }//catch

        try {
            System.setProperty("java.rmi.server.codebase", "file:///f:\\redes2\\RMI\\RMI2");
            ServidorRMI obj = new ServidorRMI(nick);
            Checksum stub = (Checksum) UnicastRemoteObject.exportObject(obj, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("Checksum" + nick, stub);

            System.err.println("Servidor listo...");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
