package StainesCounter;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class MainPanel extends JPanel {
    private boolean saved = true;
    private ArrayDeque<String> phraseStack;
    private final LocalDateTime dateTime = LocalDateTime.now();

    private final Theme theme;
    private final Color BUTTON_COLOR_FOREGROUND;
    private final Color BUTTON_COLOR_BACKGROUND;
    private final Color TEXT_COLOR_FOREGROUND;
    private final Color TEXT_COLOR_BACKGROUND;
    private final Color BACKGROUND_COLOR;

    private Font FONT_AWESOME;
    private final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 18);
    private final Dimension PREFERRED_BUTTON_SIZE = new Dimension(140, 40);

    private JTextArea textArea;
    private final List<Phrase> phrases;
    private List<CustomPhraseButton> buttons;

    public MainPanel(Theme theme, List<Phrase> list) {
        try {
            FONT_AWESOME = Font.createFont(Font.TRUETYPE_FONT,
                    new File("src/main/resources/FontAwesome.otf"));

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(FONT_AWESOME);
        } catch (FontFormatException e) {
            JOptionPane.showMessageDialog(null, "Font failure");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Loading font failed, application might not look as intended",
                    "Error - Font loading", JOptionPane.ERROR_MESSAGE);
        }

        this.BUTTON_COLOR_BACKGROUND = theme.getButtonBackground();
        this.BUTTON_COLOR_FOREGROUND = theme.getButtonForeground();
        this.TEXT_COLOR_BACKGROUND = theme.getTextAreaBackground();
        this.TEXT_COLOR_FOREGROUND = theme.getTextAreaForeground();
        this.BACKGROUND_COLOR = theme.getBackground();
        this.theme = theme;
        this.phrases = list;

        setLayout(new BorderLayout(0, 0));
        setBorder(new EmptyBorder(5, 5, 5, 5));

        init();
    }

    public void init() {
        /*
            Text Area
         */

        textArea = new JTextArea();
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        textArea.setRows(20);
        textArea.setWrapStyleWord(true);
        textArea.setForeground(TEXT_COLOR_FOREGROUND);
        textArea.setBackground(TEXT_COLOR_BACKGROUND);
        textArea.setEditable(false);
        textArea.setLineWrap(true);

        JScrollPane jsp = new JScrollPane(textArea);
        
        /*
            Buttons
         */

        buttons = new ArrayList<>();
        phraseStack = new ArrayDeque<>();

        for (Phrase p : phrases) {
            CustomPhraseButton temp = new CustomPhraseButton(p, BUTTON_COLOR_BACKGROUND, BUTTON_COLOR_FOREGROUND,
                    PREFERRED_BUTTON_SIZE, BUTTON_FONT, null);
            temp.addActionListener(new CustomActionListener(this, temp, textArea, phraseStack));
            buttons.add(temp);
        }

        CustomButton copyButton = new CustomButton("\uf0c5",
                BUTTON_COLOR_BACKGROUND, BUTTON_COLOR_FOREGROUND,
                new Dimension(50, (int) PREFERRED_BUTTON_SIZE.getHeight()),
                FONT_AWESOME.deriveFont(26f), e -> copyAction());

        CustomButton settingsButton = new CustomButton("\uf013",
                BUTTON_COLOR_BACKGROUND, BUTTON_COLOR_FOREGROUND,
                new Dimension(50, (int) PREFERRED_BUTTON_SIZE.getHeight()),
                FONT_AWESOME.deriveFont(26f), e -> settingsAction());

        CustomButton saveButton = new CustomButton("\uf0c7",
                BUTTON_COLOR_BACKGROUND, BUTTON_COLOR_FOREGROUND,
                new Dimension(50, (int) PREFERRED_BUTTON_SIZE.getHeight()),
                FONT_AWESOME.deriveFont(26f), e -> saveAction());

        CustomButton undoButton = new CustomButton("\uf0e2",
                BUTTON_COLOR_BACKGROUND, BUTTON_COLOR_FOREGROUND,
                new Dimension(50, (int) PREFERRED_BUTTON_SIZE.getHeight()),
                FONT_AWESOME.deriveFont(26f), e -> undoAction());

        CustomButton reportButton = new CustomButton("Get Report",
                BUTTON_COLOR_BACKGROUND, BUTTON_COLOR_FOREGROUND,
                PREFERRED_BUTTON_SIZE, BUTTON_FONT, e -> reportAction());

        /*
            Panels
         */

        JPanel topPanel = new JPanel();
        topPanel.setBackground(BACKGROUND_COLOR);
        topPanel.setLayout(new GridLayout(0, 3, 5, 5));

        for (CustomPhraseButton b : buttons) {
            topPanel.add(b);
        }

        JPanel middlePanel = new JPanel();
        middlePanel.setBorder(new CompoundBorder(new EmptyBorder(10, 10, 10, 10),
                new LineBorder(BACKGROUND_COLOR)));
        middlePanel.setBackground(BACKGROUND_COLOR);
        middlePanel.setLayout(new CardLayout(10, 10));
        middlePanel.add(jsp);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(BACKGROUND_COLOR);
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 0));
        bottomPanel.setPreferredSize(new Dimension(600, 80));
        bottomPanel.add(copyButton);
        bottomPanel.add(saveButton);
        bottomPanel.add(settingsButton);
        bottomPanel.add(undoButton);
        bottomPanel.add(reportButton);

        add(topPanel, BorderLayout.NORTH);
        add(middlePanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    public void reportAction() {
        StringBuilder sb = new StringBuilder();

        for (CustomPhraseButton b : buttons) {
            String buttonName = b.getText();
            String temp = buttonName.substring(0, 1);
            temp = temp.toUpperCase();

            String finalString = temp + buttonName.substring(1) + "(s): " + b.getCounter() + "\n";
            sb.append(finalString);
        }

        JOptionPane.showMessageDialog(null, sb, "Staines Counter Report", JOptionPane.PLAIN_MESSAGE);
    }

    public void saveAction() {
        try {
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy HH-mm-ss");

            File file = new File("reports");

            if (!file.exists()) {
                file.mkdir();
            }

            BufferedWriter bw = new BufferedWriter(
                new FileWriter("reports/" + dateFormat.format(dateTime) + ".txt")
            );

            bw.write(dateFormat.format(dateTime));
            bw.newLine();

            for (CustomPhraseButton b : buttons) {
                String buttonName = b.getText();
                String temp = buttonName.substring(0, 1);
                temp = temp.toUpperCase();

                bw.write(String.format("%s%s(s): %d\n", temp, buttonName.substring(1),
                        b.getCounter()));
            }

            saved = true;
            bw.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void settingsAction() {
        SettingsPanel panel = new SettingsPanel(theme);

        JFrame settingsFrame = new JFrame();
        settingsFrame.setContentPane(panel);
        settingsFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        settingsFrame.setBackground(Color.BLACK);
        settingsFrame.setLocationRelativeTo(null);
        settingsFrame.setFocusable(true);
        settingsFrame.setSize(new Dimension(1000, 900));
        settingsFrame.setVisible(true);
        settingsFrame.requestFocus();
        settingsFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (panel.getChosenFilePath() != null) {
                    if (panel.setTheme(panel.getChosenFilePath())) {
                        JOptionPane.showMessageDialog(null,
                                "Theme changed successfully, restart for changes to take place",
                                "Theme changed", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Theme change failed",
                                "Error - Theme was not changed", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    public void copyAction() {
        Toolkit.getDefaultToolkit()
                .getSystemClipboard()
                .setContents(new StringSelection(textArea.getText()), null);
    }

    public void undoAction() {
        if (!phraseStack.isEmpty()) {
            String toRemove = phraseStack.pop();
            String text = textArea.getText();

            int temp = text.lastIndexOf(toRemove);
            String newText = text.substring(0, temp);

            textArea.setText(newText);

            for (CustomPhraseButton b : buttons) {
                if (b.getPhrase().equals(toRemove)) {
                    b.decrement();
                    break;
                }
            }
        }
    }
}