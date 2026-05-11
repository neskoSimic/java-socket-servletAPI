package Controller;

import com.google.gson.Gson;
import request.Request;
import response.HtmlResponse;
import response.RedirectResponse;
import response.Response;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class QuoteController extends Controller {

    public QuoteController(Request request) {
        super(request);
    }

    @Override
    public Response doGet() {
        String json = Communication.fetchJson();
        QuoteModel quoteModel = new Gson().fromJson(json, QuoteModel.class);
        String htmlStrana = HtmlBuild.build(quoteModel);
        return new HtmlResponse(htmlStrana);
    }

    @Override
    public Response doPost() {
        String body = request.getBody();

        String author = parseFormField(body, "author");
        String quote = parseFormField(body, "quote");
        QuoteStorage.quotes.add(new QuoteModel(author, quote));
        return new RedirectResponse("/quotes");
    }

    private static String parseFormField(String body, String key) {
        String[] pairs = body.split("&");

        for (String pair : pairs) {
            String[] parts = pair.split("=", 2);
            if (parts.length == 2) {
                String currentKey = URLDecoder.decode(parts[0], StandardCharsets.UTF_8);
                String currentValue = URLDecoder.decode(parts[1], StandardCharsets.UTF_8);

                if (currentKey.equals(key)) {
                    return currentValue;
                }
            }
        }

        return "";
    }
}
