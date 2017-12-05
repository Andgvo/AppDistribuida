package test;

import Frames.Menu;
import RMI.ServidorRMI;
import SocketsMulticast.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import sources.*;

public class Main extends JFrame implements ActionListener{
    private JButton jBtnInicio;
    private JTextField jTxtUser;
    private JLabel jLblNick;
    
    public Main() {
        configFrame();
        initComponents();
    }
    
    public void configFrame(){
        this.setTitle("Inicio");                                //Titulo de la Ventana   
        this.setSize(400,100);                                  //Tamaño de la Ventana
        this.setResizable(false);
        this.setLocationRelativeTo(null);                       //Centrar la Ventana
        this.setLayout(null);                                   //No se usa ningun layout, solo asi se podran dar posiciones a los componentes                       
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    //Al cerrar la ventana se cerrara todo el proceso
        this.getContentPane().setBackground(new Color(204, 229, 255));
    }
    
    public void initComponents(){
        jLblNick = new JLabel("Nick: ");
        jLblNick.setFont(new Font("Tahoma", 0, 20));
        jLblNick.setBounds(10,25,100,20);
        add(jLblNick);
        jTxtUser = new JTextField();
        jTxtUser.setBounds(65,20,200,30);
        //Desactivar Barra Espaciadora
        jTxtUser.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e){
                char caracter = e.getKeyChar();
                if(caracter == KeyEvent.VK_SPACE){
                    e.consume();
                }else if (caracter == KeyEvent.VK_ENTER){
                    try {
                        iniciarSesion();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        add(jTxtUser);
        
        jBtnInicio = new JButton("Conectar");
        jBtnInicio.setBounds(280, 20, 100, 28);
        jBtnInicio.addActionListener(this);
        add(jBtnInicio);
    }
    
        @Override
    public void actionPerformed(ActionEvent e) {
        try {
            iniciarSesion();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    
    public void iniciarSesion() throws IOException{
        if(jTxtUser.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Introduce un Nick Válido.");
        }else{
            String nick = jTxtUser.getText();
            iniciarServicios(nick);
        }
    }

    public void iniciarServicios(String nick) throws IOException{
        Usuarios usersList = new Usuarios();
        int port = 8000;
        while(true){
            try {
                ServerSocket ss = new ServerSocket(port);
                System.out.println("SE conecto con puerto: " + port);
                break;
            } catch(Exception ex){
                port++;
            }
        }
        
        ClienteMulticast cm = new ClienteMulticast(nick, usersList);
        ServidorMulticast sm = new ServidorMulticast(nick, "localhost", port);
        

        
        Thread trSM = new Thread(sm);
        Thread trCM = new Thread(cm);
        trSM.start();
        trCM.start();

        //Iniciar Servidor RMI
        ServidorRMI.iniciarServidorRMI(nick);
        
        Menu menu = new Menu();
        menu.setVisible(true);
        dispose();
        
        System.out.println("Usuarios activos:\n");
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
        }
    }
    
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Main main = new Main();
                main.setVisible(true);
            }
        });
    }
}
