package sources;

import java.io.Serializable;

public class Usuario implements Serializable{
    private String nameServer;
    private String ipDir;
    private int pto;
    private int temp;

    public Usuario(String nameServer, String ipDir, int pto) {
        this.nameServer = nameServer;
        this.ipDir = ipDir;
        this.pto = pto;
        this.temp = 0;
    }

    public String getNameServer() {
        return nameServer;
    }

    public String getIpDir() {
        return ipDir;
    }

    public int getPto() {
        return pto;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    @Override
    public String toString() {
        return "Usuario{" + "nameServer=" + nameServer + ", ipDir=" + ipDir + ", pto=" + pto + ", temp=" + temp + '}';
    }
}
