import java.util.List;
import java.util.Random;

public class QuoteBase {
    private static final List<QuoteModel> quotes = List.of(
            new QuoteModel("You have to believe in yourself when no one else does.", "Serena Wiliams"),
            new QuoteModel("When you have a dream, you’ve got to grab it and never let go.", "Francis Bacon"),
            new QuoteModel("Spread love everywhere you go. Let no one ever come without leaving happier.", "Mother Teresa")
    );

    private static final Random random = new Random();

    public static QuoteModel getRandomQuote() {
        return quotes.get(random.nextInt(quotes.size()));
    }
}