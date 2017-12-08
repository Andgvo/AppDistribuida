/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Envio;

import RMI.ClienteRMI;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import sources.Usuario;
import sources.Usuarios;

/**
 *
 * @author xXEdG
 */
public class ClienteFlujo implements Runnable{
    private RandomAccessFile raf;
    private ReentrantLock rl;
    private Usuario usuario;
    private String fileName;
    private long tam;
    private int noPartes;
    private int parte;

    public ClienteFlujo(RandomAccessFile raf, ReentrantLock rl, Usuario usuario, String fileName ,long tam, int noPartes, int parte) {
        this.raf = raf;
        this.rl = rl;
        this.usuario = usuario;
        this.fileName = fileName;
        this.tam = tam;
        this.noPartes = noPartes;
        this.parte = parte;
    }
    
    @Override
    public void run() {
        Socket cl;
        try {
            cl = new Socket(usuario.getIpDir(), usuario.getPto()); //CREANDO SOCKET
            System.out.println("Conexion Establecida en puerto "+usuario.getPto()+"...");
            //Abriendo flujo de lectura y escritura
            DataInputStream dis = new DataInputStream(cl.getInputStream());
            DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
            //Enviar datos del archivo
            dos.writeUTF(fileName); //Se enviara el nombre del archivo
            dos.flush();
            dos.writeInt(noPartes); //Se enviara el numero de partes
            dos.flush();
            dos.writeInt(parte); //Se enviara el numero de partes
            dos.flush();

            //Obtener el tamaño de las partes
            long parteB = (int)tam/noPartes;
            //Obtener posicion de escritura
            int seekBytes = (int)parteB*(parte-1);
            //Ver cuanto se leera
            long tamS;
            if(parte == noPartes)
                    tamS = tam - parteB*(noPartes-1);
                else
                    tamS = parteB;
            
            long r = 0;
            int n = 0; //, porcentaje;
            System.out.println("Recibiendo parte " + parte + " del archivo " + fileName);
            //Se comenzara a leer el archivo
            while (r < tamS) {
                rl.lock();
                //raf.seek(0);
                raf.seek(seekBytes); //Saltar Hasta el byte correspondiente
                byte[] b;
                if(tamS-r < 1500)
                        b = new byte[(int)(tamS-r)];
                    else
                        b = new byte[1500];
                n = dis.read(b);
                raf.write(b,0,n);
                r = r + n;
                rl.unlock();
                seekBytes += n;
            }
            
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static boolean downloadFile(String fileName, Usuarios usersList, String nick){
        long tam = 0;
        String checksum = "";
        
        ArrayList<Usuario> usuarios = new ArrayList<>();
        for (Usuario usuario : usersList) {
            if(ClienteRMI.fileExist(fileName, usuario.getNickName())){
                if(checksum.isEmpty()){
                    checksum = ClienteRMI.obtenerChecksum(fileName, usuario.getNickName());
                    tam = ClienteRMI.getFileSize(fileName, usuario.getNickName());
                    usuarios.add(usuario);
                }else{
                    String chck = ClienteRMI.obtenerChecksum(fileName, usuario.getNickName());
                    if(chck.compareTo(checksum) == 0)
                        usuarios.add(usuario);
                }
            }
        }
        
        try {
            RandomAccessFile raf = new RandomAccessFile("archivos_" + nick + File.separator + fileName, "rw");
            ReentrantLock rl = new ReentrantLock();
            ArrayList<Thread> trsList = new ArrayList<>();
            int noPartes = usuarios.size();
            for (int i = 0; i < usuarios.size(); i++) {
                System.out.println("Pidiendo a usuario: " + usuarios.get(i).getNickName());
                ClienteFlujo cf = new ClienteFlujo(raf, rl, usuarios.get(i), fileName, tam, noPartes, (i+1));
                Thread tr = new Thread(cf);
                tr.start();
                trsList.add(tr);
            }
            for (Thread thread : trsList)
                thread.join();
            raf.close();
            return true;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("Hubo algun error");
        return false;
    }


}
