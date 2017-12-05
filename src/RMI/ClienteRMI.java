package RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClienteRMI {
    
    public static void main(String[] args) {
        String host = (args.length < 1) ? null : args[0];
	try {
            Registry registry = LocateRegistry.getRegistry(host);	
            //tambien puedes usar getRegistry(String host, int port)
	    
	    Checksum stub = (Checksum) registry.lookup("Checksum");
	    if(stub.fileExist("MD5Checksum.java")){
                String servidor = stub.getChecksum("MD5Checksum.java");
                String cliente = MD5Checksum.getMD5Checksum("MD5Checksum.java");
                if(servidor.compareTo(cliente) == 0)
                    System.out.println("Si son el mismo Cheksum");
                else
                    System.out.println("No son el mismo :c");
                System.out.println("SERV: " + servidor);
                System.out.println("CLIENT: " + cliente);
            }else{
                System.out.println("No existio");
            }
	    
	} catch (Exception e) {
	    System.err.println("Client exception: " + e.toString());
	    e.printStackTrace();
	}
    }
    
}
