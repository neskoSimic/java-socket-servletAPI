package rs.raf.demo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@WebServlet(name = "orderedDishes", value = "/ordered-dishes-servlet")
public class OrderedDishesServlet extends HttpServlet {

    public OrderedDishesServlet() {}

    public void init(){}

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        String enteredPassword = req.getParameter("lozinka");
        String realPassword = (String) getServletContext().getAttribute("password");
        List<Map<String, String>> allOrders =
                (List<Map<String, String>>) getServletContext().getAttribute("allOrders");

        if (enteredPassword == null || !enteredPassword.equals(realPassword)) {
            resp.setContentType("text/html;charset=UTF-8");
            resp.getWriter().println("<h1>Neispravna lozinka</h1>");
            return;
        }

        Map<String, Map<String, Integer>> summary = new LinkedHashMap<>();

        for (Map<String, String> order : allOrders) {
            for (Map.Entry<String, String> entry : order.entrySet()) {
                String day = entry.getKey();
                String dish = entry.getValue();

                summary.putIfAbsent(day, new LinkedHashMap<>());
                Map<String, Integer> dayMap = summary.get(day);

                dayMap.put(dish, dayMap.getOrDefault(dish, 0) + 1);
            }
        }

        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        out.println("<html><body>");
        out.println("<h1>Pregled svih porudzbina</h1>");

        if (allOrders == null || allOrders.isEmpty()) {
            out.println("<p>Nema nijedne porudzbine.</p>");
        } else {
            for (String day : summary.keySet()) {
                out.println("<h2>" + day + "</h2>");
                out.println("<table border='1'>");
                out.println("<tr><th>Jelo</th><th>Kolicina</th></tr>");

                Map<String, Integer> dayMap = summary.get(day);
                for (Map.Entry<String, Integer> e : dayMap.entrySet()) {
                    out.println("<tr>");
                    out.println("<td>" + e.getKey() + "</td>");
                    out.println("<td>" + e.getValue() + "</td>");
                    out.println("</tr>");
                }

                out.println("</table><br>");
            }
        }

        out.println("<form method='post' action='" + req.getContextPath() + "/ordered-dishes-servlet?lozinka=" + enteredPassword + "'>");
        out.println("<button type='submit'>Obrisi sve porudzbine</button>");
        out.println("</form>");
        out.println("</body></html>");


    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String enteredPassword = req.getParameter("lozinka");
        String realPassword = (String) getServletContext().getAttribute("password");

        if (enteredPassword == null || !enteredPassword.equals(realPassword)) {
            resp.setContentType("text/html;charset=UTF-8");
            resp.getWriter().println("<h1>Neispravna lozinka</h1>");
            return;
        }

        List<Map<String, String>> allOrders =
                (List<Map<String, String>>) getServletContext().getAttribute("allOrders");

        synchronized (allOrders) {
            allOrders.clear();
        }
        Integer currentVersion = (Integer) getServletContext().getAttribute("appVersion");
        if (currentVersion == null) {
            currentVersion = 1;
        }
        getServletContext().setAttribute("appVersion", currentVersion + 1);

        resp.sendRedirect(req.getContextPath() + "/ordered-dishes-servlet?lozinka=" + enteredPassword);
        //resp.sendRedirect(req.getContextPath() + "/ordered-dishes-servlet");
    }

    public void destroy(){}




}
