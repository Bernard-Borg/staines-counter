package StainesCounter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Deque;

public class CustomActionListener implements ActionListener {
    private final MainPanel location;
    private final CustomPhraseButton source;
    private final JTextArea textArea;
    private final Deque<String> stack;

    public CustomActionListener(MainPanel location, CustomPhraseButton source, JTextArea textArea, Deque<String> stack) {
        this.location = location;
        this.source = source;
        this.textArea = textArea;
        this.stack = stack;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        textArea.setText(textArea.getText() + source.getPhrase() + " ... ");
        source.increment();
        location.setSaved(false);
        stack.push(source.getPhrase());
    }
}
