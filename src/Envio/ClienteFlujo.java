/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Envio;

import RMI.ClienteRMI;
import java.util.ArrayList;
import sources.Usuario;
import sources.Usuarios;

/**
 *
 * @author xXEdG
 */
public class ClienteFlujo implements Runnable{

    public ClienteFlujo() {}
    
    @Override
    public void run() {}
    
    public static boolean downloadFile(String fileName, Usuarios usersList){
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
        
        System.out.println("Usuarios con el archivo "+fileName+", de tamaño "+tam+" y  checksum "+checksum+":");
        for (Usuario usuario : usuarios) {
            System.out.println(usuario.getNickName());
        }
        return true;
    }


}
