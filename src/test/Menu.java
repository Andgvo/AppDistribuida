/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import RMI.ClienteRMI;
import RMI.ServidorRMI;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import sources.Usuario;
import sources.Usuarios;

/**
 *
 * @author xXEdG
 */
public class Menu extends JFrame implements ActionListener{
    private Usuarios usersList;
    private Usuario myUser;
    private JTable jTblUsers, jTblFiles;
    private JScrollPane jScrUsers, jScrFiles;
    private DefaultTableModel jDtmUsers, jDtmFiles;
    private JTextField jTxtArchivo;
    private JLabel jLblArchivo;
    private JButton jBtnBuscar, jBtnDescargar;
    
    public Menu(Usuario myUser ,Usuarios usersList) {
        this.usersList = usersList;
        this.myUser = myUser;
        configFrame();
        initComponents();
    }
    
    private void configFrame(){
        this.setTitle("Peer To Peer : " + myUser.getNickName());//Titulo de la Ventana   
        this.setSize(720,550);                                  //Tamaño de la Ventana
        this.setResizable(false);
        this.setLocationRelativeTo(null);                       //Centrar la Ventana
        this.setLayout(null);                                   //No se usa ningun layout, solo asi se podran dar posiciones a los componentes                       
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    //Al cerrar la ventana se cerrara todo el proceso
        this.getContentPane().setBackground(new Color(204, 229, 255));
    }
    
    private void initComponents(){
        //Propiedades Area de Texto Chat
        //jTxPChat = new JTextPane();
        //jTxPChat.setEditable(false);
        //jTxPChat.setContentType("text/html");
        //jScrChat = new JScrollPane(jTxPChat);
        //jScrChat.setBounds(10, 10, 545, 340);
        //jScrChat.setBorder(BorderFactory.createLineBorder(Color.black));
        //add(jScrChat);
        jTxtArchivo = new JTextField();
        jTxtArchivo.setBounds(10, 30, 420, 28);
        add(jTxtArchivo);
        
        //Propiedades Botones
        jBtnBuscar = new JButton("Buscar");
        jBtnBuscar.setBounds(435, 30, 120, 28);
        jBtnBuscar.addActionListener(this);
        add(jBtnBuscar);
        
        jBtnDescargar = new JButton("Descargar");
        jBtnDescargar.setBounds(435, 475, 120, 28);
        jBtnDescargar.addActionListener(this);
        add(jBtnDescargar);
        
        //Propiedades Etiquetas
        jLblArchivo = new JLabel("Introdusca el nombre del archivo por buscar.");
        jLblArchivo.setBounds(10,10, 500,20);
        jLblArchivo.setFont(new Font("Tahoma", 0, 15));
        add(jLblArchivo);
        
        //Propiedades de Tabla de Usuarios
        jScrUsers = new JScrollPane();
        String[] colNames = {"Usuarios Conectados"};
        jDtmUsers = new DefaultTableModel();
        jDtmUsers.setColumnIdentifiers(colNames);
        jTblUsers = new JTable(jDtmUsers){
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        jTblUsers.getColumnModel().getColumn(0).setPreferredWidth(100);
        jScrUsers.setViewportView(jTblUsers);
        jScrUsers.setBounds(565, 10, 135, 495);
        jScrUsers.getViewport().setBackground(new Color(240, 255, 255));
        add(jScrUsers);
        
        //Propiedades de Tabla de Usuarios
        jScrFiles = new JScrollPane();
        String[] colNames2 = {"Archivos compartidos:"};
        jDtmFiles = new DefaultTableModel();
        jDtmFiles.setColumnIdentifiers(colNames2);
        jTblFiles = new JTable(jDtmFiles){
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        //jTblFiles.getColumnModel().getColumn(0).setPreferredWidth(100);
        jScrFiles.setViewportView(jTblFiles);
        jScrFiles.setBounds(10, 65, 545, 400);
        jScrFiles.getViewport().setBackground(new Color(240, 255, 255));
        add(jScrFiles);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == jBtnBuscar)
            buscarArchivos();
        if(e.getSource() == jBtnDescargar)
            descargarArchivo();
    }
    
    public void buscarArchivos(){
        if(jTxtArchivo.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Introduce el nombre del archivo.");
        }else{
            String fileName = jTxtArchivo.getText();
            //Buscar Coincidencias entre los usuarios conectados
            ArrayList<String> coincidencias = ClienteRMI.buscarCoincidencias(fileName, usersList);
            if(coincidencias.isEmpty()){
                JOptionPane.showMessageDialog(null, "No se encontraron coincidencias.");
            }else{
                //Actualizar Tabla
                jDtmFiles.setRowCount(0);
                for (String coincidencia : coincidencias) {
                    Object[] object = new Object[1];
                    object[0] = coincidencia;
                    jDtmFiles.addRow(object); 
                }
            }
        }
    }
    
    public void descargarArchivo(){
        int row;
        if((row = jTblFiles.getSelectedRow()) != -1){
            String fileName = (String)jTblFiles.getValueAt(row, 0);
            System.out.println("Archivo encontrado:\n" + fileName);
        }else
            JOptionPane.showMessageDialog(null, "Selecciona primero un archivo.");
    }
    
    public void actualizarTabla(){
        jDtmUsers.setRowCount(0);
        for (Usuario usuario : usersList) {
            Object[] object = new Object[1];
            object[0] = usuario.getNickName();
            jDtmUsers.addRow(object);
        }
    }
}
