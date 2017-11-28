package sources;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class ClienteMulticast implements Runnable{
    private static String IP_DIR = "228.1.1.1";
    private static int PORT = 4000; 
    private String localNameSer;
    private MulticastSocket ms;
    private InetAddress gpo;

    public ClienteMulticast(String localNameSer) throws IOException {
        this.localNameSer = localNameSer;
        ms = new MulticastSocket(PORT);
        ms.setReuseAddress(true);
        gpo = InetAddress.getByName(IP_DIR);
        ms.joinGroup(gpo);
        System.out.println("Servicio de Cliente Multicast iniciado, esperando datagramas...");
    }
    
    @Override
    public void run() {
        while(true){
            try {
                DatagramPacket p = new DatagramPacket(new byte[1024], 1024);
                //recibir datagrama
                ms.receive(p);
                System.out.println("Se recibieron estps bytes: " + p.getLength());
                ByteArrayInputStream bais = new ByteArrayInputStream(p.getData());
                ObjectInputStream ois = new ObjectInputStream(bais);
                //Parcear Arreglo de bytes a objeto Datos
                Object objeto = ois.readObject();
                Usuario user = (Usuario)objeto;
                System.out.println("RECIBIDO:" + user.toString());
                ois.close();
                bais.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) throws IOException {
        ClienteMulticast cm = new ClienteMulticast("nickName");
        Thread tr = new Thread(cm);
        tr.start();
    }
    
}
