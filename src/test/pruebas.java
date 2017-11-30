
package test;


public class pruebas {
    public static void main(String[] args) {
        long tam = 7546;
        int nPart=3, parte=2;
        long tamPart = tam/nPart;
        long finTam = tamPart*parte;
        long iniTam = tamPart*(parte-1);
        System.out.println("Inicio "+iniTam+" fin en "+finTam);
    }
}