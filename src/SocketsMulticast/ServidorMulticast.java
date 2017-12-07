package SocketsMulticast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import sources.Usuario;

public class ServidorMulticast implements Runnable{
    private static String IP_DIR = "228.1.1.1";
    private static int PORT = 4000; 
    private Usuario user;
    private MulticastSocket ms;
    private InetAddress gpo;

    public ServidorMulticast(String nameServer, String ipDir, int pto) throws IOException {
        user = new Usuario(nameServer, ipDir, pto);
        ms = new MulticastSocket(PORT);
        ms.setReuseAddress(true);
        //Numero de saltos del multicast
        ms.setTimeToLive(255);
        gpo = InetAddress.getByName(IP_DIR);
        ms.joinGroup(gpo);
        System.out.println("Servicio de Servidor Multicast iniciado y unido al grupo...");
    }
    
    @Override
    public void run() {
        while(true){
            try {
                //Se abren flujos para enviar objeto
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(user);
                oos.flush();
                //Obtener bytes del objeto
                byte[] b = baos.toByteArray();
                //System.out.println("S enviaran los siguientes bytes: " + b.length);
                DatagramPacket p = new DatagramPacket(b, b.length, gpo, PORT);
                ms.send(p);
                oos.close();
                baos.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally{
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
