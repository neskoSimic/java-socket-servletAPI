package rs.raf.demo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

@WebServlet(name = "mainservlet", value = "/main-servlet")
public class MainServlet extends HttpServlet {

    private  String password= "";
    private int appVersion = 1;
    private Map<String, List<String>> menu= new LinkedHashMap<>();

    public MainServlet() {}

    public void init() throws ServletException {
        loadData("Ponedeljak", "/ponedeljak.txt");
        loadData("Utorak", "/utorak.txt");
        loadData("Srijeda", "/srijeda.txt");
        loadData("Cetvrtak", "/cetvrtak.txt");
        loadData("Petak", "/petak.txt");
        loadData("Subota", "/subota.txt");
        loadData("Nedelja", "/nedelja.txt");
        password =loadPassword("/password.txt");
        getServletContext().setAttribute("menu", menu);
        getServletContext().setAttribute("password", password);
        getServletContext().setAttribute("allOrders", new ArrayList<Map<String,String>>());
        getServletContext().setAttribute("appVersion", appVersion);

    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.service(req, resp);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Boolean ordered = (Boolean) session.getAttribute("ordered");
        Integer orderedVersion = (Integer) session.getAttribute("orderedVersion");
        Integer currentVersion = (Integer) getServletContext().getAttribute("appVersion");
        String succes= req.getParameter("success");

        if (Boolean.TRUE.equals(ordered) && orderedVersion != null && orderedVersion.equals(currentVersion)) {
            resp.setContentType("text/html;charset=UTF-8");
            PrintWriter out = resp.getWriter();

            if ("true".equals(succes)) {
                out.println("<h1>Uspjesno ste porucili.</h1>");
            } else {
                out.println("<h1>Vec ste napravili porudzbinu.</h1>");
            }

            Map<String, String> userOrder = (Map<String, String>) session.getAttribute("userOrder");
            if (userOrder != null) {
                out.println("<ul>");
                for (Map.Entry<String, String> entry : userOrder.entrySet()) {
                    out.println("<li>" + entry.getKey() + ": " + entry.getValue() + "</li>");
                }
                out.println("</ul>");
            }

            return;
        }

        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        out.println("<h1>Izaberi jela</h1>");
        out.println("<form method='POST' action='/main-servlet'>");

        for (String dan : menu.keySet()) {
            out.println("<label>" + dan + ":</label>");
            out.println("<select name='" + dan + "'>");

            out.println("<option value=''>-- Izaberi --</option>");

            for (String jelo : menu.get(dan)) {
                out.println("<option value= '" + jelo + "'>" + jelo + "</option>");
            }

            out.println("</select><br><br>");
        }

        out.println("<input type='submit' value='Potvrdi'/>");
        out.println("</form>");


    }
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer currentVersion = (Integer) getServletContext().getAttribute("appVersion");

        HttpSession session = req.getSession();
        Boolean ordered = (Boolean) session.getAttribute("ordered");
        Integer orderedVersion = (Integer) session.getAttribute("orderedVersion");

        if (Boolean.TRUE.equals(ordered) && orderedVersion != null && orderedVersion.equals(currentVersion)) {
            resp.setContentType("text/html;charset=UTF-8");
            resp.getWriter().println("Vec ste napravili porudzbinu.");
            return;
        }



        List<Map<String, String>> allOrders =
                (List<Map<String, String>>) getServletContext().getAttribute("allOrders");

        Map<String, String> order = new LinkedHashMap<>();


        for (String dan : menu.keySet()) {
            String value = req.getParameter(dan);

            if (value == null || value.isEmpty()) {
                resp.getWriter().println("Niste izabrali sva polja");
                resp.setStatus(403);
                return;
            }
            order.put(dan, value);
        }
        synchronized (allOrders) {
            allOrders.add(order);
        }
        session.setAttribute("ordered", true);
        session.setAttribute("orderedVersion", currentVersion);
        session.setAttribute("userOrder", order);

        resp.sendRedirect(req.getContextPath() + "/main-servlet?success=true");

    }

    public void destroy() {
    }

    private void  loadData(String dan, String path) {
        List<String> list = new ArrayList<>();

        try {
            InputStream is = getServletContext().getResourceAsStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String linija;
            while ((linija = br.readLine()) != null) {
                if (!linija.trim().isEmpty()) {
                    list.add(linija);
                }
            }

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        menu.put(dan, list);
    }
    private String loadPassword(String path){
        try {
            InputStream is = getServletContext().getResourceAsStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line= br.readLine();
            br.close();
            return line;
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }


}
