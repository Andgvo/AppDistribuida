package SocketsMulticast;

import test.Menu;
import java.io.IOException;
import java.util.Iterator;
import sources.Usuario;
import sources.Usuarios;

public class ServicioMulticast implements Runnable{
    private String nick;
    private int port;
    private Usuarios usersList;
    private Menu menu;
    
    public ServicioMulticast(String nick, int port, Usuarios usersList, Menu menu) {
        this.nick = nick;
        this.port = port;
        this.usersList = usersList;
        this.menu = menu;
    }
    
    @Override
    public void run() {
        try {
            ClienteMulticast cm = new ClienteMulticast(nick, usersList);
            ServidorMulticast sm = new ServidorMulticast(nick, "localhost", port);
            //Crear Hilos del servicio Multicast
            Thread trSM = new Thread(sm);
            Thread trCM = new Thread(cm);
            trSM.start();
            trCM.start();
            //Actualizar Lista de Usuarios Constantemente
            System.out.println("Usuarios activos:\n");
            int noUsers = 0;
            while(true){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {}
                usersList.lockList(); //Bloquear lista
                Iterator<Usuario> iterUsers = usersList.iterator();
                while(iterUsers.hasNext()){
                    Usuario user = iterUsers.next();
                    user.setTemp(user.getTemp()-1);
                    //Si llega a 0 el temporalizador, remover de la lista
                    if(user.getTemp() == 0){
                        System.out.println("Se desconecto: " + user.getNickName());
                        iterUsers.remove();
                    }
                }
                usersList.unlockList(); //Liberar Lista
                if(noUsers != usersList.size()){
                    noUsers = usersList.size();
                    menu.actualizarTabla();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }
    
}
