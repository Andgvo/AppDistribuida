package sources;

import java.io.FileInputStream;
import java.io.InputStream;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;

public class ServidorRMI implements Checksum{
    private String localCheksum, fileName;
    private int port;

    public ServidorRMI(String fileName) throws Exception {
    }
    
    @Override
    public byte[] createChecksum(String filename) throws Exception, RemoteException {
       InputStream fis =  new FileInputStream(filename);

       byte[] buffer = new byte[1024];
       MessageDigest complete = MessageDigest.getInstance("MD5");
       int numRead;

       do {
           numRead = fis.read(buffer);
           if (numRead > 0) {
               complete.update(buffer, 0, numRead);
           }
       } while (numRead != -1);

       fis.close();
       return complete.digest();
   }

   // see this How-to for a faster way to convert
   // a byte array to a HEX string
    @Override
    public String getMD5Checksum(String filename) throws Exception, RemoteException {
       byte[] b = createChecksum(filename);
       String result = "";

       for (int i=0; i < b.length; i++) {
           result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
       }
       return result;
   }

    @Override
    public String getLocalCheksum() throws RemoteException {
        return this.localCheksum;
    }
        
    public static void main(String[] args) throws Exception {
        try {
		 java.rmi.registry.LocateRegistry.createRegistry(8001); //puerto default del rmiregistry
		 System.out.println("RMI registry ready.");
	  } catch (Exception e) {
		 System.out.println("Exception starting RMI registry:");
		 e.printStackTrace();
	  }//catch
	
	try {
            System.setProperty("java.rmi.server.codebase","http://8.25.100.18/clases/"); ///file:///f:\\redes2\\RMI\\RMI2
	    ServidorRMI obj = new ServidorRMI("MD5Checksum.java");
	    Checksum stub = (Checksum) UnicastRemoteObject.exportObject(obj, 0);

	    // Bind the remote object's stub in the registry
	    Registry registry = LocateRegistry.getRegistry();
	    registry.bind("Checksum", stub);

	    System.err.println("Servidor listo...");
	} catch (Exception e) {
	    System.err.println("Server exception: " + e.toString());
	    e.printStackTrace();
	}
    }
    
}
