package StainesCounter;

import java.awt.Color;

public class Theme {
    private final Color buttonBackground;
    private final Color buttonForeground;
    private final Color textAreaBackground;
    private final Color textAreaForeground;
    private final Color background;

    public Theme(Color buttonBack, Color buttonFore, Color textAreaBack, Color textAreaFore, Color background) {
        this.buttonBackground = buttonBack;
        this.buttonForeground = buttonFore;
        this.textAreaBackground = textAreaBack;
        this.textAreaForeground = textAreaFore;
        this.background = background;
    }

    public Color getButtonBackground() {
        return buttonBackground;
    }

    public Color getButtonForeground() {
        return buttonForeground;
    }

    public Color getTextAreaBackground() {
        return textAreaBackground;
    }

    public Color getTextAreaForeground() {
        return textAreaForeground;
    }

    public Color getBackground() {
        return background;
    }
}
