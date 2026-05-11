package Controller;

public class HtmlBuild {

    public static String build(QuoteModel quoteModel) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html lang='en'>");
        html.append("<head>");
        html.append("<link rel=\"icon\" href=\"data:,\">");
        html.append("<meta charset='UTF-8'>");
        html.append("<title>Quotes</title>");
        html.append("</head>");
        html.append("<body>");

        html.append("<p>Quotes list</p>");
        html.append("<form method='POST' action='/save-quote'>");
        html.append("<label>Quote:</label><br>");
        html.append("<input type='text' name='text'><br><br>");


        html.append("<label>Author:</label><br>");
        html.append("<input type='text' name='author'><br><br>");

        html.append("<button type='submit'>Save Quote</button>");
        html.append("</form>");


        html.append("<h2>Quote of the day</h2>");
        html.append("<p>");
        html.append(quoteModel.getQuote()).append(" - ").append(quoteModel.getAuthor());
        html.append("</p>");

        html.append("<h2>Saved quotes</h2>");
        if (!QuoteStorage.quotes.isEmpty()) {
            for (QuoteModel q : QuoteStorage.quotes) {
                html.append("<div>");
                html.append("<p>" + q.getQuote() + "</p>");
                html.append("<p>" + q.getAuthor() + "</p>");
                html.append("</div>");
            }
        }else {
            html.append("<p>No quotes yet.</p>");
        }

        html.append("</body>");
        html.append("</html>");



        return html.toString();
    }
}
