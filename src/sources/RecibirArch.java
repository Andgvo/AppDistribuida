package sources;

import java.io.*;
import java.net.*;

public class RecibirArch extends Thread {
    String path;
    ServerSocket s;

    public RecibirArch(ServerSocket s) {
        this.s = s;
        this.path = System.getProperty("user.dir")+File.separator+"archivos"+File.separator;
    }
    
     @Override
     public void run(){
         try{
             System.out.println("Esperando archivos");
             for(;;){
                Socket cl = s.accept();
                System.out.println("Cliente conectado desde -> "+cl.getInetAddress()+":"+cl.getPort());
                DataInputStream dis = new DataInputStream(cl.getInputStream());
                
                String nombre = dis.readUTF();
                
                System.out.println("Recibiendo archivo " + nombre);
                DataOutputStream dos = new DataOutputStream(new FileOutputStream("archivos"+ File.separator+nombre));
                long r = 0, tam;
                int n = 0, porcentaje;
                tam = dis.readLong();
                
                while (r < tam) {
                    byte[] b = new byte[1500];
                    n = dis.read(b);
                    dos.write(b,0,n);
                    dos.flush();
                    r = r + n;
                    porcentaje = (int)((r*100)/tam);
                    System.out.println("\rRecibido el " + porcentaje + "%");
                }
                System.out.println("Archivo Recibido...");
                
                dos.close();
                dis.close();
                cl.close();
            }
         }catch(Exception ex){
             
         }
     }
    
    public static void main(String[] args) throws IOException {
        int pto = 9876;
        ServerSocket s = new ServerSocket(pto);
        RecibirArch ra = new RecibirArch(s);
        ra.start();
    }
}
