package Controller;

import java.io.*;
import java.net.Socket;

public class Communication {

    public static String fetchJson() {
        try {
            Socket socket = new Socket("localhost", 8081);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

            String request =
                    "GET /quoteod HTTP/1.1\r\n" +
                            "Host: localhost:8081\r\n" +
                            "\r\n";

            out.println(request);

            String line;
            StringBuilder response = new StringBuilder();

            while ((line = in.readLine()) != null) {
                response.append(line).append("\n");
            }
            in.close();
            out.close();
            socket.close();


            return response.substring(response.indexOf("{"));

        } catch (Exception e) {
            e.printStackTrace();
            return "greska";
        }
    }
}
