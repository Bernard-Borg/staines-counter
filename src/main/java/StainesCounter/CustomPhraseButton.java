package StainesCounter;

import java.awt.Font;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.event.ActionListener;

public class CustomPhraseButton extends CustomButton {
    private String phrase;
    private int counter;

    public CustomPhraseButton(Phrase phrase, Color bgColor, Color fgColor, Dimension preferredSize, Font font, ActionListener actionListener) {
        super(phrase.getText(), bgColor, fgColor, preferredSize, font, actionListener);
        this.phrase = phrase.getPhrase();
    }

    public String getPhrase() {
        return phrase;
    }

    public int getCounter() {
        return counter;
    }

    //Increases counter by 1
    public void increment() {
        counter++;
    }

    //Decreases counter by 1
    public void decrement() {
        counter--;
    }
}
