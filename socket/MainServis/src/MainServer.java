import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServer {

    public static final int PORT=8080;

    public static void main(String[] args){


        try {
            ServerSocket ss= new ServerSocket(PORT);
            while(true){
                Socket socket=ss.accept();
                new Thread(new ServerThread(socket)).start();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
