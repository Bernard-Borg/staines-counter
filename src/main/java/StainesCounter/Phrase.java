package StainesCounter;

public class Phrase {
    private final String text;
    private final String phrase;

    public Phrase(String text, String phrase) {
        this.text = text;
        this.phrase = phrase;
    }

    public String getText() {
        return text;
    }

    public String getPhrase() {
        return phrase;
    }
}
