package SocketsMulticast;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import sources.Usuario;
import sources.Usuarios;

public class ClienteMulticast implements Runnable{
    private static int TEMP_DEFAULT = 6;
    private static String IP_DIR = "228.1.1.1";
    private static int PORT = 4000; 
    private String serverName;
    private MulticastSocket ms;
    private InetAddress gpo;
    private Usuarios usersList;

    public ClienteMulticast(String serverName, Usuarios usersList) throws IOException {
        this.serverName = serverName;
        this.usersList = usersList;
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
                //System.out.println("Se recibieron estps bytes: " + p.getLength());
                ByteArrayInputStream bais = new ByteArrayInputStream(p.getData());
                ObjectInputStream ois = new ObjectInputStream(bais);
                //Obtener direccion ip del pacquete
                String ipAddress = p.getAddress().toString().substring(1);
                //Parcear Arreglo de bytes a objeto Datos
                Object objeto = ois.readObject();
                Usuario user = (Usuario)objeto;
                ois.close();
                bais.close();
                
                //En caso de ser su mismo anunciamiento
                if(user.getNickName().compareTo(serverName) == 0)
                    continue;
                
                boolean isNew = true;
                //Revisar Lista de usuarios
                usersList.lockList(); //Bloquear Lista
                for (Usuario usuario : usersList) {
                    String nickName = usuario.getNickName();
                    if(nickName.compareTo(user.getNickName()) == 0){
                        usuario.setTemp(TEMP_DEFAULT);
                        isNew = false;
                        break;
                    }
                }
                //Si no se encontro agregarlo
                if(isNew){
                    user.setTemp(TEMP_DEFAULT);
                    usersList.add(user);
                    System.out.println("Se unio: " + user.getNickName());
                }
                usersList.unlockList(); //Liberar Lista
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }
}
