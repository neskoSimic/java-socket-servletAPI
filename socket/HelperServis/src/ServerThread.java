import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;

public class ServerThread implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public ServerThread(Socket socket){
        this.socket=socket;

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        try {

        String requestLine= in.readLine();
        StringTokenizer tokenizer = new StringTokenizer(requestLine);

        String method=tokenizer.nextToken();
        String path=tokenizer.nextToken();

        if (method.equals(HttpMethod.GET.toString()) && path.equals("/quoteod")) {


            Gson gson = new Gson();
            QuoteModel quote = QuoteBase.getRandomQuote();
            String body = gson.toJson(quote);

            String header =
                    "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: application/json; charset=UTF-8\r\n" +
                            "Content-Length: " + body.length() + "\r\n" +
                            "\r\n";
            out.print(header + body);
            out.flush();
            in.close();
            out.close();
            socket.close();


        }



        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
