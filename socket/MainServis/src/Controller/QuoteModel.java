package Controller;

public class QuoteModel {
    private String quote;
    private String author;


    public QuoteModel(String quote,String author) {
        this.quote = quote;
        this.author = author;
    }
    public String getQuote() {
        return quote;
    }
    public String getAuthor() {
        return author;
    }
}
