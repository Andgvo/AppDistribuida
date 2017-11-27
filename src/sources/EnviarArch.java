package sources;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import javax.swing.JFileChooser;

public class EnviarArch extends Thread{
    
    public void enviar(String path){
        
    }
    
    public static void main(String[] args) {
        try{
            //SELECCION DEL ARCHIVO
            //Opcion jf.MultiSelectionEnabled(true); para multiples archivos
            JFileChooser jf = new JFileChooser();
            int r = jf.showOpenDialog(null);
            if (r == JFileChooser.APPROVE_OPTION) {
                File f = jf.getSelectedFile();
                
                //COMPROBAR QUE NO SEA DIRECTORIO
                if (!f.isDirectory()) {
                    String host = "127.0.0.1"; 
                    int pto = 9876;
                    Socket cl = new Socket(host, pto); //CREANDO SOCKET
                    String nombre = f.getName(); 
                    String ruta = f.getAbsolutePath();
                    long tam = f.length(); //OBTENIENDO TAMAÑO DEL ARCHVIO

                    System.out.println("Inicia Transferenia del archivo" + ruta + "\n");
                    int porcentaje = 0,n;
                    long enviados = 0;
                    
                    //SE CREAN LOS FLUJOS DE ENTRADA Y SALIDA
                    DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
                    DataInputStream dis = new DataInputStream(new FileInputStream(ruta));
                    
                    //MANDANDO NOMBRE Y TAMAÑO DEL ARCHIVO
                    dos.writeUTF(nombre);
                    dos.flush();
                    dos.writeLong(tam);
                    dos.flush();
                    //ENVIANDO DATOS EN PARTES DE 1500 BYTES
                    while (enviados < tam) {                        
                        byte []b = new byte[1500];
                        //LEER CUANTOS BYTES SE ENVIARAN
                        n = dis.read(b);
                        //ENVIAR DATOS LEIDOS ANTERIORMENTE
                        dos.write(b,0,n);
                        dos.flush();
                        enviados += n;
                        porcentaje = (int)((enviados*100)/tam);
                        System.out.println("\rEnviados: " + porcentaje + "%");
                    }
                    System.out.println("Archivo Enviado...");
                    
                    dos.close();
                    dis.close();
                    cl.close();
                } else{
                    System.out.println("No Se Aceptan Directorios");
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
