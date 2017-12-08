package sources;

import java.io.Serializable;

public class Usuario implements Serializable{
    private String nickName;
    private String ipDir;
    private int pto;
    private int temp;

    public Usuario(String nickName, String ipDir, int pto) {
        this.nickName = nickName;
        this.ipDir = ipDir;
        this.pto = pto;
        this.temp = 0;
    }

    public String getNickName() {
        return nickName;
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
    
    public void setIpDir(String IpDir){
        this.ipDir = IpDir;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }
}
