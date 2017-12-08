package Envio;

import RMI.ClienteRMI;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import sources.Usuario;
import sources.Usuarios;

public class ServidorFlujo implements Runnable {
    private final ServerSocket ss;
    private String dirUser;
    
    public ServidorFlujo(int port, String nick) throws IOException {
        ss = new ServerSocket(port);
        dirUser = "archivos_" + nick;
        System.out.println("Servicio iniciado... esperando clientes");
    }

    @Override
    public void run() {
        while(true){
            try{
                Socket cl = ss.accept();
                System.out.println("Cliente conectado desde -> "
                + cl.getInetAddress() + ":" 
                + cl.getPort());
                
                DataInputStream dis = new DataInputStream(cl.getInputStream());
                DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
                
                //Leer datos iniciales para el envio del archivo
                
                String fileName = dis.readUTF();
                int noDivisiones = dis.readInt();
                int parteRequerida = dis.readInt();
                
                File f = new File(dirUser + File.separator + fileName);
                long tamF = f.length();
                long enviados = 0;
                
                //Abrir lectura con el archivo
                DataInputStream disf = new DataInputStream(
                        new FileInputStream(dirUser + File.separator + fileName));
                //Obtener el tamaño de las partes
                long parte = (int)tamF/noDivisiones;
                //Saltar bytes que no se enviaran.
                if(parte != 1){
                    long seekBytes = parte*(parteRequerida-1);
                    disf.skip(seekBytes);
                }
                
                long tamS;
                if(parteRequerida == noDivisiones)
                    tamS = tamF - parte*(noDivisiones-1);
                else
                    tamS = parte;
                System.out.println("Enviando parte " + parteRequerida + " del archivo " + fileName);
                System.out.println("Numero de bytes por enviar: " + tamS);
                //ENVIANDO DATOS EN PARTES DE 1500 BYTES
                while (enviados < tamS) {
                    byte []b;
                    if(tamS-enviados < 1500)
                        b = new byte[(int)(tamS-enviados)];
                    else
                        b = new byte[1500];
                    //LEER BYTES POSIBLES DE ENVIAR
                    int n = disf.read(b);
                    //ENVIAR DATOS LEIDOS ANTERIORMENTE
                    dos.write(b,0,n);
                    dos.flush();
                    enviados += n;
                    
                    int porcentaje = (int)((enviados*100)/tamS);
                    System.out.println("\rEnviados: " + porcentaje + "%");
                }
                System.out.println("Parte del archivo Enviada...");
                dis.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
