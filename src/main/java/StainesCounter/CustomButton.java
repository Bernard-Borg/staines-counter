package StainesCounter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomButton extends JButton {
    private Color bgColor;
    private Color fgColor;

    public CustomButton(String text, Color bgColor, Color fgColor, Dimension preferredSize, Font font, ActionListener actionListener) {
        super(text);
        setBackground(bgColor);
        setForeground(fgColor);
        setPreferredSize(preferredSize);
        setFont(font);
        setBorderPainted(false);
        addActionListener(actionListener);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setForeground(bgColor);
                setBackground(fgColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(bgColor);
                setForeground(fgColor);
            }
        });
    }
}
