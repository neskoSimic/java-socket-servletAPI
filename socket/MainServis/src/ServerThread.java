import Controller.RequestHandler;
import request.HttpMethod;
import request.Request;
import response.Response;

import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;

public class ServerThread implements Runnable{

        private  Socket socket;
        private  BufferedReader in;
        private  PrintWriter out;

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
                String requestline=in.readLine();
                StringTokenizer st=new StringTokenizer(requestline);

                String method=st.nextToken();
                String path=st.nextToken();

                String line;
                int contentLength = 0;


                while ((line = in.readLine()) != null && !line.isEmpty()) {
                    if (line.startsWith("Content-Length:")) {
                        contentLength = Integer.parseInt(line.split(":")[1].trim());
                    }
                }

                String body = "";
                if (contentLength > 0) {
                    char[] bodyChars = new char[contentLength];
                    in.read(bodyChars, 0, contentLength);
                    body = new String(bodyChars);
                }


                Request request = new Request(HttpMethod.valueOf(method), path, body);
                RequestHandler requestHandler = new RequestHandler();
                Response response=requestHandler.handle(request);


                out.println(response.getResponseString());

                in.close();
                out.close();
                socket.close();

            }catch (Exception e){
                e.printStackTrace();
            }
        }


}
