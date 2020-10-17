package StainesCounter;

import org.json.JSONException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class Main {
    private static final String version = "0.8 [BETA]";
    private static final String iconImagePath = "src/main/resources/logo.png";

    public static void main(String[] args) {
        ConfigurationLoader cl = new ConfigurationLoader();
        Theme tempTheme = null;
        List<Phrase> phrases = null;

        try {
            tempTheme = cl.loadTheme();
            phrases = cl.loadButtons();
        } catch (JSONException e) {
            JOptionPane.showMessageDialog(null, "Phrases file invalid/corrupt",
                    "Error - Invalid phrases configuration file", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        } catch (LoadingException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        MainPanel mPanel = new MainPanel(tempTheme, phrases);
        mPanel.setBackground(tempTheme.getBackground());

        JFrame frame = new JFrame("Staines Counter v" + version);
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(iconImagePath));
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setSize(new Dimension(1000, 900));
        frame.setLocationRelativeTo(null);
        frame.setFocusable(true);
        frame.setContentPane(mPanel);
        frame.setVisible(true);
        frame.requestFocus();

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                if (!mPanel.isSaved()) {
                    int choice = JOptionPane.showConfirmDialog(null, "Do you want to save?",
                            "Save", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                    if (choice == JOptionPane.YES_OPTION) {
                        mPanel.saveAction();
                        System.exit(0);
                    } else if (choice == JOptionPane.NO_OPTION) {
                        System.exit(0);
                    }
                } else {
                    System.exit(0);
                }
            }
        });
    }
}