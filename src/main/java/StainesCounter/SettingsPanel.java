package StainesCounter;

import javax.security.auth.login.Configuration;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class SettingsPanel extends JPanel {
    private int themeFileOption; //Option 1 is load from chosen file, Option 2 is load from preset

    private String chosenFilePath;
    private JLabel importLabel;
    private JLabel presetLabel;

    private final String initialImportLabelText = "Import theme (.JSON): ";
    private final String initialPresetLabelText = "Chosen preset: ";

    private final Font SETTINGS_FONT = new Font("Segoe UI", Font.BOLD, 20);

    private final Color BUTTON_COLOR_FOREGROUND;
    private final Color BUTTON_COLOR_BACKGROUND;
    private final Color TEXT_COLOR_FOREGROUND;
    private final Color BACKGROUND_COLOR;

    public SettingsPanel(Theme theme) {
        setLayout(new GridLayout(1, 2));
        setBorder(new EmptyBorder(5, 5, 5, 5));

        this.BUTTON_COLOR_BACKGROUND = theme.getButtonBackground();
        this.BUTTON_COLOR_FOREGROUND = theme.getButtonForeground();
        this.TEXT_COLOR_FOREGROUND = theme.getTextAreaForeground();
        this.BACKGROUND_COLOR = theme.getBackground();

        init();
    }

    public void init() {
        /*
            Panels
         */

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(BACKGROUND_COLOR);
        leftPanel.setBorder(new MatteBorder(0, 0, 0, 1, TEXT_COLOR_FOREGROUND));

        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(BACKGROUND_COLOR);
        rightPanel.setBorder(new MatteBorder(0, 1, 0, 0, TEXT_COLOR_FOREGROUND));

        JPanel rightTopPanel = new JPanel();
        rightTopPanel.setBackground(BACKGROUND_COLOR);

        JPanel rightBottomPanel = new JPanel();
        rightBottomPanel.setBackground(BACKGROUND_COLOR);

        /*
            GridBagLayout
         */

        GridBagConstraints gbcLabel = new GridBagConstraints();
        gbcLabel.gridx = 0;
        gbcLabel.gridy = 0;

        GridBagConstraints gbcOther = new GridBagConstraints();
        gbcOther.gridx = 0;
        gbcOther.gridy = 1;

        /*
            Top right panel
         */

        importLabel = new JLabel(initialImportLabelText);
        importLabel.setForeground(TEXT_COLOR_FOREGROUND);
        importLabel.setBackground(BACKGROUND_COLOR);
        importLabel.setFont(SETTINGS_FONT);
        importLabel.setPreferredSize(new Dimension(340, 60));

        CustomButton chooseButton = new CustomButton("Open File",
                BUTTON_COLOR_BACKGROUND, BUTTON_COLOR_FOREGROUND,
                new Dimension(340, 60), SETTINGS_FONT, e -> chooseFile());

        rightTopPanel.setLayout(new GridBagLayout());
        rightTopPanel.add(importLabel, gbcLabel);
        rightTopPanel.add(chooseButton, gbcOther);

        /*
            Bottom right panel
         */

        presetLabel = new JLabel(initialPresetLabelText);
        presetLabel.setForeground(TEXT_COLOR_FOREGROUND);
        presetLabel.setBackground(BACKGROUND_COLOR);
        presetLabel.setFont(SETTINGS_FONT);
        presetLabel.setPreferredSize(new Dimension(340, 60));

        Map<String, String> map = new LinkedHashMap<String, String>() {{
            put("Default Light Mode", "presets/preset_1.json");
            put("Dark Mode", "presets/preset_2.json");
            put("Nathan Mode", "presets/preset_3.json");
        }};

        Set<String> set = new LinkedHashSet<>(map.keySet());
        String[] options = set.toArray(new String[0]);

        JComboBox<String> comboBox = new JComboBox<>(options);
        comboBox.setSelectedIndex(0);
        comboBox.setBackground(BUTTON_COLOR_BACKGROUND);
        comboBox.setForeground(BUTTON_COLOR_FOREGROUND);
        comboBox.setPreferredSize(new Dimension(340, 60));
        comboBox.setFont(SETTINGS_FONT);

        //Workaround for JComboBox.setBorderPainted(false)
        for (int i = 0; i < comboBox.getComponentCount(); i++) {
            if (comboBox.getComponent(i) instanceof JComponent) {
                ((JComponent) comboBox.getComponent(i)).setBorder(null);
            }
        }

        comboBox.addActionListener(e -> {
            String selected = (String) comboBox.getSelectedItem();
            String filePath = map.get(selected);

            themeFileOption = 2;
            presetLabel.setText(replaceTextAfterColonSpace(presetLabel.getText(), selected));
            importLabel.setText(initialImportLabelText);
            chosenFilePath = filePath;
        });

        rightBottomPanel.setLayout(new GridBagLayout());
        rightBottomPanel.add(presetLabel, gbcLabel);
        rightBottomPanel.add(comboBox, gbcOther);

        /*
            Adding panels to each other
         */

        rightPanel.setLayout(new GridLayout(2, 0));
        rightPanel.add(rightTopPanel);
        rightPanel.add(rightBottomPanel);

        JLabel label = new JLabel("Coming Soon");
        label.setForeground(TEXT_COLOR_FOREGROUND);
        label.setFont(SETTINGS_FONT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;

        leftPanel.setLayout(new GridBagLayout());
        leftPanel.add(label, gbc);

        add(leftPanel);
        add(rightPanel);
    }

    public void chooseFile() {
        themeFileOption = 1;
        LookAndFeel previousLF = UIManager.getLookAndFeel();
        final JFileChooser fileChooser;

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            fileChooser = new JFileChooser(new File(System.getProperty("user.home") + "/Desktop"));
            fileChooser.setFileFilter(new JSONFilter());

            int returnVal = fileChooser.showOpenDialog(new JOptionPane());

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();

                ConfigurationLoader cl = new ConfigurationLoader();

                if (cl.isValidTheme(file.getAbsolutePath())) {
                    importLabel.setText(replaceTextAfterColonSpace(importLabel.getText(), file.getName()));
                    presetLabel.setText(initialPresetLabelText);
                    chosenFilePath = file.getAbsolutePath();
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Selected file invalid, please select a different file", "Error - Selected file invalid",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

            UIManager.setLookAndFeel(previousLF);
        } catch (Exception e) {
            System.err.println("This error should never occur");
        }
    }

    public String getChosenFilePath() {
        return chosenFilePath;
    }

    public String replaceTextAfterColonSpace(String initialString, String newString) {
        int index = initialString.lastIndexOf(':');

        String tempString = initialString.substring(0, index + 2);
        return tempString.concat(newString);
    }

    public boolean setThemeFromJar(String filePath) {
        final String myDocumentsPath = new JFileChooser().getFileSystemView().getDefaultDirectory().toString();
        InputStream is = SettingsPanel.class.getClassLoader().getResourceAsStream(filePath);

        File file = new File(myDocumentsPath + "/Staines Counter/theme.json");

        try {
            Files.copy(is, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean setThemeFromFile(String filePath) {
        final String myDocumentsPath = new JFileChooser().getFileSystemView().getDefaultDirectory().toString();

        File file = new File(filePath);
        File destinationFile = new File(myDocumentsPath + "/Staines Counter/theme.json");

        try {
            Files.copy(file.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public int getOption() {
        return themeFileOption;
    }
}