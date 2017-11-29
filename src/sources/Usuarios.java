package sources;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class Usuarios extends ArrayList<Usuario>{
    private ReentrantLock rl;

    public Usuarios() {
        rl = new ReentrantLock();
    }
    
    public void lockList() {
        rl.lock();
        //System.out.println("\033[34mBloqueada la lista");
    }
    
    public void unlockList(){
        rl.unlock();
        //System.out.println("\033[34mLiberada la lista");
    }
}
