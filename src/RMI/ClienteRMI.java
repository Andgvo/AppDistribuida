package RMI;

import sources.MD5Checksum;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import sources.Usuario;
import sources.Usuarios;

public class ClienteRMI {
    
    public static long getFileSize(String fileName, String nickUser){
        try {
            Registry registry = LocateRegistry.getRegistry(null);	
            //tambien puedes usar getRegistry(String host, int port)
            Checksum stub = (Checksum) registry.lookup("Checksum" + nickUser);
            long size = stub.getFileSize(fileName);
            System.out.println("tamaño : " +size);
            return size;
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
        return -1;
    }
    
    public static String obtenerChecksum(String fileName, String nickUser){
        try {
            Registry registry = LocateRegistry.getRegistry(null);	
            //tambien puedes usar getRegistry(String host, int port)
            Checksum stub = (Checksum) registry.lookup("Checksum" + nickUser);
            String checksum = stub.getChecksum(fileName);
            return checksum;
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
        return "";
    }
    
    public static boolean fileExist(String fileName, String nickUser){
        try {
            Registry registry = LocateRegistry.getRegistry(null);	
            //tambien puedes usar getRegistry(String host, int port)
            Checksum stub = (Checksum) registry.lookup("Checksum" + nickUser);
            Boolean exist = stub.fileExist(fileName);
            return exist;
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
        return false;
    }
    
    public static ArrayList<String> buscarCoincidencias(String fileName, Usuarios usersList){
        ArrayList<String> archivos = new ArrayList<>();
        for (Usuario usuario : usersList) {
            try {
                Registry registry = LocateRegistry.getRegistry(null);	
                //tambien puedes usar getRegistry(String host, int port)

                Checksum stub = (Checksum) registry.lookup("Checksum" + usuario.getNickName());
                ArrayList<String> files = stub.findFiles(fileName);
                
                for (String file : files) {
                    if(!archivos.contains(file))
                        archivos.add(file);
                }
            } catch (Exception e) {
                System.err.println("Client exception: " + e.toString());
                e.printStackTrace();
            }
        }
        return archivos;
    }
    
}
