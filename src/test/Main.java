package test;

import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;
import sources.*;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Usuarios usersList = new Usuarios();
        Scanner sc = new Scanner(System.in);
        
        System.out.println("Introduce tu nombre de usuario");
        String nick = sc.nextLine();
        
        ClienteMulticast cm = new ClienteMulticast(nick, usersList);
        ServidorMulticast sm = new ServidorMulticast(nick, "localhost", 8001);
        
        Thread trSM = new Thread(sm);
        Thread trCM = new Thread(cm);
        trSM.start();
        trCM.start();
        
        System.out.println("Usuarios activos:\n");
        while(true){
            Thread.sleep(1000);
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
        }
    }
}
